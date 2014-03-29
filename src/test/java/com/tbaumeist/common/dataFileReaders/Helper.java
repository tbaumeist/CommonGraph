package com.tbaumeist.common.dataFileReaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.tbaumeist.common.Utils.PathBuilder;


public class Helper {
    private final static String resourcePath = PathBuilder.path("src","test","resources");

    public static String getResourcePath() {
        return PathBuilder.path(new java.io.File("").getAbsolutePath(), resourcePath);
    }

    public static String getResourcePath(String resource) {
        return getResourcePath() + resource;
    }

    public static int possiblePairs(int nodeCount) {
        int n = (nodeCount * nodeCount) - nodeCount;
        return n / 2;
    }

    public static boolean filesAreEqual(String f1Name, String f2Name)
            throws Exception {
        File f1 = new File(f1Name);
        File f2 = new File(f2Name);

        if (!f1.exists())
            return false;
        if (!f2.exists())
            return false;
        if (f1.length() != f2.length())
            return false;

        BufferedReader in1 = new BufferedReader(new FileReader(f1));
        BufferedReader in2 = new BufferedReader(new FileReader(f2));

        String line1, line2;
        while ((line1 = in1.readLine()) != null
                && (line2 = in2.readLine()) != null) {
            if (!line1.equals(line2)) {
                in1.close();
                in2.close();
                return false;
            }
        }
        in1.close();
        in2.close();
        return true;
    }
}
