package com.github.assisstion.RobotSimulator;

import java.util.Map;

import com.github.assisstion.RobotSimulator.sensor.Sensor;

public interface RobotProgrammingBase{
	double[] getMotors();
	void wait1Msec(long millis);
	default int SensorValue(Sensor sensor){
		if(sensor == null){
			return -1;
		}
		return sensor.sensorValue();
	}
	Map<ShapeEntity, Boolean> getShapes();
}
