package com.github.assisstion.RobotSimulator.sensor;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import com.github.assisstion.RobotSimulator.RobotCanvas;
import com.github.assisstion.RobotSimulator.RobotProgram;
import com.github.assisstion.RobotSimulator.Vector2;

public class TouchSensor implements Sensor{

	protected RobotProgram program;
	protected Vector2 relativeLocation;
	protected double range;

	public TouchSensor(RobotProgram program, Vector2 relativeLocation, double d){
		this.program = program;
		this.relativeLocation = relativeLocation;
		range = d;
	}

	@Override
	public int sensorValue(){
		Vector2 sensorVector = RobotCanvas.relativeVector(program.getRightWheel(),
				relativeLocation, program.getDirection());
		Ellipse2D.Double ellipse = new Ellipse2D.Double(
				sensorVector.x - range/2, sensorVector.y - range/2,
				range, range);
		for(Shape shape : program.getShapes()){
			Area ellipseArea = new Area(ellipse);
			ellipseArea.intersect(new Area(shape));
			if(!ellipseArea.isEmpty()){
				return 1;
			}
		}
		return 0;
	}

}
