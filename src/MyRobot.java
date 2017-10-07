/**
 * CS 4710 Homework 2
 * Pak Hin Luu - pl4me
 * Ellie O'Neil - ebo6jt
 */

import world.Robot;
import world.World;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MyRobot extends Robot {
    private World myWorld;
    private Point start;
    private Point goal;
    private boolean isUncertain;
	
    @Override
    public void travelToDestination() {
        pathfinding();
    }

    public void pathfinding() {
        // Reference: https://en.wikipedia.org/wiki/A*_search_algorithm
        List<Node> closed = new ArrayList<Node>();
        Comparator<Node> comparator = new DistanceComparator<Node>();
        PriorityQueue<Node> open = new PriorityQueue<Node>(50, comparator);
        Node currentNode = new Node();
        currentNode.setPoint(start);
        open.add(currentNode);
        Node endGoal = new Node();
        endGoal.setPoint(goal);
        while (!open.isEmpty()) {
            currentNode = open.poll();
            if (currentNode.getPoint().equals(endGoal.getPoint())) {
                endGoal.setParent(currentNode);
                tracePath(endGoal);
                return;
            }
            open.remove(currentNode);
            closed.add(currentNode);
            List<Node> neighbors = getNeighbors(currentNode);
            for (Node currentNeighbor : neighbors) {
                if (!currentNeighbor.queueContains(open) && !currentNeighbor.listContains(closed)) {
                    open.add(currentNeighbor);
                    currentNeighbor.setParent(currentNode);
                }
            }
        }
    }

    public void tracePath(Node endGoal) {
        List<Node> path = new ArrayList<Node>();
        Node current = endGoal;
        int x = current.getX();
        int y = current.getY();
        while (!pingMap(new Point(x, y)).equals("S")) {
            path.add(current);
//            System.out.println("current: " + current.getPoint());
            current = current.getParent();
            x = current.getX();
            y = current.getY();
        }
        Collections.reverse(path);
        for (Node node : path) {
            move(node.getPoint());
        }
    }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        int x = node.getX();
        int y = node.getY();
        if (x-1 > -1 && y-1 > -1) {
            if (!uncertainPing(new Point(x - 1, y - 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y - 1));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //upper left
        }
        if (x-1 > -1) {
            if (!uncertainPing(new Point(x - 1, y)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //up
        }
        if (y-1 > -1) {
            if (!uncertainPing(new Point(x, y - 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x, y - 1));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //left
        }
        if (x+1 < myWorld.numRows() && y+1 < myWorld.numCols()) {
            if (!uncertainPing(new Point(x + 1, y + 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y + 1));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //bottom right
        }
        if (x+1 < myWorld.numRows()) {
            if (!uncertainPing(new Point(x + 1, y)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //down
        }
        if (y+1 < myWorld.numCols()) {
            if (!uncertainPing(new Point(x, y + 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x, y + 1));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //right
        }
        if (x+1 < myWorld.numRows() && y-1 > -1) {
            if (!uncertainPing(new Point(x + 1, y - 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y - 1));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //lower left
        }
        if (x-1 > -1 && y+1 < myWorld.numCols()) {
            if (!uncertainPing(new Point(x - 1, y + 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y + 1));
                neighbor.setfScore(getDistance(neighbor));
                neighbors.add(neighbor);
            } //upper right
        }
        return neighbors;
    }

    public String uncertainPing(Point currentPosition) {
        if (!isUncertain)
            return pingMap(currentPosition);
        else {
            String ans;
            int X = 0;
            int O = 0;
            for (int i = 0; i < 1000; i++) {
                if (pingMap(currentPosition).equals("X"))
                    X++;
                else
                    O++;
            }
            if (X > O)
                ans = "X";
            else
                ans = "O";

            return ans;
        }
    }

    public double getDistance(Node current) {
        return (Math.abs((start.getX() - current.getX())) + Math.abs((start.getY() - current.getY()))
            + Math.abs((goal.getX() - current.getX())) + Math.abs((goal.getY() - current.getY())));
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
            MyRobot robot = new MyRobot();
            robot.myWorld = new World("TestCases/myInputFile4.txt", true);
            robot.start = robot.myWorld.getStartPos();
            robot.goal = robot.myWorld.getEndPos();

            robot.addToWorld(robot.myWorld);
			robot.myWorld.createGUI(400, 400, 200); // uncomment this and create a GUI; the last parameter is delay in msecs

			robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class DistanceComparator<T> implements Comparator<Node> {
        @Override
        public int compare(Node p1, Node p2) {
            if (p1.getfScore() < p2.getfScore()) {
                return -1;
            }
            if (p2.getfScore() > p1.getfScore()) {
                return 1;
            }
            return 0;
        }
    }
}
