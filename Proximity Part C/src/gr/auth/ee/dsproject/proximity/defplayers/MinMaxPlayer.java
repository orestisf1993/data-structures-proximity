package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.ArrayList;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

/**
 * The Class MinMaxPlayer implements a player that chooses the next move using
 * the MinMax algorithm.
 *
 * @author Orestis Floros.
 * @author Ioanna Gartzonika.
 */
public class MinMaxPlayer implements AbstractPlayer {

    /** The max depth of our tree. */
    final private static int MAX_DEPTH = 2;

    /**
     * Find empty tiles.
     *
     * @param board
     *            The current board.
     * @param nextDepth
     *            The depth for which we are searching the empty tiles.
     * @return ArrayList with the empty tiles in the board.
     */
    private static ArrayList<Tile> findEmptyTiles(final Board board, final int nextDepth) {
        // TODO: prefer corner tiles
        final ArrayList<Tile> emptyTiles = new ArrayList<Tile>();
        int loneTilesCount = 0;
        for (int i = 0; i < ProximityUtilities.NUMBER_OF_COLUMNS; i++) {
            for (int j = 0; j < ProximityUtilities.NUMBER_OF_ROWS; j++) {
                final Tile tile = board.getTile(i, j);
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

    /**
     * Check if node level is ours.
     *
     * @param depth
     *            the depth
     * @return true if the depth is odd (ours), otherwise false.
     */
    static boolean nodeLevelIsOurs(final int depth) {
        return depth % 2 == 1;
    }

    /**
     * Check if node level is ours.
     *
     * @param node
     *            the node
     * @return true if the depth of the node is odd (ours), otherwise false.
     */
    static boolean nodeLevelIsOurs(final Node77968125 node) {
        final int depth = node.getNodeDepth();
        return nodeLevelIsOurs(depth);
    }

    /**
     * Reverse id.
     *
     * @param id
     *            The id to reverse.
     * @return The reversed id
     */
    static int reverseId(final int id) {
        if (id != 1 && id != 2) {
            return id;
        } else {
            return (id == 1) ? 2 : 1;
        }
    }

    /**
     * check if tile is lone.
     *
     * @param tile
     *            The tile to check.
     * @param board
     *            The current board.
     * @return true when the tile doesn't have any non-empty neighbors,
     *         otherwise false.
     */
    static boolean tileIsLone(final Tile tile, final Board board) {
        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        for (final Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() != 0) {
                return false;
            }
        }
        return true;
    }

    /** The player's score. */
    private int score;

    /** The player's id. */
    private int id;

    /** The opponent's id. */
    private int opponentId;

    /** The name of the player. */
    private String name;

    /** The number of tiles. */
    private int numOfTiles;

    /** The next numbers to be played. */
    private int[] nextNumbersToBePlayed;

    /**
     * Instantiates a new min max player.
     *
     * @param pid
     *            the pid
     */
    public MinMaxPlayer(final Integer pid) {
        id = pid;
        opponentId = reverseId(id);
        name = "MinMaxPlayerTeam2";
    }

    /**
     * Choose a move using MinMax.
     *
     * @param node
     *            The parent node.
     * @return a matrix with the coordinates of the best move.
     */
    int[] chooseMinMaxMove(final Node77968125 node) {
        final ArrayList<Node77968125> children = node.getChildren();
        if (children.isEmpty()) {
            node.evaluate();
            return node.getNodeMove();
        }

        // if node is ours, it's child will not be.
        final boolean minimizingPlayer = nodeLevelIsOurs(node);
        Node77968125 bestNode = children.get(0);
        double bestValue = minimizingPlayer ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
        for (final Node77968125 child : children) {
            chooseMinMaxMove(child);
            final double evaluation = child.getNodeEvaluation();
            final boolean isBestValue = minimizingPlayer ? evaluation < bestValue
                    : evaluation > bestValue;
            if (isBestValue) {
                bestNode = child;
                bestValue = evaluation;
            }
        }
        node.setNodeEvaluation(bestValue);
        return bestNode.getNodeMove();
    }

    /**
     * Creates the sub tree.
     *
     * @param parent
     *            The parent node.
     */
    void createSubTree(final Node77968125 parent) {
        // Find the empty tile spots of the board of the parent.
        final Board board = parent.getNodeBoard();
        final int nextDepth = parent.getNodeDepth() + 1;
        final int nodeId = nodeLevelIsOurs(nextDepth) ? id : opponentId;
        final int s = nextNumbersToBePlayed[nextDepth - 1];
        final ArrayList<Tile> emptyTiles = findEmptyTiles(board, nextDepth);

        for (final Tile emptyTile : emptyTiles) {
            // Get needed values for Node() and boardAfterMove() call.
            final int x = emptyTile.getX();
            final int y = emptyTile.getY();
            final int[] move = new int[] { x, y };

            // Simulate putting a tile on this spot on the parent node board.
            final Board nextBoard = ProximityUtilities.boardAfterMove(nodeId, board, x, y, s);

            // Create the new node.
            final Node77968125 newNode = new Node77968125(parent, nextDepth, move, nextBoard);

            // Add the node as child of the parent node.
            parent.addChild(newNode);

            // Add opponent's branches.
            if (nextDepth < MAX_DEPTH) {
                createSubTree(newNode);
            }
        }
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
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the next move.
     *
     * @param board
     *            the board
     * @param randomNumber
     *            the random number
     * @return int[] with our next move.
     */
    public int[] getNextMove(final Board board, final int randomNumber) {
        nextNumbersToBePlayed = Board.getNextTenNumbersToBePlayed();
        assert (randomNumber == nextNumbersToBePlayed[0]);
        final Node77968125 root = new Node77968125(board, id);
        // create a tree of depth MAX_DEPTH.
        createSubTree(root);
        final int[] nextMove = chooseMinMaxMove(root);
        return nextMove;
    }

    /**
     * Gets the num of tiles.
     *
     * @return numOfTiles
     */
    public int getNumOfTiles() {
        return numOfTiles;
    }

    /**
     * Gets the score.
     *
     * @return score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the id to set
     */
    public void setId(final int id) {
        this.id = id;
        this.opponentId = reverseId(id);
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the num of tiles.
     *
     * @param tiles
     *            the new num of tiles
     */
    public void setNumOfTiles(final int tiles) {
        numOfTiles = tiles;
    }

    /**
     * Sets the score.
     *
     * @param score
     *            the new score
     */
    public void setScore(final int score) {
        this.score = score;
    }
}
