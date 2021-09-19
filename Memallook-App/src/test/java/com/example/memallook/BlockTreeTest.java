package com.example.memallook;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTreeTest {

    @Test
    public void getClosestInSize() {
        Block block_100 = new Block(100);
        Block block_25 = new Block(25);
        Block block_125 = new Block(125);

        BlockTree tree = new BlockTree(block_100);
        tree.add(new Block(50));
        tree.add(new Block(150));
        tree.add(block_25);
        tree.add(new Block(75));
        tree.add(block_125);
        tree.add(new Block(175));

        Block closestInSize_101 = tree.getClosestInSize(101).getNode();
        assertEquals(block_100, closestInSize_101);

        Block closestInSize_113 = tree.getClosestInSize(113).getNode();
        assertEquals(block_125, closestInSize_113);

        Block closestInSize_1 = tree.getClosestInSize(1).getNode();
        assertEquals(block_25, closestInSize_1);

        Block closestInSize_37 = tree.getClosestInSize(37).getNode();
        assertEquals(block_25, closestInSize_37);
    }

    @Test
    public void testAdd() {
        BlockTree tree = new BlockTree(new Block(4));
        tree.add(new Block(2));
        tree.add(new Block(6));
        tree.add(new Block(1));
        tree.add(new Block(3));
        tree.add(new Block(5));
        tree.add(new Block(7));

        Assert.assertEquals(3, tree.getHeight());

        BlockTree badTree = new BlockTree(new Block(4));
        badTree.add(new Block(3));
        badTree.add(new Block(2));
        badTree.add(new Block(1));
        badTree.add(new Block(5));
        badTree.add(new Block(6));
        badTree.add(new Block(7));

        Assert.assertEquals(4, badTree.getHeight());

        BlockTree worstTree = new BlockTree(new Block(1));
        worstTree.add(new Block(2));
        worstTree.add(new Block(3));
        worstTree.add(new Block(4));
        worstTree.add(new Block(5));
        worstTree.add(new Block(6));
        worstTree.add(new Block(7));

        Assert.assertEquals(7, worstTree.getHeight());
    }

    @Test
    public void delete() {
        BlockTree justANode = new BlockTree(new Block(4));

        BlockTree deleted1 = justANode.delete(5);
        assertEquals(justANode, deleted1);

        BlockTree deleted2 = justANode.delete(4);
        assertNull(deleted2);

        BlockTree tree = new BlockTree(new Block(4));
        tree.add(new Block(2));
        tree.add(new Block(6));
        tree.add(new Block(1));
        tree.add(new Block(3));
        tree.add(new Block(5));
        tree.add(new Block(7));


        BlockTree expectedTree = new BlockTree(new Block(5));
        expectedTree.add(new Block(2));
        expectedTree.add(new Block(6));
        expectedTree.add(new Block(1));
        expectedTree.add(new Block(3));
        expectedTree.add(new Block(7));

        tree.delete(4);
        assertEquals(expectedTree, tree);

        BlockTree stubbyTree = new BlockTree(new Block(4));
        stubbyTree.add(new Block(2));
        stubbyTree.add(new Block(5));
        stubbyTree.add(new Block(1));
        stubbyTree.add(new Block(3));
        stubbyTree.add(new Block(7));


        BlockTree expectedTree2 = new BlockTree(new Block(5));
        expectedTree2.add(new Block(2));
        expectedTree2.add(new Block(7));
        expectedTree2.add(new Block(1));
        expectedTree2.add(new Block(3));

        stubbyTree.delete(4);
        assertEquals(expectedTree2, stubbyTree);

        BlockTree letsGoLeft = new BlockTree(new Block(4));
        letsGoLeft.add(new Block(2));
        letsGoLeft.add(new Block(5));
        letsGoLeft.add(new Block(1));
        letsGoLeft.add(new Block(3));
        letsGoLeft.add(new Block(7));


        BlockTree expectedTree3 = new BlockTree(new Block(4));
        expectedTree3.add(new Block(1));
        expectedTree3.add(new Block(6));
        expectedTree3.add(new Block(3));
        expectedTree3.add(new Block(5));
        expectedTree3.add(new Block(7));

        letsGoLeft.delete(2);
        assertEquals(expectedTree3, letsGoLeft);


    }
}