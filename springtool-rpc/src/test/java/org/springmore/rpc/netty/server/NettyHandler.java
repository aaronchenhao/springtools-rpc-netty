package org.springmore.rpc.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by aaron on 7/12/15.
 */
public class NettyHandler  extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("SERVER接收到消息:"+msg);
        System.out.println("SERVER发送消息:"+msg);
        ctx.channel().writeAndFlush(msg);

    }
}
