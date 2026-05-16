package falcon.physics;

public record PredictionResult(
        boolean legal,
        double actual,
        double allowed,
        double confidence,
        String reason
) {
    public static PredictionResult pass(String reason, double actual, double allowed) {
        return new PredictionResult(true, actual, allowed, 0.0, reason);
    }
}
