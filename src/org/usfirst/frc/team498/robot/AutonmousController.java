package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;

public class AutonmousController {
	// Vision
	Vision2017 vision = new Vision2017();

	private Timer clock;
	private Drive2016 drive;
	private Shooter2017 shooter;
	public AnalogUltrasonicSensor2017 analogSensor;
	public ADXRS450_Gyro gyro = new ADXRS450_Gyro();

	double currentContourHeight = 0.0;
	double leftContour = 0.0;
	double rightContour = 0.0;
	double rangeInches = 2.5;

	final int highGoalheight = 200; // Measured in pixels
	final int horizontalDeadzone = 20; // Measured in pixels
	final int verticalDeadzone = 20; // Measured in pixels
	final int gearDeadzone = 20; // Measured in pixels

	int phase = 0;
	int clockTime = 15;

	AutonmousController(Drive2016 drive_a, Shooter2017 shoot_a, Ports ports) {
		drive = drive_a;
		shooter = shoot_a;
		analogSensor = new AnalogUltrasonicSensor2017(ports);
		clock = new Timer();
	}

	public void run(AutoSelector key) {
		switch (key) {
		case LowBar:
			break;
		}
	}

	// The Autonomous for starting at the low bar, driving through, turning, and
	// shooting into the high goal
	public void autoInit(int startPhase) {

		phase = startPhase;
		clock.stop();
		clock.reset();
	}

	public void autoLowBar() {

		switch (phase) {
		case 0:

			clock.start();
			phase++;

			break;
		case 1:
			// Drive through low bar

			drive.manualDrive(-.8, 0);
			if (clock.get() > 6) {
				phase = 40;
				drive.manualDrive(0, 0);
			}
			break;

		case 40:
			break;

		}
	}

	public TeleOpMode AlignHighGoal() {
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
			// TODO Shit is done yo

			return TeleOpMode.OPERATORCONTROL;
		}
		return TeleOpMode.HIGHGOALALIGNMENT;
	}

	public TeleOpMode AlignGearPeg() {
		// Checks if we are in the gear Deadzone
		if (Math.abs(vision.GetContour1Height() - vision.GetContour2Height()) > gearDeadzone) {

			if (analogSensor.GetRangeInches() > rangeInches) {
				drive.manualDrive(.5, 0);
				return TeleOpMode.OPERATORCONTROL;
			}
		} else if (vision.GetContour1Height() < vision.GetContour2Height()) {
			leftContour = vision.GetContour1Height();
			rightContour = vision.GetContour2Height(); // Checks the
														// first contour
														// if its
														// farthest;
														// makes it the
														// left contour.
		} else {
			leftContour = vision.GetContour2Height(); // This makes the
														// left contour
														// the second
														// one.
			rightContour = vision.GetContour1Height();
		}

		if (leftContour < rightContour) { // If the left contour is
											// shorter than the right
											// one, turns right.
			switch (phase) { // aligns from right
			case -1:
				clock.reset();
				phase++;
				break;
			case 0:
				drive.manualDrive(0, .5);// turns right
				if (clock.get() > clockTime) {
					clock.reset();
					phase++;
				}

				break;
			case 1:
				drive.manualDrive(-.5, 0);// moves backwards
				if (clock.get() > clockTime) {
					clock.reset();
					phase++;
				}

				break;
			case 2:
				if (clock.get() > clockTime) {
					drive.manualDrive(0, -.5);// turns left
				}
				clock.reset();
				phase++;
				break;
			case 3:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(.5, 0);// moves forward
				}
				clock.reset();
				phase++;
				break;
			case 4:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(0, .5);// turns right
				}
				clock.reset();
				phase++;
				break;
			case 5:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(.5, 0);// moves forward
				}
				clock.reset();
				phase++;
				break;
			case 40:
				break;
			}
		} else if (leftContour > rightContour) {
			switch (phase) { // aligns from left
			case 0:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(0, -.5);// turns left
				}
				clock.reset();
				phase++;
				break;
			case 1:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(-.5, 0);// moves backwards
				}
				clock.reset();
				phase++;
				break;
			case 2:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(0, .5);// turns right
				}
				clock.reset();
				phase++;
				break;
			case 3:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(.5, 0);// moves forward
				}
				clock.reset();
				phase++;
				break;
			case 4:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(0, -.5);// turns left
				}
				clock.reset();
				phase++;
				break;
			case 5:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(.5, 0);// moves forward
				}
				clock.reset();
				phase++;
				break;
			case 40:
				break;
			}
		} else {
			switch (phase) {
			case 0:
				clock.start();
				if (clock.get() > clockTime) {
					drive.manualDrive(.5, 0);// moves forward
				}
				clock.reset();
				phase++;
				break;
			case 40:
				break;
			}
		}
		return TeleOpMode.GEARALIGNMENT;
	}
}
