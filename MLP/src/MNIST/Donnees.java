package MNIST;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Donnees {
    private Imagette[] imagettesArray;

    public Imagette[] getImagettesArray() {
        return imagettesArray;
    }

    public Donnees(Imagette[] imagettesArray, String path) {
        int[] etiquettes = Etiquette.getEtiquette(path);
        for (int i = 0; i < imagettesArray.length; i++) {
            imagettesArray[i].setEtiquette(etiquettes[i]);
        }
        this.imagettesArray = imagettesArray;
    }

    public static Donnees loadImagette(int nbIterationDemande, String pathImage, String pathEtiquette) {
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(pathImage));
            int magicNumber = dis.readInt(); // Lire l'entier identifiant le type de fichier (2051)
            int nbImages = dis.readInt(); // Lire le nombre d'images dans le fichier (int)
            int nbIteration = nbIterationDemande < 0 ? nbImages : Math.min(nbIterationDemande, nbImages);

            int numRows = dis.readInt(); // Lire le nombre de lignes des images (int)
            int numCols = dis.readInt(); // Lire le nombre de colonnes des images (int)
            Imagette[] imagettes = new Imagette[nbIteration];

            for (int i = 0; i < imagettes.length; i++) {
                int[][] pixels = new int[numRows][numCols];
                for (int row = 0; row < numRows; row++) {
                    for (int col = 0; col < numCols; col++) {
                        int pixelValue = dis.readUnsignedByte(); // Lire le niveau de gris de chaque pixel (octet non signé)
                        pixels[col][row] = pixelValue;
                    }
                }
                imagettes[i] = new Imagette(pixels);
            }
            return new Donnees(imagettes, pathEtiquette);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
