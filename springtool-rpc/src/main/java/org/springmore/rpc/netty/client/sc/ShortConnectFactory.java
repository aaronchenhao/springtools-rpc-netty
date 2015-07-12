package org.springmore.rpc.netty.client.sc;

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
import org.springmore.rpc.netty.client.ConnectFactory;
import org.springmore.rpc.netty.client.ProtocolCodecFactory;
import org.springmore.rpc.netty.client.Result;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 短连接Factory.
 * 通过spring初始化
 * @author 陈浩
 * @date 2017-7-12
 */
public class ShortConnectFactory implements ConnectFactory{
	
	private String host;
	private int port;

	
	/**
	 * 连接超时时间
	 */
	private int connectTimeoutMillis;
	
	/**
	 * 编码解码工厂
	 */
	private ProtocolCodecFactory protocolCodecFactory;
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    private AttributeKey<Result> RESULT = new AttributeKey<Result>(Result.RESULT);
    private AttributeKey<Result> COUNTER = new AttributeKey<Result>(Result.COUNTER);
	private ShortConnectFactory(){
	}

    @Override
    public AttributeKey<Result> getRESULT() {
        return RESULT;
    }

    @Override
    public AttributeKey<AtomicLong> getCOUNTER() {
        return null;
    }

    @Override
    public AttributeKey<ConcurrentHashMap> getRESULT_MAP() {
        return null;
    }


    /**
	 * 初始化
	 * 
	 * @author 陈浩
	 * @date 2015-7-12
	 */
	private void init() {
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
                if(decode  instanceof StringDecoder ) {
                    StringDecoder decoder = (StringDecoder)decode;
                    pipeline.addLast(decoder);
                }else if(decode  instanceof ObjectDecoder) {
                    pipeline.addLast((ObjectDecoder) decode);
                }
                if(encode  instanceof StringEncoder ) {
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



    }
	
	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
		

	/**
	 * 获取连接
	 * 
	 * @author 陈浩
	 * @date 2015-7-12
	 * @param
	 * @return Channel
	 * @throws InterruptedException 
	 */
	@Override
	public Channel getConnection() throws InterruptedException {
        ChannelFuture f=bootstrap.connect(host, port).sync();
        Channel channel = f.channel();
        return channel;
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
//		connection.getSession().getCloseFuture().awaitUninterruptibly();
        connection.closeFuture().sync();
	}
	
	/**
	 * 关闭资源group,优雅释放线程资源
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
	public void setProtocolCodecFactory(ProtocolCodecFactory protocolCodecFactory) {
		this.protocolCodecFactory = protocolCodecFactory;
	}



}
