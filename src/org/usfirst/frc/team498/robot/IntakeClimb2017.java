package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Spark;

public class IntakeClimb2017 {

	Spark sparkBall;
	Spark sparkClimb;
	FancyJoystick thisStick;
	DoubleSolenoid ds;
	boolean aOldState = false;

	boolean wasIntakePressed = false;
	boolean isIntakeRunning = false;
	boolean intakeReverse = false;
	
	boolean wasClimbPressed = false;
	boolean isClimbRunning = false;

	public IntakeClimb2017(FancyJoystick thisStick, Ports ports) {
		ds = new DoubleSolenoid(ports.GEAR_INTAKE_FORWARD_CHANNEL, ports.GEAR_INTAKE_REVERSE_CHANNEL);
		this.thisStick = thisStick;
		sparkBall = new Spark(ports.SPARK_BALL_INTAKE_PWM_CHANNEL);
		sparkClimb = new Spark(ports.SPARK_CLIMB_PWM_CHANNEL);

	}

	public boolean ADown() {
		boolean localTemp = false;
		if (!aOldState && thisStick.getButton(Button.A))
			localTemp = true;
		aOldState = thisStick.getButton(Button.A);
		return localTemp;
	}
	
	//ball
	public void IntakeOn() {
		sparkBall.set(1);
	}

	public void IntakeOff() {
		sparkBall.set(0);
	}

	public void IntakeReverse() {
		sparkBall.set(-1);
	}
	
	//climb
	public void ClimbOn() {
		sparkClimb.set(1);
	}
	
	public void ClimbOff() {
		sparkClimb.set(0);
	}
	
	public void Listener() {
		
		if ((thisStick.getButton(Button.BACK) && thisStick.getButton(Button.RightJoystick)) && wasClimbPressed == false) {
			isClimbRunning = !isClimbRunning;
			wasClimbPressed = true;
		}
		
		if (wasClimbPressed == true && (thisStick.getButton(Button.BACK) && thisStick.getButton(Button.RightJoystick)) == false) {
			wasClimbPressed = false;
		}
		
		if (isClimbRunning) {
			ClimbOn();
		} else {
			ClimbOff();
		}

		if (thisStick.getButton(Button.X) && wasIntakePressed == false) {
			isIntakeRunning = !isIntakeRunning;
			wasIntakePressed = true;
			if (thisStick.getButton(Button.LeftBumper))
				intakeReverse = true;
			else
				intakeReverse = false;

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
		}

		if (ADown()) {
			if (ds.get() == Value.kOff || ds.get() == Value.kForward)
				OpenFlap();
			else if (ds.get() == Value.kReverse)
				CloseFlap();

		}
	}

	public void OpenFlap() {
		ds.set(Value.kReverse);
	}

	public void CloseFlap() {
		ds.set(Value.kForward);
	}

}
