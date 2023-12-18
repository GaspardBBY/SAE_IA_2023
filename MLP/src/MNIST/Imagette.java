package MNIST;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Imagette {
    private int[][] imagette;
    private int width;
    private int height;

    private int etiquette;

    public Imagette(int[][] imagette) {
        this.imagette = imagette;
        this.width = imagette.length;
        this.height = imagette[0].length;
    }

    public void setEtiquette(int etiquette) {
        this.etiquette = etiquette;
    }


    public void screen(String path) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                image.setRGB(col, row, (imagette[col][row] << 16) | (imagette[col][row] << 8) | imagette[col][row]);
            }
        }

        try {
            ImageIO.write(image, "png", new File(path));
            System.out.println("Image sauvegardée avec succès : " + path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[][] getImagette() {
        return imagette;
    }

    public int getEtiquette() {
        return etiquette;
    }

}
