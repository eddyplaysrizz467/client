package falcon.checks.movement;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.physics.PhysicsConstants;
import falcon.player.MovementState;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

public final class TimerCheck implements FalconCheck {
    @Override
    public String name() {
        return "Timer";
    }

    @Override
    public CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet) {
        MovementState previous = data.previousState();
        MovementState current = data.currentState();

        if (previous == null || current.exempt() || previous.exempt()) {
            return CheckResult.pass(name(), "insufficient or exempt state");
        }

        long packetDelta = current.receivedAtNanos() - previous.receivedAtNanos();
        long expectedDelta = Math.max(1L, current.serverTick() - previous.serverTick())
                * PhysicsConstants.EXPECTED_PACKET_NANOS;

        if (packetDelta >= expectedDelta - PhysicsConstants.EXPECTED_PACKET_NANOS / 4) {
            return CheckResult.pass(name(), "packet cadence legal");
        }

        double shortage = expectedDelta - packetDelta;
        double confidence = Math.min(1.0, shortage / PhysicsConstants.TIMER_CONFIDENCE_NANOS);
        return CheckResult.alert(
                name(),
                confidence,
                "movement packets arrived %.2fms earlier than expected".formatted(shortage / 1_000_000.0)
        );
    }
}
