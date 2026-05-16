package falcon.api;

import falcon.packet.MovementPacket;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

public interface FalconCheck {
    String name();

    CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet);
}
