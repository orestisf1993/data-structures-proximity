package gr.auth.ee.dsproject.proximity.defplayers;

import java.util.ArrayList;

import gr.auth.ee.dsproject.proximity.board.Board;

public class Node {
    Node parent;

    ArrayList<Node> children;

    int nodeDepth;
    int[] nodeMove;
    Board nodeBoard;
    double nodeEvaluation;

    /**
     * @param nodeBoard
     */
    public Node(Board nodeBoard) {
        this.nodeDepth = 0;
        this.nodeBoard = nodeBoard;
        this.children = new ArrayList<Node>();
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
