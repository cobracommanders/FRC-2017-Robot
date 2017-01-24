//Hmm... what would Curt do... :)

package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;

public class AutonmousController {
	// Vision
	public Vision2017 vision = new Vision2017(0);

	private Timer clock;
	private Drive2017 drive;
	private IntakeShooterClimber2017 shooter;
	public AnalogUltrasonicSensor2017 analogSensor;
	public ADXRS450_Gyro gyro = new ADXRS450_Gyro();

	double currentContourHeight = 0.0;
	double leftContour = 0.0;
	double rightContour = 0.0;
	double rangeInches = 2.5;

	double driveAngle;

	final double GEARADJUSTANGLE = 30;

	final int highGoalheight = 200; // Measured in pixels
	final int horizontalDeadzone = 20; // Measured in pixels
	final int verticalDeadzone = 20; // Measured in pixels
	final int gearDeadzone = 20; // Measured in pixels

	int phase = 0;
	int clockTime = 2;

	AutonmousController(Drive2017 drive_a, IntakeShooterClimber2017 shoot_a, Ports ports) {
		drive = drive_a;
		shooter = shoot_a;
		analogSensor = new AnalogUltrasonicSensor2017(ports);
		clock = new Timer();
	}

	public void run(AutoSelector key) { // In theory, this should be able to
										// choose between different types of
										// autonomous modes
		switch (key) {
		case DRIVEFORWARD:
			break;
		case TOPPEG:
			break;
		case MIDPEG:
			break;
		case BOTPEG:
			break;
		}
	}

	// The autonomous for driving forward
	public void autoInit(int startPhase) {

		phase = startPhase;
		clock.start();
	}

	public void autoDriveForward() { // This is our test auto
		switch (phase) {
		case 0:
			clock.reset();
			phase++;

			break;

		case 1: // 153 far, 13 1/4 off, wasn't for 2 seconds however(.4, .2)
			// 116 1/2 far, for 2 seconds (.6, .217)

			/*
			 * conclusion (January 16): .217 is really close to the ideal turn
			 * value, however there is a better alternative, especially if
			 * gyro.getAngle() works
			 */

			/*
			 * goal is to get (1, *and some number that will get the robot to
			 * drive STRAIGHT!!!* (internal screaming, KMP)
			 */

			drive.manualDrive(.6, .217); // moves forward for two seconds.

			/*
			 * I want to use the gyro.getAngle() method, but all it does make
			 * the robot turn really fast. Previously, it was -gyro.getAngle() *
			 * 0.03. Curt help me pls
			 */

			// Cody, why you cucking Aaron? (See classmate secret message)
			if (clock.get() > clockTime) {
				clock.reset();
			}
			break;
		}
	}

	public void autoTopPeg() { // auto for the top peg (Top as in farthest from
								// BOILER)
		switch (phase) {
		case 0:
			clock.start();
			phase++;
			break;
		case 1:
			drive.manualDrive(0, -.5); // turns left
			if (clock.get() > 2) {
				clock.reset();
				phase++;
			}
			break;
		case 2:
			drive.manualDrive(.5, 0); // moves forward
			if (clock.get() > 3) {
				clock.reset();
				phase++;
			}
			break;
		case 3:
			drive.manualDrive(0, .5); // turns right
			if (clock.get() > 2) {
				clock.reset();
				phase++;
			}
			break;
		case 4:
			AlignGearPeg(); // aligns gear peg
			phase++;
			break;
		case 40:
			break;
		}
	}

	// code for high goal below from top peg, just in case

	/*
	 * public void autoTopPeg() { // auto for the top peg (Top as in farthest
	 * from // BOILER) switch (phase) { case 0: clock.start(); phase++; break;
	 * case 1: drive.manualDrive(0, -.5); // turns left if (clock.get() > 2) {
	 * clock.reset(); phase++; } break; case 2: case 2: drive.manualDrive(.5,
	 * 0); // moves forward if (clock.get() > 3) { clock.reset(); phase++; }
	 * break; case 3: drive.manualDrive(0, .5); // turns right if (clock.get() >
	 * 2) { clock.reset(); phase++; } break; case 4: AlignGearPeg(); // aligns
	 * gear peg phase++; break; case 5: drive.manualDrive(0, .5); //turns right
	 * if (clock.get() > 2) { clock.reset(); phase++; } break; case 6:
	 * drive.manualDrive(.5, 0) //moves forward if (clock.get() > 2) {
	 * clock.reset(); phase++; } break; case 7: drive.manualDrive(0, -.5)
	 * //turns left if (clock.get() > 2) { clock.reset(); phase++; } break; case
	 * 8: drive.manualDrive(.5, 0) //moves forward if (clock.get() > 6) {
	 * clock.reset(); phase++; } break; case 9: drive.manualDrive(0, .5) //turns
	 * right if (clock.get() > 1) { clock.reset(); phase++; } break; case 10:
	 * AlignHighGoal(); phase++; break; case 40: break; }
	 */
	public void autoMidpeg() {
		// TODO: TEST
		switch (phase) {
		case 0:
			clock.start();
			phase++;
			break;
		case 1:
			drive.manualDrive(.5, 0); // moves forward
			if (clock.get() > 2) {
				clock.reset();
				phase++;
			}
			break;
		case 2:
			AlignGearPeg(); // aligns gear peg
			phase++;
			break;
		case 40:
			break;
		}
	}

	public void autoBotPeg() { // inverse of autoTopPeg
		// TODO: TEST
		switch (phase) {
		case 0:
			clock.start();
			phase++;
			break;
		case 1:
			drive.manualDrive(0, .5); // turns right
			if (clock.get() > 2) {
				clock.reset();
				phase++;
			}
			break;
		case 2:
			drive.manualDrive(.5, 0); // moves forward
			if (clock.get() > 3) {
				clock.reset();
				phase++;
			}
			break;
		case 3:
			drive.manualDrive(0, -.5); // turns left
			if (clock.get() > 2) {
				clock.reset();
				phase++;
			}
			break;
		case 4:
			AlignGearPeg(); // aligns gear peg
			phase++;
			break;
		case 40:
			break;
		}
	}

	/*
	 * public void autoLowBar() {
	 * 
	 * switch (phase) { case 0:
	 * 
	 * clock.reset(); phase++;
	 * 
	 * break; case 1: // Drive through low bar
	 * 
	 * drive.manualDrive(-.8, 0); if (clock.get() > 6) { phase = 40;
	 * drive.manualDrive(0, 0); } break;
	 * 
	 * case 40: break;
	 * 
	 * } }
	 */

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * Below is the Alignment code for both the high goal on the boiler and the
	 * peg alignment for the gear!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

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
			// TODO Shit is done yo!

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
			rightContour = vision.GetContour2Height();
			// Checks the first contour if its farthest makes it the left
			// contour.
		} else {
			leftContour = vision.GetContour2Height();
			// This makes the left contour the second one.
			rightContour = vision.GetContour1Height();
		}

		if (leftContour < rightContour) {
			driveAngle = .5;
		} else {
			driveAngle = -.5;
		}

		// If the left contour is shorter than the right one, turns right.
		switch (phase) { // aligns from right
		case -1:
			clock.reset();
			gyro.reset();
			phase++;
			break;
		case 0:
			drive.manualDrive(0, driveAngle);// turns in a way (left)
			if (Math.abs(gyro.getAngle()) > GEARADJUSTANGLE) {
				clock.reset();
				gyro.reset();
				phase++;
			}

			break;
		case 1:
			drive.manualDrive(-.65, gyro.getAngle() * 0.03);// moves
															// backwards
			if (clock.get() > clockTime) {
				clock.reset();
				gyro.reset();
				phase++;
			}

			break;
		case 2:
			drive.manualDrive(0, -driveAngle);// turns in a way(right)
			if (Math.abs(gyro.getAngle()) > GEARADJUSTANGLE) {
				clock.reset();
				gyro.reset();
				phase++;
			}

			break;
		case 3:
			drive.manualDrive(.65, -gyro.getAngle() * 0.03);// moves forward
			if (clock.get() > clockTime) {
				clock.reset();
				gyro.reset();
				phase++;
			}

			break;
		case 4:
			drive.manualDrive(0, driveAngle);// turns in a way (left)
			if (Math.abs(gyro.getAngle()) > GEARADJUSTANGLE) {
				clock.reset();
				gyro.reset();
				phase++;
			}
			break;
		case 5:
			drive.manualDrive(.65, -gyro.getAngle() * 0.03);// moves forward
			if (clock.get() > clockTime) {
				clock.reset();
				phase++;
			}
			break;
		case 40:
			break;
		}

		return TeleOpMode.GEARALIGNMENT;
	}

	public void testDrive() {

		drive.manualDrive(-.65, gyro.getAngle() * 0.03);

		/*
		 * if (gyro.getAngle() > .5) { drive.manualDrive(-.65, .3); } else if
		 * (gyro.getAngle() < -.5) { //yancy's line drive.manualDrive(-.65,
		 * -.3); } else{ drive.manualDrive(-.65, 0); }
		 */
	}
}
