package com.briup.web.client.impl;

import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.util.Configuration;
import com.briup.web.client.Client;

public class ClientImpl implements Client{
	private static String  ip;
	private  static String port;
	private static Log log;
	public Configuration configuration;
	

	@Override
	public void init(Properties properties) throws Exception {
		ip = properties.getProperty("ip");
		port = properties.getProperty("port");
	}

	@Override
	public void setConfiguration(Configuration con) {
		try {
			this.configuration = con;
			log = configuration.getLogger();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void send(Collection<Environment> coll) throws Exception {
		//1.与服务器建立连接
		//2.发送对象数据   可以使用:ObjectOutputStream流
		Socket socket = null;
		ObjectOutputStream oos = null;
		try {
			socket = new Socket(ip,Integer.parseInt(port));
			oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));//用BuffereOutputStream可以提高写入的效率
			oos.writeObject(coll);				//writeObject可以自动刷新
			log.info("client 发送成功!!");	
			
		} catch (Exception e) {
			log.error("发送异常！");
			e.printStackTrace();
		}finally{
			if(oos != null){
				oos.close();
			}
			if(socket != null){
				socket.close();
			}
		}

	}


}
