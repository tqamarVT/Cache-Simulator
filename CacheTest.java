import java.util.ArrayList;

/**
 * 
 */

/**
 * @author taimo
 *
 */
public class CacheTest extends student.TestCase {
    // FIELDS
    private Cache testCache;
    private ArrayList<Data> testFileData;
    private Parser testParser;

    /**
     * 
     */
    public void setUp() {
        this.testParser = new Parser("test1.trace");
        this.testFileData = testParser.parseFile();
    }
    
    public void testCache() {
        this.testCache = new Cache(1024, 8, WritePolicy.WB, CachePlacement.DM, testFileData);
        this.testCache.directMapping();
        this.testCache = new Cache(1024, 8, WritePolicy.WT, CachePlacement.DM, testFileData);
        this.testCache.directMapping();
        this.testCache = new Cache(1024, 8, WritePolicy.WB, CachePlacement.TW, testFileData);
        this.testCache.twoWay();
        this.testCache = new Cache(1024, 8, WritePolicy.WT, CachePlacement.TW, testFileData);
        this.testCache.twoWay();
    }

}
