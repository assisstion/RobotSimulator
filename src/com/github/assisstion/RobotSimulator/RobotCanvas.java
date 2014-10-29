package com.github.assisstion.RobotSimulator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;

import javax.swing.JPanel;

public class RobotCanvas extends JPanel implements Printable, KeyListener{

	private NavigableMap<Long, Object> frameCounterLocks =
			new TreeMap<Long, Object>();

	private Map<Object, Supplier<Boolean>> waitLocks = new HashMap<Object,
			Supplier<Boolean>>();

	//Boolean: does collide
	protected Map<Shape, Boolean> shapes = new HashMap<Shape, Boolean>();

	//Position relative to the right wheel
	//(5.0f, 0)
	protected Vector2 leftWheel = new Vector2(5.0f, 0);
	//(300, 300)
	protected Vector2 rightWheel = new Vector2(300, 300);
	//In radians; 0 is top, pi/2 is right
	//(double)(Math.PI / 2)
	protected double direction = Math.PI / 2;
	protected Set<Pair<Integer, Integer>> points = new ConcurrentSkipListSet<Pair<Integer, Integer>>();

	public double[] motor = new double[3];

	public static final int motorA = 0;
	public static final int motorB = 1;
	public static final int motorC = 2;


	//protected int speedMultiplier = 100;
	private int updatesPerSecond = 1000;
	private int updatesPerPaint = 15;

	private Object pauseLock = new Object();
	private boolean paused;
	private boolean enabled;

	private int paintCounter = 0;
	//In nanos
	protected long frameCounter = 0;
	private long lastNano = 0;
	private long diff = 1000000;
	private double aboveY;
	private double belowY;

	private boolean rect = false;

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

	public RobotCanvas(Vector2 rightWheelStart, double wheelDistance, double aboveY,
			double belowY, double direction
			, double initialLeftSpeed, double initialRightSpeed, boolean rect){
		this.rect = rect;
		rightWheel = new Vector2(rightWheelStart);
		leftWheel = new Vector2(wheelDistance, 0);
		this.direction = direction;
		motor[motorB] = initialLeftSpeed;
		motor[motorC] = initialRightSpeed;
		enabled = true;
		this.aboveY = aboveY;
		this.belowY = belowY;
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
				pauseLock.notifyAll();
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
			g2d.drawRect(point.getValueOne(), point.getValueTwo(),
					0, 0);
		}
		g2d.setColor(Color.RED);
		g2d.fillOval((int) rightWheel.x - 2, (int) rightWheel.y - 2,
				4, 4);
		g2d.setColor(Color.GREEN);
		Vector2 sub = relativeVector(rightWheel, leftWheel, direction);
		double subX = sub.x;
		double subY = sub.y;
		g2d.fillOval((int) subX - 2, (int) subY - 2, 4, 4);
		g2d.setColor(Color.BLUE);
		/*
		double centerX = (rightWheel.x + subX) / 2;
		double centerY = (rightWheel.y + subY) / 2;
		g2d.fillOval((int)(centerX - leftWheel.x / 2), (int)(centerY - leftWheel.x / 2),
				(int) leftWheel.x, (int) leftWheel.x);
		 */
		if(rect){
			g2d.fill(getRobotRect());
		}
		else{
			g2d.fill(getRobotEllipse());
		}
		g2d.setColor(Color.GRAY);
		for(Map.Entry<Shape, Boolean> shape : shapes.entrySet()){
			if(shape.getValue()){
				g2d.fill(shape.getKey());
			}
			else{
				g2d.draw(shape.getKey());
			}
		}
		g2d.setColor(Color.RED);
		double width = leftWheel.x;
		g2d.drawString("WIDTH: " + width + "cm", 100, 100);
		g2d.drawLine(100, 125, 100, 135);
		g2d.drawLine(200, 125, 200, 135);
		g2d.drawLine(100, 130, 200, 130);
		g2d.drawString("1m", 135, 140);

	}

	public static Vector2 relativeVector(Vector2 orig, Vector2 add, double direction){
		return new Vector2(orig.x - Math.cos(direction) * add.x
				+ Math.sin(direction) * add.y, orig.y - Math.cos(direction) * add.y
				- Math.sin(direction) * add.x);
	}


	//Diff in nanos
	public void updateMotion(long diff){
		double a = 1000000000 / diff;
		double roc = (motor[motorB] - motor[motorC]) / leftWheel.x;
		direction += roc / a;
		double speed = motor[motorC] / a;
		if(!resolveWheel(speed)){
			rightWheel.y -= -Math.cos(direction) * speed;
			rightWheel.x -= Math.sin(direction) * speed;
			direction -= roc / a;
		}
		Vector2 sub = relativeVector(rightWheel, leftWheel, direction);
		points.add(Pair.make((int)rightWheel.x, (int)rightWheel.y));
		points.add(Pair.make((int)sub.x, (int)sub.y));
	}

	public boolean resolveWheel(double dist){
		rightWheel.y += -Math.cos(direction) * dist;
		rightWheel.x += Math.sin(direction) * dist;
		Shape e2dd;
		if(rect){
			e2dd = getRobotRect();
		}
		else{
			e2dd = getRobotEllipse();
		}
		for(Map.Entry<Shape, Boolean> shapeHolder : shapes.entrySet()){
			if(!shapeHolder.getValue()){
				continue;
			}
			Shape shape = shapeHolder.getKey();
			Area ae2dd = new Area(e2dd);
			ae2dd.intersect(new Area(shape));
			if(!ae2dd.isEmpty()){
				return false;
			}
		}
		return true;
	}

	public Ellipse2D.Double getRobotEllipse(){
		Vector2 sub = relativeVector(rightWheel, leftWheel, direction);
		double subX = sub.x;
		double subY = sub.y;
		double centerX = (rightWheel.x + subX) / 2;
		double centerY = (rightWheel.y + subY) / 2;
		return new Ellipse2D.Double(centerX - leftWheel.x / 2, centerY - leftWheel.x / 2,
				leftWheel.x, leftWheel.x);
	}

	public Polygon getRobotRect(){
		Vector2 sub = relativeVector(rightWheel, leftWheel, direction);
		double aXSub = aboveY * Math.sin(direction);
		double aYSub = aboveY * -Math.cos(direction);
		double bXSub = belowY * Math.sin(direction);
		double bYSub = belowY * -Math.cos(direction);
		int[] pointsX = new int[4];
		int[] pointsY = new int[4];
		pointsX[0] = (int)(rightWheel.x + aXSub);
		pointsY[0] = (int)(rightWheel.y + aYSub);
		pointsX[1] = (int)(sub.x + aXSub);
		pointsY[1] = (int)(sub.y + aYSub);
		pointsX[2] = (int)(sub.x - bXSub);
		pointsY[2] = (int)(sub.y - bYSub);
		pointsX[3] = (int)(rightWheel.x - bXSub);
		pointsY[3] = (int)(rightWheel.y - bYSub);
		return new Polygon(pointsX, pointsY, 4);
	}

	protected class RobotCanvasRunner implements Runnable{
		@Override
		public void run(){
			lastNano = System.nanoTime();
			while(enabled){
				long pauseNano = System.nanoTime();
				while(paused){
					try{
						synchronized(pauseLock){
							pauseLock.wait();
						}
					}
					catch(InterruptedException e){
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				long pausedNano = System.nanoTime() - pauseNano;
				lastNano += pausedNano;
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
				for(Map.Entry<Object, Supplier<Boolean>> condition : waitLocks.entrySet()){
					if(condition.getValue().get()){
						Object lock = condition.getKey();
						synchronized(lock){
							lock.notifyAll();
						}
					}
				}
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

	public Object getWaitLock(Supplier<Boolean> condition){
		Object o = new Object();
		waitLocks.put(o, condition);
		return o;
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



	public Vector2 getRightWheel(){
		return rightWheel;
	}

	public Vector2 getLeftWheel(){
		return relativeVector(rightWheel, leftWheel, direction);
	}

	public Vector2 getLeftWheelRelative(){
		return leftWheel;
	}

	public double getDirection(){
		return direction;
	}

	public boolean isRect(){
		return rect;
	}
}
