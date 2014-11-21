package com.github.assisstion.RobotSimulator.sensor;

import java.util.Comparator;
import java.util.List;

public class CompositeSensor<T extends Sensor> implements Sensor{

	protected List<? extends T> sensors;
	protected Comparator<T> comp;

	public CompositeSensor(List<? extends T> sensors){
		this(sensors, (s, t) -> Integer.compare(s.sensorValue(), t.sensorValue()));
	}

	public CompositeSensor(List<? extends T> sensors, Comparator<T> comp){
		this.sensors = sensors;
		this.comp = comp;
	}

	@Override
	public int sensorValue(){
		T highest = null;
		int current = 0;
		for(T sensor : sensors){
			if(highest == null){
				highest = sensor;
				current = sensor.sensorValue();
			}
			else{
				if(comp.compare(sensor, highest) >= 0){
					highest = sensor;
					current = sensor.sensorValue();
				}
			}
		}
		return current;
	}

}
