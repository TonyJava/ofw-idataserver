package com.htong.domain;

import java.util.Date;

public class SQLData {
	private long id;
	private int wellId;
	private float chongci;
	private String weiyi;
	private String zaihe;
	private Date time;
	private SQLWell sqlWell;
	private Date saveTime;
	
	
	public Date getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}
	public SQLWell getSqlWell() {
		return sqlWell;
	}
	public void setSqlWell(SQLWell sqlWell) {
		this.sqlWell = sqlWell;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getWellId() {
		return wellId;
	}
	public void setWellId(int wellId) {
		this.wellId = wellId;
	}

	public float getChongci() {
		return chongci;
	}
	public void setChongci(float chongci) {
		this.chongci = chongci;
	}
	public String getWeiyi() {
		return weiyi;
	}
	public void setWeiyi(String weiyi) {
		this.weiyi = weiyi;
	}
	public String getZaihe() {
		return zaihe;
	}
	public void setZaihe(String zaihe) {
		this.zaihe = zaihe;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

}
