package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class GearIntake2017 {

	FancyJoystick thisStick;
	DoubleSolenoid ds;
	boolean aOldState = false;

	public GearIntake2017(FancyJoystick thisStick, Ports ports) {
		ds = new DoubleSolenoid(ports.GEAR_INTAKE_FORWARD_CHANNEL, ports.GEAR_INTAKE_REVERSE_CHANNEL);
		this.thisStick = thisStick;
	}

	public boolean ADown() {
		boolean localTemp = false;
		if (!aOldState && thisStick.getButton(Button.A))
			localTemp = true;
		aOldState = thisStick.getButton(Button.A);
		return localTemp;
	}

	public void Listener() {
		if (ADown()) {
			if (ds.get() == Value.kOff || ds.get() == Value.kForward)
				OpenIntake();
			else if (ds.get() == Value.kReverse)
				CloseIntake();

		}
	}

	public void OpenIntake() {
		ds.set(Value.kReverse);
	}

	public void CloseIntake() {
		ds.set(Value.kForward);
	}
}
