package ia.problemes;

import java.util.Objects;

public class Vehicle {
    private int x;
    private int y;
    private int taille;
    private Direction direction;
    private char color;

    /**
     * Constructeur pour representer un vehicule
     * @param x, y Les coordonnées du vehicule (on doit prendre la premiere apparition du vehicule)
     * @param taille La taille du vehicule
     * @param direction Le sens du vehicule
     * @param color La couleur du vehicule (de preference unique)
     */
    public Vehicle(int x, int y, int taille, Direction direction, char color) {
        this.x = x;
        this.y = y;
        this.taille = taille;
        this.direction = direction;
        this.color = color;
    }

    public void moveLeft() {
        y -= 1;
    }

    public void moveRight() {
        y += 1;
    }

    public void moveUp() {
        x -= 1;
    }

    public void moveDown() {
        x += 1;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTaille() {
        return taille;
    }

    public Direction getDirection() {
        return direction;
    }

    public char getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return x == vehicle.x && y == vehicle.y && taille == vehicle.taille && color == vehicle.color && direction == vehicle.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, taille, direction, color);
    }

    /**
     * Copier un vehicule pour eviter l'effet de bord
     * @return Vehicule
     */
    public Vehicle cloneVehicle() {
        return new Vehicle(x, y, taille, direction, color);
    }
}
