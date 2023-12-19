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

    /**
     * Il y a 4 actions possible dans ce jeu : HAUT, BAS, GAUCHE, DROITE
     * Au lieu d'écrire à la main toutes les actions par voiture, on sélectionne plutôt
     * toutes les actions autorisée pour chaque voiture, et on les ajoutes à la liste d'actions
     * On utilise le nom de l'aciton pour connaître l'action ET la voiture en question
     */
    @Override
    public ArrayList<Action> getActions(State s) {
        RushHourState state = (RushHourState) s;

        ArrayList<Action> actions = new ArrayList<>();
        for (Vehicle vehicle : state.getBoard()) {
            for (Action action : ACTIONS) {
                if (state.isLegal(vehicle, action)) {
                    Action vehicleAction = new Action(action.getName() + " " + vehicle.getColor());
                    actions.add(vehicleAction);
                }
            }
        }

        return actions;
    }

    /**
     * Une méthode pour pouvoir récupérer une action en fonction du nom
     * @param name Le nom de l'action
     * @return Action
     */
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


    /**
     * Exécuter l'action à un état de jeu
     * @param s Un état
     * @param a Une action
     * @return Le nouvelle état où l'action a été appliqué
     */
    @Override
    public State doAction(State s, Action a) {

        // on copie l'état courant et on le modifie
        RushHourState state = (RushHourState) s.clone();

        String[] actionSplit = a.getName().split(" ");

        Action direction = getActionByName(actionSplit[0]); // Le nom de l'action
        Vehicle vehicle = state.getVehicleByColor(actionSplit[1]); // Le nom du véhicule

        // vérification pour éviter les soucis avec les voitures non trouvés
        if (vehicle == null) {
            throw new NullPointerException("Couleur du vehicule non trouvé");
        }

        // on applique l'action au vehicule
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

    /**
     * Si la voiture rouge se trouve à la deuxième ligne et à la 4ème colonne ou au delà
     * Alors la partie est terminée, le chemin est libre pour la voiture rouge.
     */
    @Override
    public boolean isGoalState(State s) {
        RushHourState state = (RushHourState) s;
        Vehicle red = state.getVehicleByColor("R");
        return red.getX() == 2 && red.getY() >= 4;
    }

    /**
     * Le cout de chaque action
     */
    @Override
    public double getActionCost(State s, Action a) {
        return 1.0;
    }
}
