package com.tbaumeist.common;

/*
 * Interface for a node type object
 */
public abstract class INode implements Comparable<Object> {
    protected double location;
    protected String id = "";

    public double getLocation() {
        return this.location;
    }

    public String getID() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;

        if (!(obj instanceof INode))
            return false;
        INode node = (INode) obj;
        if (node.getID().isEmpty() || getID().isEmpty())
            return node.getLocation() == getLocation();
        return node.getLocation() == getLocation()
                && node.getID().equals(getID());
    }

    @Override
    public int hashCode() {
        return new Double(getLocation()).hashCode();
    }

    public int compareTo(Object obj) {
        if (obj == null)
            return 1;
        if (!(obj instanceof INode))
            return 1;

        INode node = (INode) obj;
        return new Double(getLocation()).compareTo(new Double(node
                .getLocation()));
    }

    @Override
    public String toString() {
        return this.getLocation() + "";
    }
}
