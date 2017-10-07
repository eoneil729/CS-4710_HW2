/**
 * CS 4710 Homework 2
 * Pak Hin Luu - pl4me
 * Ellie O'Neil - ebo6jt
 */

package HW2;

import world.Robot;
import world.World;

import java.awt.*;
import java.util.*;

public class MyRobot extends Robot {
    private static String[][] worldMap;
    boolean isUncertain;
	
    @Override
    public void travelToDestination() {
        if (isUncertain) {
			// call function to deal with uncertainty
            pathfindingWithUncertainty();
        }
        else {
			// call function to deal with certainty
            pathfindingWithCertainty();
        }
    }

    public String pathfindingWithUncertainty() {



        return "";
    }

    public String pathfindingWithCertainty() {
        // https://en.wikipedia.org/wiki/A*_search_algorithm
        List<Point> closed = new ArrayList<Point>();
        List<Point> open = new ArrayList<Point>();
        Point point = this.getStartPos();
        open.add(point);
        while (!open.isEmpty()) {

        }




        return "";
    }

    @Override
    public void addToWorld(World world) {
        isUncertain = world.getUncertain();
        super.addToWorld(world);
    }

    public static void main(String[] args) {
        try {
            //uncertainty
//			World myWorld = new World("TestCases/myInputFile1.txt", true);
            //certainty
            World myWorld = new World("TestCases/myInputFile1.txt", false);
            System.out.println(myWorld.startingPosition());
			
            MyRobot robot = new MyRobot();
            robot.addToWorld(myWorld);
			//myWorld.createGUI(400, 400, 200); // uncomment this and create a GUI; the last parameter is delay in msecs
            for (int i = 0; i < myWorld.numRows(); i++) {
                for (int j = 0; j < myWorld.numCols(); j++) {
                    Point p = new Point(y, x);
                    worldMap[j][i] = myWorld.pingMap(p);
                }
            }

			robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
