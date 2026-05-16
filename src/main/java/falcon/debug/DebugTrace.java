package falcon.debug;

import falcon.violation.CheckResult;

import java.util.List;

public record DebugTrace(List<String> lines) {
    public static DebugTrace from(List<CheckResult> results) {
        return new DebugTrace(results.stream()
                .map(result -> "%s confidence=%.2f %s".formatted(
                        result.checkName(),
                        result.confidence(),
                        result.message()))
                .toList());
    }
}
