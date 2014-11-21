package com.github.assisstion.RobotSimulator.controller;

public interface RobotController{

	/*
	 * Tasks to do:
	 * driving;
	 * armUp, armDown;
	 * sweeper;
	 * creeper;
	 * armMechanism;
	 */

	//Updates the stored values
	boolean poll();

	boolean getButton(Button button);

	/*
	 * Event listeners to be implemented
	public static void buttonDown(Button button){

	}

	public static void buttonUp(Button button){

	}
	 */

	//float from 0 to 1
	float getTrigger(Trigger trigger);

	//float from 0 to 1
	float getJoystickX(Joystick joystick);

	//float from 0 to 1
	float getJoystickY(Joystick joystick);

	public static enum Button{
		//
		UP, RIGHT, DOWN, LEFT,
		Y,
		B,
		A,
		X,
		LEFT_BUMPER, RIGHT_BUMPER,
		BACK,
		START,
		LEFT_JOYSTICK_BUTTON,
		RIGHT_JOYSTICK_BUTTON;
	}

	public static enum Trigger{
		// CONTROLLER BOTH: Rotate (left / right)
		LEFT_TRIGGER, RIGHT_TRIGGER;
	}

	public static enum Joystick{
		// CONTROLLER 1: Driving
		LEFT_JOYSTICK,
		// CONTROLLER 2: Arm
		RIGHT_JOYSTICK;
	}
}
