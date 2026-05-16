package falcon.debug;

import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.physics.NearbyBlockSnapshot;
import falcon.player.MovementState;
import falcon.violation.CheckResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class ReplayRecorder {
    private final Map<UUID, List<ReplayFrame>> frames = new HashMap<>();

    public void record(
            FalconPlayer player,
            MovementPacket packet,
            MovementState previousState,
            MovementState currentState,
            NearbyBlockSnapshot nearbyBlockSnapshot,
            List<CheckResult> decisions,
            DebugTrace trace
    ) {
        frames.computeIfAbsent(player.uniqueId(), ignored -> new ArrayList<>())
                .add(new ReplayFrame(packet, previousState, currentState, nearbyBlockSnapshot, List.copyOf(decisions), trace));
    }

    public List<ReplayFrame> frames(FalconPlayer player) {
        return frames.getOrDefault(player.uniqueId(), Collections.emptyList());
    }
}
