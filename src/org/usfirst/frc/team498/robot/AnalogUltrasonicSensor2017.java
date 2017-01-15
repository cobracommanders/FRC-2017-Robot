package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogUltrasonicSensor2017 {
	AnalogInput AI;

	public AnalogUltrasonicSensor2017(Ports ports) {
		AI = new AnalogInput(ports.ULTRASONIC_SENSOR); // !!!MUY IMPORTANTE!!!
	}

	public double GetRangeMM() {
		double output = 0;
		output = AI.getVoltage();
		output = output / 1000; // Measured in millivolts
		output = output / 0.977;
		return output;
	}

	public double GetRangeInches() {
		double output = GetRangeMM();
		output = output * 25.4;
		return output;
	}

	public double GetVoltage() {
		return AI.getVoltage();
	}
}
