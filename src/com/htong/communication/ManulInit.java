package com.htong.communication;

import com.htong.domain.ChannelModel;
import com.htong.domain.DeviceModel;
import com.htong.persist.PersistManager;
import com.htong.util.CollectionConstants;

public class ManulInit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		DeviceModel deviceModel = new DeviceModel();
		deviceModel.setName("设备1");
		deviceModel.setRetry("2");
		deviceModel.setSlaveId("1");
		deviceModel.setUsed("true");
		deviceModel.setTimeout("1000");
		deviceModel.setOid("1");
		deviceModel.setChannelName("采集通道1");
		

		
		ChannelModel channelModel = new ChannelModel();

		channelModel.setBaud("");
		channelModel.setComPort("");
		channelModel.setDataBit("");
		channelModel.setDtuId("069");
		channelModel.setHeartBeat("hello");
		channelModel.setInterval("100");
		channelModel.setIp("202.118.66.6");
		channelModel.setLoopInterval("600");
		channelModel.setName("采集通道1");
		channelModel.setOffline("5");
		channelModel.setParity("");
		channelModel.setProtocal("DL645_DTU");
		channelModel.setStopBit("");
		channelModel.setTcpPort("2012");
		

		
		PersistManager.INSTANCE.getMongoTemplate().insert(CollectionConstants.CHANNEL_COLLECTION_NAME,channelModel);
		PersistManager.INSTANCE.getMongoTemplate().insert(CollectionConstants.DEVICE_COLLECTION_NAME, deviceModel);

	}

}
