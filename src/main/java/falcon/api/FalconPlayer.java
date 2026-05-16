package falcon.api;

import java.util.Objects;
import java.util.UUID;

public final class FalconPlayer {
    private final UUID uniqueId;
    private final String name;

    public FalconPlayer(UUID uniqueId, String name) {
        this.uniqueId = Objects.requireNonNull(uniqueId, "uniqueId");
        this.name = Objects.requireNonNull(name, "name");
    }

    public UUID uniqueId() {
        return uniqueId;
    }

    public String name() {
        return name;
    }
}
