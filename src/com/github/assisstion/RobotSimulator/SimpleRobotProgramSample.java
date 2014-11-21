package com.github.assisstion.RobotSimulator;

public class SimpleRobotProgramSample extends RobotProgram{

	private static final long serialVersionUID = -9209441905064152827L;

	public SimpleRobotProgramSample(){
		super(5.0f, true);
	}

	@Override
	public void run(){
		//Draws a figure 8
		float speed = 100f;
		while(true){
			motor[motorB] = speed;
			motor[motorC] = speed - leftWheel.x * speed / 100;
			wait1Msec((int)(Math.PI * 200000 / speed));
			motor[motorB] = speed - leftWheel.x * speed / 100;
			motor[motorC] = speed;
			wait1Msec((int)(Math.PI * 200000 / speed));
			resetRobot();
		}
	}

	public static void main(String[] args){
		new SimpleRobotProgramSample().init();
	}

}
