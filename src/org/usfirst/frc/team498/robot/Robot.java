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
	
	// Drive
	Ports ports = new Ports();
	private Timer clock;
	FancyJoystick thisStick = new FancyJoystick(0);
	Shooter2017 shooter = new Shooter2017(ports);
	Drive2016 drive = new Drive2016(thisStick, ports);
	AutonmousController auto = new AutonmousController(drive, shooter, ports);
	

	
	
	Ultrasonic ultrasonic = new Ultrasonic(0, 1);
	



	PowerDistributionPanel pdp = new PowerDistributionPanel();

;

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
		auto.gyro.reset();

		TeleOpMode mode = TeleOpMode.OPERATORCONTROL;

		while (isOperatorControl() && isEnabled()) {
			// Checks button
			if (thisStick.getButton(Button.Y)) {
				auto.gyro.reset();
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
				mode = auto.AlignGearPeg();
				break;
			case HIGHGOALALIGNMENT:
				mode = auto.AlignHighGoal();
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
		SmartDashboard.putNumber("Range (Inches)", ultrasonic.getRangeInches());
		SmartDashboard.putNumber("Range millimeters (Analog)", auto.analogSensor.GetRangeMM());
		SmartDashboard.putNumber("Range Inches (Analog)", auto.analogSensor.GetRangeInches());
		SmartDashboard.putNumber("Voltage (Analog)", auto.analogSensor.GetVoltage());

	}

}
