package com.example.expressscanningsystem.bean;
/**
 * Ա��
 * @author Administrator
 *
 */
public class EmployeeBean {
	
	private String EP_Name;//Ա������
	private String password;//����
	private String UserNo;//Ա������
	private String BelongNet;//Ա����������
	
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
