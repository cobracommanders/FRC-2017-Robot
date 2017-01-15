package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Joystick;

public class FancyJoystick {
	Joystick thisStick;
	FancyJoystick(int USBChannel) {
		thisStick = new Joystick(USBChannel);
	}
	
	boolean getButton(Button button) {
		return thisStick.getRawButton(button.buttonIndex);
	}
	
	double getAxis(Axis axis) {
		return thisStick.getRawAxis(axis.axisIndex);
	}
}
