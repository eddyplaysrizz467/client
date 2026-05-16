package falcon.physics;

import java.util.List;

public record NearbyBlockSnapshot(
        long serverTick,
        List<BlockBounds> collidableBounds,
        String source
) {
    public static NearbyBlockSnapshot empty(long serverTick) {
        return new NearbyBlockSnapshot(serverTick, List.of(), "empty");
    }
}
