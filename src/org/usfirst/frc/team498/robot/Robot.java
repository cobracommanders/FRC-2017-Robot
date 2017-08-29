//Made in Japan
/*================================================.
     .-.   .-.     .--.                         |
    | OO| | OO|   / _.-' .-.   .-.  .-.   .''.  |
    |   | |   |   \  '-. '-'   '-'  '-'   '..'  |
    '^^^' '^^^'    '--'                         |
===============.  .-.  .================.  .-.  |
               | |   | |                |  '-'  |
               | |   | |                |       |
               | ':-:' |                |  .-.  |
               |  '-'  |                |  '-'  |
==============='       '================'       |
*/package org.usfirst.frc.team498.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.vision.VisionRunner;
//import edu.wpi.first.wpilibj.vision.VisionThread;
import org.opencv.core.Mat;
//import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends SampleRobot {
	// Command autonomousCommand;

	// Drive
	Ports ports = new Ports();
	private Timer clock = new Timer();
	private Timer digitClock = new Timer();
	FancyJoystick thisStick = new FancyJoystick(0);
	ADXRS450_Gyro gyro = new ADXRS450_Gyro();
	Drive2017 drive2017 = new Drive2017(thisStick, ports, gyro);
	Servo servo1 = new Servo(ports.SERVO_1_PWM_PORT);
	Servo servo2 = new Servo(ports.SERVO_2_PWM_PORT);
	Relay light = new Relay(ports.SPIKE_LIGHT);
	ButtonPress buttonPress = new ButtonPress(thisStick, ports, servo1, servo2);
	REVImprovedDigitBoard digitBoard = new REVImprovedDigitBoard();
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	Solenoid sol = new Solenoid(ports.ULTRASONIC_SENSOR_PCM_PORT);

	public boolean hasDigitStarted = false;
	public NetworkTable table;
	// IntakeClimb2017 gearIntake = new IntakeClimb2017(thisStick, ports);
	// PewPew2017 shooter = new PewPew2017(digitBoard, thisStick, ports);
	AnalogUltrasonicSensor2017 ultra = new AnalogUltrasonicSensor2017(thisStick, ports, sol);
	AutonmousController auto = new AutonmousController(drive2017, buttonPress, digitBoard, thisStick, ports, ultra,
			gyro, clock, table);

	// Camera Code
	// private static final int IMG_WIDTH = 320;
	// private static final int IMG_HEIGHT = 240;

	// private VisionThread visionThread;
	// private double centerX = 0.0;

	// private final Object imgLock = new Object();
	// private RobotDrive drive;

	// boolean dToggle = false;

	// boolean intakeToggle = false;
	// boolean xDown = false;

	@Override
	public void robotInit() {
		//turns on ultrasonic connected to the solenoid
		if (!sol.get()) {
			sol.set(true);
		}
		//turns on the green light (theoretically)
		if (light.get() != Relay.Value.kOn) {
			light.set(Relay.Value.kOn);
		}
		
		//starts up camera
		UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture("cam0", 0);
		// camera0.setResolution(320, 320);
		/*
		 * NetworkTable.setClientMode(); NetworkTable.setTeam(498);
		 * NetworkTable.setIPAddress("roborio-498-frc.local");
		 * NetworkTable.initialize();
		 */
		
		//initializes the network table
		table = NetworkTable.getTable("LiftTracker");
		
	

	}

	public void autonomous() {

		while (isAutonomous() && isEnabled()) {
			//prints when bot is in auto and enabled
			print();
			// Start Auto Phases
			auto.Auto();

		}

		while (isAutonomous() && !isEnabled()) {
			//sets the phase of auto to zero when not enabled
			auto.phase = 0;
		}

	}

	int count = 0;
	int count2 = 0;

	//converts the voltage from the pdp into battery voltage for the digit board
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

	// this case converts a number to a character
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

	//teleop version of AngleComp(); makes bot drive straight
	public double AngleComp() {
		return gyro.getAngle() * -0.3;
	}

	public void operatorControl() {

		drive2017.moveValue = 0;
		drive2017.turnValue = 0;

		//by default, mode is in teleop when bot is in operator control
		TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;

		while (isOperatorControl() && isEnabled()) {

			//starts the digitboard if it hasn't started yet
			if (!hasDigitStarted) {
				//starts the clock for the digitboard
				digitClock.start();
				hasDigitStarted = true;
				//displays battery voltage
				digitBoard.UpdateDisplay(DisplayVoltageConversion(), true);
			}
			//if the digit board is over 3 seconds, it refreshes to display the current battery voltage
			if (digitClock.get() > 3) {
				digitClock.start();
				digitBoard.UpdateDisplay(DisplayVoltageConversion(), true);
			}
			// Button Mapping
			// Semi-Auto mode selection
			if (thisStick.getButton(Button.START)) {
				teleMode = TeleOpMode.OPERATORCONTROL; // makes robot go back to
														// TeleOp
			}
			/*
			 * if (thisStick.getButton(Button.A) && teleMode ==
			 * TeleOpMode.OPERATORCONTROL) { teleMode =
			 * TeleOpMode.GEARALIGNMENT; //Aligns the robot to the gear peg }
			 */
			/*
			 * if (thisStick.getButton(Button.Y) && teleMode ==
			 * TeleOpMode.OPERATORCONTROL) { teleMode =
			 * TeleOpMode.HIGHGOALALIGNMENT; //Aligns the robot to the shooter }
			 */
			switch (teleMode) {
			case OPERATORCONTROL:
				// Drive the robot via controller
				drive2017.rampedDriveListener(); // drive
				buttonPress.Listener();// shoots, intake, climb, does
				//drive2017.testListener();
										// EVERYTHING!
				// gearIntake.Listener();
				// shooter.shootListener();

				break;
			case GEARALIGNMENT:
				// teleMode = auto.AlignGearPeg();
				break;
			case HIGHGOALALIGNMENT:
				// teleMode = auto.AlignHighGoal();
				break;
			/*
			 * case TEST: auto.autoDriveForward(); drive2017.moveValue = 0;
			 * drive2017.turnValue = 0; break;
			 */
			}

			// Send stats to the driver
			// Randy made this a bonding moment
			print();
		}

	}

	public void disabled() {
		while (isDisabled()) {
			//a delay everytime you press the a button. It toggles between the different auto modes
			if (digitBoard.getButtonA()) {
				Timer.delay(0.25);
				auto.autonomousSelector(); // Displays auto on digit board
			}
			//same delay to button b, nothing happens..yet
			if (digitBoard.getButtonB()) {
				Timer.delay(0.25);
				/*
				 * dToggle = !dToggle; if (dToggle)
				 * digitBoard.UpdateDisplay('C', 'U', 'C', 'K'); else
				 * digitBoard.UpdateDisplay('8', '-', '-', 'D');
				 */
			}
			//still prints to dashboard, even while disabled
			print();
		}

	}

	// Sends information to the driver
	private void print() {
		// TODO Just so we can click here easier, nothing to actually do here.
		// The ultimate bonding moment
		SmartDashboard.putNumber("Center X table", table.getNumber("centerX", 0));
		//both of these display current autonomous phase
		SmartDashboard.putNumber("AutoPhase", auto.phase);
		SmartDashboard.putNumber("phase", auto.phase);

		//displays robot's move and turn values
		SmartDashboard.putNumber("Move value", drive2017.moveValue);
		SmartDashboard.putNumber("Turn value", drive2017.turnValue);

		SmartDashboard.putNumber("Gyro Value", gyro.getAngle());
		SmartDashboard.putNumber("Last Gyro Value", auto.lastAngle);
		// ultrasonic values
		SmartDashboard.putNumber("Ultrasonic Value", ultra.getValue());
		SmartDashboard.putNumber("UltraInches Value", ultra.GetRangeInches(false));
		SmartDashboard.putNumber("Ultrasonic Voltage", ultra.GetVoltage());
		SmartDashboard.putNumber("UltraInches Voltage", ultra.GetRangeInches(true));
		//shows the turn value in even more detail
		SmartDashboard.putNumber("Turn Value After Cap", drive2017.turnValue_f);
		SmartDashboard.putNumber("Turn Value Before Cap", drive2017.turnValue);
		//shows how many seconds are on the clock
		SmartDashboard.putNumber("Clock Seconds", clock.get());

		// SmartDashboard.putNumber("AngleFromGoal",
		// table.getDouble("angleFromGoal"));
		// SmartDashboard.putNumber("DistanceFromTarget",
		// table.getDouble("distanceFromTarget"));
		// net table prints
		/*
		 * double distanceFromTarget = table.getDouble("distanceFromTarget");
		 * 
		 * //double[] centerX = table.getNumberArray("centerX");
		 * System.out.println("====Other====");
		 * System.out.println(distanceFromTarget);
		 * System.out.println(angleFromGoal);
		 * System.out.println("====CenterX====");
		 */
		/*
		 * if (centerX != null) { try { System.out.println(centerX[0]);
		 * System.out.println(centerX[1]); } catch (Exception e) {
		 * System.out.println("One of the center x values didn't exist"); } }
		 */

		// pot value
		SmartDashboard.putNumber("Potentiometer value", digitBoard.getPot());

	}
}
