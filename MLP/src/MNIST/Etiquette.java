package MNIST;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Etiquette {

    public static int[] getEtiquette(String path) {
        try {
            DataInputStream labels = new DataInputStream(new FileInputStream(path));
            int magicNumber = labels.readInt(); // Lire l'entier identifiant le type de fichier (2051)
            int numLabels = labels.readInt(); // Lire le nombre d'images dans le fichier (int)
            int[] etiquettes = new int[numLabels];
            for (int i = 0; i < numLabels; i++) {
                int label = labels.readUnsignedByte();
                etiquettes[i] = label;
            }
            return etiquettes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        try {
            DataInputStream labels = new DataInputStream(new FileInputStream("C:\\Users\\Gaspard\\Documents\\BUT\\S5\\Optimisation\\TD1\\images\\train-labels.idx1-ubyte"));
            int magicNumber = labels.readInt(); // Lire l'entier identifiant le type de fichier (2051)
            int numLabels = labels.readInt(); // Lire le nombre d'images dans le fichier (int)
            int[] etiquettes = new int[numLabels];
            System.out.println("Nombre d'étiquettes : " + numLabels);
            for (int i = 0; i < numLabels; i++) {
                int label = labels.readUnsignedByte();
                etiquettes[i] = label;
            }
            System.out.println("Première étiquette : " + etiquettes[0]);
            System.out.println("Dernière étiquette : " + etiquettes[numLabels - 1]);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
