package com.htong.status;

import java.util.HashMap;
import java.util.Map;

/**
 * DTU状态
 * @author 赵磊
 *
 */
public enum DTUStatus {
	instance;
	
	private Map<String, Boolean> dtuStatusMap = new HashMap<String, Boolean>();//连接状态
	private Map<String, String> heartBeatMap = new HashMap<String, String>();//最近心跳时间
	private Map<String, Boolean> commStatusMap = new HashMap<String, Boolean>();//通讯状态

	public Map<String, Boolean> getDtuStatusMap() {
		return dtuStatusMap;
	}

	public synchronized void putDtuStatusMap(String dtuId, Boolean b) {
		this.dtuStatusMap.put(dtuId, b);
	}

	public Map<String, String> getHeartBeatMap() {
		return heartBeatMap;
	}

	public synchronized void putHeartBeatMap(String dtuId, String time) {
		this.heartBeatMap.put(dtuId, time);
	}

	public Map<String, Boolean> getCommStatusMap() {
		return commStatusMap;
	}

	public synchronized void putCommStatusMap(String dtuId, Boolean b) {
		this.commStatusMap.put(dtuId, b);
	}
	
	
}
