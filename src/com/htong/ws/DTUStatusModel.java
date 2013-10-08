package com.htong.ws;

public class DTUStatusModel {
	private String wellNum;
	private String dtuNum;
	private Boolean connStatus;
	private String heartBeatTime;
	private Boolean commStatus;

	public DTUStatusModel() {
		
	}

	public String getWellNum() {
		return wellNum;
	}

	public void setWellNum(String wellNum) {
		this.wellNum = wellNum;
	}

	public String getDtuNum() {
		return dtuNum;
	}

	public void setDtuNum(String dtuNum) {
		this.dtuNum = dtuNum;
	}

	public Boolean getConnStatus() {
		return connStatus;
	}

	public void setConnStatus(Boolean connStatus) {
		this.connStatus = connStatus;
	}

	public String getHeartBeatTime() {
		return heartBeatTime;
	}

	public void setHeartBeatTime(String heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}

	public Boolean getCommStatus() {
		return commStatus;
	}

	public void setCommStatus(Boolean commStatus) {
		this.commStatus = commStatus;
	}

	
	

}
