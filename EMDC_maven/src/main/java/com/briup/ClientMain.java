package com.briup;

import java.util.Collection;

import com.briup.bean.Environment;
import com.briup.util.impl.ConfigurationImpl;
import com.briup.web.client.Client;
import com.briup.web.client.Gather;

public class ClientMain {

	public static void main(String[] args) {
		try {
			ConfigurationImpl con = new ConfigurationImpl();
			Gather gather = con.getGather();
			Collection<Environment> coll = gather.gather();
			
			Client client = con.getClient();
			client.send(coll);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
