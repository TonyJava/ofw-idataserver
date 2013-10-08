package com.htong.domain;

import java.util.Date;

public class EnergyDataMSSQL {
	
	public EnergyDataMSSQL(EnergyData energyData, String dtuNum, String deviceAddress) {
		this.dtuNum = dtuNum;
		this.deviceAddress = deviceAddress;
		
		this.saveTime = energyData.getSave_time();
		
		this.wellNum = energyData.getWell_num();

		this.zxygZ = energyData.getZxygZ();
		this.zxygJ = energyData.getZxygJ();
		this.zxygF = energyData.getZxygF();
		this.zxygP = energyData.getZxygP();
		this.zxygG = energyData.getZxygG();
		
		this.fxygZ = energyData.getFxygZ();
		this.fxygJ = energyData.getFxygJ();
		this.fxygF = energyData.getFxygF();
		this.fxygP = energyData.getFxygP();
		this.fxygG = energyData.getFxygG();
		
		this.zxwgZ = energyData.getZxwgZ();
		this.zxwgJ = energyData.getZxwgJ();
		this.zxwgF = energyData.getZxwgF();
		this.zxwgP = energyData.getZxwgP();
		this.zxwgG = energyData.getZxwgG();
		
		this.fxwgZ = energyData.getFxwgZ();
		this.fxwgJ = energyData.getFxwgJ();
		this.fxwgF = energyData.getFxwgF();
		this.fxwgP = energyData.getFxwgP();
		this.fxwgG = energyData.getFxwgG();
	}
	
	private int id;
	
	private String wellNum; // 井号
	private String dtuNum;	//DTU号
	private String deviceAddress; //设备地址
	
	private Date saveTime;
	
	private float zxygZ;	//正向有功总
	private float zxygJ;	//正向有功尖
	private float zxygF;
	private float zxygP;
	private float zxygG;
	
	private float fxygZ;	//反向有功总
	private float fxygJ;	//反向有功尖
	private float fxygF;
	private float fxygP;
	private float fxygG;
	
	private float zxwgZ;	//正向无功总
	private float zxwgJ;	//正向无功尖
	private float zxwgF;
	private float zxwgP;
	private float zxwgG;
	
	private float fxwgZ;	//反向无功总
	private float fxwgJ;	//反向无功尖
	private float fxwgF;
	private float fxwgP;
	private float fxwgG;
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
	public String getDtuNum() {
		return dtuNum;
	}
	public void setDtuNum(String dtuNum) {
		this.dtuNum = dtuNum;
	}
	public String getDeviceAddress() {
		return deviceAddress;
	}
	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	public Date getSaveTime() {
		return saveTime;
	}
	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}
	public float getZxygZ() {
		return zxygZ;
	}
	public void setZxygZ(float zxygZ) {
		this.zxygZ = zxygZ;
	}
	public float getZxygJ() {
		return zxygJ;
	}
	public void setZxygJ(float zxygJ) {
		this.zxygJ = zxygJ;
	}
	public float getZxygF() {
		return zxygF;
	}
	public void setZxygF(float zxygF) {
		this.zxygF = zxygF;
	}
	public float getZxygP() {
		return zxygP;
	}
	public void setZxygP(float zxygP) {
		this.zxygP = zxygP;
	}
	public float getZxygG() {
		return zxygG;
	}
	public void setZxygG(float zxygG) {
		this.zxygG = zxygG;
	}
	public float getFxygZ() {
		return fxygZ;
	}
	public void setFxygZ(float fxygZ) {
		this.fxygZ = fxygZ;
	}
	public float getFxygJ() {
		return fxygJ;
	}
	public void setFxygJ(float fxygJ) {
		this.fxygJ = fxygJ;
	}
	public float getFxygF() {
		return fxygF;
	}
	public void setFxygF(float fxygF) {
		this.fxygF = fxygF;
	}
	public float getFxygP() {
		return fxygP;
	}
	public void setFxygP(float fxygP) {
		this.fxygP = fxygP;
	}
	public float getFxygG() {
		return fxygG;
	}
	public void setFxygG(float fxygG) {
		this.fxygG = fxygG;
	}
	public float getZxwgZ() {
		return zxwgZ;
	}
	public void setZxwgZ(float zxwgZ) {
		this.zxwgZ = zxwgZ;
	}
	public float getZxwgJ() {
		return zxwgJ;
	}
	public void setZxwgJ(float zxwgJ) {
		this.zxwgJ = zxwgJ;
	}
	public float getZxwgF() {
		return zxwgF;
	}
	public void setZxwgF(float zxwgF) {
		this.zxwgF = zxwgF;
	}
	public float getZxwgP() {
		return zxwgP;
	}
	public void setZxwgP(float zxwgP) {
		this.zxwgP = zxwgP;
	}
	public float getZxwgG() {
		return zxwgG;
	}
	public void setZxwgG(float zxwgG) {
		this.zxwgG = zxwgG;
	}
	public float getFxwgZ() {
		return fxwgZ;
	}
	public void setFxwgZ(float fxwgZ) {
		this.fxwgZ = fxwgZ;
	}
	public float getFxwgJ() {
		return fxwgJ;
	}
	public void setFxwgJ(float fxwgJ) {
		this.fxwgJ = fxwgJ;
	}
	public float getFxwgF() {
		return fxwgF;
	}
	public void setFxwgF(float fxwgF) {
		this.fxwgF = fxwgF;
	}
	public float getFxwgP() {
		return fxwgP;
	}
	public void setFxwgP(float fxwgP) {
		this.fxwgP = fxwgP;
	}
	public float getFxwgG() {
		return fxwgG;
	}
	public void setFxwgG(float fxwgG) {
		this.fxwgG = fxwgG;
	}
	
	
	

}
