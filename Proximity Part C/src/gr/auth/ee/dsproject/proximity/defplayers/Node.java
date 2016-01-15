package gr.auth.ee.dsproject.proximity.defplayers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

// TODO: rename to correct class name for assignment.
public class Node {
    private static Logger logger;

    // static initializer for logger
    static {
        if (logger == null) {
            logger = Logger.getLogger("Node");
            logger.setLevel(MinMaxPlayer.loggerLevel);
            logger.setUseParentHandlers(false);

            FileHandler fh;
            try {
                // This block configure the logger with handler and formatter
                fh = new FileHandler("node.log");
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param tile
     *            An object in the class Tile.
     * @param nodeBoard
     *            An object in the class Board.
     * @param randomNumber
     *            It is the randomNumber.
     * @return a double which represents the "safety" of the move.
     */
    private static double calculateRisk(final Tile tile, Board board,
            ArrayList<Integer> nextEnemies) {
        final double scoreTile = tile.getScore();

        double emptyNeighbors = countEmptyNeighbors(tile, board);
        if (emptyNeighbors < 1) {
            return 0.0;
        }

        // TODO replace 10 with static.
        for (int i = 0; i < nextEnemies.size(); i++) {
            if (nextEnemies.get(i) > scoreTile) {
                final double movesAfter = i + 1;
                final double risk = (scoreTile / (movesAfter)) * Math.sqrt(emptyNeighbors);
                return risk;
            }
        }
        return 0;
    }

    static int countEmptyNeighbors(Tile tile, Board board) {
        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        return countEmptyNeighbors(neighbors);
    }

    static int countEmptyNeighbors(Tile[] neighbors) {
        int emptyNeighbors = 0;
        for (final Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() == 0) {
                emptyNeighbors++;
            }
        }
        return emptyNeighbors;
    }

    Node parent;
    ArrayList<Node> children;
    private int id;
    private int opponentId;
    int nodeDepth;
    int[] nodeMove;
    Board nodeBoard;
    double nodeEvaluation;

    // TODO: fill
    public Node() {
    }

    /**
     * @param nodeBoard
     */
    public Node(Board nodeBoard, int id) {
        // constructor used for the root Node
        this(null, 0, null, nodeBoard);
        this.id = id;
        this.opponentId = (id == 1) ? 2 : 1;
    }

    /**
     * @param parent
     * @param nodeDepth
     * @param nodeMove
     * @param nodeBoard
     */
    public Node(Node parent, int nodeDepth, int[] nodeMove, Board nodeBoard) {
        // arguments of constructor
        this.parent = parent;
        this.nodeDepth = nodeDepth;
        this.nodeMove = nodeMove;
        this.nodeBoard = nodeBoard;

        // initialize children.
        this.children = new ArrayList<Node>();

        if (parent != null) {
            this.id = parent.getId();
            this.opponentId = parent.getOpponentId();
        }
    }

    void addChild(Node child) {
        children.add(child);
    }

    void evaluate() {
        double theirs = 0;
        double ours = 0;
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_ROWS; j++) {
                final Tile tile = nodeBoard.getTile(i, j);
                final int tileId = tile.getPlayerId();
                if (tileId == 0) {
                    continue;
                }
                final int tileScore = tile.getScore();
                final boolean isOurs = (tileId == id);
                final boolean isEnemys = (tileId == opponentId);
                final ArrayList<Integer> nextEnemies = new ArrayList<Integer>();
                final int[] nextTen = Board.getNextTenNumbersToBePlayed();

                int idxStart = nodeDepth;
                if (idxStart % 2 == 1 && isEnemys) {
                    idxStart++;
                }
                if (idxStart % 2 == 0 && isOurs) {
                    idxStart++;
                }
                for (int nextTenIdx = idxStart; nextTenIdx < nextTen.length; nextTenIdx += 2) {
                    nextEnemies.add(nextTen[nextTenIdx]);
                }
                final double risk = calculateRisk(tile, nodeBoard, nextEnemies);

                ours += isOurs ? tileScore - risk : 0;
                theirs += isEnemys ? tileScore - risk : 0;
            }
        }
        this.nodeEvaluation = ours - theirs;
    }

    /**
     * @return the children
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the nodeBoard
     */
    public Board getNodeBoard() {
        return nodeBoard;
    }

    /**
     * @return the nodeDepth
     */
    public int getNodeDepth() {
        return nodeDepth;
    }

    /**
     * @return the nodeEvaluation
     */
    public double getNodeEvaluation() {
        return nodeEvaluation;
    }

    /**
     * @return the nodeMove
     */
    public int[] getNodeMove() {
        return nodeMove;
    }

    /**
     * @return the opponentId
     */
    public int getOpponentId() {
        return opponentId;
    }

    /**
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @param nodeEvaluation
     *            the nodeEvaluation to set
     */
    public void setNodeEvaluation(double nodeEvaluation) {
        this.nodeEvaluation = nodeEvaluation;
    }
}
