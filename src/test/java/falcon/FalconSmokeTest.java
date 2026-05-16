package falcon;

import falcon.api.FalconPlayer;
import falcon.checks.movement.HorizontalPredictionCheck;
import falcon.checks.movement.TimerCheck;
import falcon.debug.ReplayRecorder;
import falcon.packet.MovementPacket;
import falcon.packet.PacketListener;
import falcon.physics.BlockBounds;
import falcon.physics.CollisionProvider;
import falcon.physics.MovementSimulator;
import falcon.physics.NearbyBlockSnapshot;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;
import falcon.violation.ViolationTracker;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class FalconSmokeTest {
    public static void main(String[] args) {
        CollisionProvider floor = new CollisionProvider() {
            private final BlockBounds ground = new BlockBounds(-16, -1, -16, 16, 0, 16);

            @Override
            public boolean hasCollision(BlockBounds bounds, falcon.player.MovementState previous) {
                return bounds.intersects(ground);
            }

            @Override
            public NearbyBlockSnapshot snapshotAround(falcon.player.MovementState state) {
                return new NearbyBlockSnapshot(state.serverTick(), List.of(ground), "smoke-test");
            }
        };

        MovementSimulator simulator = new MovementSimulator(floor);
        ReplayRecorder recorder = new ReplayRecorder();
        PacketListener listener = new PacketListener(
                simulator,
                List.of(new TimerCheck(), new HorizontalPredictionCheck(simulator)),
                recorder,
                new ViolationTracker()
        );

        FalconPlayer player = new FalconPlayer(UUID.randomUUID(), "TestPlayer");
        PlayerData data = new PlayerData(20);

        listener.onMovement(player, data, packet(1, 50_000_000L, 0.0));
        List<CheckResult> clean = listener.onMovement(player, data, packet(2, 100_000_000L, 0.1));
        if (!clean.isEmpty()) {
            throw new AssertionError("clean walk should not alert: " + clean);
        }

        List<CheckResult> fast = listener.onMovement(player, data, packet(3, 150_000_000L, 1.0));
        if (fast.stream().noneMatch(result -> result.checkName().equals("HorizontalPrediction"))) {
            throw new AssertionError("fast movement should alert: " + fast);
        }

        if (recorder.frames(player).size() != 3 || recorder.frames(player).get(2).nearbyBlockSnapshot().collidableBounds().isEmpty()) {
            throw new AssertionError("replay frames should include nearby block snapshots");
        }
    }

    private static MovementPacket packet(long tick, long nanos, double x) {
        return new MovementPacket(tick, nanos, x, 0.0, 0.0, 0.0f, 0.0f, true, Set.of());
    }
}
