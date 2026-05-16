package falcon.violation;

public record CheckResult(
        String checkName,
        boolean alert,
        double confidence,
        String message
) {
    public static CheckResult pass(String checkName, String message) {
        return new CheckResult(checkName, false, 0.0, message);
    }

    public static CheckResult alert(String checkName, double confidence, String message) {
        return new CheckResult(checkName, true, Math.max(0.0, Math.min(1.0, confidence)), message);
    }
}
