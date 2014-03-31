package com.tbaumeist.common.logging;

import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Condense log output formatter.
 */
public class ReallySimpleFormatter extends Formatter {
    // This method is called for every log records
    @Override
    public final String format(final LogRecord rec) {
        StringBuffer buf = new StringBuffer();

        if(rec.getLevel() == Level.SEVERE)
            buf.append(rec.getLevel()).append(' ');

        //buf.append('\t');
        buf.append(formatMessage(rec).replaceAll("\\r\\n|\\r|\\n", "\n\t"));
        buf.append('\n');

        return buf.toString();
    }
}
