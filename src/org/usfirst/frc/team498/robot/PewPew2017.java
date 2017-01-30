package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Talon;

public class PewPew2017 {

	Talon talon;
	FancyJoystick thisStick;
	REVImprovedDigitBoard  digitBoard;

	boolean wasShootPressed = false;
	boolean isShootRunning = false;

	public PewPew2017(REVImprovedDigitBoard board, FancyJoystick joystick, Ports ports) {

		thisStick = joystick;
		talon = new Talon(ports.SHOOTER_PWM_PORT);
		digitBoard = board;
	}

	public void Shoot() {
		double voltage = digitBoard.getPot();
		double motorValue = voltage / 5;
		
		if (motorValue > 1) {
			motorValue = 1;
		} 
		talon.set(motorValue); 

	}

	public void StopShoot() {
		talon.set(0);
	}

	public void shootListener() {
		if (thisStick.getButton(Button.A) && wasShootPressed == false) {
			isShootRunning = !isShootRunning;
			wasShootPressed = true;
		}
		if (wasShootPressed == true && thisStick.getButton(Button.A) == false) {
			wasShootPressed = false;
		}

		if (isShootRunning) {
			Shoot();
		} else {
			StopShoot();
		}
	}

}
