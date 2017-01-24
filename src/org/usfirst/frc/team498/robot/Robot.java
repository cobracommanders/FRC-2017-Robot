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

//import edu.wpi.first.wpilibj.networktables.NetworkTable; *garbage*

public class Robot extends SampleRobot {
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
	}

	// Select which autonomous to run
	public void autonomous() {
		SendableChooser<AutoSelector> sc = new SendableChooser<AutoSelector>();
		sc.addDefault("Top", AutoSelector.TOPPEG); // When on the opposite
													// playing side (Blue
													// station), Top and Bot peg
													// are switched. (i.e, use
													// the top peg mode for the
													// bottom peg if on blue
													// side)
		sc.addObject("Middle", AutoSelector.MIDPEG);
		sc.addObject("Bottom", AutoSelector.BOTPEG);

		auto.autoInit(-1); // Autonomous method is copied from Unnamed Mark 4

		while (isAutonomous() && isEnabled()) {
			if ((AutoSelector) sc.getSelected() == AutoSelector.TOPPEG) {
				auto.autoTopPeg();
			} else {
				System.out.println("Selector did not match any known pattern");
			}
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
				auto.gyro.reset(); //resets gyro
			}

			if (thisStick.getButton(Button.RightBumper)) {
				accessories.Shoot(); //shoots
			}

			if (thisStick.getButton(Button.BACK) && thisStick.getButton(Button.B)) {
				accessories.Climb(); //climbs
			}
			// robot is cancer
			if (thisStick.getButton(Button.Y)) {
				teleMode = TeleOpMode.HIGHGOALALIGNMENT; //aligns robot to high goal
			}
			if (thisStick.getButton(Button.B)) {
				teleMode = TeleOpMode.GEARALIGNMENT; //aligns robot to peg
			}
			if (thisStick.getButton(Button.START)) {
				teleMode = TeleOpMode.OPERATORCONTROL; //makes robot go back to TeleOp
			}
			if (thisStick.getButton(Button.X)) {
				teleMode = TeleOpMode.TEST; //Testing code
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

		// SmartDashboard.putNumber("Ramp Clock",
		// drive.forwardDriveRamp.clock.get());

		// 2 camera code
		/*
		 * 8NIVision.IMAQdxGrab(currSession, frame, 1);
		 * CameraServer.getInstance().setImage(frame);
		 */
	}

}
