package org.springmore.rpc.netty.client;

/**
 * 编码解码过滤器工厂接口.
 *
 * @author 陈浩
 * @date 2015-7-12
 */
public interface ProtocolCodecFactory {
    Object getEncoder() throws Exception;

    Object getDecoder() throws Exception;
}
