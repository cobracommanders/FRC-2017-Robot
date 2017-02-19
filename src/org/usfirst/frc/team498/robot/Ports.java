package org.usfirst.frc.team498.robot;

public class Ports {
	//PWM
	final int LEFT_FRONT_PWM_PORT = 8; // 8 (1st robot)
	final int LEFT_BACK_PWM_PORT = 9; // 9,  
	final int RIGHT_FRONT_PWM_PORT = 6;// 6, 
	final int RIGHT_BACK_PWM_PORT = 7;// 7,  
	
	final int SHOOTER_INTAKE_CONVEYOR_PWM_VICTOR = 3;
	final int SHOOTER_SPARK_PORT = 4; //TODO CHANGE
	
	final int SPARK_BALL_INTAKE_PWM_CHANNEL = 5; //5 (1st robot), 1
	final int CANTALON_CLIMBER_0 = 0;
	final int CANTALON_CLIMBER_1 = 1;
	//final int TALON_CONVEYOR_CHANNEL = 1;

	
	//PCM
	final int GEAR_INTAKE_FORWARD_CHANNEL = 2;
	final int GEAR_INTAKE_REVERSE_CHANNEL = 3;
	



	// 1 is 9, 2 is 8, 3 is 7, 4 is 6

	// analog sensors
	final int ULTRASONIC_SENSOR_ANALOG_PORT = 1; //1
	final int ULTRASONIC_SENSOR_PCM_PORT = 0; //0
	// ramp code
	final double forwardRampIncreaseValue = 2.0; //used
	final double turningRampIncreaseValue = 2.0; //used
}
