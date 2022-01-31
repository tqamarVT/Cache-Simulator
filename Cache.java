import java.util.ArrayList;

/**
 * 
 */

/**
 * @author Taimoor Qamar
 * @version 05/06/2020
 *
 */
public class Cache {
    // FIELDS
    private int cacheSize; // Size of cache in bytes
    private int blockSize; // Size of blocks in bytes
    private WritePolicy wPolicy; // Write policy for this cache
    private CachePlacement cPlacement; // Cache placemeny type for this cache
    private int numBlocks; // Number of blocks in cache
    private double hitRate; // Hit ratio
    private int memToCacheBytes; // Total bytes transferred from memory to the
                                 // cache
    private int cacheToMemBytes; // Total bytes transferred from cache to memory
    private int blocksPerSet; // Number of blocks within each block set.
    private int numSets; // Number of sets in the cache.
    private ArrayList<Data> fileData; // Array list holding trace file data
    private Data data;
    private int[][] cache;
    private boolean[][] validBitArray;
    private long[][] tagArray;
    private int offsetBitSize;
    private int setIndexBitSize;
    private int tagBitSize;
    private int offset;
    private int setIndex;
    private long tag;

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Constructor for the cache class.
     */
    public Cache(
        int cacheSize,
        int blockSize,
        WritePolicy wPolicy,
        CachePlacement cPlacement,
        ArrayList<Data> fileData) {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.wPolicy = wPolicy;
        this.cPlacement = cPlacement;
        this.numBlocks = cacheSize / blockSize;
        this.hitRate = 0;
        this.memToCacheBytes = 0;
        this.cacheToMemBytes = 0;
        this.blocksPerSet = 0;
        this.numSets = 0;
        this.fileData = fileData;
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Subroutine to set up the variables and delegate read/write instructions
     * for Direct Mapping cache configuration.
     */
    public void directMapping() {
        this.numSets = numBlocks; // Because of direct mapping
        this.blocksPerSet = numBlocks / numSets;
        this.offsetBitSize = (int)(Math.log(blockSize) / Math.log(2));
        this.setIndexBitSize = (int)(Math.log(numSets) / Math.log(2));
        this.tagBitSize = 32 - setIndexBitSize - offsetBitSize;
// System.out.print(this.offsetBitSize);
// System.out.print("\n");
// System.out.print(this.setIndexBitSize);
// System.out.print("\n");
// System.out.print(this.tagBitSize);
// System.out.print("\n");
        this.cache = new int[numSets][blocksPerSet];
        this.validBitArray = new boolean[numSets][numBlocks];
        this.tagArray = new long[numSets][numBlocks];

        for (int i = 0; i < this.fileData.size(); i++) {
            String instruction = this.fileData.get(i).getInstruction();
            String address = this.fileData.get(i).getAddress();
            this.tag = Long.parseLong(address.substring(0, tagBitSize), 2);
            this.setIndex = Integer.parseInt(address.substring(tagBitSize,
                tagBitSize + setIndexBitSize), 2);
            this.offset = Integer.parseInt(address.substring(tagBitSize
                + setIndexBitSize, tagBitSize + setIndexBitSize
                    + offsetBitSize), 2);
// System.out.print("tag: ");
// System.out.print(this.tag);
// System.out.print("\n");
// System.out.print("set index: ");
// System.out.print(this.setIndex);
// System.out.print("\n");
// System.out.print("offset: ");
// System.out.print(this.offset);
// System.out.print("\n");
            switch (instruction) {
                case "read":
                    this.directMappingRead();
                    break;
                case "write":
                    this.directMappingWrite();
                    break;
                default:
                    System.out.print("Invalid Instruction! \n");
            }
        }

        hitRate /= fileData.size();
        System.out.println(cacheSize + " " + blockSize + " " + cPlacement + " "
            + wPolicy + " " + hitRate + " " + memToCacheBytes + " "
            + cacheToMemBytes + " " + blocksPerSet);

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Subroutine to simulate read instruction cache/memory executions for the
     * Direct Mapping cache configuration.
     */
    public void directMappingRead() {
        // If tag from address matches tag in cache at that set index
        if (tag == tagArray[0][setIndex]) {
            hitRate++;
        }

        // If tag from address does not match tag in cache at that set index
        else if (validBitArray[0][setIndex]) {
            tagArray[0][setIndex] = tag;
            validBitArray[0][setIndex] = false;
            cacheToMemBytes += blockSize;
            memToCacheBytes += blockSize;
        }

        //
        else {
            tagArray[0][setIndex] = tag;
            memToCacheBytes += blockSize;
        }

    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Subroutine to delegate to the correct function depending on if cache is
     * Write-Through or Write-Back configuration.
     */
    public void directMappingWrite() {
        switch (wPolicy) {
            case WT:
                this.directMappingWriteThrough();
                break;
            case WB:
                this.directMappingWriteBack();
                break;
            default:
                System.out.print("Invalid Write Policy! \n");
                break;
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Subroutine to simulate Write-Through policy based write executions for
     * cache/memory for the Direct Mapping cache configuration.
     */
    public void directMappingWriteThrough() {
        if (tagArray[0][setIndex] == tag) {
            hitRate++;
            cacheToMemBytes += 4;
        }
        else {
            tagArray[0][setIndex] = tag;
            cacheToMemBytes += 4;
            memToCacheBytes += blockSize;
        }
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Subroutine to simulate Write-Back policy based write executions for
     * cache/memory for the Direct mapping cache configuration.
     */
    public void directMappingWriteBack() {
        if (tagArray[0][setIndex] == tag) {
            hitRate++;
            validBitArray[0][setIndex] = true;
        }
        // Ensure that the previous data in the block is updated before it is
        // over-written; ie) ensure a cacheToMem transfer
        else if (validBitArray[0][setIndex]) {
            tagArray[0][setIndex] = tag;
            cacheToMemBytes += blockSize;
            memToCacheBytes += blockSize;
        }
        else {
            tagArray[0][setIndex] = tag;
            validBitArray[0][setIndex] = true;
            memToCacheBytes += blockSize;

        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void twoWay() {
        this.numSets = numBlocks / 2; // Because Two Way Set Associativity
        this.blocksPerSet = numBlocks / numSets;
        this.offsetBitSize = (int)(Math.log(blockSize) / Math.log(2));
        this.setIndexBitSize = (int)(Math.log(numSets) / Math.log(2));
        this.tagBitSize = 32 - setIndexBitSize - offsetBitSize;
// System.out.print(this.offsetBitSize);
// System.out.print("\n");
// System.out.print(this.setIndexBitSize);
// System.out.print("\n");
// System.out.print(this.tagBitSize);
// System.out.print("\n");
        this.cache = new int[numSets][blocksPerSet];
        this.validBitArray = new boolean[numSets][numBlocks];
        this.tagArray = new long[numSets][numBlocks];
        
        for (int i = 0; i < this.fileData.size(); i++) {
            String instruction = this.fileData.get(i).getInstruction();
            String address = this.fileData.get(i).getAddress();
            this.tag = Long.parseLong(address.substring(0, tagBitSize), 2);
            this.setIndex = Integer.parseInt(address.substring(tagBitSize,
                tagBitSize + setIndexBitSize), 2);
            this.offset = Integer.parseInt(address.substring(tagBitSize
                + setIndexBitSize, tagBitSize + setIndexBitSize
                    + offsetBitSize), 2);
// System.out.print("tag: ");
// System.out.print(this.tag);
// System.out.print("\n");
// System.out.print("set index: ");
// System.out.print(this.setIndex);
// System.out.print("\n");
// System.out.print("offset: ");
// System.out.print(this.offset);
// System.out.print("\n");
            switch (instruction) {
                case "read":
                    //this.directMappingRead();
                    break;
                case "write":
                    //this.directMappingWrite();
                    break;
                default:
                    System.out.print("Invalid Instruction! \n");
            }
        }
    }
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void twoWayRead() {
        
    }

}
