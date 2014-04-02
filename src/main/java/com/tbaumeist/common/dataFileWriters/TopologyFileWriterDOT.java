package com.tbaumeist.common.dataFileWriters;

import java.io.OutputStream;
import java.util.logging.Logger;

import com.tbaumeist.common.Node;
import com.tbaumeist.common.Topology;

public class TopologyFileWriterDOT {
    
    private static final Logger LOGGER = Logger.getLogger(TopologyFileWriterDOT.class
            .getName());

    public void writeDot(Topology top, OutputStream output) throws Exception {
        try {
            
            //LOGGER.info("Writing DOT formatted connections to output stream.");
            
            output.write("digraph G {\n".getBytes("UTF-8"));
            
            /*
             * Write every connection; undirected edges are two directed edges.
             */

            for (Node from : top.getAllNodes()) {
                for (Node to : from.getDirectNeighbors()) {
                    StringBuilder b = new StringBuilder();
                    b.append("\"");
                    b.append(from.getLocation());
                    b.append(" ");
                    b.append(from.getID());
                    b.append("\" -> \"");
                    b.append(to.getLocation());
                    b.append(" ");
                    b.append(to.getID());
                    b.append("\"\n");

                    output.write(b.toString().getBytes("UTF-8"));
                }
            }

            output.write("}\n".getBytes("UTF-8"));
            output.flush();

            LOGGER.info("Finished writing DOT formatted connections to output stream.");
            
        } catch (Exception e) {
            throw new Exception("Could not write DOT graph to output stream: " + e.getMessage());
        }
    }
}
