import java.util.ArrayList;

/**
 * 
 */

/**
 * @author taimo
 *
 */
public class Simulate {

    /**
     * @return
     * 
     */
    public static void main(String args[]) {
        Parser parser = new Parser("test1.trace");
        ArrayList<Data> fileData = parser.parseFile();
        Cache cache = new Cache(1024, 8, WritePolicy.WT, CachePlacement.DM,
            fileData);
        cache.directMapping();
        cache = new Cache(1024, 8, WritePolicy.WB, CachePlacement.DM, fileData);
        cache.directMapping();
        // System.out.print("hello!\n");

    }

}
