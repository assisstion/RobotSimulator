package com.github.assisstion.RobotSimulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class StandardRobotController implements RobotController{

	protected Map<Component.Identifier, Float> lastMapping = new HashMap<Component.Identifier, Float>();
	protected ControllerMapping mapping;
	protected Controller controller;
	//Listeners only fire on poll()
	protected List<StandardControllerListener> listeners = new ArrayList<StandardControllerListener>();
	protected ExecutorService tpe;

	public StandardRobotController(Controller input){
		this(input, new StandardControllerMapping());
	}

	public StandardRobotController(Controller input, ControllerMapping mapping){
		tpe = new ThreadPoolExecutor(4, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(1024));
		controller = input;
		this.mapping = mapping;
		controller.poll();
		for(Component c : controller.getComponents()){
			lastMapping.put(c.getIdentifier(), c.getPollData());
		}
	}

	@Override
	public boolean poll(){
		boolean b = controller.poll();
		for(Component c : controller.getComponents()){
			Component.Identifier id = c.getIdentifier();
			float pd = c.getPollData();
			if(!lastMapping.containsKey(id)){
				lastMapping.put(id, pd);
			}
			else{
				float last = lastMapping.get(id);
				if(last != pd){
					lastMapping.put(id, pd);
					for(StandardControllerListener scl : listeners){
						dispatch(scl, id, last, pd);
					}
				}
			}
		}
		return b;
	}

	public class ControllerEventDispatcher implements Runnable{

		protected StandardControllerListener c;
		protected Component.Identifier id;
		protected float pd;
		protected float last;

		public ControllerEventDispatcher(StandardControllerListener c,
				Component.Identifier id, float last, float pd){
			this.c = c;
			this.id = id;
			this.last = last;
			this.pd = pd;
		}

		@Override
		public void run(){
			switch(mapping.getIDType(id)){
				case "button":
					if(pd == 0.0f){
						c.buttonReleased(mapping.getButtonFromID(id));
					}
					else{
						c.buttonPressed(mapping.getButtonFromID(id));
					}
					break;
				case "trigger":
					c.triggerChanged(mapping.getTriggerFromID(id), pd);
					break;
				case "joystick":
					c.joystickChanged(mapping.getJoystickFromID(id),
							mapping.isJoystickX(id), pd);
					break;
				case "pov":
					Button[] ba = mapping.getPOVButton(last);
					Button[] ba2 = mapping.getPOVButton(pd);
					List<Button> released = new ArrayList<Button>();
					outer: for(Button b : ba){
						for(Button b2 : ba2){
							if(b.equals(b2)){
								continue outer;
							}
						}
						released.add(b);
					}
					List<Button> pressed = new ArrayList<Button>();
					outer: for(Button b : ba2){
						for(Button b2 : ba){
							if(b.equals(b2)){
								continue outer;
							}
						}
						pressed.add(b);
					}
					for(Button b : released){
						c.buttonReleased(b);
					}
					for(Button b : pressed){
						c.buttonPressed(b);
					}
					break;
			}
		}
	}

	protected void dispatch(StandardControllerListener c, Component.Identifier id, float old, float pd){
		tpe.execute(new ControllerEventDispatcher(c, id, old ,pd));
	}

	public void addControllerListener(StandardControllerListener cl){
		listeners.add(cl);
	}

	public void removeControllerListener(StandardControllerListener cl){
		listeners.remove(cl);
	}

	public void clearControllerListeners(){
		listeners.clear();
	}

	@Override
	public boolean getButton(Button button){
		if(mapping.isPOVButton(button)){
			float[] pov = mapping.getButtonPOV(button);
			float data = controller.getComponent(Component.Identifier.Axis.POV)
					.getPollData();
			return data == pov[0] || data == pov[1] || data == pov[2];
		}
		else{
			Component.Identifier id = mapping.getIDFromButton(button);
			if(id == null){
				throw new IllegalStateException("no controller state");
			}
			return controller.getComponent(id).getPollData() != 0.0f;
		}
	}

	@Override
	public float getTrigger(Trigger trigger){
		Component.Identifier id = mapping.getIDFromTrigger(trigger);
		if(id == null){
			throw new IllegalStateException("no controller state");
		}
		return controller.getComponent(id).getPollData();
	}

	@Override
	public float getJoystickX(Joystick joystick){
		Component.Identifier id = mapping.getIDFromJoystickX(joystick);
		if(id == null){
			throw new IllegalStateException("no controller state");
		}
		return controller.getComponent(id).getPollData();
	}

	@Override
	public float getJoystickY(Joystick joystick){
		Component.Identifier id = mapping.getIDFromJoystickY(joystick);
		if(id == null){
			throw new IllegalStateException("no controller state");
		}
		return controller.getComponent(id).getPollData();
	}

}
