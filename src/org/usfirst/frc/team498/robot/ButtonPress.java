package org.usfirst.frc.team498.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class ButtonPress {
	public FancyJoystick thisStick;
	Talon talon;
	Spark sparkBall;
	CANTalon climb1;
	CANTalon climb0;
	DoubleSolenoid ds;

	boolean wasShootPressed = false;// shoot
	boolean isShootRunning = false;
	boolean wasIntakePressed = false;// intake
	boolean isIntakeRunning = false;
	boolean intakeReverse = false;
	boolean wasClimbPressed = false;// climb
	boolean isClimbRunning = false;
	boolean climbReverse = false;
	boolean wasFlapPressed = false;// flaps
	boolean isFlapRunning = false;

	public ButtonPress(FancyJoystick thisStick, Ports ports) {
		talon = new Talon(ports.SHOOTER_PWM_PORT);
		this.thisStick = thisStick;
		sparkBall = new Spark(ports.SPARK_BALL_INTAKE_PWM_CHANNEL);
		climb0 = new CANTalon(ports.CANTALON_CLIMBER_0);
		climb1 = new CANTalon(ports.CANTALON_CLIMBER_1);
		ds = new DoubleSolenoid(ports.GEAR_INTAKE_FORWARD_CHANNEL, ports.GEAR_INTAKE_REVERSE_CHANNEL);
	}

	// ball intake
	public void IntakeOn() {
		sparkBall.set(1);
	}

	public void IntakeOff() {
		sparkBall.set(0);
	}

	public void IntakeReverse() {
		sparkBall.set(-1);
	}

	// shooter
	public void Shoot() {
		// double voltage = digitBoard.getPot();
		// double motorValue = voltage; // voltage / 5
		talon.set(4.85); // motorValue works
	}

	public void StopShoot() {
		talon.set(0);
	}

	// climb
	public void ClimbOn() {
		// For some reason, the motors are calibrated in reverse?
		climb0.set(-1.0);
		climb1.set(-1.0);
	}

	public void ClimbOff() {
		climb0.set(0);
		climb1.set(0);
	}

	public void ClimbReverse() {
		climb0.set(1.0);
		climb1.set(1.0);
	}

	// Flaps for GearIntake
	public void OpenFlap() {
		ds.set(Value.kReverse);
	}

	public void CloseFlap() {
		ds.set(Value.kForward);
	}

	// Listener Method
	public void Listener() {

		// Shooter
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
		} // End of Shooter

		// Gear Flap
		if (thisStick.getButton(Button.A) && wasFlapPressed == false) {
			isFlapRunning = !isFlapRunning;
			wasFlapPressed = true;
		}

		if (wasFlapPressed == true && thisStick.getButton(Button.A) == false) {
			wasFlapPressed = false;
		}

		if (isFlapRunning) {
			OpenFlap();
		} else {
			CloseFlap();
		} // End of Gear Flap

		// Climber
		if (thisStick.getButton(Button.BACK) && wasClimbPressed == false) {
			isClimbRunning = !isClimbRunning;
			wasClimbPressed = true;
		}
		if (wasClimbPressed == true && thisStick.getButton(Button.BACK) == false) {
			wasClimbPressed = false;
		}

		if (isClimbRunning) {
			ClimbOn();
		} else {
			ClimbOff();
		} // End of Climber

		// Intake
		if (thisStick.getButton(Button.X) && wasIntakePressed == false) {
			isIntakeRunning = !isIntakeRunning;
			wasIntakePressed = true;

			if (thisStick.getButton(Button.LeftBumper)) {
				intakeReverse = true;
			} else {
				intakeReverse = false;
			}
		}
		
		if (wasIntakePressed == true && thisStick.getButton(Button.X) == false) {
			wasIntakePressed = false;
		}

		if (isIntakeRunning) {
			if (intakeReverse)
				IntakeReverse();
			else
				IntakeOn();
		} else {
			IntakeOff();
		} // End of Intake

	}

}
