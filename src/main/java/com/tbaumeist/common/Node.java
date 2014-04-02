package com.tbaumeist.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tbaumeist.common.utils.CircleList;
import com.tbaumeist.common.utils.DistanceTools;


public class Node extends INode {

    private List<Node> neighbors = new CircleList<Node>();
    
    private int degreeTarget = -1;

    private final static String DELIMITER = "&&";

    public Node(double loc, String id) {
        this.location = loc;
        this.id = id;
    }
    
    public void setDegreeTarget(int degree){
        this.degreeTarget = degree;
    }
    
    public int getDegreeTarget(){
        return this.degreeTarget;
    }
    
    public boolean atDegree(){
        return this.neighbors.size() >= this.degreeTarget;
    }

    public void addNeighbor(Node node) {
        if (this.neighbors.contains(node))
            return;

        this.neighbors.add(node);
        Collections.sort(this.neighbors);
    }

    public void removeNeighbors(List<Node> nodes) {
        this.neighbors.removeAll(nodes);
    }

    public boolean hasDirectNeighbor(Node n) {
        return this.getDirectNeighbors().contains(n);
    }

    public List<Node> getDirectNeighbors() {
        return getDirectNeighbors(null);
    }

    public List<Node> getDirectNeighbors(List<Node> ignoreNodes) {
        List<Node> ns = new CircleList<Node>();
        ns.addAll(this.neighbors);

        if (ignoreNodes != null)
            ns.removeAll(ignoreNodes);

        return ns;
    }
    
    public List<Node> getAllNeighborsInXHops(int lookAheadHops) {
        return getAllNeighborsInXHops(lookAheadHops, null);
    }
    
    public List<Node> getAllNeighborsInXHops(int lookAheadHops, List<Node> excludeNodes) {
        
        ArrayList<Set<Node>> arrayNodeLists = new ArrayList<Set<Node>>();
        getAllNeighborsAtHop(lookAheadHops, 1, arrayNodeLists, excludeNodes);
        
        List<Node> neighbors = new CircleList<Node>();
        for(Set<Node> n : arrayNodeLists){
            neighbors.addAll(n);
        }
        
        return neighbors;
    }
    
    private void getAllNeighborsAtHop(final int maxLookAhead, int curLookAhead, ArrayList<Set<Node>> arrayNodeLists, List<Node> excludeNodes){
 
        if(curLookAhead < 1 || curLookAhead > maxLookAhead)
            return;
        
        Set<Node> thisHop = new HashSet<Node>();
        
        if(curLookAhead == 1) {
            thisHop.addAll(getDirectNeighbors(excludeNodes));
        } else {
            Set<Node> previousHop = arrayNodeLists.get(curLookAhead-2);
            for(Node n : previousHop){
                thisHop.addAll(n.getDirectNeighbors(excludeNodes));
            }
            // remove previous hops
            for(Set<Node> alreadyDoneHops : arrayNodeLists){
                thisHop.removeAll(alreadyDoneHops);
            }
            // remove this node
            thisHop.remove(this);
        }
        
        arrayNodeLists.add(curLookAhead - 1, thisHop);
        
        // get the next level of hops
        getAllNeighborsAtHop(maxLookAhead, curLookAhead + 1, arrayNodeLists, excludeNodes);
    }

    public Node getNextClosestPeerExcluding(double loc,
            List<Node> excludeNodes, boolean onlyCloserThenMe, int lookAheadHops) {

        List<Node> neighbors = getAllNeighborsInXHops(lookAheadHops, excludeNodes);
        
        Node closest = null;
        double closestDiff = 1;
        for (Node n : neighbors) {
            double diff = DistanceTools.getDistance(n.getLocation(), loc);

            if (closest == null) {
                closest = n;
                closestDiff = diff;
                continue;
            }
            if (diff < closestDiff) {
                closest = n;
                closestDiff = diff;
            }
        }

        if (closest == null)
            return null;
        if (onlyCloserThenMe
                && closestDiff > DistanceTools.getDistance(this.getLocation(),
                        loc))
            return null;

        return closest;
    }

    @Override
    public String toString() {
        return this.getLocation() + " {" + this.id + "}";
    }

    public String serialize() {
        StringBuilder b = new StringBuilder();
        b.append(this.location);
        b.append(DELIMITER);
        b.append(this.id);
        b.append(DELIMITER);
        b.append(this.degreeTarget);
        return b.toString();
    }

    public static Node deserialize(String s) {
        String[] parts = s.split(DELIMITER);
        double loc = Double.parseDouble(parts[0]);
        Node n = new Node(loc, parts[1]);
        n.setDegreeTarget(Integer.parseInt(parts[2]));
        return n;
    }
}
