package falcon.physics;

public record BlockBounds(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ
) {
    public BlockBounds offset(double x, double y, double z) {
        return new BlockBounds(
                minX + x,
                minY + y,
                minZ + z,
                maxX + x,
                maxY + y,
                maxZ + z
        );
    }

    public boolean intersects(BlockBounds other) {
        return maxX > other.minX
                && minX < other.maxX
                && maxY > other.minY
                && minY < other.maxY
                && maxZ > other.minZ
                && minZ < other.maxZ;
    }
}
