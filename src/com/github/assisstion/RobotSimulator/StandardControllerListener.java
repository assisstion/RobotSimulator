package com.github.assisstion.RobotSimulator;

import com.github.assisstion.RobotSimulator.RobotController.Button;
import com.github.assisstion.RobotSimulator.RobotController.Joystick;
import com.github.assisstion.RobotSimulator.RobotController.Trigger;

public interface StandardControllerListener{
	void buttonPressed(Button button);
	void buttonReleased(Button button);
	void triggerChanged(Trigger trigger, float newValue);
	void joystickChanged(Joystick joystick, boolean x, float newValue);
}
