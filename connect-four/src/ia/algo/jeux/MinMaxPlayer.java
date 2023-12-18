package ia.algo.jeux;


import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.common.ArgParse;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

/**
 * Définie un joueur MinMax
 */

public class MinMaxPlayer extends Player {

    public int nbEtats = 0;

    /**
     * Crée un joueur MinMax
     *
     * @param g  l'instance du jeux
     * @param p1 vrai si joueur 1
     */
    public MinMaxPlayer(Game g, boolean p1) {
        super(g, p1);
        name = "MinMax";
    }


    /**
     * {@inheritDoc}
     * <p>Retourn un coup minmax</p>
     */
    public Action getMove(GameState state) {
        nbEtats = 0;
        Action action;
        if (state.getPlayerToMove() == PLAYER1) {
            action = getMaxValue(state).getAction();
        } else {
            action = getMinValue(state).getAction();
        }
        System.out.println("Nombre d'états calculés: " + nbEtats);
        return action;
    }

    public ActionValuePair getMaxValue(GameState state) {
        nbEtats++;
        if (game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMax = Double.MIN_VALUE;
        Action cMax = null;

        for (Action action : game.getActions(state)) {

            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair minVal = getMinValue(nextState);

            if (minVal.getValue() > vMax) {
                vMax = minVal.getValue();
                cMax = action;
            }

        }

        return new ActionValuePair(cMax, vMax);

    }

    public ActionValuePair getMinValue(GameState state) {
        nbEtats++;
        if (game.endOfGame(state)) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double vMin = Double.MAX_VALUE;
        Action cMin = null;

        for (Action action : game.getActions(state)) {
            GameState nextState = (GameState) game.doAction(state, action);
            ActionValuePair maxVal = getMaxValue(nextState);

            if (maxVal.getValue() < vMin) {
                vMin = maxVal.getValue();
                cMin = action;
            }

        }

        return new ActionValuePair(cMin, vMin);
    }


}
