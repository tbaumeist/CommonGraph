package com.tbaumeist.common;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.tbaumeist.common.testing.TestHelper;
import com.tbaumeist.common.utils.CircleList;
import com.tbaumeist.common.dataFileReaders.TopologyFileReaderManager;

public class TestNode {
    
    private Node node8 = new Node(0.8, "4722");
    private Node node7 = new Node(0.7, "4707");
    private Node node64 = new Node(0.64, "4698");
    private Node node68 = new Node(0.68, "4704");
    
    private Node node5 = new Node(0.5, "4677");
    private Node node78 = new Node(0.78, "4719");
    private Node node82 = new Node(0.82, "4725");
    private Node node72 = new Node(0.72, "4710");
    private Node node54 = new Node(0.54, "4683");
    private Node node76 = new Node(0.76, "4716");
    private Node node62 = new Node(0.62, "4695");

    public TestNode() {
    }

    @Test
    public void oneHopAway() throws Exception {
        TopologyFileReaderManager topReader = new TopologyFileReaderManager();
        Topology top = topReader.readFromFile(TestHelper
                .getResourcePath("topology-50-4-full.dot"));
        assertTrue(top != null);
        
        Node node66 = top.findNode(0.66, "4701");
        assertTrue(node66 != null);
        
        List<Node> directNeighbors = node66.getDirectNeighbors();
        List<Node> oneHopNeighbors = node66.getAllNeighborsInXHops(1);
        
        // should be the same
        assertTrue(directNeighbors.containsAll(oneHopNeighbors));
        assertTrue(oneHopNeighbors.containsAll(directNeighbors));
        
        List<Node> expected = new CircleList<Node>();
        expected.add(node8);
        expected.add(node7);
        expected.add(node64);
        expected.add(node68);
        
        assertTrue(oneHopNeighbors.containsAll(expected));
    }
    
    @Test
    public void twoHopAway() throws Exception {
        TopologyFileReaderManager topReader = new TopologyFileReaderManager();
        Topology top = topReader.readFromFile(TestHelper
                .getResourcePath("topology-50-4-full.dot"));
        assertTrue(top != null);
        
        Node node66 = top.findNode(0.66, "4701");
        assertTrue(node66 != null);
        
        List<Node> twoHopNeighbors = node66.getAllNeighborsInXHops(2);
        
        List<Node> expected = new CircleList<Node>();
        expected.add(node8);
        expected.add(node7);
        expected.add(node64);
        expected.add(node68);
        
        expected.add(node5);
        expected.add(node78);
        expected.add(node82);
        expected.add(node72);
        expected.add(node54);
        expected.add(node76);
        expected.add(node62);
        
        assertTrue(twoHopNeighbors.containsAll(expected));
        assertTrue(expected.containsAll(twoHopNeighbors));
    }

}
