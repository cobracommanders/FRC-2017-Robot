package org.usfirst.frc.team498.robot;

public class Ports {
	//Motors
	final int LEFT_FRONT_PWM_PORT = 2;
	final int LEFT_BACK_PWM_PORT = 1;
	final int RIGHT_FRONT_PWM_PORT = 4;
	final int RIGHT_BACK_PWM_PORT = 3;
	
	//We have two talons with CAN wires
	
	//analog sensors
	final int ULTRASONIC_SENSOR = 2;
	//zuntin dat kert med (something that curt made)
	final double forwardRampIncreaseValue = 1.3;
	final double turningRampIncreaseValue = 1.3;
	final double speedCap = .8;
}
