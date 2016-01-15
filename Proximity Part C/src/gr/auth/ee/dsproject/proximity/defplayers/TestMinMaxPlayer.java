package gr.auth.ee.dsproject.proximity.defplayers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gr.auth.ee.dsproject.proximity.board.Tile;

public class TestMinMaxPlayer {

    private void printTree(Node77968125 root) {
        for (int i = 0; i < root.nodeDepth; i++) {
            System.out.print("-");
        }
        System.out.print(">");
        System.out.println(" (" + root.getNodeEvaluation() + ")");
        for (Node77968125 child : root.getChildren()) {
            printTree(child);
        }
    }

    @Test
    public void testCountEmptyNeighbors() {
        Tile[] neighbors = new Tile[] { new Tile(0, 0, 0, 0, 0, 0), new Tile(0, 0, 0, 0, 0, 1),
                new Tile(0, 0, 0, 0, 0, 2), null, null, new Tile(0, 0, 0, 0, 0, 0) };
        assertEquals(2, Node77968125.countEmptyNeighbors(neighbors));
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
        class NodeTest extends Node77968125 {
            public NodeTest() {
                super(null, 0);
            }

            public NodeTest(Node77968125 parent, int nodeDepth, int[] nodeMove) {
                super(null, nodeDepth, nodeMove, null);
                this.parent = parent;
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
    }
}
