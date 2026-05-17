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
    private final Map<UUID, Map<String, Integer>> buffers = new HashMap<>();

    public boolean record(FalconPlayer player, CheckResult result) {
        Objects.requireNonNull(player, "player");
        if (!result.alert()) {
            return false;
        }
        int count = buffers
                .computeIfAbsent(player.uniqueId(), ignored -> new HashMap<>())
                .merge(result.checkName(), 1, Integer::sum);
        int required = requiredBuffer(result.checkName());
        if (count < required) {
            return false;
        }
        buffers.get(player.uniqueId()).put(result.checkName(), required / 2);
        alerts.computeIfAbsent(player.uniqueId(), ignored -> new ArrayList<>()).add(result);
        return true;
    }

    public List<CheckResult> alerts(FalconPlayer player) {
        return alerts.getOrDefault(player.uniqueId(), Collections.emptyList());
    }

    private static int requiredBuffer(String checkName) {
        return switch (checkName) {
            case "Flight" -> 8;
            case "HorizontalPrediction" -> 5;
            case "Timer" -> 10;
            default -> 6;
        };
    }
}
