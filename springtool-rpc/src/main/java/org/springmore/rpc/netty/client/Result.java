package org.springmore.rpc.netty.client;

import io.netty.channel.Channel;

/**
 * 结果集接口
 * 
 * @author 陈浩
 * @date 2015年7月12日
 */
public interface Result {

	public static final String RESULT = "result";

	public static final String RESULT_MAP = "resultMap";
	
	public static final String COUNTER = "counter";
	
	/**
	 * 获取消息
	 * @return
	 * @throws InterruptedException
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	<T> T get() throws InterruptedException;
	
	/**
	 * 存放消息
	 * @param message
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	void set(Object message);
	
	
	void setConnectFactory(ConnectFactory connectFactory);
	
	void setConnection(Channel connection);
	
	ConnectFactory getConnectFactory();
	
}
