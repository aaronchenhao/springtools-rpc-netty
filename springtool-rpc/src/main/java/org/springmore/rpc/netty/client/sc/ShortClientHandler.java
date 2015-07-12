package org.springmore.rpc.netty.client.sc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.apache.log4j.Logger;
import org.springmore.rpc.netty.client.Result;

/**
 * 短连接Handler.
 *
 * @author 陈浩
 * @date 2015-7-12
 */
public class ShortClientHandler extends ChannelInboundHandlerAdapter {
    private AttributeKey<Result> RESULT =  null;//new AttributeKey<Result>(Result.RESULT);
    private static final Logger logger = Logger
            .getLogger(ShortClientHandler.class.getName());

    public ShortClientHandler(AttributeKey<Result> RESULT) {
        this.RESULT = RESULT;

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //与服务端建立连接后
        System.out.println("client connected..");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("client channelRead..");
        //服务端返回消息后
        Result result = (Result)ctx.channel().attr(RESULT).get();
        result.set(msg);
        ctx.close();
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
