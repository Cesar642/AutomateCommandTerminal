package com.business.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.general.files.CreateReadFiles;

public class NewAutomateCommand {

	CreateReadFiles crf = new CreateReadFiles();
	
	public static void main(String[] args) {

		NewAutomateCommand nac = new NewAutomateCommand();
		ArrayList<String> arrayServers = null;
		ArrayList<String> arrayCommands = null;
		if(args.length == 5){
			arrayServers = (ArrayList<String>) nac.readServersList(args[0]);
			arrayCommands = (ArrayList<String>) nac.readCommands(args[1]);
			nac.sendCommands(arrayServers, arrayCommands, args[2], args[3], args[4]);
		}
		else {
            System.err.println("usage: serverListFile commandListFile user password outputFile");
            System.exit(0);
        }
		System.out.println("");
	}
	
	private void sendCommands(List<String> arrayServers, List<String> arrayCommands, String username, String password, String outputFileName){
		String server;
		Thread thread = null;
		String[] serverPassword;
		String splitStr;
		boolean serverPassDetected;;

		for (int i = 0; i < arrayServers.size(); i++) {
			splitStr = "";
			serverPassDetected = false;
			serverPassword = null;
			server = arrayServers.get(i);

			if(server.indexOf(" ") != -1){
				splitStr = " ";
				serverPassDetected = true;
			}
			else if(server.indexOf("\\t") != -1){
				splitStr = "\\t";
				serverPassDetected = true;
			}
			if(serverPassDetected){
				serverPassword = server.split(splitStr);
			}			
			if(serverPassDetected && serverPassword.length == 2){
				thread = new Thread(new RunCommands(arrayCommands, username, password, serverPassword[0], outputFileName, serverPassword[1]), "ThreadCommand" + i + "_" + serverPassword[0]);
			}
			else if (!serverPassDetected){
				thread = new Thread(new RunCommands(arrayCommands, username, password, server, outputFileName, password), "ThreadCommand" + i + "_" + server);
			}
			else{
				System.out.println("***ERROR: server file must be only 1. name or 2. name password");
				System.out.println("***Omitting " + server);
			}
			
			thread.start();
		}
		System.out.println("");
	}

	private List<String> readCommands(String file){
		ArrayList<String> arrayCommands = new ArrayList<>();
        FileInputStream fis = this.crf.readFile(file);
		DataInputStream in = null;
		BufferedReader br = null;
		Pattern lookForCommentPattern = Pattern.compile("^#");
        Matcher lookForCommentMatcher = null;
		if (fis != null) {            
			try {
                // Get the object of DataInputStream
                in = new DataInputStream(fis);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;
				// Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                	lookForCommentMatcher = lookForCommentPattern.matcher(strLine);
                    if (!lookForCommentMatcher.find() && strLine.trim().length() > 0) {
                    	arrayCommands.add(strLine);
                    }
                }
            } catch (Exception e) {// Catch exception if any
                System.err.println("Exception getting data from source file: " + e.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                }

                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                }
            }
        }
		return arrayCommands;
	}
	
	private List<String> readServersList(String file){
		ArrayList<String> arrayServers = new ArrayList<>();
		FileInputStream fis = this.crf.readFile(file);
		DataInputStream in = null;
		BufferedReader br = null;
		if (fis != null) {            
			try {
                // Get the object of DataInputStream
                in = new DataInputStream(fis);
                br = new BufferedReader(new InputStreamReader(in));
                String strLine;
				// Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                	if(!arrayServers.contains(strLine.trim())){
                		arrayServers.add(strLine.trim());
                	}
                }
            } catch (Exception e) {// Catch exception if any
                System.err.println("Exception getting data from source file: " + e.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                }

                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ioe) {
                        System.out.println(ioe.getMessage());
                    }
                }
            }
        }
		return arrayServers;
	}
}
