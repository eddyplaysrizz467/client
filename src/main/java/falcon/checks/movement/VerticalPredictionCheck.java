package falcon.checks.movement;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

public final class VerticalPredictionCheck implements FalconCheck {
    @Override
    public String name() {
        return "VerticalPrediction";
    }

    @Override
    public CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet) {
        return CheckResult.pass(name(), "reserved for later physics milestone");
    }
}
