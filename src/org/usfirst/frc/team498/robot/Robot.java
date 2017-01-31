//Made in Japan
package org.usfirst.frc.team498.robot;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.I2C.Port;
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
	REVImprovedDigitBoard digitBoard = new REVImprovedDigitBoard();

	PewPew2017 shooter = new PewPew2017(digitBoard, thisStick, ports);
	AutonmousController auto = new AutonmousController(drive, shooter, digitBoard, ports);

	GearIntake2017 gearIntake = new GearIntake2017(thisStick, ports);
	// AnalogUltrasonicSensor2017 ultra = new AnalogUltrasonicSensor2017(ports);
	PowerDistributionPanel pdp = new PowerDistributionPanel();

	boolean dToggle = false;

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
	}

	// Select which autonomous to run
	public void autonomous() {

		// auto.autoInit(-1); // This autonomous method is copied from Unnamed
		// Mark
		// 4

	}

	int count = 0;
	int count2 = 0;

	public void operatorControl() {

		// For Network table double x = 0; *garbage* double y = 0;

		drive.moveValue = 0;
		drive.turnValue = 0;
		// auto.gyro.reset();

		TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;
		// digitBoard.display(2.0);
		digitBoard.CreateScrollMsg("Randy Left Early    ");
		while (isOperatorControl() && isEnabled()) {
			if (count % 1000 == 0) {
				digitBoard.SlideScrollMsg();
			}
			count++;
		
		while (isOperatorControl() && isEnabled()) {
			
			if(thisStick.getButton(Button.Y)) { // 0.735 on smart dashboard is perfect!
			shooter.Shoot();
			} else {
				shooter.StopShoot();
			}
		}

			// digitBoard.display(10.00);

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

			if (thisStick.getButton(Button.BACK) && thisStick.getButton(Button.B)) {
				// TODO climbing feature
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
				gearIntake.Listener();
				// shooter.shootListener();
				break;
			case GEARALIGNMENT:
				// teleMode = auto.AlignGearPeg();
				break;
			case HIGHGOALALIGNMENT:
				// teleMode = auto.AlignHighGoal();
				break;
			case TEST:
				// auto.testDrive();
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
			if (digitBoard.getButtonA()) {
				auto.autonomousSelector(); // Displays auto on digit board
			}
			if (digitBoard.getButtonB()) {
				Timer.delay(0.25);
				dToggle = !dToggle;
				if (dToggle)
					digitBoard.UpdateDisplay('C', 'U', 'C', 'K');
				else
					digitBoard.UpdateDisplay('8', '-', '-', 'D');
			}
			
		}

	}

	// Sends information to the driver
	private void print() {

		// SmartDashboard.putNumber("Gyro Angle", auto.gyro.getAngle());
		// SmartDashboard.putNumber("Gyro getRate()", auto.gyro.getRate());

		// SmartDashboard.putNumber("Range (Inches)", ultra.GetRangeInches());
		// SmartDashboard.putNumber("Ultrasonic Voltage", ultra.GetVoltage());
		
		SmartDashboard.putNumber("Shooter value", digitBoard.getPot());

		/*
		 * SmartDashboard.putNumber("Range millimeters (Analog)",
		 * auto.analogSensor.GetRangeMM());
		 * SmartDashboard.putNumber("Range Inches (Analog)",
		 * auto.analogSensor.GetRangeInches());
		 * SmartDashboard.putNumber("Voltage (Analog)",
		 * auto.analogSensor.GetVoltage());
		 * 
		 * // These should print out GRIP's contour info into Dashboard
		 * SmartDashboard.putNumber("Contour1 CenterX",
		 * auto.vision.GetContour1CenterX());
		 * SmartDashboard.putNumber("Contour1 CenterY",
		 * auto.vision.GetContour1CenterY());
		 * SmartDashboard.putNumber("Contour1 Height",
		 * auto.vision.GetContour1Height());
		 * SmartDashboard.putNumber("Contour2 CenterX",
		 * auto.vision.GetContour2CenterX());
		 * SmartDashboard.putNumber("Contour2 CenterY",
		 * auto.vision.GetContour2CenterY());
		 * SmartDashboard.putNumber("Contour2 Height",
		 * auto.vision.GetContour2Height());
		 * 
		 * SmartDashboard.putBoolean("flag", auto.vision.flag);
		 * 
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
		 * // 2 camera code /* 8NIVision.IMAQdxGrab(currSession, frame, 1);
		 * CameraServer.getInstance().setImage(frame);
		 */
	}

}
