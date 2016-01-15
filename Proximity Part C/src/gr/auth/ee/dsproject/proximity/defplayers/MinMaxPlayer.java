package gr.auth.ee.dsproject.proximity.defplayers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
    // final static Level loggerLevel = Level.ALL;
    final static Level loggerLevel = Level.OFF;
    private final static Logger logger = Logger.getLogger("log");

    // static initializer for logger
    static {
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

    // TODO: change to depth we want to use.
    final private static int MAX_DEPTH = 2;

    private static ArrayList<Tile> findEmptyTiles(Board board, int nextDepth) {
        ArrayList<Tile> emptyTiles = new ArrayList<Tile>();
        int loneTilesCount = 0;
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_ROWS; j++) {
                Tile tile = board.getTile(i, j);
                if (tile.getPlayerId() == 0) {
                    if (tileIsLone(tile, board) && (++loneTilesCount > MAX_DEPTH + 1 - nextDepth)) {
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

    static String printBoardToString(Board board) {
        // Create a stream to hold the output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        // IMPORTANT: Save the old System.out!
        PrintStream old = System.out;
        // Tell Java to use your special stream
        System.setOut(ps);
        // Print some output: goes to your special stream
        board.printBoard();
        // Put things back
        System.out.flush();
        System.setOut(old);
        // return what happened
        return baos.toString();
    }

    static boolean tileIsLone(Tile tile, Board board) {
        // TODO: prefer corner tiles
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
    }

    int[] chooseMinMaxMove(Node node) {
        ArrayList<Node> children = node.getChildren();
        if (children.isEmpty()) {
            node.evaluate();
            return node.getNodeMove();
        }

        // if node is ours, it's child will not be.
        boolean minimizingPlayer = nodeLevelIsOurs(node);
        Node bestNode = children.get(0);
        double bestValue = minimizingPlayer ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        for (Node child : children) {
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
        final int nextDepth = parent.getNodeDepth() + 1;
        final int nodeId = nodeLevelIsOurs(nextDepth) ? id : opponentId;
        final int s = nextNumbersToBePlayed[nextDepth - 1];
        ArrayList<Tile> emptyTiles = findEmptyTiles(board, nextDepth);

        for (Tile emptyTile : emptyTiles) {
            // Get needed values for Node() and boardAfterMove() call.
            int x = emptyTile.getX();
            int y = emptyTile.getY();
            int[] move = new int[] { x, y };

            // Simulate putting a tile on this spot on the parent node board.
            Board nextBoard = ProximityUtilities.boardAfterMove(nodeId, board, x, y, s);

            // Create the new node.
            Node newNode = new Node(parent, nextDepth, move, nextBoard);

            // Add the node as child of the parent node.
            parent.addChild(newNode);

            // Add opponent's branches.
            if (nextDepth < MAX_DEPTH) {
                createSubTree(newNode);
            }
        }
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getNextMove(final Board board, final int randomNumber) {
        nextNumbersToBePlayed = Board.getNextTenNumbersToBePlayed();
        assert (randomNumber == nextNumbersToBePlayed[0]);
        Node root = new Node(board, id);
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

    /**
     * @param id
     *            the id to set
     */
    public void setId(int id) {
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
