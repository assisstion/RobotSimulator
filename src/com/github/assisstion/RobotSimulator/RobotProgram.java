package com.github.assisstion.RobotSimulator;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.Set;
import java.util.function.BooleanSupplier;

import javax.swing.JFrame;


public abstract class RobotProgram extends RobotCanvas implements RobotProgrammingBase, Runnable{

	private static final long serialVersionUID = 7745541041274709141L;
	public static final int width = 640;
	public static final int height = 640;
	public static final int DEFAULT_X = 300;
	public static final int DEFAULT_Y = 300;
	public static final double DEFAULT_DIRECTION = 0;

	/*public RobotProgram(){
		super();
	}*/

	public RobotProgram(double diameter, boolean rect, boolean enableController){
		this(new Vector2(DEFAULT_X, DEFAULT_Y), diameter,
				DEFAULT_DIRECTION, rect, enableController);
	}

	public RobotProgram(Vector2 location, double diameter, double direction,
			boolean rect, boolean enableController){
		this(location, diameter, diameter/2, diameter/2,
				direction, 0, 0, rect, enableController);
	}

	public RobotProgram(Vector2 rightWheelStart, double wheelDistance, double aboveY,
			double belowY, double direction, double initialLeftSpeed,
			double initialRightSpeed, boolean rect, boolean enableController){
		super(rightWheelStart, wheelDistance, aboveY, belowY, direction,
				initialLeftSpeed, initialRightSpeed, rect, enableController);
	}

	@Override
	public Set<ShapeEntity> getShapes(){
		return shapes;
	}

	@Override
	public double[] getMotors(){
		return motor;
	}

	public void waitUntil(BooleanSupplier condition){
		Object o = getWaitLock(condition);
		while(!condition.getAsBoolean()){
			synchronized(o){
				try{
					o.wait();
				}
				catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void wait1Msec(long millis){
		long currentFrame = getFrameCounter() / 1000000;
		long target = currentFrame + millis;
		Object o = getFrameCounterLock(target);
		while(getFrameCounter() / 1000000 < target){
			synchronized(o){
				try{
					o.wait();
				}
				catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void init(){
		start();
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setBounds(100, 100, width, height);
				frame.setContentPane(RobotProgram.this);
				frame.addKeyListener(RobotProgram.this);
				frame.setTitle("Robot Simulator");
				frame.setVisible(true);
				repaint();
				new Thread(new RobotStarter()).start();
			}
		});
	}

	protected class RobotStarter implements Runnable{
		@Override
		public void run(){
			waitUntil(() -> getPaints() > getUpdatesPerSecond() /
					getUpdatesPerPaint() / 10 + 1);
			new Thread(RobotProgram.this).start();
		}
	}

	@Override
	public abstract void overlay(Graphics g);
}
