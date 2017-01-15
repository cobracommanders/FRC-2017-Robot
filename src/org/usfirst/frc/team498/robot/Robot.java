package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * 
 * 
 *  The revised version of Unnamed Mark 2
 * 
 * 
 * 
 */
public class Robot extends SampleRobot {
	// Vision
	Vision2017 vision = new Vision2017();
	// Drive
	Ports ports = new Ports();
	private Timer clock;
	FancyJoystick thisStick = new FancyJoystick(0);
	Drive2016 drive = new Drive2016(thisStick, ports);
	double currentContourHeight = 0.0;
	double leftContour = 0.0;
	double rightContour = 0.0;

	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	Shooter2017 shooter = new Shooter2017(ports);
	Ultrasonic ultrasonic = new Ultrasonic(0, 1);
	AnalogUltrasonicSensor2017 analogSensor = new AnalogUltrasonicSensor2017(ports);

	final int highGoalheight = 200; // Measured in pixels
	final int horizontalDeadzone = 20; // Measured in pixels
	final int verticalDeadzone = 20; // Measured in pixels
	final int gearDeadzone = 20; // Measured in pixels

	PowerDistributionPanel pdp = new PowerDistributionPanel();

	SendableChooser sc = new SendableChooser();

	@Override
	public void robotInit() {

	}

	// Select which autonomous to run
	public void autonomous() {

		while (isAutonomous() && isEnabled()) {

		}

	}

	public void operatorControl() {

		drive.moveValue = 0;
		drive.turnValue = 0;
		gyro.reset();

		TeleOpMode mode = TeleOpMode.OPERATORCONTROL;

		while (isOperatorControl() && isEnabled()) {
			// Checks button
			if (thisStick.getButton(Button.Y)) {
				gyro.reset();
			}
			if (thisStick.getButton(Button.A)) {
				mode = TeleOpMode.HIGHGOALALIGNMENT;
			} else if (thisStick.getButton(Button.B)) {
				mode = TeleOpMode.GEARALIGNMENT;
			} else if (thisStick.getButton(Button.START)) {
				mode = TeleOpMode.OPERATORCONTROL;
			}

			switch (mode) {
			case OPERATORCONTROL:
				// Drive the robot via controller
				drive.rampedDriveListener();
				break;
			case GEARALIGNMENT:

				while (Math.abs(vision.GetContour1Height() - vision.GetContour2Height()) > gearDeadzone) {
					if (vision.GetContour1Height() == vision.GetContour2Height()) {

					} else if (vision.GetContour1Height() < vision.GetContour2Height()) {
						leftContour = vision.GetContour1Height();
						rightContour = vision.GetContour2Height();
					} else {
						leftContour = vision.GetContour2Height();
						rightContour = vision.GetContour1Height();
					}

					if (leftContour < rightContour) {
						drive.manualDrive(0, .5);
					} else if (leftContour > rightContour) {
						drive.manualDrive(0, -.5);
					} else {
						drive.manualDrive(.5, 0);
						mode = TeleOpMode.OPERATORCONTROL;
					}
				}
				/*
				 * if (Math.abs(vision.GetContour1CenterX() -
				 * (vision.GetCameraWidth() / 2)) < horizontalDeadzone) {
				 * drive.manualDrive(.5, 0); } else if
				 * (vision.GetContour1CenterX() > (vision.GetCameraWidth() / 2))
				 * { drive.manualDrive(0, -.5); } else { drive.manualDrive(0,
				 * .5); }
				 */
				break;
			case HIGHGOALALIGNMENT:
				// Checks if we are within horizontal Deadzone
				if (Math.abs(vision.GetContour1CenterX() - (vision.GetCameraWidth() / 2)) < horizontalDeadzone) {
					// gets thickest contour
					if (vision.GetContour1Height() > vision.GetContour2Height()) {
						currentContourHeight = vision.GetContour1Height();
					} else {
						currentContourHeight = vision.GetContour2Height();
					}
					// Aligns distance
					if (Math.abs(currentContourHeight - (vision.GetCameraHeight())) < verticalDeadzone) {
						shooter.Shoot();
					} else if (currentContourHeight < highGoalheight) {
						drive.manualDrive(-.5, 0);
					} else if (currentContourHeight > highGoalheight) {
						drive.manualDrive(.5, 0);
					}
					// Turns camera towards goal
				} else if (vision.GetContour1CenterX() > (vision.GetCameraWidth() / 2)) {
					drive.manualDrive(0, -.5); // turns left, TODO: gyro
				} else if (vision.GetContour1CenterX() < (vision.GetCameraWidth() / 2)) {
					drive.manualDrive(0, .5); // turns right, TODO: gyro
				} else {

					mode = TeleOpMode.OPERATORCONTROL;
				}
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

		SmartDashboard.putNumber("Gyro Angle", gyro.getAngle());
		SmartDashboard.putNumber("Range (Inches)", ultrasonic.getRangeInches());
		SmartDashboard.putNumber("Range millimeters (Analog)", analogSensor.GetRangeMM());
		SmartDashboard.putNumber("Range Inches (Analog)", analogSensor.GetRangeInches());
		SmartDashboard.putNumber("Voltage (Analog)", analogSensor.GetVoltage());

	}

}
