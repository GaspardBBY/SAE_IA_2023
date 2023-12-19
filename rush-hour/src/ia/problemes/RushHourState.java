package ia.problemes;

import ia.framework.common.Action;
import ia.framework.common.Misc;
import ia.framework.common.State;

import java.util.Arrays;

public class RushHourState extends State {

    // Taille du plateau (6x6)
    private static final int GAME_SIZE = 6;
    private static final int EMPTY = ' ';

    public static final Vehicle[] BEGINNER_PUZZLE = new Vehicle[]{
            new Vehicle(0, 2, 3, Direction.VERTICAL, 'Y'),
            new Vehicle(0, 4, 2, Direction.HORIZONTAL, 'G'),
            new Vehicle(2, 0, 2, Direction.HORIZONTAL, 'R'),
            new Vehicle(3, 0, 3, Direction.HORIZONTAL, 'P'),
            new Vehicle(3, 5, 3, Direction.VERTICAL, 'B'),
    };

    public static final Vehicle[] INTERMEDIATE_PUZZLE = new Vehicle[]{
            new Vehicle(0, 0, 3, Direction.HORIZONTAL, 'Y'),
            new Vehicle(0, 3, 2, Direction.VERTICAL, 'L'),
            new Vehicle(0, 5, 2, Direction.VERTICAL, 'O'),
            new Vehicle(1, 2, 3, Direction.VERTICAL, 'V'),
            new Vehicle(2, 0, 2, Direction.HORIZONTAL, 'R'),
            new Vehicle(2, 5, 2, Direction.VERTICAL, 'A'),
            new Vehicle(3, 3, 2, Direction.HORIZONTAL, 'P'),
            new Vehicle(4, 0, 3, Direction.HORIZONTAL, 'B'),
            new Vehicle(4, 4, 2, Direction.VERTICAL, 'D'),
            new Vehicle(5, 1, 2, Direction.HORIZONTAL, 'G'),
    };
    public static final Vehicle[] ADVANCED_PUZZLE = new Vehicle[]{
            new Vehicle(0, 0, 2, Direction.HORIZONTAL, 'L'),
            new Vehicle(0, 2, 2, Direction.VERTICAL, 'O'),
            new Vehicle(0, 3, 3, Direction.VERTICAL, 'Y'),
            new Vehicle(1, 0, 3, Direction.VERTICAL, 'V'),
            new Vehicle(2, 1, 2, Direction.HORIZONTAL, 'R'),
            new Vehicle(3, 3, 3, Direction.HORIZONTAL, 'B'),
            new Vehicle(4, 0, 3, Direction.HORIZONTAL, 'G'),
            new Vehicle(4, 4, 2, Direction.VERTICAL, 'A'),
            new Vehicle(4, 5, 2, Direction.VERTICAL, 'P'),
            new Vehicle(5, 0, 2, Direction.HORIZONTAL, 'D'),
    };
    public static final Vehicle[] EXPERT_PUZZLE = new Vehicle[]{
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


    private Vehicle[] board;

    /**
     * Constructeur par défaut (problème: BEGINNER)
     */
    public RushHourState() {
        this.board = BEGINNER_PUZZLE;
    }

    /**
     * Constructeur avec un tableau à passer
     *
     * @param board Un tableau de vehicule
     */
    public RushHourState(Vehicle[] board) {
        this.board = board.clone();
    }

    public Vehicle[] getBoard() {
        return board;
    }

    /**
     * Copier l'état du jeu (en s'assurant qu'il n'y a pas d'effet de bord)
     *
     * @return State
     */
    @Override
    protected State cloneState() {
        Vehicle[] boardClone = new Vehicle[board.length];
        for (int i = 0; i < board.length; i++) {
            boardClone[i] = board[i].cloneVehicle();
        }
        return new RushHourState(boardClone);
    }

    /**
     * Vérifier si cet état est égal à un autre état (en regardant le tableau des vehicules)
     *
     * @param o Un autre état
     */
    @Override
    protected boolean equalsState(State o) {
        RushHourState state = (RushHourState) o;
        return Arrays.equals(board, state.getBoard());
    }

    @Override
    protected int hashState() {
        return Arrays.hashCode(board);
    }

    /**
     * Vérifier si un coup est légal pour une voiture et une action donnée
     *
     * @param v Un véhicule
     * @param a Une action
     * @return true ou false
     */
    public boolean isLegal(Vehicle v, Action a) {

        int[][] game = toGameArray();
        // Ces coordonnées correspondant à la position de la voiture
        // et evolueront par la direction qu'elles prennent
        // et l'action sera légal si ces coordonnées tombent sur une case vide
        int x = v.getX();
        int y = v.getY();

        // Si la voiture est horizontal, on avance que a gauche ou à droite
        if (v.getDirection() == Direction.HORIZONTAL) {
            if (a == RushHour.LEFT) {
                y = y - 1;
            } else if (a == RushHour.RIGHT) {
                y = y + v.getTaille();
            } else {
                // action non légale
                return false;
            }
            // Si la voiture est vertical, on avance en haut et en bas
        } else {

            if (a == RushHour.UP) {
                x = x - 1;
            } else if (a == RushHour.DOWN) {
                x = x + v.getTaille();
            } else {
                // action non légale
                return false;
            }
        }

        // verification que les coordonnées sont toujours dans le plateau de jeu
        if (x < 0 || y < 0 || x >= GAME_SIZE || y >= GAME_SIZE) {
            return false;
        }

        // si les coordonnées correspondent à une case vide, alors l'action est légal
        // l'action ne gène aucun autre vehicule (pas de collision)
        return game[x][y] == EMPTY;

    }


    /**
     * Méthode qui permet de transformer le tableau de véhicule
     * en un tableau à deux dimensions correspondant au plateau du jeu
     * avec le positionnement de chaque vehicule sur les cases du plateau
     *
     * @return int[][]
     */
    public int[][] toGameArray() {
        // on crée un plateau
        int[][] game = new int[GAME_SIZE][GAME_SIZE];

        // on le remplit de case vide
        for (int i = 0; i < GAME_SIZE; i++) {
            Arrays.fill(game[i], EMPTY);
        }

        // On veut placer le vehicule sur le plateau
        for (Vehicle vehicle : board) {

            for (int i = 0; i < vehicle.getTaille(); i++) {

                if (vehicle.getDirection() == Direction.HORIZONTAL) {
                    game[vehicle.getX()][vehicle.getY() + i] = vehicle.getColor();
                } else {
                    game[vehicle.getX() + i][vehicle.getY()] = vehicle.getColor();
                }

            }

        }

        return game;
    }

    /**
     * Permet d'afficher une visualisation de ce jeu
     */
    public String toString() {

        int[][] game = toGameArray();

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
        for (Vehicle v : board) {
            if (v.getColor() == c) {
                return v;
            }
        }
        return null;
    }

    /**
     * Methode heuristique
     */
    public double getHeuristic() {
        return manathanDistance();
    }

    // Calcule la distance entre le puzzle et la solution
    // https://en.wikipedia.org/wiki/Taxicab_geometry
    private double manathanDistance() {
        double result = 0;
        Vehicle locCurr = getVehicleByColor("R");
        result += Math.abs(5 - locCurr.getX());
        return result;
    }

}
