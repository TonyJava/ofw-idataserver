package com.htong.domain;

import java.util.Date;

public class ElecDataMSSQL {
	
	public ElecDataMSSQL(ElecData elecData, String dtuNum, String deviceAddress) {
		this.dtuNum = dtuNum;
		this.deviceAddress = deviceAddress;
		
		this.saveTime = elecData.getSave_time();
		
		this.wellNum = elecData.getWell_num();
		this.ua = elecData.getUa();
		this.ub = elecData.getUb();
		this.uc = elecData.getUc();
		this.ia = elecData.getIa();
		this.ib = elecData.getIb();
		this.ic = elecData.getIc();
		this.shygglZ = elecData.getShygglZ();
		this.shygglA = elecData.getShygglA();
		this.shygglB = elecData.getShygglB();
		this.shygglC = elecData.getShygglC();
		this.shwgglZ = elecData.getShwgglZ();
		this.shwgglA = elecData.getShwgglA();
		this.shwgglB = elecData.getShwgglB();
		this.shwgglC = elecData.getShwgglC();
		this.glysZ = elecData.getGlysZ();
		this.glysA = elecData.getGlysA();
		this.glysB = elecData.getGlysB();
		this.glysC = elecData.getGlysC();
	}
	
	private int id;
	
	private String wellNum; // 井号
	private String dtuNum;	//DTU号
	private String deviceAddress; //设备地址
	
	private Date saveTime;
	
	private float ua;
	private float ub;
	private float uc;
	private float ia;
	private float ib;
	private float ic;
	
	private float shygglZ;	//瞬时有功功率总
	private float shygglA;
	private float shygglB;
	private float shygglC;
	
	private float shwgglZ;	//瞬时无功功率总
	private float shwgglA;
	private float shwgglB;
	private float shwgglC;
	
	private float glysZ;	//功率因素总
	private float glysA;
	private float glysB;
	private float glysC;
	

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWellNum() {
		return wellNum;
	}

	public void setWellNum(String wellNum) {
		this.wellNum = wellNum;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public String getDtuNum() {
		return dtuNum;
	}

	public void setDtuNum(String dtuNum) {
		this.dtuNum = dtuNum;
	}

	public float getUa() {
		return ua;
	}

	public void setUa(float ua) {
		this.ua = ua;
	}

	public float getUb() {
		return ub;
	}

	public void setUb(float ub) {
		this.ub = ub;
	}

	public float getUc() {
		return uc;
	}

	public void setUc(float uc) {
		this.uc = uc;
	}

	public float getIa() {
		return ia;
	}

	public void setIa(float ia) {
		this.ia = ia;
	}

	public float getIb() {
		return ib;
	}

	public void setIb(float ib) {
		this.ib = ib;
	}

	public float getIc() {
		return ic;
	}

	public void setIc(float ic) {
		this.ic = ic;
	}

	public float getShygglZ() {
		return shygglZ;
	}

	public void setShygglZ(float shygglZ) {
		this.shygglZ = shygglZ;
	}

	public float getShygglA() {
		return shygglA;
	}

	public void setShygglA(float shygglA) {
		this.shygglA = shygglA;
	}

	public float getShygglB() {
		return shygglB;
	}

	public void setShygglB(float shygglB) {
		this.shygglB = shygglB;
	}

	public float getShygglC() {
		return shygglC;
	}

	public void setShygglC(float shygglC) {
		this.shygglC = shygglC;
	}

	public float getShwgglZ() {
		return shwgglZ;
	}

	public void setShwgglZ(float shwgglZ) {
		this.shwgglZ = shwgglZ;
	}

	public float getShwgglA() {
		return shwgglA;
	}

	public void setShwgglA(float shwgglA) {
		this.shwgglA = shwgglA;
	}

	public float getShwgglB() {
		return shwgglB;
	}

	public void setShwgglB(float shwgglB) {
		this.shwgglB = shwgglB;
	}

	public float getShwgglC() {
		return shwgglC;
	}

	public void setShwgglC(float shwgglC) {
		this.shwgglC = shwgglC;
	}

	public float getGlysZ() {
		return glysZ;
	}

	public void setGlysZ(float glysZ) {
		this.glysZ = glysZ;
	}

	public float getGlysA() {
		return glysA;
	}

	public void setGlysA(float glysA) {
		this.glysA = glysA;
	}

	public float getGlysB() {
		return glysB;
	}

	public void setGlysB(float glysB) {
		this.glysB = glysB;
	}

	public float getGlysC() {
		return glysC;
	}

	public void setGlysC(float glysC) {
		this.glysC = glysC;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	
	

}
