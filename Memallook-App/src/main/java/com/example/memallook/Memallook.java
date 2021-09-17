package com.example.memallook;

import java.io.PrintStream;

public class Memallook {

    //Things to represent

    //Map<Pointer, Block(Size, Index)>
    // This is for occupied blocks

    //BST<Block(Index, Size)>, something where we can lookup based on size quickly
    // This is for unoccupied blocks

    //If the above collection is BACKED by the below collections.... we might have an easier time

    //Maybe an OCCUPIED list
    // Starts with [0, pointer, size of block]

    //VACANCY list
    //  Starts with [0, size of block]

    //OR we have one List<Block> and the lack of a pointer on a bloock could reprresent that it's unoccupied

    //Can we keep a quick look up structure based on SIZE of block (see above)

    public void init(int pageSize, int numPages) {
        //save these variables
        //init the array?
        //init other things
    }

    public void alloc(int blockSize) {
        //Find the unoccupied block CLOSEST TO YOUR SIZE that fits you
        // -> traverse the BST, get a candidate

        //"place" it
        // update the unoccupied block to have a smaller size (and possibly its index (either incremented or decremented)
        // -> the BST should sort this out (literally haha)
        // create an occupied block
        // -> put it in the block list at [unoccupied index or unoccupied index++]
        // -> put it in the map with the correct pointer and index
    }

    public void dealloc(char pointer) {
        //need quick lookup of INDEX from pointer, grab that block and change it to empty
        // -> CHECK for adjacent unoccupied's and merge them if necessary
        // -> easy to do this, increment and decrement index of block list, make a decision
        // remove the value from the block index, we still have the ppointer value
    }

    public void show(PrintStream out) {
        //iterate over the blocklist, creating a sequence of characters by putting one for each block's pointer's value
        // do a little square root rounding and loop over the list we created to print it out nicely

    }

    public void clear() {
        //refresh and overwrite all of our stuff, trivial i think
    }

}
