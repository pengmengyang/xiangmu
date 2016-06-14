package com.example.expressscanningsystem.bean;
/**
 * 员工
 * @author Administrator
 *
 */
public class EmployeeBean {
	
	private String EP_Name;//员工名称
	private String password;//密码
	private String UserNo;//员工工号
	private String BelongNet;//员工属于网点
	
	public String getUserNo() {
		return UserNo;
	}
	public void setUserNo(String userNo) {
		UserNo = userNo;
	}
	public String getBelongNet() {
		return BelongNet;
	}
	public void setBelongNet(String belongNet) {
		BelongNet = belongNet;
	}
	public String getEP_Name() {
		return EP_Name;
	}
	public void setEP_Name(String eP_Name) {
		EP_Name = eP_Name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
