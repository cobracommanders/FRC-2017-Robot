package org.usfirst.frc.team498.robot;

//shooter class that was replaced by the button press class
import edu.wpi.first.wpilibj.Talon;

public class PewPew2017 {

	Talon talon;
	FancyJoystick thisStick;
	REVImprovedDigitBoard digitBoard;

	boolean wasShootPressed = false;
	boolean isShootRunning = false;

	public PewPew2017(REVImprovedDigitBoard board, FancyJoystick joystick, Ports ports) {

		thisStick = joystick;
		talon = new Talon(ports.SHOOTER_INTAKE_CONVEYOR_PWM_VICTOR);
		digitBoard = board;
	}

	//sets the motor controller (talon) to a set speed (depreciated)
	public void Shoot() {
		//double voltage = digitBoard.getPot();
		//double motorValue = voltage; // voltage / 5
		talon.set(4.85); // motorValue works
	}

	//stops the motor controller (depreciated)
	public void StopShoot() {
		talon.set(0);
	}

	//checks to see if you pressed the shoot button, which was right bumper (depreciated)
	public void shootListener() {
		if (thisStick.getButton(Button.RightBumper) && wasShootPressed == false) {
			isShootRunning = !isShootRunning;
			wasShootPressed = true;
		}
		if (wasShootPressed == true && thisStick.getButton(Button.RightBumper) == false) {
			wasShootPressed = false;
		}

		if (isShootRunning) {
			Shoot();
		} else {
			StopShoot();
		}
	}

}
