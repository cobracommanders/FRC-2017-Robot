//Made in Syria
package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.RobotDrive;

public class Drive2017 {
	private FancyJoystick thisStick;
	private RobotDrive drive;
	private RampManager forwardDriveRamp;
	private boolean isGoingForward = true;
	private boolean isSpeedReduced = true;
	private boolean wasTransmitionPressed = false;
	double turnCap = 0.75; // 0.66
	double moveCap = 0.75;
	ADXRS450_Gyro gyro;
	boolean trigDown = false;
	boolean turning = false;
	static boolean turbo;

	RampManager turningDriveRamp;
	public double moveValue;
	public double turnValue;

	double moveValue_f;
	double turnValue_f;

	Drive2017(FancyJoystick joystick, Ports ports, ADXRS450_Gyro gyro) {
		this.thisStick = joystick;
		this.gyro = gyro;

		drive = new RobotDrive(ports.LEFT_FRONT_PWM_PORT, ports.LEFT_BACK_PWM_PORT, ports.RIGHT_FRONT_PWM_PORT,
				ports.RIGHT_BACK_PWM_PORT);
		forwardDriveRamp = new RampManager(ports.forwardRampIncreaseValue);

		turningDriveRamp = new RampManager(ports.turningRampIncreaseValue);
	}

	//makes the robot drive straight using soem compensation from the gyro.
	public double AngleComp() {
		if (thisStick.getAxis(Axis.RightTrigger) >= 0.2)
			return -gyro.getAngle() * 0.25;
		else
			return 0;

	}

	//returns the movecap, unless you're in turbo mode
	public double MoveCap() {
		if (turbo)
			return 1;
		else
			return moveCap;
	}

	// The robot's speed slowly increases over time.
	public void rampedDriveListener() {
		// Axis 3 is RT Axis 2 is LT
		forwardDriveRamp
				.rampTo((thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger)) * MoveCap());
		moveValue = forwardDriveRamp.getCurrentValue();
		// Axis 0 is X Value of Left Stick
		if (Math.abs(thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger)) > 0.2 && !trigDown) {
			trigDown = true;
			gyro.reset(); //resets the gyro 
		} else {
			trigDown = false;
		}
		if (Math.abs(thisStick.getAxis(Axis.LeftX)) <= 0.2 && turning) {
			turning = false;
			gyro.reset(); //resets the gyro 
		} else {
			turning = true;
		}
		if (Math.abs(thisStick.getAxis(Axis.LeftX)) <= 0.2) {
			turningDriveRamp.rampTo(AngleComp());
		} else {
			turningDriveRamp.rampTo(-thisStick.getAxis(Axis.LeftX) * turnCap);
		}
		turnValue = turningDriveRamp.getCurrentValue();
		// turnValue = -thisStick.getAxis(Axis.LeftX);
		transmitionListener();
		// reverseListener();
		drive();
	}

	//drive method without a ramp
	public void ramplessDriveListener() {
		moveValue = thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger);
		turnValue = -thisStick.getAxis(Axis.LeftX);
		transmitionListener();
		// reverseListener();
		drive();
	}
	
	//makes it so you press RB and it inverst the controls, and you press LB to change it back.
	private void reverseListener() {
		if (thisStick.getButton(Button.LeftBumper)) {
			isGoingForward = true;
		} else if (thisStick.getButton(Button.RightBumper)) {
			isGoingForward = false;
		}
	}

	//a toggle from what it looks like, no use though?
	private void transmitionListener() {
		if (thisStick.getButton(Button.X) && !wasTransmitionPressed) {
			isSpeedReduced = !isSpeedReduced;
			wasTransmitionPressed = true;
		}
		if (wasTransmitionPressed && thisStick.getButton(Button.X)) {
			wasTransmitionPressed = false;
		}
	}

	//drives
	private void drive() {
		moveValue_f = moveValue;
		turnValue_f = turnValue;

		drive.arcadeDrive(-moveValue_f, turnValue_f);

	}

	//driving with already inputted values from the user. 
	public void manualDrive(double move, double turn) {
		drive.arcadeDrive(move, turn);
	}

}
