package gr.auth.ee.dsproject.proximity.defplayers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import gr.auth.ee.dsproject.proximity.board.Board;

public class Node {
    private static Logger logger;
    Node parent;

    ArrayList<Node> children;

    int nodeDepth;
    int[] nodeMove;
    Board nodeBoard;
    double nodeEvaluation;

    /**
     * @param nodeBoard
     */
    public Node(Board nodeBoard, int id) {
        // constructor used for the root Node
        this(null, 0, null, nodeBoard, -1);

        if (logger == null) {
            logger = Logger.getLogger("Node");

            logger.setLevel(Level.ALL);
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
    public Node(Node parent, int nodeDepth, int[] nodeMove, Board nodeBoard) {
        this.parent = parent;
        this.nodeDepth = nodeDepth;
        this.nodeMove = nodeMove;
        this.nodeBoard = nodeBoard;
        this.children = new ArrayList<Node>();

        if (parent != null) {
            logger = parent.getLogger();
        }
    }

    }

    public double evaluate() {
        // TODO: call evaluator here
        return 0.0;
    }

    /**
     * @return the children
     */
    public ArrayList<Node> getChildren() {
        return children;
    }

    /**
     * public Logger getLogger() { return logger; }
     * 
     * /**
     * 
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
