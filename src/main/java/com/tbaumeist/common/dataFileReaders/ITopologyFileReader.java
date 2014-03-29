package com.tbaumeist.common.dataFileReaders;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.tbaumeist.common.Topology;


public abstract class ITopologyFileReader {

    public boolean canRead(String fileName) throws Exception {
        return canRead(getFileStream(fileName));
    }

    protected abstract boolean canRead(InputStream topInput) throws Exception;

    public Topology readFromFile(String fileName) throws Exception {
        return readFromFile(getFileStream(fileName));
    }

    protected abstract Topology readFromFile(InputStream topInput)
            throws Exception;

    private InputStream getFileStream(String fileName) throws Exception {
        File top = new File(fileName);
        if (!top.exists())
            throw new Exception("Unable to find the topology file: " + fileName);

        return new FileInputStream(top);
    }
}
