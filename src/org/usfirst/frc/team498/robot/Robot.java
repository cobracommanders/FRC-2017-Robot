//Made in Japan
package org.usfirst.frc.team498.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionThread;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.networktables.NetworkTable; *garbage*

public class Robot extends SampleRobot {
	// Command autonomousCommand;

	// Drive
	Ports ports = new Ports();
	private Timer clock = new Timer();
	private Timer digitClock = new Timer();
	FancyJoystick thisStick = new FancyJoystick(0);
	Drive2017 drive2017 = new Drive2017(thisStick, ports);
	REVImprovedDigitBoard digitBoard = new REVImprovedDigitBoard();
	public boolean hasDigitStarted = false;

	PewPew2017 shooter = new PewPew2017(digitBoard, thisStick, ports);
	AnalogUltrasonicSensor2017 ultra = new AnalogUltrasonicSensor2017(thisStick, ports);
	AutonmousController auto = new AutonmousController(drive2017, shooter, digitBoard, thisStick, ports, ultra, clock);

	GearIntake2017 gearIntake = new GearIntake2017(thisStick, ports);
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	CameraVision2017 vision = new CameraVision2017();
	
	//Camera Code
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	
	private VisionThread visionThread;
	private double centerX = 0.0;
	
	private final Object imgLock = new Object();
	private RobotDrive drive;
	
	

	boolean dToggle = false;

	// boolean intakeToggle = false;
	// boolean xDown = false;

	@Override
	public void robotInit() {

		// table = NetworkTable.getTable("datatable"); *garbage*
		// CameraServer.getInstance().startAutomaticCapture();

		//Camera Code
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
	    camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
	    
	    visionThread = new VisionThread(camera, new GearPipeline(), pipeline -> {
	        if (!pipeline.filterContoursOutput().isEmpty()) {
	            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
	            synchronized (imgLock) {
	                centerX = r.x + (r.width / 2);
	            }
	        }
	    });
	    visionThread.start();
	        
	    drive = new RobotDrive(1, 2);
	}

	// 2 USB Cameras
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

	/*
	 * public boolean xDown() { if (!xDown && thisStick.getButton(Button.X)) {
	 * //xDown = thisStick.getButton(Button.X); //
	 * System.out.println("Should have set to true"); return true; } else {
	 * xDown = thisStick.getButton(Button.X); return false; } }
	 */

	// Select which autonomous to run
	
	//Camera Code
	//@Override
	public void autonomousPeriodic() {
		double centerX;
		synchronized (imgLock) {
			centerX = this.centerX;
		}
		double turn = centerX - (IMG_WIDTH / 2);
		drive.arcadeDrive(-0.6, turn * 0.005);
	}
	
	public void autonomous() {

		while (isAutonomous() && isEnabled()) {
			print();
			// Another bonding moment
			auto.Auto();
		}

		while (isAutonomous() && !isEnabled()) {
			auto.phase = 0;
		}

		// auto.Auto();
		// auto.autoInit(-1); // This autonomous method is copied from Unnamed
		// Mark
		// 4

	}

	int count = 0;
	int count2 = 0;

	public char[] DisplayVoltageConversion() {
		double voltage = pdp.getVoltage();
		voltage *= 10;
		voltage = Math.floor(voltage);
		voltage /= 10;
		char[] charz = new char[4];
		charz[0] = ConvertNumToChar((int) (voltage / 10));
		charz[1] = ConvertNumToChar((int) (voltage % 10));
		charz[2] = ConvertNumToChar((int) (voltage * 10 % 10));
		charz[3] = 'V';
		return charz;
	}

	public char ConvertNumToChar(int num) {
		switch (num) {
		case 0:
			return '0';
		case 1:
			return '1';
		case 2:
			return '2';
		case 3:
			return '3';
		case 4:
			return '4';
		case 5:
			return '5';
		case 6:
			return '6';
		case 7:
			return '7';
		case 8:
			return '8';
		case 9:
			return '9';
		}
		return '0';
	}

	public void operatorControl() {

		// For Network table double x = 0; *garbage* double y = 0;

		drive2017.moveValue = 0;
		drive2017.turnValue = 0;
		// auto.gyro.reset();

		TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;

		while (isOperatorControl() && isEnabled()) {

			if (!hasDigitStarted) {
				digitClock.start();
				hasDigitStarted = true;
				digitBoard.UpdateDisplay(DisplayVoltageConversion(), true);
			}
			if (digitClock.get() > 3) {
				digitClock.start();
				digitBoard.UpdateDisplay(DisplayVoltageConversion(), true);
			}

			// Checks button

			// if (thisStick.getButton(Button.B)) {
			// teleMode = TeleOpMode.TEST; // drives straight w/ gyro
			// }
			if (thisStick.getButton(Button.START)) {
				teleMode = TeleOpMode.OPERATORCONTROL; // makes robot go back to
														// TeleOp
			}

			switch (teleMode) {
			case OPERATORCONTROL:
				// Drive the robot via controller
				// drive.rampedDriveListener();
				gearIntake.Listener();
				shooter.shootListener();
				break;
			case GEARALIGNMENT:
				// teleMode = auto.AlignGearPeg();
				break;
			case HIGHGOALALIGNMENT:
				// teleMode = auto.AlignHighGoal();
				break;
			case TEST:
				auto.autoDriveForward();
				drive2017.moveValue = 0;
				drive2017.turnValue = 0;
				break;
			}

			// Send stats to the driver
			// Randy made this a bonding moment
			print();
		}

	}

	public void disabled() {
		while (isDisabled()) {
			if (digitBoard.getButtonA()) {
				Timer.delay(0.25);
				auto.autonomousSelector(); // Displays auto on digit board
			}
			if (digitBoard.getButtonB()) {
				Timer.delay(0.25);
				/*
				 * dToggle = !dToggle; if (dToggle)
				 * digitBoard.UpdateDisplay('C', 'U', 'C', 'K'); else
				 * digitBoard.UpdateDisplay('8', '-', '-', 'D');
				 */
			}

		}

	}

	// Sends information to the driver
	private void print() {

		// TODO Just so we can click here
		// SmartDashboard.putNumber("Gyro Angle", auto.gyro.getAngle());
		// SmartDashboard.putNumber("Gyro Angle", auto.gyro.getAngle());
		// SmartDashboard.putNumber("Gyro getRate()", auto.gyro.getRate());
		// SmartDashboard.putNumber("Gyro Converted Angle",
		// auto.ConvertGyroStuff(auto.gyro.getAngle()));

		// SmartDashboard.putBoolean("intakeToggle", intakeToggle);
		// SmartDashboard.putBoolean("xDown", xDown);

		// SmartDashboard.putBoolean("output", xDown());
		SmartDashboard.putNumber("Ultrasonic value", ultra.getValue());
		SmartDashboard.putNumber("Ultrasonic Inches", ultra.GetRangeInches());
		SmartDashboard.putNumber("Ultrasonic Voltage", ultra.GetVoltage());

		SmartDashboard.putNumber("Shooter value", digitBoard.getPot());

		try {
			for(int i = 0; i < CameraVision2017.boxes.size(); i++) {
				SmartDashboard.putNumber("Boxes", CameraVision2017.boxes.size());
				SmartDashboard.putNumber("Box Height" + String.valueOf(CameraVision2017.boxes.get(i)), CameraVision2017.boxes.get(i).height);
				SmartDashboard.putNumber("Box Width" + String.valueOf(CameraVision2017.boxes.get(i)), CameraVision2017.boxes.get(i).width);
			}
		}
		catch (Exception e) {
			SmartDashboard.putString("Error", e.getMessage());
		}
		/*try {
			SmartDashboard.putNumber("Contours", Vision2017.matPointStuff.size());
		} catch (Exception e) {
			System.out.println("Contour Count error");
			System.out.println(e);
		}*/
		
		
		// SmartDashboard.putNumber("Network Table Value",
		// auto.netTable.getDouble("test"));

		/*
		 * SmartDashboard.putNumber("Range millimeters (Analog)",
		 * auto.analogSensor.GetRangeMM());
		 * SmartDashboard.putNumber("Range Inches (Analog)",
		 * auto.analogSensor.GetRangeInches());
		 * SmartDashboard.putNumber("Voltage (Analog)",
		 * auto.analogSensor.GetVoltage());
		 */
		// These should print out GRIP's contour info into Dashboard
		// SmartDashboard.putNumber("Contour1 CenterX",
		// auto.vision.GetContour1CenterX());
		// SmartDashboard.putNumber("Contour1 CenterY",
		// auto.vision.GetContour1CenterY());
		// SmartDashboard.putNumber("Contour1 Height",
		// auto.vision.GetContour1Height());
		// SmartDashboard.putNumber("Contour2 CenterX",
		// auto.vision.GetContour2CenterX());
		// SmartDashboard.putNumber("Contour2 CenterY",
		// auto.vision.GetContour2CenterY());
		// SmartDashboard.putNumber("Contour2 Height",
		// auto.vision.GetContour2Height());

		// SmartDashboard.putBoolean("flag", auto.vision.flag);

		/*
		 * // SmartDashboard.putNumber("Battery Voltage", pdp.getVoltage());
		 * //SmartDashboard.putNumber("Potentiometer Value",
		 * digitBoard.getPot()); SmartDashboard.putNumber("Move Value",
		 * drive.moveValue);
		 * 
		 * //SmartDashboard.putBoolean("Button A", digitBoard.getButtonA());
		 * //SmartDashboard.putBoolean("Button B", digitBoard.getButtonB());
		 * SmartDashboard.putNumber("AutoMode", auto.autoMode);
		 * SmartDashboard.putString("Display", auto.display); //
		 * SmartDashboard.putNumber("A7", potMaybe.getVoltage());
		 * 
		 * // digitBoard.display(pdp.getVoltage());
		 * 
		 * // SmartDashboard.putNumber("Ramp Clock", //
		 * drive.forwardDriveRamp.clock.get());
		 * 
		 * // 2 camera code 8NIVision.IMAQdxGrab(currSession, frame, 1);
		 * CameraServer.getInstance().setImage(frame);
		 */
	}

}
