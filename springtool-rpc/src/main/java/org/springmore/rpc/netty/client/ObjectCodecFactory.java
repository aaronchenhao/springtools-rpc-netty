package org.springmore.rpc.netty.client;

import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 对象编码解码过滤器工厂.
 *
 * @author 陈浩
 * @date 2015-7-12
 */
public class ObjectCodecFactory  implements ProtocolCodecFactory {

    @Override
    public Object getEncoder() throws Exception {
        return new ObjectEncoder();
    }

    @Override
    public Object getDecoder() throws Exception {
        return new ObjectDecoder(1024,ClassResolvers.cacheDisabled(this.getClass().getClassLoader()));//添加POJO对象解码器 禁止缓存类加载器
    }
}
