package falcon.checks.movement;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.player.MovementState;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

public final class GroundSpoofCheck implements FalconCheck {
    @Override
    public String name() {
        return "GroundSpoof";
    }

    @Override
    public CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet) {
        MovementState current = data.currentState();
        if (current == null || current.exempt() || current.clientOnGround() == current.collisionOnGround()) {
            return CheckResult.pass(name(), "ground state legal or exempt");
        }

        return CheckResult.alert(
                name(),
                0.35,
                "client ground=%s collision ground=%s".formatted(current.clientOnGround(), current.collisionOnGround())
        );
    }
}
