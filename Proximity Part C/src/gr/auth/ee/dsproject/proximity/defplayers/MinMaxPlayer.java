package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.ArrayList;

import gr.auth.ee.dsproject.proximity.board.Board;

public class MinMaxPlayer implements AbstractPlayer {

    int score;
    int id;
    String name;
    int numOfTiles;

    public MinMaxPlayer(final Integer pid) {
        id = pid;
        name = "MinMaxPlayerTeam2";
    }

    private Node chooseMinMaxMove(Node node) {
        ArrayList<Node> children = node.getChildren();
        if (children == null) {
            node.evaluate();
            return node;
        }
        int depth = node.getNodeDepth();
        double bestValue = 0;
        // min
        if (depth % 2 == 1) {
            bestValue = Double.POSITIVE_INFINITY;
            for (Node child : children) {
                Node result = chooseMinMaxMove(child);
                double evaluation = result.getNodeEvaluation();
                if (evaluation < bestValue) {
                    bestValue = evaluation;
                }
            }
        }
        // max
        else {
            bestValue = Double.NEGATIVE_INFINITY;
            for (Node child : children) {
                Node result = chooseMinMaxMove(child);
                double evaluation = result.getNodeEvaluation();
                if (evaluation > bestValue) {
                    bestValue = evaluation;
                }
            }

        }

        node.setNodeEvaluation(bestValue);
        return node;
    }

    public void createMySubTree(final Node parent, final int depth) {

    }

    public void createOpponentSubtree(final Node parent, final int depth) {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        return null;
    }

    public int getNumOfTiles() {
        return numOfTiles;
    }

    public int getScore() {
        return score;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;

    }

    public void setNumOfTiles(final int tiles) {
        numOfTiles = tiles;
    }

    public void setScore(final int score) {
        this.score = score;
    }
}
