package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Solenoid;

public class AnalogUltrasonicSensor2017 {
	AnalogInput AI;
	Solenoid sol;
	FancyJoystick thisStick;

	public AnalogUltrasonicSensor2017(FancyJoystick thisStick, Ports ports) {
		this.thisStick = thisStick;
		AI = new AnalogInput(ports.ULTRASONIC_SENSOR_ANALOG_PORT); // !!!MUY

		sol = new Solenoid(ports.ULTRASONIC_SENSOR_PCM_PORT);
		// IMPORTANTE!!!
	}

	/*
	 * Min. 2 inches Max 1 ft;
	 */

	public double GetRangeMM() {
		//double output = 0;
		//output = AI.getVoltage();
		//output = output / 1000; // Measured in millivolts
		//output = output / 0.977;
		return GetRangeInches() / 25.4;
	}

	public double GetRangeInches() {
		// double output = GetRangeMM();
		// output = output * 25.4;
		return (getValue() / 400) + 2;
	}

	public double GetVoltage() {
		return AI.getVoltage();
	}

	public double getValue() {
		// getValue seems more reliable than getRangeInches, it actually moves,
		// but can't go past 4000. I don't know how it's measured.
		return AI.getValue();
	}

	public void ultraListener() {
		if (!sol.get()) {
			sol.set(true);
		}
	}
}
