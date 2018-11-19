package com.briup.web.server.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import com.briup.bean.Environment;
import com.briup.log.Log;
import com.briup.util.Configuration;
import com.briup.web.server.DBStore;

public class DBStoreImpl implements DBStore {
	private static String driver ;
	private static String url ;
	private static String username;
	private static String password ;
	private Log log;
	private Configuration configuration;
	
	
	@Override
	public void init(Properties properties) throws Exception {
		driver = properties.getProperty("driver");
		url = properties.getProperty("url");
		username = properties.getProperty("username");
		password = properties.getProperty("password");
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
	public void saveDb(Collection<Environment> coll) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
			Calendar calendar = Calendar.getInstance();
			int i = calendar.get(Calendar.DAY_OF_MONTH);
			
			String sql = "insert into e_detail_" + i + " values(?,?,?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			int count = 0;
			log.info("开始入库！");
			for (Environment e : coll) {
				ps.setString(1, e.getName());
				ps.setString(2, e.getSrcId());
				ps.setString(3, e.getDstId());
				ps.setString(4, e.getSersorAddress());
				ps.setInt(5, e.getCount());
				ps.setString(6, e.getCmd());
				ps.setInt(7, e.getStatus());
				ps.setFloat(8, e.getData());
				java.util.Date date = e.getGather_date();
				java.sql.Date date2 = new java.sql.Date(date.getTime());
				ps.setDate(9, date2);
				ps.addBatch();
				count++;
				if(count%500 == 0){
					ps.executeBatch();
					//清空批处理集合
					ps.clearBatch();
				}
			}
			ps.executeBatch();
			
			log.info("入库完成！");
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			//关闭资源必须执行
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

	}

}
