package com.github.assisstion.RobotSimulator.sensor;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.Map;

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
		this.relativeLocation = relativeLocation;
		range = d;
	}

	//1 for colliding, 2 for "on a shape"
	@Override
	public int sensorValue(){
		Vector2 sensorVector = RobotCanvas.relativeVector(program.getRightWheel(),
				relativeLocation, program.getDirection());
		Ellipse2D.Double ellipse = new Ellipse2D.Double(
				sensorVector.x - range/2, sensorVector.y - range/2,
				range, range);
		for(Map.Entry<ShapeEntity, Boolean> shape : program.getShapes().entrySet()){
			Area ellipseArea = new Area(ellipse);
			ellipseArea.intersect(new Area(shape.getKey().get()));
			if(!ellipseArea.isEmpty()){
				if(shape.getValue()){
					return shape.getKey().getID();
				}
				else{
					return -shape.getKey().getID();
				}
			}
		}
		return 0;
	}

}
