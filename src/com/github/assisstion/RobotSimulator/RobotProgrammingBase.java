package com.github.assisstion.RobotSimulator;

import com.github.assisstion.RobotSimulator.sensor.Sensor;

public interface RobotProgrammingBase{
	double[] getMotors();
	void wait1Msec(long millis);
	default int SensorValue(Sensor sensor){
		return sensor.sensorValue();
	}
}
