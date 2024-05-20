package com.general.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class OpenFile {

    FileInputStream fs;
    InputStreamReader in;
    BufferedReader br;

    protected void openfile(final String filepath) {
        File f = new File(filepath);

        try {
            this.fs = new FileInputStream(f);
            this.in = new InputStreamReader(this.fs);
            this.br = new BufferedReader(this.in);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected void closeConnections() {
        try {
            if (this.fs != null) {
                this.fs.close();
            }
            if (this.in != null) {
                this.in.close();
            }
            if (this.br != null) {
                this.br.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
