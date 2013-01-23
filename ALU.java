	/**
	 * Evan Reichard :: January 2013
	 *
	 * NOTE: Using Boolean vs BitSet
	 * REASON: Memory Savings is minimal on BitSet (8-bits per Boolean, 1-bit per BitSet) but BitSet is more CPU intensive. 
	 **/
	 
public class ALU
{
	public int nandcalls = 0;

	/**
	 * Constructors
	 **/
	
	// Default Constructor
	public ALU(){}	

	//---------------------------------------------Basic Logic-------------------------------------------------\\

	// Considering NAND as the only primitive logic gate.  All further logic gates will be implemented with NAND(s) 
	public Boolean nandL(boolean bitOne, boolean bitTwo){
		nandcalls = nandcalls + 1;
		if(bitOne == true && bitTwo == true)
			return false;
		else
			return true;
	}

	public Boolean notL(boolean bitOne){
		return nandL(bitOne, bitOne);
	}
	public Boolean andL(boolean bitOne, boolean bitTwo){
		boolean tempBitOne = nandL(bitOne, bitTwo);
		return nandL(tempBitOne, tempBitOne);
	}
	public Boolean orL(boolean bitOne, boolean bitTwo){
		return nandL(nandL(bitOne, bitOne), nandL(bitTwo, bitTwo));
	}

	/** 
	 * I initialized a local variable because in an actual circuit this value 
	 * would be split as opposed to using another NAND gate.  Hopefully this
	 * is faster because I'm calling the nandL() function one less time.
	 **/
	public Boolean xorL(boolean bitOne, boolean bitTwo){
		boolean tempBitOne = nandL(bitOne, bitTwo);	
		return nandL(nandL(bitOne, tempBitOne), nandL(tempBitOne, bitTwo));
	}

	/**
	 * Same situation as above
	 **/
	public Boolean norL(boolean bitOne, boolean bitTwo){
		boolean tempBitOne = nandL(nandL(bitOne, bitOne), nandL(bitTwo, bitTwo));
		return nandL(tempBitOne, tempBitOne);
	}

	//----------------------Dynamic Multi-Way Input (More than one), Single Output Logic----------------------\\

	/**
	 * The argument boolean arrays for the following methods consist of the booleans you intend to apply the logic gate to.
	 * LMW = Logic Multi-Way 
	 * E.g. You'd use orLMW for a Or8Way function. (8-bit Array in, 1-bit out)
	 **/ 
	public Boolean orLMW(Boolean[] multiArrayOne){
		int length = multiArrayOne.length;
		boolean outBit = orL(multiArrayOne[0], multiArrayOne[1]);		

		for(int i = 2; i < length; i++){
			outBit = orL(multiArrayOne[i], outBit);
		}
		return outBit;
	}
	public Boolean andLMW(Boolean[] multiArrayOne){
		int length = multiArrayOne.length;
		boolean outBit = andL(multiArrayOne[0], multiArrayOne[1]);		

		for(int i = 2; i < length; i++){
			outBit = andL(multiArrayOne[i], outBit);
		}
		
		return outBit;
	}
	
	// CHECK OR AND XOR -- MAY NOT WORK CORRECTLY because youre comparing two then comparing the outcome of that to the 3rd and etc...
	public Boolean xorLMW(Boolean[] multiArrayOne){
		int length = multiArrayOne.length;
		boolean outBit = xorL(multiArrayOne[0], multiArrayOne[1]);		

		for(int i = 2; i < length; i++){
			outBit = xorL(multiArrayOne[i], outBit);
		}
		
		return outBit;
	}
	public Boolean norLMW(Boolean[] multiArrayOne){
		int length = multiArrayOne.length;
		boolean outBit = norL(multiArrayOne[0], multiArrayOne[1]);		

		for(int i = 2; i < length; i++){
			outBit = norL(multiArrayOne[i], outBit);
		}
		
		return outBit;
	}
	
	//------------------------------------------Basic Mux & Dmux-----------------------------------------------\\

	public Boolean mux(boolean bitOne, boolean bitTwo, boolean bitSelector){
		return orL(andL(bitOne, notL(bitSelector)), andL(bitTwo, bitSelector));
	}
	public Boolean[] dMux(boolean bitOne, boolean bitSelector){
		Boolean[] dMuxTwoVar = new Boolean[2]; // Two out DeMux array.  Positions [0], [1] to (T or F), (T or F) -- respectively
		dMuxTwoVar[0] = andL(bitOne, notL(bitSelector));
		dMuxTwoVar[1] = andL(bitOne, bitSelector);
		return dMuxTwoVar; // Returns a[0] and b[1]
	}

	//--------------------------------------Multi-Way Mux's & dMux's-------------------------------------------\\

	public Boolean mux4Way(boolean bitOne, boolean bitTwo, boolean bitThree, boolean bitFour, boolean bitSelectorOne, boolean bitSelectorTwo){
		Boolean[] tempArrayOne = new Boolean[4]; // Initiates Array
		
		tempArrayOne[0] = andL(andL(bitOne, notL(bitSelectorOne)), notL(bitSelectorTwo));
		tempArrayOne[1] = andL(andL(bitTwo, notL(bitSelectorOne)), bitSelectorTwo);
		tempArrayOne[2] = andL(andL(bitThree, bitSelectorOne), notL(bitSelectorTwo));
		tempArrayOne[3] = andL(andL(bitFour, bitSelectorOne), bitSelectorTwo);
		return orLMW(tempArrayOne); // Passes to orLMW() to calculate a compounded OR gate with all four tempArrayOne values. 
					    // The return from orLMW() is a single boolean (NOT an array)
	}
	public Boolean[] dMux4Way(boolean bitIn, Boolean[] selectorArray){
		Boolean[] outArray = new Boolean[4];
		boolean bitSelectorOne = selectorArray[0];
		boolean bitSelectorTwo = selectorArray[1];
		
		
		outArray[0] = andL(bitIn, andL(notL(bitSelectorOne), notL(bitSelectorTwo)));
		outArray[1] = andL(bitIn, andL(notL(bitSelectorOne), bitSelectorTwo));
		outArray[2] = andL(bitIn, andL(bitSelectorOne, notL(bitSelectorTwo)));
		outArray[3] = andL(bitIn, andL(bitSelectorOne, bitSelectorTwo));
		return outArray;
	}
	public Boolean[] dMux8Way(boolean bitIn, Boolean[] selectorArray){
		// selectorArray size = 3 (0, 1, 2)
		
		// Selector Array: 
		// [0] = 0 0 0
		// [1] = 0 0 1
		// [2] = 0 1 0
		// [3] = 0 1 1
		// [4] = 1 0 0
		// [5] = 1 0 1
		// [6] = 1 1 0
		// [7] = 1 1 1
		
		Boolean[] outArray = new Boolean[8];
		
		outArray[0] = andL(andL(bitIn, andL(notL(selectorArray[0]), notL(selectorArray[1]))), notL(selectorArray[2]));
		outArray[1] = andL(andL(bitIn, andL(notL(selectorArray[0]), notL(selectorArray[1]))), selectorArray[2]);
		outArray[2] = andL(andL(bitIn, andL(notL(selectorArray[0]), selectorArray[1])), notL(selectorArray[2]));
		outArray[3] = andL(andL(bitIn, andL(notL(selectorArray[0]), selectorArray[1])), selectorArray[2]);
		outArray[4] = andL(andL(bitIn, andL(selectorArray[0], notL(selectorArray[1]))), notL(selectorArray[2]));
		outArray[5] = andL(andL(bitIn, andL(selectorArray[0], notL(selectorArray[1]))), selectorArray[2]);
		outArray[6] = andL(andL(bitIn, andL(selectorArray[0], selectorArray[1])), notL(selectorArray[2]));
		outArray[7] = andL(andL(bitIn, andL(selectorArray[0], selectorArray[1])), selectorArray[2]);
		return outArray;
	}
	public Boolean[] mux4Way16(Boolean[] twoByteArrayOne, Boolean[] twoByteArrayTwo, Boolean[] twoByteArrayThree, Boolean[] twoByteArrayFour, Boolean[] selectorArray){
		Boolean[] tempArrayOne = new Boolean[16];
		Boolean[] tempArrayTwo = new Boolean[16];
		Boolean[] outArray = new Boolean[16];
		
		tempArrayOne = mux16(twoByteArrayOne, twoByteArrayThree, selectorArray[0]);
		tempArrayTwo = mux16(twoByteArrayTwo, twoByteArrayFour, selectorArray[0]);
		outArray = mux16(tempArrayOne, tempArrayTwo, selectorArray[1]);
		
		return outArray;
	}
	public Boolean[] mux8Way16(Boolean[] twoByteArrayOne, Boolean[] twoByteArrayTwo, Boolean[] twoByteArrayThree, Boolean[] twoByteArrayFour, Boolean[] twoByteArrayFive, Boolean[] twoByteArraySix, Boolean[] twoByteArraySeven, Boolean[] twoByteArrayEight, Boolean[] selectorArray){
		
		Boolean[] tempArrayOne = new Boolean[16];
		Boolean[] tempArrayTwo = new Boolean[16];
		Boolean[] outArray = new Boolean[16];
		
		Boolean[] tempSelectorArray = {selectorArray[1], selectorArray[2]};
		boolean tempSelectBit = selectorArray[0];
		
		tempArrayOne = mux4Way16(twoByteArrayOne, twoByteArrayTwo, twoByteArrayThree, twoByteArrayFour, tempSelectorArray);
		tempArrayTwo = mux4Way16(twoByteArrayFive, twoByteArraySix, twoByteArraySeven, twoByteArrayEight, tempSelectorArray);
		
		outArray = mux16(tempArrayOne, tempArrayTwo, tempSelectBit);
		
		return outArray;
	}

	//---------------------------------------16-Bit Bus Mux & Dmux---------------------------------------------\\

	public Boolean[] mux16(Boolean[] twoByteArrayOne, Boolean[] twoByteArrayTwo, boolean bitSelector){
		int length = 16; // 16 bit bus - Array length is 16 (0-15)
		Boolean[] outTwoByteArray = new Boolean[length];
		
		for(int i = 0; i < length; i++){ // For loop so I don't have to type it out. Circuit is still computing logic like native hardware.
			outTwoByteArray[i] = mux(twoByteArrayOne[i], twoByteArrayTwo[i], bitSelector);
		}
		return outTwoByteArray;		
	}
	public Boolean[][] dMux16(Boolean[] twoByteArrayOne, boolean bitSelector){
		int length = 16; // 16 bit bus - Array length is 16 (0-15)
		Boolean[][] outFourByteArray = new Boolean[2][length]; // Two rows tall, each are 2Bytes - total 4Bytes
		
		for(int i = 0; i < length; i++){ // For loop so I don't have to type it out. Circuit is still computing logic like native hardware.
			Boolean[] tempTwoBitArray = dMux(twoByteArrayOne[i], bitSelector); //dMux() returns 1D array [0] = a; [1] = b
			outFourByteArray[1][i] = tempTwoBitArray[0]; 
			outFourByteArray[2][i] = tempTwoBitArray[1];
		}
		return outFourByteArray; // Returns 2D array :: outFourByteArray[0][] = a; outFourByteArray[1][] = b
	}

	//------------------------------------------16bit Bus Logic------------------------------------------------\\

	public Boolean[] not16(Boolean[] twoByteArrayOne){
		int length = 16; // 16 bit bus - Array length is 16 (0-15)
		Boolean[] outTwoByteArray = new Boolean[length];
		
		for(int i = 0; i < length; i++){
			outTwoByteArray[i] = notL(twoByteArrayOne[i]);
		}
		return outTwoByteArray;
	}
	public Boolean[] and16(Boolean[] twoByteArrayOne, Boolean[] twoByteArrayTwo){
		int length = 16; // 16 bit bus - Array length is 16 (0-15)
		Boolean[] outTwoByteArray = new Boolean[length];
		
		for(int i = 0; i < length; i++){
			outTwoByteArray[i] = andL(twoByteArrayOne[i], twoByteArrayTwo[i]);
		}
		return outTwoByteArray;	
	}
	public Boolean[] or16(Boolean[] twoByteArrayOne, Boolean[] twoByteArrayTwo){
		int length = 16; // 16 bit bus - Array length is 16 (0-15)
		Boolean[] outTwoByteArray = new Boolean[length];
		
		for(int i = 0; i < length; i++){
			outTwoByteArray[i] = orL(twoByteArrayOne[i], twoByteArrayTwo[i]);
		}
		return outTwoByteArray;		
	}

	//---------------------------------------------------------------------------------------------------------\\
	//------------------------------------------Boolean Arithmetic---------------------------------------------\\
	//---------------------------------------------------------------------------------------------------------\\
	
	
	public Boolean[] halfAdder(boolean bitOne, boolean bitTwo){
		// Need to output SUM & CARRY ---- Array[0] = SUM; Array[1] = CARRY

		// CARRY = bitOne AND bitTwo
		// SUM = ((bitOne OR bitTwo) AND (bitOne NAND bitTwo))

		Boolean[] outArray = new Boolean[2];
		
		outArray[0] = andL(orL(bitOne, bitTwo), nandL(bitOne, bitTwo));
		outArray[1] = andL(bitOne, bitTwo);
		
		return outArray;
	}
	public Boolean[] fullAdder(boolean bitOne, boolean bitTwo, boolean bitThree){
		// CARRY = fuck it, I did it in code I'm not deciphering it... you can. 
		// SUM = bitOne XOR bitTwo XOR bitThree
		Boolean[] outArray = new Boolean[2];
		outArray[0] = xorL(bitOne, xorL(bitTwo, bitThree));
		outArray[1] = orL(andL(bitOne, bitTwo), andL(bitThree, xorL(bitOne, bitTwo)));
		return outArray;
	}
	public Boolean[] adder16Bit(Boolean[] twoByteArrayOne, Boolean[] twoByteArrayTwo){
		// Return from fullAdder() [0] = SUM :: [1] = CARRY
		int length = 16;
		boolean carryBit = false;
		Boolean[] outArray = new Boolean[length];
		Boolean[] tempArray = new Boolean[2];
		
		// From -15 to -1 because binary is right to left
		for(int i = 15; i > -1; i--){
			tempArray = fullAdder(twoByteArrayOne[i], twoByteArrayTwo[i], carryBit);
			
			outArray[i] = tempArray[0];
			carryBit = tempArray[1];
		}
		
		// NOTE: If you want to add overflow handling, add another column to outArray and make outArray[16] = carryBit
		return outArray;
	}
	public Boolean[] inc16Bit(Boolean[] twoByteArrayOne){
		Boolean[] outArray = new Boolean[16];
		
		// valOneArray is equal to a binary ONE
		// THIS IS A SHITTY IMPLEMENTATION -- WAY TOO MANY NAND GATES REQUIRED FOR THIS, PLEASE REVISE
		Boolean[] valOneArray = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true};
		outArray = adder16Bit(twoByteArrayOne, valOneArray);
		return outArray;
	}
	
	//---------------------------------------------------------------------------------------------------------\\
	//-----------------------------------------------THE ALU---------------------------------------------------\\
	//---------------------------------------------------------------------------------------------------------\\
	
	public Boolean[] logicALU(String _xInput, String _yInput, String _logicBits){
		
		// _xInput = String with 16 1's & 0's
		// _yInput = String with 16 1's & 0's
		// _logicBits = String with 6 1's & 0's
		
		// zx	=	if zx then x = 0
		// nx	=	if nx then x = !x
		// zy	=	if zy then y = 0
		// ny	=	if ny then y = !y
		// f	=	if f then out = x + y else out = x & y
		// no	=	if no then out = !out

		// outArray
		// zr   =   True if out = 0
		// ng   =   True if out < 0
		
		int length = 16;
		Boolean[] logicBits = new Boolean[6];
		Boolean[] xInputArray = new Boolean[16];
		Boolean[] yInputArray = new Boolean[16];
		Boolean[] logicFArray = new Boolean[16];
		Boolean[] outArray = new Boolean[length];
		
		// Converts Strings into Boolean
		for(int i = 0; i < 16; i++){
			if(_xInput.substring(i, i + 1).equals("1")){
				xInputArray[i] = true;
			}else{
				xInputArray[i] = false;
			}
			
			if(_yInput.substring(i, i + 1).equals("1")){
				yInputArray[i] = true;
			}else{
				yInputArray[i] = false;
			}
			
			if(i < 6){
				if(_logicBits.substring(i, i + 1).equals("1")){
					logicBits[i] = true;
				}else{
					logicBits[i] = false;
				}
			}
		}
		
		// Creates and defines individual logic bit variables
		boolean logicZX = logicBits[0];
		boolean logicNX = logicBits[1];
		boolean logicZY = logicBits[2];
		boolean logicNY = logicBits[3];
		boolean logicF = logicBits[4];
		boolean logicNO = logicBits[5];
		
		// ZX & ZY Control Bits
		for(int i = 0; i < length; i++){			
			// ---------------------------------------- Computes both ZX and ZY
			// NOTE: Can do both at the same time because there is no cross function between xInputArray & yInputArray
			xInputArray[i] = notL(nandL(notL(logicZX), xInputArray[i]));
			yInputArray[i] = notL(nandL(notL(logicZY), yInputArray[i]));
		}
		
		// NX & NY Control Bits
		for(int i = 0; i < length; i++){			
			// ---------------------------------------- Computes both NX and NY
			// NOTE: Can do both at the same time because there is no cross function between xInputArray & yInputArray
			xInputArray[i] = xorL(logicNX, xInputArray[i]);
			yInputArray[i] = xorL(logicNY, yInputArray[i]);
		}
		
		// F Logic Bit
		for(int i = 0; i < length; i++){
			logicFArray[i] = logicF;
		}
		outArray = or16(and16(adder16Bit(xInputArray, yInputArray), logicFArray), and16(and16(xInputArray, yInputArray), not16(logicFArray)));
		
		// NO Logic Bit
		for(int i = 0; i < length; i++){
			outArray[i] = xorL(logicNO, outArray[i]);
		}
		
		// System.out.println("NAND CALLS: " + nandcalls);
		nandcalls = 0;
		
		return outArray;
	}
	
	//--------------------------------Output Management & Testing - Not Implementation-----------------------------------\\

	/**
	 * Converts the Boolean Arrays to 0, 1 exclusive Binary Integer Arrays.
	 **/
	public Integer[] convertArrayBinary(Boolean[] TFArray){
		int length = TFArray.length;
		Integer[] convertedTF = new Integer[length];
		
		for(int i = 0; i < length; i++){
			if(TFArray[i] == true)
				convertedTF[i] = 1;
			else
				convertedTF[i] = 0;	
		}
		printArray(convertedTF);
		return convertedTF;
	}

	/**
	 * Converts a single Boolean variable to either 0 or 1
	 **/
	public Integer convertSingleBinary(boolean TFSingle){
		if(TFSingle == true){
			System.out.print("1");
			return 1;
		}
		else{
			System.out.print("0");
			return 0;
		}
	}

	/**
	 * Prints an Integer Array. COMBINE ME WITH convertArrayBinary() (ONLY TIME WE NEED TO CONVERT TO BINARY IS WHEN WE NEED TO PRINT; MIGHT AS WELL COMBINE.)
	 **/
	public void printArray(Integer[] arrayOne){
		int length = arrayOne.length;
		
		for(int i = 0; i < length; i++){
			System.out.print(arrayOne[i]);
		}
		System.out.println();
	}

	public void logicCheck()
	{
	
		// NOT GATE TEST (TESTED CORRECT)
		System.out.println("\n**NOT GATE TEST**\n");
		System.out.print("In: 1 Out: ");
		convertSingleBinary(notL(true));
		System.out.print("\nIn: 0 Out: ");
		convertSingleBinary(notL(false));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// AND GATE TEST (TESTED CORRECT)
		System.out.println("\n**AND GATE TEST**\n");
		System.out.print("In: 00 Out: ");
		convertSingleBinary(andL(false, false));
		System.out.print("\nIn: 01 Out: ");
		convertSingleBinary(andL(false, true));
		System.out.print("\nIn: 10 Out: ");
		convertSingleBinary(andL(true, false));
		System.out.print("\nIn: 11 Out: ");
		convertSingleBinary(andL(true, true));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// OR GATE TEST (TESTED CORRECT)
		System.out.println("\n**OR GATE TEST**\n");
		System.out.print("In: 00 Out: ");
		convertSingleBinary(orL(false, false));
		System.out.print("\nIn: 01 Out: ");
		convertSingleBinary(orL(false, true));
		System.out.print("\nIn: 10 Out: ");
		convertSingleBinary(orL(true, false));
		System.out.print("\nIn: 11 Out: ");
		convertSingleBinary(orL(true, true));		
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// XOR GATE TEST (TESTED CORRECT)
		System.out.println("\n**XOR GATE TEST**\n");
		System.out.print("In: 00 Out: ");
		convertSingleBinary(xorL(false, false));
		System.out.print("\nIn: 01 Out: ");
		convertSingleBinary(xorL(false, true));
		System.out.print("\nIn: 10 Out: ");
		convertSingleBinary(xorL(true, false));
		System.out.print("\nIn: 11 Out: ");
		convertSingleBinary(xorL(true, true));		
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// MUX TEST (TESTED CORRECT)
		System.out.println("\n**MUX TEST**\n");
		System.out.print("In: 000 Out: ");
		convertSingleBinary(mux(false, false, false));
		System.out.print("\nIn: 001 Out: ");
		convertSingleBinary(mux(false, false, true));
		System.out.print("\nIn: 010 Out: ");
		convertSingleBinary(mux(false, true, false));
		System.out.print("\nIn: 011 Out: ");
		convertSingleBinary(mux(false, true, true));
		System.out.print("\nIn: 100 Out: ");
		convertSingleBinary(mux(true, false, false));
		System.out.print("\nIn: 101 Out: ");
		convertSingleBinary(mux(true, false, true));
		System.out.print("\nIn: 110 Out: ");
		convertSingleBinary(mux(true, true, false));
		System.out.print("\nIn: 111 Out: ");
		convertSingleBinary(mux(true, true, true));	
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// DMUX TEST (TESTED CORRECT)
		System.out.println("\n**DMUX TEST**\n");
		System.out.print("In: 00 Out: ");
		convertArrayBinary(dMux(false, false));
		System.out.print("\nIn: 01 Out: ");
		convertArrayBinary(dMux(false, true));
		System.out.print("\nIn: 10 Out: ");
		convertArrayBinary(dMux(true, false));
		System.out.print("\nIn: 11 Out: ");
		convertArrayBinary(dMux(true, true));	
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// -----------------------------------//
		// Defines arrays for following tests //
		// -----------------------------------//
		
		// 0000000000000000
		Boolean[] noneArray = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
		
		// 1111111111111111
		Boolean[] allArray = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
		
		// 1010101010101010
		Boolean[] oneArray = {true, false, true, false, true, false, true, false, true, false, true, false, true, false, true, false};
		
		// 0011110011000011
		Boolean[] twoArray = {false, false, true, true, true, true, false, false, true, true, false, false, false, false, true, true};
		
		// 0001001000110100
		Boolean[] threeArray = {false, false, false, true, false, false, true, false, false, false, true, true, false, true, false, false};
		
		// 0101010101010101
		Boolean[] fourArray = {false, true, false, true, false, true, false, true, false, true, false, true, false, true, false, true};
		
		// 0000111111110000
		Boolean[] fiveArray = {false, false, false, false, true, true, true, true, true, true, true, true, false, false, false, false};
		
		// 1001100001110110
		Boolean[] sixArray = {true, false, false, true, true, false, false, false, false, true, true, true, false, true, true, false};
		
		// 00000000
		Boolean[] sevenArray = {false, false, false, false, false, false, false, false};
		
		// 11111111
		Boolean[] eightArray = {true, true, true, true, true, true, true, true};
		
		// 00010000
		Boolean[] nineArray = {false, false, false, true, false, false, false, false};
		
		// 00000001
		Boolean[] tenArray = {false, false, false, false, false, false, false, true};
		
		// 00100110
		Boolean[] elevenArray = {false, false, true, false, false, true, true, false};
		
		// 0010001101000101
		Boolean[] twelveArray = {false, false, true, false, false, false, true, true, false, true, false, false, false, true, false, true};
		
		// 0011010001010110 
		Boolean[] thirteenArray = {false, false, true, true, false, true, false, false, false, true, false, true, false, true, true, false};
		
		// 0100010101100111
		Boolean[] fourteenArray = {false, true, false, false, false, true, false, true, false, true, true, false, false, true, true, true};
		////////
		// 0101011001111000
		Boolean[] fifteenArray = {false, true, false, true, false, true, true, false, false, true, true, true, true, false, false, false};
		
		// 0110011110001001
		Boolean[] sixteenArray = {false, true, true, false, false, true, true, true, true, false, false, false, true, false, false, true};
		
		// 0111100010011010
		Boolean[] seventeenArray = {false, true, true, true, true, false, false, false, true, false, false, true, true, false, true, false};
		
		// 1000100110101011
		Boolean[] eighteenArray = {true, false, false, false, true, false, false, true, true, false, true, false, true, false, true, true};
		
		// 0000000000000101
		Boolean[] nineteenArray = {false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true};
		
		// 1111111111111011
		Boolean[] twentyArray = {true, true, true, true, true, true, true, true, true, true, true, true, true, false, true, true};
		
		// Selector Arrays
		Boolean[] selectZero = {false, false};
		Boolean[] selectOne = {false, true};
		Boolean[] selectTwo = {true, false};
		Boolean[] selectThree = {true, true};
		
		Boolean[] selectZeroThree = {false, false, false};
		Boolean[] selectOneThree = {false, false, true};
		Boolean[] selectTwoThree = {false, true, false};
		Boolean[] selectThreeThree = {false, true, true};
		Boolean[] selectFourThree = {true, false, false};
		Boolean[] selectFiveThree = {true, false, true};
		Boolean[] selectSixThree = {true, true, false};
		Boolean[] selectSevenThree = {true, true, true};
		
		// NOT16 TEST (TESTED CORRECT)
		System.out.println("\n**NOT16 TEST**\n");
		System.out.print("In: 0000000000000000 Out: ");
		convertArrayBinary(not16(noneArray));
		System.out.print("\nIn: 1111111111111111 Out: ");
		convertArrayBinary(not16(allArray));
		System.out.print("\nIn: 1010101010101010 Out: ");
		convertArrayBinary(not16(oneArray));
		System.out.print("\nIn: 0011110011000011 Out: ");
		convertArrayBinary(not16(twoArray));
		System.out.print("\nIn: 0001001000110100 Out: ");
		convertArrayBinary(not16(threeArray));	
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// AND16 TEST (TESTED CORRECT)
		System.out.println("\n**AND16 TEST**\n");
		System.out.print("In: 0000000000000000 & 0000000000000000 Out: ");
		convertArrayBinary(and16(noneArray, noneArray));
		System.out.print("\nIn: 0000000000000000 & 1111111111111111 Out: ");
		convertArrayBinary(and16(noneArray, allArray));
		System.out.print("\nIn: 1111111111111111 & 1111111111111111 Out: ");
		convertArrayBinary(and16(allArray, allArray));
		System.out.print("\nIn: 1010101010101010 & 0101010101010101 Out: ");
		convertArrayBinary(and16(oneArray, fourArray));
		System.out.print("\nIn: 0011110011000011 & 0000111111110000 Out: ");
		convertArrayBinary(and16(twoArray, fiveArray));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110 Out: ");
		convertArrayBinary(and16(threeArray, sixArray));	
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// OR16 TEST (TESTED CORRECT)
		System.out.println("\n**OR16 TEST**\n");
		System.out.print("In: 0000000000000000 & 0000000000000000 Out: ");
		convertArrayBinary(or16(noneArray, noneArray));
		System.out.print("\nIn: 0000000000000000 & 1111111111111111 Out: ");
		convertArrayBinary(or16(noneArray, allArray));
		System.out.print("\nIn: 1111111111111111 & 1111111111111111 Out: ");
		convertArrayBinary(or16(allArray, allArray));
		System.out.print("\nIn: 1010101010101010 & 0101010101010101 Out: ");
		convertArrayBinary(or16(oneArray, fourArray));
		System.out.print("\nIn: 0011110011000011 & 0000111111110000 Out: ");
		convertArrayBinary(or16(twoArray, fiveArray));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110 Out: ");
		convertArrayBinary(or16(threeArray, sixArray));	
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// MUX16 TEST (TESTED CORRECT)
		System.out.println("\n**MUX16 TEST**\n");
		System.out.print("In: 0000000000000000 & 0000000000000000 & 0 Out: ");
		convertArrayBinary(mux16(noneArray, noneArray, false));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000 & 1 Out: ");
		convertArrayBinary(mux16(noneArray, noneArray, true));
		System.out.print("\nIn: 0000000000000000 & 0001001000110100 & 0 Out: ");
		convertArrayBinary(mux16(noneArray, threeArray, false));
		System.out.print("\nIn: 0000000000000000 & 0001001000110100 & 1 Out: ");
		convertArrayBinary(mux16(noneArray, threeArray, true));
		System.out.print("\nIn: 1001100001110110 & 0000000000000000 & 0 Out: ");
		convertArrayBinary(mux16(sixArray, noneArray, false));
		System.out.print("\nIn: 1001100001110110 & 0000000000000000 & 1 Out: ");
		convertArrayBinary(mux16(sixArray, noneArray, true));	
		System.out.print("\nIn: 1010101010101010 & 0101010101010101 & 0 Out: ");
		convertArrayBinary(mux16(oneArray, fourArray, false));
		System.out.print("\nIn: 1010101010101010 & 0101010101010101 & 1 Out: ");
		convertArrayBinary(mux16(oneArray, fourArray, true));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// OR8WAY TEST (TESTED CORRECTLY)
		System.out.println("\n**OR8WAY TEST**\n");
		System.out.print("In: 00000000 Out: ");
		convertSingleBinary(orLMW(sevenArray));
		System.out.print("\nIn: 11111111 Out: ");
		convertSingleBinary(orLMW(eightArray));
		System.out.print("\nIn: 00010000 Out: ");
		convertSingleBinary(orLMW(nineArray));
		System.out.print("\nIn: 00000001 Out: ");
		convertSingleBinary(orLMW(tenArray));
		System.out.print("\nIn: 00100110 Out: ");
		convertSingleBinary(orLMW(elevenArray));	
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// MUX4WAY16 TEST (TESTED CORRECTLY)
		System.out.println("\n**MUX4WAY16 TEST**\n");
		System.out.print("In: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 00 Out: ");
		convertArrayBinary(mux4Way16(noneArray, noneArray, noneArray, noneArray, selectZero));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 01 Out: ");
		convertArrayBinary(mux4Way16(noneArray, noneArray, noneArray, noneArray, selectOne));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 10 Out: ");
		convertArrayBinary(mux4Way16(noneArray, noneArray, noneArray, noneArray, selectTwo));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 11 Out: ");
		convertArrayBinary(mux4Way16(noneArray, noneArray, noneArray, noneArray, selectThree));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110\n    1010101010101010 & 0101010101010101 & 00 Out: ");
		convertArrayBinary(mux4Way16(threeArray, sixArray, oneArray, fourArray, selectZero));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110\n    1010101010101010 & 0101010101010101 & 01 Out: ");
		convertArrayBinary(mux4Way16(threeArray, sixArray, oneArray, fourArray, selectOne));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110\n    1010101010101010 & 0101010101010101 & 10 Out: ");
		convertArrayBinary(mux4Way16(threeArray, sixArray, oneArray, fourArray, selectTwo));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110\n    1010101010101010 & 0101010101010101 & 11 Out: ");
		convertArrayBinary(mux4Way16(threeArray, sixArray, oneArray, fourArray, selectThree));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// MUX8WAY16 (TESTED CORRECTLY)
		System.out.println("\n**MUX8WAY16 TEST**\n");
		System.out.print("In: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 000 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectZeroThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 001 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectOneThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 010 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectTwoThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 011 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectThreeThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 000 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectFourThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 001 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectFiveThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 010 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectSixThree));
		System.out.print("\nIn: 0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000\n    0000000000000000 & 0000000000000000 & 011 Out: ");
		convertArrayBinary(mux8Way16(noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, noneArray, selectSevenThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 000 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectZeroThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 001 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectOneThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 010 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectTwoThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 011 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectThreeThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 100 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectFourThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 101 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectFiveThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 110 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectSixThree));
		System.out.print("\nIn: 0001001000110100 & 0010001101000101\n    0011010001010110 & 0100010101100111\n    0101011001111000 & 0110011110001001\n    0111100010011010 & 1000100110101011 & 111 Out: ");
		convertArrayBinary(mux8Way16(threeArray, twelveArray, thirteenArray, fourteenArray, fifteenArray, sixteenArray, seventeenArray, eighteenArray, selectSevenThree));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// DMUX4WAY (TESTED CORRECTLY)
		System.out.println("\n**DMUX4WAY TEST**\n");
		System.out.print("In: 0 & 00 Out: ");
		convertArrayBinary(dMux4Way(false, selectZero));
		System.out.print("\nIn: 0 & 01 Out: ");
		convertArrayBinary(dMux4Way(false, selectOne));
		System.out.print("\nIn: 0 & 10 Out: ");
		convertArrayBinary(dMux4Way(false, selectTwo));
		System.out.print("\nIn: 0 & 11 Out: ");
		convertArrayBinary(dMux4Way(false, selectThree));
		System.out.print("\nIn: 1 & 00 Out: ");
		convertArrayBinary(dMux4Way(true, selectZero));
		System.out.print("\nIn: 1 & 01 Out: ");
		convertArrayBinary(dMux4Way(true, selectOne));
		System.out.print("\nIn: 1 & 10 Out: ");
		convertArrayBinary(dMux4Way(true, selectTwo));
		System.out.print("\nIn: 1 & 11 Out: ");
		convertArrayBinary(dMux4Way(true, selectThree));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// DMUX8WAY (TESTED CORRECTLY)
		System.out.println("\n**DMUX8WAY TEST**\n");
		System.out.print("In: 0 & 000 Out: ");
		convertArrayBinary(dMux8Way(false, selectZeroThree));
		System.out.print("\nIn: 0 & 001 Out: ");
		convertArrayBinary(dMux8Way(false, selectOneThree));
		System.out.print("\nIn: 0 & 010 Out: ");
		convertArrayBinary(dMux8Way(false, selectTwoThree));
		System.out.print("\nIn: 0 & 011 Out: ");
		convertArrayBinary(dMux8Way(false, selectThreeThree));
		System.out.print("\nIn: 0 & 100 Out: ");
		convertArrayBinary(dMux8Way(false, selectFourThree));
		System.out.print("\nIn: 0 & 101 Out: ");
		convertArrayBinary(dMux8Way(false, selectFiveThree));
		System.out.print("\nIn: 0 & 110 Out: ");
		convertArrayBinary(dMux8Way(false, selectSixThree));
		System.out.print("\nIn: 0 & 111 Out: ");
		convertArrayBinary(dMux8Way(false, selectSevenThree));
		System.out.print("\nIn: 1 & 000 Out: ");
		convertArrayBinary(dMux8Way(true, selectZeroThree));
		System.out.print("\nIn: 1 & 001 Out: ");
		convertArrayBinary(dMux8Way(true, selectOneThree));
		System.out.print("\nIn: 1 & 010 Out: ");
		convertArrayBinary(dMux8Way(true, selectTwoThree));
		System.out.print("\nIn: 1 & 011 Out: ");
		convertArrayBinary(dMux8Way(true, selectThreeThree));
		System.out.print("\nIn: 1 & 100 Out: ");
		convertArrayBinary(dMux8Way(true, selectFourThree));
		System.out.print("\nIn: 1 & 101 Out: ");
		convertArrayBinary(dMux8Way(true, selectFiveThree));
		System.out.print("\nIn: 1 & 110 Out: ");
		convertArrayBinary(dMux8Way(true, selectSixThree));
		System.out.print("\nIn: 1 & 111 Out: ");
		convertArrayBinary(dMux8Way(true, selectSevenThree));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		System.out.println("---------- Arithemetic Tests ----------");
		
		// HALF ADDER TEST (TESTED CORRECTLY)
		System.out.println("\n**HALF ADDER TEST**\n");
		System.out.print("\nIn: 0 & 0 Out: ");
		convertArrayBinary(halfAdder(false, false));
		System.out.print("\nIn: 0 & 1 Out: ");
		convertArrayBinary(halfAdder(false, true));
		System.out.print("\nIn: 1 & 0 Out: ");
		convertArrayBinary(halfAdder(true, false));
		System.out.print("\nIn: 1 & 1 Out: ");
		convertArrayBinary(halfAdder(true, true));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// FULL ADDER TEST (TESTED CORRECTLY)
		System.out.println("\n**FULL ADDER TEST**\n");
		System.out.print("\nIn: 0 & 0 & 0 Out: ");
		convertArrayBinary(fullAdder(false, false, false));
		System.out.print("\nIn: 0 & 0 & 1 Out: ");
		convertArrayBinary(fullAdder(false, false, true));
		System.out.print("\nIn: 0 & 1 & 0 Out: ");
		convertArrayBinary(fullAdder(false, true, false));
		System.out.print("\nIn: 0 & 1 & 1 Out: ");
		convertArrayBinary(fullAdder(false, true, true));
		System.out.print("\nIn: 1 & 0 & 0 Out: ");
		convertArrayBinary(fullAdder(true, false, false));
		System.out.print("\nIn: 1 & 0 & 1 Out: ");
		convertArrayBinary(fullAdder(true, false, true));
		System.out.print("\nIn: 1 & 1 & 0 Out: ");
		convertArrayBinary(fullAdder(true, true, false));
		System.out.print("\nIn: 1 & 1 & 1 Out: ");
		convertArrayBinary(fullAdder(true, true, true));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// ADD16 TEST (TESTED CORRECTLY)
		System.out.println("\n**HALF ADDER TEST**\n");
		System.out.print("\nIn: 0000000000000000 & 0000000000000000 Out: ");
		convertArrayBinary(adder16Bit(noneArray, noneArray));
		System.out.print("\nIn: 0000000000000000 & 1111111111111111 Out: ");
		convertArrayBinary(adder16Bit(noneArray, allArray));
		System.out.print("\nIn: 1111111111111111 & 1111111111111111 Out: ");
		convertArrayBinary(adder16Bit(allArray, allArray));
		System.out.print("\nIn: 1010101010101010 & 0101010101010101 Out: ");
		convertArrayBinary(adder16Bit(oneArray, fourArray));
		System.out.print("\nIn: 0011110011000011 & 0000111111110000 Out: ");
		convertArrayBinary(adder16Bit(twoArray, fiveArray));
		System.out.print("\nIn: 0001001000110100 & 1001100001110110 Out: ");
		convertArrayBinary(adder16Bit(threeArray, sixArray));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// INCREMENTER16 TEST (TESTED CORRECTLY)
		System.out.println("\n**INCREMENTER TEST**\n");
		System.out.print("\nIn: 0000000000000000 Out: ");
		convertArrayBinary(inc16Bit(noneArray));
		System.out.print("\nIn: 1111111111111111 Out: ");
		convertArrayBinary(inc16Bit(allArray));
		System.out.print("\nIn: 0000000000000101 Out: ");
		convertArrayBinary(inc16Bit(nineteenArray));
		System.out.print("\nIn: 1111111111111011 Out: ");
		convertArrayBinary(inc16Bit(twentyArray));
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		// XOR GATE TEST (TESTED CORRECT)
		System.out.println("\n**XOR GATE TEST**\n");
		System.out.print("In: 00 Out: ");
		convertSingleBinary(xorL(false, false));
		System.out.print("\nIn: 01 Out: ");
		convertSingleBinary(xorL(false, true));
		System.out.print("\nIn: 10 Out: ");
		convertSingleBinary(xorL(true, false));
		System.out.print("\nIn: 11 Out: ");
		convertSingleBinary(xorL(true, true));		
		System.out.println("\n\nNAND CALLS: " + nandcalls + "\n");
		nandcalls = 0;
		
		System.out.println("Press Enter To Continue...");
        new java.util.Scanner(System.in).nextLine();
	}
}
