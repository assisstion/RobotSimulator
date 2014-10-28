package com.github.assisstion.RobotSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class RobotCanvas extends JPanel implements Runnable{

	protected Vector2 currentPoint = new Vector2(100, 100);
	//In radians; 0 is top, pi/2 is right
	protected float direction = (float)(Math.PI / 4);
	protected Set<Pair<Integer, Integer>> points = new HashSet<Pair<Integer, Integer>>();

	protected int pixelsPerSecond = 100;
	protected int updatesPerSecond = 200;
	protected int updatesPerPaint = 3;

	/**
	 *
	 */
	private static final long serialVersionUID = -9117978120156221878L;

	public RobotCanvas(){
		new Thread(this).start();
	}

	@Override
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, 10, 10);
		for(Pair<Integer, Integer> point : points){
			g2d.drawLine(point.getValueOne(), point.getValueTwo(),
					point.getValueOne(), point.getValueTwo());
		}
		g2d.setColor(Color.RED);
		g2d.fillOval((int) currentPoint.x - 2, (int) currentPoint.y - 2,
				4, 4);
	}

	//protected float counter = 10000;

	public void updateMotion(){
		direction += 0.01 * 100 / updatesPerSecond;
		float speed = (float) pixelsPerSecond / updatesPerSecond;
		currentPoint.y += -Math.cos(direction) * speed;
		currentPoint.x += Math.sin(direction) * speed;
		points.add(Pair.make((int)currentPoint.x, (int)currentPoint.y));
	}

	protected int paintCounter = 0;

	@Override
	public void run(){
		while(true){
			try{
				paintCounter--;
				if(paintCounter <= 0){
					repaint();
					paintCounter = updatesPerPaint;
				}
				Thread.sleep(1000 / updatesPerSecond);
			}
			catch(InterruptedException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			updateMotion();
		}
	}
}
