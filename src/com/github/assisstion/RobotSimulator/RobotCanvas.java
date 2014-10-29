package com.github.assisstion.RobotSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.JPanel;

public class RobotCanvas extends JPanel implements Printable, KeyListener{

	private NavigableMap<Long, Object> frameCounterLocks =
			new TreeMap<Long, Object>();

	//5.0f
	protected double wheelDistance = 5.0f;
	protected Vector2 leftWheel = new Vector2(wheelDistance, 0);
	//(300, 300)
	protected Vector2 rightWheel = new Vector2(300, 300);
	//In radians; 0 is top, pi/2 is right
	//(double)(Math.PI / 2)
	protected double direction = Math.PI / 2;
	protected Set<Pair<Integer, Integer>> points = new ConcurrentSkipListSet<Pair<Integer, Integer>>();

	protected double[] motor = new double[2];

	protected static final int motorA = 0;
	protected static final int motorB = 1;

	//protected int speedMultiplier = 100;
	protected int updatesPerSecond = 1000;
	protected int updatesPerPaint = 15;

	private Object pauseLock = new Object();
	private boolean paused;
	private boolean enabled;

	private int paintCounter = 0;
	protected long frameCounter = 0;
	protected long lastNano = 0;
	protected long diff = 1000000;

	/**
	 *
	 */
	private static final long serialVersionUID = -9117978120156221878L;

	public RobotCanvas(){
		//motor[motorA] = -100;
		//motor[motorB] = -100;
		enabled = true;
		new Thread(new RobotCanvasRunner()).start();
	}

	public RobotCanvas(Vector2 rightWheelStart, double wheelDistance, double direction
			, double initialLeftSpeed, double initialRightSpeed ){
		this.wheelDistance = wheelDistance;
		rightWheel = new Vector2(rightWheelStart);
		leftWheel = new Vector2(wheelDistance, 0);
		this.direction = direction;
		motor[motorA] = initialLeftSpeed;
		motor[motorB] = initialRightSpeed;
		enabled = true;
		new Thread(new RobotCanvasRunner()).start();
	}

	@Override
	public void setEnabled(boolean b){
		enabled = b;
	}

	public boolean getEnabled(){
		return enabled;
	}

	public void setPaused(boolean b){
		synchronized(pauseLock){
			if(paused == b){
				return;
			}
			paused = b;
			if(!b){
				notifyAll();
			}
		}
	}

	public boolean isPaused(){
		synchronized(pauseLock){
			return paused;
		}
	}



	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
			throws PrinterException{
		//Only one page is printed
		if(pageIndex > 0){
			return Printable.NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D) graphics;
		//Translate into imageable area
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		draw(graphics);
		//Apply borders
		Color tc = g2d.getColor();
		g2d.setColor(Color.BLACK);
		g2d.drawRect(0, 0, (int) pageFormat.getImageableWidth(),
				(int) pageFormat.getImageableHeight());
		g2d.setColor(tc);
		return Printable.PAGE_EXISTS;
	}

	@Override
	public void paint(Graphics g){
		draw(g);
	}

	//Function for drawing called by paint and print
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.setBackground(Color.WHITE);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		//g2d.setColor(Color.BLACK);
		//g2d.fillRect(0, 0, 10, 10);
		for(Pair<Integer, Integer> point : points){
			g2d.drawLine(point.getValueOne(), point.getValueTwo(),
					point.getValueOne(), point.getValueTwo());
		}
		g2d.setColor(Color.RED);
		g2d.fillOval((int) rightWheel.x - 2, (int) rightWheel.y - 2,
				4, 4);
		g2d.setColor(Color.GREEN);
		int subX = (int) (rightWheel.x - Math.cos(direction) * leftWheel.x
				+ Math.sin(direction) * leftWheel.y);
		int subY = (int)(rightWheel.y - Math.cos(direction) * leftWheel.y
				- Math.sin(direction) * leftWheel.x);
		g2d.fillOval(subX - 2, subY - 2, 4, 4);

	}


	//Diff in nanos
	public void updateMotion(long diff){
		double a = 1000000000 / diff;
		double roc = (motor[motorA] - motor[motorB]) / wheelDistance;
		direction += roc / a;
		double speed = motor[motorB] / a;
		rightWheel.y += -Math.cos(direction) * speed;
		rightWheel.x += Math.sin(direction) * speed;
		points.add(Pair.make((int)rightWheel.x, (int)rightWheel.y));
		points.add(Pair.make((int)(rightWheel.x - Math.cos(direction) * leftWheel.x
				+ Math.sin(direction) * leftWheel.y), (int)(rightWheel.y
						- Math.cos(direction) * leftWheel.y
						- Math.sin(direction) * leftWheel.x)));
	}

	protected class RobotCanvasRunner implements Runnable{
		@Override
		public void run(){
			lastNano = System.nanoTime();
			while(enabled){
				while(paused){
					try{
						synchronized(pauseLock){
							wait();
						}
					}
					catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try{
					paintCounter--;
					if(paintCounter <= 0){
						repaint();
						paintCounter = updatesPerPaint;
					}
					long curr = System.nanoTime();
					diff = curr - lastNano;
					frameCounter += diff;
					lastNano = curr;
					Long l = frameCounterLocks.lowerKey(frameCounter / 1000000);
					while(l != null){
						Object o = frameCounterLocks.get(l);
						synchronized(o){
							o.notifyAll();
						}
						frameCounterLocks.remove(l);
						l = frameCounterLocks.lowerKey(frameCounter / 1000000);
					}
					Thread.sleep(1000 / updatesPerSecond);
				}
				catch(InterruptedException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				updateMotion(diff);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e){
		// TODO Auto-generated method stub

	}

	/**
	 * Keys
	 * p: pause / resume
	 * r: print
	 * q: terminate
	 * @param e
	 */
	@Override
	public void keyPressed(KeyEvent e){
		if(e.getKeyChar() == 'r'){
			try{
				boolean tempPaused;
				synchronized(pauseLock){
					tempPaused = isPaused();
					setPaused(true);
				}
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(this);
				boolean doPrint = job.printDialog();
				if(doPrint){
					job.print();
				}
				synchronized(pauseLock){
					setPaused(tempPaused);
				}
			}
			catch(PrinterException e1){
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getKeyChar() == 'q'){
			setEnabled(false);
		}
		if(e.getKeyChar() == 'p'){
			synchronized(this){
				setPaused(!isPaused());
			}
		}
	}



	public Object getFrameCounterLock(long target){
		Object o = frameCounterLocks.get(target);
		if(o != null){
			return o;
		}
		else{
			Object o2 = new Object();
			frameCounterLocks.put(target, o2);
			return o2;
		}
	}


	@Override
	public void keyReleased(KeyEvent e){
		// TODO Auto-generated method stub

	}

	public void clearPoints(){
		points.clear();
	}

}
