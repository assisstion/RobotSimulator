package com.github.assisstion.RobotSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import com.github.assisstion.RobotSimulator.sensor.TouchSensor;


public class RobotProgramSample extends RobotProgram{

	protected TouchSensor sensor;
	protected ShapeEntity ball;

	public static final double ROBOT_WIDTH = 20.0;

	private static int a = 10;
	private static int b = 10;

	public RobotProgramSample(){
		super(new Vector2(300, 300), ROBOT_WIDTH, a, b, 0, 0, 0, true, false);
		shapes.add(new ShapeEntity(new Rectangle2D.Double(267, 100, 50, 50), true));
		ball = new ShapeEntity(new Ellipse2D.Double(400, 150, ROBOT_WIDTH, ROBOT_WIDTH)
		, Color.BLACK, false);
		shapes.add(ball);
		sensor = new TouchSensor(this, new Vector2(
				getLeftWheelRelative().x / 2, a), 0.0001 * getLeftWheelRelative().x);
	}

	private static final long serialVersionUID = 346397218252074433L;

	@Override
	public void run(){

		/*
		 * Move until you reach a wall, then turn, and move for 100 pixels
		 * Then move back, turn back, and move into the wall again
		 */

		motor[motorB] = 100;
		motor[motorC] = 100;
		waitUntil(() -> SensorValue(sensor) > 0);
		wait1Msec(500);
		motor[motorB] = -100;
		motor[motorC] = -100;
		wait1Msec((int)((Math.sqrt(ROBOT_WIDTH*ROBOT_WIDTH+4*a*a)/2-a)*10) + 1);
		motor[motorB] = 10;
		motor[motorC] = -10;
		wait1Msec((int) (getLeftWheelRelative().x * Math.PI * 100 / 4) + 1);
		motor[motorB] = 100;
		motor[motorC] = 100;
		waitUntil(() -> SensorValue(sensor) < 0);
		//wait1Msec(1000);
		motor[motorA] = 50;
		motor[motorB] = 0;
		motor[motorC] = 0;
		wait1Msec(500);
		shapes.remove(ball);
		motor[motorA] = -50;
		wait1Msec(500);
		motor[motorA] = 0;
		motor[motorB] = -100;
		motor[motorC] = -100;
		wait1Msec(1000);
		motor[motorB] = -10;
		motor[motorC] = 10;
		wait1Msec((int) (getLeftWheelRelative().x * Math.PI * 100 / 4) + 1);
		motor[motorB] = 100;
		motor[motorC] = 100;
		wait1Msec((int)((Math.sqrt(ROBOT_WIDTH*ROBOT_WIDTH+4*a*a)/2-a)*10) + 1);
		motor[motorB] = 0;
		motor[motorC] = 0;
		ball = new ShapeEntity(new Ellipse2D.Double(300-ROBOT_WIDTH,
				150-ROBOT_WIDTH, ROBOT_WIDTH, ROBOT_WIDTH), Color.BLACK, false);
		shapes.add(ball);
	}

	protected long lastSensorValue = -1;

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
		new RobotProgramSample().init();
	}

	@Override
	public void overlay(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.RED);
		double width = leftWheel.x;
		g2d.drawString("WIDTH: " + width + "cm (pixels)", 100, 100);
		g2d.drawLine(100, 125, 100, 135);
		g2d.drawLine(200, 125, 200, 135);
		g2d.drawLine(100, 130, 200, 130);
		g2d.drawString("1m", 135, 140);
	}
}
