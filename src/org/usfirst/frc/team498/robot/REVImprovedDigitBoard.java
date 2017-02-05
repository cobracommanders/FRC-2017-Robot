package org.usfirst.frc.team498.robot;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C.Port;

public class REVImprovedDigitBoard {

	byte[][] charreg;
	Map charmap;
	I2C i2c;
	AnalogInput pot;
	DigitalInput buttonA;
	DigitalInput buttonB;
	
	String scrollMsg;
	int scrollOffset = 0;

	public REVImprovedDigitBoard() {
		charreg = new byte[38][2]; // charreg is short for character registry
		charmap = new HashMap<Character, Integer>();

		charreg[0][0] = (byte) 0b00111111;
		charreg[9][1] = (byte) 0b00000000; // 0
		charmap.put('0', 0);
		charreg[1][0] = (byte) 0b00000110;
		charreg[0][1] = (byte) 0b00000000; // 1
		charmap.put('1', 1);
		charreg[2][0] = (byte) 0b11011011;
		charreg[1][1] = (byte) 0b00000000; // 2
		charmap.put('2', 2);
		charreg[3][0] = (byte) 0b11001111;
		charreg[2][1] = (byte) 0b00000000; // 3
		charmap.put('3', 3);
		charreg[4][0] = (byte) 0b11100110;
		charreg[3][1] = (byte) 0b00000000; // 4
		charmap.put('4', 4);
		charreg[5][0] = (byte) 0b11101101;
		charreg[4][1] = (byte) 0b00000000; // 5
		charmap.put('5', 5);
		charreg[6][0] = (byte) 0b11111101;
		charreg[5][1] = (byte) 0b00000000; // 6
		charmap.put('6', 6);
		charreg[7][0] = (byte) 0b00000111;
		charreg[6][1] = (byte) 0b00000000; // 7
		charmap.put('7', 7);
		charreg[8][0] = (byte) 0b11111111;
		charreg[7][1] = (byte) 0b00000000; // 8
		charmap.put('8', 8);
		charreg[9][0] = (byte) 0b11101111;
		charreg[8][1] = (byte) 0b00000000; // 9
		charmap.put('9', 9);

		charreg[10][0] = (byte) 0b11110111;
		charreg[10][1] = (byte) 0b00000000; // A
		charmap.put('A', 10);
		charreg[11][0] = (byte) 0b10001111;
		charreg[11][1] = (byte) 0b00010010; // B
		charmap.put('B', 11);
		charreg[12][0] = (byte) 0b00111001;
		charreg[12][1] = (byte) 0b00000000; // C
		charmap.put('C', 12);
		charreg[13][0] = (byte) 0b00001111;
		charreg[13][1] = (byte) 0b00010010; // D
		charmap.put('D', 13);
		charreg[14][0] = (byte) 0b11111001;
		charreg[14][1] = (byte) 0b00000000; // E
		charmap.put('E', 14);
		charreg[15][0] = (byte) 0b11110001;
		charreg[15][1] = (byte) 0b00000000; // F
		charmap.put('F', 15);
		charreg[16][0] = (byte) 0b10111101;
		charreg[16][1] = (byte) 0b00000000; // G
		charmap.put('G', 16);
		charreg[17][0] = (byte) 0b11110110;
		charreg[17][1] = (byte) 0b00000000; // H
		charmap.put('H', 17);
		charreg[18][0] = (byte) 0b00001001;
		charreg[18][1] = (byte) 0b00010010; // I
		charmap.put('I', 18);
		charreg[19][0] = (byte) 0b00011110;
		charreg[19][1] = (byte) 0b00000000; // J
		charmap.put('J', 19);
		charreg[20][0] = (byte) 0b01110000;
		charreg[20][1] = (byte) 0b00001100; // K
		charmap.put('K', 20);
		charreg[21][0] = (byte) 0b00111000;
		charreg[21][1] = (byte) 0b00000000; // L
		charmap.put('L', 21);
		charreg[22][0] = (byte) 0b00110110;
		charreg[22][1] = (byte) 0b00000101; // M
		charmap.put('M', 22);
		charreg[23][0] = (byte) 0b00110110;
		charreg[23][1] = (byte) 0b00001001; // N
		charmap.put('N', 23);
		charreg[24][0] = (byte) 0b00111111;
		charreg[24][1] = (byte) 0b00000000; // O
		charmap.put('O', 24);
		charreg[25][0] = (byte) 0b11110011;
		charreg[25][1] = (byte) 0b00000000; // P
		charmap.put('P', 25);
		charreg[26][0] = (byte) 0b00111111;
		charreg[26][1] = (byte) 0b00001000; // Q
		charmap.put('Q', 26);
		charreg[27][0] = (byte) 0b11110011;
		charreg[27][1] = (byte) 0b00001000; // R
		charmap.put('R', 27);
		charreg[28][0] = (byte) 0b10001101;
		charreg[28][1] = (byte) 0b00000001; // S
		charmap.put('S', 28);
		charreg[29][0] = (byte) 0b00000001;
		charreg[29][1] = (byte) 0b00010010; // T
		charmap.put('T', 29);
		charreg[30][0] = (byte) 0b00111110;
		charreg[30][1] = (byte) 0b00000000; // U
		charmap.put('U', 30);
		charreg[31][0] = (byte) 0b00110000;
		charreg[31][1] = (byte) 0b00100100; // V
		charmap.put('V', 31);
		charreg[32][0] = (byte) 0b00110110;
		charreg[32][1] = (byte) 0b00101000; // W
		charmap.put('W', 32);
		charreg[33][0] = (byte) 0b00000000;
		charreg[33][1] = (byte) 0b00101101; // X
		charmap.put('X', 33);
		charreg[34][0] = (byte) 0b00000000;
		charreg[34][1] = (byte) 0b00010101; // Y
		charmap.put('Y', 34);
		charreg[35][0] = (byte) 0b00001001;
		charreg[35][1] = (byte) 0b00100100; // Z
		charmap.put('Z', 35);
		charreg[36][0] = (byte) 0b00000000;
		charreg[36][1] = (byte) 0b00000000; // space
		charmap.put(' ', 36);
		charreg[37][0] = (byte) 0b11000000;
		charreg[37][1] = (byte) 0b00000000; // -
		charmap.put('-', 37);

		i2c = new I2C(Port.kMXP, 0x70); // 0x70 0xA8

		buttonA = new DigitalInput(19); // Original 19
		buttonB = new DigitalInput(20); // Original 20
		pot = new AnalogInput(7); // Original 3

		byte[] osc = new byte[1];
		byte[] blink = new byte[1];
		byte[] bright = new byte[1];
		osc[0] = (byte) 0x21; // 0x21
		blink[0] = (byte) 0x81; // 0x81
		bright[0] = (byte) 0xEF; // 0xEF

		i2c.writeBulk(osc);
		Timer.delay(.06);
		i2c.writeBulk(bright);
		Timer.delay(.06);
		i2c.writeBulk(blink);
		Timer.delay(.06);

	}

	//Set a static 4 characters to the display
	public void UpdateDisplay(int num1, int num2, int num3, int num4) {
		System.out.println("Update Display");
		int[] charz = new int[] { num1, num2, num3, num4 };
		byte[] byte1 = new byte[10];
		byte1[0] = (byte) (0b0000111100001111);
		byte1[2] = charreg[charz[3]][0];
		byte1[3] = charreg[charz[3]][1];
		byte1[4] = charreg[charz[2]][0];
		byte1[5] = charreg[charz[2]][1];
		byte1[6] = charreg[charz[1]][0];
		byte1[7] = charreg[charz[1]][1];
		byte1[8] = charreg[charz[0]][0];
		byte1[9] = charreg[charz[0]][1];
		// send the array to the board\ \
		i2c.writeBulk(byte1);

	}
	
	//Sets scrolling message
	public void CreateScrollMsg(String msg) {
		System.out.println("Create Scroll Msg");
		scrollMsg = msg;
		scrollOffset = 0;
	}
	
	//Moves message along path by one character
	public void SlideScrollMsg() {

		UpdateDisplay(scrollMsg.charAt(scrollOffset % (scrollMsg.length() - 1)),scrollMsg.charAt((scrollOffset + 1) % (scrollMsg.length() - 1)),scrollMsg.charAt((scrollOffset + 2) % (scrollMsg.length() - 1)),scrollMsg.charAt((scrollOffset + 3) % (scrollMsg.length() - 1)));
		scrollOffset++;
		
	}
	
	//Sets static 4 characters to the display
	public void UpdateDisplay(char char1, char char2, char char3, char char4)
	{		
		UpdateDisplay(CharToNum(char1),CharToNum(char2),CharToNum(char3),CharToNum(char4));		
	}
	
	public void UpdateDisplay(char[] charz)
	{
		if(charz.length == 4)
		UpdateDisplay(CharToNum(charz[0]),CharToNum(charz[1]),CharToNum(charz[2]),CharToNum(charz[3]));		
	}
	
	//Converts a character to its equivelent int value for display
	private int CharToNum(char letter) {
		letter = Character.toUpperCase(letter);
		switch(letter) {
		case 'A':
			return 10;
		case 'B':
			return 11;
		case 'C':
			return 12;
		case 'D':
			return 13;
		case 'E':
			return 14;
		case 'F':
			return 15;
		case 'G':
			return 16;
		case 'H':
			return 17;
		case 'I':
			return 18;
		case 'J':
			return 19;
		case 'K':
			return 20;
		case 'L':
			return 21;
		case 'M':
			return 22;
		case 'N':
			return 23;
		case 'O':
			return 24;
		case 'P':
			return 25;
		case 'Q':
			return 26;
		case 'R':
			return 27;
		case 'S':
			return 28;
		case 'T':
			return 29;
		case 'U':
			return 30;
		case 'V':
			return 31;
		case 'W':
			return 32;
		case 'X':
			return 33;
		case 'Y':
			return 34;
		case 'Z':
			return 35;
		case '0':
			return 0;
		case '1':
			return 1;
		case '2':
			return 2;
		case '3':
			return 3;
		case '4':
			return 4;
		case '5':
			return 5;
		case '6':
			return 6;
		case '7':
			return 7;
		case '8':
			return 8;
		case '9':
			return 9;
		case '-':
			return 37;
			default:
				return 36;
		
		}
		
		
	}
	public boolean getButtonA() {
		return !buttonA.get();
	}

	boolean getButtonB() {
		return !buttonB.get();
	}

	double getPot() {
		return pot.getVoltage();
	}
}
