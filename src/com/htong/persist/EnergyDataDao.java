package com.htong.persist;

import org.apache.log4j.Logger;

import com.htong.domain.EnergyData;
import com.htong.util.CollectionConstants;

public class EnergyDataDao {
	private static final Logger log = Logger.getLogger(EnergyDataDao.class);
	/**
	 * 数据写库
	 */
	public void writeToDatabase(EnergyData energyData) {
		PersistManager.INSTANCE.getMongoTemplate().insert(CollectionConstants.ENERGY_DATA, energyData);
		log.debug("写入数据成功" + CollectionConstants.ENERGY_DATA);
	}
}
