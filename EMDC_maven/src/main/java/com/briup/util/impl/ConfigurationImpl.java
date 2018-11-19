package com.briup.util.impl;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.log.Log;
import com.briup.util.Configuration;
import com.briup.util.ConfigurationAware;
import com.briup.util.WossModuleInit;
import com.briup.web.client.Client;
import com.briup.web.client.Gather;
import com.briup.web.server.DBStore;
import com.briup.web.server.Server;

public class ConfigurationImpl implements Configuration {
	public static HashMap<String, WossModuleInit> map = new HashMap<String, WossModuleInit>();

	public ConfigurationImpl() {
		this("src/main/java/com/briup/util/config.xml");
	}

	public ConfigurationImpl(String path) {
		try {
			// 1.解析xml文件
			SAXReader reader = new SAXReader();
			Document document = reader.read(new FileInputStream(path));
			Element rootElement = document.getRootElement();
			List<Element> list = rootElement.elements();
			// 2.将每个模块的标签名作为key，class属性反射的对象作为value值
			for (Element e : list) {
				String elementName = e.getName();
				String className = e.attributeValue("class");
				WossModuleInit object = (WossModuleInit) Class.forName(className).newInstance();
				map.put(elementName, object);
				List<Element> list2 = e.elements();
				// 3.将每个模块的子标签名作为key值，文本内容作为value值
				Properties properties = new Properties();
				for (Element e2 : list2) {
					String text2 = e2.getText();
					String elementName2 = e2.getName();
					properties.setProperty(elementName2, text2);
				}
				// 4.需要调用每个模块的init方法，然后将构建好的Properties对象传给每个模块
				object.init(properties);

				// 5.需要调用每一个模块的setConfiguration()方法，然后将自身引用传给所有模块
				// (这个this代表的是现在状态Configuration的实例对象，而此时的实例对象还是不完整的，所以要等到循环全部完成之后
				// 再将this传到相应的模块)

				// if(object instanceof ConfigurationAware){
				// ((ConfigurationAware) object).setConfiguration(this);
				// }
			}
			Set<Entry<String, WossModuleInit>> entrys = map.entrySet();
			for (Entry<String, WossModuleInit> e : entrys) {
				String key = e.getKey();
				WossModuleInit value = e.getValue();
				if (value instanceof ConfigurationAware) {
					((ConfigurationAware) value).setConfiguration(this);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Log getLogger() throws Exception {
		WossModuleInit log = map.get("logger");
		return (Log) log;
	}

	@Override
	public Server getServer() throws Exception {
		WossModuleInit server = map.get("server");
		return (Server) server;
	}

	@Override
	public Client getClient() throws Exception {
		WossModuleInit client = map.get("client");
		return (Client) client;
	}

	@Override
	public DBStore getDbStore() throws Exception {
		WossModuleInit dbstore = map.get("dbstore");
		return (DBStore) dbstore;
	}

	@Override
	public Gather getGather() throws Exception {
		WossModuleInit gather = map.get("gather");
		return (Gather) gather;
	}

}
