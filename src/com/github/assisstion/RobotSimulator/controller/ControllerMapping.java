package com.github.assisstion.RobotSimulator.controller;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;

import com.github.assisstion.RobotSimulator.controller.RobotController.Button;
import com.github.assisstion.RobotSimulator.controller.RobotController.Joystick;
import com.github.assisstion.RobotSimulator.controller.RobotController.Trigger;

public interface ControllerMapping{
	Component.Identifier getIDFromButton(Button button);
	Component.Identifier getIDFromTrigger(Trigger trigger);
	Component.Identifier getIDFromJoystickX(Joystick joystick);
	Component.Identifier getIDFromJoystickY(Joystick joystick);
	Button getButtonFromID(Component.Identifier id);
	Trigger getTriggerFromID(Component.Identifier id);
	Joystick getJoystickFromID(Component.Identifier id);
	boolean isJoystickX(Component.Identifier id);
	String getIDType(Component.Identifier id);
	boolean isPOVButton(Button button);
	float[] getButtonPOV(Button button);
	Button[] getPOVButton(float pov);
	boolean isPOVID(Identifier id);
}
