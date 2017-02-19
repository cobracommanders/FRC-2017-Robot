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
	double turnCap = 0.75; //0.66
	double moveCap = 0.8; //don't increase above 0.8
	ADXRS450_Gyro gyro;
	boolean trigDown = false;
	boolean turning = false;
	static boolean turbo = false;
	
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

	public double AngleComp() {
		return gyro.getAngle() * -0.3;
	}
	
	public double Speed() {
		if(turbo) {
			return 1;
		} else {
			return moveCap;
		}
	}
	
	// The robot's speed slowly increases over time.
	public void rampedDriveListener() {
		// Axis 3 is RT Axis 2 is LT
		forwardDriveRamp.rampTo((thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger)) * Speed());
		moveValue = forwardDriveRamp.getCurrentValue();
		// Axis 0 is X Value of Left Stick
		if(Math.abs(thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger)) > 0.2 && !trigDown) {
			trigDown = true;
			gyro.reset();
		} else {
			trigDown = false;
		}
		if(Math.abs(thisStick.getAxis(Axis.LeftX)) <= 0.2 && turning) {
			turning = false;
			gyro.reset();
		} else {
			turning = true;
		}
		if(Math.abs(thisStick.getAxis(Axis.LeftX)) <= 0.2 ){
			turningDriveRamp.rampTo(AngleComp());
		}else{
			turningDriveRamp.rampTo(-thisStick.getAxis(Axis.LeftX)*turnCap);
		}
		turnValue = turningDriveRamp.getCurrentValue();
		// turnValue = -thisStick.getAxis(Axis.LeftX);
		transmitionListener();
		reverseListener();
		drive();
	}

	public void ramplessDriveListener() {
		moveValue = thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger);
		turnValue = -thisStick.getAxis(Axis.LeftX);
		transmitionListener();
		reverseListener();
		drive();
	}

	private void reverseListener() {
		if (thisStick.getButton(Button.LeftBumper)) {
			isGoingForward = true;
		} else if (thisStick.getButton(Button.RightBumper)) {
			isGoingForward = false;
		}
	}

	private void transmitionListener() {
		if (thisStick.getButton(Button.X) && !wasTransmitionPressed) {
			isSpeedReduced = !isSpeedReduced;
			wasTransmitionPressed = true;
		}
		if (wasTransmitionPressed && thisStick.getButton(Button.X)) {
			wasTransmitionPressed = false;
		}
	}

	private void drive() {
		moveValue_f = moveValue;
		turnValue_f = turnValue;
		if (isGoingForward) {
			drive.arcadeDrive(moveValue_f, turnValue_f);
		} else {
			drive.arcadeDrive(-moveValue_f, turnValue_f);
		}
	}

	public void manualDrive(double move, double turn) {
		drive.arcadeDrive(move, turn);
	}

}
