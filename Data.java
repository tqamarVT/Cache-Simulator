/**
 * 
 */

/**
 * @author taimo
 *
 */
public class Data {
    // FIELD
    private String instruction;
    private String address;

    /**
     * Constructor for the Data class. 
     */
    public Data(String instruction, String address) {
        this.instruction = instruction;
        this.address = address;
    }
    
    /**
     * Returns the instruction.
     * @return instruction
     */
    public String getInstruction() {
        return instruction;
    }
    
    /**
     * Returns the address.
     * @return address
     */
    public String getAddress() {
        return address;
    }
    
    

}
