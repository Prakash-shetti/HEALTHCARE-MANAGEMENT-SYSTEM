package com.srm.hms.util;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;

public class JdbcUtils {
	private static String jdbcURL="jdbc:postgresql://localhost:5432/HospitalManagementSystem";
	private static String jdbcUserName="postgres";
	private static String jdbcPassword="Maha@123";
	
	public static Connection getConnection() {
		Connection conn=null;
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);
		}catch(SQLException se) { 
			se.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static Date getSQLDate(LocalDate dt) {
		return java.sql.Date.valueOf(dt);
	}
	
	public static LocalDate getUtilDate(Date dt) {
		return dt.toLocalDate();
	}
	public static void printSQLException(SQLException se) {
		for(Throwable te:se) {
			if(te instanceof SQLException ) {
				te.printStackTrace(System.err);
				System.err.println("SQLState :"+((SQLException) te).getSQLState());
				System.err.println("Error code"+((SQLException) te).getErrorCode());
				System.err.println("Message :"+te.getMessage());
				Throwable t = se.getCause();
				while(t!=null) {
					System.out.println("Cause :"+t.getCause());
				}
			}
		}
	}

}

