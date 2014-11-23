package com.github.assisstion.RobotSimulator.sensor;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import com.github.assisstion.RobotSimulator.RobotCanvas;
import com.github.assisstion.RobotSimulator.RobotProgram;
import com.github.assisstion.RobotSimulator.ShapeEntity;
import com.github.assisstion.RobotSimulator.Vector2;

public class TouchSensor implements Sensor{

	protected RobotProgram program;
	protected Vector2 relativeLocation;
	protected double range;

	public TouchSensor(RobotProgram program, Vector2 relativeLocation, double d){
		this.program = program;
		this.relativeLocation = new Vector2(relativeLocation);
		range = d;
	}

	//positive for colliding, negative for "on a shape"
	@Override
	public int sensorValue(){
		Vector2 sensorVector = RobotCanvas.relativeVector(program.getRightWheel(),
				relativeLocation, program.getDirection());
		Ellipse2D.Double ellipse = new Ellipse2D.Double(
				sensorVector.x - range/2, sensorVector.y - range/2,
				range, range);
		for(ShapeEntity shape : program.getShapes()){
			Area ellipseArea = new Area(ellipse);
			ellipseArea.intersect(new Area(shape.get()));
			if(!ellipseArea.isEmpty()){
				if(shape.isColliding()){
					return shape.getID();
				}
				else{
					return -shape.getID();
				}
			}
		}
		return 0;
	}

}
