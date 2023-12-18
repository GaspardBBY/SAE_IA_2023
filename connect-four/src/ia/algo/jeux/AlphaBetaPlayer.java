package ia.algo.jeux;


import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.common.ArgParse;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Définie un joueur AlphaBeta
 */

public class AlphaBetaPlayer extends Player {

    public int nbEtats = 0;
    private int MAX_DEPTH;
    private String graphviz = "";
    private int showDepth = 2;

    /**
     * Crée un joueur AlphaBeta
     *
     * @param g  l'instance du jeux
     * @param p1 vrai si joueur 1
     */
    public AlphaBetaPlayer(Game g, boolean p1) {
        super(g, p1);
        name = "AlphaBeta";
    }


    /**
     * {@inheritDoc}
     * <p>Retourn un coup minmax</p>
     */
    public Action getMove(GameState state) {

        nbEtats = 0;
        ActionValuePair av;

        MAX_DEPTH = 6;

        do {
            NameUtils.reset();
            graphviz = "digraph MiniMaxTree {\n" +
                    "  node [shape=ellipse];\n";
            graphviz += "root [label=\"root\"];\n";

            System.out.println("player=" + state.getPlayerToMove());
            if (state.getPlayerToMove() == 'X') {
                av = getMaxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, "root");
            } else {
                av = getMinValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, "root");
            }
            System.out.println("max depth: " + MAX_DEPTH);
            MAX_DEPTH--;
            if (MAX_DEPTH < 0) {
                String t = state.getPlayerToMove() == 'X' ? "Joueur 1" : "Joueur 2";
                System.err.println(t + " : Bien joué, tu as gagné, j'ai calculé toutes les possibilités, j'ai perdu.");
                break;
            }
        } while (av.getAction() == null);

        System.out.println("Nombre d'états calculés: " + nbEtats);
        graphviz += "}";

        Action action = av.getAction();
        System.out.println("Valeur action: " + av.getValue());

        try (FileWriter fileWriter = new FileWriter("input.dot")) {
            fileWriter.write(graphviz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return action;
    }

    public ActionValuePair getMaxValue(GameState state, double alpha, double beta, int depth, String nodeName) {

        nbEtats++;

        if (game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        }

        if (depth > MAX_DEPTH) {
            return new ActionValuePair(null, state.getHeuristic());
        }

        double vMax = Integer.MIN_VALUE;
        Action cMax = null;

        depth++;

        for (Action action : game.getActions(state)) {

            String nextNodeName = NameUtils.next();

            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair minVal = getMinValue(nextState, alpha, beta, depth, nextNodeName);

            if (ArgParse.DEBUG && depth <= showDepth) {
                graphviz += nextNodeName + " [label=\"" + action.getName() + " (MAX;" + minVal.getValue() + ")\"";
            }

            if (minVal.getValue() > vMax) {
                vMax = minVal.getValue();
                cMax = action;
                if (ArgParse.DEBUG && depth <= showDepth) {
                    graphviz += " color=green";
                }
            }

            if (vMax > alpha) {
                alpha = vMax;
            }

            if (ArgParse.DEBUG && depth <= showDepth) {
                graphviz += "];\n" + nodeName + " -> " + nextNodeName + ";\n";
            }

            if (vMax >= beta) {
                break;
            }

        }

        return new ActionValuePair(cMax, vMax);

    }

    public ActionValuePair getMinValue(GameState state, double alpha, double beta, int depth, String nodeName) {

        nbEtats++;

        if (game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        }

        if (depth > MAX_DEPTH) {
            return new ActionValuePair(null, state.getHeuristic());
        }

        double vMin = Integer.MAX_VALUE;
        Action cMin = null;

        depth++;

        for (Action action : game.getActions(state)) {

            String nextNodeName = NameUtils.next();

            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair maxVal = getMaxValue(nextState, alpha, beta, depth, nextNodeName);

            if (ArgParse.DEBUG && depth <= showDepth) {
                graphviz += nextNodeName + " [label=\"" + action.getName() + " (MIN;" + maxVal.getValue() + ")\"";
            }

            if (maxVal.getValue() < vMin) {
                vMin = maxVal.getValue();
                cMin = action;
                if (ArgParse.DEBUG && depth <= showDepth) {
                    graphviz += " color=red";
                }
            }

            if (vMin < beta) {
                beta = vMin;
            }

            if (ArgParse.DEBUG && depth <= showDepth) {
                graphviz += "];\n" + nodeName + " -> " + nextNodeName + ";\n";
            }

            if (vMin <= alpha) {
                break;
            }

        }

        return new ActionValuePair(cMin, vMin);
    }


}
