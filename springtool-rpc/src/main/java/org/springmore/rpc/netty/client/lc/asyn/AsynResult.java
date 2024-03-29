package org.springmore.rpc.netty.client.lc.asyn;

import org.springmore.rpc.netty.client.BaseResult;

/**
 * 异步result
 * @author 陈浩
 * @date 2015年6月26日
 */
public class AsynResult extends BaseResult{	

	@Override
	public <T> T get() throws InterruptedException {
		T message = sybGet();
		return message;
	}

	@Override
	public void set(Object message) {
		synSet(message);
	}

	
}
