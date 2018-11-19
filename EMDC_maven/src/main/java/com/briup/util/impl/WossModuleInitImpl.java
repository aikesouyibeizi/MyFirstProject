package com.briup.util.impl;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.util.WossModuleInit;

public class WossModuleInitImpl implements WossModuleInit{

	@Override
	public void init(Properties properties) throws Exception {
	
		SAXReader reader = new SAXReader();
		Document document = reader.read(new File("src/main/java/com/briup/util/config.xml"));
		Element root = document.getRootElement();
		List<Element> list = root.elements();
		for(Element e:list){
//			String attribute = e.attributeValue("class");
//			System.out.println(attribute);
			List<Element> list2 = e.elements();
			for(Element e2:list2){
//				System.out.println(e2.getName()+","+e2.getText());
				properties.put(e2.getName(),e2.getText());
			}
		}
		
	}
//	public static void main(String[] args) {
//		try {
//			new WossModuleInitImpl().init(new Properties());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
