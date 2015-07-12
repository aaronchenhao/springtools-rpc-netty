package org.springmore.rpc.netty.client;

import io.netty.channel.Channel;

import io.netty.util.AttributeKey;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 连接工厂接口
 * @author bypay
 *
 */
public interface ConnectFactory extends InitializingBean{

	/**
	 * 关闭连接
	 * @param connection
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	void close(Channel connection) throws InterruptedException;
	
	/**
	 * 获得连接
	 * @return
	 * @throws InterruptedException
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
    Channel getConnection() throws InterruptedException;
	
	/**
	 * 关闭工厂并释放资源
	 * 
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	void shutdown();
	
	void afterPropertiesSet() throws Exception;
	
	void setHost(String host);
	
	void setPort(int port);
	
	void setConnectTimeoutMillis(int connectTimeoutMillis);
	
	void setProtocolCodecFactory(ProtocolCodecFactory protocolCodecFactory);
	
//	void setReadBufferSize(String readBufferSize);

    AttributeKey<Result> getRESULT();

    AttributeKey<AtomicLong> getCOUNTER();

    AttributeKey<ConcurrentHashMap> getRESULT_MAP();
}
