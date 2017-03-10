package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Timer;



public class RampManager {
	
	
	double currentValue;
	double rampSpeed;
	double lastTime;
	Timer clock = new Timer();
	
	/*
	 * 
	 * Meant to be run in a loop. Use case below
	 * RampTo(Joystick.getValue(axis));
	 * Motor.set(getCurrentValue());
	 * 
	 * 
	 */
	
	RampManager (double percentIncreasePerSecond) {
		rampSpeed = percentIncreasePerSecond;
		currentValue = 0;
		clock.start();
		lastTime = 0;
	}
	//Accelerates robot to desired speed
	public void rampTo(double desiredValue) {
		if (clock.get() <= 0){	//Start the clock if it hasn't started.
			clock.start();
		}
		if(clock.get() > 0.1) {
			clock.reset();
		}
		//if(Math.abs(currentValue) > Math.abs(desiredValue)) {
			//Checks if desired speed has been reached
			if(currentValue < desiredValue) {
			currentValue = clock.get() * rampSpeed + currentValue;
			if(currentValue > 1) {
				currentValue = 1;
			}
			clock.reset();	
		}else if(currentValue > desiredValue) {
			currentValue = clock.get() * -rampSpeed  + currentValue;
			if(currentValue < -1) {
				currentValue = -1;
			}
			clock.reset();	//comment this flat
			//fail save
		} else {
			clock.reset();
		}
		/*} else {
			if(currentValue < desiredValue) {
				
				currentValue = clock.get() * (rampSpeed - .7)+ currentValue;
				if(currentValue > 1) {
					currentValue = 1;
				}
				clock.reset();	
			}else if(currentValue > desiredValue) {
				currentValue = clock.get() * -rampSpeed + currentValue;
				if(currentValue < -1) {
					currentValue = -1;
				}
				clock.reset();	//comment this flat
			} else {
				clock.reset();
			}
		}*/
		if(Math.abs(currentValue) >= Math.abs(desiredValue)){	//Stop the clock and reset if we have gotten to the value we want.
			if (desiredValue < 0 && currentValue < 0){
				currentValue = desiredValue;
			} else if (desiredValue > 0 && currentValue > 0){
				currentValue = desiredValue;
			}
			//clock.stop();	//comment this kyle
			//clock.reset();	//comment this kyle
		}
	}
	public double getCurrentValue() {
		return currentValue;
	}
	
	
}
