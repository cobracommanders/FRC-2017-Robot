package org.usfirst.frc.team498.robot;

public class Ports {
	// Motor controllers
	final int LEFT_FRONT_PWM_PORT = 8; // 8 (1st robot)
	final int LEFT_BACK_PWM_PORT = 9; // 9,  
	final int RIGHT_FRONT_PWM_PORT = 6;// 6, 
	final int RIGHT_BACK_PWM_PORT = 7;// 7,  

	final int SHOOTER_PWM_PORT = 5; //4 (1st robot), 5 (test robot)
	final int GEAR_INTAKE_FORWARD_CHANNEL = 2; //0, 2
	final int GEAR_INTAKE_REVERSE_CHANNEL = 3; //1, 3
	final int SPARK_BALL_INTAKE_PWM_CHANNEL = 6; //5 (1st robot)

	// 1 is 9, 2 is 8, 3 is 7, 4 is 6

	// We have two talons with CAN wires

	// analog sensors
	final int ULTRASONIC_SENSOR_ANALOG_PORT = 1; //1
	final int ULTRASONIC_SENSOR_PCM_PORT = 0; //0
	// ramp code
	final double forwardRampIncreaseValue = 1.3;
	final double reverseRampIncreaseValue = .1;
	final double turningRampIncreaseValue = 1.3;

	final double speedCap = .85;
}
