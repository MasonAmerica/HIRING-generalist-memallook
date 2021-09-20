Miles Gillis - Memallook Submission
===

## Running the code

This project is written in java, built with gradle, and run with a bash script. The following dependencies are required:
* JDK 11 installed and configured
* A CLI that can run a bash script 

The application runs continuously rather than serializing the memory buffer to text and reading it back in. This means you can expect the following command sequence: 

```bash
    My-Computer:HIRING-generalist-memallook user$ ./memallook.bash 
    Memallook started, please initialize with two integers 'pageSize' and 'numberOfPages':
    16 16
    Done. Enter next command.
    alloc 64
    Allocated 64 bytes at pointer a.
    exit
```

## Design decisions

The design was driven by a desire to avoid full scans of the memory buffer on every operation. Additionally, I wanted to optimize by doing allocations within the smallest empty space that could fit the requested number of pages, avoiding fragmentation to some degree.

The three data structures I leveraged were:
* Map<Character, Block> occupiedBlockIndex, a Map of pointer to occupied blocks for constant-time lookup and presence-checking 
* List<Block> unoccupiedBlockList, a list of unoccupied blocks ordered by size
* List<Block> blockList, a sequence of occupied and unoccupied blocks in the order they are found

The model object `Block` holds its own index relative to the blockList so that when it is looked up by size, we can trace back to its neighbors in a different list (the blockList). Unfortunately, this did necessitate a scan over subsequent blocks when new blocks are added. I considered a linked list as well, but that had its own obstacles. Ultimately I was happy to avoid scanning page by page. I'm pretty sure there are ways to have different lookup tables on top of the same collection, but I did not have the expertise to implement those and I believe my solution offers the same result.

Deallocation's merge component ended up being the most complicated, but was successfully added thanks to our three data structures.  

## Why Java?

Java is the only language I feel really fluent in and can achieve the "flow". I can work with Python and Scala as well, but I would have to google so many more things, so I chose Java.

## References

The only outside reference that I copied from (other than stack overflow one-liners for trivial things) was https://www.geeksforgeeks.org/binary-search-tree-set-2-delete/. This was an overview of a Binary Search Tree. However, I decided that an ordered list would be sufficient for looking up unoccupied blocks by size, so my BST implementation did not make it into the solution at all.  

