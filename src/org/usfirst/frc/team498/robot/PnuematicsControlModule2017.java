package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class PnuematicsControlModule2017 { // TODO: NEEDS TO BE COMPLETED
	Timer clock;
	VictorSP vicsp;
	Compressor c = new Compressor(0);
	DoubleSolenoid ds = new DoubleSolenoid(1, 2);

	public PnuematicsControlModule2017() {
		clock = new Timer();
		vicsp = new VictorSP(3);
	}

	public void turnOn() { // supposed to turn on ports 0,1,2,3 on PCM. Needs to
							// add more stuff
		c.getClosedLoopControl();
		ds.get();
	}
}
