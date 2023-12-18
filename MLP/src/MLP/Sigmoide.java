package MLP;

public class Sigmoide implements TransferFunction {
    @Override
    public double evaluate(double value) {
        //fonction : σ(x) = 1 / (1+e(−x))
        return  1 / (1 + Math.exp(-value));
    }

    @Override
    public double evaluateDer(double value) {
        //dérivée : σ'(x) = σ(x)−σ2(x))
        return value - Math.pow(value, 2);
    }

    @Override
    public String getName() {
        return "Sigmoide";
    }
}
