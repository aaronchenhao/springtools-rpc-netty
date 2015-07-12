package org.springmore.rpc.netty.client.sc;

import org.springmore.rpc.netty.client.BaseResult;

/**
 * 同步结果类
 * 
 * @author 陈浩
 * @date 2015-7-12
 * 
 */
public class ShortResult extends BaseResult{
	
	/**
	 * 获取返回信息
	 * 
	 * @return
	 * @author 陈浩
	 * @throws InterruptedException 
	 * @date 2015年7月12日
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get() throws InterruptedException {
		connectFactory.close(connection);
		return (T) message;
	}

	/**
	 * 设置消息
	 * 
	 * @param message
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	@Override
	public void set(Object message) {
		this.message = message;
	}
}
