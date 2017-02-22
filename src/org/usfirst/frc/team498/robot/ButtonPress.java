package org.usfirst.frc.team498.robot;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Servo;

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
	}

	public void ServoFrontLeft() {
		servo1.set(1.0);
		/*
		 * if (clock.get() > 0.5) { ServoOff(); }
		 */
	}

	public void ServoFrontRight() {
		servo1.set(0.0);
	}

	public void ServoOff() {
		servo1.setDisabled(); // 0.5 has it turning slightly
		servo2.setDisabled();
	}

	public void ServoRight() {
		servo2.set(0.0);
	}

	public void ServoLeft() {
		servo2.set(1.0);
	}

	// ball intake
	public void IntakeOn() {
		sparkBall.set(0.8);
	}

	public void IntakeOff() {
		sparkBall.set(0);
	}

	public void IntakeReverse() {
		sparkBall.set(-0.8);
	}

	// shooter
	public void Shoot() {
		// double voltage = digitBoard.getPot();
		// double motorValue = voltage; // voltage / 5
		// talon.set(4.85); // motorValue works
		sparkShoot.set(0.86);
	}

	public void StopShoot() {
		sparkShoot.set(0);
	}

	public void intakeConveyorOn() {
		intakeConveyor.set(-1);
	}

	public void intakeConveyorOff() {
		intakeConveyor.set(0);
	}

	// climb
	public void ClimbOn() {
		// For some reason, the motors are calibrated in reverse?
		climb0.set(-1.0);
		climb1.set(-1.0);
	}

	public void ClimbOff() {
		climb0.set(0);
		climb1.set(0);
	}

	public void ClimbReverse() {
		climb0.set(1.0);
		climb1.set(1.0);
	}

	// Flaps for GearIntake
	public void OpenFlap() {
		ds.set(Value.kReverse);
	}

	public void CloseFlap() {
		ds.set(Value.kForward);
	}

	public boolean ADown() {
		if (thisStick.getButton(Button.A) && !aDown) {
			aDown = true;
			return true;
		}
		aDown = thisStick.getButton(Button.A);
		return false;
	}

	// Listener Method
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

		// Shooter
		if (thisStick.getButton(Button.RightBumper) && wasShootPressed == false) {
			isShootRunning = !isShootRunning;
			isConveyorRunning = !isConveyorRunning;
			wasShootPressed = true;
		}
		if (wasShootPressed == true && thisStick.getButton(Button.RightBumper) == false) {
			wasShootPressed = false;
		}

		if (isShootRunning) {
			Shoot();
			intakeConveyorOn();
			ServoLeft();
		} else {
			StopShoot();
			intakeConveyorOff();
			ServoOff();
		} // End of Shooter

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
		// Gear Flap
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

		// Climber
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

		// Intake
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

		// TurboMode
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
