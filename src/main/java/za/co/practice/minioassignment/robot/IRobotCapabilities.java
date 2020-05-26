package za.co.practice.minioassignment.robot;

import za.co.practice.minioassignment.model.Status;

public interface IRobotCapabilities {

	Status perform(); // it will either return SUCCESS or FAILURE
	
	void init();
}
