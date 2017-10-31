import java.awt.*;
import java.util.List;
import java.util.PriorityQueue;

public class Node {
    private Point point;
    private List<Node> neighbors;
    private double fScore;
    private double gScore;
    private Node parent;
    private int distance;

    public Node() {}

    public void setPoint(Point point) {
        this.point = point;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public void setfScore(double fScore) {
        this.fScore = fScore;
    }

    public void setgScore(double gScore) { this.gScore = gScore; }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Point getPoint() {
        return point;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public double getfScore() {
        return fScore;
    }

    public double getgScore() {
        return gScore;
    }

    public Node getParent() {
        return parent;
    }

    public int getDistance() { return distance; }

    public int getX() {
        return (int)this.getPoint().getX();
    }

    public int getY() { return (int)this.getPoint().getY(); }

    public boolean listContains (List<Node> list) {
        boolean ans = false;
        for (Node n: list) {
            if (this.getPoint().equals(n.getPoint()))
                ans = true;
        }
        return ans;
    }

}