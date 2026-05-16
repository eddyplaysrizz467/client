package falcon.player;

import java.util.Set;

public record MovementState(
        long serverTick,
        long receivedAtNanos,
        double x,
        double y,
        double z,
        float yaw,
        float pitch,
        boolean clientOnGround,
        boolean collisionOnGround,
        Set<Exemption> exemptions
) {
    public double horizontalDistanceTo(MovementState other) {
        double dx = x - other.x;
        double dz = z - other.z;
        return Math.hypot(dx, dz);
    }

    public double verticalDistanceTo(MovementState other) {
        return y - other.y;
    }

    public boolean exempt() {
        return !exemptions.isEmpty();
    }
}
