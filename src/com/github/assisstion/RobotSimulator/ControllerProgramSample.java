package com.github.assisstion.RobotSimulator;

import net.java.games.input.Component;

public class ControllerProgramSample extends RobotProgram{

	private static final long serialVersionUID = 8368673221320981088L;
	private static final float ROBOT_SIDE = 10.0f;

	public ControllerProgramSample(){
		super(ROBOT_SIDE, true);
	}

	protected long lastFrameNum = 0;
	protected float speed = 0.1f;

	@Override
	public void run(){
		//Must have controller to test
		if(controller == null){
			return;
		}
		while(true){
			waitUntil(() ->  updates > lastFrameNum);
			lastFrameNum = updates;
			Component x = controller.getComponent(Component.Identifier.Axis.X);
			float fx = x.getPollData();
			Component y = controller.getComponent(Component.Identifier.Axis.Y);
			float fy = y.getPollData();
			rightWheel.x += round(fx, 2) * speed;
			rightWheel.y += round(fy, 2) * speed;

			/*if(attached.getComponent(Component.Identifier.Button._1).getPollData() == 1.0f){
				System.out.println("hahaha");
			}*/
		}
	}

	public float round(float f, int digits){
		double d = Math.pow(10, digits);
		int i = (int)(f * d);
		return (float)(i / d);
	}

	public static void main(String[] args){
		new ControllerProgramSample().init();
	}
}
