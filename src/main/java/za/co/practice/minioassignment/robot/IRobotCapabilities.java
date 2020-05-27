package za.co.practice.minioassignment.robot;

import org.slf4j.Logger;

import za.co.practice.minioassignment.model.Status;

public interface IRobotCapabilities {

    Status perform(); // it will either return SUCCESS or FAILURE

    void init();

    Logger getLogger();
}
