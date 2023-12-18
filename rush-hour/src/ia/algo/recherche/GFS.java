package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

public class GFS extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public GFS(SearchProblem p, State s) {
        super(p, s);
        this.frontier = new ArrayList<>();
    }

    @Override
    public boolean solve() {

        SearchNode initialNode = SearchNode.makeRootSearchNode(intial_state);

        this.frontier = new ArrayList<>(Collections.singleton(initialNode));
        this.explored = new HashSet<>();

        while (!frontier.isEmpty()) {

            SearchNode selectedNode = frontier.get(0);
            frontier.remove(selectedNode);

            State selectedState = selectedNode.getState();

            if (ArgParse.DEBUG)
                System.out.print("[" + selectedState);

            if (problem.isGoalState(selectedState)) {
                end_node = selectedNode;
                return true;

            } else {
                explored.add(selectedState);

                ArrayList<Action> actions = problem.getActions(selectedState);

                for (Action a : actions) {
                    SearchNode childNode = SearchNode.makeChildSearchNode(problem, selectedNode, a);
                    State childState = childNode.getState();

                    if (ArgParse.DEBUG)
                        System.out.print(" + " + a + "] -> [" + childState);

                    if (!frontier.contains(childNode) && !explored.contains(childState)) {
                        frontier.add(childNode);
                    } else if (frontier.contains(childNode) && frontier.get(frontier.indexOf(childNode)).getCost() > childNode.getCost()) {
                        if (ArgParse.DEBUG)
                            System.out.print("remplacement noeud");
                        frontier.remove(childNode);
                        frontier.add(childNode);
                    }
                }

            }

            frontier.sort(Comparator.comparingDouble(SearchNode::getHeuristic));

            if (ArgParse.DEBUG)
                System.out.println("]");

        }

        return false;
    }

}
