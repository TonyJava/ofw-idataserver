package com.htong.idataserver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 原始数据
 * 
 * @author 赵磊
 * 
 */
public class RawWellData {

	private static final Logger log = Logger.getLogger(RawWellData.class);
	private String well_num; // 井号
	String startTime; // 上次开井时间
	String stopTime; // 上次停井时间
	String basicTime; // 基本数据时间 yyMMddHHmmss
	String chong_cheng_time;
	String chong_ci;
	String gtmk; // 功图模块电压
	String yjmk; // 油井模块电压

	String zaihe_1;
	String zaihe_1_time;
	String zaihe_2;
	String zaihe_2_time;
	String zaihe_3;
	String zaihe_3_time;
	String zaihe_4;
	String zaihe_4_time;

	String weiyi_1;
	String weiyi_1_time;
	String weiyi_2;
	String weiyi_2_time;
	String weiyi_3;
	String weiyi_3_time;
	String weiyi_4;
	String weiyi_4_time;

	String ib_1;
	String ib_1_time;
	String ib_2;
	String ib_2_time;
	String ib_3;
	String ib_3_time;
	String ib_4;
	String ib_4_time;

	String power_1;
	String power_1_time;
	String power_2;
	String power_2_time;
	String power_3;
	String power_3_time;
	String power_4;
	String power_4_time;

	String pf_1;
	String pf_1_time;
	String pf_2;
	String pf_2_time;
	String pf_3;
	String pf_3_time;
	String pf_4;
	String pf_4_time;

	String dgt_1;
	String dgt_1_time;
	String dgt_2;
	String dgt_2_time;
	String dgt_3;
	String dgt_3_time;
	String dgt_4;
	String dgt_4_time;

	/**
	 * 初始化基本数据
	 * 
	 * @param datasStrings
	 */
	public void initBasicData(String datasStrings) {
		startTime = datasStrings.substring(0, 8);
		stopTime = datasStrings.substring(8, 16);
		basicTime = datasStrings.substring(36, 48);
		chong_cheng_time = datasStrings.substring(20, 28);
		chong_ci = datasStrings.substring(28, 36);
		gtmk = datasStrings.substring(48, 52);
		yjmk = datasStrings.substring(52, 56);
	}

	public int getChong_ci() {
		return Integer.valueOf(this.chong_ci);
	}

	public void setZaihe_1(String datasStrings) {
		this.zaihe_1 = datasStrings.substring(0, 200);
		this.zaihe_1_time = datasStrings.substring(200, 212);
	}

	public void setZaihe_2(String datasStrings) {
		this.zaihe_2 = datasStrings.substring(0, 200);
		this.zaihe_2_time = datasStrings.substring(200, 212);
	}

	public void setZaihe_3(String datasStrings) {
		this.zaihe_3 = datasStrings.substring(0, 200);
		this.zaihe_3_time = datasStrings.substring(200, 212);
	}

	public void setZaihe_4(String datasStrings) {
		this.zaihe_4 = datasStrings.substring(0, 200);
		this.zaihe_4_time = datasStrings.substring(200, 212);
	}

	public void setWeiyi_1(String datasStrings) {
		this.weiyi_1 = datasStrings.substring(0, 200);
		this.weiyi_1_time = datasStrings.substring(200, 212);
	}

	public void setWeiyi_2(String datasStrings) {
		this.weiyi_2 = datasStrings.substring(0, 200);
		this.weiyi_2_time = datasStrings.substring(200, 212);
	}

	public void setWeiyi_3(String datasStrings) {
		this.weiyi_3 = datasStrings.substring(0, 200);
		this.weiyi_3_time = datasStrings.substring(200, 212);
	}

	public void setWeiyi_4(String datasStrings) {
		this.weiyi_4 = datasStrings.substring(0, 200);
		this.weiyi_4_time = datasStrings.substring(200, 212);
	}

	public void setIb_1(String datasStrings) {
		this.ib_1 = datasStrings.substring(0, 200);
		this.ib_1_time = datasStrings.substring(200, 212);
	}

	public void setIb_2(String datasStrings) {
		this.ib_2 = datasStrings.substring(0, 200);
		this.ib_2_time = datasStrings.substring(200, 212);
	}

	public void setIb_3(String datasStrings) {
		this.ib_3 = datasStrings.substring(0, 200);
		this.ib_3_time = datasStrings.substring(200, 212);
	}

	public void setIb_4(String datasStrings) {
		this.ib_4 = datasStrings.substring(0, 200);
		this.ib_4_time = datasStrings.substring(200, 212);
	}

	public void setPower_1(String datasStrings) {
		this.power_1 = datasStrings.substring(0, 200);
		this.power_1_time = datasStrings.substring(200, 212);
	}

	public void setPower_2(String datasStrings) {
		this.power_2 = datasStrings.substring(0, 200);
		this.power_2_time = datasStrings.substring(200, 212);
	}

	public void setPower_3(String datasStrings) {
		this.power_3 = datasStrings.substring(0, 200);
		this.power_3_time = datasStrings.substring(200, 212);
	}

	public void setPower_4(String datasStrings) {
		this.power_4 = datasStrings.substring(0, 200);
		this.power_4_time = datasStrings.substring(200, 212);
	}

	public void setPf_1(String datasStrings) {
		this.pf_1 = datasStrings.substring(0, 200);
		this.pf_1_time = datasStrings.substring(200, 212);
	}

	public void setPf_2(String datasStrings) {
		this.pf_2 = datasStrings.substring(0, 200);
		this.pf_2_time = datasStrings.substring(200, 212);
	}

	public void setPf_3(String datasStrings) {
		this.pf_3 = datasStrings.substring(0, 200);
		this.pf_3_time = datasStrings.substring(200, 212);

	}

	public void setPf_4(String datasStrings) {
		this.pf_4 = datasStrings.substring(0, 200);
		this.pf_4_time = datasStrings.substring(200, 212);
	}

	/**
	 * 判断是否初始化完毕
	 * 
	 * @return
	 */
	private boolean isInitOk() {
		if (startTime == null || stopTime == null || basicTime == null
				|| chong_cheng_time == null || chong_ci == null || gtmk == null
				|| yjmk == null || zaihe_1 == null || zaihe_1_time == null
				|| zaihe_2 == null || zaihe_2_time == null || zaihe_3 == null
				|| zaihe_3_time == null || zaihe_4 == null
				|| zaihe_4_time == null || weiyi_1 == null
				|| weiyi_1_time == null || weiyi_2 == null
				|| weiyi_2_time == null || weiyi_3 == null
				|| weiyi_3_time == null || weiyi_4 == null
				|| weiyi_4_time == null

				|| ib_1 == null || ib_1_time == null || ib_2 == null
				|| ib_2_time == null || ib_3 == null || ib_3_time == null
				|| ib_4 == null || ib_4_time == null || power_1 == null
				|| power_1_time == null || power_2 == null
				|| power_2_time == null || power_3 == null
				|| power_3_time == null || power_4 == null
				|| power_4_time == null || pf_1 == null || pf_1_time == null
				|| pf_2 == null || pf_2_time == null || pf_3 == null
				|| pf_3_time == null || pf_4 == null || pf_4_time == null) {
			log.debug("【RawWellData】数据未采集或采集不完整");
			return false;
		} else {
			return true;
		}
	}

	public boolean isDGTInitOK() {
		if (dgt_1 == null || dgt_2 == null || dgt_3 == null || dgt_4 == null
				|| dgt_1_time == null || dgt_2_time == null
				|| dgt_3_time == null || dgt_4_time == null) {
			return false;
		} else {
			return true;
		}

	}

	public Date getStartTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
		try {
			Date d = sdf.parse(this.startTime);
			d.setYear(new Date().getYear());
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Date getStopTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmm");
		try {
			Date d = sdf.parse(this.stopTime);
			d.setYear(new Date().getYear());
			return d;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Date getDeviceTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		try {
			return sdf.parse(this.zaihe_1_time);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public float getChong_cheng_time() {
		return (float) (Integer.valueOf(this.chong_cheng_time) * 0.001);
	}

	public float getGtmk() {
		return (float) (Integer.valueOf(gtmk) * 0.001);
	}

	public float getYjmk() {
		return (float) (Integer.valueOf(yjmk) * 0.001);
	}

	/**
	 * 判断是否合法
	 * 
	 * @return
	 */
	public boolean isValid() {
		if (!isInitOk()) {
			return false;
		} else {
			// SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHH");
			// String currentTime =
			// sdf.format(Calendar.getInstance().getTime());
			// String standardTime = zaihe_1_time.substring(0, 8);
			// if (
			// // !standardTime.equals(basicTime.substring(0, 8))
			// !standardTime.equals(zaihe_2_time.substring(0, 8))
			// || !standardTime.equals(zaihe_3_time.substring(0, 8))
			// || !standardTime.equals(zaihe_4_time.substring(0, 8))
			// || !standardTime.equals(weiyi_1_time.substring(0, 8))
			// || !standardTime.equals(weiyi_2_time.substring(0, 8))
			// || !standardTime.equals(weiyi_3_time.substring(0, 8))
			// || !standardTime.equals(weiyi_4_time.substring(0, 8))
			// || !standardTime.equals(ib_1_time.substring(0, 8))
			// || !standardTime.equals(ib_2_time.substring(0, 8))
			// || !standardTime.equals(ib_3_time.substring(0, 8))
			// || !standardTime.equals(ib_4_time.substring(0, 8))
			// || !standardTime.equals(power_1_time.substring(0, 8))
			// || !standardTime.equals(power_2_time.substring(0, 8))
			// || !standardTime.equals(power_3_time.substring(0, 8))
			// || !standardTime.equals(power_4_time.substring(0, 8))
			// || !standardTime.equals(pf_1_time.substring(0, 8))
			// || !standardTime.equals(pf_2_time.substring(0, 8))
			// || !standardTime.equals(pf_3_time.substring(0, 8))
			// || !standardTime.equals(pf_4_time.substring(0, 8))) {
			// log.debug("【RawWellData】数据时间不一致");
			// return false;
			// } else {
			// return true;
			// }
			return true;
		}
	}

	public float[] getZaihe() {
		float zaihe[] = new float[200];
		boolean hexFlag = false;
		for (int i = 0; i < 50; i++) {
			try {
				int z1 = Integer
						.parseInt(zaihe_1.substring(i * 4, (i + 1) * 4));
				int z2 = Integer
						.parseInt(zaihe_2.substring(i * 4, (i + 1) * 4));
				int z3 = Integer
						.parseInt(zaihe_3.substring(i * 4, (i + 1) * 4));
				int z4 = Integer
						.parseInt(zaihe_4.substring(i * 4, (i + 1) * 4));
			} catch (NumberFormatException e) {
				hexFlag = true;
				e.printStackTrace();
				break;
			}
		}
		for (int i = 0; i < 50; i++) {
			if (!hexFlag) {// 10进制
				zaihe[49 - i] = (float) (Integer.valueOf(
						zaihe_1.substring(i * 4, (i + 1) * 4), 10) * 0.01);
				zaihe[99 - i] = (float) (Integer.valueOf(
						zaihe_2.substring(i * 4, (i + 1) * 4), 10) * 0.01);
				zaihe[149 - i] = (float) (Integer.valueOf(
						zaihe_3.substring(i * 4, (i + 1) * 4), 10) * 0.01);
				zaihe[199 - i] = (float) (Integer.valueOf(
						zaihe_4.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			} else {// 16进制
				zaihe[49 - i] = (float) (Integer.valueOf(
						zaihe_1.substring(i * 4, (i + 1) * 4 - 2), 16) + Integer
						.valueOf(zaihe_1.substring(i * 4 + 2, (i + 1) * 4), 10) * 0.01);
				zaihe[99 - i] = (float) (Integer.valueOf(
						zaihe_2.substring(i * 4, (i + 1) * 4 - 2), 16) + Integer
						.valueOf(zaihe_2.substring(i * 4 + 2, (i + 1) * 4), 10) * 0.01);
				zaihe[149 - i] = (float) (Integer.valueOf(
						zaihe_3.substring(i * 4, (i + 1) * 4 - 2), 16) + Integer
						.valueOf(zaihe_3.substring(i * 4 + 2, (i + 1) * 4), 10) * 0.01);
				zaihe[199 - i] = (float) (Integer.valueOf(
						zaihe_4.substring(i * 4, (i + 1) * 4 - 2), 16) + Integer
						.valueOf(zaihe_4.substring(i * 4 + 2, (i + 1) * 4), 10) * 0.01);
			}

		}
		return zaihe;
	}

	public float[] getDGT() {
		if (dgt_1 == null || dgt_2 == null || dgt_3 == null || dgt_4 == null) {
			return null;
		}
		float dgt[] = new float[200];
		// log.debug(dgt_1);
		// log.debug(dgt_2);
		// log.debug(dgt_3);
		// log.debug(dgt_4);
		try {
			for (int i = 0; i < 50; i++) {
				dgt[49 - i] = (float) (Integer.valueOf(
						dgt_1.substring(i * 4, (i + 1) * 4), 10) * 0.01);
				dgt[99 - i] = (float) (Integer.valueOf(
						dgt_2.substring(i * 4, (i + 1) * 4), 10) * 0.01);
				dgt[149 - i] = (float) (Integer.valueOf(
						dgt_3.substring(i * 4, (i + 1) * 4), 10) * 0.01);
				dgt[199 - i] = (float) (Integer.valueOf(
						dgt_4.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("电功图处理BUG:", e);
			// for(float f:dgt) {
			// log.debug(f);
			// }

		}
		return dgt;
	}

	public float[] getWeiyi() {
		float weiyi[] = new float[200];
		for (int i = 0; i < 50; i++) {
			weiyi[49 - i] = (float) (Integer.valueOf(
					weiyi_1.substring(i * 4, (i + 1) * 4), 10) * 0.001);
			weiyi[99 - i] = (float) (Integer.valueOf(
					weiyi_2.substring(i * 4, (i + 1) * 4), 10) * 0.001);
			weiyi[149 - i] = (float) (Integer.valueOf(
					weiyi_3.substring(i * 4, (i + 1) * 4), 10) * 0.001);
			weiyi[199 - i] = (float) (Integer.valueOf(
					weiyi_4.substring(i * 4, (i + 1) * 4), 10) * 0.001);
		}
		return weiyi;
	}

	public float[] getIB() {
		float ib[] = new float[200];
		for (int i = 0; i < 50; i++) {
			ib[49 - i] = (float) (Integer.valueOf(
					ib_1.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			ib[99 - i] = (float) (Integer.valueOf(
					ib_2.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			ib[149 - i] = (float) (Integer.valueOf(
					ib_3.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			ib[199 - i] = (float) (Integer.valueOf(
					ib_4.substring(i * 4, (i + 1) * 4), 10) * 0.01);
		}
		return ib;
	}

	public float[] getPower() {
		float power[] = new float[200];
		for (int i = 0; i < 50; i++) {
			power[49 - i] = (float) (Integer.valueOf(
					power_1.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			power[99 - i] = (float) (Integer.valueOf(
					power_2.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			power[149 - i] = (float) (Integer.valueOf(
					power_3.substring(i * 4, (i + 1) * 4), 10) * 0.01);
			power[199 - i] = (float) (Integer.valueOf(
					power_4.substring(i * 4, (i + 1) * 4), 10) * 0.01);
		}
		return power;
	}

	public float[] getPf() {
		float pf[] = new float[200];
		for (int i = 0; i < 50; i++) {
			pf[49 - i] = (float) (Integer.valueOf(
					pf_1.substring(i * 4, (i + 1) * 4), 10) * 0.001);
			pf[99 - i] = (float) (Integer.valueOf(
					pf_2.substring(i * 4, (i + 1) * 4), 10) * 0.001);
			pf[149 - i] = (float) (Integer.valueOf(
					pf_3.substring(i * 4, (i + 1) * 4), 10) * 0.001);
			pf[199 - i] = (float) (Integer.valueOf(
					pf_4.substring(i * 4, (i + 1) * 4), 10) * 0.001);
		}
		return pf;
	}

	public String getWell_num() {
		return well_num;
	}

	public void setWell_num(String well_num) {
		this.well_num = well_num;
	}

	public void setDgt_1(String datasStrings) {
		this.dgt_1 = datasStrings.substring(0, 200);
		this.dgt_1_time = datasStrings.substring(200, 212);
	}

	public void setDgt_2(String datasStrings) {
		this.dgt_2 = datasStrings.substring(0, 200);
		this.dgt_2_time = datasStrings.substring(200, 212);
	}

	public void setDgt_3(String datasStrings) {
		this.dgt_3 = datasStrings.substring(0, 200);
		this.dgt_3_time = datasStrings.substring(200, 212);
	}

	public void setDgt_4(String datasStrings) {
		this.dgt_4 = datasStrings.substring(0, 200);
		this.dgt_4_time = datasStrings.substring(200, 212);
	}

	public void setNull() {
		startTime = null; // 上次开井时间
		stopTime = null; // 上次停井时间
		basicTime = null; // 基本数据时间 yyMMddHHmmss
		chong_cheng_time = null;
		chong_ci = null;
		gtmk = null; // 功图模块电压
		yjmk = null; // 油井模块电压

		zaihe_1 = null;
		zaihe_1_time = null;
		zaihe_2 = null;
		zaihe_2_time = null;
		zaihe_3 = null;
		zaihe_3_time = null;
		zaihe_4 = null;
		zaihe_4_time = null;

		weiyi_1 = null;
		weiyi_1_time = null;
		weiyi_2 = null;
		weiyi_2_time = null;
		weiyi_3 = null;
		weiyi_3_time = null;
		weiyi_4 = null;
		weiyi_4_time = null;

		ib_1 = null;
		ib_1_time = null;
		ib_2 = null;
		ib_2_time = null;
		ib_3 = null;
		ib_3_time = null;
		ib_4 = null;
		ib_4_time = null;

		power_1 = null;
		power_1_time = null;
		power_2 = null;
		power_2_time = null;
		power_3 = null;
		power_3_time = null;
		power_4 = null;
		power_4_time = null;

		pf_1 = null;
		pf_1_time = null;
		pf_2 = null;
		pf_2_time = null;
		pf_3 = null;
		pf_3_time = null;
		pf_4 = null;
		pf_4_time = null;

		dgt_1 = null;
		dgt_1_time = null;
		dgt_2 = null;
		dgt_2_time = null;
		dgt_3 = null;
		dgt_3_time = null;
		dgt_4 = null;
		dgt_4_time = null;
	}

}
