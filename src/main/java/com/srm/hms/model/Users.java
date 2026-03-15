package com.srm.hms.model;


import java.io.Serializable;
/**
 * JavaBean class used in jsp action tags
 * @author prakashy
 *
 */

public class Users  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3876106171745073944L;
	private  int user_id;
	private String user_firstName;
	private String user_lastName;
	private String user_role;
	private String user_email;
	private String user_password;
	private String  user_mobile_number;
	
	public Users() {
		super();
	}
	public Users(int user_id, String user_firstName, String user_lastName, String user_role, String user_email,
			String user_password, String user_mobile_number) {
		super();
		this.user_id = user_id;
		this.user_firstName = user_firstName;
		this.user_lastName = user_lastName;
		this.user_role = user_role;
		this.user_email = user_email;
		this.user_password = user_password;
		this.user_mobile_number = user_mobile_number;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public String getUser_firstName() {
		return user_firstName;
	}
	public void setUser_firstName(String user_firstName) {
		this.user_firstName = user_firstName;
	}
	public String getUser_lastName() {
		return user_lastName;
	}
	public void setUser_lastName(String user_lastName) {
		this.user_lastName = user_lastName;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getUser_password() {
		return user_password;
	}
	public void setUser_password(String user_password) {
		this.user_password = user_password;
	}
	public String getUser_mobile_number() {
		return user_mobile_number;
	}
	public void setUser_mobile_number(String user_mobile_number) {
		this.user_mobile_number = user_mobile_number;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	

}
