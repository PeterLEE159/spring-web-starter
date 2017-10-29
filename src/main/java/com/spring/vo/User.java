package com.spring.vo;

import java.util.Date;

import com.spring.service.annotation.Column;
import com.spring.service.annotation.Table;

@Table("TB_USERS")
public class User extends BaseVO {
	
	@Column("USER_NO")
	private int id;
	@Column("USER_FULLNAME")
	private String username;
	@Column("USER_PASSWORD")
	private String password;
	@Column("USER_BIRTH")
	private Date userBirth;
	@Column("USER_CREATEDATE")
	private Date created_at;
	@Column("USER_ID")
	private String userId;
	@Column("USER_EMAIL")
	private String email;
	@Column("USER_PHONE")
	private String phone;
	@Column("USER_POINT")
	private double point;
	@Column("ENERGE")
	private double energe;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	public Date getUserBirth() {
		return userBirth;
	}
	public void setUserBirth(Date userBirth) {
		this.userBirth = userBirth;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public double getPoint() {
		return point;
	}
	public void setPoint(double point) {
		this.point = point;
	}
	public double getEnerge() {
		return energe;
	}
	public void setEnerge(double energe) {
		this.energe = energe;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", userBirth=" + userBirth
				+ ", created_at=" + created_at + ", userId=" + userId + ", email=" + email + ", phone=" + phone
				+ ", point=" + point + ", energe=" + energe + "]";
	}
	
}
