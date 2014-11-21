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

	//float from 0 to 1
	float getTrigger(Trigger trigger);

	//float from 0 to 1
	float getJoystickX(Joystick joystick);

	//float from 0 to 1
	float getJoystickY(Joystick joystick);

	public static enum Button{
		//
		UP("up"), RIGHT("right"), DOWN("down"), LEFT("left"),
		Y("y"),
		B("b"),
		A("a"),
		X("x"),
		LEFT_BUMPER("left bumper"), RIGHT_BUMPER("right bumper"),
		BACK("back"),
		START("start"),
		LEFT_JOYSTICK_BUTTON("left joystick button"),
		RIGHT_JOYSTICK_BUTTON("right joystick button");

		String name;

		Button(String name){
			this.name = name;
		}

		public String getName(){
			return name;
		}
	}

	public static enum Trigger{
		// CONTROLLER BOTH: Rotate (left / right)
		LEFT_TRIGGER("left trigger"), RIGHT_TRIGGER("right trigger");

		String name;

		Trigger(String name){
			this.name = name;
		}

		public String getName(){
			return name;
		}
	}

	public static enum Joystick{
		// CONTROLLER 1: Driving
		LEFT_JOYSTICK("left joystick"),
		// CONTROLLER 2: Arm
		RIGHT_JOYSTICK("right joystick");

		String name;

		Joystick(String name){
			this.name = name;
		}

		public String getName(){
			return name;
		}
	}
}
