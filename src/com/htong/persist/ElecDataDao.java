package com.htong.persist;

import org.apache.log4j.Logger;

import com.htong.domain.ElecData;
import com.htong.util.CollectionConstants;

public class ElecDataDao {
	private static final Logger log = Logger.getLogger(ElecDataDao.class);
	/**
	 * 数据写库
	 */
	public void writeToDatabase(ElecData elecData) {
		PersistManager.INSTANCE.getMongoTemplate().insert(CollectionConstants.ELEC_DATA, elecData);
		log.debug("写入数据成功" + CollectionConstants.ELEC_DATA);
	}
}
