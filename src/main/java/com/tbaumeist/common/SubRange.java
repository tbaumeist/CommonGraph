package com.tbaumeist.common;

import java.util.ArrayList;
import java.util.List;

public class SubRange implements Comparable<Object> {

    private Node toNode;
    private Edge rangeStart, rangeStop;
    private boolean isRetry = false;
    private int tieCount = 0;
    private boolean isSelfRoute = false;

    private final static String DELIMITER = "@@";

    public SubRange(Node toNode, Edge rangeStart, Edge rangeStop, int tieCount,
            boolean isSelfRoute) {
        this(toNode, rangeStart, rangeStop);
        this.tieCount = tieCount;
        this.isSelfRoute = isSelfRoute;
    }

    public SubRange(Node toNode, Edge rangeStart, Edge rangeStop) {
        this.toNode = toNode;
        this.rangeStart = rangeStart;
        this.rangeStop = rangeStop;
        this.tieCount++;
    }

    public boolean isSelfRoute() {
        return this.isSelfRoute;
    }

    public Node getNode() {
        return this.toNode;
    }

    public Edge getStart() {
        return this.rangeStart;
    }

    public double getStartLocation() {
        return this.rangeStart.getLocation();
    }

    public int getTieCount() {
        return this.tieCount;
    }

    public boolean getIsRetry() {
        return this.isRetry;
    }

    public void setIsRetry(boolean b) {
        this.isRetry = b;
    }

    public Edge getStop() {
        return this.rangeStop;
    }

    public double getStopLocation() {
        return this.rangeStop.getLocation();
    }

    public boolean containsPoint(double pt) {
        if (!wrapsAround())
            return this.rangeStart.getLocation() <= pt
                    && pt < this.rangeStop.getLocation();
        return this.rangeStart.getLocation() <= pt
                || pt < this.rangeStop.getLocation();
    }

    public boolean overlaps(SubRange range) {
        if (range == null)
            return false;

        if (!edgesOverlap(this, range).isEmpty())
            return true;
        if (!edgesOverlap(range, this).isEmpty())
            return true;

        return false;
    }

    public boolean areAdjacent(SubRange range) {
        if (range == null)
            return false;

        if (this.getStart().equals(range.getStop()))
            return true;
        if (this.getStop().equals(range.getStart()))
            return true;

        return false;
    }

    public SubRange addAdjacentRanges(SubRange range) {
        if (this.getStart().equals(range.getStop()))
            this.rangeStart = range.getStart();
        if (this.getStop().equals(range.getStart()))
            this.rangeStop = range.getStop();

        return this;
    }

    public boolean isEntireRange() {
        return this.rangeStart.equals(this.rangeStop);
    }

    public List<SubRange> splitRangeOverMe(SubRange range) {
        List<SubRange> ranges = new ArrayList<SubRange>();

        if (range.isEntireRange()) { // special case
            ranges.add(new SubRange(range.getNode(), this.rangeStart,
                    this.rangeStop, range.tieCount, range.isSelfRoute()));
            return ranges;
        }

        List<Edge> edges = edgesOverlap(this, range);
        edges.remove(range.rangeStart);
        edges.remove(range.rangeStop);

        Edge prev = range.rangeStart;
        for (Edge d : edges) {
            ranges.add(new SubRange(range.getNode(), prev, d, range.tieCount,
                    range.isSelfRoute()));
            prev = d;
        }
        ranges.add(new SubRange(range.getNode(), prev, range.rangeStop,
                range.tieCount, range.isSelfRoute()));

        return ranges;
    }

    public SubRange getIntersection(SubRange rr) {
        Edge start = (this.getStartLocation() > rr.getStartLocation()) ? this.rangeStart
                : rr.rangeStart;
        Edge stop;
        if (this.wrapsAround() && !rr.wrapsAround())
            stop = rr.rangeStop;
        else if (!this.wrapsAround() && rr.wrapsAround())
            stop = this.rangeStop;
        else
            stop = (this.getStopLocation() < rr.getStopLocation()) ? this.rangeStop
                    : rr.rangeStop;

        return new SubRange(this.toNode, start, stop, this.tieCount,
                this.isSelfRoute());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[ ");
        s.append(this.rangeStart);
        s.append(", ");
        s.append(this.rangeStop);
        s.append(" )");
        return s.toString();
    }

    public String serialize() {
        StringBuilder b = new StringBuilder();

        b.append(this.toNode.serialize());
        b.append(DELIMITER);
        b.append(this.rangeStart.serialize());
        b.append(DELIMITER);
        b.append(this.rangeStop.serialize());
        b.append(DELIMITER);
        b.append(this.isRetry);
        b.append(DELIMITER);
        b.append(this.tieCount);
        b.append(DELIMITER);
        b.append(this.isSelfRoute);
        b.append(DELIMITER);

        return b.toString();
    }

    public static SubRange deserialize(String s) {
        String[] parts = s.split(DELIMITER);
        Node n = Node.deserialize(parts[0]);
        Edge start = Edge.deserialize(parts[1]);
        Edge stop = Edge.deserialize(parts[2]);
        Boolean retry = Boolean.parseBoolean(parts[3]);
        int tie = Integer.parseInt(parts[4]);
        Boolean selfRoute = Boolean.parseBoolean(parts[5]);

        SubRange r = new SubRange(n, start, stop, tie, selfRoute);
        r.setIsRetry(retry);

        return r;
    }

    @Override
    public int compareTo(Object obj) {
        if (obj == null)
            return 1;
        if (!(obj instanceof SubRange))
            return 1;

        SubRange r = (SubRange) obj;
        return new Double(this.getStartLocation()).compareTo(new Double(r
                .getStartLocation()));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SubRange))
            return false;
        SubRange r = (SubRange) obj;

        return this.rangeStart.equals(r.rangeStart)
                && this.rangeStop.equals(r.rangeStop);
    }

    private List<Edge> edgesOverlap(SubRange r1, SubRange r2) {
        List<Edge> overlaps = new ArrayList<Edge>();
        if (r1.wrapsAround() && r2.wrapsAround()) {
            if (r1.rangeStart.getLocation() >= r2.rangeStart.getLocation()
                    && r1.rangeStart.getLocation() < r2.rangeStop.getLocation() + 1)
                overlaps.add(r1.rangeStart); // r1 front is in r2 ranges
            if (r1.rangeStop.getLocation() + 1 > r2.rangeStart.getLocation()
                    && r1.rangeStop.getLocation() <= r2.rangeStop.getLocation())
                overlaps.add(r1.rangeStop); // r1 end is in r2 ranges
        } else if (r2.wrapsAround()) {
            if (r1.rangeStart.getLocation() >= r2.rangeStart.getLocation()
                    || r1.rangeStart.getLocation() < r2.rangeStop.getLocation())
                overlaps.add(r1.rangeStart); // r1 front is in r2 ranges
            if (r1.rangeStop.getLocation() > r2.rangeStart.getLocation()
                    || r1.rangeStop.getLocation() <= r2.rangeStop.getLocation())
                overlaps.add(r1.rangeStop); // r1 end is in r2 ranges
        } else {
            // normal conditions
            if (r1.rangeStart.getLocation() >= r2.rangeStart.getLocation()
                    && r1.rangeStart.getLocation() < r2.rangeStop.getLocation())
                overlaps.add(r1.rangeStart); // r1 front is in r2 ranges
            if (r1.rangeStop.getLocation() > r2.rangeStart.getLocation()
                    && r1.rangeStop.getLocation() <= r2.rangeStop.getLocation())
                overlaps.add(r1.rangeStop); // r1 end is in r2 ranges
        }
        return overlaps;
    }

    private boolean wrapsAround() {
        return this.rangeStart.getLocation() >= this.rangeStop.getLocation();
    }

}
