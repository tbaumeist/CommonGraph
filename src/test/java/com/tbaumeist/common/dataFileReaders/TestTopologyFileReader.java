package com.tbaumeist.common.dataFileReaders;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tbaumeist.common.Topology;
import com.tbaumeist.common.testing.TestHelper;

public class TestTopologyFileReader {
    @Test
    public void readerDotGml() throws Exception {
        TopologyFileReaderManager topReader = new TopologyFileReaderManager();

        Topology topologyDot = topReader.readFromFile(TestHelper
                .getResourcePath("topology-50-4-full.dot"));
        assertTrue(topologyDot != null);

        Topology topologyGML = topReader.readFromFile(TestHelper
                .getResourcePath("topology-50-4-full.gml"));
        assertTrue(topologyGML != null);

        Topology topologyNull = topReader.readFromFile(TestHelper
                .getResourcePath("topology-50-4.dot"));
        assertTrue(topologyNull == null);

        String topDot = topologyDot.toString();
        String topGml = topologyGML.toString();
        assertTrue(topDot.equals(topGml));
    }
}
