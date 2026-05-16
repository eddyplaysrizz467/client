package falcon.checks.movement;

import falcon.api.FalconCheck;
import falcon.api.FalconPlayer;
import falcon.packet.MovementPacket;
import falcon.physics.MovementSimulator;
import falcon.physics.PredictionResult;
import falcon.player.MovementState;
import falcon.player.PlayerData;
import falcon.violation.CheckResult;

import java.util.Objects;

public final class HorizontalPredictionCheck implements FalconCheck {
    private final MovementSimulator simulator;

    public HorizontalPredictionCheck(MovementSimulator simulator) {
        this.simulator = Objects.requireNonNull(simulator, "simulator");
    }

    @Override
    public String name() {
        return "HorizontalPrediction";
    }

    @Override
    public CheckResult handle(FalconPlayer player, PlayerData data, MovementPacket packet) {
        MovementState previous = data.previousState();
        MovementState current = data.currentState();
        PredictionResult prediction = simulator.horizontalEnvelope(previous, current);

        if (prediction.legal()) {
            return CheckResult.pass(name(), prediction.reason());
        }

        return CheckResult.alert(
                name(),
                prediction.confidence(),
                "horizontal distance %.4f exceeded %.4f".formatted(prediction.actual(), prediction.allowed())
        );
    }
}
