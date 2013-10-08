package com.htong.persist;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.htong.domain.WellData;
import com.htong.util.CollectionConstants;

public class WellDataDao {
	private static final Logger log = Logger.getLogger(WellDataDao.class);
	/**
	 * 数据写库
	 */
	public void writeToDatabase(WellData wellData) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyMM");
//		String time = sdf.format(Calendar.getInstance().getTime());
//		PersistManager.INSTANCE.getMongoTemplate().insert(
//				CollectionConstants.WELL_DATA + "_" + wellData.getWell_num() + "_"
//						+ time, wellData);
		PersistManager.INSTANCE.getMongoTemplate().insert(
				CollectionConstants.WELL_DATA + "_" + wellData.getWell_num(),wellData);
		log.debug("写入数据成功" + CollectionConstants.WELL_DATA + "_" + wellData.getWell_num());
	}

}
