package com.tbaumeist.common.dataFileReaders;


import java.io.BufferedReader;

public class DataSectionReader {
    private String openDelimiter;
    private String closeDelimiter;
    private int braceCount = 0;

    public DataSectionReader(String open, String close) {
        this.openDelimiter = open;
        this.closeDelimiter = close;
    }

    public DataSectionReader() {
        this("[", "]");
    }

    public String readNextLine(BufferedReader in) throws Exception {
        String line = in.readLine();
        if (line == null)
            return null;

        line = line.trim().toLowerCase();
        if (line.equals(this.openDelimiter)) {
            braceCount++;
        } else if (line.equals(this.closeDelimiter)) {
            braceCount--;
        }

        if (braceCount == 0) {
            return null;
        }
        return line;

    }
}
