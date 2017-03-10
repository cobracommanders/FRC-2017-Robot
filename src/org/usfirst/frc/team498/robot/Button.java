package org.usfirst.frc.team498.robot;

//various buttons for controller (toggles)
public enum Button {
	A(1),B(2),X(3),Y(4),LeftBumper(5),RightBumper(6),BACK(7),START(8),LeftJoystick(9),RightJoystick(10), /*DPadUp(12), DPadDown(13), DPadLeft(14), DPadRight(15)*/;
	public int buttonIndex;
	Button(int buttonNum) {
		buttonIndex = buttonNum;
	}
}
