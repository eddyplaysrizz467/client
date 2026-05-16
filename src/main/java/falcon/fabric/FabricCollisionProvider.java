package falcon.fabric;

import falcon.physics.BlockBounds;
import falcon.physics.CollisionProvider;
import falcon.physics.NearbyBlockSnapshot;
import falcon.player.MovementState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

import java.util.ArrayList;
import java.util.List;

public final class FabricCollisionProvider implements CollisionProvider {
    private final ThreadLocal<ServerPlayerEntity> currentPlayer = new ThreadLocal<>();

    public void withPlayer(ServerPlayerEntity player, Runnable task) {
        currentPlayer.set(player);
        try {
            task.run();
        } finally {
            currentPlayer.remove();
        }
    }

    @Override
    public boolean hasCollision(BlockBounds bounds, MovementState previous) {
        ServerPlayerEntity player = currentPlayer.get();
        if (player == null) {
            return false;
        }
        return player.getEntityWorld().getBlockCollisions(player, toBox(bounds)).iterator().hasNext();
    }

    @Override
    public NearbyBlockSnapshot snapshotAround(MovementState state) {
        ServerPlayerEntity player = currentPlayer.get();
        if (player == null || state == null) {
            return CollisionProvider.super.snapshotAround(state);
        }

        List<BlockBounds> bounds = new ArrayList<>();
        Box playerBox = toBox(falcon.physics.MovementSimulator.playerBounds(state.x(), state.y(), state.z())).expand(1.0D);
        for (VoxelShape shape : player.getEntityWorld().getBlockCollisions(player, playerBox)) {
            for (Box box : shape.getBoundingBoxes()) {
                bounds.add(fromBox(box));
            }
        }
        return new NearbyBlockSnapshot(state.serverTick(), bounds, "fabric-world");
    }

    private static Box toBox(BlockBounds bounds) {
        return new Box(bounds.minX(), bounds.minY(), bounds.minZ(), bounds.maxX(), bounds.maxY(), bounds.maxZ());
    }

    private static BlockBounds fromBox(Box box) {
        return new BlockBounds(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }
}
