package com.tbaumeist.common.Utils;

import java.io.File;

public class PathBuilder {

    public static String path(String... directories ){
        StringBuilder b = new StringBuilder();
        for(String dir : directories){
            b.append(dir);
            if(!dir.endsWith(File.separator))
                b.append(File.separator);
        }
        return b.toString();
    }
}
