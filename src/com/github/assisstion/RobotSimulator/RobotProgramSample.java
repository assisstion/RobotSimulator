package com.github.assisstion.RobotSimulator;

public class RobotProgramSample extends RobotProgram{

	public RobotProgramSample(){
		super(new Vector2(300, 300), 5.0f, 0, 0, 0);
	}

	private static final long serialVersionUID = 346397218252074433L;

	@Override
	public void run(){
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
