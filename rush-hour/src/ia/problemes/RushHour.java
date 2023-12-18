package ia.problemes;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.recherche.SearchProblem;

import java.util.ArrayList;

public class RushHour extends SearchProblem {

    public static final Action UP = new Action("Up");
    public static final Action LEFT = new Action("Left");
    public static final Action DOWN = new Action("Down");
    public static final Action RIGHT = new Action("Right");

    public RushHour() {

        // La liste des actions possibles
        ACTIONS = new Action[]{UP, LEFT, DOWN, RIGHT};
    }

    @Override
    public ArrayList<Action> getActions(State s) {
        RushHourState state = (RushHourState) s;

        ArrayList<Action> actions = new ArrayList<>();
        for (RushHourState.Vehicle vehicle : state.getBoard()) {
            for (Action a : ACTIONS) {
                if (state.isLegal(vehicle, a)) {
                    Action vehicleAction = new Action(a.getName() + " " + vehicle.getColor());
                    actions.add(vehicleAction);
                }
            }
        }

        return actions;
    }

    public Action getActionByName(String name) {
        if (name.equalsIgnoreCase(LEFT.getName())) {
            return LEFT;
        } else if (name.equalsIgnoreCase(RIGHT.getName())) {
            return RIGHT;
        } else if (name.equalsIgnoreCase(UP.getName())) {
            return UP;
        } else {
            return DOWN;
        }
    }



    @Override
    public State doAction(State s, Action a) {

        // on copie l'état courent et on le modifie
        RushHourState state = (RushHourState) s.clone();

        String[] t = a.getName().split(" ");

        Action direction = getActionByName(t[0]);
        RushHourState.Vehicle vehicle = state.getVehicleByColor(t[1]);

        if (vehicle == null) {
            throw new NullPointerException("Couleur du vehicule non trouvé");
        }

        if (direction == LEFT)
            vehicle.moveLeft();
        else if (direction == RIGHT)
            vehicle.moveRight();
        else if (direction == UP)
            vehicle.moveUp();
        else if (direction == DOWN)
            vehicle.moveDown();
        else
            throw new IllegalArgumentException("Invalid" + a);

        return state;
    }

    @Override
    public boolean isGoalState(State s) {
        RushHourState state = (RushHourState) s;
        RushHourState.Vehicle red = state.getVehicleByColor("R");
        return red.getX() == 2 && red.getY() >= 4;
    }

    @Override
    public double getActionCost(State s, Action a) {
        return 1.0;
    }
}
