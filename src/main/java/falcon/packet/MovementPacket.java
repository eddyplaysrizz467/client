package falcon.packet;

import falcon.player.Exemption;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class MovementPacket {
    private final long serverTick;
    private final long receivedAtNanos;
    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;
    private final boolean onGround;
    private final Set<Exemption> exemptions;

    public MovementPacket(
            long serverTick,
            long receivedAtNanos,
            double x,
            double y,
            double z,
            float yaw,
            float pitch,
            boolean onGround,
            Set<Exemption> exemptions
    ) {
        this.serverTick = serverTick;
        this.receivedAtNanos = receivedAtNanos;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.exemptions = exemptions == null || exemptions.isEmpty()
                ? Collections.emptySet()
                : Collections.unmodifiableSet(EnumSet.copyOf(exemptions));
    }

    public long serverTick() {
        return serverTick;
    }

    public long receivedAtNanos() {
        return receivedAtNanos;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() {
        return z;
    }

    public float yaw() {
        return yaw;
    }

    public float pitch() {
        return pitch;
    }

    public boolean onGround() {
        return onGround;
    }

    public Set<Exemption> exemptions() {
        return exemptions;
    }

    public boolean hasAnyExemption() {
        return !exemptions.isEmpty();
    }

    public MovementPacket requireSaneTime() {
        if (serverTick < 0) {
            throw new IllegalArgumentException("serverTick must be non-negative");
        }
        if (receivedAtNanos < 0) {
            throw new IllegalArgumentException("receivedAtNanos must be non-negative");
        }
        return this;
    }

    @Override
    public String toString() {
        return "MovementPacket{" +
                "serverTick=" + serverTick +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", onGround=" + onGround +
                ", exemptions=" + exemptions +
                '}';
    }
}
