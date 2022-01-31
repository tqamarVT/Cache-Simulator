import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    // FIELDS
    private Scanner fileScanner;
    private ArrayList<Data> fileData;

    /**
     * 
     * @param fname
     */
    public Parser(String fname) {

        try {
            this.fileScanner = new Scanner(new File(fname));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            return;
        }
    }


    public ArrayList<Data> parseFile() {
        this.fileData = new ArrayList<Data>();
        String line = null;
        String instruction = null;
        String addr = null;
        String addrInBinary = null;
        while (this.fileScanner.hasNext()) { // maybe use hasNextLine() ?
            line = this.fileScanner.nextLine();
            instruction = line.split(" ")[0];
            addr = line.split(" ")[1];
            addr = addr.substring(2);
            // byte[] address = this.hexToByteArray(addr);
            addrInBinary = this.hexToByteArray(addr);
            this.fileData.add(new Data(instruction, addrInBinary));
        }
        //////////////////////////////////////////////////////
// byte[] test = new byte[4];
// for (int i = 0; i < test.length; i++) {
// test[i] = this.fileData.get(31).getAddress()[i];
// System.out.print(test[i]);
// }
// ByteBuffer testbb = ByteBuffer.wrap(test);
// int num = testbb.get();
// System.out.print("\n");
///////////////////////////////////////////////////////
        fileScanner.close(); // hopefully good enough
        return this.fileData;
    }


    public String hexToByteArray(String hexString) {
        String addr = hexString;
        String addrInBinary = null;
        long address = 0;
        address = Long.parseLong(addr, 16);
        addrInBinary = Long.toBinaryString(address);
        addrInBinary = String.format("%32s", addrInBinary).replace(" ", "0");
        //System.out.print(addrInBinary);
        //System.out.print("\n");
        //System.out.print(Long.parseLong("1111", 2));
        //System.out.print("\n");
        return addrInBinary;

    }

}
