package org.springmore.rpc.netty.client;

import java.nio.ByteBuffer;

import io.netty.channel.Channel;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * BaseResult
 * 
 * @author 陈浩
 * @date 2015-7-12
 * 
 */
public abstract class BaseResult implements Result{	

	protected ConnectFactory connectFactory;

	protected Channel connection;

	/**
	 * 信息
	 */
	protected Object message;

	/**
	 * 是否接收完成
	 */
	protected boolean done;

	

	/**
	 * 同步获取返回信息
	 * 
	 * @author 陈浩
	 * @throws InterruptedException 
	 * @date 2015年7月12日
	 */
	@SuppressWarnings("unchecked")
	protected synchronized <T> T sybGet() throws InterruptedException {
		// 等待消息返回
		// 必须要在同步的情况下执行
		if (!done) {
			wait();
		}
		T result = null;
		if (message instanceof IoBuffer) {
			IoBuffer buf = (IoBuffer) message;
			ByteBuffer bf = buf.buf();
			byte[] data = new byte[bf.limit()];
			bf.get(data);
			result = (T) data;
		} else {
			result = (T) message;
		}
		return result;
	}

	
	
	/**
	 * 同步设置消息
	 * @param message
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	protected synchronized void synSet(Object message) {
		this.message = message;
		this.done = true;
		notify();
	}	

	@Override
	public void setConnectFactory(ConnectFactory connectFactory) {
		this.connectFactory = connectFactory;
	}

	@Override
	public void setConnection(Channel connection) {
		this.connection = connection;
	}

	@Override
	public ConnectFactory getConnectFactory() {
		return connectFactory;
	}



	@Override
	public abstract <T> T get() throws InterruptedException;



	@Override
	public abstract void set(Object message);


}
