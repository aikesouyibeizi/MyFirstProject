package com.briup.web.server.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Create {
	private static String driver = "oracle.jdbc.driver.OracleDriver";
	private static String url="jdbc:oracle:thin:@127.0.0.1:1521:XE";
	private static String user = "briup";
	private static String password = "briup";
	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, user, password);
				stmt = conn.createStatement();
				for(int i=1;i<=31;i++){
					String sql = "create table e_detail_"+i+"(name varchar2(20),srcId varchar2(5),dstId varchar2(5),sersorAddress varchar2(7),count number(2),cmd  varchar2(5),status number(2),data number(9,4),gather_date date)";
					stmt.execute(sql);
					System.out.println("create success!!");
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(stmt != null){
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				if(conn != null){
					try {
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
	}

}
