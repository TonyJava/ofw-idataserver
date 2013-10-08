package com.htong.idataserver;

import org.apache.log4j.Logger;

public class RawElecData {
	private static final Logger log = Logger.getLogger(RawElecData.class);
	
	private String well_num; // 井号
	private String name;	//井名字
	
	private String xdy;	//相电压
	private String xdl;	//相电流
	private String shyggl;	//瞬时有功功率
	private String shwggl;	//瞬时无功功率
	private String glys;	//功率因数
//	private String otherElec;	//其他电
	private String xiandy;	//线电压
	
	private String pinlv;
	private String lxdl;
	private String lxdy;
	private String dlbphd;
	private String dybphd;
	
	public float getUA() {
		return Float.parseFloat(xdy.substring(8, 12));
	}
	public float getUB() {
		return Float.parseFloat(xdy.substring(4, 8));
	}
	public float getUC() {
		return Float.parseFloat(xdy.substring(0, 4));
	}
	
	public float getIA() {
		return Float.parseFloat(xdl.substring(8, 12))*0.01f;
	}
	public float getIB() {
		return Float.parseFloat(xdl.substring(4, 8))*0.01f;
	}
	public float getIC() {
		return Float.parseFloat(xdl.substring(0, 4))*0.01f;
	}
	public float getSHYgglZ() {
		return Float.parseFloat(shyggl.substring(18, 24))*0.0001f;
	}
	public float getSHYgglA() {
		return Float.parseFloat(shyggl.substring(12, 18))*0.0001f;
	}
	public float getSHYgglB() {
		return Float.parseFloat(shyggl.substring(6, 12))*0.0001f;
	}
	public float getSHYgglC() {
		return Float.parseFloat(shyggl.substring(0, 6))*0.0001f;
	}
	public float getSHWgglZ() {
		return Float.parseFloat(shwggl.substring(12, 16))*0.01f;
	}
	public float getSHWgglA() {
		return Float.parseFloat(shwggl.substring(8, 12))*0.01f;
	}
	public float getSHWgglB() {
		return Float.parseFloat(shwggl.substring(4, 8))*0.01f;
	}
	public float getSHWgglC() {
		return Float.parseFloat(shwggl.substring(0, 4))*0.01f;
	}
	
	public float getGLYSZ() {
		return Float.parseFloat(glys.substring(12, 16))*0.001f;
	}
	public float getGLYSA() {
		return Float.parseFloat(glys.substring(8, 12))*0.001f;
	}
	public float getGLYSB() {
		return Float.parseFloat(glys.substring(4, 8))*0.001f;
	}
	public float getGLYSC() {
		return Float.parseFloat(glys.substring(0, 4))*0.001f;
	}
	
	public float getPinLv() {
//		return Float.parseFloat(otherElec.substring(18, 22))*0.01f;
		return Float.parseFloat(pinlv)*0.01f;
	}
	public float getLXDL() {
//		return Float.parseFloat(otherElec.substring(14, 18))*0.01f;
		return Float.parseFloat(lxdl)*0.01f;
	}
	public float getLXDY() {
//		return Float.parseFloat(otherElec.substring(10, 14));
		return Float.parseFloat(lxdy);
	}
	public float getDLBPHD() {
		return Float.parseFloat(dlbphd);
	}
	public float getDYBPHD() {
		return Float.parseFloat(dybphd);
	}
	public float getUab() {
		return Float.parseFloat(xiandy.substring(8, 12));
	}
	public float getUbc() {
		return Float.parseFloat(xiandy.substring(4, 8));
	}
	public float getUca() {
		return Float.parseFloat(xiandy.substring(0, 4));
	}
	
	

	public void setXdy(String xdy) {
		this.xdy = xdy;
	}

	public void setXdl(String xdl) {
		this.xdl = xdl;
	}

	public void setShyggl(String shyggl) {
		this.shyggl = shyggl;
	}

	public void setShwggl(String shwggl) {
		this.shwggl = shwggl;
	}

	public void setGlys(String glys) {
		this.glys = glys;
	}

//	public void setOtherElec(String otherElec) {
//		this.otherElec = otherElec;
//	}

	public void setXiandy(String xiandy) {
		this.xiandy = xiandy;
	}
	
	public String getWell_num() {
		return well_num;
	}
	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private boolean isInitOk() {
		if (xdy  == null || xdl == null || shyggl == null
				|| shwggl == null || glys == null || pinlv == null || lxdl == null || lxdy == null || dlbphd == null || dybphd == null
				|| xiandy == null) {
			log.debug("【RawElecData】数据未采集或采集不完整");
			return false;
		} else {
			return true;
		}
	}
	
	public boolean isValid() {
		return isInitOk();
	}

	public void setNull() {
		xdy = null;	//相电压
		xdl = null;	//相电流
		shyggl = null;	//瞬时有功功率
		shwggl = null;	//瞬时无功功率
		glys = null;	//功率因数
//		otherElec = null;	//其他电
		xiandy = null;	//线电压
		pinlv = null;
		lxdl = null;
		lxdy = null;
		dlbphd = null;
		dybphd = null;
		
	}
	public void setPinlv(String pinlv) {
		this.pinlv = pinlv;
	}
	public void setLxdl(String lxdl) {
		this.lxdl = lxdl;
	}
	public void setLxdy(String lxdy) {
		this.lxdy = lxdy;
	}
	public void setDlbphd(String dlbphd) {
		this.dlbphd = dlbphd;
	}
	public void setDybphd(String dybphd) {
		this.dybphd = dybphd;
	}
	

}
