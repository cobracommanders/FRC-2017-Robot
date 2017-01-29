//Made in Japan
package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.networktables.NetworkTable; *garbage*

public class Robot extends SampleRobot {
	Command autonomousCommand;

	/*
	 * //Network tables NetworkTable table; *garbage*
	 */
	// Drive
	Ports ports = new Ports();
	private Timer clock;
	FancyJoystick thisStick = new FancyJoystick(0);
	Drive2017 drive = new Drive2017(thisStick, ports);
	REVDigitBoard digitBoard = new REVDigitBoard();
	PewPew2017 shooter = new PewPew2017(digitBoard, thisStick, ports);
	//AnalogInput potMaybe = new AnalogInput(7);
	AutonmousController auto = new AutonmousController(drive, shooter, digitBoard, ports);

	Ultrasonic ultrasonic = new Ultrasonic(0, 1);

	PowerDistributionPanel pdp = new PowerDistributionPanel();

	DoubleSolenoid ds;

	boolean aOldState = false;

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
		ds = new DoubleSolenoid(0, 0, 1);
	}

	// Select which autonomous to run
	public void autonomous() {

		auto.autoInit(-1); // This autonomous method is copied from Unnamed Mark
							// 4

	}

	public boolean ADown() {
		boolean localTemp = false;
		if (!aOldState && thisStick.getButton(Button.A))
			localTemp = true;
		aOldState = thisStick.getButton(Button.A);
		return localTemp;
	}

	public void operatorControl() {

		// For Network table double x = 0; *garbage* double y = 0;

		drive.moveValue = 0;
		drive.turnValue = 0;
		// auto.gyro.reset();

		TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;
		digitBoard.display(2.0);
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

			// Checks button
			if (ADown()) {
				// auto.gyro.reset(); // resets gyro
				if (ds.get() == Value.kOff)
					ds.set(Value.kReverse);
				if (ds.get() == Value.kForward)
					ds.set(Value.kReverse);
				if (ds.get() == Value.kReverse)
					ds.set(Value.kForward);

			}

				drive.rampedDriveListener();
			if (thisStick.getButton(Button.RightBumper)) {
				shooter.Shoot(); // shoots
			}

			if (thisStick.getButton(Button.BACK) && thisStick.getButton(Button.B)) {
				// climbing feature
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

			/*switch (teleMode) {
			case OPERATORCONTROL:
				// Drive the robot via controller
				drive.rampedDriveListener();
				
				shooter.shootListener();
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
			}*/

			// Send stats to the driver
			print();
		}

	}

	public void disabled() {
		while (isDisabled()) {
			print();
			if (digitBoard.getButtonA()) {
				auto.autonomousSelector(); // Displays auto on digit board
			}
		}

	}

	// Sends information to the driver
	private void print() {

		// SmartDashboard.putNumber("Gyro Angle", auto.gyro.getAngle());
		// SmartDashboard.putNumber("Gyro getRate()", auto.gyro.getRate());
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

		//SmartDashboard.putNumber("Battery Voltage", pdp.getVoltage());
		SmartDashboard.putNumber("Potentiometer Value", digitBoard.getPot());
		SmartDashboard.putNumber("Move Value", drive.moveValue);
		//SmartDashboard.putNumber("A7", potMaybe.getVoltage());

		// digitBoard.display(pdp.getVoltage());

		// SmartDashboard.putNumber("Ramp Clock",
		// drive.forwardDriveRamp.clock.get());

		// 2 camera code
		/*
		 * 8NIVision.IMAQdxGrab(currSession, frame, 1);
		 * CameraServer.getInstance().setImage(frame);
		 */
	}

}
