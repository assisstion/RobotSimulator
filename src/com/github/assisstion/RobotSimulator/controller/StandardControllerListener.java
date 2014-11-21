package com.github.assisstion.RobotSimulator.controller;

import com.github.assisstion.RobotSimulator.controller.RobotController.Button;
import com.github.assisstion.RobotSimulator.controller.RobotController.Joystick;
import com.github.assisstion.RobotSimulator.controller.RobotController.Trigger;

public interface StandardControllerListener{
	void buttonPressed(Button button);
	void buttonReleased(Button button);
	void triggerChanged(Trigger trigger, float newValue);
	void joystickChanged(Joystick joystick, boolean x, float newValue);
}
