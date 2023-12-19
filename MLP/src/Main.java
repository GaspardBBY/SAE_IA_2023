import MLP.MLP;
import MLP.Sigmoide;
import MNIST.Donnees;
import MNIST.Imagette;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) {
        List<Integer> couches = new ArrayList<>();
        double initalLeanringRate = 0.3;
        double finalLearningRate = 0.3;
        boolean schuffle = true;
        double tauxSortie = 0.05;
        int maxIteration = 40;
        try {
            String[] couchesString = args[0].split(",");
            for (String s : couchesString) {
                couches.add(Integer.parseInt(s));
            }
            couches.add(10);
            initalLeanringRate = Double.parseDouble(args[1]);
            finalLearningRate = Double.parseDouble(args[2]);
            schuffle = Boolean.parseBoolean(args[3]);
            tauxSortie = Double.parseDouble(args[4]);
            maxIteration = Integer.parseInt(args[5]);
        } catch (Exception e) {
            System.err.println("Erreur dans la saisie des paramètres => ");
            System.out.println("Usage : java -jar Main.jar couchesCachés initialLearningRate schuffle tauxSortie");
            System.out.println("\t> couchesCaches : liste des couches cachés séparées par des virgules (ex: 200,100)");
            System.out.println("\t> initialLearningRate : taux d'apprentissage initial (ex: 0.6)");
            System.out.println("\t> finalLearningRate : taux d'apprentissage final (même si on veut que ça ne change pas) (ex: 0.6)");
            System.out.println("\t> schuffle : mélange des données (ex: true)");
            System.out.println("\t> tauxSortie : taux de sortie (0.02)");
            System.out.println("\t> maxIteration : nombre d'itération max (ex: 40)");
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Chargement des données..");
        Donnees donneesEntrainement = null;
        Donnees donneesTest = null;
        try {
            CompletableFuture<Donnees> donneesEntrainementFutureChargement = CompletableFuture.supplyAsync(() -> Donnees.loadImagette(-1, "images" + File.separator + "train-images.idx3-ubyte", "images" + File.separator + "train-labels.idx1-ubyte"));
            CompletableFuture<Donnees> donneesTestFutureChargement = CompletableFuture.supplyAsync(() -> Donnees.loadImagette(-1, "images" + File.separator + "t10k-images.idx3-ubyte", "images" + File.separator + "t10k-labels.idx1-ubyte"));
            donneesEntrainement = donneesEntrainementFutureChargement.join();
            donneesTest = donneesTestFutureChargement.join();
        } catch (Exception e) {
            System.err.println("Impossible d'atteindre les images");
            System.err.println("Veuillez placer le dossier images à la même racine que le fichier jar");
            System.exit(1);
        }
        if (donneesEntrainement == null || donneesTest == null) {
            System.err.println("Impossible de lire les images");
            System.exit(1);
        }
        System.out.println("nb images d'entrainement : " + donneesEntrainement.getImagettesArray().length);
        int tailleImage = donneesEntrainement.getImagettesArray()[0].getHeight() * donneesEntrainement.getImagettesArray()[0].getWidth();
        couches.add(0, tailleImage);
        MLP mlp = new MLP(couches.stream().mapToInt(i -> i).toArray(), initalLeanringRate, new Sigmoide());

        boolean learned = false;

        List<Imagette> imagesEntrainements = Arrays.asList(donneesEntrainement.getImagettesArray());
        List<Imagette> imagesDeTests = Arrays.asList(donneesTest.getImagettesArray());

        runMPL(couches, initalLeanringRate, finalLearningRate, schuffle, imagesEntrainements, imagesDeTests, tauxSortie, maxIteration, learned, mlp);
    }

    /**
     * Méthode qui permet d'entrainer et de tester un MLP
     *
     * @param couches             List des couches du MLP
     * @param initialLearningRate Pas d'apprentissage initial
     * @param finalLearningRate   Pas d'apprentissage final (!= de l'initial si dégressif)
     * @param schuffle            boolean -> mélange des images avant de les apprendres à chaque itération
     * @param imagesEntrainements Lists des images de la base d'entrainement
     * @param imagesDeTests       Lists des images de la base de test
     * @param tauxSortie          Taux de sortie du programme
     * @param maxIteration        maximum d'itération. Chaque itération contient 10 répétitions de l'apprentissage sur toutes les images d'entrainements
     * @param mlp                 MLP
     */
    private static void runMPL(List<Integer> couches, double initialLearningRate, double finalLearningRate, boolean schuffle, List<Imagette> imagesEntrainements, List<Imagette> imagesDeTests, double tauxSortie, int maxIteration, MLP mlp) {
        int iteration = 0;
        boolean learned = false;
        System.out.println("Lancement avec les paramètres : ");
        System.out.println("Couches : " + Arrays.toString(couches.toArray()));
        boolean leanringRateDegressif = (initialLearningRate != finalLearningRate);
        if (leanringRateDegressif) {
            System.out.println("Initial learning rate : " + initialLearningRate);
            System.out.println("Final learning rate : " + finalLearningRate);
        } else
            System.out.println("Learning rate : " + initialLearningRate);
        System.out.println("Schuffle : " + schuffle);
        System.out.println("Nombre d'images d'entrainement : " + imagesEntrainements.size());
        System.out.println("Nombre d'images de test : " + imagesDeTests.size());
        System.out.println("Taux de sortie : " + tauxSortie);
        System.out.println("Nombre d'iteration max : " + maxIteration);

        System.out.println("Modèle CSV puis résultats");
        System.out.println("Iteration;testBaseTest;testBaseApprentissage;tempsIteration(ms)" + (leanringRateDegressif ? ";learningRate" : ""));

        double slope = (finalLearningRate - initialLearningRate) / maxIteration;

        int nbRepetition = 10;
        long allTime = System.nanoTime();
        while (iteration < maxIteration && !learned) {
            if (leanringRateDegressif)
                mlp.setLearningRate(initialLearningRate + slope * iteration);
            long startTime = System.nanoTime();
            double errorSum = 0;
            for (int i = 0; i < nbRepetition; i++) {
                // Affichage progression
//                int progress = (int) ((double) i / nbRepetition * 20);
//                System.out.print("\rProgression de l'itération: [" + "=".repeat(progress) + " ".repeat(20 - progress) + "] " + i + "/" + nbRepetition);
                if (schuffle) Collections.shuffle(imagesEntrainements);
                for (Imagette imagette : imagesEntrainements) {
                    double etiquetteInput = imagette.getEtiquette();
                    errorSum += mlp.backPropagate(toOneArray(imagette.getImagette()), intToArray((int) etiquetteInput));
                }
            }
            double errorBackPropagate = errorSum / (imagesEntrainements.size() * 10);

            iteration++;
            // Tests de notre MLP
            int nbIncorrect = 0;
            for (Imagette imagette : imagesDeTests) {
                double etiquetteInput = imagette.getEtiquette();
                int etiquetteInputInt = (int) etiquetteInput;
                double[] output = mlp.execute(toOneArray(imagette.getImagette()));
                if (findMaxIndex(output) != etiquetteInputInt) {
                    nbIncorrect++;
                }
            }
            double tauxIncorrect = (double) nbIncorrect / imagesDeTests.size();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000;
            System.out.println(iteration + ";" + tauxIncorrect + ";" + errorBackPropagate + ";" + duration + ((leanringRateDegressif) ? ";" + mlp.getLearningRate() : ""));
            if (tauxIncorrect < tauxSortie) {
                learned = true;
                long allEndTime = System.nanoTime();
                System.out.println("Appris en " + (allEndTime - allTime) / 1000000 + " ms");
            }
        }
    }


    /**
     * @param input index souhaité
     * @return tableau de double à 0 sauf à l'index que l'on souhaite 1
     */
    static double[] intToArray(int input) {
        double[] result = new double[10];
        result = Arrays.stream(result).map(x -> 0).toArray();
        result[input] = 1;
        return result;
    }

    /**
     * @param array Tableau à deux dimensions
     * @return Tableau à une dimension
     */
    static double[] toOneArray(int[][] array) {
        return Arrays.stream(array).flatMapToInt(Arrays::stream).mapToDouble(i -> i / 255.0).toArray();
    }

    /**
     * Le resultat est l'index de la valeur max dans le tableau (10 entrées)
     *
     * @param array tableau d'entrée
     * @return index du max
     */
    private static int findMaxIndex(double[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}