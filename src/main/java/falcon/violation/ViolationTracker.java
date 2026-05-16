package falcon.violation;

import falcon.api.FalconPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class ViolationTracker {
    private final Map<UUID, List<CheckResult>> alerts = new HashMap<>();

    public void record(FalconPlayer player, CheckResult result) {
        Objects.requireNonNull(player, "player");
        if (!result.alert()) {
            return;
        }
        alerts.computeIfAbsent(player.uniqueId(), ignored -> new ArrayList<>()).add(result);
    }

    public List<CheckResult> alerts(FalconPlayer player) {
        return alerts.getOrDefault(player.uniqueId(), Collections.emptyList());
    }
}
