package com.github.assisstion.RobotSimulator;

public class RobotProgramSample extends RobotProgram{

	public RobotProgramSample(){
		super(new Vector2(300, 300), 5.0f, 0, 0, 0);
	}

	private static final long serialVersionUID = 346397218252074433L;

	@Override
	public void run(){
		// TODO Auto-generated method stub
		while(true){
			motor[motorA] = 500;
			motor[motorB] = 475;
			wait1Msec(1257);
			motor[motorA] = 475;
			motor[motorB] = 500;
			wait1Msec(1257);
			clearPoints();
		}
	}

	public static void main(String[] args){
		new RobotProgramSample().startMain();
	}
}
