package org.usfirst.frc.team498.robot;

public class Ports {
	//Motors
	final int LEFT_FRONT_PWM_PORT = 8;
	final int LEFT_BACK_PWM_PORT = 9;
	final int RIGHT_FRONT_PWM_PORT = 6;
	final int RIGHT_BACK_PWM_PORT = 7;
	
	
	
	//1 is 9, 2 is 8, 3 is 7, 4 is 6
	
	//We have two talons with CAN wires
	
	//analog sensors
	final int ULTRASONIC_SENSOR = 2;
	//zuntin dat kert med (something that curt made)
	final double forwardRampIncreaseValue = 1.3;
	final double turningRampIncreaseValue = 1.3;
	final double speedCap = .8;
}
