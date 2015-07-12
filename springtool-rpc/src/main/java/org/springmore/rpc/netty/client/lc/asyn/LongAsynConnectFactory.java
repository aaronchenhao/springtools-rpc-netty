package org.springmore.rpc.netty.client.lc.asyn;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;

import org.springmore.rpc.netty.client.ConnectFactory;
import org.springmore.rpc.netty.client.Result;
import org.springmore.rpc.netty.client.sc.ShortClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 长连接Factory.
 * 通过spring初始化
 * @author 陈浩
 * @date 2015-7-12
 */
public class LongAsynConnectFactory implements ConnectFactory{
    private static final Logger log = Logger.getLogger(LongAsynConnectFactory.class);
	/**
	 * 连接池
	 */
	private final List<Channel> connectionPool = new ArrayList<Channel>();
	

	/**
	 * 连接池默认初始化连接数量
	 */
	private final static int DEFAULT_POOL_SIZE = 10;

	
	/**
	 * 线程池大小
	 */
	private int poolSize = DEFAULT_POOL_SIZE;
	
	private String host;
	
	private int port;


    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private AttributeKey<Result> RESULT = new AttributeKey<Result>(Result.RESULT);
    private AttributeKey<AtomicLong> COUNTER = new AttributeKey<AtomicLong>(Result.COUNTER);
    private AttributeKey<ConcurrentHashMap> RESULT_MAP = new AttributeKey<ConcurrentHashMap>(Result.RESULT_MAP);
	
	/**
	 * 连接超时时间
	 */
	private int connectTimeoutMillis;
	
	private AtomicInteger counter = new AtomicInteger();
	
	/**
	 * 编码解码工厂
	 */
    private org.springmore.rpc.netty.client.ProtocolCodecFactory protocolCodecFactory;
	
	private LongAsynConnectFactory(){
		
	}	
		
	/**
	 * 初始化
	 * 
	 * @author 陈浩
	 * @date 2015-7-12
	 */
	private void init() {
		// 设置连接参数
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                Object encode = protocolCodecFactory.getEncoder();
                Object decode = protocolCodecFactory.getDecoder();
//                pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
//                pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                if(decode  instanceof StringDecoder) {
                    StringDecoder decoder = (StringDecoder)decode;
                    pipeline.addLast(decoder);
                }else if(decode  instanceof ObjectDecoder) {
                    pipeline.addLast((ObjectDecoder) decode);
                }
                if(encode  instanceof StringEncoder) {
                    StringEncoder encoder = (StringEncoder)encode;
                    pipeline.addLast(encoder);
                }else if(encode  instanceof ObjectEncoder) {
                    pipeline.addLast((ObjectEncoder)encode);
                }
                pipeline.addLast(new ReadTimeoutHandler(connectTimeoutMillis/1000));
                pipeline.addLast("handler", new ShortClientHandler(RESULT));
            }
        });
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        try {
            initConnection(poolSize);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("initConnection false! "+e.getMessage());
        }
    }
	
	/**
	 * 初始化连接
	 * @param size
	 * @author 陈浩
	 * @date 2015-7-12
	 */
	private void initConnection(int size)  throws InterruptedException{
		for (int i = 0; i < size; i++) {
			// 连接服务端
            // 连接服务端
            ChannelFuture f = bootstrap.connect(host, port).sync();
            Channel channel = f.channel();
			//放入counter
            channel.attr(COUNTER).set(new AtomicLong());
			//放入map
			ConcurrentHashMap<Long, Result> resultMap = new ConcurrentHashMap<Long, Result>(100,0.75f,16);
            channel.attr(RESULT_MAP).set(resultMap);
			connectionPool.add(channel);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	/**
	 * 获取连接
	 * 
	 * @author 陈浩
	 * @date 2015-1-15
	 * @param
	 * @return
	 * @throws InterruptedException 
	 */
	@Override
	public Channel getConnection() throws InterruptedException {
		int count = counter.incrementAndGet();
		if(count>1000000){
			counter.set(0);
		}
		int index = count%connectionPool.size();
        Channel connection = connectionPool.get(index);
		return connection;
	}
	
	
	
	/**
	 * 关闭连接
	 * @param connection
	 * @throws InterruptedException
	 * @author 陈浩
	 * @date 2015-7-12
	 */
	@Override
	public void close(Channel connection) throws InterruptedException{
		//connection.getSession().getCloseFuture().awaitUninterruptibly();
	}
	
	/**
	 * 关闭资源connector
	 * 
	 * @author 陈浩
	 * @date 2015-7-12
	 */
	@Override
	public void shutdown(){
        group.shutdownGracefully();
	}


	@Override
	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void setConnectTimeoutMillis(int connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
	}

    @Override
    public void setProtocolCodecFactory(org.springmore.rpc.netty.client.ProtocolCodecFactory protocolCodecFactory) {
        this.protocolCodecFactory = protocolCodecFactory;
    }

    @Override
    public AttributeKey<Result> getRESULT() {
        return RESULT;
    }

    @Override
    public AttributeKey<AtomicLong> getCOUNTER() {
        return COUNTER;
    }

    @Override
    public AttributeKey<ConcurrentHashMap> getRESULT_MAP() {
        return RESULT_MAP;
    }


}
