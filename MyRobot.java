/**
 * CS 4710 Homework 2
 * Pak Hin Luu - pl4me
 * Ellie O'Neil - ebo6jt
 */

import world.Robot;
import world.World;

import java.awt.*;
import java.util.*;

public class MyRobot extends Robot {
    boolean isUncertain;
	
    @Override
    public void travelToDestination() {
        if (isUncertain) {
			// call function to deal with uncertainty
        }
        else {
			// call function to deal with certainty
        }
    }

    @Override
    public void addToWorld(World world) {
        isUncertain = world.getUncertain();
        super.addToWorld(world);
    }

    public static void main(String[] args) {
        try {
			World myWorld = new World("TestCases/myInputFile1.txt", true);
			
            MyRobot robot = new MyRobot();
            robot.addToWorld(myWorld);
			//myWorld.createGUI(400, 400, 200); // uncomment this and create a GUI; the last parameter is delay in msecs
			

			robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
