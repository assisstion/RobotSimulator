package com.github.assisstion.RobotSimulator;

import java.awt.Color;
import java.awt.Shape;
import java.util.function.Supplier;

public class ShapeEntity implements Supplier<Shape>, Comparable<ShapeEntity>{

	private static volatile int idCounter = 1048576;

	protected static final Color DEFAULT_COLOR = Color.GRAY;

	protected Color color;
	protected Shape shape;
	protected int id;
	protected boolean colliding;

	public ShapeEntity(Shape shape, boolean colliding){
		this(shape, DEFAULT_COLOR, colliding);
	}

	public ShapeEntity(Shape shape, Color color, boolean colliding){
		this(shape, idCounter++, color, colliding);
	}

	public ShapeEntity(Shape shape, int id, boolean colliding){
		this(shape, id, DEFAULT_COLOR, colliding);
	}

	public ShapeEntity(Shape shape, int id, Color color, boolean colliding){
		if(id <= 0){
			throw new IllegalArgumentException("Illegal ID number");
		}
		this.shape = shape;
		this.id = id;
		this.color = color;
		this.colliding = colliding;
	}

	public boolean isColliding(){
		return colliding;
	}

	@Override
	public Shape get(){
		return shape;
	}

	public int getID(){
		return id;
	}

	public Color getColor(){
		return color;
	}

	@Override
	public int hashCode(){
		return getID();
	}

	@Override
	public int compareTo(ShapeEntity o){
		return Integer.compare(getID(), o.getID());
	}
}
