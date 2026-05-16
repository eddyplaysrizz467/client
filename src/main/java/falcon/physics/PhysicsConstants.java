package falcon.physics;

public final class PhysicsConstants {
    public static final double PLAYER_WIDTH = 0.6;
    public static final double PLAYER_HEIGHT = 1.8;
    public static final double GROUND_EPSILON = 0.03125;
    public static final double HORIZONTAL_EPSILON = 0.035;
    public static final double WALK_SPEED_PER_TICK = 0.22;
    public static final double AIRBORNE_SPEED_PER_TICK = 0.19;
    public static final double AIRBORNE_HORIZONTAL_ALLOWANCE = 0.04;
    public static final double SPEED_CONFIDENCE_DISTANCE = 0.30;
    public static final long EXPECTED_PACKET_NANOS = 50_000_000L;
    public static final double TIMER_CONFIDENCE_NANOS = 25_000_000.0;

    private PhysicsConstants() {
    }

    public static double walkSpeedPerTick(boolean onGround) {
        return onGround ? WALK_SPEED_PER_TICK : AIRBORNE_SPEED_PER_TICK;
    }
}
