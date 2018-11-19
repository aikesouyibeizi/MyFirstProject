package com.briup.web.server.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.util.Configuration;
import com.briup.web.server.Server;

public class ServerImpl implements Server {
	private ServerSocket serverSocket;
	private ObjectInputStream ois;
	private Socket accept;
	private static String port;
	private static Log log;
	private static Configuration configuration;


	@Override
	public void init(Properties properties) throws Exception {
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
	public Collection<Environment> reciver() throws Exception {
		//1.创建服务端socket
		//2.accept等待客户端连接
		//3.接收客户端发送过来的信息（ObjectInputStream接收对象）

		serverSocket = new ServerSocket(Integer.parseInt(port));
		log.info("server 等待连接.................");
		accept = serverSocket.accept();
		
		log.info("连接成功！");
		ois = new ObjectInputStream(new BufferedInputStream(accept.getInputStream()));
		Collection<Environment> e = (Collection<Environment>) ois.readObject();
		log.info("server接收成功");
	
		if( ois != null){
			ois.close();
		}
		return e;
	}

	@Override
	public void shutdown() {
		//通过调用方法关闭资源
		try {
			if(serverSocket != null ){
				serverSocket.close();
			}
			if(accept != null){
				accept.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
