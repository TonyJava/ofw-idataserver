package com.htong.persist;

import java.math.BigDecimal;
import java.util.Date;

import com.htong.domain.SQLData;
import com.htong.domain.SQLWell;
import com.htong.domain.WellData;

public class WriteToSQLServer {
	private SQLWellDao wellDao = new SQLWellDao();
	private SQLDataDao dataDao = new SQLDataDao();
	
	public void writeToSQL(String dtuId, WellData wellData) {
		SQLWell well = wellDao.getWellByDtuId(dtuId);
		if(well == null) {
			//不存在该井
			System.out.println("SQL数据库里无该井！");
			return;
		}
		
		SQLData data = new SQLData();
		
		float chongci;
		if(wellData.getChong_cheng_time()<1) {
			chongci = 0;
		} else {
			chongci = 60/wellData.getChong_cheng_time();
		}
		data.setChongci(chongci);
		System.out.println("冲次：" + chongci);
		
		StringBuilder sbWeiyi = new StringBuilder();
		for(float f : wellData.getWeiyi()) {
			BigDecimal bd = new BigDecimal(f);
			float newWeiyi = bd.setScale(3, BigDecimal.ROUND_HALF_UP)
					.floatValue();
			sbWeiyi.append(String.valueOf(newWeiyi)+",");
		}
		data.setWeiyi(sbWeiyi.toString().substring(0, sbWeiyi.toString().length()-1));
		System.out.println("位移：" + sbWeiyi.toString().substring(0, sbWeiyi.toString().length()-1));
		
		StringBuilder sbZaihe = new StringBuilder();
		for(float f : wellData.getZaihe()) {
			BigDecimal bd = new BigDecimal(f);
			float newZaihe = bd.setScale(2, BigDecimal.ROUND_HALF_UP)
					.floatValue();
			sbZaihe.append(String.valueOf(newZaihe)+",");
		}
		data.setZaihe(sbZaihe.toString().substring(0, sbZaihe.toString().length()-1));
		System.out.println("载荷：" + sbZaihe.toString().substring(0, sbZaihe.toString().length()-1));
		
		data.setTime(wellData.getDevice_time());
		data.setSaveTime(new Date());
		
		data.setSqlWell(well);
		dataDao.insert(data);
		System.out.println("成功写入SQL Server数据库");
	}

}
