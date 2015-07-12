package org.springmore.rpc.netty.client;

import io.netty.channel.Channel;
//import org.springmore.rpc.netty.client.lc.asyn.AsynResult;
//import org.springmore.rpc.netty.client.lc.syn.LongConnectFactory;
//import org.springmore.rpc.netty.client.lc.syn.LongSynResult;
//import org.springmore.rpc.netty.client.sc.ShortConnectFactory;
import org.springmore.rpc.netty.client.lc.asyn.AsynResult;
import org.springmore.rpc.netty.client.lc.syn.LongConnectFactory;
import org.springmore.rpc.netty.client.lc.syn.LongSynResult;
import org.springmore.rpc.netty.client.sc.ShortConnectFactory;
import org.springmore.rpc.netty.client.sc.ShortResult;

/**
 * result util
 * @author 陈浩
 * @date 2015年7月12日
 */
public class ResultUtil {

	/**
	 * get Result
	 * @param connectFactory
	 * @param connection
	 * @return
	 * @author 陈浩
	 * @date 2015年7月12日
	 */
	public static Result getResult(ConnectFactory connectFactory,Channel connection){
		Result result = null;
		if (connectFactory instanceof LongConnectFactory) {
			result = new LongSynResult();
		}else if(connectFactory instanceof ShortConnectFactory){
			result = new ShortResult();
		}else{
			result = new AsynResult();
		}
		if(result != null){
			result.setConnectFactory(connectFactory);
			result.setConnection(connection);
		}
		return result;
	}
}
