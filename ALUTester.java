/**
 * Evan Reichard :: January 2013
 * THIS IS JUST THE TESTER
 * THE MEATY CLASS IS HERE: http://pastebin.com/qKddeAWi
 **/


public class ALUTester
{
	public static void main(String[] args)
	{
		ALU alu1 = new ALU();
		// alu1.logicCheck();
		
		// 6 CONTROL BITS
		// zx nx zy ny f no
		// 32 (16 + 16) INPUT BITS		
		
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "101010"));
		// System.out.println("Press Enter To Continue...");
       		// new java.util.Scanner(System.in).nextLine();
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "111111"));
		// System.out.println("Press Enter To Continue...");
        	// new java.util.Scanner(System.in).nextLine();
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "111010"));
		// System.out.println("Press Enter To Continue...");
        	// new java.util.Scanner(System.in).nextLine();
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "001100"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "110000"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "001101"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "110001"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "001111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "110011"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "011111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "110111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "001110"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "110010"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "000010"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "010011"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "000111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "000000"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000000000", "1111111111111111", "010101"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "101010"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "111111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "111010"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "001100"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "110000"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "001101"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "110001"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "001111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "110011"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "011111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "110111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "001110"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "110010"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "000010"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "010011"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "000111"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "000000"));
		alu1.convertArrayBinary(alu1.logicALU("0000000000010001", "0000000000000011", "010101"));
		

		
	}
}
