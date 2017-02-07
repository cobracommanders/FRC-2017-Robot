//Hmm... what would Curt do... :)

package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import edu.wpi.first.wpilibj.Timer;

public class AutonmousController {
	// Vision
	public Vision2017 vision = new Vision2017(0);
	public DriverStation driveStation = DriverStation.getInstance();
	private Timer clock;
	private Drive2017 drive;
	private PewPew2017 shooter;
	public AnalogUltrasonicSensor2017 analogSensor;
	//public ADXRS450_Gyro gyro = new ADXRS450_Gyro();

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
	int autoMode = 0;
	char colorAuto;
	char positionAuto;
	
	NetworkTable netTable = NetworkTable.getTable("CamTable");

	REVImprovedDigitBoard digitBoard;

	AutonmousController(Drive2017 drive, PewPew2017 shooter, REVImprovedDigitBoard digitBoard, FancyJoystick thisStick,
			Ports ports, AnalogUltrasonicSensor2017 ultra, Timer clock) {
		this.drive = drive;
		this.shooter = shooter;
		this.analogSensor = ultra;
		this.clock = clock;
		this.digitBoard = digitBoard;
		digitBoard.UpdateDisplay(' ', 'R', ' ', 'L', false);
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

	public double ConvertGyroStuff(double theta) {
		// Gets rid of excess rotations
		theta %= 360d;
		// Makes 270 = -90, 350 = -10, etc.
		if (theta >= 180)
			theta -= 360d;
		//randy's modifier, makes -270 = 90, -350 = 10, etc.
		if (theta <= -180)
			theta -= -360d;
		// Otherwise, no change needed
		return theta;
	}

	public void Auto() {
		switch (autoMode) {
		case 0:
			autoLeftPeg(false);
			break;
		case 1:
			autoMidPeg(false);
			break;
		case 2:
			autoRightPeg(false);
			break;
		case 3:
			autoLeftPeg(true);
			break;
		case 4:
			autoMidPeg(true);
			break;
		case 5:
			autoRightPeg(true);
			break;
		}
	}

	public void autoDriveForward() { // This is our test auto
		switch (phase) {
		case 0:
			clock.reset();
			 //gyro.reset();
			//gyro.calibrate();
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

			//drive.manualDrive(.6, ConvertGyroStuff(gyro.getAngle()) * -0.03);
			// // moves
			// forward
			// for
			// two
			// seconds.
			// convertGyroStuff is supposed to work. If it doesn't, ask Micah

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

	public void autoLeftPeg(boolean blue) { // auto for the top peg (Top as in
											// farthest from
		// BOILER)
		/*
		 * drive.manualDrive(0, -.5 * blueToggle); drive.manualDrive(.5, 0);
		 * drive.manualDrive(0, .5 * blueToggle); AlignGearPeg();
		 */
		int blueToggle = 1;
		if (blue)
			blueToggle = -1;
		switch (phase) {
		case 0:
			clock.start();
			phase++;
			break;
		case 1:
			drive.manualDrive(0, -.5 * blueToggle); // turns left
			if (clock.get() > 2) {
				clock.start();
				phase++;
			}
			break;
		case 2:
			drive.manualDrive(.5, 0); // moves forward
			if (clock.get() > 3) {
				clock.start();
				phase++;
			}
			break;
		case 3:
			drive.manualDrive(0, .5 * blueToggle); // turns right

			if (clock.get() > 2) {
				clock.start();
				phase++;
			}

			break;
		case 4:
			clock.reset();
			clock.stop();
			drive.manualDrive(0, 0);
			// AlignGearPeg(); // aligns gear peg
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
	public void autoMidPeg(boolean blue) {
		// TODO: TEST
		int blueToggle = 1;
		if (blue)
			blueToggle = -1;
		switch (phase) {
		case 0:
			clock.start();
			phase++;
			break;
		case 1:
			drive.manualDrive(.5, 0); // moves forward
			if (clock.get() > 2) {
				clock.start();
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

	public void autoRightPeg(boolean blue) { // inverse of autoleftPeg
		// TODO: TEST
		int blueToggle = 1;
		if (blue)
			blueToggle = -1;
		switch (phase) {
		case 0:
			clock.start();
			phase++;
			break;
		case 1:
			drive.manualDrive(0, .5 * blueToggle); // turns right
			if (clock.get() > 2) {
				clock.start();
				phase++;
			}
			break;
		case 2:

			drive.manualDrive(.5, 0); // moves forward
			if (clock.get() > 3) {

				clock.start();
				phase++;
			}
			break;
		case 3:
			drive.manualDrive(0, -.5 * blueToggle); // turns left
			if (clock.get() > 2) {
				clock.start();
				phase++;
			}
			break;
		case 4:
			clock.reset();
			clock.stop();
			drive.manualDrive(0, 0);
			//AlignGearPeg(); // aligns gear peg
			phase++;
			break;
		case 40:
			break;
		}
	}

	/*
	 * 
	 * Below is the Alignment code for both the high goal on the boiler and the
	 * peg alignment for the gear!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
			// Shit is done yo!

			return TeleOpMode.OPERATORCONTROL;
		}
		return TeleOpMode.HIGHGOALALIGNMENT;
	}

	public TeleOpMode AlignGearPeg() {
		// Checks if we are in the gear Deadzone
		double turnDirection = 0.5;
		switch (autoMode) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		}
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
			// gyro.reset();
			phase++;
			break;
		case 0:
			drive.manualDrive(0, driveAngle);// turns in a way (left)
			// if (Math.abs(ConvertGyroStuff(gyro.getAngle())) >
			// GEARADJUSTANGLE) {
			clock.reset();
			// gyro.reset();
			phase++;
			// }

			break;
		case 1:
			// drive.manualDrive(-.65, ConvertGyroStuff(gyro.getAngle() *
			// 0.03));// moves
			// backwards
			if (clock.get() > clockTime) {
				clock.reset();
				// gyro.reset();
				phase++;
			}

			break;
		case 2:
			// drive.manualDrive(0, -driveAngle);// turns in a way(right)
			// if (Math.abs(ConvertGyroStuff(gyro.getAngle())) >
			// GEARADJUSTANGLE) {
			// clock.reset();
			// gyro.reset();
			// phase++;
			// }

			break;
		case 3:
			// drive.manualDrive(.65, -ConvertGyroStuff(gyro.getAngle()) *
			// 0.03);// moves
			// forward
			if (clock.get() > clockTime) {
				clock.reset();
				// gyro.reset();
				phase++;
			}

			break;
		case 4:
			drive.manualDrive(0, driveAngle);// turns in a way (left)
			// if (Math.abs(ConvertGyroStuff(gyro.getAngle())) >
			// GEARADJUSTANGLE) {
			// clock.reset();
			// gyro.reset();
			// phase++;
			// }
			break;
		case 5:
			// drive.manualDrive(.65, -ConvertGyroStuff(gyro.getAngle()) *
			// 0.03);// moves
			// forward
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

		// drive.manualDrive(-.65, ConvertGyroStuff(gyro.getAngle() * 0.03));

		/*
		 * if (gyro.getAngle() > .5) { drive.manualDrive(-.65, .3); } else if
		 * (gyro.getAngle() < -.5) { //yancy's line drive.manualDrive(-.65,
		 * -.3); } else{ drive.manualDrive(-.65, 0); }
		 */
	}

	public void autonomousSelector() {
		Timer.delay(0.25);
		autoMode++;
		if (autoMode > 5)
			autoMode = 0;

		colorAuto = ' ';
		positionAuto = ' ';
		if (autoMode < 3)
			colorAuto = 'R';
		else
			colorAuto = 'B';
		switch (autoMode % 3) {
		case 0:
			positionAuto = 'L';
			break;
		case 1:
			positionAuto = 'M';
			break;
		case 2:
			positionAuto = 'R';
			break;
		}
		digitBoard.UpdateDisplay(' ', colorAuto, ' ', positionAuto, false);

	}
}
