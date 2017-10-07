import java.awt.*;
import java.util.List;
import java.util.PriorityQueue;

public class Node {
    private Point point;
    private List<Node> neighbors;
    private double fScore;
    private Node parent;

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

    public void setParent(Node parent) {
        this.parent = parent;
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

    public Node getParent() {
        return parent;
    }

    public int getX() {
        return (int)this.getPoint().getX();
    }

    public int getY() {
        return (int)this.getPoint().getY();
    }

    public boolean queueContains (PriorityQueue<Node> queue) {
        boolean ans = false;
        for (Node n: queue) {
            if (this.getPoint().equals(n.getPoint()))
                ans = true;
        }
        return ans;
    }

    public boolean listContains (List<Node> list) {
        boolean ans = false;
        for (Node n: list) {
            if (this.getPoint().equals(n.getPoint()))
                ans = true;
        }
        return ans;
    }

}