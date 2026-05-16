package falcon.packet;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.debug.DebugTrace;
import falcon.debug.ReplayRecorder;
import falcon.physics.MovementSimulator;
import falcon.player.MovementState;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;
import falcon.violation.ViolationTracker;

import java.util.List;
import java.util.Objects;

public final class PacketListener {
    private final MovementSimulator simulator;
    private final List<FalconCheck> checks;
    private final ReplayRecorder replayRecorder;
    private final ViolationTracker violationTracker;

    public PacketListener(
            MovementSimulator simulator,
            List<FalconCheck> checks,
            ReplayRecorder replayRecorder,
            ViolationTracker violationTracker
    ) {
        this.simulator = Objects.requireNonNull(simulator, "simulator");
        this.checks = List.copyOf(checks);
        this.replayRecorder = Objects.requireNonNull(replayRecorder, "replayRecorder");
        this.violationTracker = Objects.requireNonNull(violationTracker, "violationTracker");
    }

    public List<CheckResult> onMovement(FalconPlayer player, PlayerData data, MovementPacket packet) {
        Objects.requireNonNull(player, "player");
        Objects.requireNonNull(data, "data");
        packet.requireSaneTime();

        MovementState previous = data.currentState();
        MovementState current = simulator.toState(packet, previous);
        data.accept(packet, current);

        List<CheckResult> results = checks.stream()
                .map(check -> check.handle(player, data, packet))
                .filter(CheckResult::alert)
                .toList();

        results.forEach(result -> violationTracker.record(player, result));
        replayRecorder.record(player, packet, previous, current, simulator.snapshotAround(current), results, DebugTrace.from(results));
        return results;
    }
}
