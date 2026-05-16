package falcon.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class StateHistory {
    private final int maxSize;
    private final Deque<MovementState> states;

    public StateHistory(int maxSize) {
        if (maxSize < 2) {
            throw new IllegalArgumentException("history must keep at least two states");
        }
        this.maxSize = maxSize;
        this.states = new ArrayDeque<>(maxSize);
    }

    public void add(MovementState state) {
        states.addLast(state);
        while (states.size() > maxSize) {
            states.removeFirst();
        }
    }

    public MovementState latest() {
        return states.peekLast();
    }

    public MovementState previous() {
        if (states.size() < 2) {
            return null;
        }
        MovementState latest = states.removeLast();
        MovementState previous = states.peekLast();
        states.addLast(latest);
        return previous;
    }

    public int size() {
        return states.size();
    }

    public List<MovementState> snapshot() {
        return new ArrayList<>(states);
    }
}
