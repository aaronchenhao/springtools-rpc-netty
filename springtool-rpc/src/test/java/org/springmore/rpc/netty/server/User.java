package org.springmore.rpc.netty.server;

public class User implements java.io.Serializable{
    private static final long serialVersionUID = 7590999461767050471L;
	private long userId;
	
	public User(){}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
}
