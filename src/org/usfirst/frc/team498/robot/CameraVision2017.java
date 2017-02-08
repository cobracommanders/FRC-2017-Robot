package org.usfirst.frc.team498.robot;




import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class CameraVision2017 {
	private double centerXOne = 0.0;
	private double centerYOne = 0.0;
	private double centerXTwo = 0.0;
	private double centerYTwo = 0.0;
	private UsbCamera cam0;
	private UsbCamera cam1;
	private final int RES_WIDTH = 320;
	private final int RES_HEIGHT = 240;
	public static List<MatOfPoint> matOfPoints;
	public static List<Rect> boxes;
	
	public CameraVision2017(UsbCamera cam0, UsbCamera cam1) {
		this.cam0 = cam0;
		this.cam1 = cam1;
		cam0.setResolution(RES_WIDTH, RES_HEIGHT);
		cam1.setResolution(RES_WIDTH, RES_HEIGHT);
		startThread();
	}
	
	public CameraVision2017() {
		cam0 = CameraServer.getInstance().startAutomaticCapture(0);
		cam1 = CameraServer.getInstance().startAutomaticCapture(1);
		cam0.setResolution(RES_WIDTH, RES_HEIGHT);
		cam1.setResolution(RES_WIDTH, RES_HEIGHT);
		startThread();
	}
	
	public void startThread() {
		new Thread(() -> {
			GearPipeline gearPipe = new GearPipeline();
			
			CvSink cvSink = CameraServer.getInstance().getVideo();
			CvSource outputStream = CameraServer.getInstance().putVideo("Output Stream", 640, 480);
			
			Mat frame = new Mat();
			
			while(true) {
				cvSink.grabFrame(frame);
				gearPipe.process(frame);
				matOfPoints = gearPipe.filterContoursOutput();
				boxes.clear();
				for(int i = 0; i < matOfPoints.size(); i++) {
					boxes.add(Imgproc.boundingRect(matOfPoints.get(i)));
				}
				outputStream.putFrame(gearPipe.rgbThresholdOutput());
			}
		}).start();
	}
	
	//public double GetCenter() {
	//	List<Double> widths;
	//	List<Double> heights;
	//	
	//}
}
