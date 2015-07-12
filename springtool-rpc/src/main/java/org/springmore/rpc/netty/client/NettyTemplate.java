package org.springmore.rpc.netty.client;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.channel.Channel;

import org.apache.mina.core.buffer.IoBuffer;
import org.springmore.rpc.netty.client.lc.asyn.AsynResult;

/**
 * MinaTemplate
 * @author 陈浩
 * @date 2015年7月12日
 */
public class NettyTemplate {

	/**
	 * conncet factory
	 */
	private ConnectFactory connectFactory;

	
	/**
	 * 同步传输对象
	 * @param message
	 * @return
	 * @author 陈浩
	 * @throws InterruptedException 
	 * @date 2015年7月12日
	 */
	@SuppressWarnings("unchecked")
	public <T> T sengObject(T message) throws InterruptedException{
		//获取tcp连接
        Channel connection = connectFactory.getConnection();
		Result result = ResultUtil.getResult(connectFactory, connection);
		//发送信息
		if(result instanceof AsynResult){
			ConcurrentHashMap<Long, Result> resultMap = (ConcurrentHashMap<Long, Result>)
                    connection.attr(connectFactory.getRESULT_MAP()).get();
            AtomicLong counter = (AtomicLong) connection.attr(connectFactory.getCOUNTER()).get();
            long count = counter.incrementAndGet();
			if(count>1000000){
				counter.set(0);
			}
			resultMap.put(count, (Result)result);
			MessageWrapper wrapper = new MessageWrapper();
			wrapper.setMessage(message);
			wrapper.setId(count);
            connection.writeAndFlush(wrapper);
		}else{
            connection.attr(connectFactory.getRESULT()).set(result);
			if(message instanceof byte[]){
				byte[] msg = ( byte[])message;
                connection.writeAndFlush(IoBuffer.wrap(msg));
			}else{
                connection.writeAndFlush(message);
			}
		}
		//同步阻塞获取响应
		T returnMsg = result.get();
		//此处并不是真正关闭连接，而是将连接放回连接池
		connectFactory.close(connection);
		return returnMsg;
	}
	
	public void setConnectFactory(ConnectFactory connectFactory) {
		this.connectFactory = connectFactory;
	}
	
}
