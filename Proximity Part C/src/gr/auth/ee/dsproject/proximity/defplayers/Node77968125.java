package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.ArrayList;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

/**
 * The Class Node77968125 implements a Node for the MinMax tree. *
 *
 * @author Orestis Floros.
 * @author Ioanna Gartzonika.
 */
public class Node77968125 {

    /**
     * Calculate the risk for a tile.
     *
     * @param tile
     *            The tile to calculate risk for.
     * @param board
     *            The current board
     * @param nextEnemies
     *            The list with the next enemies' scores.
     * @return a double which represents the "safety" of the move.
     */
    private static double calculateRisk(final Tile tile, final Board board,
            final ArrayList<Integer> nextEnemies) {
        final double scoreTile = tile.getScore();

        final double emptyNeighbors = countEmptyNeighbors(tile, board);
        if (emptyNeighbors < 1) {
            return 0.0;
        }

        for (int i = 0; i < nextEnemies.size(); i++) {
            if (nextEnemies.get(i) > scoreTile) {
                final double movesAfter = i + 1;
                final double risk = (scoreTile / (movesAfter)) * Math.sqrt(emptyNeighbors);
                return risk;
            }
        }
        return 0;
    }

    /**
     * Count empty neighbors.
     *
     * @param tile
     *            The tile
     * @param board
     *            The board
     * @return The number of empty neighbors.
     */
    static int countEmptyNeighbors(final Tile tile, final Board board) {
        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        return countEmptyNeighbors(neighbors);
    }

    /**
     * Count empty neighbors.
     *
     * @param tile
     *            The tile
     * @param board
     *            The board
     * @return The number of empty neighbors.
     */
    static int countEmptyNeighbors(final Tile[] neighbors) {
        int emptyNeighbors = 0;
        for (final Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() == 0) {
                emptyNeighbors++;
            }
        }
        return emptyNeighbors;
    }

    /** The parent node. */
    Node77968125 parent;

    /** The list with the children nodes.. */
    ArrayList<Node77968125> children;

    /**
     * The id of the player. Needed to use the correct sign for the evaluation .
     */
    private int id;

    /** The opponent's id. */
    private int opponentId;

    /** The node depth. */
    int nodeDepth;

    /** The node move. */
    int[] nodeMove;

    /** The node board. */
    Board nodeBoard;

    /** The node evaluation. */
    double nodeEvaluation;

    /**
     * Instantiates a new node.
     */
    public Node77968125() {
        id = opponentId = nodeDepth = -1;
        nodeEvaluation = Double.NaN;
    }

    /**
     * Instantiates a new node. Mainly used for the root node of a tree.
     *
     * @param nodeBoard
     *            The node's board
     * @param id
     *            The player's id.
     */
    public Node77968125(final Board nodeBoard, final int id) {
        // constructor used for the root Node
        this(null, 0, null, nodeBoard);
        this.id = id;
        this.opponentId = MinMaxPlayer.reverseId(id);
    }

    /**
     * Instantiates a new node.
     *
     * @param parent
     *            The parent node.
     * @param nodeDepth
     *            The node depth.
     * @param nodeMove
     *            The node move.
     * @param nodeBoard
     *            The node board.
     */
    public Node77968125(final Node77968125 parent, final int nodeDepth, final int[] nodeMove,
            final Board nodeBoard) {
        // arguments of constructor
        this.parent = parent;
        this.nodeDepth = nodeDepth;
        this.nodeMove = nodeMove;
        this.nodeBoard = nodeBoard;

        // initialize children.
        this.children = new ArrayList<Node77968125>();

        if (parent != null) {
            // All nodes that are used by a player should share the same ids.
            this.id = parent.getId();
            this.opponentId = parent.getOpponentId();
        }
    }

    /**
     * Add a child.
     *
     * @param child
     *            The child to add.
     */
    void addChild(final Node77968125 child) {
        children.add(child);
    }

    /**
     * Evaluate the current node and update nodeEvaluation.
     */
    void evaluate() {
        nodeEvaluation = 0;
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
                if ((idxStart % 2 == 1 && isEnemys) || (idxStart % 2 == 0 && isOurs)) {
                    idxStart++;
                }
                for (int nextTenIdx = idxStart; nextTenIdx < nextTen.length; nextTenIdx += 2) {
                    nextEnemies.add(nextTen[nextTenIdx]);
                }
                final double risk = calculateRisk(tile, nodeBoard, nextEnemies);

                nodeEvaluation += (tileScore - risk) * (isOurs ? 1 : -1);
            }
        }
    }

    /**
     * Gets the children.
     *
     * @return the children
     */
    public ArrayList<Node77968125> getChildren() {
        return children;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the node board.
     *
     * @return the nodeBoard
     */
    public Board getNodeBoard() {
        return nodeBoard;
    }

    /**
     * Gets the node depth.
     *
     * @return the nodeDepth
     */
    public int getNodeDepth() {
        return nodeDepth;
    }

    /**
     * Gets the node evaluation.
     *
     * @return the nodeEvaluation
     */
    public double getNodeEvaluation() {
        return nodeEvaluation;
    }

    /**
     * Gets the node move.
     *
     * @return the nodeMove
     */
    public int[] getNodeMove() {
        return nodeMove;
    }

    /**
     * Gets the opponent id.
     *
     * @return the opponentId
     */
    public int getOpponentId() {
        return opponentId;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public Node77968125 getParent() {
        return parent;
    }

    /**
     * Sets the node evaluation.
     *
     * @param nodeEvaluation
     *            the nodeEvaluation to set
     */
    public void setNodeEvaluation(final double nodeEvaluation) {
        this.nodeEvaluation = nodeEvaluation;
    }
}
