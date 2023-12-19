package ia.framework.common;

import java.util.Arrays;

import ia.framework.recherche.*;
import ia.problemes.*;
import ia.algo.recherche.*;

/**
 * Quelques méthodes rudimentaires pour lire la ligne de commande
 * et lancer le Schmilblick
 */

public class ArgParse {

    /**
     * Pour afficher plus de chose
     */
    public static boolean DEBUG = false;

    /**
     * Stock le message d'aide
     */
    public static String msg = null;

    /**
     * spécifie le message d'aide
     */
    public static void setUsage(String m) {
        msg = m;
    }

    /**
     * Affiche un message d'utilisation
     */
    public static void usage() {
        System.err.println(msg);
    }

    /**
     * Retourne la valeur d'un champ demandé
     *
     * @param args Le tableau de la ligne de commande
     * @param arg  Le paramètre qui nous intéresse
     * @return La valeur du paramètre
     */
    public static String getArgFromCmd(String[] args, String arg) {
        if (args.length > 0) {
            int idx = Arrays.asList(args).indexOf(arg);
            if (idx % 2 == 0 && idx < args.length - 1)
                return args[idx + 1];
            if (idx < 0)
                return null;
            usage();
        }
        return null;

    }

    /**
     * Pour vérifier l'existence d'une option donnée
     *
     * @param args Le tableau de la ligne de commande
     * @param arg  L'option qui nous intéresse
     * @return vrai si l'option existe
     */
    public static boolean getFlagFromCmd(String[] args, String arg) {
        int idx = Arrays.asList(args).indexOf(arg);
        if (idx >= 0)
            return true;
        return false;

    }

    /**
     * Retourne le nom problème choisi
     *
     * @param args Le tableau de la ligne de commande
     * @return le nom du problème ou null
     */
    public static String getProblemFromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-prob");
    }

    /**
     * Retourne le nom du jeux choisi
     *
     * @param args Le tableau de la ligne de commande
     * @return le nom du jeux ou null
     */
    public static String getGameFromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-game");
    }

    /**
     * Retourne le nom l'algorithme choisi
     *
     * @param args Le tableau de la ligne de commande
     * @return le nom de l'algorithme ou null
     */
    public static String getAlgoFromCmd(String[] args) {
        handleFlags(args);

        return getArgFromCmd(args, "-algo");
    }

    /**
     * Retourne le type joueur 1
     *
     * @param args Le tableau de la ligne de commande
     * @return le jeureur 1  ou null
     */
    public static String getPlayer1FromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-p1");
    }

    /**
     * Retourne le type joueur 2
     *
     * @param args Le tableau de la ligne de commande
     * @return le jeureur 2  ou null
     */
    public static String getPlayer2FromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-p2");
    }

    /**
     * Traitement des options -v, -h
     *
     * @param args Le tableau de la ligne de commande
     */
    public static void handleFlags(String[] args) {
        DEBUG = getFlagFromCmd(args, "-v");
        if (getFlagFromCmd(args, "-h")) {
            usage();
            System.exit(0);
        }
    }


    /**
     * Factory qui retourne une instance du problème choisie ou celui par défaut
     *
     * @param prob Le nom du problème ou null
     * @return Une instance du problème
     */

    public static SearchProblem makeProblem(String prob) {
        return new RushHour();
    }


    /**
     * Factory qui retourne une instance de l'algorithme choisi ou celui par défaut
     *
     * @param algo Le nom de l'algorithme ou null
     * @param p    Le problème a résoudre
     * @param s    L'état initial
     * @return Une instance de l'algorithme
     */
    public static TreeSearch makeAlgo(String algo,
                                      SearchProblem p,
                                      State s) {
        if (algo == null)
            algo = "rnd";
        switch (algo) {
            case "rnd":
                return new RandomSearch(p, s);
            case "bfs":
                return new BFS(p, s);
            case "dfs":
                return new DFS(p, s);
            case "ucs":
                return new UCS(p, s);
            case "gfs":
                return new GFS(p, s);
            case "astar":
                return new AStar(p, s);
            default:
                System.out.println("Algorithme inconnu");
                usage();
                System.exit(1);

        }
        return null;  // inatteignable, faire plaisir a javac
    }

    /**
     * Factory qui retourne une instance de l'état initial du problème choisi
     *
     * @param prob Le nom du problème ou null
     * @return L'état initial qui peut être fixé ou généré aléatoirement
     */
    public static State makeInitialState(String prob) {
        if (prob == null)
            prob = "rush-beginner";
        switch (prob) {
            case "rush-beginner":
                return new RushHourState(RushHourState.BEGINNER_PUZZLE);
            case "rush-intermediate":
                return new RushHourState(RushHourState.INTERMEDIATE_PUZZLE);
            case "rush-advanced":
                return new RushHourState(RushHourState.ADVANCED_PUZZLE);
            case "rush-expert":
                return new RushHourState(RushHourState.EXPERT_PUZZLE);
        }
        return null;
    }
}


