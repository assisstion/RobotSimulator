package com.github.assisstion.RobotSimulator.controller;

import com.github.assisstion.RobotSimulator.RobotProgram;
import com.github.assisstion.RobotSimulator.controller.RobotController.Button;
import com.github.assisstion.RobotSimulator.controller.RobotController.Joystick;
import com.github.assisstion.RobotSimulator.controller.RobotController.Trigger;

public class ControllerProgramSample extends RobotProgram implements StandardControllerListener{

	private static final long serialVersionUID = 8368673221320981088L;
	private static final float ROBOT_SIDE = 10.0f;

	public ControllerProgramSample(){
		super(ROBOT_SIDE, true);
	}

	protected long lastFrameNum = 0;
	protected float speed = 100f / getUpdatesPerSecond();
	protected StandardRobotController src;

	@Override
	public void run(){
		//Must have controller to test
		if(controller == null){
			return;
		}
		autoControllerPolling = false;
		src = new StandardRobotController(controller);
		src.addControllerListener(this);
		while(true){
			waitUntil(() -> getUpdates() > lastFrameNum);
			src.poll();
			lastFrameNum = getUpdates();
			float fx = src.getJoystickX(Joystick.LEFT_JOYSTICK);
			float fy = src.getJoystickY(Joystick.LEFT_JOYSTICK);
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

	@Override
	public void buttonPressed(Button button){
		if(button.equals(Button.A)){
			System.out.println("a");
		}
	}

	@Override
	public void buttonReleased(Button button){
		if(button.equals(Button.A)){
			System.out.println("a!");
		}
	}

	@Override
	public void triggerChanged(Trigger trigger, float newValue){
		if(trigger.equals(Trigger.LEFT_TRIGGER)){
			if(newValue == 0){
				System.out.println("r!");
			}
			else{
				System.out.println("r");
			}
		}
	}

	@Override
	public void joystickChanged(Joystick joystick, boolean x, float newValue){
		if(joystick.equals(Joystick.LEFT_JOYSTICK)){
			System.out.println("move");
		}
	}
}
