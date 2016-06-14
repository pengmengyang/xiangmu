package com.example.expressscanningsystem.bean;

/**
 * 跟踪查询
 * 
 * @author Administrator
 * 
 */
public class TrackTheQueryBean {

	private String FDateTime;// 时间
	private String FStatus;// 状态
	private String FBillNo;// 运单号

	public String getFDateTime() {
		return FDateTime;
	}

	public void setFDateTime(String fDateTime) {
		FDateTime = fDateTime;
	}

	public String getFStatus() {
		return FStatus;
	}

	public void setFStatus(String fStatus) {
		FStatus = fStatus;
	}

	public String getFBillNo() {
		return FBillNo;
	}

	public void setFBillNo(String fBillNo) {
		FBillNo = fBillNo;
	}

}
