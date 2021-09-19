package com.example.memallook;

import java.util.Objects;

//For quick lookup of unoccupied blocks of pages by size
//Additionally, we're not trying to find exact matches, but the closest match
//I referenced this a lot: https://www.geeksforgeeks.org/binary-search-tree-set-2-delete/
public class BlockTree {

    BlockTree left;
    BlockTree right;

    Block node;

    public BlockTree(Block node) {
        if (node == null) {
            throw new RuntimeException("Block node cannot be null!");
        }
        this.node = node;
        this.left = null;
        this.right = null;
    }

    public BlockTree getClosestInSize(int desiredSize) {
        if (node.getSizeInPages() == desiredSize) {
            return this;
        } else if (desiredSize > node.getSizeInPages()) {
            //Look to the right for a value larger than this.node.size
            // -> If there is nothing to the right, then this node is the upper bound
            // -> If there is something to the right, this node is the lower bound. getClosestInSize on the right node and compare value
            if (right == null) {
                return this;
            } else {
                BlockTree upperBound = right.getClosestInSize(desiredSize);
                if (Math.abs(desiredSize - upperBound.node.getSizeInPages()) < Math.abs(desiredSize - this.node.getSizeInPages())) {
                    return upperBound;
                } else { // this node has a smaller difference between its size and the desiredSize
                    return this;
                }
            }
        } else { //(desiredSize < node.getSizeInPages())
            //Look to the left for a value smaller than this.node.size
            // -> If there is nothing to the left, then this node is the lower bound
            // -> If there is something to the left, this node is the upper bound. getClosestInSize on the left node and compare value

            if (left == null) {
                return this;
            } else {
                BlockTree lowerBound = left.getClosestInSize(desiredSize);
                if (Math.abs(desiredSize - lowerBound.node.getSizeInPages()) < Math.abs(desiredSize - this.node.getSizeInPages())) {
                    return lowerBound;
                } else { // this node has a smaller difference between its size and the desiredSize
                    return this;
                }
            }
        }
    }

    public void add(Block toAdd) {
        if (toAdd.getSizeInPages() < node.getSizeInPages()) {
            if (left != null) {
                left.add(toAdd);
            } else {
                left = new BlockTree(toAdd);
            }
        } else { //toAdd.getSizeInPages() >= node.getSizeInPages()
            if (right != null) {
                right.add(toAdd);
            } else {
                right = new BlockTree(toAdd);
            }
        }
    }

    public BlockTree delete(int valueToDelete) {
        if (valueToDelete < node.getSizeInPages()) {
            if (left != null) {
                left = left.delete(valueToDelete);
            } else {
                System.out.println("Value " + valueToDelete + " not found in BockTree");
            }
            return this;
        } else if (valueToDelete > node.getSizeInPages()) {
            if (right != null) {
                right = right.delete(valueToDelete);
            } else {
                System.out.println("Value " + valueToDelete + " not found in BockTree");
            }
            return this;
        }  //else this node is the one to delete...


        // If one of the children is empty, the non-empty one "moves up" a level, replacing this node
        if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        }

        // Find successor and it's immediate parent, starting with this node
        // The successor's value will become the new value if this node, and we will update the successor's parent's
        //  children to point further down the tree, effectively deleting it there because its value is now up here at this node
        BlockTree successor = right;
        BlockTree parent = this;

        while (successor.left != null) {
            parent = successor;
            successor = successor.left;
        }

        // "Delete" the successor node. We're sure it has no left children (otherwise it's not the successor). Replace
        //  its parent's reference to it with its reference to the right tree
        if (parent != this) {
            parent.left = successor.right;
        } else { //If we didn't move down the tree at all, don't overwrite the left side, just move the right half of the tree up one node
            parent.right = successor.right;
        }

        //Copy successor data to root
        this.node = successor.node;

        return this;
    }

    public int getHeight() {
        if (left == null && right == null) {
            return 1;
        } else if (left == null) {
            return right.getHeight() + 1;
        } else if (right == null) {
            return left.getHeight()  + 1;
        } else {
            int tallerChildHeight = Math.max(left.getHeight(), right.getHeight());
            return tallerChildHeight + 1;
        }
    }

    public BlockTree getLeft() {
        return left;
    }

    public BlockTree getRight() {
        return right;
    }

    public Block getNode() {
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockTree blockTree = (BlockTree) o;
        return Objects.equals(left, blockTree.left) &&
                Objects.equals(right, blockTree.right) &&
                node.equals(blockTree.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, node);
    }

}
