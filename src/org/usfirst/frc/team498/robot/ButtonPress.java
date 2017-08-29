package org.usfirst.frc.team498.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Servo;

//methods for buttons
public class ButtonPress { // x, a, back, rightBumper being used
	FancyJoystick thisStick;
	Victor intakeConveyor;
	Spark sparkBall;
	Spark sparkShoot;
	CANTalon climb1;
	CANTalon climb0;
	DoubleSolenoid ds;
	Timer clock;

	Servo servo1;
	Servo servo2;
	// Servo is possibly ports 1 and 2

	boolean wasShootPressed = false;// shoot
	boolean isShootRunning = false;
	// boolean wasConveyorPressed = false;
	boolean isConveyorRunning = false;
	boolean wasIntakePressed = false;// intake
	boolean isIntakeRunning = false;
	boolean wasIntakeReversed = false;
	boolean isIntakeReversed = false;
	// boolean intakeReverse = false;
	boolean wasClimbPressed = false;// climb
	boolean isClimbRunning = false;
	boolean climbReverse = false;
	boolean wasFlapPressed = false;// flaps
	boolean isFlapRunning = false;
	boolean wasTurboPressed = false;
	boolean isTurboRunning = false;
	boolean isServoRunning = false;
	boolean aDown = false;

	public ButtonPress(FancyJoystick thisStick, Ports ports, Servo servo1, Servo servo2) {
		intakeConveyor = new Victor(ports.SHOOTER_INTAKE_CONVEYOR_PWM_VICTOR);
		this.thisStick = thisStick;
		sparkBall = new Spark(ports.SPARK_BALL_INTAKE_PWM_CHANNEL);
		sparkShoot = new Spark(ports.SHOOTER_SPARK_PORT);
		climb0 = new CANTalon(ports.CANTALON_CLIMBER_0);
		climb1 = new CANTalon(ports.CANTALON_CLIMBER_1);
		ds = new DoubleSolenoid(ports.GEAR_INTAKE_FORWARD_CHANNEL, ports.GEAR_INTAKE_REVERSE_CHANNEL);
		this.servo1 = servo1;
		this.servo2 = servo2;
		clock = new Timer();
		
	}

	//turns on second servo
	public void ServoFrontLeft() {
		servo1.set(1.0);
		/*
		 * if (clock.get() > 0.5) { ServoOff(); }
		 */
	}

	//depreciated
	public void ServoFrontRight() {
		servo1.set(0.0);
	}

	//turns servos off
	public void ServoOff() {
		servo1.setDisabled(); // 0.5 has it turning slightly
		servo2.setDisabled();
	}

	//depreciated
	public void ServoRight() {
		servo2.set(0.0);
	}

	//turns on first servo
	public void ServoLeft() {
		servo2.set(1.0);
	}

	// ball intake
	
	//turns on intake
	public void IntakeOn() {
		sparkBall.set(0.8);
	}

	//turns off intake
	public void IntakeOff() {
		sparkBall.set(0);
	}

	//reverse the intake direction
	public void IntakeReverse() {
		sparkBall.set(-0.8);
	}

	int phase = 0;
	// shooter
	public void Shoot() {
		// double voltage = digitBoard.getPot();
		// double motorValue = voltage; // voltage / 5
		// talon.set(4.85); // motorValue works
		//sparkShoot.set(0.86);
		//TeleOpMode teleMode = TeleOpMode.OPERATORCONTROL;
		
		switch (phase) {
		case 0:
			clock.start();
			IntakeOn();
			sparkShoot.set(0.86);
			phase++;
			break;
		case 1:
		//turns on first servo
			ServoLeft();
			sparkShoot.set(0.86);
			if (clock.get() > 0.15) {
				ServoOff();
				phase++;
			}
			break;
		case 2:
		//turns on intake
			IntakeOn();
			sparkShoot.set(0.86);
			if (clock.get() > 2.15) {
				phase++;
			}
			break;
		case 3:
		//waits a little bit
			if (clock.get() > 2.275) {
				phase++;
			}
			break;
		case 4:
		//turns on the second servo
			//buttonPress.ServoRight();
			ServoFrontLeft();
			if (clock.get() > 2.425) {
				phase++;
				ServoOff();
			}
			break; 
		case 5:
		//turns on intake again, and turns it off
			IntakeOn();
			sparkShoot.set(0.86);
			if (clock.get() > 14.9) {
				phase++;
				IntakeOff();
				StopShoot();
			}
			break;
		case 6:
		//turns off servos
			//buttonPress.ServoFrontRight();
			if (clock.get() > 15) {
				phase++;
				ServoOff();
			}
			break;
		/*case 7:
			//StopShoot();
			//teleMode = TeleOpMode.OPERATORCONTROL;
			break;*/
		}
	}

	//turns off the spark control for shooter
	public void StopShoot() {
		sparkShoot.set(0);
	}

	//turns on conveyor
	public void intakeConveyorOn() {
		intakeConveyor.set(-1);
	}

	//turns off conveyor
	public void intakeConveyorOff() {
		intakeConveyor.set(0);
	}

	// climb
	//turns on climber
	public void ClimbOn() {
		// For some reason, the motors are calibrated in reverse?
		climb0.set(-1.0);
		climb1.set(-1.0);
	}

	//turns off climber
	public void ClimbOff() {
		climb0.set(0);
		climb1.set(0);
	}

	//reverses the climber
	public void ClimbReverse() {
		climb0.set(1.0);
		climb1.set(1.0);
	}

	// Flaps for GearIntake
	//opens flap for gear
	public void OpenFlap() {
		ds.set(Value.kReverse);
	}

	//closes flap for gear
	public void CloseFlap() {
		ds.set(Value.kForward);
	}

	//toggle for if a is pressed
	public boolean ADown() {
		if (thisStick.getButton(Button.A) && !aDown) {
			aDown = true;
			return true;
		}
		aDown = thisStick.getButton(Button.A);
		return false;
	}

	// Listener Method
	//listens fro all the buttons pressed
	public void Listener() {
		
		if (ADown()) {
			clock.start();
			isServoRunning = true;
		}
		if (clock.get() > 0.1)
			isServoRunning = false;
		if (isServoRunning) {
			ServoLeft();
		} else {
			ServoOff();
		}

		// Toggles Shooter
		/*if (thisStick.getButton(Button.RightBumper) && wasShootPressed == false) {
			isShootRunning = !isShootRunning;
			//isConveyorRunning = !isConveyorRunning;
			wasShootPressed = true;
		}*/
//		if (wasShootPressed == true && thisStick.getButton(Button.RightBumper) == false) {
//			wasShootPressed = false;
//		}
//
//		if (isShootRunning) {
//			Shoot();
//			//intakeConveyorOn();
//			//ServoLeft();
//		} else {
//			//StopShoot();
//			//intakeConveyorOff();
//			//ServoOff();
//		} // End of Shooter

		/*
		 * if (thisStick.getButton(Button.Y) && wasConveyorPressed == false) {
		 * isConveyorRunning = !isConveyorRunning; wasConveyorPressed = true; }
		 * 
		 * if (wasConveyorPressed == true && thisStick.getButton(Button.Y) ==
		 * false) { wasConveyorPressed = false; }
		 * 
		 * if (isConveyorRunning) { intakeConveyorOn(); } else {
		 * intakeConveyorOff(); }
		 */
		// Toggles Gear Flap
		if (thisStick.getButton(Button.B) && wasFlapPressed == false) {
			isFlapRunning = !isFlapRunning;
			wasFlapPressed = true;
		}

		if (wasFlapPressed == true && thisStick.getButton(Button.B) == false) {
			wasFlapPressed = false;
		}

		if (isFlapRunning) {
			OpenFlap();
		} else {
			CloseFlap();
		} // End of Gear Flap

		// Toggles Climber
		if (thisStick.getButton(Button.BACK) && wasClimbPressed == false) {
			isClimbRunning = !isClimbRunning;
			wasClimbPressed = true;
		}
		if (wasClimbPressed == true && thisStick.getButton(Button.BACK) == false) {
			wasClimbPressed = false;
		}

		if (isClimbRunning) {
			ClimbOn();
		} else {
			ClimbOff();
		} // End of Climber

		// Toggle Intake
		if (thisStick.getButton(Button.X) && wasIntakePressed == false) {
			isIntakeRunning = !isIntakeRunning;
			wasIntakePressed = true;
		}
		if (thisStick.getButton(Button.LeftBumper) && wasIntakeReversed == false) {
			isIntakeReversed = !isIntakeReversed;
			wasIntakeReversed = true;
		}

		if (wasIntakePressed == true && thisStick.getButton(Button.X) == false) {
			wasIntakePressed = false;
		}

		if (wasIntakeReversed == true && thisStick.getButton(Button.LeftBumper) == false) {
			wasIntakeReversed = false;
		}

		if (isIntakeRunning) {
			IntakeOn();
		} else if (isIntakeReversed) {
			IntakeReverse();
		} else {
			IntakeOff();
		} // End of Intake

		// Toggles TurboMode
		if (thisStick.getButton(Button.Y) && wasTurboPressed == false) {
			isTurboRunning = !isTurboRunning;
			wasTurboPressed = true;
		}
		if (wasTurboPressed == true && thisStick.getButton(Button.Y) == false) {
			wasTurboPressed = false;
		}
		// end of TurboMode

		Drive2017.turbo = isTurboRunning;
	}

}
