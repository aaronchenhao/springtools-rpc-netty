package org.springmore.rpc.netty.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;



public class TextLineServer {
    private static final Logger logger = Logger.getLogger(TextLineServer.class);
    private static final String IP = "localhost";
    private static final int PORT = 9999;
    /**用于分配处理业务线程的线程组个数 */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2; //默认
    /** 业务出现线程大小*/
    protected static final int BIZTHREADSIZE = 1;
    /*
 * NioEventLoopGroup实际上就是个线程池,实际上bossGroup中有多个NioEventLoop线程，每个NioEventLoop绑定一个端口，也就是说，如果程序只需要监听1个端口的话，bossGroup里面只需要有一个NioEventLoop线程就行了。
 * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
 * 每一个NioEventLoop负责处理m个Channel,
 * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
 */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);


    public TextLineServer() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
//                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new NettyHandler());
            }
        });

        try {
            b.bind(IP, PORT).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("TCP服务器已启动");
        System.out.println("--------------------------------------------------");
		System.out.println("Server Started");
		System.out.println("--------------------------------------------------");
	}
	
	public static void main(String[] args) {
		TextLineServer server = new TextLineServer();
		
	}
}
