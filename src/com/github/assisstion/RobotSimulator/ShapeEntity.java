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

	public ShapeEntity(Shape shape){
		this(shape, DEFAULT_COLOR);
	}

	public ShapeEntity(Shape shape, Color color){
		this(shape, idCounter++, color);
	}

	public ShapeEntity(Shape shape, int id){
		this(shape, id, DEFAULT_COLOR);
	}

	public ShapeEntity(Shape shape, int id, Color color){
		this.shape = shape;
		this.id = id;
		this.color = color;
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
