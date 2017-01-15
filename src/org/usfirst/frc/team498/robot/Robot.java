package org.usfirst.frc.team498.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * 
 * 
 *  The revised version of Unnamed Mark 2
 * 
 * 
 * 
 */
public class Robot extends SampleRobot {

	// Drive
	Ports ports = new Ports();
	private Timer clock;
	FancyJoystick thisStick = new FancyJoystick(0);
	Shooter2017 shooter = new Shooter2017(ports);
	Drive2016 drive = new Drive2016(thisStick, ports);
	AutonmousController auto = new AutonmousController(drive, shooter, ports);

	Ultrasonic ultrasonic = new Ultrasonic(0, 1);

	PowerDistributionPanel pdp = new PowerDistributionPanel();

	;

	@Override
	public void robotInit() {
		 //CameraServer.getInstance().startAutomaticCapture();
		 
		/* new Thread(() -> {
             UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
             camera.setResolution(640, 480);
             
             CvSink cvSink = CameraServer.getInstance().getVideo();
             CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);
             
             Mat source = new Mat();
             Mat output = new Mat();
                          
             while(true) {
                 cvSink.grabFrame(source);
                 Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                 outputStream.putFrame(output);
             }
         }).start();
 } */
	}

	// Select which autonomous to run
	public void autonomous() {

		while (isAutonomous() && isEnabled()) {

		}

	}

	public void operatorControl() {

		drive.moveValue = 0;
		drive.turnValue = 0;
		auto.gyro.reset();

		TeleOpMode mode = TeleOpMode.OPERATORCONTROL;

		while (isOperatorControl() && isEnabled()) {
			// Checks button
			if (thisStick.getButton(Button.Y)) {
				auto.gyro.reset();
			}
			if (thisStick.getButton(Button.A)) {
				mode = TeleOpMode.HIGHGOALALIGNMENT;
			} else if (thisStick.getButton(Button.B)) {
				mode = TeleOpMode.GEARALIGNMENT;
			} else if (thisStick.getButton(Button.START)) {
				mode = TeleOpMode.OPERATORCONTROL;
			} else if (thisStick.getButton(Button.X)) {
				mode = TeleOpMode.TEST;
			}

			switch (mode) {
			case OPERATORCONTROL:
				// Drive the robot via controller
				drive.rampedDriveListener();
				break;
			case GEARALIGNMENT:
				mode = auto.AlignGearPeg();
				break;
			case HIGHGOALALIGNMENT:
				mode = auto.AlignHighGoal();
				break;
			case TEST:
				auto.testDrive();
				drive.moveValue = 0;
				drive.turnValue = 0;
				break;
			}

			// Send stats to the driver
			print();
		}

	}

	public void disabled() {
		while (isDisabled()) {
			print();
		}

	}

	// Sends information to the driver
	private void print() {

		SmartDashboard.putNumber("Gyro Angle", auto.gyro.getAngle());
		SmartDashboard.putNumber("Range (Inches)", ultrasonic.getRangeInches());
		SmartDashboard.putNumber("Range millimeters (Analog)", auto.analogSensor.GetRangeMM());
		SmartDashboard.putNumber("Range Inches (Analog)", auto.analogSensor.GetRangeInches());
		SmartDashboard.putNumber("Voltage (Analog)", auto.analogSensor.GetVoltage());
		
		SmartDashboard.putNumber("Contour1 CenterX", auto.vision.GetContour1CenterX());
		SmartDashboard.putNumber("Contour1 CenterY", auto.vision.GetContour1CenterY());
		SmartDashboard.putNumber("Contour1 Height", auto.vision.GetContour1Height());
		SmartDashboard.putNumber("Contour2 CenterX", auto.vision.GetContour2CenterX());
		SmartDashboard.putNumber("Contour2 CenterY", auto.vision.GetContour2CenterY());
		SmartDashboard.putNumber("Contour2 Height", auto.vision.GetContour2Height());
		
		SmartDashboard.putBoolean("flag", auto.vision.flag);

		//SmartDashboard.putNumber("Ramp Clock", drive.forwardDriveRamp.clock.get());

	}

}
