package org.springmore.rpc.netty.client;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springmore.rpc.netty.server.User;


public class NettyClientTest {

	NettyTemplate nettyTemplate;
	
	@Before
	public void before() {
		String[] xmls = new String[] { "classpath:applicationContext.xml"};
		ApplicationContext context = new ClassPathXmlApplicationContext(xmls);
        nettyTemplate = context.getBean(NettyTemplate.class);
	}

	@Test
	public void 发送对象() throws InterruptedException {
		User user = new User();
		user.setUserId(10);
		User user2 = nettyTemplate.sengObject(user);
		System.out.println(user2.getUserId());

	}
	
	@Test
	public void 发送字符串() throws InterruptedException {
		
		String s = nettyTemplate.sengObject("xxx");
		System.out.println(s);

	}
	
	@Test
	public void 发送字节() throws InterruptedException {
		
		byte[] s = nettyTemplate.sengObject("fff".getBytes());
		System.out.println(new String(s));
		System.out.println(s);

	}

}
