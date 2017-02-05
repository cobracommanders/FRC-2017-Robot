//oooh vision
//get it working randy or else.

package org.usfirst.frc.team498.robot;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
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
	

	public Vision2017(int cam) {
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture("cam0", 0);
		camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

		//cam0.addCamera(camera);

		// 2 camera code
		/*
		 * int currSession; int sessionfront; int sessionback; Image frame;
		 */

		
		/*new Thread(() -> {
			UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
			camera.setResolution(640, 480);

			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 640, 480);

			Mat source = new Mat();
			Mat output = new Mat();

			while (true) {
				cvSink.grabFrame(source);
				Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
				outputStream.putFrame(output);
			}
		}).start();*/
		
		// Good, you create a VisionThread
		/*visionThread = new VisionThread(camera, new Pipeline(), pipeline -> {
			// I would modify this if statement to check if you have 2 contours:
			// pipeline.filterContoursOutput().size() >= 2

			
			  //modified
			  if (pipeline.filterContoursOutput().size() >= 2) {
				Rect contour1 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
				Rect contour2 = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1));
				// We take the lock here but we never use it anywhere else? This
				// seems weird. // I can't help much more unless I can see your
				// entire
				// * program.
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
		});*/

		
		//visionThread.start();
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
