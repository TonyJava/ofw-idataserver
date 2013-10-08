package com.htong.idataserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.htong.domain.ChannelModel;
import com.htong.domain.DeviceModel;
import com.htong.domain.IndexNodeModel;
import com.htong.domain.WellModel;
import com.htong.persist.PersistManager;
import com.htong.util.CollectionConstants;
/**
 * 初始化信息
 * @author 赵磊
 *
 */
public enum TagDataBase {
	INSTANCE;

	private static final Logger log = Logger.getLogger(TagDataBase.class);

	List<IndexNodeModel> indexNodeList;
	
	private Map<String, IndexNodeModel> indexNodeMap = new HashMap<String, IndexNodeModel>();// 索引节点映射，key为索引节点路径名称
	// private Map<String, WellModel> wellModelMap = new HashMap<String,
	// WellModel>(); //井，key井号

	private List<WellModel> wellList = new ArrayList<WellModel>();
	private List<DeviceModel> deviceList = new ArrayList<DeviceModel>();
	private List<ChannelModel> channelList = new ArrayList<ChannelModel>();

	private Map<String, DeviceModel> wellDeviceMap = new HashMap<String, DeviceModel>();// 设备，key为井号
	private Map<String, ChannelModel> deviceChannelMap = new HashMap<String, ChannelModel>();// 通道，key为设备oid
	private Map<String, ChannelModel> wellChannelMap = new HashMap<String, ChannelModel>(); // 通道，key为井号

	private Map<String, List<DeviceModel>> deviceListMap = new HashMap<String, List<DeviceModel>>();// key为通道号
	

	/**
	 * 初始化
	 */
	public void init() {
		// 初始化索引
		log.debug("*************************初始化索引信息...");
		Query query = new Query(Criteria.where("name").exists(true));
		indexNodeList = PersistManager.INSTANCE.getMongoTemplate()
					.find(CollectionConstants.TAG_COLLECTION_NAME, query,
							IndexNodeModel.class);
		if (indexNodeList != null && !indexNodeList.isEmpty()) {
			for(IndexNodeModel indexNode : indexNodeList) {
				initIndexNodeMap(indexNode, "");
			}
		}
		log.debug("索引对象个数为：" + String.valueOf(indexNodeMap.size()));
		log.debug("【索引信息初始化完毕】");

		// 初始化井
		log.debug("*************************初始化井信息...");
		Query wellQuery = new Query(Criteria.where("num").exists(true));
		this.wellList = PersistManager.INSTANCE.getMongoTemplate().find(
				CollectionConstants.WELL_COLLECTION_NAME, wellQuery,
				WellModel.class);
		if (!wellList.isEmpty()) {
			log.debug("井数量为：".concat(String.valueOf(wellList.size())));
		}
		log.debug("【井信息初始化完毕】");

		// 初始化设备
		log.debug("*************************初始化设备信息...");
		Query deviceQuery = new Query(Criteria.where("oid").exists(true));
		this.deviceList = PersistManager.INSTANCE.getMongoTemplate().find(
				CollectionConstants.DEVICE_COLLECTION_NAME, deviceQuery,
				DeviceModel.class);
		if (!deviceList.isEmpty()) {
			log.debug("设备数量为：".concat(String.valueOf(deviceList.size())));
		}
		log.debug("【设备信息初始化完毕】");

		// 初始化采集通道
		log.debug("*************************初始化采集通道信息...");
		Query channelQuery = new Query(Criteria.where("oid").exists(true));
		this.channelList = PersistManager.INSTANCE.getMongoTemplate().find(
				CollectionConstants.CHANNEL_COLLECTION_NAME, channelQuery,
				ChannelModel.class);
		if (!channelList.isEmpty()) {
			log.debug("通道数量为：".concat(String.valueOf(channelList.size())));
		}
		log.debug("【采集通道信息初始化完毕】");
		
		//初始化井-设备=通道关联
		log.debug("*************************关联井-设备-采集通道...");
		for(WellModel well : wellList) {
			for(DeviceModel device:deviceList) {
				if(well.getDeviceOid().equals(device.getOid())) {
					this.wellDeviceMap.put(well.getNum(), device);
					log.debug("关联【井号】： " + well.getNum() + "与【设备oid】:" + device.getOid());
					for(ChannelModel channel:channelList) {
						if(channel.getName().equals(device.getChannelName())) {
							this.wellChannelMap.put(well.getNum(), channel);
							log.debug("关联【井号】： " + well.getNum() + "与【采集通道】:" + channel.getName());
							this.deviceChannelMap.put(device.getOid(), channel);
							log.debug("关联【设备oid】： " + device.getOid() + "与【采集通道】:" + channel.getName());
							if(this.deviceListMap.get(channel.getOid()) == null) {
								List<DeviceModel> deviceModelList = new ArrayList<DeviceModel>();
								deviceModelList.add(device);
								this.deviceListMap.put(channel.getOid(), deviceModelList);
							} else {
								this.deviceListMap.get(channel.getOid()).add(device);
							}
							log.debug("通道oid:"+channel.getOid()+"的设备数：" + this.deviceListMap.get(channel.getOid()).size());
							break;
						}
					}
					break;
				}
			}
		}
		log.debug("【关联井-设备-采集通道完毕】");

	}

	/**
	 * 初始化索引
	 * 递归调用
	 * @param indexNode
	 */
	private void initIndexNodeMap(IndexNodeModel indexNode, String parentPath) {
//		indexNode.setParentObject(null);
		
		String key = parentPath + indexNode.getName();
		log.debug("初始化索引对象：" + key);
		indexNodeMap.put(key, indexNode);
		key = key + "/";

		if (indexNode.getIndexNodes() != null
				&& !indexNode.getIndexNodes().isEmpty()) {
			List<IndexNodeModel> indexNodeList = indexNode.getIndexNodes();
			for (IndexNodeModel inm : indexNodeList) {
				initIndexNodeMap(inm, key);
			}
		}
	}

	public Map<String, IndexNodeModel> getIndexNodeMap() {
		return indexNodeMap;
	}

	public void setIndexNodeMap(Map<String, IndexNodeModel> indexNodeMap) {
		this.indexNodeMap = indexNodeMap;
	}

	public Map<String, DeviceModel> getWellDeviceMap() {
		return wellDeviceMap;
	}

	public void setWellDeviceMap(Map<String, DeviceModel> wellDeviceMap) {
		this.wellDeviceMap = wellDeviceMap;
	}

	public Map<String, ChannelModel> getWellChannelMap() {
		return wellChannelMap;
	}

	public void setWellChannelMap(Map<String, ChannelModel> wellChannelMap) {
		this.wellChannelMap = wellChannelMap;
	}

	public Map<String, ChannelModel> getDeviceChannelMap() {
		return deviceChannelMap;
	}

	public void setDeviceChannelMap(Map<String, ChannelModel> deviceChannelMap) {
		this.deviceChannelMap = deviceChannelMap;
	}

	public Map<String, List<DeviceModel>> getDeviceListMap() {
		return deviceListMap;
	}

	public void setDeviceListMap(Map<String, List<DeviceModel>> deviceListMap) {
		this.deviceListMap = deviceListMap;
	}

	public static void main(String args[]) {
		TagDataBase.INSTANCE.init();
	}

	public List<IndexNodeModel> getIndexNodeList() {
		return indexNodeList;
	}

	public void setIndexNodeList(List<IndexNodeModel> indexNodeList) {
		this.indexNodeList = indexNodeList;
	}

	public List<WellModel> getWellList() {
		return wellList;
	}

	public void setWellList(List<WellModel> wellList) {
		this.wellList = wellList;
	}
	
	

}
