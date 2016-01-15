package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.ArrayList;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

public class Node77968125 {
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

    static int countEmptyNeighbors(final Tile tile, final Board board) {
        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        return countEmptyNeighbors(neighbors);
    }

    static int countEmptyNeighbors(final Tile[] neighbors) {
        int emptyNeighbors = 0;
        for (final Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() == 0) {
                emptyNeighbors++;
            }
        }
        return emptyNeighbors;
    }

    Node77968125 parent;
    ArrayList<Node77968125> children;
    private int id;
    private int opponentId;
    int nodeDepth;
    int[] nodeMove;
    Board nodeBoard;
    double nodeEvaluation;

    public Node77968125() {
        id = opponentId = nodeDepth = -1;
        nodeEvaluation = Double.NaN;
    }

    /**
     * @param nodeBoard
     */
    public Node77968125(final Board nodeBoard, final int id) {
        // constructor used for the root Node
        this(null, 0, null, nodeBoard);
        this.id = id;
        this.opponentId = MinMaxPlayer.reverseId(id);
    }

    /**
     * @param parent
     * @param nodeDepth
     * @param nodeMove
     * @param nodeBoard
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

    void addChild(final Node77968125 child) {
        children.add(child);
    }

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
     * @return the children
     */
    public ArrayList<Node77968125> getChildren() {
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
    public Node77968125 getParent() {
        return parent;
    }

    /**
     * @param nodeEvaluation
     *            the nodeEvaluation to set
     */
    public void setNodeEvaluation(final double nodeEvaluation) {
        this.nodeEvaluation = nodeEvaluation;
    }
}
