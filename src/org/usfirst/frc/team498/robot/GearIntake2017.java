package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Spark;

public class GearIntake2017 {

	FancyJoystick thisStick;
	DoubleSolenoid ds;
	boolean aOldState = false;
	
	boolean wasIntakePressed = false;
	boolean isIntakeRunning = false;

	Spark spark;


	public GearIntake2017(FancyJoystick thisStick, Ports ports, Spark spark) {
		ds = new DoubleSolenoid(ports.GEAR_INTAKE_FORWARD_CHANNEL, ports.GEAR_INTAKE_REVERSE_CHANNEL);
		this.thisStick = thisStick;
		this.spark = new Spark(ports.SPARK_BALL_INTAKE_PWM_CHANNEL);

	}

	public boolean ADown() {
		boolean localTemp = false;
		if (!aOldState && thisStick.getButton(Button.A))
			localTemp = true;
		aOldState = thisStick.getButton(Button.A);
		return localTemp;
	}
	
	public void IntakeOn() {
		spark.set(1);
	}
	
	public void IntakeOff() {
		spark.set(0);
	}

	public void Listener() {
		
		if (thisStick.getButton(Button.X) && wasIntakePressed == false) {
			isIntakeRunning = !isIntakeRunning;
			wasIntakePressed = true;
		} 
		if (wasIntakePressed == true && thisStick.getButton(Button.X) == false ) {
			wasIntakePressed = false;
		}
		
		if (isIntakeRunning) {
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
