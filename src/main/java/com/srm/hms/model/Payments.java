package com.srm.hms.model;

import java.io.Serializable;
import java.time.LocalDate;


public class Payments implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 2462160195179644372L;
	private  int payment_id;
	private int user_id;
	private  float amount;
	private LocalDate payment_date;
	private String payment_mode;
	private long card_number;
	private String expire_date;
	private int cvv_number;
	private String transaction_id;
	private String user_firstName;
	private String user_email;
	private Users user;
	public Payments(int user_id, float amount, LocalDate payment_date, String payment_mode, long card_number,
			String expire_date, int cvv_number, String transaction_id) {
		super();
		this.user_id = user_id;
		this.amount = amount;
		this.payment_date = payment_date;
		this.payment_mode = payment_mode;
		this.card_number = card_number;
		this.expire_date = expire_date;
		this.cvv_number = cvv_number;
		this.transaction_id = transaction_id;
	}
	public Payments(float amount, LocalDate payment_date, String payment_mode, long card_number, String expire_date,
			int cvv_number, String transaction_id, String user_firstName, String user_email) {
		super();
		this.amount = amount;
		this.payment_date = payment_date;
		this.payment_mode = payment_mode;
		this.card_number = card_number;
		this.expire_date = expire_date;
		this.cvv_number = cvv_number;
		this.transaction_id = transaction_id;
		this.user_firstName = user_firstName;
		this.user_email = user_email;
	}
	public int getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(int payment_id) {
		this.payment_id = payment_id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public LocalDate getPayment_date() {
		return payment_date;
	}
	public void setPayment_date(LocalDate payment_date) {
		this.payment_date = payment_date;
	}
	public String getPayment_mode() {
		return payment_mode;
	}
	public void setPayment_mode(String payment_mode) {
		this.payment_mode = payment_mode;
	}
	public long getCard_number() {
		return card_number;
	}
	public void setCard_number(long card_number) {
		this.card_number = card_number;
	}
	public String getExpire_date() {
		return expire_date;
	}
	public void setExpire_date(String expire_date) {
		this.expire_date = expire_date;
	}
	public int getCvv_number() {
		return cvv_number;
	}
	public void setCvv_number(int cvv_number) {
		this.cvv_number = cvv_number;
	}
	public String getTransaction_id() {
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
	public String getUser_firstName() {
		return user_firstName;
	}
	public void setUser_firstName(String user_firstName) {
		this.user_firstName = user_firstName;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Payments() {
		super();
	}

	
	
	
}
