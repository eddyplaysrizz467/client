package falcon.player;

import falcon.packet.MovementPacket;

import java.util.Objects;

public final class PlayerData {
    private final StateHistory history;
    private MovementPacket lastPacket;
    private MovementState currentState;

    public PlayerData(int historySize) {
        this.history = new StateHistory(historySize);
    }

    public MovementPacket lastPacket() {
        return lastPacket;
    }

    public MovementState currentState() {
        return currentState;
    }

    public MovementState previousState() {
        return history.previous();
    }

    public StateHistory history() {
        return history;
    }

    public void accept(MovementPacket packet, MovementState state) {
        this.lastPacket = Objects.requireNonNull(packet, "packet");
        this.currentState = Objects.requireNonNull(state, "state");
        this.history.add(state);
    }
}
