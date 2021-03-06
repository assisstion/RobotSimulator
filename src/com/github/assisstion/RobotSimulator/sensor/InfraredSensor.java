package com.github.assisstion.RobotSimulator.sensor;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.github.assisstion.RobotSimulator.RobotProgram;
import com.github.assisstion.RobotSimulator.ShapeEntity;
import com.github.assisstion.RobotSimulator.Vector2;

public class InfraredSensor implements Sensor{

	protected RobotProgram program;
	protected Vector2 absolutePosition;
	protected double absoluteDirection;
	protected Vector2 relativePosition;
	protected double maxRange;
	protected double precision;
	protected double testSize;

	public InfraredSensor(RobotProgram program, Vector2 absolutePosition,
			double absoluteDirection, Vector2 relativePosition,
			double maxRange, double precision, double testSize){
		this.program = program;
		this.absolutePosition = absolutePosition;
		this.absoluteDirection = absoluteDirection;
		this.relativePosition = relativePosition;
		this.maxRange = maxRange;
		this.precision = precision;
		this.testSize = testSize;
	}

	@Override
	public int sensorValue(){
		for(ShapeEntity se : program.getShapes()){
			if(test(se) >= 0){
				return 0;
			}
		}
		Vector2 relative = RobotProgram.relativeVector(
				program.getRightWheel(), relativePosition, program.getDirection());
		int dist = test(new ShapeEntity(new Rectangle2D.Double(
				relative.x - testSize, relative.y - testSize,
				testSize * 2, testSize * 2), true));
		if(dist < 0){
			dist = 0;
		}
		return dist;
	}

	//Returns true if hit
	protected int test(ShapeEntity se){
		if(!se.isColliding()){
			return -1;
		}
		Shape shape = se.get();
		Area area = new Area(shape);
		Vector2 v = absolutePosition;
		double direction = absoluteDirection;
		double x1 = v.x;
		double x2 = v.x + Math.sin(direction) * maxRange;
		double y1 = v.y;
		double y2 = v.y - Math.cos(direction) * maxRange;
		double yMod = Math.abs(Math.sin(direction) * precision);
		double xMod = Math.abs(Math.cos(direction) * precision);
		double[] xPoints = new double[]{x1 - xMod, x1 + xMod, x2 + xMod, x2 - xMod};
		double[] yPoints = new double[]{y1 - yMod, y1 + yMod, y2 + yMod, y2 - yMod};
		Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, 4);
		path.moveTo(xPoints[0], yPoints[0]);
		for(int i = 1; i < 4; i++){
			path.lineTo(xPoints[i], yPoints[i]);
		}
		path.closePath();
		Area polyArea = new Area(path);
		area.intersect(polyArea);
		Area newPolyArea = new Area(polyArea);
		newPolyArea.exclusiveOr(area);
		PathIterator pi = polyArea.getPathIterator(null);
		PathIterator pi1 = newPolyArea.getPathIterator(null);
		boolean unequal = false;
		List<Vector2> pis = new ArrayList<Vector2>();
		List<Vector2> pi1s = new ArrayList<Vector2>();
		double[] da0 = new double[6];
		double[] da1 = new double[6];
		while(!pi.isDone()){
			pi.currentSegment(da0);
			for(int i = 0; i < 2; i += 2){
				pis.add(new Vector2(da0[i], da0[i + 1]));
			}
			pi.next();
		}
		while(!pi1.isDone()){
			pi1.currentSegment(da1);
			for(int i = 0; i < 2; i += 2){
				pi1s.add(new Vector2(da1[i], da1[i + 1]));
			}
			pi1.next();
		}
		List<Vector2> coords = new ArrayList<Vector2>();
		Vector2 lastStack = null;
		boolean isLast = false;
		for(int i = 0; i < pi1s.size(); i++){
			Vector2 dn = pi1s.get(i);
			if(!pis.contains(dn)){
				unequal = true;
				if(isLast){
					coords.add(new Vector2(dn).add(lastStack).divide(2));
				}
				isLast = true;
				lastStack = dn;
			}
			else{
				isLast = false;
			}
		}
		if(!unequal){
			return -1;
		}
		double mindist = Double.MAX_VALUE;
		int sz = coords.size();
		for(int i = 0; i < sz; i++){
			Vector2 coord = coords.get(i);
			double dist = Math.sqrt((coord.x - v.x) * (coord.x - v.x) +
					(coord.y - v.y) * (coord.y - v.y));
			if(dist < mindist){
				mindist = dist;
			}
		}
		return (int)mindist;
	}
}
