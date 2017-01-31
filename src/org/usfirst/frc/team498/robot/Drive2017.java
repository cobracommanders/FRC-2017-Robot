//Made in Syria
package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.RobotDrive;

public class Drive2017 {
	private FancyJoystick thisStick;
	private RobotDrive drive;
	private RampManager forwardDriveRamp;
	private boolean isGoingForward = true;
	private boolean isSpeedReduced = true;
	private boolean wasTransmitionPressed = false;
	double speedCap;

	RampManager turningDriveRamp;
	public double moveValue;
	public double turnValue;

	Drive2017(FancyJoystick joystick, Ports ports) {
		this.thisStick = joystick;

		drive = new RobotDrive(ports.LEFT_FRONT_PWM_PORT, ports.LEFT_BACK_PWM_PORT, ports.RIGHT_FRONT_PWM_PORT,
				ports.RIGHT_BACK_PWM_PORT);
		forwardDriveRamp = new RampManager(ports.forwardRampIncreaseValue);

		turningDriveRamp = new RampManager(ports.turningRampIncreaseValue);
	}

	// The robot's speed slowly increases over time.
	public void rampedDriveListener() {
		// Axis 3 is RT Axis 2 is LT
		forwardDriveRamp.rampTo(thisStick.getAxis(Axis.RightTrigger) - thisStick.getAxis(Axis.LeftTrigger));
		moveValue = forwardDriveRamp.getCurrentValue();
		// Axis 0 is X Value of Left Stick
		turningDriveRamp.rampTo(-thisStick.getAxis(Axis.LeftX));
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
		double moveValue_f;
		double turnValue_f;
		if (false) {
			moveValue_f = moveValue * speedCap;
			turnValue_f = turnValue * speedCap;
		} else {
			moveValue_f = moveValue;
			turnValue_f = turnValue;
		}
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
