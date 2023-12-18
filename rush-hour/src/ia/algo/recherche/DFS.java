package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class DFS extends TreeSearch {

    private static int MAX_DEPTH = 1;

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public DFS(SearchProblem p, State s) {
        super(p, s);
        this.frontier = new ArrayList<>();
    }

    @Override
    public boolean solve() {
        boolean done;
        do {
            done = solveDFS(MAX_DEPTH++);
        } while (!done);

        return done;

    }

    public boolean solveDFS(int maxDepth) {

        SearchNode initialNode = SearchNode.makeRootSearchNode(intial_state);

        this.frontier = new ArrayList<>(Collections.singleton(initialNode));
        this.explored = new HashSet<>();

        while (!frontier.isEmpty()) {

            System.out.println(frontier);
            SearchNode selectedNode = frontier.get(frontier.size() - 1);
            frontier.remove(selectedNode);

            if (selectedNode.getDepth() <= maxDepth) {

                State selectedState = selectedNode.getState();

                if (ArgParse.DEBUG)
                    System.out.print("[");

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
                        }
                    }

                }

                if (ArgParse.DEBUG)
                    System.out.println("]");
            }

        }

        return false;

    }

}
