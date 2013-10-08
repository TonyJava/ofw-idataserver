package com.htong.idataserver;

import org.apache.log4j.Logger;

/**
 * 电能原始数据
 * @author 赵磊
 *
 */
public class RawEnergyData {
	private static final Logger log = Logger.getLogger(RawEnergyData.class);
	private String well_num; // 井号
	private String name;	//井名字
	
	private String zxyg;	//正向有功
	private String fxyg;	//反向有功
	private String zxwg;
	private String fxwg;
	
	private String syZxygZ;	//上月正向有功总
	private String syFxygZ;
	private String syZxwgZ;
	private String syFxwgZ;
	
	public float getZxygZ() {
		return Float.parseFloat(zxyg.substring(32, 40))*0.01f;
	}
	public float getZxygJ() {
		return Float.parseFloat(zxyg.substring(24, 32))*0.01f;
	}
	public float getZxygF() {
		return Float.parseFloat(zxyg.substring(16, 24))*0.01f;
	}
	public float getZxygP() {
		return Float.parseFloat(zxyg.substring(8, 16))*0.01f;
	}
	public float getZxygG() {
		return Float.parseFloat(zxyg.substring(0, 8))*0.01f;
	}
	
	public float getFxygZ() {
//		return Float.parseFloat(fxyg.substring(112, 120))*0.01f;
		return 0;
	}
	public float getFxygJ() {
//		return Float.parseFloat(fxyg.substring(104, 112))*0.01f;
		return 0;
	}
	public float getFxygF() {
//		return Float.parseFloat(fxyg.substring(96, 104))*0.01f;
		return 0;
	}
	public float getFxygP() {
//		return Float.parseFloat(fxyg.substring(88, 96))*0.01f;
		return 0;
	}
	public float getFxygG() {
//		return Float.parseFloat(fxyg.substring(80, 88))*0.01f;
		return 0;
	}
	
	public float getZxwgZ() {
		return Float.parseFloat(zxwg.substring(32, 40))*0.01f;
	}
	public float getZxwgJ() {
		return Float.parseFloat(zxwg.substring(24, 32))*0.01f;
	}
	public float getZxwgF() {
		return Float.parseFloat(zxwg.substring(16, 24))*0.01f;
	}
	public float getZxwgP() {
		return Float.parseFloat(zxwg.substring(8, 16))*0.01f;
	}
	public float getZxwgG() {
		return Float.parseFloat(zxwg.substring(0, 8))*0.01f;
	}
	
	public float getFxwgZ() {
//		return Float.parseFloat(fxwg.substring(112, 120))*0.01f;
		return 0;
	}
	public float getFxwgJ() {
//		return Float.parseFloat(fxwg.substring(104, 112))*0.01f;
		return 0;
	}
	public float getFxwgF() {
//		return Float.parseFloat(fxwg.substring(96, 104))*0.01f;
		return 0;
	}
	public float getFxwgP() {
//		return Float.parseFloat(fxwg.substring(88, 96))*0.01f;
		return 0;
	}
	public float getFxwgG() {
//		return Float.parseFloat(fxwg.substring(80, 88))*0.01f;
		return 0;
	}
	
	public float getSYZxygZ() {
		return Float.parseFloat(syZxygZ)*0.01f;
	}
	public float getSYFxygZ() {
//		return Float.parseFloat(syFxygZ)*0.01f;
		return 0;
	}
	public float getSYZxwgZ() {
		return Float.parseFloat(syZxwgZ)*0.01f;
	}
	public float getSYFxwgZ() {
//		return Float.parseFloat(syFxwgZ)*0.01f;
		return 0;
	}
	

	public void setZxyg(String zxyg) {
		this.zxyg = zxyg;
	}

	public void setFxyg(String fxyg) {
		this.fxyg = fxyg;
	}

	public void setZxwg(String zxwg) {
		this.zxwg = zxwg;
	}

	public void setFxwg(String fxwg) {
		this.fxwg = fxwg;
	}

	public void setSyZxygZ(String syZxygZ) {
		this.syZxygZ = syZxygZ;
	}

	public void setSyFxygZ(String syFxygZ) {
		this.syFxygZ = syFxygZ;
	}

	public void setSyZxwgZ(String syZxwgZ) {
		this.syZxwgZ = syZxwgZ;
	}

	public void setSyFxwgZ(String syFxwgZ) {
		this.syFxwgZ = syFxwgZ;
	}

	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}
	
	
	public String getWell_num() {
		return well_num;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private boolean isInitOk() {
		if (zxyg  == null || zxwg == null
				||  syZxygZ == null 
				|| syZxwgZ == null ) {
			log.debug("【RawEnergyData】数据未采集或采集不完整");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isValid() {
		return isInitOk();
	}
	
	public void setNull() {
		zxyg = null;	//正向有功
		fxyg = null;	//反向有功
		zxwg = null;
		fxwg = null;
		
		syZxygZ = null;	//上月正向有功总
		syFxygZ = null;
		syZxwgZ = null;
		syFxwgZ = null;
	}

}
