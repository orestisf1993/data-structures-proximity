package gr.auth.ee.dsproject.proximity.defplayers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class MinMaxPlayer implements AbstractPlayer {
    private final static Logger logger = Logger.getLogger("log");

    private int score;
    private int id;
    private int opponentId;
    private String name;
    private int numOfTiles;
    final private int MAX_DEPTH = 2;

    public MinMaxPlayer(final Integer pid) {
        id = pid;
        opponentId = id == 1 ? 2 : 1;
        name = "MinMaxPlayerTeam2";

        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.log(Level.FINEST, "Created MinMaxPlayer");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[] chooseMinMaxMove(Node node) {
        ArrayList<Node> children = node.getChildren();
        if (children.isEmpty()) {
            node.evaluate();
            return node.getNodeMove();
        }
        Node bestNode = null;

        boolean minimizingPlayer = node.getNodeDepth() % 2 == 0;
        double bestValue = minimizingPlayer ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        for (Node child : children) {
            chooseMinMaxMove(child);
            double evaluation = child.getNodeEvaluation();
            boolean isBestValue = minimizingPlayer ? evaluation < bestValue
                    : evaluation > bestValue;
            if (isBestValue) {
                bestNode = child;
                bestValue = evaluation;
            }
        }
        node.setNodeEvaluation(bestValue);
        return bestNode.getNodeMove();
    }

    private void createSubTree(final Node parent) {
        Board board = parent.getNodeBoard();
        final int depth = parent.getNodeDepth() + 1;
        HashMap<Integer, Integer> pool = depth % 2 == 1 ? board.getMyPool()
                : board.getOpponentsPool();
        int s = (int) ProximityUtilities.calculateMedianForPool(pool);
        createSubTree(parent, s);
    }

    void createSubTree(final Node parent, final int s) {
        // Find the empty tile spots of the board of the parent.
        Board board = parent.getNodeBoard();
        ArrayList<Tile> emptyTiles = findEmptyTiles(board);
        final int depth = parent.getNodeDepth() + 1;
        final int nodeId = depth % 2 == 1 ? id : opponentId;

        logger.log(Level.FINEST, "Creating sub tree:" + "\ndepth: " + depth + "\nid: " + nodeId
                + "\nemptyTiles: " + emptyTiles.size());

        for (Tile emptyTile : emptyTiles) {
            // Get needed values for Node() and boardAfterMove() call.
            int x = emptyTile.getX();
            int y = emptyTile.getY();
            int[] move = new int[] { x, y };

            // Simulate putting a tile on this spot on the parent node board.
            Board nextBoard = ProximityUtilities.boardAfterMove(nodeId, board, x, y, s);

            // Create the new node.
            Node newNode = new Node(parent, depth, move, nextBoard);

            // Add the node as child of the parent node.
            ArrayList<Node> children = parent.getChildren();
            children.add(newNode);
            parent.setChildren(children);

            // Add opponent's branches.
            if (depth < MAX_DEPTH) {
                createSubTree(newNode);
            }
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
        createSubTree(root, randomNumber);
        return chooseMinMaxMove(root);
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
