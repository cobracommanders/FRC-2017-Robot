package org.usfirst.frc.team498.robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class IntakeShooterClimber2017 {
	Vision2017 vision = new Vision2017(0);
	FancyJoystick thisStick;
	public Timer clock;

	private boolean wasShootPressed = false; // Add in if statement for Shoot()
												// later

	public IntakeShooterClimber2017(FancyJoystick joystick, Ports ports) {
		thisStick = joystick;
		clock = new Timer();
	}

	public void Shoot() {
		if (thisStick.getButton(Button.RightBumper)) {
			/*
			 * 
			 */
		}
	}

	public void Intake() {
		// TODO: Add in methods for intake
	}
	
	public void Climb() {
		//TODO: Add in methods for climbing
	}
}
