package com.example.memallook;

import java.util.Objects;

public class Block implements Comparable<Block> {

    private Character pointer;
    private int index;
    private int sizeInPages;

    public Block(Character pointer, int index, int sizeInPages) {
        this.pointer = pointer;
        this.index = index;
        this.sizeInPages = sizeInPages;
    }

    //For testing only
    Block(int sizeInPages) {
        this.sizeInPages = sizeInPages;
    }

    public boolean isOccupied() {
        return pointer != null;
    }

    @Override
    public int compareTo(Block that) {
        int difference = this.sizeInPages - that.sizeInPages;
        if (difference != 0) {
            return difference;
        } else {
            return this.index - that.index;
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return index == block.index &&
                sizeInPages == block.sizeInPages &&
                Objects.equals(pointer, block.pointer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pointer, index, sizeInPages);
    }
}
