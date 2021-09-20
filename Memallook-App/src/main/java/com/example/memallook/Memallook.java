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

    private int pageSize;
    private int numPages;

    //Things to represent

    //Map<Pointer, Block(Size, Index)>
    // This is for occupied blocks
    private Map<Character, Block> occupiedBlockIndex;

    //BST<Block(Index, Size)>, something where we can lookup based on size quickly
    // This is for unoccupied blocks
    private BlockTree unoccupiedBlockTree;

    private List<Block> unoccupiedBlockList;

    //If the above collection is BACKED by the below collections.... we might have an easier time

    //Maybe an OCCUPIED list
    // Starts with [0, pointer, size of block]

    //VACANCY list
    //  Starts with [0, size of block]

    //OR we have one List<Block> and the lack of a pointer on a bloock could reprresent that it's unoccupied
    private List<Block> blockList;

    private List<Character> availablePointers;

    public Memallook(int pageSize, int numPages) {
        this.pageSize = pageSize;
        this.numPages = numPages;
        this.occupiedBlockIndex = new HashMap<>();
        Block firstEmptyBlock = new Block(null, 0, numPages);
        this.unoccupiedBlockTree = new BlockTree(firstEmptyBlock);
        this.unoccupiedBlockList = new ArrayList<>();
        unoccupiedBlockList.add(firstEmptyBlock);
        this.blockList = new ArrayList<>();
        blockList.add(firstEmptyBlock);
        this.availablePointers = new ArrayList<>(POINTERS);
    }

    public char alloc(int blockSize) throws Exception {
        if (blockSize < 1) {
            throw new RuntimeException("Aborting allocation of 0 or less bytes.");
        }
        int sizeOfNewBlock = ((blockSize - 1) / pageSize) + 1;

        //Find the unoccupied block CLOSEST TO YOUR SIZE that fits you
        // -> traverse the BST, get a candidate
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

        int blockListPlacementIndex = emptySpaceToUse.getIndex();
        //"place" it
        // create an occupied block
        // -> put it in the block list at [unoccupied index or unoccupied index++]
        // -> put it in the map with the correct pointer and index
        Character nextPointer = availablePointers.remove(0);
        Block newOccupiedBlock = new Block(nextPointer, blockListPlacementIndex, sizeOfNewBlock);
        blockList.add(blockListPlacementIndex, newOccupiedBlock);
        occupiedBlockIndex.put(nextPointer, newOccupiedBlock);
        updateDownstreamIndices(blockListPlacementIndex + 1);

        // update the unoccupied block to have a smaller size (and possibly its index (either incremented or decremented)
        // -> the BST should sort this out (literally haha)
        emptySpaceToUse.setSizeInPages(emptySpaceToUse.getSizeInPages() - sizeOfNewBlock);
        unoccupiedBlockList.add(emptySpaceToUse);
        unoccupiedBlockList.sort(Block::compareTo);
        return nextPointer;
    }

    private void updateDownstreamIndices(int startingIndex) {
        for (int i = startingIndex; i < blockList.size(); i++) {
            Block blockToIncrement = blockList.get(i);
            blockToIncrement.setIndex(i);
        }
    }

    public void dealloc(char pointer) {
        //need quick lookup of INDEX from pointer, grab that block and change it to empty
        Block blockToDeallocate = occupiedBlockIndex.remove(pointer);
        if (blockToDeallocate == null) {
            System.out.println("No block of allocated pages found for pointer " + pointer);
            return;
        }
        availablePointers.add(blockToDeallocate.getPointer());

        int deallocatedBlockIndex = blockToDeallocate.getIndex();
        // If the block to the left is nonexistent or occupied AND the block to the right is nonexistent or occupied,
        //simply swap it out
        if ((deallocatedBlockIndex == 0 || blockList.get(deallocatedBlockIndex - 1).getPointer() != null) &&
                (deallocatedBlockIndex + 1 == blockList.size() || blockList.get(deallocatedBlockIndex + 1).getPointer() != null)) {
            blockToDeallocate.setPointer(null);
        }
        // -> CHECK for adjacent unoccupied's and merge them if necessary
        if (deallocatedBlockIndex - 1 > 0 && blockList.get(deallocatedBlockIndex - 1).getPointer() == null) {
            //We're absorbing the block to the left. We reference that block from index-1. We update the block at the
            //given index. But once we remove the block at index-1, the index of this block becomes index - 1
            deallocatedBlockIndex = deallocatedBlockIndex - 1;
            Block emptyLeftBlockToMerge = blockList.remove(deallocatedBlockIndex);
            unoccupiedBlockList.remove(emptyLeftBlockToMerge);
            blockToDeallocate.setSizeInPages(blockToDeallocate.getSizeInPages() + emptyLeftBlockToMerge.getSizeInPages());
            blockToDeallocate.setPointer(null);
            blockToDeallocate.setIndex(deallocatedBlockIndex);
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

    public String getFullBuffer() {
        //iterate over the blocklist, creating a sequence of characters by putting one for each block's pointer's value
        // do a little square root rounding and loop over the list we created to print it out nicely
        StringBuilder stringBuilder = new StringBuilder();
        for (Block block : blockList) {
            Character pointer = block.getPointer() == null ? UNALLOCATED_PAGE_CHARACTER : block.getPointer();
            stringBuilder.append(String.valueOf(pointer).repeat(block.getSizeInPages()));
        }
        return stringBuilder.toString();
    }

    public void show(PrintStream out) {
        String fullBuffer = getFullBuffer();

        int rowLength = Math.min(MAX_ROW_LENGTH, (int) Math.sqrt(fullBuffer.length()));
        int startingIndex = 0;
        for(int i = 1; i <= (fullBuffer.length() / rowLength) + 1; i++) {
            out.println(fullBuffer.substring(startingIndex, Math.min(rowLength*i, fullBuffer.length())));
            startingIndex = rowLength*i;
        }
    }



    public void clear() {
        //Just torch as much memory as we can? This should really be happening with a constructor and stuff.
        this.occupiedBlockIndex = new HashMap<>();
        this.unoccupiedBlockList = new ArrayList<>();
        this.blockList = new ArrayList<>();
        this.availablePointers = new ArrayList<>();
    }

}
