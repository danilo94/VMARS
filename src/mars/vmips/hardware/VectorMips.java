package mars.vmips.hardware;

import java.util.Observer;
import mars.Globals;
import mars.util.Binary;

public class VectorMips {

	public static VectorRegister[] registers = {

	        new VectorRegister("$V0", 0, new byte[16]),
			new VectorRegister("$V1", 1, new byte[16]),
			new VectorRegister("$V2", 2, new byte[16]),
			new VectorRegister("$V3", 3, new byte[16]),
			new VectorRegister("$V4", 4, new byte[16]),
			new VectorRegister("$V5", 5, new byte[16]),
			new VectorRegister("$V6", 6, new byte[16]),
			new VectorRegister("$V7", 7, new byte[16]),
			new VectorRegister("$V8", 8, new byte[16]),
			new VectorRegister("$V9", 9, new byte[16]),
			new VectorRegister("$V10", 10, new byte[16]),
			new VectorRegister("$V11", 11, new byte[16]),
			new VectorRegister("$V12", 12, new byte[16]),
			new VectorRegister("$V13", 13, new byte[16]),
			new VectorRegister("$V14", 14, new byte[16]),
			new VectorRegister("$V15", 15, new byte[16]),
			new VectorRegister("$V16", 16, new byte[16]),
			new VectorRegister("$V17", 17, new byte[16]),
			new VectorRegister("$V18", 18, new byte[16]),
			new VectorRegister("$V19", 19, new byte[16]),
			new VectorRegister("$V20", 20, new byte[16]),
			new VectorRegister("$V21", 21, new byte[16]),
			new VectorRegister("$V22", 22, new byte[16]),
			new VectorRegister("$V23", 23, new byte[16]),
			new VectorRegister("$V24", 24, new byte[16]),
			new VectorRegister("$V25", 25, new byte[16]),
			new VectorRegister("$V26", 26, new byte[16]),
			new VectorRegister("$V27", 27, new byte[16]),
			new VectorRegister("$V28", 28, new byte[16]),
			new VectorRegister("$V29", 29, new byte[16]),
			new VectorRegister("$V30", 30, new byte[16]),
			new VectorRegister("$V31", 31, new byte[16]) };
	
	public static void setRegisterToDoubleWords(String nameRegister, long[] values){
		setRegisterToDoubleWords(getRegisterNumber(nameRegister),values);
	}
	public static void setRegisterToDoubleWords(int numRegister, long[] values) {
		if (values.length != 2)
			return;
		byte[] bytes = new byte[16];
		for (int i = 0; i < 8; i++) {
			bytes[i] = Binary.getByte(values[0], i);
		}
		for (int i = 8; i < 16; i++) {
			bytes[i] = Binary.getByte(values[1], i-8);
		}
		registers[numRegister].setValues(bytes);
	}
	public static void setRegisterToWords(String nameRegister, int[] values){
		setRegisterToWords(getRegisterNumber(nameRegister),values);
	}
	public static void setRegisterToWords(int numRegister, int[] values) {
		if (values.length != 4)
			return;
		byte[] bytes = new byte[16];
		for (int i = 0; i < 4; i++) {
			bytes[i] = Binary.getByte(values[0], i);
		}
		for (int i = 4; i < 8; i++) {
			bytes[i] = Binary.getByte(values[1], i-4);
		}
		for (int i = 8; i < 12; i++) {
			bytes[i] = Binary.getByte(values[2], i-8);
		}
		for (int i = 12; i < 16; i++) {
			bytes[i] = Binary.getByte(values[3], i-12);
		}
		
		registers[numRegister].setValues(bytes);
	}
	public static void setRegisterToHalfWords(String nameRegister, short[] values){
		setRegisterToHalfWords(getRegisterNumber(nameRegister),values);
	}
	public static void setRegisterToHalfWords(int numRegister, short[] values) {
		if (values.length != 8)
			return;
		byte[] bytes = new byte[16];
		for (int i = 0; i < 2; i++) {
			bytes[i] = Binary.getByte(values[0], i);
		}
		for (int i = 2; i < 4; i++) {
			bytes[i] = Binary.getByte(values[1], i-4);
		}
		for (int i = 4; i < 6; i++) {
			bytes[i] = Binary.getByte(values[2], i-8);
		}
		for (int i = 6; i < 8; i++) {
			bytes[i] = Binary.getByte(values[3], i-12);
		}
		for (int i = 8; i < 10; i++) {
			bytes[i] = Binary.getByte(values[4], i);
		}
		for (int i = 10; i < 12; i++) {
			bytes[i] = Binary.getByte(values[5], i-4);
		}
		for (int i = 12; i < 14; i++) {
			bytes[i] = Binary.getByte(values[6], i-8);
		}
		for (int i = 14; i < 16; i++) {
			bytes[i] = Binary.getByte(values[7], i-12);
		}
		
		registers[numRegister].setValues(bytes);
	}
	public static void setRegisterToBytes(String nameRegister, byte[] values){
		setRegisterToBytes(getRegisterNumber(nameRegister),values);
	}
	public static void setRegisterToBytes(int numRegister, byte[] values) {
		if (values.length != 16 )
			return;
		registers[numRegister].setValues(values);
	}
	public static long[] getDoubleWordsFromRegister(String nameRegister) {
		return getDoubleWordsFromRegister(getRegisterNumber(nameRegister));
	}

	public static long[] getDoubleWordsFromRegister(int numRegister) {
		byte[] bytes = registers[numRegister].getValues();
		long[] Dwords = new long[2];
		for (int i = 0; i < 2; i++) {

			Dwords[i] = bytes[7 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] = bytes[6 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] = bytes[5 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] = bytes[4 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] = bytes[3 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] |= bytes[2 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] |= bytes[1 + (i * 8)];
			Dwords[i] <<= 8;
			Dwords[i] |= bytes[0 + (i * 8)];

		}

		return Dwords;
	}

	public static int[] getWordsFromRegister(String nameRegister) {
		return getWordsFromRegister(getRegisterNumber(nameRegister));
	}

	public static int[] getWordsFromRegister(int numRegister) {
		byte[] bytes = registers[numRegister].getValues();
		int[] words = new int[4];
		for (int i = 0; i < 4; i++) {
			words[i] = bytes[3 + (i * 4)];
			words[i] <<= 8;
			words[i] |= bytes[2 + (i * 4)];
			words[i] <<= 8;
			words[i] |= bytes[1 + (i * 4)];
			words[i] <<= 8;
			words[i] |= bytes[0 + (i * 4)];

		}

		return words;
	}

	public static short[] getHalfWordsFromRegister(String nameRegister) {
		return getHalfWordsFromRegister(getRegisterNumber(nameRegister));
	}

	public static short[] getHalfWordsFromRegister(int numRegister) {
		byte[] bytes = registers[numRegister].getValues();
		short[] halWords = new short[8];
		for (int i = 0; i < 8; i++) {
			halWords[i] = bytes[1 + (i * 2)];
			halWords[i] <<= 8;
			halWords[i] |= bytes[0 + (i * 2)];
		}

		return halWords;
	}

	public static byte[] getBytesFromRegister(String nameRegister) {
		return getValues(getRegisterNumber(nameRegister));
	}

	/**
	 * Method for displaying the register values for debugging.
	 **/

	public static void showRegisters() {
		for (int i = 0; i < registers.length; i++) {

			System.out.println("Name: " + registers[i].getName());
			System.out.println("Number: " + registers[i].getNumber());
			System.out
					.println("Values: " + registers[i].getValues().toString());
			System.out.println("");
		}
	}

	/**
	 * This method updates the VectorMips register value who's number is num.
	 * 
	 * @param num
	 *            VectorMips register to set the value of.
	 * @param val
	 *            The desired bytes[] value for the register.
	 **/

	public static byte[] updateRegister(int num, byte[] val) {
		byte[] old = null;
		for (int i = 0; i < registers.length; i++) {
			if (registers[i].getNumber() == num) {
				old = (Globals.getSettings().getBackSteppingEnabled()) ? Globals.program
						.getBackStepper().addVectMipsRestore(num,
								registers[i].setValues(val)) : registers[i]
						.setValues(val);
				break;
			}
		}
		return old;
	}

	/**
	 * Returns the value of the FPU register who's number is num. Returns the
	 * raw int value actually stored there. If you need a float, use
	 * Float.intBitsToFloat() to get the equivent float.
	 * 
	 * @param num
	 *            The FPU register number.
	 * @return The int value of the given register.
	 **/

	public static byte[] getValues(int num) {
		return registers[num].getValues();
	}

	/**
	 * For getting the number representation of the FPU register.
	 * 
	 * @param n
	 *            The string formatted register name to look for.
	 * @return The number of the register represented by the string.
	 **/

	public static int getRegisterNumber(String n) {
		int j = -1;
		for (int i = 0; i < registers.length; i++) {
			if (registers[i].getName().equals(n)) {
				j = registers[i].getNumber();
				break;
			}
		}
		return j;
	}
	/**
	 * For returning the set of registers.
	 * 
	 * @return The set of registers.
	 **/

	public static VectorRegister[] getRegisters() {
		return registers;
	}

	/**
	 * Get register object corresponding to given name. If no match, return
	 * null.
	 * 
	 * @param rName
	 *            The VectorMips register name, must be "$V0" through "$V31".
	 * @return The register object,or null if not found.
	 **/
	public static VectorRegister getRegister(String rName) {
		VectorRegister reg = null;
		if (rName.charAt(0) == '$' && rName.length() > 1
				&& rName.charAt(1) == 'V') {
			try {
				// check for register number 0-31.
				reg = registers[Binary.stringToInt(rName.substring(2))]; // KENV
																			// 1/6/05
			} catch (Exception e) {
				// handles both NumberFormat and ArrayIndexOutOfBounds
				reg = null;
			}
		}
		return reg;

	}

	/**
	 * Method to reinitialize the values of the registers.
	 **/
	public static void resetRegisters() {
		// TODO Auto-generated method stub
		for (int i = 0; i < registers.length; i++)
			registers[i].resetValues();
	}

	/**
	 * Each individual register is a separate object and Observable. This handy
	 * method will add the given Observer to each one.
	 */
	public static void addRegistersObserver(Observer observer) {
		for (int i = 0; i < registers.length; i++) {
			registers[i].addObserver(observer);
		}
	}

	/**
	 * Each individual register is a separate object and Observable. This handy
	 * method will delete the given Observer from each one.
	 */
	public static void deleteRegistersObserver(Observer observer) {
		for (int i = 0; i < registers.length; i++) {
			registers[i].deleteObserver(observer);
		}
	}



}
