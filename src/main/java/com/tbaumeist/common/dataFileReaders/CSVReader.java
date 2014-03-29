package com.tbaumeist.common.dataFileReaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CSVReader {
    BufferedReader fileReader = null;
    String[] lineParsed = null;
    String[] headersParsed = null;

    public CSVReader(String fileName) throws Exception {
        this.fileReader = new BufferedReader(new FileReader(new File(fileName)));
        if (!this.readLine())
            throw new Exception("Error reading the file " + fileName);
        this.headersParsed = this.lineParsed;
    }

    public boolean readLine() throws Exception {
        return readLine(-1, "");
    }

    public boolean readLine(int column, String crit) throws Exception {
        do {
            String line = this.fileReader.readLine();
            if (line == null)
                return false;
            line = line.replace(".00,", ",");
            this.lineParsed = line.split(",");
        } while (column > -1 && !this.getColumn(column).trim().equals(crit));
        return true;
    }

    public String getColumn(int index) {
        return this.lineParsed[index];
    }

    public String getHeader(int i) {
        return this.headersParsed[i];
    }

    public int getHeaderCount() {
        return this.headersParsed.length;
    }
}
