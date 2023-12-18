package MLP;

public class Hyperbolique implements TransferFunction {
    @Override
    public double evaluate(double value) {
        //fonction : σ(x) = tanh(x)
        return Math.tanh(value);
    }

    @Override
    public double evaluateDer(double value) {
        //dérivée : σ'(x) = 1 − σ2(x)
        return 1 - Math.pow(value, 2);
    }

    @Override
    public String getName() {
        return "Hyperbolique";
    }
}
