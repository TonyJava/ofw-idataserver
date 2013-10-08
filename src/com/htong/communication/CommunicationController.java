package com.htong.communication;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;

import com.htong.communication.dl645.DL645Channel;
import com.htong.domain.ChannelModel;
import com.htong.idataserver.TagDataBase;
import com.htong.status.DTUStatus;

/**
 * 通讯控制器，采集数据， 转发数据
 * 
 * @author 薄成文
 * @author 赵磊
 */
public enum CommunicationController {
	INSTANCE;
	
	private CommunicationController() {
		//init();
	}

	private final Logger log = Logger
			.getLogger(CommunicationController.class);

	private List<Thread> channelThreadList = new LinkedList<Thread>();
	private Map<String, IChannel> channelMap = new HashMap<String, IChannel>();	//key为dtuID

	private boolean runing = false;// 通讯状态

	private void init() {
		log.info("初始化通讯控制器");

		this.initChannelComm();// 采集程序
		this.initTagVar();// 中间变量计算程序, 监听器程序

		this.initTransChannelComm();// 转发程序
	}

	/**
	 * 初始化采集通道
	 */
	private void initChannelComm() {
		channelThreadList.clear();
		Set<Map.Entry<String, ChannelModel>> entryseSet = TagDataBase.INSTANCE
				.getWellChannelMap().entrySet();
		for (Map.Entry<String, ChannelModel> entry : entryseSet) {
			ChannelModel channel = entry.getValue();

			if (TagDataBase.INSTANCE.getDeviceListMap().get(channel.getOid()) != null
					&& !TagDataBase.INSTANCE.getDeviceListMap()
							.get(channel.getOid()).isEmpty()) {// 通道下有设备
				Protocal protocal = Protocal.valueOf(channel.getProtocal()
						.toUpperCase());
				IChannel commChannel = null;
				switch (protocal) {
				case MODBUS_RTU:
				case MODBUS_TCP:
					// commChannel = new ModbusChannel(channel);

					break;
				case DL645:
				case DL645_DTU:
					commChannel = new DL645Channel(channel);
					break;

				default:
					break;
				}

				if (commChannel != null) {
					Thread thread = new Thread(commChannel);
					thread.setDaemon(false);
					this.channelThreadList.add(thread);
					this.channelMap.put(channel.getName(), commChannel);
				}
			}
		}
	}

	private void initTagVar() {

	}

	private void initTransChannelComm() {
	}

	/**
	 * 开启服务
	 */
	public void startService() {
		if (this.runing) {
			return;
		}		
		init();
		log.info("启动采集程序: " + this.channelThreadList.size());
		// 设置通讯状态为1
		this.runing = true;
		// 启动线程
		for (Thread thread : this.channelThreadList) {
			thread.start();
		}
	}

	/**
	 * 停止服务
	 */
	public void stopService() {
		if (this.runing == false) {
			return;
		}

		this.runing = false;

		for (Thread thread : this.channelThreadList) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
		log.info("通讯服务已停止");
	}

	public boolean isRuning() {
		return this.runing;
	}
	
	//所有线程都执行一次查询
	public void executeOneQuery() {
		if(this.runing) {
			for(IChannel i : channelMap.values()) {
				if(i instanceof DL645Channel) {
					DL645Channel dl645 = (DL645Channel)i;
					if(DTUStatus.instance.getCommStatusMap()!=null && 
							DTUStatus.instance.getCommStatusMap().get(dl645.getChannel().getDtuId())!=null && 
							DTUStatus.instance.getCommStatusMap().get(dl645.getChannel().getDtuId())) {//在通讯中
						dl645.excuteOneQuery();
					} else {
						continue;
					}
				}
			}
			MessageDialog.openInformation(null, "提示", "采集数据成功！");
		}
	}
	
	public boolean executeOneQuery(String dtuId, String deviceId) {
		if(this.runing) {
			for(IChannel i : channelMap.values()) {
				if(i instanceof DL645Channel) {
					DL645Channel dl645 = (DL645Channel)i;
					if(dl645.getChannel().getDtuId().equals(dtuId) &&DTUStatus.instance.getCommStatusMap()!=null && 
							DTUStatus.instance.getCommStatusMap().get(dl645.getChannel().getDtuId())!=null&& DTUStatus.instance.getCommStatusMap().get(dl645.getChannel().getDtuId())) {
						log.debug(deviceId);
						dl645.excuteOneQuery(deviceId);
						return true;
					}
				}
				
			}
			log.debug("不存在该DTU号的采油井！");
			MessageDialog.openWarning(null, "错误", "不存在该DTU号的采油井！");
		}
		return false;
	}

	public void executeYK(String tagPathKey) {
		// IOTagModel tagVar =
		// TagDataBase.INSTANCE.getIoTagVarMap().get(tagPathKey);
		// if (tagVar == null) {
		// return;
		// }
		//
		// IChannel channel =
		// this.commChannelMap.get(tagVar.getDeviceModel().getChannel().getName());
		// if (channel == null) {
		// return;
		// }
		//
		// channel.executeYK(tagPathKey);
	}

	/**
	 * 执行广播复位
	 */
	public void executeBroadCastReset() {
	}

	public List<Thread> getChannelThreadList() {
		return this.channelThreadList;
	}

}
