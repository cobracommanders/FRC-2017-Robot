//Hmm... what would Curt do... Go to Syria:)

package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

public class AutonmousController {
	public DriverStation driveStation = DriverStation.getInstance();
	private Timer clock;
	private Drive2017 drive;
	public AnalogUltrasonicSensor2017 ultra;

	PewPew2017 shooter;

	ButtonPress buttonPress;

	double currentContourHeight = 0.0;
	double leftContour = 0.0;
	double rightContour = 0.0;
	double rangeInches = 2.5;

	ADXRS450_Gyro gyro;

	double driveAngle;

	final double GEARADJUSTANGLE = 30;

	final int highGoalheight = 200; // Measured in pixels
	final int horizontalDeadzone = 20; // Measured in pixels
	final int verticalDeadzone = 20; // Measured in pixels
	final int gearDeadzone = 20; // Measured in pixels

	int phase = 0;
	int clockTime = 2;
	char colorAuto;
	char positionAuto;

	NetworkTable netTable;

	REVImprovedDigitBoard digitBoard;

	AutonmousController(Drive2017 drive, ButtonPress buttonPress, REVImprovedDigitBoard digitBoard,
			FancyJoystick thisStick, Ports ports, AnalogUltrasonicSensor2017 ultra, ADXRS450_Gyro gyro, Timer clock,
			NetworkTable netTable) {
		this.drive = drive;
		this.buttonPress = buttonPress;
		this.ultra = ultra;
		this.clock = clock;
		this.digitBoard = digitBoard;
		this.gyro = gyro;
		this.netTable = netTable;
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

	public void Auto() {
		double ultraInches = ultra.GetRangeInches(false);
		switch (AutoMode.currentValue) {
		case AutoMode.RED_LEFT:
			autoLeftPeg(false);
			break;
		case AutoMode.RED_MIDDLE:
			autoMidPeg(false, ultraInches);
			break;
		case AutoMode.RED_RIGHT:
			autoRightPeg(false);
			break;
		case AutoMode.BLUE_LEFT:
			autoLeftPeg(true);
			break;
		case AutoMode.BLUE_MIDDLE:
			autoMidPeg(true, ultraInches);
			break;
		case AutoMode.BLUE_RIGHT:
			autoRightPeg(true);
			break;
		}
	}

	public void autoLeftPeg(boolean blue) { // auto for the top peg (Top as in
											// farthest from
		int blueToggle = blue ? -1 : 1;
		switch (phase) {
		case 0:
			clock.start();
			gyro.reset();
			phase++;
			break;
		case 1:
			drive.manualDrive(-0.8, -AngleComp());// -0.6
			if (clock.get() > 1.2) { //1.7
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 2:
			if (gyro.getAngle() <= 56) {
				drive.manualDrive(0, -0.5);
			} else {
				drive.manualDrive(0, 0.1);
			}
			if (Math.abs(gyro.getAngle() - 56) < 0.1) {
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 3:
			drive.manualDrive(-0.8, -AngleComp());// -0.6
			if (clock.get() > 2.0) {//2.5
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 4:
			gyro.reset();
			drive.manualDrive(0, 0);
			if (clock.get() > 2.0) {
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 5:
			drive.manualDrive(0.8, -AngleComp());// 0.6
			if (clock.get() > 0.5) {// 1.0
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 6:
			if (gyro.getAngle() >= -17) { // changed degree, originally -15
				drive.manualDrive(0, 0.5);
			} else {
				drive.manualDrive(0, -0.1);
			}
			if (Math.abs(gyro.getAngle() + 17) < 0.1) { // changed degree,
														// originally 15
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 7:
			drive.manualDrive(0.8, -AngleComp());// 0.6
			if (clock.get() > 2.0) { // changed time by .5 //2.5
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 8:
			drive.manualDrive(0, 0);
			clock.start();
			gyro.reset();
			phase++;
			break;
		// New Code
		case 9:
			shooter.Shoot();
			if (clock.get() > 5) {
				clock.stop();
				clock.reset();
				phase++;
			}
			break;
		case 10:
				shooter.StopShoot();
				phase++;
				break;
			}
			
	}

	public void autoMidPeg(boolean blue, double ultraInches) {

		// TODO: TEST
		int blueToggle = blue ? -1 : 1;
		switch (phase) {
		case 0:
			clock.start();
			gyro.reset();
			phase++;
			break;
		case 1:
			drive.manualDrive(-0.5, -VisionAngleComp());
			if (netTable.getDouble("distanceFromTarget") < 165) {
				clock.start();
				phase++;
				drive.manualDrive(0, 0);
			}
			break;
		case 2:
			drive.manualDrive(-0.5, -AngleComp());
			if (clock.get() >= 1) {
				clock.stop();
				clock.reset();
				phase++;
				drive.manualDrive(0, 0);
			}
		}
		/*
		 * switch (phase) { case 0: clock.start(); phase++; break; case 1:
		 * drive.manualDrive(0, 0.8); if (clock.get() > 2) { phase++; } break;
		 * case 2: drive.manualDrive(0, 0); break; }
		 */
		/*
		 * case 0: clock.start(); phase++; break;
		 * 
		 * case 1: drive.manualDrive(-0.5, 0); if (clock.get() > 1 + (9/99)) {
		 * //clock.stop(); clock.reset(); clock.start(); phase++; } break;
		 * 
		 * case 2: drive.manualDrive(0, -0.8); if (clock.get() > 1) {
		 * clock.reset(); clock.start(); phase++; } break;
		 * 
		 * case 3: drive.manualDrive(-0.5, 0); if (clock.get() > 1) {
		 * clock.reset(); clock.start(); phase++; } break;
		 * 
		 * case 4: drive.manualDrive(0, 0.8); if (clock.get() > 1) {
		 * clock.reset(); clock.start(); phase++; } break;
		 * 
		 * case 5: drive.manualDrive(-0.5, 0); if (clock.get() > 2 + (54/99)) {
		 * clock.reset(); clock.start(); phase++; } break;
		 * 
		 * case 6: drive.manualDrive(0, 0); break; }
		 */
		/*
		 * switch (phase) {
		 * 
		 * case 0: gyro.reset(); gyro.calibrate(); clock.start(); phase++;
		 * break; case 1: drive.manualDrive(-.5,
		 * ConvertGyroStuff(gyro.getAngle() * 0.03)); if (clock.get() > 2) {
		 * phase++; } break; case 2: drive.manualDrive(0, 0); break; }
		 */
	}

	public void autoRightPeg(boolean blue) { // inverse of autoleftPeg
		// TODO: TEST
		int blueToggle = blue ? -1 : 1;
		switch (phase) {
		case 0:
			clock.start();
			gyro.reset();
			phase++;
			break;
		case 1:
			drive.manualDrive(-0.6, -AngleComp());
			if (clock.get() > 2.0) {
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 2:
			if (gyro.getAngle() >= -57) {
				drive.manualDrive(0, 0.5);
			} else {
				drive.manualDrive(0, 0.1);
			}
			if (Math.abs(gyro.getAngle() + 57) < 0.1) {
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 3:
			drive.manualDrive(-0.6, -AngleComp());
			if (clock.get() > 2.5) {
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 4:
			gyro.reset();
			drive.manualDrive(0, 0);
			clock.stop();
			clock.reset();
			break;
		}
	}

	/*
	 * public TeleOpMode AlignHighGoal() { // Checks if we are within horizontal
	 * Deadzone if (Math.abs(vision.GetContour1CenterX() -
	 * (vision.GetCameraWidth() / 2)) < horizontalDeadzone) { // gets thickest
	 * contour if (vision.GetContour1Height() > vision.GetContour2Height()) {
	 * currentContourHeight = vision.GetContour1Height(); } else {
	 * currentContourHeight = vision.GetContour2Height(); } // Aligns distance
	 * if (Math.abs(currentContourHeight - (vision.GetCameraHeight())) <
	 * verticalDeadzone) { shooter.Shoot(); } else if (currentContourHeight <
	 * highGoalheight) { drive.manualDrive(-.5, 0); } else if
	 * (currentContourHeight > highGoalheight) { drive.manualDrive(.5, 0); } //
	 * Turns camera towards goal } else if (vision.GetContour1CenterX() >
	 * (vision.GetCameraWidth() / 2)) { drive.manualDrive(0, -.5); // turns
	 * left, TODO: gyro } else if (vision.GetContour1CenterX() <
	 * (vision.GetCameraWidth() / 2)) { drive.manualDrive(0, .5); // turns
	 * right, TODO: gyro } else { // Shit is done yo!
	 * 
	 * return TeleOpMode.OPERATORCONTROL; } return TeleOpMode.HIGHGOALALIGNMENT;
	 * }
	 */

	public double AngleComp() {
		return gyro.getAngle() * -0.25;
	}

	public double VisionAngleComp() {
		return netTable.getDouble("angleFromGoal") * -0.3;
	}

	public TeleOpMode AlignGearPeg() {
		// Checks if we are in the gear Deadzone
		double turnDirection = 0.5;
		switch (AutoMode.currentValue) {
		case AutoMode.RED_LEFT:
			break;
		case AutoMode.RED_MIDDLE:
			break;
		case AutoMode.RED_RIGHT:
			break;
		case AutoMode.BLUE_LEFT:
			break;
		case AutoMode.BLUE_MIDDLE:
			break;
		case AutoMode.BLUE_RIGHT:
			break;
		}
		/*
		 * if (Math.abs(vision.GetContour1Height() - vision.GetContour2Height())
		 * > gearDeadzone) {
		 * 
		 * if (analogSensor.GetRangeInches() > rangeInches) {
		 * drive.manualDrive(.5, 0); return TeleOpMode.OPERATORCONTROL; } } else
		 * if (vision.GetContour1Height() < vision.GetContour2Height()) {
		 * leftContour = vision.GetContour1Height(); rightContour =
		 * vision.GetContour2Height(); // Checks the first contour if its
		 * farthest makes it the left // contour. } else { leftContour =
		 * vision.GetContour2Height(); // This makes the left contour the second
		 * one. rightContour = vision.GetContour1Height(); }
		 * 
		 * if (leftContour < rightContour) { driveAngle = .5; } else {
		 * driveAngle = -.5; }
		 */

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

		// LCD display needs a pause between changes
		Timer.delay(0.25);

		AutoMode.currentValue++;
		if (AutoMode.currentValue > 5)
			AutoMode.currentValue = 0;

		colorAuto = ' ';
		positionAuto = ' ';
		switch (AutoMode.currentValue) {
		case AutoMode.RED_LEFT:
			colorAuto = 'R';
			positionAuto = 'L';
			break;
		case AutoMode.RED_MIDDLE:
			colorAuto = 'R';
			positionAuto = 'M';
			break;
		case AutoMode.RED_RIGHT:
			colorAuto = 'R';
			positionAuto = 'R';
			break;
		case AutoMode.BLUE_LEFT:
			colorAuto = 'B';
			positionAuto = 'L';
			break;
		case AutoMode.BLUE_MIDDLE:
			colorAuto = 'B';
			positionAuto = 'M';
			break;
		case AutoMode.BLUE_RIGHT:
			colorAuto = 'B';
			positionAuto = 'R';
			break;
		}
		digitBoard.UpdateDisplay(' ', colorAuto, ' ', positionAuto, false);
	}
}
