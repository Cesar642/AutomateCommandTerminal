package com.business.client;

import java.io.IOException;
import java.util.List;

import com.business.dao.SessionSpawnDAO;
import com.general.files.CreateReadFiles;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import expectj.ExpectJ;
import expectj.Spawn;
import expectj.TimeoutException;

public class RunCommands implements Runnable {

	private final List<String> arrayCommands;
	private final static int timeToSleep = 1000;
	private final String password;
	private final String secondPassword;
	private final String userLogging;
	private final String server;
	private final String outputFileName;
	private volatile Thread t;
	CreateReadFiles crf = new CreateReadFiles();
	
	public RunCommands(List<String> arrayCommands, String userLogging, String password, String server, String outputFileName, String secondPassword){
		this.arrayCommands = arrayCommands;
		this.password = password;
		this.userLogging = userLogging;
		this.server = server;
		this.outputFileName = outputFileName;
		this.secondPassword = secondPassword;
	}
	@Override
	public void run() {
		t = Thread.currentThread();
		while(t == Thread.currentThread()){
			executeList(establishSession());
		}
	}
	
	public void stop(){
		t = null;
	}
	
	private synchronized void printAllOutput(SessionSpawnDAO spawnDao){
//		System.out.println("\n-----------" + Thread.currentThread().getName() + "------------------");
//		System.out.println(spawnDao.getSpawn().getCurrentStandardOutContents());
//		System.out.println("-----------------------------");
//		
//		System.out.println("\n+++++++++++" + Thread.currentThread().getName() + "++++++++++++++++++");
//		System.out.println(spawnDao.getSpawn().getCurrentStandardErrContents());
//		System.out.println("+++++++++++++++++++++++++++++");
//		StringBuffer stb = new StringBuffer(spawnDao.getSpawn().getCurrentStandardOutContents() + "\n");
		StringBuffer stb = new StringBuffer("*************Server:" + this.server + "\n");
		if(spawnDao.getSpawn() != null){
			stb.append(spawnDao.getSpawn().getCurrentStandardOutContents() + "\n");
			this.crf.createResultFiles(stb, outputFileName, true);
		}
		else if(spawnDao.getSpawn() != null && spawnDao.getSpawn().getCurrentStandardErrContents() != null){
//			stb = new StringBuffer(spawnDao.getSpawn().getCurrentStandardErrContents() + "\n");
//			stb = new StringBuffer("*************Server:" + this.server + "\n");
			stb.append(spawnDao.getSpawn().getCurrentStandardErrContents() + "\n");
			this.crf.createResultFiles(stb, outputFileName.substring(0,outputFileName.lastIndexOf(".") - 1) + "err" + outputFileName.substring(outputFileName.lastIndexOf("."), outputFileName.length()), true);
			stop();
		}
		else if(spawnDao.getErrorStr() != null && spawnDao.getErrorStr().length() > 0){
//			stb = new StringBuffer("*************Server:" + this.server + "\n");
			stb.append(spawnDao.getErrorStr() + "\n");
			this.crf.createResultFiles(stb, outputFileName.substring(0,outputFileName.lastIndexOf(".") - 1) + "err" + outputFileName.substring(outputFileName.lastIndexOf("."), outputFileName.length()), true);
			stop();
		}
	}
	
	private synchronized void executeList(SessionSpawnDAO spawnDao) {
		if(spawnDao.getSpawn() != null){
			for (String currentCommand : arrayCommands) {
	            try {                
	            	System.out.println("\n****" + Thread.currentThread().getName() + " command: " + currentCommand);
	            	spawnDao.getSpawn().send(currentCommand + "\r");
	            	sleepTime();
	            	if(currentCommand.indexOf("ssh ") != -1 || currentCommand.indexOf("scp ") != -1){
	            		spawnDao.getSpawn().expect("password:",2000);
	            		spawnDao.getSpawn().send(password + "\r");
	            		sleepTime(2000);
	            	}
//	            	if(currentCommand.indexOf("su - EMR") != -1){
//	            		spawnDao.getSpawn().expect("password:",2000);
//	            		spawnDao.getSpawn().send(secondPassword + "\r");
//	            		sleepTime(2000);
//	            	}
	            	if(currentCommand.indexOf("su - ") == 0){
	            		spawnDao.getSpawn().expect("password:",2000);
	            		spawnDao.getSpawn().send(secondPassword + "\r");
	            		sleepTime(2000);
	            	}
//					spawnDao.getSpawn().expect("[");
//	            	spawnDao.getSpawn().expect("@" + (String.valueOf(server.charAt(server.length() -1)).equalsIgnoreCase("O") || String.valueOf(server.charAt(server.length() -1)).equalsIgnoreCase("S") ? server.substring(0, server.length() - 1) : server));
//	            	System.out.println("***server " + server);
	            	spawnDao.getSpawn().expect("@" + server);
	            }
	            catch (IOException | TimeoutException e) {
	                e.printStackTrace();
	            }
	        }
	        finalizeSession(spawnDao);
		}        
    }
	
    private synchronized void finalizeSession(SessionSpawnDAO spawnDao) {
        try {
        	spawnDao.getSpawn().send("exit\n");
        	sleepTime(2000);
        	spawnDao.getSpawn().send("exit\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (spawnDao.getSpawn() != null) {
            	printAllOutput(spawnDao);
                closeConnections(spawnDao);
                System.gc();
                // System.exit(0);
            }
        }
    }
    
    private synchronized void closeConnections(SessionSpawnDAO spawnDao) {
    	spawnDao.getSpawn().stop();
    	spawnDao.getSession().disconnect();
    	spawnDao.setExpectJ(null);
    	spawnDao.setJsch(null);
    	spawnDao.setSession(null);
    	spawnDao.setSpawn(null);
    	spawnDao = null;
        System.out.println("**** Closed connection");
//        System.exit(0);
        stop();
    }
    
	private synchronized SessionSpawnDAO establishSession() {
        ExpectJ ex = new ExpectJ();
        JSch jsch = new JSch();
        Session session;
        Spawn spawn = null;
        SessionSpawnDAO spawnDao = null;
        try {
            session = jsch.getSession(userLogging, server, 22);
            session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.setConfig("ClientAliveInterval", "60");
            session.setPassword(password);
            session.setTimeout(60000);
            session.connect();
            Channel channel = session.openChannel("shell");
            channel = session.openChannel("shell");
            session.setTimeout(0);
            spawn = ex.spawn(channel);
            sleepTime();
            spawnDao = new SessionSpawnDAO(ex, jsch, session, spawn, null); 
            
            spawnDao.getSpawn().send("bash\r");
            sleepTime(2000);
//            spawnDao.getSpawn().expect("@" + (String.valueOf(server.charAt(server.length() -1)).equalsIgnoreCase("O") || String.valueOf(server.charAt(server.length() -1)).equalsIgnoreCase("S") ? server.substring(0, server.length() - 1) : server));
//            System.out.println("***server " + server);
            spawnDao.getSpawn().expect("@" + server);
//            spawnDao.getSpawn().expect("@");
        } catch (JSchException | IOException e) {
        	System.out.println("Error connecting: " + server);
        	System.out.println("\t" + e.getMessage());
        	spawnDao = new SessionSpawnDAO(null,null,null,null,e.getMessage());
        	printAllOutput(spawnDao);
            e.printStackTrace();
        } catch (TimeoutException e) {
			e.printStackTrace();
			spawnDao = new SessionSpawnDAO(null,null,null,null,e.getMessage());
			printAllOutput(spawnDao);
		}
        return spawnDao;
    }
	
    private void sleepTime() {
        try {
            Thread.sleep(timeToSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    private void sleepTime(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
