package com.general.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class CreateReadFiles {

    private FileWriter fw = null;
    private PrintWriter out = null;
    private FileInputStream fstream = null;

    public void createResultFiles(final ArrayList<String> resultData, final String nameOutput) {
        try {
            this.fw = new FileWriter(nameOutput);
            this.out = new PrintWriter(this.fw);
            Iterator<String> it = resultData.iterator();
            while (it.hasNext()) {
                this.out.write(it.next());
            }
            this.out.close();
            this.fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: File To Create the output file" + ioe.getMessage());
        }
    }

    public void createResultFiles(final StringBuffer resultData, final String nameOutput) {
        try {
            this.fw = new FileWriter(nameOutput);
            this.out = new PrintWriter(this.fw);
            this.out.write(resultData.toString());
            this.fw.close();
        } catch (IOException ioe) {
            System.err.println("IOException: File To Create the output file" + ioe.getMessage());
        }
    }
    
    public void createResultFiles(final StringBuffer resultData, final String nameOutput, boolean append) {
    	if(append){
    		try {
                this.fw = new FileWriter(nameOutput, append);
                this.out = new PrintWriter(this.fw);
                this.out.write(resultData.toString());
                this.fw.close();
            } catch (IOException ioe) {
                System.err.println("IOException: File To Create the output file" + ioe.getMessage());
            }
    	}else{
    		createResultFiles(resultData, nameOutput);
    	}        
    }

    public FileInputStream readFile(final String filename) {
        this.fstream = null;
        try {
            this.fstream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            // System.out.println(e);
            System.out.println("The file " + filename + " does not exist.");
            return null;
        }
        return this.fstream;
    }

    public void Close() {
        if (this.fstream != null) {
            try {
                this.fstream.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public void deleteFile(final String filename) {
        File f = new File(filename);
        if (f.exists()) {
            f.delete();
        }
    }

    public boolean existFile(final String filename) {
        File f = new File(filename);
        return f.exists();
    }
}