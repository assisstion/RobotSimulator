package com.github.assisstion.RobotSimulator;

import java.awt.EventQueue;
import java.awt.Shape;
import java.util.Set;
import java.util.function.Supplier;

import javax.swing.JFrame;


public abstract class RobotProgram extends RobotCanvas implements RobotProgrammingBase, Runnable{

	private static final long serialVersionUID = 7745541041274709141L;
	public static final int width = 640;
	public static final int height = 640;

	/*public RobotProgram(){
		super();
	}*/

	public RobotProgram(Vector2 rightWheelStart, float wheelDistance, float direction
			, float initialLeftSpeed, float initialRightSpeed ){
		super(rightWheelStart, wheelDistance, direction, initialLeftSpeed, initialRightSpeed);
	}

	@Override
	public Set<Shape> getShapes(){
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
		long currentFrame = frameCounter / 1000000;
		long target = currentFrame + millis;
		Object o = getFrameCounterLock(target);
		while(frameCounter / 1000000 < target){
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

	public void startMain(){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setBounds(100, 100, width, height);
				frame.setContentPane(RobotProgram.this);
				frame.addKeyListener(RobotProgram.this);
				frame.setVisible(true);
				repaint();
				startProgram();
			}
		});
	}

	public void startProgram(){
		new Thread(this).start();
	}
}
