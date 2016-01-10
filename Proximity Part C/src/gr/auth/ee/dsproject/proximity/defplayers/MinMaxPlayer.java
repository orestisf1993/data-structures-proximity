package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.ArrayList;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

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

    public void createMySubTree(final Node parent, final int depth, final int randomNumber) {
        // - Find the empty tile spots of the board of the parent.
        // - For each empty tile spot on the board:
        // ----- Create a clone of the parent node’s board and simulate putting
        // a tile on this spot by using boardAfterMove() on the parent node
        // board.
        // ----- Create a new node as child of the parent node using new board
        // state.
        // ----- Add the node as child of the parent node.
        // ----- Complete the tree branches by calling
        // ----- createOpponentSubtree(newNode, depth+1)

        Board board = parent.getNodeBoard();
        ArrayList<Tile> emptyTiles = findEmptyTiles(board);

        for (Tile emptyTile : emptyTiles) {
            // Get needed values for Node() call.
            int x = emptyTile.getX();
            int y = emptyTile.getY();
            int s = randomNumber;
            int[] move = new int[] { x, y };
            Board nextBoard = ProximityUtilities.boardAfterMove(id, board, x, y, s);

            // Create the new node.
            Node newNode = new Node(parent, depth + 1, move, nextBoard);

            // Add it as a child of parent node so they are connected.
            ArrayList<Node> children = parent.getChildren();
            children.add(newNode);
            parent.setChildren(children);

            // Add opponent's branches.
            createOpponentSubtree(newNode, depth + 1);
        }
    }

    public void createOpponentSubtree(final Node parent, final int depth) {
        Board board = parent.getNodeBoard();
        ArrayList<Tile> emptyTiles = findEmptyTiles(board);

        for (Tile emptyTile : emptyTiles) {
            // Get needed values for Node() call.
            int x = emptyTile.getX();
            int y = emptyTile.getY();
            int s = (int) ProximityUtilities.calculateMedianForPool(board.getOpponentsPool());
            int[] move = new int[] { x, y };
            Board nextBoard = ProximityUtilities.boardAfterMove(id, board, x, y, s);

            // Create the new node.
            Node newNode = new Node(parent, depth + 1, move, nextBoard);

            // Add it as a child of parent node so they are connected.
            ArrayList<Node> children = parent.getChildren();
            children.add(newNode);
            parent.setChildren(children);
        }
    }

    private ArrayList<Tile> findEmptyTiles(Board board) {
        ArrayList<Tile> emptyTiles = new ArrayList<Tile>();
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_ROWS; j++) {
                Tile tile = board.getTile(i, j);
                if (tile.getPlayerId() == 0) {
                    emptyTiles.add(tile);
                }
            }
        }
        return emptyTiles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        Node root = new Node(board);
        // create a tree of depth 2.
        createMySubTree(root, 1, randomNumber);
        Node nextMove = chooseMinMaxMove(root);
        return nextMove.getNodeMove();
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
