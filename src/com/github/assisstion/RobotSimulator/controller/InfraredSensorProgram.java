package com.github.assisstion.RobotSimulator.controller;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import com.github.assisstion.RobotSimulator.RobotProgram;
import com.github.assisstion.RobotSimulator.ShapeEntity;
import com.github.assisstion.RobotSimulator.Vector2;
import com.github.assisstion.RobotSimulator.sensor.InfraredSensor;

public class InfraredSensorProgram extends RobotProgram{

	private static final long serialVersionUID = -7377548853375157054L;
	protected InfraredSensor irs;

	public InfraredSensorProgram(){
		super(5, true, true);
		irs = new InfraredSensor(
				this, new Vector2(350, 250), -Math.PI/2,
				new Vector2(2.5, 0), 10000, 0.01, 1);
		ShapeEntity se = new ShapeEntity(new Rectangle2D.Double(345, 245, 10, 10), false);
		shapes.add(se);
	}

	@Override
	public void run(){
		motor[motorB] = 10;
		motor[motorC] = -10;
		wait1Msec(392);
		motor[motorB] = 100;
		motor[motorC] = 100;
		wait1Msec(750);
		motor[motorB] = -10;
		motor[motorC] = 10;
		wait1Msec(392);
		motor[motorB] = 100;
		motor[motorC] = 100;
		wait1Msec(250);
		motor[motorB] = -10;
		motor[motorC] = 10;
		wait1Msec(392);
		motor[motorB] = 100;
		motor[motorC] = 100;
		wait1Msec(250);
		motor[motorB] = 120;
		motor[motorC] = 100;
		wait1Msec(393);
		motor[motorB] = 10;
		motor[motorC] = -10;
		wait1Msec(392);
		motor[motorB] = 100;
		motor[motorC] = 100;
		wait1Msec(250);
		motor[motorB] = 0;
		motor[motorC] = 0;
	}

	@Override
	public void overlay(Graphics g){

	}

	public static void main(String[] args){
		new InfraredSensorProgram().init();
	}

}
