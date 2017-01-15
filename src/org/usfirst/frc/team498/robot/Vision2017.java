//oooh vision

package org.usfirst.frc.team498.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.vision.VisionRunner;
import edu.wpi.first.wpilibj.vision.VisionRunner.Listener;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class Vision2017 {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;

	private VisionThread visionThread;
	private double contour1CenterX = 0.0;
	private double contour1CenterY = 0.0;
	private double contour1Height = 0.0;

	private double contour2CenterX = 0.0;
	private double contour2CenterY = 0.0;
	private double contour2Height = 0.0;
	
	public boolean flag = false; 

	private final Object imgLock = new Object();

	public Vision2017() {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

		visionThread = new VisionThread(camera, new Pipeline(), pipeline -> {
			if (!pipeline.filterContoursOutput().isEmpty()) {
				Rect contour1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
				Rect contour2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
				synchronized (imgLock) {
					contour1CenterX = contour1.x + (contour1.width / 2);
					contour1CenterY = contour1.y + (contour1.height / 2);
					contour1Height = contour1.height;

					contour2CenterX = contour2.x + (contour2.width / 2);
					contour2CenterY = contour2.y + (contour2.height / 2);
					contour2Height = contour2.height;
					flag = true;
				}

			}
		});
		visionThread.start(); 
	}

	// methods for getting contour values
	public double GetContour1CenterX() {
		return contour1CenterX;
	}

	public double GetContour1CenterY() {
		return contour1CenterY;
	}

	public double GetContour1Height() {
		return contour1Height;
	}

	public double GetContour2CenterX() {
		return contour2CenterX;
	}

	public double GetContour2CenterY() {
		return contour2CenterY;
	}

	public double GetContour2Height() {
		return contour2Height;
	}

	public int GetCameraWidth() {
		return IMG_WIDTH;
	}

	public int GetCameraHeight() {
		return IMG_HEIGHT;
	}
}
