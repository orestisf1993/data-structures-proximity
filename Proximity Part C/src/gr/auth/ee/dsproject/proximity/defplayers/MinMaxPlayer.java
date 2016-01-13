package gr.auth.ee.dsproject.proximity.defplayers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class MinMaxPlayer implements AbstractPlayer {
    // TODO: delete logger for final version.
    private final static Logger logger = Logger.getLogger("log");
    final static Level loggerLevel = Level.WARNING;
    // TODO: change to depth we want to use.
    final private static int MAX_DEPTH = 2;

    private static ArrayList<Tile> findEmptyTiles(Board board) {
        ArrayList<Tile> emptyTiles = new ArrayList<Tile>();
        int loneTilesCount = 0;
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_ROWS; j++) {
                Tile tile = board.getTile(i, j);
                if (tile.getPlayerId() == 0) {

                    if (tileIsLone(tile, board) && (++loneTilesCount > MAX_DEPTH)) {
                        continue;
                    }

                    // finally add the tile.
                    emptyTiles.add(tile);
                }
            }
        }
        return emptyTiles;
    }

    static boolean nodeLevelIsOurs(int depth) {
        return depth % 2 == 1;
    }

    static boolean nodeLevelIsOurs(Node node) {
        int depth = node.getNodeDepth();
        return nodeLevelIsOurs(depth);
    }

    static boolean tileIsLone(Tile tile, Board board) {
        Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        boolean isLone = true;
        for (Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() != 0) {
                isLone = false;
                break;
            }
        }
        return isLone;
    }

    private int score;
    private int id;
    private int opponentId;
    private String name;
    private int numOfTiles;
    private int[] nextNumbersToBePlayed;

    public MinMaxPlayer(final Integer pid) {
        id = pid;
        opponentId = (id == 1) ? 2 : 1;
        name = "MinMaxPlayerTeam2";

        // logging stuff.
        logger.setLevel(loggerLevel);
        logger.setUseParentHandlers(false);
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("log.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.log(Level.FINE, "Created MinMaxPlayer");
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int[] chooseMinMaxMove(Node node) {
        ArrayList<Node> children = node.getChildren();
        if (children.isEmpty()) {
            node.evaluate();
            return node.getNodeMove();
        }

        boolean minimizingPlayer = node.getNodeDepth() % 2 == 1;
        Node bestNode = children.get(0);
        double bestValue = minimizingPlayer ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        for (Node child : children) {
            // if we are minimizing then it is an enemy's node.
            assert ((child.getId() == opponentId) == minimizingPlayer);

            chooseMinMaxMove(child);
            double evaluation = child.getNodeEvaluation();
            boolean isBestValue = minimizingPlayer ? evaluation < bestValue
                    : evaluation > bestValue;
            logger.log(Level.FINEST, evaluation + ":" + isBestValue + " at " + node.getNodeDepth());
            if (isBestValue) {
                bestNode = child;
                bestValue = evaluation;
            }
        }
        node.setNodeEvaluation(bestValue);
        return bestNode.getNodeMove();
    }

    void createSubTree(final Node parent) {
        // Find the empty tile spots of the board of the parent.
        Board board = parent.getNodeBoard();
        ArrayList<Tile> emptyTiles = findEmptyTiles(board);
        final int depth = parent.getNodeDepth() + 1;
        final int nodeId = depth % 2 == 1 ? id : opponentId;
        final int s = nextNumbersToBePlayed[parent.getNodeDepth()];

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
            Node newNode = new Node(parent, depth, move, nextBoard, s);

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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        nextNumbersToBePlayed = Board.getNextTenNumbersToBePlayed();
        assert (randomNumber == nextNumbersToBePlayed[0]);
        // opponentId so depth 1 gets our id.
        Node root = new Node(board, opponentId);
        // create a tree of depth MAX_DEPTH.
        createSubTree(root);
        int[] nextMove = chooseMinMaxMove(root);
        logger.log(Level.INFO, nextMove[0] + "," + nextMove[1]);
        return nextMove;
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
