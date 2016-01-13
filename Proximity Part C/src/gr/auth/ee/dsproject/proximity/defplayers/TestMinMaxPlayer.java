package gr.auth.ee.dsproject.proximity.defplayers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gr.auth.ee.dsproject.proximity.board.Board;

public class TestMinMaxPlayer {

    private void printTree(Node root) {
        for (int i = 0; i < root.nodeDepth; i++) {
            System.out.print("-");
        }
        System.out.print(">");
        System.out.println(" (" + root.getNodeEvaluation() + ")");
        for (Node child : root.getChildren()) {
            printTree(child);
        }
    }

    @Test
    public void testIncrement() {
        int a = 0;

        @SuppressWarnings("unused")
        boolean f1 = (false && ++a > 0);
        assertEquals(false, f1);
        assertEquals(0, a);

        boolean f2 = (true && ++a > 0);
        assertEquals(true, f2);
        assertEquals(1, a);
    }

    @Test
    public void testMiniMax() {
        class NodeTest extends Node {
            public NodeTest() {
                super(null, 0);
            }

            public NodeTest(Node parent, int nodeDepth, int[] nodeMove) {
                super(null, nodeDepth, nodeMove, null, -1);
                this.parent = parent;
            }

            public NodeTest(Node parent, int nodeDepth, int[] nodeMove, Board nodeBoard,
                    int randomNumber) {
                super(null, nodeDepth, nodeMove, nodeBoard, randomNumber);
                this.parent = parent;
            }

            void addChild(NodeTest child) {
                children.add(child);
            }

            NodeTest createChild() {
                return createChild(0.0);
            }

            NodeTest createChild(double eval) {
                NodeTest node = new NodeTest(this, nodeDepth + 1, null);
                node.setNodeEvaluation(eval);
                addChild(node);
                return node;
            }

            @Override
            public void evaluate() {
            }
        }

        final double epsilon = 0.01;
        final double bestScore = 100.55;
        final int[] bestMove = new int[] { 0, 0 };

        // max
        NodeTest root = new NodeTest();
        NodeTest child = root.createChild();
        NodeTest bestChild = new NodeTest(root, 1, bestMove);
        root.addChild(bestChild);

        // min and then max
        child = child.createChild();
        child.createChild(-500);
        child.createChild(100.4);
        // 2 children for max
        child = child.createChild();
        child.createChild(10);
        child.createChild(-100);

        // min for bestChild
        child = bestChild.createChild();
        child.createChild(-1000);
        child.createChild(bestScore);
        bestChild.createChild().createChild(bestScore * 2);
        child = bestChild.createChild();
        child.createChild(bestScore * 10);
        child.createChild(0);

        assertEquals(0, root.getNodeEvaluation(), epsilon);

        int[] result = new MinMaxPlayer(1).chooseMinMaxMove(root);

        // print the tree after MiniMax
        System.out.println();
        printTree(root);

        assertEquals(bestScore, root.getNodeEvaluation(), epsilon);
        assertEquals(bestMove, result);
    }

    @Test
    public void testNodeLevel() {
        assertFalse(MinMaxPlayer.nodeLevelIsOurs(0));
        assertTrue(MinMaxPlayer.nodeLevelIsOurs(1));
        assertFalse(MinMaxPlayer.nodeLevelIsOurs(2));
        assertTrue(MinMaxPlayer.nodeLevelIsOurs(3));
        assertFalse(MinMaxPlayer.nodeLevelIsOurs(4));
        assertTrue(MinMaxPlayer.nodeLevelIsOurs(5));

        Node node0 = new Node(null, 0);
        node0.setNodeDepth(0);
        Node node1 = new Node(null, 0);
        node1.setNodeDepth(1);
        Node node2 = new Node(null, 0);
        node2.setNodeDepth(2);
        Node node3 = new Node(null, 0);
        node3.setNodeDepth(3);
        Node node4 = new Node(null, 0);
        node4.setNodeDepth(4);
    }
}
