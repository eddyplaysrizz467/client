package falcon.checks.movement;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.player.MovementState;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

public final class FlightPredictionCheck implements FalconCheck {
    private static final double MAX_LEGAL_AIR_TIME_UPWARD_MOTION = 0.08D;

    @Override
    public String name() {
        return "Flight";
    }

    @Override
    public CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet) {
        MovementState previous = data.previousState();
        MovementState current = data.currentState();

        if (previous == null || current.exempt() || previous.exempt() || current.collisionOnGround()) {
            return CheckResult.pass(name(), "grounded, exempt, or insufficient state");
        }

        double verticalDelta = current.verticalDistanceTo(previous);
        boolean hoveringOrRising = verticalDelta > -MAX_LEGAL_AIR_TIME_UPWARD_MOTION;
        boolean clientClaimsAir = !current.clientOnGround();

        if (!hoveringOrRising || !clientClaimsAir) {
            return CheckResult.pass(name(), "air movement still plausible");
        }

        double confidence = Math.min(1.0D, (verticalDelta + MAX_LEGAL_AIR_TIME_UPWARD_MOTION) / 0.30D);
        return CheckResult.alert(
                name(),
                Math.max(0.20D, confidence),
                "player stayed airborne without expected falling motion"
        );
    }
}
