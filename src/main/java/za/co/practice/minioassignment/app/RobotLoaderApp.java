package za.co.practice.minioassignment.app;

import za.co.practice.minioassignment.robot.ReadEmailAndUploadRobot;

public class RobotLoaderApp {

    public static void main(String[] args) {
        ReadEmailAndUploadRobot robot = new ReadEmailAndUploadRobot();
        robot.init();
        robot.perform();
    }
}
