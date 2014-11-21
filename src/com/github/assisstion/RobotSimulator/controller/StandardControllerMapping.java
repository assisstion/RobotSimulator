package com.github.assisstion.RobotSimulator.controller;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;

import com.github.assisstion.RobotSimulator.controller.RobotController.Button;
import com.github.assisstion.RobotSimulator.controller.RobotController.Joystick;
import com.github.assisstion.RobotSimulator.controller.RobotController.Trigger;

public class StandardControllerMapping implements ControllerMapping{

	@Override
	public Identifier getIDFromButton(Button button){
		Identifier id = null;
		switch(button){
			case A:
				id = Component.Identifier.Button._1;
				break;
			case B:
				id = Component.Identifier.Button._2;
				break;
			case BACK:
				id = Component.Identifier.Button._8;
				break;
			case LEFT_BUMPER:
				id = Component.Identifier.Button._4;
				break;
			case LEFT_JOYSTICK_BUTTON:
				id = Component.Identifier.Button._10;
				break;

			case RIGHT_BUMPER:
				id = Component.Identifier.Button._5;
				break;
			case RIGHT_JOYSTICK_BUTTON:
				id = Component.Identifier.Button._11;
				break;
			case START:
				id = Component.Identifier.Button._9;
				break;
			case X:
				id = Component.Identifier.Button._0;
				break;
			case Y:
				id = Component.Identifier.Button._3;
				break;
			default:
				break;
		}
		return id;
	}

	@Override
	public Identifier getIDFromTrigger(Trigger trigger){
		Identifier id = null;
		switch(trigger){
			case LEFT_TRIGGER:
				id = Component.Identifier.Button._6;
				break;
			case RIGHT_TRIGGER:
				id = Component.Identifier.Button._7;
				break;
			default:
				break;
		}
		return id;
	}

	@Override
	public Identifier getIDFromJoystickX(Joystick joystick){
		Identifier id = null;
		switch(joystick){
			case LEFT_JOYSTICK:
				id = Component.Identifier.Axis.X;
				break;
			case RIGHT_JOYSTICK:
				id = Component.Identifier.Axis.Z;
				break;
			default:
				break;
		}
		return id;
	}

	@Override
	public Identifier getIDFromJoystickY(Joystick joystick){
		Identifier id = null;
		switch(joystick){
			case LEFT_JOYSTICK:
				id = Component.Identifier.Axis.Y;
				break;
			case RIGHT_JOYSTICK:
				id = Component.Identifier.Axis.RZ;
				break;
			default:
				break;
		}
		return id;
	}

	@Override
	public Button getButtonFromID(Identifier id){
		Button button = null;
		if(id.equals(Component.Identifier.Button._1)){
			button = Button.A;
		}
		else if(id.equals(Component.Identifier.Button._2)){
			button = Button.B;
		}
		else if(id.equals(Component.Identifier.Button._8)){
			button = Button.BACK;
		}
		else if(id.equals(Component.Identifier.Button._4)){
			button = Button.LEFT_BUMPER;
		}
		else if(id.equals(Component.Identifier.Button._10)){
			button = Button.LEFT_JOYSTICK_BUTTON;
		}
		else if(id.equals(Component.Identifier.Button._5)){
			button = Button.RIGHT_BUMPER;
		}
		else if(id.equals(Component.Identifier.Button._11)){
			button = Button.RIGHT_JOYSTICK_BUTTON;
		}
		else if(id.equals(Component.Identifier.Button._9)){
			button = Button.START;
		}
		else if(id.equals(Component.Identifier.Button._0)){
			button = Button.X;
		}
		else if(id.equals(Component.Identifier.Button._1)){
			button = Button.Y;
		}
		return button;
	}

	@Override
	public Trigger getTriggerFromID(Identifier id){
		Trigger trigger = null;
		if(id.equals(Component.Identifier.Button._6)){
			trigger = Trigger.LEFT_TRIGGER;
		}
		else if(id.equals(Component.Identifier.Button._7)){
			trigger = Trigger.RIGHT_TRIGGER;
		}
		return trigger;
	}

	@Override
	public Joystick getJoystickFromID(Identifier id){
		Joystick joystick = null;
		if(id.equals(Component.Identifier.Axis.X) ||
				id.equals(Component.Identifier.Axis.Y)){
			joystick = Joystick.LEFT_JOYSTICK;
		}
		else if(id.equals(Component.Identifier.Axis.Z) ||
				id.equals(Component.Identifier.Axis.RZ)){
			joystick = Joystick.RIGHT_JOYSTICK;
		}
		return joystick;
	}

	@Override
	public boolean isJoystickX(Identifier id){
		if(id.equals(Component.Identifier.Axis.X) ||
				id.equals(Component.Identifier.Axis.Z)){
			return true;
		}
		else if(id.equals(Component.Identifier.Axis.Y) ||
				id.equals(Component.Identifier.Axis.RZ)){
			return false;
		}
		return false;
	}

	@Override
	public String getIDType(Identifier id){
		String type = "";
		if(id.equals(Component.Identifier.Button._1)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._2)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._8)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._4)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._10)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._5)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._11)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._9)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._0)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._1)){
			type = "button";
		}
		else if(id.equals(Component.Identifier.Button._6)){
			type = "trigger";
		}
		else if(id.equals(Component.Identifier.Button._7)){
			type = "trigger";
		}
		else if(id.equals(Component.Identifier.Axis.X)){
			type = "joystick";
		}
		else if(id.equals(Component.Identifier.Axis.Y)){
			type = "joystick";
		}
		else if(id.equals(Component.Identifier.Axis.Z)){
			type = "joystick";
		}
		else if(id.equals(Component.Identifier.Axis.RZ)){
			type = "joystick";
		}
		else if(id.equals(Component.Identifier.Axis.POV)){
			type = "pov";
		}
		return type;
	}

	@Override
	public boolean isPOVButton(Button button){
		switch(button){
			case DOWN:
				return true;
			case LEFT:
				return true;
			case RIGHT:
				return true;
			case UP:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean isPOVID(Identifier id){
		return id.equals(Component.Identifier.Axis.POV);
	}

	@Override
	public Button[] getPOVButton(float pov){
		if(pov == Component.POV.LEFT){
			return new Button[]{Button.LEFT};
		}
		else if(pov == Component.POV.UP_LEFT){
			return new Button[]{Button.UP, Button.LEFT};
		}
		else if(pov == Component.POV.UP){
			return new Button[]{Button.UP};
		}
		else if(pov == Component.POV.UP_RIGHT){
			return new Button[]{Button.UP, Button.RIGHT};
		}
		else if(pov == Component.POV.RIGHT){
			return new Button[]{Button.RIGHT};
		}
		else if(pov == Component.POV.DOWN_RIGHT){
			return new Button[]{Button.DOWN, Button.RIGHT};
		}
		else if(pov == Component.POV.DOWN){
			return new Button[]{Button.DOWN};
		}
		else if(pov == Component.POV.DOWN_LEFT){
			return new Button[]{Button.DOWN, Button.LEFT};
		}
		return new Button[]{};
	}

	@Override
	public float[] getButtonPOV(Button button){
		float pov = 0;
		float pov2 = 0;
		float pov3 = 0;
		switch(button){
			case DOWN:
				pov = Component.POV.DOWN;
				pov2 = Component.POV.DOWN_LEFT;
				pov3 = Component.POV.DOWN_RIGHT;
				break;
			case LEFT:
				pov = Component.POV.LEFT;
				pov2 = Component.POV.DOWN_LEFT;
				pov3 = Component.POV.UP_LEFT;
				break;
			case RIGHT:
				pov = Component.POV.RIGHT;
				pov2 = Component.POV.DOWN_RIGHT;
				pov3 = Component.POV.UP_RIGHT;
				break;
			case UP:
				pov = Component.POV.UP;
				pov2 = Component.POV.UP_LEFT;
				pov3 = Component.POV.UP_RIGHT;
				break;
			default:
				return new float[0];
		}
		return new float[]{pov, pov2, pov3};
	}

}
