package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Joystick;

public class FancyJoystick {
	Joystick thisStick;
	//initializes the usb channel where your controller is (Usually 0)
	FancyJoystick(int USBChannel) {
		this.thisStick = new Joystick(USBChannel);
	}
	
	//makes sure you're getting a button
	boolean getButton(Button button) {
		return thisStick.getRawButton(button.buttonIndex);
	}
	
	//makes sure you're getting an axis
	double getAxis(Axis axis) {
		return thisStick.getRawAxis(axis.axisIndex);
	}
}
