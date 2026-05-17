package falcon.checks.movement;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.player.MovementState;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

import java.util.List;

public final class FlightPredictionCheck implements FalconCheck {
    private static final int MIN_AIR_TICKS = 18;
    private static final double MIN_SUSPICIOUS_VERTICAL_DELTA = -0.015D;

    @Override
    public String name() {
        return "Flight";
    }

    @Override
    public CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet) {
        MovementState previous = data.previousState();
        MovementState current = data.currentState();

        if (previous == null || current.exempt() || previous.exempt() || current.collisionOnGround() || current.clientOnGround()) {
            return CheckResult.pass(name(), "grounded, exempt, or insufficient state");
        }

        if (airTicks(data) < MIN_AIR_TICKS) {
            return CheckResult.pass(name(), "normal short air time");
        }

        double verticalDelta = current.verticalDistanceTo(previous);
        boolean hoveringOrRising = verticalDelta > MIN_SUSPICIOUS_VERTICAL_DELTA;
        if (!hoveringOrRising) {
            return CheckResult.pass(name(), "air movement still plausible");
        }

        double confidence = Math.min(1.0D, 0.25D + ((airTicks(data) - MIN_AIR_TICKS) / 30.0D));
        return CheckResult.alert(
                name(),
                confidence,
                "player stayed airborne without expected falling motion"
        );
    }

    private static int airTicks(PlayerData data) {
        int ticks = 0;
        List<MovementState> snapshot = data.history().snapshot();
        for (int i = snapshot.size() - 1; i >= 0; i--) {
            MovementState state = snapshot.get(i);
            if (state.collisionOnGround() || state.clientOnGround() || state.exempt()) {
                break;
            }
            ticks++;
        }
        return ticks;
    }
}
