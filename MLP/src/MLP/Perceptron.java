package MLP;

import java.util.Arrays;
import java.util.OptionalDouble;

public class Perceptron {
    static final double[][] INPUTOU = new double[][]{
            new double[]{-1, -1},
            new double[]{-1, 1},
            new double[]{1, -1},
            new double[]{1, 1}
    };

    static final double[] OUTPUTOU = new double[]{-1, 1, 1, 1};
    static final double[][] INPUTET = new double[][]{
            new double[]{0, 0},
            new double[]{0, 1},
            new double[]{1, 0},
            new double[]{1, 1}
    };

    static final double[] OUTPUTET = new double[]{0, 0, 0, 1};

    static final double[][] INPUTXOR = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};

    static final double[] OUTPUTXOR = new double[]{0, 1, 1, 0};

    public static void main(String[] args) {
        System.out.println("Test du perceptron multicouche sur ET avec les deux fonctions");
        TestPerceptron(new int[]{2, 1}, INPUTET, OUTPUTET, new Hyperbolique());
        TestPerceptron(new int[]{2, 1}, INPUTET, OUTPUTET, new Sigmoide());

        System.out.println("Test du perceptron multicouche sur OU avec les deux fonctions");
        TestPerceptron(new int[]{2, 1}, INPUTOU, OUTPUTOU, new Hyperbolique());
        TestPerceptron(new int[]{2, 1}, INPUTOU, OUTPUTOU, new Sigmoide());

        System.out.println("Test du perceptron multicouche sur XOR avec les deux fonctions");
        TestPerceptron(new int[]{2, 2, 1}, INPUTXOR, OUTPUTXOR, new Hyperbolique());
        TestPerceptron(new int[]{2, 2, 1}, INPUTXOR, OUTPUTXOR, new Sigmoide());
    }

    public static void TestPerceptron(int[] couches, double[][] inputArray, double[] output, TransferFunction transferFunction) {
        MLP mlp = new MLP(couches, 0.1, transferFunction);
        double[] errorArray = {1, 1, 1, 1};
        int iteration = 0;
        boolean learned = false;
        while (iteration < 100000 && !learned) {
            int index = (int) (Math.random() * inputArray.length);
            // recuperer un element au hasard en fonctionnelle
            double[] inputTest = inputArray[index];
            double goodOutPut = output[index];

            double erreurCourante = mlp.backPropagate(inputTest, new double[]{goodOutPut});
            errorArray[index] = erreurCourante;
            OptionalDouble errorMax = Arrays.stream(errorArray).max();
            if (errorMax.getAsDouble() < 0.1) {
                learned = true;
            }
            iteration++;
        }
        if (!learned) {
            System.out.println("    Aucune solution trouvée en " + iteration + " iterations");
            System.out.println("    Avec en couches : " + Arrays.toString(couches));
            System.out.println("    Avec en fonction : " + transferFunction.getName());
        } else {
            System.out.println("Systeme appris en " + iteration + " iteration");
        }

    }
}
