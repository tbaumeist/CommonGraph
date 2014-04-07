package com.tbaumeist.common.dataFileReaders;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;

import com.tbaumeist.common.Node;
import com.tbaumeist.common.Topology;


public class TopologyFileReaderGML extends ITopologyFileReader {

    private static final Logger LOGGER = Logger.getLogger(TopologyFileReaderGML.class
            .getName());
    
    @Override
    public boolean canRead(InputStream topInput) throws Exception {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    topInput));
            String line = "";
            while((line = in.readLine().trim())!= null )
            {
                // read input until none emtpy line
                if(!line.isEmpty())
                    break;
            }
            in.close();
            boolean canRead =  line.toLowerCase().startsWith("graph");
            LOGGER.info("Can read file type " + this.getClass().getSimpleName() + ": " + canRead);
            
            return canRead;
        } catch (Exception ex) {
            throw new Exception(
                    "Error reading topology file. Improperly formatted. "
                            + ex.getMessage());
        }
    }

    @Override
    public Topology readFromFile(InputStream topInput) throws Exception {
        Topology topology = new Topology();
        readFile(topology, topInput);
        topology.clearOneWayConnections();
        return topology;
    }

    private void readFile(Topology topology, InputStream topInput)
            throws Exception {
        try {
            HashMap<Integer, Node> allNodes = new HashMap<Integer, Node>();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    topInput));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.equals("node")) {
                    topology.addNode(processNode(in, allNodes));
                } else if (line.equals("edge")) {
                    processEdge(in, allNodes);
                }
            }
        } catch (Exception ex) {
            throw new Exception(
                    "Error reading topology file. Improperly formatted. "
                            + ex.getMessage());
        }
    }

    private Node processNode(BufferedReader in, HashMap<Integer, Node> allNodes)
            throws Exception {
        DataSectionReader reader = new DataSectionReader();
        Node node = null;
        Integer id = -1;
        String line;
        while ((line = reader.readNextLine(in)) != null) {
            if (line.startsWith("label")) {
                String[] locAndId = line.split("\"")[1].split(" ");
                node = new Node(Double.parseDouble(locAndId[0]), locAndId[1]);
            } else if (line.startsWith("id")) {
                id = Integer.parseInt(line.split(" ")[1]);
            }
        }
        allNodes.put(id, node);
        return node;
    }

    private void processEdge(BufferedReader in, HashMap<Integer, Node> allNodes)
            throws Exception {
        DataSectionReader reader = new DataSectionReader();
        Integer sourceId = -1;
        Integer targetId = -1;
        String line;
        while ((line = reader.readNextLine(in)) != null) {
            if (line.startsWith("source")) {
                sourceId = Integer.parseInt(line.split(" ")[1]);
            } else if (line.startsWith("target")) {
                targetId = Integer.parseInt(line.split(" ")[1]);
            }
        }

        Node sourceNode = allNodes.get(sourceId);
        Node targetNode = allNodes.get(targetId);
        sourceNode.addNeighbor(targetNode);
        targetNode.addNeighbor(sourceNode);
    }
}
