package com.github.assisstion.RobotSimulator;

import java.awt.geom.Rectangle2D;

import com.github.assisstion.RobotSimulator.sensor.TouchSensor;


public class RobotProgramSample extends RobotProgram{

	protected TouchSensor sensor;

	public RobotProgramSample(){
		super(new Vector2(300, 300), 5.0f, 0, 0, 0);
		shapes.add(new Rectangle2D.Double(270, 100, 50, 50));
		sensor = new TouchSensor(this, new Vector2(
				getLeftWheelRelative().x / 2, getLeftWheelRelative().x / 2), 0.2f);
	}

	private static final long serialVersionUID = 346397218252074433L;

	@Override
	public void run(){

		/*
		 * Move until you reach a wall, then turn, and move for 100 pixels
		 */
		motor[motorA] = 100;
		motor[motorB] = 100;
		waitUntil(() -> SensorValue(sensor) == 1);
		motor[motorA] = -100;
		motor[motorB] = -100;
		wait1Msec(10);
		motor[motorA] = 10;
		motor[motorB] = -10;
		wait1Msec(393);
		motor[motorA] = 100;
		motor[motorB] = 100;
		wait1Msec(1000);
		motor[motorA] = 0;
		motor[motorB] = 0;

		/*
		float speed = 100;
		while(true){
			motor[motorA] = speed;
			motor[motorB] = speed - leftWheel.x * speed / 100;
			wait1Msec((int)(Math.PI * 200000 / speed));
			motor[motorA] = speed - leftWheel.x * speed / 100;
			motor[motorB] = speed;
			wait1Msec((int)(Math.PI * 200000 / speed));
			clearPoints();
		}
		 */
	}

	long lastSensorValue = -1;

	@Override
	public void updateMotion(long diff){
		super.updateMotion(diff);
		int i = SensorValue(sensor);
		if(i != lastSensorValue){
			System.out.println(i);
			lastSensorValue = i;
		}
	}

	public static void main(String[] args){
		new RobotProgramSample().startMain();
	}
}
