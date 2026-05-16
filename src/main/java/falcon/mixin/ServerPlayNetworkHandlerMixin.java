package falcon.mixin;

import falcon.FalconMod;
import falcon.packet.MovementPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onPlayerMove", at = @At("TAIL"))
    private void falcon$afterPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
        FalconMod falcon = FalconMod.instance();
        if (falcon == null || player.getServer() == null) {
            return;
        }

        MovementPacket movementPacket = new MovementPacket(
                player.getServer().getTicks(),
                System.nanoTime(),
                packet.getX(player.getX()),
                packet.getY(player.getY()),
                packet.getZ(player.getZ()),
                packet.getYaw(player.getYaw()),
                packet.getPitch(player.getPitch()),
                packet.isOnGround(),
                falcon.exemptionsFor(player)
        );
        falcon.handleMovement(player, movementPacket);
    }
}
