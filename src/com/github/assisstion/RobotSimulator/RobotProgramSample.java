package com.github.assisstion.RobotSimulator;

public class RobotProgramSample extends RobotProgram{

	public RobotProgramSample(){
		super(new Vector2(300, 300), 5.0f, 0, 0, 0);
	}

	private static final long serialVersionUID = 346397218252074433L;

	@Override
	public void run(){

		/*
		 * Simple sample code for figure 8:
		 * Given 2 motors 5 units apart,
		 * Moving at (speed) units per second

		while(true){
			motor[motorA] = 100;
			motor[motorB] = 95;
			wait1Msec(6283);
			motor[motorA] = 95;
			motor[motorB] = 100;
			wait1Msec(6283);
		}

		 */

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
	}

	public static void main(String[] args){
		new RobotProgramSample().startMain();
	}
}
