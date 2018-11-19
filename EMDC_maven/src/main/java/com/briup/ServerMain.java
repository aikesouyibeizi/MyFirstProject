package com.briup;

import java.util.Collection;

import com.briup.bean.Environment;
import com.briup.util.impl.ConfigurationImpl;
import com.briup.web.server.DBStore;
import com.briup.web.server.Server;

public class ServerMain {

	public static void main(String[] args) {
		try {
			ConfigurationImpl con = new ConfigurationImpl();
			Server server = con.getServer();
			Collection<Environment> reciver = server.reciver();
			server.shutdown();
			
			DBStore store = con.getDbStore();
			store.saveDb(reciver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
