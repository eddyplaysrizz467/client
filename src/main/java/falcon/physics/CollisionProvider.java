package falcon.physics;

import falcon.player.MovementState;

public interface CollisionProvider {
    boolean hasCollision(BlockBounds bounds, MovementState previous);

    default NearbyBlockSnapshot snapshotAround(MovementState state) {
        return state == null ? NearbyBlockSnapshot.empty(-1L) : NearbyBlockSnapshot.empty(state.serverTick());
    }
}
