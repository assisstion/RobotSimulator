package com.github.assisstion.RobotSimulator;

import java.awt.EventQueue;
import java.awt.Shape;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;


public abstract class RobotProgram extends RobotCanvas implements RobotProgrammingBase, Runnable{

	private static final long serialVersionUID = 7745541041274709141L;
	public static final int width = 640;
	public static final int height = 640;
	public static final int DEFAULT_X = 300;
	public static final int DEFAULT_Y = 300;
	public static final float DEFAULT_DIRECTION = 0;

	/*public RobotProgram(){
		super();
	}*/

	public RobotProgram(float diameter, boolean rect){
		this(new Vector2(DEFAULT_X, DEFAULT_Y), diameter, DEFAULT_DIRECTION, rect);
	}

	public RobotProgram(Vector2 location, float diameter, float direction, boolean rect){
		this(location, diameter, diameter/2, diameter/2, direction, 0, 0, rect);
	}

	public RobotProgram(Vector2 rightWheelStart, float wheelDistance, double aboveY,
			double belowY, float direction
			, float initialLeftSpeed, float initialRightSpeed, boolean rect){
		super(rightWheelStart, wheelDistance, aboveY, belowY,
				direction, initialLeftSpeed, initialRightSpeed, rect);
	}

	@Override
	public Map<Shape, Boolean> getShapes(){
		return shapes;
	}

	@Override
	public double[] getMotors(){
		return motor;
	}

	public void waitUntil(Supplier<Boolean> condition){
		Object o = getWaitLock(condition);
		while(!condition.get()){
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
}
