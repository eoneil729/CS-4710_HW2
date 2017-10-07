package HW2;

public class Node extends Point {
    private List<Node> neighbors;

    public Node() {}

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }
}