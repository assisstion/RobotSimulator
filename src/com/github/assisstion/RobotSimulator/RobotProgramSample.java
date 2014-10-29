package com.github.assisstion.RobotSimulator;

import java.awt.geom.Rectangle2D;

import com.github.assisstion.RobotSimulator.sensor.TouchSensor;


public class RobotProgramSample extends RobotProgram{

	protected TouchSensor sensor;

	public static final float ROBOT_WIDTH = 20.1f;

	public RobotProgramSample(){
		super(new Vector2(300, 300), ROBOT_WIDTH, 0, 0, 0);
		shapes.add(new Rectangle2D.Double(270, 100, 50, 50));
		sensor = new TouchSensor(this, new Vector2(
				getLeftWheelRelative().x / 2, getLeftWheelRelative().x / 2), 0.2 * getLeftWheelRelative().x / 5);
	}

	private static final long serialVersionUID = 346397218252074433L;

	@Override
	public void run(){

		/*
		 * Move until you reach a wall, then turn, and move for 100 pixels
		 */
		motor[motorB] = 100;
		motor[motorC] = 100;
		waitUntil(() -> SensorValue(sensor) == 1);
		wait1Msec(500);
		motor[motorB] = -20;
		motor[motorC] = -20;
		wait1Msec(200);
		motor[motorB] = 10;
		motor[motorC] = -10;
		wait1Msec((int) getLeftWheelRelative().x * 393 / 5);
		motor[motorB] = 100;
		motor[motorC] = 100;
		wait1Msec(1000);
		motor[motorB] = 0;
		motor[motorC] = 0;

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
