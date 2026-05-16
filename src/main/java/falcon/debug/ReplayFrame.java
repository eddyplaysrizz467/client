package falcon.debug;

import falcon.packet.MovementPacket;
import falcon.physics.NearbyBlockSnapshot;
import falcon.player.MovementState;
import falcon.violation.CheckResult;

import java.util.List;

public record ReplayFrame(
        MovementPacket packet,
        MovementState previousState,
        MovementState currentState,
        NearbyBlockSnapshot nearbyBlockSnapshot,
        List<CheckResult> decisions,
        DebugTrace trace
) {
}
