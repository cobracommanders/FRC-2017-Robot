package org.usfirst.frc.team498.robot;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera {
	
	public Object imgLock = new Object();
	private Thread visionThread;
	private BoilerPipeline pipeline;
	private boolean runProcessing = false;
	public double centerXOne = 0.0;
	public double centerYOne = 0.0;
	private double centerXTwo = 0.0;
	private double centerYTwo = 0.0;
	public double centerXAvg = 0.0;
	public double centerYAvg = 0.0;
	private double rectangleArea=0.0;
	
	
	public Camera() {
		enableVisionThread(); //outputs a processed feed to the dashboard (overlays the found boiler tape)
	}
	
	public void enableVisionThread() {
		pipeline = new BoilerPipeline();
		//AxisCamera camera = CameraServer.getInstance().addAxisCamera("10.3.3.6");
		UsbCamera camera0 = CameraServer.getInstance().startAutomaticCapture("cam0", 0);
		camera0.setResolution(480, 360);
		UsbCamera camera1 = CameraServer.getInstance().startAutomaticCapture("cam1", 1);
		camera1.setResolution(480, 360);
		
		CvSink cvSink = CameraServer.getInstance().getVideo(); //capture mats from camera
		CvSource outputStream = CameraServer.getInstance().putVideo("Stream", 480, 360); //send steam to CameraServer
		Mat mat = new Mat(); //define mat in order to reuse it
		
		runProcessing = true;
		
		visionThread = new Thread(() -> {
			
			while(!Thread.interrupted()) { //this should only be false when thread is disabled
				
				if(cvSink.grabFrame(mat)==0) { //fill mat with image from camera)
					outputStream.notifyError(cvSink.getError()); //send an error instead of the mat
					SmartDashboard.putString("Vision State", "Acquisition Error");
					continue; //skip to the next iteration of the thread
				}
				
				if(runProcessing) {		
					
					pipeline.process(mat); //process the mat (this does not change the mat, and has an internal output to pipeline)
					int contoursFound = pipeline.filterContoursOutput().size();
					
					if(contoursFound>=2) {
						
						Rect rectOne = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0)); //get the first MatOfPoint (contour), calculate bounding rectangle
						Rect rectTwo = Imgproc.boundingRect(pipeline.filterContoursOutput().get(1)); //get the second MatOfPoint (contour)
						Rect rectThree = null;
						
						if(contoursFound>2) {
							
							SmartDashboard.putString("More Vision State", "Saw Three Contours");
							rectThree = Imgproc.boundingRect(pipeline.filterContoursOutput().get(2));
							ArrayList<Rect> orderedRectangles= new ArrayList<Rect>();
							orderedRectangles.add(rectOne);
							if(rectTwo.area()>rectOne.area()){
								orderedRectangles.add(0,rectTwo);
							} else {
								orderedRectangles.add(rectTwo);
							} 
							
							if(rectThree.area()>orderedRectangles.get(0).area()){
								orderedRectangles.add(0,rectThree);
							} else if(rectThree.area()>orderedRectangles.get(1).area()){
								orderedRectangles.add(1,rectThree);
							} else {
								orderedRectangles.add(rectThree);
							}
							
							Rect topRect = (orderedRectangles.get(2).y>orderedRectangles.get(1).y) ? orderedRectangles.get(1) : orderedRectangles.get(2); 
							Rect bottomRect = (orderedRectangles.get(2).y>orderedRectangles.get(1).y) ? orderedRectangles.get(2) : orderedRectangles.get(1);
							Rect mergedRect = new Rect(topRect.x, topRect.y, (int)(bottomRect.br().x-topRect.tl().x), (int)(bottomRect.br().y-topRect.tl().y));
							
							if(rectOne==orderedRectangles.get(1) || rectOne==orderedRectangles.get(2)){
								rectOne = mergedRect;
								rectTwo = orderedRectangles.get(0);
							} else {
								rectOne = orderedRectangles.get(0);
								rectTwo = mergedRect;
							}
						} 
						else {
							SmartDashboard.putString("More Vision State", "Saw Two Contours");			
						}
						
						//rect.x is the left edge as far as I can tell
						//rect.y is the top edge as far as I can tell
						centerXOne = rectOne.x + (rectOne.width/2); //returns the center of the bounding rectangle
						centerYOne = rectOne.y + (rectOne.height/2); //returns the center of the bounding rectangle
						centerXTwo = rectTwo.x + (rectTwo.width/2);
						centerYTwo = rectTwo.y + (rectTwo.height/2);
						double width=rectTwo.x-(rectOne.x+rectOne.width);
						double height=rectOne.y-(rectTwo.y+rectTwo.height);
						
						synchronized (imgLock){
							rectangleArea=width*height;
							centerYAvg = (centerYOne + centerYTwo)/2;
							centerXAvg = (centerXOne + centerXTwo)/2;
						}
						
						//scalar(int, int, int) is in BGR color space
						//the points are the two corners of the rectangle as far as I can tell
						Imgproc.rectangle(mat, new Point(rectOne.x, rectOne.y), new Point(rectTwo.x + rectTwo.width, rectTwo.y + rectTwo.height), new Scalar(0, 0, 255), 2); //draw rectangle of the detected object onto the image
						Imgproc.rectangle(mat, new Point(centerXAvg-3,centerYAvg-3), new Point(centerXAvg+3,centerYAvg+3), new Scalar(255, 0, 0), 5);
					
						SmartDashboard.putString("Vision State", "Executed overlay!");
					}
					else {
						SmartDashboard.putString("Vision State", "Did not find goal, found " + pipeline.filterContoursOutput().size() + " contours");
					}
				
					SmartDashboard.putNumber("Center X", centerXAvg);
					outputStream.putFrame(mat); //give stream (and CameraServer) a new frame
				} else {
					outputStream.putFrame(mat); //give stream (and CameraServer) a new frame
				}
			}
			
		});	
		visionThread.setDaemon(true);
		visionThread.start();
	}
	
	public double getArea(){
		synchronized(imgLock) {
			return rectangleArea;
		}
	}
	
	/*public void control() {
		if(OI.xBtnA){
			enableProcessing();
			
		}
		else if(OI.xBtnB){
			disableProcessing();
		}
	}*/
	
	public double getCenterY() {
		synchronized(imgLock) {
			return centerYAvg;
		}
	}

	public double getCenterX() {
		synchronized(imgLock) {
			return centerXAvg;
		}
	}

	public void disableProcessing() {
		runProcessing = false;
	}
	
	public void enableProcessing() {
		runProcessing = true;
	}
	
	public double DistanceBetweenCenters() {
		double distance = Math.sqrt((centerXOne - centerXTwo) * (centerXOne - centerXTwo) + (centerYOne - centerYTwo) * (centerYOne - centerYTwo));
		return distance;
	}
	
}
