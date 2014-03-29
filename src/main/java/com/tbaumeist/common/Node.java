package com.tbaumeist.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tbaumeist.common.Utils.CircleList;
import com.tbaumeist.common.Utils.DistanceTools;


public class Node extends INode {

    private List<Node> neighbors = new CircleList<Node>();

    private final static String DELIMITER = "&&";

    public Node(double loc, String id) {
        this.location = loc;
        this.id = id;
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

    public Node getNextClosestPeerExcluding(double loc,
            List<Node> excludeNodes, boolean onlyCloserThenMe) {

        List<_Node> neighbors = new ArrayList<_Node>();
        // 1 hop neighbors
        for (Node n : getDirectNeighbors(excludeNodes)) {
            neighbors.add(new _Node(n, n.getLocation()));
        }

        // 2 hop neighbors
        getindirectNeighbors(excludeNodes, neighbors);

        _Node closest = null;
        double closestDiff = 1;
        for (_Node n : neighbors) {
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

        return closest.getNode();
    }

    private void getindirectNeighbors(List<Node> excludeNodes,
            List<_Node> neighbors) {
        for (Node n : getDirectNeighbors(excludeNodes)) {
            for (Node n2 : n.getDirectNeighbors(excludeNodes)) {
                Node tmp = new Node(n2.getLocation(), "");
                if (!neighbors.contains(tmp)) {
                    neighbors.add(new _Node(n, tmp.getLocation()));
                } else {
                    // increment tie counter
                    neighbors.get(neighbors.indexOf(tmp)).tie();
                }
            }
        }
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
        return b.toString();
    }

    public static Node deserialize(String s) {
        String[] parts = s.split(DELIMITER);
        double loc = Double.parseDouble(parts[0]);
        return new Node(loc, parts[1]);
    }

    public List<SubRange> getPathsOut() {
        List<Node> ignoreNodes = new ArrayList<Node>();
        ignoreNodes.add(this);
        return getPathsOut(ignoreNodes, false);
    }

    public List<SubRange> getPathsOut(List<Node> ignoreNodes,
            boolean includeSelf) {
        List<_Node> allPeers = new CircleList<_Node>();
        List<Node> directPeers = getDirectNeighbors(ignoreNodes);

        // 2 hop neighbors
        getindirectNeighbors(ignoreNodes, allPeers);

        // direct peers get preference or peer of peer
        allPeers.removeAll(directPeers); // remove peer of peer entry
        for (Node n : directPeers) {
            allPeers.add(new _Node(n, n.getLocation()));
        }

        // add current node so it can detect when it is the closest
        if (includeSelf)
            allPeers.add(new _Node(this, this.getLocation()));

        Collections.sort(allPeers);

        // calculate the mid points
        for (int i = 0; i < allPeers.size(); i++) {
            allPeers.get(i).setMid(
                    DistanceTools.calcMidPt(allPeers.get(i).getLocation(),
                            allPeers.get(i + 1).getLocation()));
        }

        List<SubRange> rangesList = new CircleList<SubRange>();
        for (int i = 0; i < allPeers.size(); i++) {
            rangesList.add(new SubRange(allPeers.get(i).getNode(), new Edge(
                    allPeers.get(i - 1).getMid()), new Edge(allPeers.get(i)
                    .getMid()), allPeers.get(i).getTieCount(), allPeers.get(i)
                    .getNode() == this));
        }
        // buildRangeLists(rangesList, allPeers, 0);

        return rangesList;
    }

    private class _Node extends INode {
        private Node node;
        private double mid;
        private int ties = 0;

        public _Node(double l) {
            this.location = l;
            this.tie();
        }

        public _Node(Node n, double l) {
            this(l);
            this.node = n;
        }

        public void tie() {
            this.ties++;
        }

        public int getTieCount() {
            return this.ties;
        }

        public Node getNode() {
            return this.node;
        }

        public void setMid(double m) {
            this.mid = m;
        }

        public double getMid() {
            return this.mid;
        }

        @Override
        public String toString() {
            return this.getLocation() + " (" + this.getNode().getLocation()
                    + ")";
        }
    }
}
