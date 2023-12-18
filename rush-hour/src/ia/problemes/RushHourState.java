package ia.problemes;

import ia.framework.common.Action;
import ia.framework.common.Misc;
import ia.framework.common.State;

import java.util.Arrays;
import java.util.Objects;

public class RushHourState extends State {

    private static final int GAME_SIZE = 6;
    private static final int EMPTY = ' ';
    private Vehicle[] board;

    public RushHourState() {
        this.board = generateBoard();
    }

    public RushHourState(Vehicle[] board) {
        this.board = board.clone();
    }

    public Vehicle[] getBoard() {
        return board;
    }

    @Override
    protected State cloneState() {
        Vehicle[] boardClone = new Vehicle[board.length];
        for (int i = 0; i < board.length; i++) {
            boardClone[i] = board[i].cloneVehicle();
        }
        return new RushHourState(boardClone);
    }

    @Override
    protected boolean equalsState(State o) {
        return Arrays.equals(board, ((RushHourState) o).getBoard());
    }

    public boolean isLegal(Vehicle v, Action a) {

        int[][] game = toIntArray();
        int x = v.x;
        int y = v.y;

        // Si la voiture est horizontal, on avance que a gauche ou à droite
        if (v.direction == Direction.HORIZONTAL) {
            if (a == RushHour.LEFT) {
                y = y - 1;
            } else if (a == RushHour.RIGHT) {
                y = y + v.taille;
            } else {
                return false;
            }
            // Si la voiture est vertical, on avance en haut et en bas
        } else {

            if (a == RushHour.UP) {
                x = x - 1;
            } else if (a == RushHour.DOWN) {
                x = x + v.taille;
            } else {
                return false;
            }
        }

        if (x < 0 || y < 0 || x >= GAME_SIZE || y >= GAME_SIZE) {
            return false;
        }

        return game[x][y] == EMPTY;

    }

    @Override
    protected int hashState() {
        return Arrays.hashCode(board);
    }

    public int[][] toIntArray() {
        int[][] game = new int[GAME_SIZE][GAME_SIZE];

        for (int i = 0; i < GAME_SIZE; i++)
            Arrays.fill(game[i], EMPTY);

        for (Vehicle vehicle : board) {

            for (int i = 0; i < vehicle.taille; i++) {
                if (vehicle.direction == Direction.HORIZONTAL) {
                    game[vehicle.x][vehicle.y + i] = vehicle.color;
                } else {
                    game[vehicle.x + i][vehicle.y] = vehicle.color;
                }
            }

        }

        return game;
    }

    public String toString() {

        int[][] game = toIntArray();

        String res = "";
        res += Misc.dupString("+---", GAME_SIZE);
        res += "+\n";
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++)
                res += "| " + (char) game[i][j] + " ";
            if (i != 2) res += "|";
            res += "\n";

            res += Misc.dupString("+---", GAME_SIZE);
            res += "+\n";
        }
        return res;

    }


    /**
     * Méthodes privés pour la logique du jeu
     */

    public Vehicle getVehicleByColor(String color) {
        char c = color.charAt(0);
        for (RushHourState.Vehicle v : board) {
            if (v.getColor() == c) {
                return v;
            }
        }
        return null;
    }

    public double getHeuristic() {
        return manathanDistance();
    }

    //
    // API privée, manipulation du jeux
    //

    // Calcule la distance entre le puzzle et la solution
    // https://en.wikipedia.org/wiki/Taxicab_geometry
    private double manathanDistance() {
        double result = 0;
        Vehicle locCurr = getVehicleByColor("R");
        result += Math.abs(5 - locCurr.getX());
        return result;
    }

    public Vehicle[] generateBoard() {

        return new Vehicle[]{
                new Vehicle(0, 2, 3, Direction.VERTICAL, 'Y'),
                new Vehicle(0, 4, 2, Direction.HORIZONTAL, 'G'),
                new Vehicle(2, 0, 2, Direction.HORIZONTAL, 'R'),
                new Vehicle(3, 0, 3, Direction.HORIZONTAL, 'P'),
                new Vehicle(3, 5, 3, Direction.VERTICAL, 'B'),
        };

    }

    public Vehicle[] hard() {

        return new Vehicle[]{
                new Vehicle(0, 0, 2, Direction.VERTICAL, 'L'),
                new Vehicle(0, 1, 2, Direction.VERTICAL, 'O'),
                new Vehicle(0, 2, 2, Direction.HORIZONTAL, 'A'),
                new Vehicle(0, 4, 2, Direction.VERTICAL, 'P'),
                new Vehicle(0, 5, 2, Direction.VERTICAL, 'D'),
                new Vehicle(2, 0, 3, Direction.VERTICAL, 'Y'),
                new Vehicle(2, 1, 2, Direction.HORIZONTAL, 'R'),
                new Vehicle(2, 3, 2, Direction.VERTICAL, 'G'),
                new Vehicle(2, 5, 2, Direction.VERTICAL, 'W'),
                new Vehicle(3, 1, 2, Direction.HORIZONTAL, 'N'),
                new Vehicle(4, 2, 2, Direction.VERTICAL, 'J'),
                new Vehicle(4, 3, 3, Direction.HORIZONTAL, 'V'),
                new Vehicle(5, 0, 2, Direction.HORIZONTAL, 'M'),
                new Vehicle(5, 3, 3, Direction.HORIZONTAL, 'Q'),
        };

    }

    public class Vehicle {
        private int x;
        private int y;
        private int taille;
        private Direction direction;
        private char color;

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

        public Vehicle cloneVehicle() {
            return new Vehicle(x, y, taille, direction, color);
        }
    }

    public enum Direction {
        VERTICAL,
        HORIZONTAL
    }

}
