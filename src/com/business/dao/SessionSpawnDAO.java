package com.business.dao;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import expectj.ExpectJ;
import expectj.Spawn;

public class SessionSpawnDAO {
    private ExpectJ expectJ;
    private JSch jsch;
    private Session session;
    private Spawn spawn;
    private String errorStr;
    
    public SessionSpawnDAO(ExpectJ expectJ, JSch jsch,Session session, Spawn spawn, String errorStr){
    	this.expectJ = expectJ;
    	this.jsch = jsch;
    	this.session = session;
    	this.spawn = spawn;
    	this.errorStr = errorStr;
    }
    
	public ExpectJ getExpectJ() {
		return expectJ;
	}
	public void setExpectJ(ExpectJ expectJ) {
		this.expectJ = expectJ;
	}
	public JSch getJsch() {
		return jsch;
	}
	public void setJsch(JSch jsch) {
		this.jsch = jsch;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Spawn getSpawn() {
		return spawn;
	}
	public void setSpawn(Spawn spawn) {
		this.spawn = spawn;
	}

	public String getErrorStr() {
		return errorStr;
	}
	public void setErrorStr(String errorStr) {
		this.errorStr = errorStr;
	}
}
