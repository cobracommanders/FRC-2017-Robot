package org.usfirst.frc.team498.robot;

public class Ports {
	// Motor controllers
	final int LEFT_FRONT_PWM_PORT = 2; // 8, 2
	final int LEFT_BACK_PWM_PORT = 1; // 9, 1
	final int RIGHT_FRONT_PWM_PORT = 4;// 6, 4
	final int RIGHT_BACK_PWM_PORT = 3;// 7, 3

	final int SHOOTER_PWM_PORT = 5;
	final int GEAR_INTAKE_FORWARD_CHANNEL = 0;
	final int GEAR_INTAKE_REVERSE_CHANNEL = 1;

	// 1 is 9, 2 is 8, 3 is 7, 4 is 6

	// We have two talons with CAN wires

	// analog sensors
	final int ULTRASONIC_SENSOR_ANALOG_PORT = 0;
	// ramp code
	final double forwardRampIncreaseValue = 1.3;
	final double reverseRampIncreaseValue = .1;
	final double turningRampIncreaseValue = 1.3;

	final double speedCap = .85;
}
