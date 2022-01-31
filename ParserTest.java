/**
 * 
 */

/**
 * @author taimo
 *
 */
public class ParserTest extends student.TestCase {
    // FIELDS
    Parser testParser;

    /**
     * 
     */
    public void setUp() {
        testParser = new Parser("test1.trace");
    }
    
    public void testParseFile() {
        testParser.parseFile();
    }

}
