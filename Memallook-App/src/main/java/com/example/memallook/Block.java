package com.example.memallook;

public class Block implements Comparable<Block> {

    private Character pointer;
    private int index;
    private int sizeInPages;

    public boolean isOccupied() {
        return pointer != null;
    }

    @Override
    public int compareTo(Block that) {
        return this.sizeInPages - that.sizeInPages;
    }

    public Character getPointer() {
        return pointer;
    }

    public Block setPointer(Character pointer) {
        this.pointer = pointer;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public Block setIndex(int index) {
        this.index = index;
        return this;
    }

    public int getSizeInPages() {
        return sizeInPages;
    }

    public Block setSizeInPages(int sizeInPages) {
        this.sizeInPages = sizeInPages;
        return this;
    }
}
