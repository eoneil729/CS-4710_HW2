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
    private String[][] knownWorld;
    private Point start;
    private Point goal;
    private boolean isUncertain;

    /**
     * Finds the path using A* search
     * Reference: https://en.wikipedia.org/wiki/A*_search_algorithm
     */
    @Override
    public void travelToDestination() {
        knownWorld = new String[myWorld.numRows()][myWorld.numCols()];
        if(!isUncertain) createMap();
        List<Node> closed = new ArrayList<Node>();
        List<Node> open = new ArrayList<Node>();
        Node currentNode = new Node();
        currentNode.setPoint(start);
        currentNode.setDistance(0);
        currentNode.setParent(currentNode);
        open.add(currentNode);
        Node endGoal = new Node();
        endGoal.setPoint(goal);
        while (!open.isEmpty()) {
            double min = 100;
            for (int i = 0; i < open.size(); i++) {
                if (open.get(i).getfScore() < min) {
                    min = open.get(i).getfScore();
                    currentNode = open.get(i);
                }
            }
            if (currentNode.getPoint().equals(endGoal.getPoint())) {
                endGoal.setParent(currentNode);
                tracePath(endGoal);
                return;
            }
            open.remove(currentNode);
            closed.add(currentNode);
            List<Node> neighbors = new ArrayList<Node>();
            if (isUncertain)
                neighbors = uncertainGetNeighbors(currentNode);
            else
                neighbors = certainGetNeighbors(currentNode);
            for (Node currentNeighbor : neighbors) {
                if (!currentNeighbor.listContains(open) && !currentNeighbor.listContains(closed)) {
                    open.add(currentNeighbor);
                    currentNeighbor.setParent(currentNode);
                }
                if (currentNeighbor.listContains(open) && !currentNeighbor.listContains(closed)) {
                    double distance = 0;
                    Node foundNode = new Node();
                    for (Node n : open) {
                        if (n.getPoint().equals(currentNeighbor.getPoint())) {
                            distance = n.getDistance();
                            foundNode.setDistance(n.getDistance());
                            foundNode.setParent(n.getParent());
                            foundNode.setPoint(n.getPoint());
                            foundNode.setfScore(n.getfScore());
                            foundNode.setgScore(n.getgScore());
                        }
                    }
                    if (currentNeighbor.getDistance() < distance) {
                        currentNeighbor.setDistance(foundNode.getParent().getDistance()+1);
                        currentNeighbor.setfScore(getfScore(currentNeighbor));
                        Node tempParent = foundNode.getParent();
                        currentNeighbor.setParent(tempParent);
                        open.add(currentNeighbor);
                    }
                }
            }
        }
    }

    /**
     * Trace path after finding goal node by tracing backwards through the recorded parent nodes and then moving
     * through in reverse order
     */
    public void tracePath(Node endGoal) {
        List<Node> path = new ArrayList<Node>();
        Node current = endGoal;
        int x = current.getX();
        int y = current.getY();

        if(!isUncertain) {
            while (!knownWorld[x][y].equals("S")) {
                path.add(current);
                current = current.getParent();
                x = current.getX();
                y = current.getY();

            }
        } else {
            while (!pingMap(new Point(x, y)).equals("S")) {
                path.add(current);
                current = current.getParent();
                x = current.getX();
                y = current.getY();
            }
        }
        Collections.reverse(path);
        for (Node node : path) {
            move(node.getPoint());
        }
    }

    /**
     * Gets all neighbors of the node if the neighbor isn't out of bounds or a wall
     */
    public List<Node> uncertainGetNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        int x = node.getX();
        int y = node.getY();
        if (x-1 > -1 && y-1 > -1) {
            if (!uncertainPing(new Point(x - 1, y - 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y - 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //upper left
        }
        if (x-1 > -1) {
            if (!uncertainPing(new Point(x - 1, y)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //up
        }
        if (y-1 > -1) {
            if (!uncertainPing(new Point(x, y - 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x, y - 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //left
        }
        if (x+1 < myWorld.numRows() && y+1 < myWorld.numCols()) {
            if (!uncertainPing(new Point(x + 1, y + 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y + 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //bottom right
        }
        if (x+1 < myWorld.numRows()) {
            if (!uncertainPing(new Point(x + 1, y)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //down
        }
        if (y+1 < myWorld.numCols()) {
            if (!uncertainPing(new Point(x, y + 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x, y + 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //right
        }
        if (x+1 < myWorld.numRows() && y-1 > -1) {
            if (!uncertainPing(new Point(x + 1, y - 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y - 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //lower left
        }
        if (x-1 > -1 && y+1 < myWorld.numCols()) {
            if (!uncertainPing(new Point(x - 1, y + 1)).equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y + 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //upper right
        }
        return neighbors;
    }

    /**
     * Gets all neighbors of the node if the neighbor isn't out of bounds or a wall
     */
    public List<Node> certainGetNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        int x = node.getX();
        int y = node.getY();
        if (x-1 > -1 && y-1 > -1) {
            if (!knownWorld[x - 1][y - 1].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y - 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //upper left
        }
        if (x-1 > -1) {
            if (!knownWorld[x - 1][y].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //up
        }
        if (y-1 > -1) {
            if (!knownWorld[x][y - 1].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x, y - 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //left
        }
        if (x+1 < myWorld.numRows() && y+1 < myWorld.numCols()) {
            if (!knownWorld[x + 1][y + 1].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y + 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //bottom right
        }
        if (x+1 < myWorld.numRows()) {
            if (!knownWorld[x + 1][y].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //down
        }
        if (y+1 < myWorld.numCols()) {
            if (!knownWorld[x][y + 1].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x, y + 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //right
        }
        if (x+1 < myWorld.numRows() && y-1 > -1) {
            if (!knownWorld[x + 1][y - 1].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x + 1, y - 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //lower left
        }
        if (x-1 > -1 && y+1 < myWorld.numCols()) {
            if (!knownWorld[x - 1][y + 1].equals("X")) {
                Node neighbor = new Node();
                neighbor.setPoint(new Point(x - 1, y + 1));
                neighbor.setDistance(node.getDistance()+1);
                neighbor.setfScore(getfScore(neighbor));

                neighbor.setParent(node);
                neighbors.add(neighbor);
            } //upper right
        }
        return neighbors;
    }

    public void createMap() {
        for (int i = 0; i < myWorld.numRows(); i++) {
            for (int j = 0; j < myWorld.numCols(); j++) {
                knownWorld[i][j] = pingMap(new Point(i,j));
            }
        }
    }

    /**
     * For certainty, returns the pinged location for currentPosition
     * For uncertainty, pings currentPosition 1000x and returns the more frequent result between X and O
     */
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

    /**
     * Returns fScore, or the distance from start to current node + the distance from current node to the goal node
     */
    public double getfScore(Node current) {
        return getgScore(current) + gethScore(current);
    }

    /**
     * Returns gScore, or the distance from start to current node
     */
    public double getgScore(Node current) {
        return current.getDistance();
    }

    public double gethScore(Node current) {
        return Math.max(Math.abs(current.getX() - goal.getX()), Math.abs(current.getY() - goal.getY()));
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
            robot.myWorld = new World("TestCases/myInputFile3.txt", false);


            robot.start = robot.myWorld.getStartPos();
            robot.goal = robot.myWorld.getEndPos();

            robot.addToWorld(robot.myWorld);
//			robot.myWorld.createGUI(400, 400, 200); // uncomment this and create a GUI; the last parameter is delay in msecs

            robot.travelToDestination();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
