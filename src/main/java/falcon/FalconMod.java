package falcon;

import com.mojang.brigadier.Command;
import falcon.api.FalconPlayer;
import falcon.checks.movement.HorizontalPredictionCheck;
import falcon.checks.movement.TimerCheck;
import falcon.debug.ReplayRecorder;
import falcon.fabric.FabricCollisionProvider;
import falcon.packet.MovementPacket;
import falcon.packet.PacketListener;
import falcon.physics.MovementSimulator;
import falcon.player.Exemption;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;
import falcon.violation.ViolationTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class FalconMod implements ModInitializer {
    public static final String MOD_ID = "falcon";
    private static FalconMod instance;

    private final Map<UUID, PlayerData> playerData = new ConcurrentHashMap<>();
    private final FabricCollisionProvider collisionProvider = new FabricCollisionProvider();
    private final MovementSimulator simulator = new MovementSimulator(collisionProvider);
    private final ReplayRecorder replayRecorder = new ReplayRecorder();
    private final ViolationTracker violationTracker = new ViolationTracker();
    private final PacketListener movementListener = new PacketListener(
            simulator,
            List.of(new TimerCheck(), new HorizontalPredictionCheck(simulator)),
            replayRecorder,
            violationTracker
    );

    private volatile boolean enabled = true;

    public FalconMod() {
        instance = this;
    }

    public static FalconMod instance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        registerCommands();
    }

    public void handleMovement(ServerPlayerEntity player, MovementPacket packet) {
        if (!enabled) {
            return;
        }

        PlayerData data = playerData.computeIfAbsent(player.getUuid(), ignored -> new PlayerData(120));
        collisionProvider.withPlayer(player, () -> {
            List<CheckResult> alerts = movementListener.onMovement(
                    new FalconPlayer(player.getUuid(), player.getGameProfile().getName()),
                    data,
                    packet
            );
            alerts.forEach(alert -> player.sendMessage(Text.literal("[Falcon] " + alert.checkName() + ": " + alert.message()), false));
        });
    }

    public Set<Exemption> exemptionsFor(ServerPlayerEntity player) {
        EnumSet<Exemption> exemptions = EnumSet.noneOf(Exemption.class);
        if (player.hasVehicle()) {
            exemptions.add(Exemption.VEHICLE_TRANSITION);
        }
        if (player.isFallFlying()) {
            exemptions.add(Exemption.ELYTRA_TRANSITION);
        }
        if (player.isSleeping() || player.isSwimming() || player.isUsingRiptide()) {
            exemptions.add(Exemption.BED_CRAWL_SWIM_RIPTIDE_TRIDENT);
        }
        return exemptions;
    }

    public boolean enabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int trackedPlayers() {
        return playerData.size();
    }

    private void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                CommandManager.literal("falcon")
                        .requires(source -> source.hasPermissionLevel(2))
                        .then(CommandManager.literal("status").executes(context -> {
                            context.getSource().sendFeedback(() -> Text.literal(
                                    "Falcon is " + (enabled ? "enabled" : "disabled") + "; tracking " + trackedPlayers() + " players."
                            ), false);
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(CommandManager.literal("on").executes(context -> {
                            setEnabled(true);
                            context.getSource().sendFeedback(() -> Text.literal("Falcon enabled."), true);
                            return Command.SINGLE_SUCCESS;
                        }))
                        .then(CommandManager.literal("off").executes(context -> {
                            setEnabled(false);
                            context.getSource().sendFeedback(() -> Text.literal("Falcon disabled."), true);
                            return Command.SINGLE_SUCCESS;
                        }))
        ));
    }
}
