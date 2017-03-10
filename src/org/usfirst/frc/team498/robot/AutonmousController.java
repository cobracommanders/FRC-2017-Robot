//Hmm... what would Curt do... Go to Syria:)

package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

public class AutonmousController {
	private Timer clock;
	private Drive2017 drive;
	public AnalogUltrasonicSensor2017 ultra;

	//PewPew2017 shooter;

	ButtonPress buttonPress;

	double currentContourHeight = 0.0;
	double leftContour = 0.0;
	double rightContour = 0.0;
	double rangeInches = 2.5;

	public double lastAngle = 0;
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

	//Init
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
		
		TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;
	}

	
	//Depreciated
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

	//Executes an auto mode dependent on the value of auto mode
	public void Auto() {
		double ultraInches = ultra.GetRangeInches(false);
		//The true-false value in the function parameters is supposed to reverse all the turns
		//Ultrainches isn't used, but it is there if we need it
		switch (AutoMode.currentValue) {
		//Switches between auto modes
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

	//Executes the left peg auto
	public void autoLeftPeg(boolean blue) { 
		
		int blueToggle = blue ? -1 : 1;
		switch (phase) {
		case 0:
			//Init
			clock.start();
			gyro.reset();
			phase++;
			break;
		case 1:
			//Drive forward
			drive.manualDrive(-0.6, -AngleComp());// -0.8
			if (clock.get() > 1.7) { // 1.2
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 2:
			//Turns
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
			//Drives
			drive.manualDrive(-0.6, -AngleComp());// -0.8
			if (clock.get() > 2.5) {// 2.0
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 4:
			//Stays still for a bit
			gyro.reset();
			drive.manualDrive(0, 0);
			if (clock.get() > 2.0) {
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
			//START OF SHOOTER, MOVES BACK
		/*case 5:
			drive.manualDrive(0.6, -AngleComp());// 0.6
			if (clock.get() > 1.0) {// 1.0
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
			drive.manualDrive(0.6, -AngleComp());// 0.8
			if (clock.get() > 2.5) { // changed time by .5 //2.0
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
			break;*/
		// New Code
		/*case 9:
			buttonPress.Shoot();
			if (clock.get() > 5) {
				clock.stop();
				clock.reset();
				phase++;
			}
			break;
		case 10:
			drive.manualDrive(0, 0);
			phase = -1;
			break;*/
		}

	}

	//Executes the middle auto
	public void autoMidPeg(boolean blue, double ultraInches) {

		// TODO: TEST
		int blueToggle = blue ? -1 : 1;
		switch (phase) {
		case 0:
			//Init
			clock.start();
			gyro.reset();
			phase++;
			break;
		case 1:
			//Drives forward
			drive.manualDrive(-0.5, -AngleComp());
			if (true) {
				clock.start();
				phase++;
				drive.manualDrive(0, 0);
			}
			break;
		case 2:
			//Drives forward
			drive.manualDrive(-0.5, -AngleComp());
			if (clock.get() >= 3) {
				clock.stop();
				clock.reset();
				phase++;
				drive.manualDrive(0, 0);
			}
			break;
		}
	}
		
		/*
		 * switch (phase) { case 0: clock.start(); phase++; break; case 1:
		 * drive.manualDrive(0, 0.8); if (clock.get() > 2) { phase++; } break;
		 * case 2: drive.manualDrive(0, 0); break; }
		 *
		 *
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
		 *
		 *
		 * switch (phase) {
		 * 
		 * case 0: gyro.reset(); gyro.calibrate(); clock.start(); phase++;
		 * break; case 1: drive.manualDrive(-.5,
		 * ConvertGyroStuff(gyro.getAngle() * 0.03)); if (clock.get() > 2) {
		 * phase++; } break; case 2: drive.manualDrive(0, 0); break; }
		 */

	//Executes the right auto
	public void autoRightPeg(boolean blue) { // inverse of autoleftPeg
		
		int blueToggle = blue ? -1 : 1;
		switch (phase) {
		case 0:
			
			//Init
			clock.start();
			gyro.reset();
			phase++;
			break;
		case 1:
			
			//Drives forward
			drive.manualDrive(-0.8, -AngleComp());// -0.6
			if (clock.get() > 1.2) { // 1.7
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 2:
			
			//Turns
			if (gyro.getAngle() >= -56) {
				drive.manualDrive(0, -0.5);
			} else {
				drive.manualDrive(0, 0.1);
			}
			if (Math.abs(gyro.getAngle() + 56) < 0.1) {
				gyro.reset();
				clock.start();
				phase++;
			}
			break;
		case 3:
			//Drives forward
			drive.manualDrive(-0.8, -AngleComp());// -0.6
			if (clock.get() > 2.0) {// 2.5
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 4: 
			//stops
			gyro.reset();
			drive.manualDrive(0, 0);
			if (clock.get() > 2.0) {
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 5:
			//start of shooter, moves back
			drive.manualDrive(0.8, -AngleComp());// 0.6
			if (clock.get() > 0.5) {// 1.0
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 6:
			//turns -17 degrees towards the boiler
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
			//drives back to boiler
			drive.manualDrive(0.8, -AngleComp());// 0.6
			if (clock.get() > 2.0) { // changed time by .5 //2.5
				clock.start();
				gyro.reset();
				phase++;
			}
			break;
		case 8:
			//stops
			drive.manualDrive(0, 0);
			clock.start();
			gyro.reset();
			phase++;
			break;
		// New Code
		case 9:
			//shoots 10 fuel
			buttonPress.Shoot();
			if (clock.get() > 5) {
				clock.stop();
				clock.reset();
				phase++;
			}
			break;
		case 10:
			//Bad Code
			phase = -1;
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

	//Takes the last angle and the current angle and subtracts the last angle from the current angle. MAkes it so the robot turns 3 times then goes forward
	public double AngleComp() {
		//return gyro.getAngle() * -0.3;
		if (Math.abs(lastAngle - gyro.getAngle()) < 4) {
			lastAngle = gyro.getAngle();
			return -gyro.getAngle() * 0.15;
		} else {
			return lastAngle;
		}
	}

	//Uses angle from target and returns it
	public double VisionAngleComp() {
		return netTable.getDouble("angleFromGoal") * -0.3;
	}

	//Depreciated
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

	//Tests auto, currently depreciated
	public void testDrive() {

		// drive.manualDrive(-.65, ConvertGyroStuff(gyro.getAngle() * 0.03));

		/*
		 * if (gyro.getAngle() > .5) { drive.manualDrive(-.65, .3); } else if
		 * (gyro.getAngle() < -.5) { //yancy's line drive.manualDrive(-.65,
		 * -.3); } else{ drive.manualDrive(-.65, 0); }
		 */
	}

	//uses digit board to select auto
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
