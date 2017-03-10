package org.usfirst.frc.team498.robot;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

//enumerator that has no use
public enum Enum {
	/*public ADXRS450_Gyro gyro;
	public BuiltInAccelerometer acc;
	public Timer clock;
	public Ultrasonic ultra;
	public Drive2016 drive;
	public IntakeAndShooter2016 accessories;
	public VisionManager2016 vm;
	public LifterArm2016 lifter;
	
	public int goalIndex;
	public int phase = 0;


	// AUTONOMOUS CONSTANTS
	public double distanceToStopFromWall = 20;
	public double distanceToStopFromTower = 15;
	public double angleToTurnTowardsTower = 30;





	switch (phase) {
				// Move Arm Down
	case -1:
					lifter.setLifter(.6);
				if(clock.get() > 2){
					lifter.setLifter(0);
					clock.stop();
					clock.reset();
					phase++;
				}
				

			
					// Drive through low b
			  
					drive.manualDrive(.8, gyro.getAngle());
					if (acc.getZ() > 1.2) {
						clock.start();
					}
					if (clock.get() > 1) {
						phase++;
					}
					break;
			
					// After through low bar, drive until XXX Distance from the wall
					
			 
					if (ultra.getRangeInches() > distanceToStopFromWall) {
						drive.manualDrive(.7, 0);
					}
					break;
			 
					// Turn XXX Degrees to face tower

			 case 1: 
					drive.manualDrive(0, .6);
					if (gyro.getAngle() > angleToTurnTowardsTower) {
						drive.manualDrive(0, 0);
						phase++;
					}
					
					break;
			
					// Drive closer to tower until XXX Distance
			 case 2: 
					drive.manualDrive(distanceToStopFromTower, 0);
					break;
			
					 goalIndex = vm.getBestCantidateIndex();

					try {
						adjustValue = 150 - vm.centerXs[goalIndex] / 150;
						if (adjustValue < .4 && adjustValue > -.4) {
							phase = 6;
						} else {
							clock.reset();
							phase++;
						}
						break;
					} catch (Exception e) {
						System.out.println(e);
					}

					break;
			
					// adjust angle
					// go to case 4
			case 3: 
					drive.manualDrive(0, adjustValue);
					if (clock.get() > .3) {
						drive.manualDrive(0, 0);
						phase = 4;
						break;
					}
					break;
				
					accessories.extendIntake();
					Timer.delay(.5);
					accessories.overrideShoot();
					break;
	}*/
}
	
	



