package gr.auth.ee.dsproject.proximity.defplayers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gr.auth.ee.dsproject.proximity.board.Board;
import gr.auth.ee.dsproject.proximity.board.ProximityUtilities;
import gr.auth.ee.dsproject.proximity.board.Tile;

// TODO: rename to correct class name for assignment.
public class Node {
    private static Logger logger;
    Node parent;

    ArrayList<Node> children;
    private int id;
    private int opponentId;
    private int randomNumber;
    int nodeDepth;
    int[] nodeMove;
    Board nodeBoard;
    double nodeEvaluation;
    private HashMap<Integer, Integer> myPool;
    private HashMap<Integer, Integer> opponentsPool;

    /**
     * @param nodeBoard
     */
    public Node(Board nodeBoard, int id) {
        // constructor used for the root Node
        this(null, 0, null, nodeBoard, -1);
        this.id = id;
        this.opponentId = id == 1 ? 2 : 1;

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
                logger.log(Level.FINEST, "Created Node");
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param parent
     * @param nodeDepth
     * @param nodeMove
     * @param nodeBoard
     */
    public Node(Node parent, int nodeDepth, int[] nodeMove, Board nodeBoard, int randomNumber) {
        this.randomNumber = randomNumber;
        this.parent = parent;
        this.nodeDepth = nodeDepth;
        this.nodeMove = nodeMove;
        this.nodeBoard = nodeBoard;
        this.children = new ArrayList<Node>();

        if (parent != null) {
            this.opponentId = parent.getId();
            this.id = (this.opponentId == 1) ? 2 : 1;
            Board parentBoard = parent.getNodeBoard();
            if (nodeDepth % 2 == 1) {
                this.myPool = parentBoard.getMyPool();
                this.opponentsPool = parentBoard.getOpponentsPool();
            } else {
                this.opponentsPool = parentBoard.getMyPool();
                this.myPool = parentBoard.getOpponentsPool();
            }
            logger = parent.getLogger();
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
    private double calculateRisk(final Tile tile, Board board) {
        HashMap<Integer, Integer> map;
        final int tileId = tile.getPlayerId();
        final boolean isEmpty = (tileId == 0);
        final double scoreTile = isEmpty ? randomNumber : tile.getScore();
        final boolean isEnemyBigger = (tileId == opponentId && randomNumber <= scoreTile);
        if (isEnemyBigger) {
            map = myPool;
        } else {
            map = opponentsPool;
        }

        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        double emptyNeighbors = isEmpty ? 1 : 0;
        for (final Tile neighbor : neighbors) {
            if (neighbor != null && neighbor.getPlayerId() == 0) {
                emptyNeighbors++;
            }
        }
        assert (emptyNeighbors != 0);

        double totalValuesCount = 0;
        double biggerValuesCount = 0;
        // TODO: try using next 10 moves instead of map.
        for (final Map.Entry<Integer, Integer> entry : map.entrySet()) {
            final Integer tileValue = entry.getKey();
            final Integer tileCount = entry.getValue();
            totalValuesCount += tileCount;
            if (tileValue > scoreTile) {
                biggerValuesCount += tileCount;
            }
        }
        if (totalValuesCount == 0) {
            return 0;
        }

        final double percentLarger = biggerValuesCount / totalValuesCount;
        final double risk = 2 * percentLarger * (scoreTile) / (emptyNeighbors * emptyNeighbors);
        if (isEnemyBigger) {
            return -risk;
        } else {
            return risk;
        }
    }

    public void evaluate() {
        int x = nodeMove[0];
        int y = nodeMove[1];
        Board board = parent.getNodeBoard();
        Tile tile = board.getTile(x, y);
        assert (tile.getPlayerId() == 0);

        final Tile[] neighbors = ProximityUtilities.getNeighbors(tile.getX(), tile.getY(), board);
        int scoreFromAlies = 0;
        int scoreFromEnemies = 0;
        double scoreFromRisk = calculateRisk(tile, board);

        for (final Tile neighbor : neighbors) {
            if (neighbor == null || neighbor.getPlayerId() == 0) {
                continue;
            }
            final int neighborPlayerId = neighbor.getPlayerId();
            final int neighborScore = neighbor.getScore();
            if (neighborPlayerId == opponentId && randomNumber > neighborScore) {
                scoreFromEnemies += neighborScore;
            } else if (neighborPlayerId == id && neighborScore != 20) {
                scoreFromAlies++;
            }
            scoreFromRisk += calculateRisk(neighbor, board);
        }

        // nodeEvaluation = scoreFromAlies + scoreFromEnemies + scoreFromRisk;
        nodeEvaluation = scoreFromAlies + scoreFromEnemies;
        if (nodeEvaluation > 1000) {
            logger.log(Level.WARNING,
                    "huge evaluation: " + scoreFromAlies + " " + scoreFromEnemies + " "
                            + scoreFromRisk + " = " + this.nodeEvaluation + "\n at depth "
                            + nodeDepth);
        }
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

    public Logger getLogger() {
        return logger;
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
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    /**
     * @param nodeBoard
     *            the nodeBoard to set
     */
    public void setNodeBoard(Board nodeBoard) {
        this.nodeBoard = nodeBoard;
    }

    /**
     * @param nodeDepth
     *            the nodeDepth to set
     */
    public void setNodeDepth(int nodeDepth) {
        this.nodeDepth = nodeDepth;
    }

    /**
     * @param nodeEvaluation
     *            the nodeEvaluation to set
     */
    public void setNodeEvaluation(double nodeEvaluation) {
        this.nodeEvaluation = nodeEvaluation;
    }

    /**
     * @param nodeMove
     *            the nodeMove to set
     */
    public void setNodeMove(int[] nodeMove) {
        this.nodeMove = nodeMove;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }
}
