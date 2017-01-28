//Made in Japan
package org.usfirst.frc.team498.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;
//import edu.wpi.first.wpilibj.networktables.NetworkTable; *garbage*

public class Robot extends SampleRobot {
	Command autonomousCommand;
	SendableChooser autoChooser;

	// DigitBoard Code
	// I2C address of the digit board is 0x70
	I2C i2c = new I2C(Port.kMXP, 0x70);

	// Buttons A and B are keyed to digital inputs 19 and 20
	DigitalInput buttonA = new DigitalInput(19);
	DigitalInput buttonB = new DigitalInput(20);

	// The potentiometer is keyed to AI 3
	AnalogInput pot = new AnalogInput(3);

	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser chooser;
	/*
	 * //Network tables NetworkTable table; *garbage*
	 */
	// Drive
	Ports ports = new Ports();
	private Timer clock;
	FancyJoystick thisStick = new FancyJoystick(0);
	IntakeShooterClimber2017 accessories = new IntakeShooterClimber2017(thisStick, ports);
	Drive2017 drive = new Drive2017(thisStick, ports);
	AutonmousController auto = new AutonmousController(drive, accessories, ports);
	PnuematicsControlModule2017 pcm2017 = new PnuematicsControlModule2017();

	Ultrasonic ultrasonic = new Ultrasonic(0, 1);

	PowerDistributionPanel pdp = new PowerDistributionPanel();

	@Override
	public void robotInit() {
		// table = NetworkTable.getTable("datatable"); *garbage*
		// CameraServer.getInstance().startAutomaticCapture();

		/*
		 * new Thread(() -> { UsbCamera camera =
		 * CameraServer.getInstance().startAutomaticCapture();
		 * camera.setResolution(640, 480);
		 * 
		 * CvSink cvSink = CameraServer.getInstance().getVideo(); CvSource
		 * outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
		 * 
		 * Mat source = new Mat(); Mat output = new Mat();
		 * 
		 * while(true) { cvSink.grabFrame(source); Imgproc.cvtColor(source,
		 * output, Imgproc.COLOR_BGR2GRAY); outputStream.putFrame(output); }
		 * }).start(); }
		 */

		/*
		 * frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
		 * 
		 * sessionfront = NIVision.IMAQdxOpenCamera("cam1",
		 * NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		 * 
		 * sessionback = NIVision.IMAQdxOpenCamera("cam2",
		 * NIVision.IMAQdxCameraControlMode.CameraControlModeController);
		 * 
		 * currSession = sessionfront;
		 * 
		 * NIVision.IMAQdxConfigureGrab(currSession);
		 */

		//DigitBoard
		// set up the board - turn on, set blinking and brightness   
    	byte[] osc = new byte[1];
    	byte[] blink = new byte[1];
    	byte[] bright = new byte[1];
    	osc[0] = (byte)0x21;
    	blink[0] = (byte)0x81;
    	bright[0] = (byte)0xEF;

	i2c.writeBulk(osc);
	Timer.delay(.01);
	i2c.writeBulk(bright);
	Timer.delay(.01);
	i2c.writeBulk(blink);
	Timer.delay(.01);
	
	
		autoChooser = new SendableChooser();
		// autoChooser.addDefault("Mid peg (Red/Blue)", new autoMidPeg());
		// autoChooser.addObject("Experimental auto", new ElevatorPickup());
		SmartDashboard.putData("Autonomous mode chooser", autoChooser);
	}


	// Select which autonomous to run
	public void autonomous() {

		auto.autoInit(-1); // This autonomous method is copied from Unnamed Mark
							// 4

	}

	public void autonomousInit() {
		autonomousCommand = (Command) autoChooser.getSelected();
		autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		switch(autoSelected) 
    	{
	    	case customAuto:
	        //Put custom auto code here   
	            break;
	    	case defaultAuto:
	    	default:
	    	//Put default auto code here
	            break;
    	}
		
	}
	
	//DigitBoard
	public void teleopPeriodic()
    {
    
   // this is the array of all characters - this is not the most efficient way to store the data - but it works for now
    	byte[] byte1 = new byte[10];
    	byte[][] charreg = new byte[36][2]; //charreg is short for character registry
    	charreg[0][0] = (byte)0b00000110; charreg[0][1] = (byte)0b00000000; //1
    	charreg[1][0] = (byte)0b11011011; charreg[1][1] = (byte)0b00000000; //2
    	charreg[2][0] = (byte)0b11001111; charreg[2][1] = (byte)0b00000000; //3
    	charreg[3][0] = (byte)0b11100110; charreg[3][1] = (byte)0b00000000; //4
    	charreg[4][0] = (byte)0b11101101; charreg[4][1] = (byte)0b00000000; //5
    	charreg[5][0] = (byte)0b11111101; charreg[5][1] = (byte)0b00000000; //6
    	charreg[6][0] = (byte)0b00000111; charreg[6][1] = (byte)0b00000000; //7
    	charreg[7][0] = (byte)0b11111111; charreg[7][1] = (byte)0b00000000; //8
    	charreg[8][0] = (byte)0b11101111; charreg[8][1] = (byte)0b00000000; //9
    	charreg[9][0] = (byte)0b00111111; charreg[9][1] = (byte)0b00000000; //0
    	charreg[10][0] = (byte)0b11110111; charreg[10][1] = (byte)0b00000000; //A
    	charreg[11][0] = (byte)0b10001111; charreg[11][1] = (byte)0b00010010; //B
    	charreg[12][0] = (byte)0b00111001; charreg[12][1] = (byte)0b00000000; //C
    	charreg[13][0] = (byte)0b00001111; charreg[13][1] = (byte)0b00010010; //D
    	charreg[14][0] = (byte)0b11111001; charreg[14][1] = (byte)0b00000000; //E
    	charreg[15][0] = (byte)0b11110001; charreg[15][1] = (byte)0b00000000; //F
    	charreg[16][0] = (byte)0b10111101; charreg[16][1] = (byte)0b00000000; //G
    	charreg[17][0] = (byte)0b11110110; charreg[17][1] = (byte)0b00000000; //H
    	charreg[18][0] = (byte)0b00001001; charreg[18][1] = (byte)0b00010010; //I
    	charreg[19][0] = (byte)0b00011110; charreg[19][1] = (byte)0b00000000; //J
    	charreg[20][0] = (byte)0b01110000; charreg[20][1] = (byte)0b00001100; //K
    	charreg[21][0] = (byte)0b00111000; charreg[21][1] = (byte)0b00000000; //L
    	charreg[22][0] = (byte)0b00110110; charreg[22][1] = (byte)0b00000101; //M
    	charreg[23][0] = (byte)0b00110110; charreg[23][1] = (byte)0b00001001; //N
    	charreg[24][0] = (byte)0b00111111; charreg[24][1] = (byte)0b00000000; //O
    	charreg[25][0] = (byte)0b11110011; charreg[25][1] = (byte)0b00000000; //P
    	charreg[26][0] = (byte)0b00111111; charreg[26][1] = (byte)0b00001000; //Q
    	charreg[27][0] = (byte)0b11110011; charreg[27][1] = (byte)0b00001000; //R
    	charreg[28][0] = (byte)0b10001101; charreg[28][1] = (byte)0b00000001; //S
    	charreg[29][0] = (byte)0b00000001; charreg[29][1] = (byte)0b00010010; //T
    	charreg[30][0] = (byte)0b00111110; charreg[30][1] = (byte)0b00000000; //U
    	charreg[31][0] = (byte)0b00110000; charreg[31][1] = (byte)0b00100100; //V
    	charreg[32][0] = (byte)0b00110110; charreg[32][1] = (byte)0b00101000; //W
    	charreg[33][0] = (byte)0b00000000; charreg[33][1] = (byte)0b00101101; //X
    	charreg[34][0] = (byte)0b00000000; charreg[34][1] = (byte)0b00010101; //Y
    	charreg[35][0] = (byte)0b00001001; charreg[35][1] = (byte)0b00100100; //Z
    	
// store the desired characters in a byte array, then write array to the board

// first reset the array to zeros
    	for(int c = 0; c < 10; c++)
    	{
    		byte1[c] = (byte)(0b00000000) & 0xFF;
    	}

// put the character data in the array    	
    	for(int c = 0; c < 9; c++)
    	{
    		byte1[0] = (byte)(0b0000111100001111);
    		byte1[2] = charreg[4*c+3][0];
    		byte1[3] = charreg[4*c+3][1];
    		byte1[4] = charreg[4*c+2][0];
    		byte1[5] = charreg[4*c+2][1];
    		byte1[6] = charreg[4*c+1][0];
    		byte1[7] = charreg[4*c+1][1];
    		byte1[8] = charreg[4*c][0];
    		byte1[9] = charreg[4*c][1];
// send the array to the board
    		i2c.writeBulk(byte1);
    		Timer.delay(3);
    	}
    }
	
	public void operatorControl() {
		/*
		 * //For Network table double x = 0; *garbage* double y = 0;
		 */

		drive.moveValue = 0;
		drive.turnValue = 0;
		auto.gyro.reset();

		TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;

		while (isOperatorControl() && isEnabled()) {
			// network table
			/*
			 * Timer.delay(0.25); table.putNumber("X", x); *garbage*
			 * table.putNumber("Y", y); x += 0.05; y +=1.0;
			 */

			// 2 camera code
			/*
			 * if(button pressing code){ if(currSession == sessionfront){
			 * NIVision.IMAQdxStopAcquisition(currSession); currSession =
			 * sessionback; NIVision.IMAQdxConfigureGrab(currSession); } else
			 * if(currSession == sessionback){
			 * NIVision.IMAQdxStopAcquisition(currSession); currSession =
			 * sessionfront; NIVision.IMAQdxConfigureGrab(currSession); } }
			 */

			pcm2017.turnOn(); // Turns on all PCM ports

			// Checks button
			if (thisStick.getButton(Button.A)) {
				auto.gyro.reset(); // resets gyro
			}

			if (thisStick.getButton(Button.RightBumper)) {
				accessories.Shoot(); // shoots
			}

			if (thisStick.getButton(Button.BACK) && thisStick.getButton(Button.B)) {
				accessories.Climb(); // climbs
			}
			// robot is cancer
			if (thisStick.getButton(Button.Y)) {
				teleMode = TeleOpMode.HIGHGOALALIGNMENT; // aligns robot to high
															// goal
			}
			if (thisStick.getButton(Button.B)) {
				teleMode = TeleOpMode.GEARALIGNMENT; // aligns robot to peg
			}
			if (thisStick.getButton(Button.START)) {
				teleMode = TeleOpMode.OPERATORCONTROL; // makes robot go back to
														// TeleOp
			}
			if (thisStick.getButton(Button.X)) {
				teleMode = TeleOpMode.TEST; // Testing code
			}

			switch (teleMode) {
			case OPERATORCONTROL:
				// Drive the robot via controller
				drive.rampedDriveListener();
				break;
			case GEARALIGNMENT:
				teleMode = auto.AlignGearPeg();
				break;
			case HIGHGOALALIGNMENT:
				teleMode = auto.AlignHighGoal();
				break;
			case TEST:
				auto.testDrive();
				drive.moveValue = 0;
				drive.turnValue = 0;
				break;
			}

			// Send stats to the driver
			print();
		}

	}

	public void disabled() {
		while (isDisabled()) {
			print();
		}

	}

	// Sends information to the driver
	private void print() {

		SmartDashboard.putNumber("Gyro Angle", auto.gyro.getAngle());
		SmartDashboard.putNumber("Gyro getRate()", auto.gyro.getRate());
		SmartDashboard.putNumber("Range (Inches)", ultrasonic.getRangeInches());
		SmartDashboard.putNumber("Range millimeters (Analog)", auto.analogSensor.GetRangeMM());
		SmartDashboard.putNumber("Range Inches (Analog)", auto.analogSensor.GetRangeInches());
		SmartDashboard.putNumber("Voltage (Analog)", auto.analogSensor.GetVoltage());

		// These should print out GRIP's contour info into Dashboard
		SmartDashboard.putNumber("Contour1 CenterX", auto.vision.GetContour1CenterX());
		SmartDashboard.putNumber("Contour1 CenterY", auto.vision.GetContour1CenterY());
		SmartDashboard.putNumber("Contour1 Height", auto.vision.GetContour1Height());
		SmartDashboard.putNumber("Contour2 CenterX", auto.vision.GetContour2CenterX());
		SmartDashboard.putNumber("Contour2 CenterY", auto.vision.GetContour2CenterY());
		SmartDashboard.putNumber("Contour2 Height", auto.vision.GetContour2Height());

		SmartDashboard.putBoolean("flag", auto.vision.flag);
		
		//DigitBoard
		SmartDashboard.putString("DB/String 1", "ButtonA = " + buttonA.get());
		SmartDashboard.putString("DB/String 2", "ButtonB = " + buttonB.get());
		SmartDashboard.putString("DB/String 3", "Analog = " + pot.getAverageValue());
		Timer.delay(.01);

		// MXPDigitBoard.directWrite(data);

		// SmartDashboard.putNumber("Ramp Clock",
		// drive.forwardDriveRamp.clock.get());

		// 2 camera code
		/*
		 * 8NIVision.IMAQdxGrab(currSession, frame, 1);
		 * CameraServer.getInstance().setImage(frame);
		 */
	}

}
