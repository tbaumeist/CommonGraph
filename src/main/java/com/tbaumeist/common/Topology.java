package com.tbaumeist.common;

import java.util.*;
import com.tbaumeist.common.utils.*;

public class Topology {

    private List<Node> nodes = new CircleList<Node>();

    private final double NOT_INITED_LOC = -1.0;

    public Node findNode(double location, String id) {
        Node tmp = new Node(location, id);
        if (!this.nodes.contains(tmp))
            return null;

        return this.nodes.get(this.nodes.indexOf(tmp));
    }

    public List<Node> getAllNodes() {
        return this.nodes;
    }

    @Override
    public String toString() {
        String out = "";
        for (Node n : getAllNodes()) {
            out += n + " -> (";
            for (Node n2 : n.getDirectNeighbors()) {
                out += " " + n2 + ",";
            }
            out += ")\n";
        }
        return out;
    }

    public void addNode(Node node) {
        this.checkAddNode(node);
        Collections.sort(this.nodes);
    }

    public void addAllNodes(Collection<Node> nodes) {
        //for(Node n : nodes)
        //    this.checkAddNode(n);
        this.nodes.addAll(nodes);
        Collections.sort(this.nodes);
    }
    
    public int getEdgeCount(){
        int count = 0;
        
        for(Node n : this.getAllNodes()){
            count += n.getDirectNeighbors().size();
        }
        
        assert count % 2 == 0;
        count = count / 2; // counted each edge twice
        
        return count;
    }

    public void clearOneWayConnections() {
        List<Node> removeNodes = new ArrayList<Node>();

        for (Node n : getAllNodes()) {
            List<Node> removeNeighbors = new ArrayList<Node>();

            for (Node n2 : n.getDirectNeighbors()) {
                if (!n2.getDirectNeighbors().contains(n))
                    removeNeighbors.add(n2);
            }
            n.removeNeighbors(removeNeighbors);
            if (n.getDirectNeighbors().isEmpty())
                removeNodes.add(n);
        }
        getAllNodes().removeAll(removeNodes);
    }

    private void checkAddNode(Node node) {
        if (this.nodes.contains(node))
            return;
        if (node.getLocation() == NOT_INITED_LOC)
            return;
        this.nodes.add(node);
    }
}
