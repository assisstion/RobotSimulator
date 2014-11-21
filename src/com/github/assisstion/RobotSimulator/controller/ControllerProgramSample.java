package com.github.assisstion.RobotSimulator.controller;

import net.java.games.input.Controller;

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
		Controller controller = getController();
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
		System.out.println("Button pressed: " + button.getName());
	}

	@Override
	public void buttonReleased(Button button){
		System.out.println("Button released: " + button.getName());
	}

	@Override
	public void triggerChanged(Trigger trigger, float newValue){
		if(newValue == 0){
			System.out.println("Trigger released: " + trigger.getName());
		}
		else{
			System.out.println("Trigger pressed: " + trigger.getName());
		}
	}

	@Override
	public void joystickChanged(Joystick joystick, boolean x, float newValue){
		System.out.println("Joystick " + (x?"x":"y") + " changed: "
				+ joystick.getName() + "; New value: " + newValue);
	}

	@Override
	public boolean isJoystickEnabled(){
		//Suppress joystick output
		return false;
	}
}
