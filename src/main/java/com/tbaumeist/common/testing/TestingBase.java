package com.tbaumeist.common.testing;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Level;

import com.tbaumeist.common.logging.LoggingManager;


public class TestingBase {

    private PrintStream originalOut = null;
    private PrintStream originalErr = null;

    public TestingBase() {
        // run silent
        this.silenceStdOut();
        try {
            LoggingManager.initialize(Level.ALL);
        } catch (Exception ex) {
        }
    }

    protected void silenceStdOut() {
        this.originalOut = System.out;
        this.originalErr = System.err;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // DO NOTHING
            }
        }));
        System.setErr(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                // DO NOTHING
            }
        }));
    }

    protected void restoreStdOut() {
        if (this.originalOut == null || this.originalErr == null) {
            return;
        }
        System.setOut(this.originalOut);
        System.setErr(this.originalErr);
    }

    protected void printTestName(String name) {
        System.out.println();
        System.out.println("*****************************************");
        System.out.println(name);
        System.out.println("*****************************************");
    }

}
