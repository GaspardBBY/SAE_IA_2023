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

    private int MAX_DEPTH;

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
     * <p>Retourne un coup minmax</p>
     */
    public Action getMove(GameState state) {


        ActionValuePair actionValue;
        // Profondeur maximum de 6
        MAX_DEPTH = 6;

        // Tant que l'action n' apas été choisi, on continue à trouver le meilleur coup

        // Remarque: Il faut diminuer la profondeur à chaque itération, car si l'arbre de décision
        // voit la même issue (gagnant pour un joueur peu importe l'action) alors, il ne peut pas
        // trouver l'action. Mais en diminuant la profondeur, on limite sa vision et ainsi il peut
        // décider de jouer un coup.
        do {

            if (state.getPlayerToMove() == 'X') {
                actionValue = getMaxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            } else {
                actionValue = getMinValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
            }

            MAX_DEPTH--;

        } while (actionValue.getAction() == null);

        return actionValue.getAction();
    }

    /**
     * Selectionne le meilleur coup maximale
     */
    public ActionValuePair getMaxValue(GameState state, double alpha, double beta, int depth) {

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

            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair minVal = getMinValue(nextState, alpha, beta, depth);

            if (minVal.getValue() > vMax) {
                vMax = minVal.getValue();
                cMax = action;
            }

            if (vMax > alpha) {
                alpha = vMax;
            }

            if (vMax >= beta) {
                break;
            }

        }

        return new ActionValuePair(cMax, vMax);

    }

    /**
     * Selectionne le meilleur coup minimale
     */
    public ActionValuePair getMinValue(GameState state, double alpha, double beta, int depth) {

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

            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair maxVal = getMaxValue(nextState, alpha, beta, depth);

            if (maxVal.getValue() < vMin) {
                vMin = maxVal.getValue();
                cMin = action;
            }

            if (vMin < beta) {
                beta = vMin;
            }

            if (vMin <= alpha) {
                break;
            }

        }

        return new ActionValuePair(cMin, vMin);
    }


}
