package com.github.assisstion.RobotSimulator.controller;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.java.games.input.Component;
import net.java.games.input.Controller;

import com.github.assisstion.RobotSimulator.RobotProgram;
import com.github.assisstion.RobotSimulator.ShapeEntity;
import com.github.assisstion.RobotSimulator.Vector2;
import com.github.assisstion.RobotSimulator.controller.RobotController.Button;
import com.github.assisstion.RobotSimulator.controller.RobotController.Joystick;
import com.github.assisstion.RobotSimulator.controller.RobotController.Trigger;
import com.github.assisstion.RobotSimulator.sensor.CompositeSensor;
import com.github.assisstion.RobotSimulator.sensor.Sensor;
import com.github.assisstion.RobotSimulator.sensor.TouchSensor;

public class ControllerProgramSample extends RobotProgram implements StandardControllerListener{

	private static final long serialVersionUID = 8368673221320981088L;
	private static final float ROBOT_SIDE = 10.0f;
	protected int columns = 4;
	protected int rows = 4;
	protected ShapeEntity[][] shapeEntities = new ShapeEntity[columns][rows];
	protected Random random;
	private static final Color EMPTY = Color.GRAY;
	private static final Color TARGET = Color.RED;
	protected Sensor sensor;
	protected long lastFrameNum = 0;
	protected float speed = 100f / getUpdatesPerSecond();
	protected float triggerMod = 2.0f;
	protected StandardRobotController src;
	protected int score = 0;
	protected int scorePerClear = 10;

	public ControllerProgramSample(){
		super(ROBOT_SIDE, true);
		random = new Random();
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
				Shape s = new Rectangle2D.Double(50 * (i+4), 50 * (j+4), 25, 25);
				ShapeEntity se = new ShapeEntity(s, i + j * columns + 1, EMPTY);
				shapeEntities[i][j] = se;
				shapes.put(se, false);
			}
		}
		List<TouchSensor> list = new ArrayList<TouchSensor>();
		list.add(new TouchSensor(this, new Vector2(
				ROBOT_SIDE, ROBOT_SIDE/2), 0.001 * ROBOT_SIDE / 5));
		list.add(new TouchSensor(this, new Vector2(
				ROBOT_SIDE, -ROBOT_SIDE/2), 0.001 * ROBOT_SIDE / 5));
		list.add(new TouchSensor(this, new Vector2(
				0, ROBOT_SIDE/2), 0.001 * ROBOT_SIDE / 5));
		list.add(new TouchSensor(this, new Vector2(
				0, -ROBOT_SIDE/2), 0.001 * ROBOT_SIDE / 5));
		sensor = new CompositeSensor<Sensor>(list);
	}

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
		int restrict = setRandomTarget(-1);
		while(true){
			waitUntil(() -> getUpdates() > lastFrameNum);
			src.poll();
			lastFrameNum = getUpdates();
			float fx = src.getJoystickX(Joystick.LEFT_JOYSTICK);
			float fy = src.getJoystickY(Joystick.LEFT_JOYSTICK);
			float speedMod = 1f;
			speedMod *= src.getTrigger(Trigger.LEFT_TRIGGER) == 1.0f ? 1.0f / triggerMod : 1.0f;
			speedMod *= src.getTrigger(Trigger.RIGHT_TRIGGER) == 1.0f ? triggerMod : 1.0f;
			boolean first = true;
			int counter = 0;
			do{
				if(!first){
					rightWheel.x -= round(fx, 2) * speed * speedMod;
					rightWheel.y -= round(fy, 2) * speed * speedMod;
					speedMod /= 2;
					if(counter++ >= subCollisionIterations){
						break;
					}
				}
				first = false;
				rightWheel.x += round(fx, 2) * speed * speedMod;
				rightWheel.y += round(fy, 2) * speed * speedMod;
			} while(collision());
			if(SensorValue(sensor) == restrict + 1){
				if(++score % scorePerClear == 0){
					clearPoints();
				}
				ShapeEntity se = shapeEntities[restrict%4][restrict/4];
				shapes.remove(se);
				shapes.put(new ShapeEntity(se.get(), restrict + 1, EMPTY), false);
				restrict = setRandomTarget(restrict);
			}
		}
	}

	public int setRandomTarget(int restrict){
		int i;
		int j;
		int n;
		do{
			i = random.nextInt(columns);
			j = random.nextInt(rows);
			n = i + j * columns;
		} while(n == restrict);
		ShapeEntity se = shapeEntities[i][j];
		shapes.remove(se);
		shapes.put(new ShapeEntity(se.get(), n + 1, TARGET), true);
		return n;
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

	@Override
	public void overlay(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		Controller controller = getController();
		if(controller != null){
			int i = 0;
			for(Component c : controller.getComponents()){
				g2d.drawString(c.getName() + ": " + c.getPollData(), 10, 20 + i++ * 20);
			}
		}
		g2d.drawString("Score: " + score, width - 100, 20);
	}
}
