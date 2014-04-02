package com.tbaumeist.common;

import com.tbaumeist.common.utils.DistanceTools;


public class Edge {
    private double location;

    public Edge(double loc) {
        this.location = DistanceTools.round(loc);
    }

    public Edge(Edge e) {
        this.location = e.getLocation();
    }

    public double getLocation() {
        return this.location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;

        if (!(obj instanceof Edge))
            return false;
        Edge node = (Edge) obj;
        return node.getLocation() == getLocation();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.getLocation());
        return s.toString();
    }

    public String serialize() {
        return toString();
    }

    public static Edge deserialize(String s) {
        return new Edge(Double.parseDouble(s));
    }
}
