package org.springmore.rpc.netty.client.lc.syn;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springmore.rpc.netty.client.Result;

public class LongClientHandler extends ChannelInboundHandlerAdapter {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());

    private AttributeKey<Result> RESULT =  null;

    public LongClientHandler(AttributeKey<Result> RESULT) {
        this.RESULT = RESULT;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //与服务端建立连接后
        System.out.println("client connected..");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("client channelRead..");
        //服务端返回消息后
        Result result = (Result)ctx.channel().attr(RESULT).get();
        result.set(msg);
        System.out.println("Now is :" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        System.out.println("client exceptionCaught.."+cause.getMessage());
        // 释放资源
        ctx.close();
    }

	
}
