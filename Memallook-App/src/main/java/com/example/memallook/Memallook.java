package com.example.memallook;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memallook {

    private static final int MAX_ROW_LENGTH = 150;
    private static final int UNALLOCATED_PAGE_CHARACTER = '.';
    private static final List<Character> POINTERS = new ArrayList<>();
    static {
        for(int i = 0; i < 26; i++) {
            POINTERS.add((char) (i + 'a'));
        }
    }

    private final int pageSize;

    private Map<Character, Block> occupiedBlockIndex;
    private List<Block> unoccupiedBlockList;
    private List<Block> blockList;

    private List<Character> availablePointers;

    public Memallook(int pageSize, int numPages) {
        this.pageSize = pageSize;
        this.occupiedBlockIndex = new HashMap<>();
        Block firstEmptyBlock = new Block(null, 0, numPages);
        this.unoccupiedBlockList = new ArrayList<>();
        unoccupiedBlockList.add(firstEmptyBlock);
        this.blockList = new ArrayList<>();
        blockList.add(firstEmptyBlock);
        this.availablePointers = new ArrayList<>(POINTERS);
    }

    public char alloc(int blockSize) {
        if (blockSize < 1) {
            throw new RuntimeException("Aborting allocation of 0 or less bytes.");
        }
        int sizeOfNewBlock = ((blockSize - 1) / pageSize) + 1;

        //Find the smallest block  that fits you
        Block emptySpaceToUse = null;
        for (int i = 0; i < unoccupiedBlockList.size(); i++) {
            Block candidateBlock = unoccupiedBlockList.get(i);
            if (candidateBlock.getSizeInPages() >= sizeOfNewBlock) {
                emptySpaceToUse = candidateBlock;
                unoccupiedBlockList.remove(i);
                break;
            }
        }
        if (emptySpaceToUse == null) {
            throw new RuntimeException("Not enough space");
        }

        // Create an occupied block, place it in the collections, and update downstream indices
        int blockListPlacementIndex = emptySpaceToUse.getIndex();
        Character nextPointer = availablePointers.remove(0);

        Block newOccupiedBlock = new Block(nextPointer, blockListPlacementIndex, sizeOfNewBlock);
        blockList.add(blockListPlacementIndex, newOccupiedBlock);
        occupiedBlockIndex.put(nextPointer, newOccupiedBlock);
        updateDownstreamIndices(blockListPlacementIndex + 1);

        // Update the unoccupied block to have a smaller size, add it back to the unoccupied list if it still exists,
        //otherwise remove it from the blockList too
        emptySpaceToUse.setSizeInPages(emptySpaceToUse.getSizeInPages() - sizeOfNewBlock);
        if (emptySpaceToUse.getSizeInPages() > 0) {
            unoccupiedBlockList.add(emptySpaceToUse);
            unoccupiedBlockList.sort(Block::compareTo);
        } else {
            blockList.remove(emptySpaceToUse);
        }
        return nextPointer;
    }

    private void updateDownstreamIndices(int startingIndex) {
        for (int i = startingIndex; i < blockList.size(); i++) {
            Block blockToIncrement = blockList.get(i);
            blockToIncrement.setIndex(i);
        }
    }

    public void dealloc(char pointer) {
        Block blockToDeallocate = occupiedBlockIndex.remove(pointer);
        if (blockToDeallocate == null) {
            System.out.println("No block of allocated pages found for pointer " + pointer);
            return;
        }
        availablePointers.add(blockToDeallocate.getPointer());

        // We easily removed the block in question from the occupiedBlockIndex, now we must transform it into an
        //unoccupied block and possibly merge it with adjacent unoccupied blocks

        // If the block to the left is nonexistent or occupied AND the block to the right is nonexistent or occupied,
        //simply transform it to unoccupied
        int deallocatedBlockIndex = blockToDeallocate.getIndex();
        if ((deallocatedBlockIndex == 0 || blockList.get(deallocatedBlockIndex - 1).getPointer() != null) &&
                (deallocatedBlockIndex + 1 == blockList.size() || blockList.get(deallocatedBlockIndex + 1).getPointer() != null)) {
            blockToDeallocate.setPointer(null);
        }
        // If there is an adjacent unoccupied block, we must merge:
        if (deallocatedBlockIndex - 1 > 0 && blockList.get(deallocatedBlockIndex - 1).getPointer() == null) {
            Block emptyLeftBlockToMerge = blockList.remove(deallocatedBlockIndex -1);
            unoccupiedBlockList.remove(emptyLeftBlockToMerge);
            blockToDeallocate.setSizeInPages(blockToDeallocate.getSizeInPages() + emptyLeftBlockToMerge.getSizeInPages());
            blockToDeallocate.setPointer(null);
            blockToDeallocate.setIndex(deallocatedBlockIndex - 1);
            deallocatedBlockIndex = deallocatedBlockIndex - 1;
        }
        if (deallocatedBlockIndex < blockList.size() - 1 && blockList.get(deallocatedBlockIndex + 1).getPointer() == null) {
            Block emptyRightBlockToMerge = blockList.remove(deallocatedBlockIndex + 1);
            unoccupiedBlockList.remove(emptyRightBlockToMerge);
            blockToDeallocate.setSizeInPages(blockToDeallocate.getSizeInPages() + emptyRightBlockToMerge.getSizeInPages());
            blockToDeallocate.setPointer(null);
        }
        unoccupiedBlockList.add(blockToDeallocate);
        unoccupiedBlockList.sort(Block::compareTo);
        updateDownstreamIndices(deallocatedBlockIndex);
    }

    // Gets a representation of the total Memallook as one String
    public String getFullBuffer() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Block block : blockList) {
            Character pointer = block.getPointer() == null ? UNALLOCATED_PAGE_CHARACTER : block.getPointer();
            stringBuilder.append(String.valueOf(pointer).repeat(block.getSizeInPages()));
        }
        return stringBuilder.toString();
    }

    //Pretty-prints the Memallook
    public void show(PrintStream out) {
        String fullBuffer = getFullBuffer();

        int rowLength = Math.min(MAX_ROW_LENGTH, (int) Math.sqrt(fullBuffer.length()));
        int startingIndex = 0;
        for(int i = 1; i <= (fullBuffer.length() / rowLength) + 1; i++) {
            out.println(fullBuffer.substring(startingIndex, Math.min(rowLength*i, fullBuffer.length())));
            startingIndex = rowLength*i;
        }
    }


    //Just torch as much memory as we can? This should really be happening with an object re-construction
    public void clear() {
        this.occupiedBlockIndex = new HashMap<>();
        this.unoccupiedBlockList = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.availablePointers = new ArrayList<>();
    }

}
