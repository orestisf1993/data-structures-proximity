package gr.auth.ee.dsproject.proximity.defplayers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gr.auth.ee.dsproject.proximity.board.Tile;

public class TestMinMaxPlayer {

    int newIdxStart(int nodeDepth, int tileId) {
        final boolean isOurs = (tileId == 1);
        return nodeDepth + (MinMaxPlayer.nodeLevelIsOurs(nodeDepth) != isOurs ? 1 : 0);
    }

    int oldIdxStart(int nodeDepth, int tileId) {
        final boolean isOurs = (tileId == 1);
        final boolean isEnemys = (tileId == 2);

        int idxStart = nodeDepth;
        if ((idxStart % 2 == 1 && isEnemys) || (idxStart % 2 == 0 && isOurs)) {
            idxStart++;
        }
        return idxStart;
    }

    private void printTree(final Node77968125 root) {
        for (int i = 0; i < root.nodeDepth; i++) {
            System.out.print("-");
        }
        System.out.print(">");
        System.out.println(" (" + root.getNodeEvaluation() + ")");
        for (final Node77968125 child : root.getChildren()) {
            printTree(child);
        }
    }

    @Test
    public void testBoolean() {
        for (int i = 1; i < 99; i++) {
            assertEquals(oldIdxStart(i, 1), newIdxStart(i, 1));
            assertEquals(oldIdxStart(i, 2), newIdxStart(i, 2));
        }
    }

    @Test
    public void testCountEmptyNeighbors() {
        final Tile[] neighbors = new Tile[] { new Tile(0, 0, 0, 0, 0, 0),
                new Tile(0, 0, 0, 0, 0, 1), new Tile(0, 0, 0, 0, 0, 2), null, null,
                new Tile(0, 0, 0, 0, 0, 0) };
        assertEquals(2, Node77968125.countEmptyNeighbors(neighbors));
    }

    @Test
    public void testIncrement() {
        int a = 0;

        @SuppressWarnings("unused")
        final boolean f1 = (false && ++a > 0);
        assertEquals(false, f1);
        assertEquals(0, a);

        final boolean f2 = (true && ++a > 0);
        assertEquals(true, f2);
        assertEquals(1, a);
    }

    @Test
    public void testMiniMax() {
        class NodeTest extends Node77968125 {
            public NodeTest() {
                super(null, 0);
            }

            public NodeTest(final Node77968125 parent, final int nodeDepth, final int[] nodeMove) {
                super(null, nodeDepth, nodeMove, null);
                this.parent = parent;
            }

            NodeTest createChild() {
                return createChild(0.0);
            }

            NodeTest createChild(final double eval) {
                final NodeTest node = new NodeTest(this, nodeDepth + 1, null);
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
        final NodeTest root = new NodeTest();
        NodeTest child = root.createChild();
        final NodeTest bestChild = new NodeTest(root, 1, bestMove);
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

        final int[] result = new MinMaxPlayer(1).chooseMinMaxMove(root);

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
