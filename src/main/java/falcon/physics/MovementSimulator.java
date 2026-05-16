package falcon.physics;

import falcon.packet.MovementPacket;
import falcon.player.MovementState;

import java.util.Objects;

public final class MovementSimulator {
    private final CollisionProvider collisionProvider;

    public MovementSimulator(CollisionProvider collisionProvider) {
        this.collisionProvider = Objects.requireNonNull(collisionProvider, "collisionProvider");
    }

    public MovementState toState(MovementPacket packet, MovementState previous) {
        boolean collisionOnGround = isOnGround(packet, previous);
        return new MovementState(
                packet.serverTick(),
                packet.receivedAtNanos(),
                packet.x(),
                packet.y(),
                packet.z(),
                packet.yaw(),
                packet.pitch(),
                packet.onGround(),
                collisionOnGround,
                packet.exemptions()
        );
    }

    public boolean isOnGround(MovementPacket packet, MovementState previous) {
        BlockBounds feetProbe = playerBounds(packet.x(), packet.y(), packet.z())
                .offset(0.0, -PhysicsConstants.GROUND_EPSILON, 0.0);
        return collisionProvider.hasCollision(feetProbe, previous);
    }

    public PredictionResult horizontalEnvelope(MovementState previous, MovementState current) {
        if (previous == null || current.exempt() || previous.exempt()) {
            return PredictionResult.pass("insufficient or exempt state", 0.0, 0.0);
        }

        long tickDelta = Math.max(1L, current.serverTick() - previous.serverTick());
        double allowed = PhysicsConstants.walkSpeedPerTick(current.collisionOnGround()) * tickDelta;
        if (!current.collisionOnGround()) {
            allowed += PhysicsConstants.AIRBORNE_HORIZONTAL_ALLOWANCE;
        }

        double actual = current.horizontalDistanceTo(previous);
        double excess = Math.max(0.0, actual - allowed);
        double confidence = excess <= 0.0 ? 0.0 : Math.min(1.0, excess / PhysicsConstants.SPEED_CONFIDENCE_DISTANCE);
        boolean legal = excess <= PhysicsConstants.HORIZONTAL_EPSILON;
        return new PredictionResult(legal, actual, allowed, confidence, "horizontal envelope");
    }

    public NearbyBlockSnapshot snapshotAround(MovementState state) {
        return collisionProvider.snapshotAround(state);
    }

    public static BlockBounds playerBounds(double x, double y, double z) {
        double radius = PhysicsConstants.PLAYER_WIDTH / 2.0;
        return new BlockBounds(
                x - radius,
                y,
                z - radius,
                x + radius,
                y + PhysicsConstants.PLAYER_HEIGHT,
                z + radius
        );
    }
}
