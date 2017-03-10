package org.usfirst.frc.team498.robot;

//various buttons of controller (axis buttons)
public enum Axis {
	LeftX(0), LeftY(1), RightTrigger(3), LeftTrigger(2), RightX(6), RightY(5);
	public int axisIndex;

	Axis(int axisChannel) {
		axisIndex = axisChannel;
	}
}
