package com.briup.web.client.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.util.Configuration;
import com.briup.web.client.Gather;

public class GatherImpl implements Gather{
	
	private Log log;
	private static String src_file;
	private Configuration configuration;

	@Override
	public void init(Properties properties) throws Exception {
		//获取采集文件的路径
		src_file = properties.getProperty("src-file");
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
	public Collection<Environment> gather() throws Exception {
		//1.采集环境数据信息：（从文件radwttmp读取数据）
		/**
		 * 需要考虑以下问题：
		 * 1.如何读取文件（选什么io流，需要一行一行的读取数据）
		 * 	io流：	BufferedReader readLine();
		 * 			RandomAccessFile  随机文件读取流   readLine();
		 * 2.读取到一行数据之后如何解析（分割字符串）
		 * 		正则表达式："[|]"  分割后得到数组strs
		 * 3.得到的每部分数据如何与Environment对象对应
		 * 		判断得到数据是何种环境类型   "16".equals("strs[3]")
		 * 		data:
		 * 		时间：有：long时间戳，需要java.sql.TimeStamp
		 * 4.将得到的数据封装成Environment对象
		 * 5.将得到的Environment对象保存到集合
		 */
		BufferedReader br = null;
		ArrayList<Environment> list = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(src_file)));
			
			String msg = null;   			//读到的第一行数据
			
			String srcId = null;			//发送端id
			String dstId = null;			//树莓派系统id
			String devId = null;			//实验箱区域模块id(1-8)
			String sersorAddress = null;	//模块上传感器地址
			int count = 0;					//传感器个数
			String cmd = null;				//发送指令标号 3:接受数据;16:发送数据
			int status = 0;					//状态 默认为1表示成功
			float data1 = 0;				//环境值
			float data2 = 0;
			float data = 0;
			int index1 = 0;
			int index2 = 0;
			int index3 = 0;
			Timestamp gather_date = null;	//采集时间
			
			Environment environment1 = null;
			Environment environment2 = null;
			Environment environment = null;
			list = new ArrayList<Environment>();
			log.info("开始采集！");
			while((msg = br.readLine()) != null){
				String[] strs = strToArr(msg);
			
				srcId = strs[0];
				dstId = strs[1];
				devId = strs[2];
				sersorAddress = strs[3];
				count = Integer.parseInt(strs[4]);
				cmd = strs[5];
				status = Integer.parseInt(strs[7]);
				
				gather_date = new Timestamp(Long.parseLong(strs[8]));
				//温度、湿度
				if("16".equals(sersorAddress)){
					int value1 = Integer.parseInt(strs[6].substring(0,4),16);
					data1 = (float) (((float)value1*0.00268127)-46.85);
					int value2 = Integer.parseInt(strs[6].substring(4,8),16);
					data2 = (float) (((float)value2*0.00190735)-6);
					
					environment1 = new Environment("wendu",srcId,dstId,devId,sersorAddress,count,cmd,status,data1,gather_date);
					environment2 = new Environment("shidu",srcId,dstId,devId,sersorAddress,count,cmd,status,data2,gather_date);
					list.add(environment1);
					list.add(environment2);
					index1++;
				}
				//光照强度
				else if("256".equals(sersorAddress)){
					int value = Integer.parseInt(strs[6].substring(0,4),16);
					data = (float)value;
					environment = new Environment("guang",srcId,dstId,devId,sersorAddress,count,cmd,status,data,gather_date);
					list.add(environment);
					index2++;
				}
				//二氧化碳
				else{
					int value = Integer.parseInt(strs[6].substring(0,4),16);
					data = (float)value;
					environment = new Environment("2",srcId,dstId,devId,sersorAddress,count,cmd,status,data,gather_date);
					list.add(environment);
					index3++;
				}
				
			}
			log.info("采集结束！");
			log.info("温度："+index1);
			log.info("光照强度："+index2);
			log.info("二氧化碳："+index3);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(br != null) br.close();
		}
		return list;
	}
	
	public static String[] strToArr(String str){
		String[] strs = str.split("[|]");
		return strs;
	}




}
