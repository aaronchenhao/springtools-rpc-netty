package org.springmore.rpc.netty.client;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


import java.nio.charset.Charset;

/**
 * 文本编码解码过滤器工厂.
 *
 * @author 陈浩
 * @date 2015-7-12
 */
public class TextLineCodecFactory implements ProtocolCodecFactory{
    private String coder;

    public TextLineCodecFactory(String coder) {
        this.coder = coder;
    }


    @Override
    public Object getEncoder() throws Exception {
        return new StringEncoder(Charset.forName(coder));
    }

    @Override
    public Object getDecoder() throws Exception {
        return new StringDecoder(Charset.forName(coder));
    }
}
