package com.htong.communication.dl645;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.document.mongodb.query.Criteria;
import org.springframework.data.document.mongodb.query.Query;

import com.ht.dl645.AbstractDL645Master;
import com.ht.dl645.DL645DTUMaster;
import com.ht.dl645.DL645RTUMaster;
import com.ht.dl645.dtu.DL645DTUServer;
import com.ht.dl645.service.serial.SerialAddress;
import com.ht.dl645.service.serial.SerialAddress.DataBits;
import com.ht.dl645.service.serial.SerialAddress.FlowControl;
import com.ht.dl645.service.serial.SerialAddress.Parity;
import com.ht.dl645.service.serial.SerialAddress.StopBits;
import com.htong.alg.SGTDataComputerProcess;
import com.htong.communication.CommConstants;
import com.htong.communication.CommunicationController;
import com.htong.communication.IChannel;
import com.htong.communication.Protocal;
import com.htong.domain.ChannelModel;
import com.htong.domain.DeviceModel;
import com.htong.domain.ElecData;
import com.htong.domain.ElecDataMSSQL;
import com.htong.domain.EnergyData;
import com.htong.domain.EnergyDataMSSQL;
import com.htong.domain.WellData;
import com.htong.domain.WellModel;
import com.htong.idataserver.RawElecData;
import com.htong.idataserver.RawEnergyData;
import com.htong.idataserver.RawWellData;
import com.htong.idataserver.TagDataBase;
import com.htong.persist.ElecDataDao;
import com.htong.persist.ElecDataDaoMSSQL;
import com.htong.persist.EnergyDataDao;
import com.htong.persist.EnergyDataDaoMSSQL;
import com.htong.persist.PersistManager;
import com.htong.persist.WellDataDao;
import com.htong.persist.WellModelDao;
import com.htong.persist.WriteToSQLServer;
import com.htong.util.CollectionConstants;

/**
 * 生成采集通道的查询信息、变量信息
 * 
 * @author 赵磊
 * @author 薄成文
 */
public class DL645Channel implements IChannel {
	private int wellDataSaveInterval = 29*60;//3600;
	private int energyDataSaveInterval = 24*60*60;//3600*24;
	private int elecDataSaveInterval = 12*60*60;//3600*24;
	
	private static final Logger log = Logger.getLogger(DL645Channel.class);
	
	private Map<String,Boolean> deviceStateMap = new HashMap<String, Boolean>(); //key为设备oid
	
	//private Map<String,WellData> wellDataMap = new HashMap<String, WellData>();	//key为设备oid
	private Map<String, RawWellData> rawWellDataMap = new HashMap<String, RawWellData>();	//key为设备oid
	
	private Map<String, RawElecData> rawElecDataMap = new HashMap<String, RawElecData>();
	
	private Map<String, RawEnergyData> rawEnergyDataMap = new HashMap<String, RawEnergyData>();

	private AbstractDL645Master master;
	private ChannelModel channel; // 采集通道
	private Timer dateAdjust;// 广播校时实时器
	
	private List<DL645FrameInfo> queryList = new LinkedList<DL645FrameInfo>();// 实时查询列表


	public DL645Channel(ChannelModel channel) {
		this.channel = channel;
		this.initDevices();
	}

	@Override
	public void run() {
		log.info("采集通道【" + channel.getName() + "】线程启动");

		open();// 打开采集通道

		while (CommunicationController.INSTANCE.isRuning()) {
			if (master != null && !master.isOpened()) {
				try {
					master.open();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				continue;
			}
			if (!CommunicationController.INSTANCE.isRuning()) {
				break;
			}
			
			for (DL645FrameInfo frame : this.queryList) {
				if (!CommunicationController.INSTANCE.isRuning()) {
					break;
				}
				if(System.currentTimeMillis()>=frame.getEndTime()) {
					try {
						frame.setEndTime(System.currentTimeMillis() + frame.getFrameLoopInterval()*1000);//增加循环间隔
					} catch (Exception e1) {
						log.error(e1);
						e1.printStackTrace();
					}
					try {
						frame.executeReadRequest();// 执行遥测采集
					} catch (Exception e) {
						log.error("解析错误" + e.getMessage());
						log.error("异常信息：", e);
						e.printStackTrace();
					}
				}
			}
			
			//wellData写库
			Set<Map.Entry<String, RawWellData>> mapEntry = rawWellDataMap.entrySet();
			for(Map.Entry<String, RawWellData> entry : mapEntry) {
				RawWellData rawWellData = entry.getValue();	//原始数据

				if(rawWellData.isValid()) {
					log.debug(rawWellData.getWell_num()+"初始化数据");
					WellData wellData = new WellData();
					wellData.setWell_num(rawWellData.getWell_num());//设置井号
					
					wellData.setChong_cheng_time(rawWellData.getChong_cheng_time());
					wellData.setChong_ci(rawWellData.getChong_ci());
					wellData.setStart_time(rawWellData.getStartTime());
					wellData.setStop_time(rawWellData.getStopTime());
					wellData.setGtmk(rawWellData.getGtmk());
					wellData.setYjmk(rawWellData.getYjmk());
					log.debug(rawWellData.getZaihe() == null);
					
					wellData.setZaihe(rawWellData.getZaihe());
					wellData.setWeiyi(rawWellData.getWeiyi());
					wellData.setPower(rawWellData.getPower());
					wellData.setPower_factor(rawWellData.getPf());
					wellData.setIb(rawWellData.getIB());
					wellData.setSave_time(Calendar.getInstance().getTime());
					wellData.setDevice_time(rawWellData.getDeviceTime());
					
//					Query query = new Query(Criteria.where("num").is(wellData.getWell_num()));
//					WellModel wellModel = PersistManager.INSTANCE.getMongoTemplate().findOne(CollectionConstants.WELL_COLLECTION_NAME, query, WellModel.class);
					
//					WellModel wellModel = TagDataBase.INSTANCE.getWellList().
					
					//计算产液量
//					if(wellData.getZaihe()[0]<0.5 && wellData.getZaihe()[1]<0.5 && wellData.getZaihe()[2]<0.5) {
//						log.debug("所有数据均为0，不能写入产液量");
//					} else {
//						try {
//							SGTDataComputerProcess scp = new SGTDataComputerProcess();
//							Map<String, Object> resultMap = scp.calcSGTData(wellData.getWeiyi().clone(), wellData.getZaihe().clone(), 0, wellData.getChong_cheng_time(), 
//									Float.valueOf(wellModel.getBengjing()),
//									Float.valueOf(wellModel.getOilDensity()),
//									Float.valueOf(wellModel.getHanshui()));
//							wellData.setLiquidProduct((Float)resultMap.get("liquidProduct"));
//						} catch (NumberFormatException e) {
//							wellData.setLiquidProduct(0);
//							e.printStackTrace();
//						}
//					}
					
					
					if(rawWellData.getDGT() != null) {
						//log.debug("set电功图数据");
						wellData.setDgt(rawWellData.getDGT());	//电功图
					}
					try {
						
						new WellDataDao().writeToDatabase(wellData);//写库
						
						WellModelDao wellDao = new WellModelDao();
						WellModel wellModel = wellDao.getWellByNum(rawWellData.getWell_num());
						new WriteToSQLServer().writeToSQL(wellModel.getDtuId(),wellData);		//写SQL库
						
						rawWellData.setNull();//清空数据
					} catch (Exception e) {
						// TODO Auto-generated catch block
						log.error(e);
						e.printStackTrace();
					}
				} else {
					log.debug(rawWellData.getWell_num()+"【WellData】不合法，未能写入数据库");
				}
			}
			//EnergyData写库
//			Set<Map.Entry<String, RawEnergyData>> energyMapEntry = rawEnergyDataMap.entrySet();
//			for(Map.Entry<String, RawEnergyData> entry : energyMapEntry) {
//				if(entry.getValue().isValid()) {
//					RawEnergyData rawEnergyData = entry.getValue();	//原始数据
//					EnergyData energyData = new EnergyData();
//					energyData.setWell_num(rawEnergyData.getWell_num());//设置井号
//					
//					energyData.setZxygZ(rawEnergyData.getZxygZ());
//					energyData.setFxygZ(rawEnergyData.getFxygZ());
//					energyData.setZxwgZ(rawEnergyData.getZxwgZ());
//					energyData.setFxwgZ(rawEnergyData.getFxwgZ());
//					
//					energyData.setZxygJ(rawEnergyData.getZxygJ());
//					energyData.setFxygJ(rawEnergyData.getFxygJ());
//					energyData.setZxwgJ(rawEnergyData.getZxwgJ());
//					energyData.setFxwgJ(rawEnergyData.getFxwgJ());
//					
//					energyData.setZxygF(rawEnergyData.getZxygF());
//					energyData.setFxygF(rawEnergyData.getFxygF());
//					energyData.setZxwgF(rawEnergyData.getZxwgF());
//					energyData.setFxwgF(rawEnergyData.getFxwgF());
//					
//					energyData.setZxygP(rawEnergyData.getZxygP());
//					energyData.setFxygP(rawEnergyData.getFxygP());
//					energyData.setZxwgP(rawEnergyData.getZxwgP());
//					energyData.setFxwgP(rawEnergyData.getFxwgP());
//					
//					energyData.setZxygG(rawEnergyData.getZxygG());
//					energyData.setFxygG(rawEnergyData.getFxygG());
//					energyData.setZxwgG(rawEnergyData.getZxwgG());
//					energyData.setFxwgG(rawEnergyData.getFxwgG());
//					
//					energyData.setSyZxygZ(rawEnergyData.getSYZxygZ());
//					energyData.setSyFxygZ(rawEnergyData.getSYFxygZ());
//					energyData.setSyZxwgZ(rawEnergyData.getSYZxwgZ());
//					energyData.setSyFxwgZ(rawEnergyData.getSYFxwgZ());
//					
//					energyData.setSave_time(Calendar.getInstance().getTime());
//					
//					rawEnergyData.setNull();//清空数据
//					
//					new EnergyDataDao().writeToDatabase(energyData);	//写库
//					
//					//写sql server 2005
//					Query query = new Query(Criteria.where("num").is(energyData.getWell_num()));
//					WellModel wellModel = PersistManager.INSTANCE.getMongoTemplate().findOne(CollectionConstants.WELL_COLLECTION_NAME, query, WellModel.class);
//					
//					new EnergyDataDaoMSSQL().insert(new EnergyDataMSSQL(energyData,wellModel.getDtuId(),wellModel.getSlaveId()));
//				
//				} else {
//					//log.debug("【EnergyData】未到采集时间或数据不合法，未能写入数据库");
//				}
//			}
			
			//ElecData写库
//			Set<Map.Entry<String, RawElecData>> elecMapEntry = rawElecDataMap.entrySet();
//			for(Map.Entry<String, RawElecData> entry : elecMapEntry) {
//				if(entry.getValue().isValid()) {
//					RawElecData rawElecData = entry.getValue();	//原始数据
//					ElecData elecData = new ElecData();
//					elecData.setWell_num(rawElecData.getWell_num());//设置井号
//					
//					elecData.setUa(rawElecData.getUA());
//					elecData.setUb(rawElecData.getUB());
//					elecData.setUc(rawElecData.getUC());
//					elecData.setUab(rawElecData.getUab());
//					elecData.setUbc(rawElecData.getUbc());
//					elecData.setUca(rawElecData.getUca());
//					elecData.setIa(rawElecData.getIA());
//					elecData.setIb(rawElecData.getIB());
//					elecData.setIc(rawElecData.getIC());
//					
//					elecData.setShygglZ(rawElecData.getSHYgglZ());
//					elecData.setShygglA(rawElecData.getSHYgglA());
//					elecData.setShygglB(rawElecData.getSHYgglB());
//					elecData.setShygglC(rawElecData.getSHYgglC());
//					
//					elecData.setShwgglZ(rawElecData.getSHWgglZ());
//					elecData.setShwgglA(rawElecData.getSHWgglA());
//					elecData.setShwgglB(rawElecData.getSHWgglB());
//					elecData.setShwgglC(rawElecData.getSHWgglC());
//					
//					elecData.setGlysZ(rawElecData.getGLYSZ());
//					elecData.setGlysA(rawElecData.getGLYSA());
//					elecData.setGlysB(rawElecData.getGLYSB());
//					elecData.setGlysC(rawElecData.getGLYSC());
//					
//					elecData.setPinlv(rawElecData.getPinLv());
//					elecData.setLxdl(rawElecData.getLXDL());
//					elecData.setLxdy(rawElecData.getLXDY());
//					elecData.setDlbphd(rawElecData.getDLBPHD());
//					elecData.setDybphd(rawElecData.getDYBPHD());
//					
//					elecData.setSave_time(Calendar.getInstance().getTime());
//					
//					rawElecData.setNull();//清空数据
//					
//					new ElecDataDao().writeToDatabase(elecData);	//写库
//					
//					//写sql server 2005
//					Query query = new Query(Criteria.where("num").is(elecData.getWell_num()));
//					WellModel wellModel = PersistManager.INSTANCE.getMongoTemplate().findOne(CollectionConstants.WELL_COLLECTION_NAME, query, WellModel.class);
//					
//					new ElecDataDaoMSSQL().insert(new ElecDataMSSQL(elecData,wellModel.getDtuId(),wellModel.getSlaveId()));
//				} else {
//					//log.debug("【ElecData】未到采集时间或数据不合法，未能写入数据库");
//				}
//			}
			
			//TODO
			// 增加循环间隔
			if (channel.getLoopInterval() != null
					&& Integer.parseInt(channel.getLoopInterval()) > 0) {
//				long endTime = System.currentTimeMillis()
//						+ Integer.parseInt(channel.getLoopInterval()) * 1000;
				long endTime = System.currentTimeMillis() + 60 * 1000;	//一分钟
				
			//	log.debug("增加一分钟时间间隔！");

				while (true) {
					if (this.channel.getProtocal().equalsIgnoreCase(
							Protocal.DL645_DTU.toString())
							&& master != null
							&& (master instanceof DL645DTUMaster)) {
						// DTU心跳检测
						log.debug("检查：");
						try {
							((DL645DTUMaster) master).checkHeartBeat();
						} catch (Exception e1) {
							log.error(e1);
							e1.printStackTrace();
						}
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
						}
						//若程序关闭，则跳出循环，结束该线程
						if(!CommunicationController.INSTANCE.isRuning()) {
							break;
						}
					} else {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
					}
					if (endTime < System.currentTimeMillis()) {
						break;
					}
				}
			}
		}
		// this.setAllDeviceOffline();

		close();// 关闭采集通道

		log.info("通道[" + channel.getName() + "]通讯线程停止.");

	}

	@Override
	public void open() {
		if (master != null) {
			try {
				master.open();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {

			if (this.channel.getProtocal().equalsIgnoreCase(
					Protocal.DL645.toString())) {
				this.initRTUProtocal();// 通讯规约初始化
			} else if (this.channel.getProtocal().equalsIgnoreCase(
					Protocal.DL645_DTU.toString())) {
				this.initDTUProtocal();// 通讯规约初始化
			} else {
				log.error("不支持的DL645规约：" + channel.getProtocal());
			}

			if (TagDataBase.INSTANCE.getWellDeviceMap().containsValue(channel)) {
				log.debug("进行广播校时...");

				long delay = 20000;// 20s
				// 广播校时
				if ((Calendar.getInstance().get(Calendar.HOUR_OF_DAY) % 12) == 0) {
					// 0点或12点的话向后推迟1个小时
					delay = 1000 * 60 * 60;
				}
				dateAdjust = new Timer();
				dateAdjust.schedule(new TimerTask() {
					@Override
					public void run() {
						dateAdjust();
					}
				}, delay, CommConstants.DATE_ADJUST_PERIOD);

			} else {
				//log.debug("未进行广播校时");
			}
			
		}
	}

	@Override
	public void close() {
		if(dateAdjust != null) {
			dateAdjust.cancel();// 停止广播校时任务
		}
		
		closeMaster();
	}

	private void initRTUProtocal() {

		try {
			log.info("正在打开串口：" + channel.getComPort());

			DataBits dataBits = DataBits.DATABITS_8;
			if (StringUtils.equals(channel.getDataBit(), "5")) {
				dataBits = DataBits.DATABITS_5;
			} else if (StringUtils.equals(channel.getDataBit(), "6")) {
				dataBits = DataBits.DATABITS_6;
			} else if (StringUtils.equals(channel.getDataBit(), "7")) {
				dataBits = DataBits.DATABITS_7;
			} else if (StringUtils.equals(channel.getDataBit(), "8")) {
				dataBits = DataBits.DATABITS_8;
			}

			StopBits stopBits = StopBits.BITS_1;
			if (StringUtils.equals(channel.getStopBit(), "1")) {
				stopBits = StopBits.BITS_1;
			} else if (StringUtils.equals(channel.getStopBit(), "2")) {
				stopBits = StopBits.BITS_2;
			} else if (StringUtils.equals(channel.getStopBit(), "1.5")) {
				stopBits = StopBits.BITS_1_5;
			}

			Parity parity = Parity.NONE;
			if (StringUtils.equalsIgnoreCase(channel.getParity(), "none")) {
				parity = Parity.NONE;
			} else if (StringUtils
					.equalsIgnoreCase(channel.getParity(), "even")) {
				parity = Parity.EVEN;
			} else if (StringUtils
					.equalsIgnoreCase(channel.getParity(), "mark")) {
				parity = Parity.MARK;
			} else if (StringUtils.equalsIgnoreCase(channel.getParity(), "odd")) {
				parity = Parity.ODD;
			} else if (StringUtils.equalsIgnoreCase(channel.getParity(),
					"space")) {
				parity = Parity.SPACE;
			}

			SerialAddress portAddress = new SerialAddress(channel.getComPort(),
					Integer.parseInt(channel.getBaud()), dataBits, stopBits, parity,
					FlowControl.NONE);
			master = new DL645RTUMaster(portAddress, 1000,
					Integer.parseInt(channel.getInterval()));
			 Thread.sleep(500);
			master.open();
		} catch (Exception e) {
			log.error("打开串口[" + channel.getComPort() + "]失败", e);
			master = null;
		}
	}

	private void initDTUProtocal() {

		try {
			log.info("正在打开采集通道：" + channel.getName());

			// 监听端口
			DL645DTUServer.listenPort(Integer.parseInt(channel.getTcpPort()));

			log.debug("master DTU ID 号：" + channel.getDtuId());
			master = new DL645DTUMaster(channel.getDtuId(),
					channel.getHeartBeat(), 3000, Integer.parseInt(channel.getInterval()));

			// Thread.sleep(500);
			master.open();
		} catch (Exception e) {
			log.error("监听端口" + channel.getTcpPort() + "超时，请检查DTU设置。", e);
			master = null;
		}
	}

	private void closeMaster() {
		// 关闭串口
		if (channel.getTcpPort() != null) {
			DL645DTUServer.closePort(Integer.parseInt(channel.getTcpPort()));
		}

		if (master != null) {
			master.close();
			log.info(channel.getName() + "正常关闭");
			master = null;
		}
	}

	/**
	 * 广播校时
	 */
	private void dateAdjust() {

		if (master != null && master.isOpened()) {

			log.info(this.channel.getName() + ": 执行广播校时");

			master.dateAdjust();

			log.info(this.channel.getName() + ": 执行广播校时完毕.");
		}
	}

	/**
	 * 广播复位
	 */
	public void broadCastReset() {
		// TODO:
	}

	/**
	 * 广播消音
	 */
	public void broadCastXiaoYin() {
		// TODO:
	}

	/**
	 * 获得通道每个设备的查询信息
	 * 
	 * @param device
	 */
	private void initDevices() {
		
		
		
		for (DeviceModel device : TagDataBase.INSTANCE.getDeviceListMap().get(channel.getOid())) {
			if (!Boolean.parseBoolean(device.getUsed())) {// 备用设备
				continue;
			}
			log.debug("解析设备" + device.getName() + "通讯帧.");
			
			RawWellData rawWellData = new RawWellData();
			
			RawElecData rawElecData = new RawElecData();
			
			RawEnergyData rawEnergyData = new RawEnergyData();
			
			Set<Map.Entry<String, DeviceModel>> mapSet = TagDataBase.INSTANCE.getWellDeviceMap().entrySet();
			for(Map.Entry<String, DeviceModel> entry : mapSet) {
				if(entry.getValue().getOid().equals(device.getOid())) {
					rawWellData.setWell_num(entry.getKey());	//设置井号
					
					rawElecData.setWell_num(entry.getKey());	//设置井号
					
					rawEnergyData.setWell_num(entry.getKey());	//设置井号
					
					break;
				}
			}
			rawWellDataMap.put(device.getOid(), rawWellData);
			
			rawElecDataMap.put(device.getOid(), rawElecData);
			
			rawEnergyDataMap.put(device.getOid(), rawEnergyData);

			
			DL645FrameInfo framInfo_basic = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_basic.init("601F", device.getSlaveId(), 1, 28, Dl645FrameType.BASIC, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_zaihe1 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_zaihe1.init("6110", device.getSlaveId(), 1, 106, Dl645FrameType.ZAIHE_1, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_zaihe2 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_zaihe2.init("6111", device.getSlaveId(), 1, 106, Dl645FrameType.ZAIHE_2, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_zaihe3 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_zaihe3.init("6112", device.getSlaveId(), 1, 106, Dl645FrameType.ZAIHE_3, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_zaihe4 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_zaihe4.init("6113", device.getSlaveId(), 1, 106, Dl645FrameType.ZAIHE_4, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_weiyi1 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_weiyi1.init("6210", device.getSlaveId(), 1, 106, Dl645FrameType.WEIYI_1, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_weiyi2 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_weiyi2.init("6211", device.getSlaveId(), 1, 106, Dl645FrameType.WEIYI_2, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_weiyi3 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_weiyi3.init("6212", device.getSlaveId(), 1, 106, Dl645FrameType.WEIYI_3, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_weiyi4 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_weiyi4.init("6213", device.getSlaveId(), 1, 106, Dl645FrameType.WEIYI_4, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_ib1 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_ib1.init("6310", device.getSlaveId(), 1, 106, Dl645FrameType.IB_1, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_ib2 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_ib2.init("6311", device.getSlaveId(), 1, 106, Dl645FrameType.IB_2, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_ib3 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_ib3.init("6312", device.getSlaveId(), 1, 106, Dl645FrameType.IB_3, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_ib4 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_ib4.init("6313", device.getSlaveId(), 1, 106, Dl645FrameType.IB_4, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_power1 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_power1.init("6410", device.getSlaveId(), 1, 106, Dl645FrameType.POWER_1, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_power2 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_power2.init("6411", device.getSlaveId(), 1, 106, Dl645FrameType.POWER_2, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_power3 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_power3.init("6412", device.getSlaveId(), 1, 106, Dl645FrameType.POWER_3, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_power4 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_power4.init("6413", device.getSlaveId(), 1, 106, Dl645FrameType.POWER_4, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_pf1 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_pf1.init("6510", device.getSlaveId(), 1, 106, Dl645FrameType.PF_1, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_pf2 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_pf2.init("6511", device.getSlaveId(), 1, 106, Dl645FrameType.PF_2, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_pf3 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_pf3.init("6512", device.getSlaveId(), 1, 106, Dl645FrameType.PF_3, wellDataSaveInterval, System.currentTimeMillis());
			
			DL645FrameInfo framInfo_pf4 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
			framInfo_pf4.init("6513", device.getSlaveId(), 1, 106, Dl645FrameType.PF_4, wellDataSaveInterval, System.currentTimeMillis());

			queryList.add(framInfo_basic);
			queryList.add(framInfo_zaihe1);
			queryList.add(framInfo_zaihe2);
			queryList.add(framInfo_zaihe3);
			queryList.add(framInfo_zaihe4);
			queryList.add(framInfo_weiyi1);
			queryList.add(framInfo_weiyi2);
			queryList.add(framInfo_weiyi3);
			queryList.add(framInfo_weiyi4);
			queryList.add(framInfo_ib1);
			queryList.add(framInfo_ib2);
			queryList.add(framInfo_ib3);
			queryList.add(framInfo_ib4);
			queryList.add(framInfo_power1);
			queryList.add(framInfo_power2);
			queryList.add(framInfo_power3);
			queryList.add(framInfo_power4);
			queryList.add(framInfo_pf1);
			queryList.add(framInfo_pf2);
			queryList.add(framInfo_pf3);
			queryList.add(framInfo_pf4);
			
			
			Query wellQuery = new Query(Criteria.where("dtuId").is(channel.getDtuId()));
			WellModel well = PersistManager.INSTANCE.getMongoTemplate().findOne(
					CollectionConstants.WELL_COLLECTION_NAME, wellQuery,
					WellModel.class);
			if (well != null && well.getChouyoujiXinghao().equals("DGT")) {//添加电功图参数
				
				DL645FrameInfo framInfo_dgt1 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
				framInfo_dgt1.init("6610", device.getSlaveId(), 1, 106, Dl645FrameType.DGT_1, wellDataSaveInterval, System.currentTimeMillis());
				
				DL645FrameInfo framInfo_dgt2 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
				framInfo_dgt2.init("6611", device.getSlaveId(), 1, 106, Dl645FrameType.DGT_2, wellDataSaveInterval, System.currentTimeMillis());
				
				DL645FrameInfo framInfo_dgt3 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
				framInfo_dgt3.init("6612", device.getSlaveId(), 1, 106, Dl645FrameType.DGT_3, wellDataSaveInterval, System.currentTimeMillis());
				
				DL645FrameInfo framInfo_dgt4 = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
				framInfo_dgt4.init("6613", device.getSlaveId(), 1, 106, Dl645FrameType.DGT_4, wellDataSaveInterval, System.currentTimeMillis());
				
				queryList.add(framInfo_dgt1);
				queryList.add(framInfo_dgt2);
				queryList.add(framInfo_dgt3);
				queryList.add(framInfo_dgt4);
			} 
				//添加电量参数
				//电能
//				DL645FrameInfo framInfo_zxyg = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_zxyg.init("901F", device.getSlaveId(), 1, 20, Dl645FrameType.ZXYG, energyDataSaveInterval, System.currentTimeMillis());
//				
////				DL645FrameInfo framInfo_fxyg = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
////				framInfo_fxyg.init("902F", device.getSlaveId(), 1, 60, Dl645FrameType.FXYG, energyDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_zxwg = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_zxwg.init("911F", device.getSlaveId(), 1, 20, Dl645FrameType.ZXWG, energyDataSaveInterval, System.currentTimeMillis());
//				
////				DL645FrameInfo framInfo_fxwg = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
////				framInfo_fxwg.init("912F", device.getSlaveId(), 1, 60, Dl645FrameType.FXWG, energyDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_sy_zxygz = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_sy_zxygz.init("9410", device.getSlaveId(), 1, 4, Dl645FrameType.SY_ZXYGZ, energyDataSaveInterval, System.currentTimeMillis());
//				
////				DL645FrameInfo framInfo_sy_fxygz = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
////				framInfo_sy_fxygz.init("9420", device.getSlaveId(), 1, 4, Dl645FrameType.SY_FXYGZ, energyDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_sy_zxwgz = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_sy_zxwgz.init("9510", device.getSlaveId(), 1, 4, Dl645FrameType.SY_ZXWGZ, energyDataSaveInterval, System.currentTimeMillis());
//				
////				DL645FrameInfo framInfo_sy_fxwgz = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
////				framInfo_sy_fxwgz.init("9520", device.getSlaveId(), 1, 4, Dl645FrameType.SY_FXWGZ, energyDataSaveInterval, System.currentTimeMillis());
//				
//				//电量
//				DL645FrameInfo framInfo_xdy = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_xdy.init("B61F", device.getSlaveId(), 1, 6, Dl645FrameType.XDY, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_xdl = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_xdl.init("B62F", device.getSlaveId(), 1, 6, Dl645FrameType.XDL, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_sh_yggl = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_sh_yggl.init("B63F", device.getSlaveId(), 1, 12, Dl645FrameType.SH_YGGL, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_sh_wggl = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_sh_wggl.init("B64F", device.getSlaveId(), 1, 8, Dl645FrameType.SH_WGGL, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_glys = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_glys.init("B65F", device.getSlaveId(), 1, 8, Dl645FrameType.GLYS, elecDataSaveInterval, System.currentTimeMillis());
//				
////				DL645FrameInfo framInfo_other = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
////				framInfo_other.init("B66F", device.getSlaveId(), 1, 11, Dl645FrameType.OTHER_D, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_pinlv = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_pinlv.init("B660", device.getSlaveId(), 1, 2, Dl645FrameType.PINLV, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_lxdl = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_lxdl.init("B661", device.getSlaveId(), 1, 2, Dl645FrameType.LXDL, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_lxdy = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_lxdy.init("B662", device.getSlaveId(), 1, 2, Dl645FrameType.LXDY, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_dlbphd = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_dlbphd.init("B663", device.getSlaveId(), 1, 2, Dl645FrameType.DLBPHD, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_dybphd = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_dybphd.init("B664", device.getSlaveId(), 1, 2, Dl645FrameType.DYBPHD, elecDataSaveInterval, System.currentTimeMillis());
//				
//				DL645FrameInfo framInfo_xiandy = new DL645FrameInfo(this,device, rawWellData, rawEnergyData, rawElecData);
//				framInfo_xiandy.init("B69F", device.getSlaveId(), 1, 8, Dl645FrameType.XIANDY, elecDataSaveInterval, System.currentTimeMillis());
//				
//				queryList.add(framInfo_xdy);
//				queryList.add(framInfo_xdl);
//				queryList.add(framInfo_sh_yggl);
//				queryList.add(framInfo_sh_wggl);
//				queryList.add(framInfo_glys);
////				queryList.add(framInfo_other);
//				queryList.add(framInfo_xiandy);
//				queryList.add(framInfo_pinlv);
//				queryList.add(framInfo_lxdl);
//				queryList.add(framInfo_lxdy);
//				queryList.add(framInfo_dlbphd);
//				queryList.add(framInfo_dybphd);
//				
//				queryList.add(framInfo_zxyg);
////				queryList.add(framInfo_fxyg);
//				queryList.add(framInfo_zxwg);
////				queryList.add(framInfo_fxwg);
//				queryList.add(framInfo_sy_zxygz);
////				queryList.add(framInfo_sy_fxygz);
//				queryList.add(framInfo_sy_zxwgz);
////				queryList.add(framInfo_sy_fxwgz);
			
		}
		log.debug("生成通讯帧个数：" + queryList.size());
	}

	public AbstractDL645Master getMaster() {
		return master;
	}

	@Override
	public void executeYK(String tagPath) {
		// TODO: 暂不支持
		// IOTagModel tagVar =
		// TagDataBase.INSTANCE.getIoTagVarMap().get(tagPath);
	}

	@Override
	public void executeYT(String tagPath, double value) {
		// TODO: 暂不支持
	}

	@Override
	public void executeBroadCast(String broadCastCode) {
		// TODO: 暂不支持
	}

	public ChannelModel getChannel() {
		return channel;
	}

	public void setChannel(ChannelModel channel) {
		this.channel = channel;
	}

	public Map<String, Boolean> getDeviceStateMap() {
		return deviceStateMap;
	}

	public void setDeviceStateMap(Map<String, Boolean> deviceStateMap) {
		this.deviceStateMap = deviceStateMap;
	}
	
	/**
	 * 执行一次查询
	 */
	public void excuteOneQuery() {
		if (CommunicationController.INSTANCE.isRuning()) {
			if (master != null && !master.isOpened()) {
				try {
					master.open();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				return;
			}
			if (!CommunicationController.INSTANCE.isRuning()) {
				return;
			}

			for (DL645FrameInfo frame : this.queryList) {
				if (!CommunicationController.INSTANCE.isRuning()) {
					break;
				}
				//if(System.currentTimeMillis()>=frame.getEndTime()) {
					//frame.setEndTime(System.currentTimeMillis() + frame.getFrameLoopInterval()*1000);//增加循环间隔
					frame.executeReadRequest();// 执行遥测采集
				//}
			}
			
			//wellData写库
			Set<Map.Entry<String, RawWellData>> mapEntry = rawWellDataMap.entrySet();
			for(Map.Entry<String, RawWellData> entry : mapEntry) {
				if(entry.getValue().isValid()) {
					RawWellData rawWellData = entry.getValue();	//原始数据
					WellData wellData = new WellData();
					wellData.setWell_num(rawWellData.getWell_num());//设置井号
					
					wellData.setChong_cheng_time(rawWellData.getChong_cheng_time());
					wellData.setChong_ci(rawWellData.getChong_ci());
					wellData.setStart_time(rawWellData.getStartTime());
					wellData.setStop_time(rawWellData.getStopTime());
					wellData.setGtmk(rawWellData.getGtmk());
					wellData.setYjmk(rawWellData.getYjmk());
					wellData.setZaihe(rawWellData.getZaihe());
					wellData.setWeiyi(rawWellData.getWeiyi());
					wellData.setPower(rawWellData.getPower());
					wellData.setPower_factor(rawWellData.getPf());
					wellData.setIb(rawWellData.getIB());
					wellData.setSave_time(Calendar.getInstance().getTime());
					wellData.setDevice_time(rawWellData.getDeviceTime());
					
					if(rawWellData.getDGT() != null) {
						wellData.setDgt(rawWellData.getDGT());	//电功图
					}
					rawWellData.setNull();//清空数据
					
					new WellDataDao().writeToDatabase(wellData);//写库
				} else {
					log.debug("【WellData】未到采集时间或数据不合法，未能写入数据库");
				}
			}
			//EnergyData写库
			Set<Map.Entry<String, RawEnergyData>> energyMapEntry = rawEnergyDataMap.entrySet();
			for(Map.Entry<String, RawEnergyData> entry : energyMapEntry) {
				if(entry.getValue().isValid()) {
					RawEnergyData rawEnergyData = entry.getValue();	//原始数据
					EnergyData energyData = new EnergyData();
					energyData.setWell_num(rawEnergyData.getWell_num());//设置井号
					
					energyData.setZxygZ(rawEnergyData.getZxygZ());
					energyData.setFxygZ(rawEnergyData.getFxygZ());
					energyData.setZxwgZ(rawEnergyData.getZxwgZ());
					energyData.setFxwgZ(rawEnergyData.getFxwgZ());
					
					energyData.setZxygJ(rawEnergyData.getZxygJ());
					energyData.setFxygJ(rawEnergyData.getFxygJ());
					energyData.setZxwgJ(rawEnergyData.getZxwgJ());
					energyData.setFxwgJ(rawEnergyData.getFxwgJ());
					
					energyData.setZxygF(rawEnergyData.getZxygF());
					energyData.setFxygF(rawEnergyData.getFxygF());
					energyData.setZxwgF(rawEnergyData.getZxwgF());
					energyData.setFxwgF(rawEnergyData.getFxwgF());
					
					energyData.setZxygP(rawEnergyData.getZxygP());
					energyData.setFxygP(rawEnergyData.getFxygP());
					energyData.setZxwgP(rawEnergyData.getZxwgP());
					energyData.setFxwgP(rawEnergyData.getFxwgP());
					
					energyData.setZxygG(rawEnergyData.getZxygG());
					energyData.setFxygG(rawEnergyData.getFxygG());
					energyData.setZxwgG(rawEnergyData.getZxwgG());
					energyData.setFxwgG(rawEnergyData.getFxwgG());
					
					energyData.setSyZxygZ(rawEnergyData.getSYZxygZ());
					energyData.setSyFxygZ(rawEnergyData.getSYFxygZ());
					energyData.setSyZxwgZ(rawEnergyData.getSYZxwgZ());
					energyData.setSyFxwgZ(rawEnergyData.getSYFxwgZ());
					
					energyData.setSave_time(Calendar.getInstance().getTime());
					
					rawEnergyData.setNull();//清空数据
					
					new EnergyDataDao().writeToDatabase(energyData);	//写库
				} else {
					log.debug("【EnergyData】未到采集时间或数据不合法，未能写入数据库");
				}
			}
			
			//ElecData写库
			Set<Map.Entry<String, RawElecData>> elecMapEntry = rawElecDataMap.entrySet();
			for(Map.Entry<String, RawElecData> entry : elecMapEntry) {
				if(entry.getValue().isValid()) {
					RawElecData rawElecData = entry.getValue();	//原始数据
					ElecData elecData = new ElecData();
					elecData.setWell_num(rawElecData.getWell_num());//设置井号
					
					elecData.setUa(rawElecData.getUA());
					elecData.setUb(rawElecData.getUB());
					elecData.setUc(rawElecData.getUC());
					elecData.setUab(rawElecData.getUab());
					elecData.setUbc(rawElecData.getUbc());
					elecData.setUca(rawElecData.getUca());
					elecData.setIa(rawElecData.getIA());
					elecData.setIb(rawElecData.getIB());
					elecData.setIc(rawElecData.getIC());
					
					elecData.setShygglZ(rawElecData.getSHYgglZ());
					elecData.setShygglA(rawElecData.getSHYgglA());
					elecData.setShygglB(rawElecData.getSHYgglB());
					elecData.setShygglC(rawElecData.getSHYgglC());
					
					elecData.setShwgglZ(rawElecData.getSHWgglZ());
					elecData.setShwgglA(rawElecData.getSHWgglA());
					elecData.setShwgglB(rawElecData.getSHWgglB());
					elecData.setShwgglC(rawElecData.getSHWgglC());
					
					elecData.setGlysZ(rawElecData.getGLYSZ());
					elecData.setGlysA(rawElecData.getGLYSA());
					elecData.setGlysB(rawElecData.getGLYSB());
					elecData.setGlysC(rawElecData.getGLYSC());
					
					elecData.setPinlv(rawElecData.getPinLv());
					elecData.setLxdl(rawElecData.getLXDL());
					elecData.setLxdy(rawElecData.getLXDY());
					elecData.setDlbphd(rawElecData.getDLBPHD());
					elecData.setDybphd(rawElecData.getDYBPHD());
					
					elecData.setSave_time(Calendar.getInstance().getTime());
					
					rawElecData.setNull();//清空数据
					
					new ElecDataDao().writeToDatabase(elecData);	//写库
				} else {
					log.debug("【ElecData】未到采集时间或数据不合法，未能写入数据库");
				}
			}
		}
	}
	
	/**
	 * 执行一次查询
	 */
	public void excuteOneQuery(String deviceId) {
		if (CommunicationController.INSTANCE.isRuning()) {
			if (master != null && !master.isOpened()) {
				try {
					master.open();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				return;
			}
			if (!CommunicationController.INSTANCE.isRuning()) {
				return;
			}

			for (DL645FrameInfo frame : this.queryList) {
				if (!CommunicationController.INSTANCE.isRuning()) {
					break;
				}
				//if(System.currentTimeMillis()>=frame.getEndTime()) {
				//	frame.setEndTime(System.currentTimeMillis() + frame.getFrameLoopInterval()*1000);//增加循环间隔
				//log.debug(deviceId);
				//log.debug(frame.getDevice().getSlaveId());
				if(deviceId.equals("") || frame.getDevice().getSlaveId().equals(deviceId)) {//deviceId为空则读所有
					frame.executeReadRequest();// 执行遥测采集
				}
					
				//}
			}
			
			//wellData写库
			Set<Map.Entry<String, RawWellData>> mapEntry = rawWellDataMap.entrySet();
			for(Map.Entry<String, RawWellData> entry : mapEntry) {
				if(entry.getValue().isValid()) {
					RawWellData rawWellData = entry.getValue();	//原始数据
					WellData wellData = new WellData();
					wellData.setWell_num(rawWellData.getWell_num());//设置井号
					
					wellData.setChong_cheng_time(rawWellData.getChong_cheng_time());
					wellData.setChong_ci(rawWellData.getChong_ci());
					wellData.setStart_time(rawWellData.getStartTime());
					wellData.setStop_time(rawWellData.getStopTime());
					wellData.setGtmk(rawWellData.getGtmk());
					wellData.setYjmk(rawWellData.getYjmk());
					wellData.setZaihe(rawWellData.getZaihe());
					wellData.setWeiyi(rawWellData.getWeiyi());
					wellData.setPower(rawWellData.getPower());
					wellData.setPower_factor(rawWellData.getPf());
					wellData.setIb(rawWellData.getIB());
					wellData.setSave_time(Calendar.getInstance().getTime());
					wellData.setDevice_time(rawWellData.getDeviceTime());
					
					if(rawWellData.getDGT() != null) {
						wellData.setDgt(rawWellData.getDGT());	//电功图
					}
					rawWellData.setNull();//清空数据
					
					new WellDataDao().writeToDatabase(wellData);//写库
				} else {
					log.debug("【WellData】未到采集时间或数据不合法，未能写入数据库");
				}
			}
			//EnergyData写库
			Set<Map.Entry<String, RawEnergyData>> energyMapEntry = rawEnergyDataMap.entrySet();
			for(Map.Entry<String, RawEnergyData> entry : energyMapEntry) {
				if(entry.getValue().isValid()) {
					RawEnergyData rawEnergyData = entry.getValue();	//原始数据
					EnergyData energyData = new EnergyData();
					energyData.setWell_num(rawEnergyData.getWell_num());//设置井号
					
					energyData.setZxygZ(rawEnergyData.getZxygZ());
					energyData.setFxygZ(rawEnergyData.getFxygZ());
					energyData.setZxwgZ(rawEnergyData.getZxwgZ());
					energyData.setFxwgZ(rawEnergyData.getFxwgZ());
					
					energyData.setZxygJ(rawEnergyData.getZxygJ());
					energyData.setFxygJ(rawEnergyData.getFxygJ());
					energyData.setZxwgJ(rawEnergyData.getZxwgJ());
					energyData.setFxwgJ(rawEnergyData.getFxwgJ());
					
					energyData.setZxygF(rawEnergyData.getZxygF());
					energyData.setFxygF(rawEnergyData.getFxygF());
					energyData.setZxwgF(rawEnergyData.getZxwgF());
					energyData.setFxwgF(rawEnergyData.getFxwgF());
					
					energyData.setZxygP(rawEnergyData.getZxygP());
					energyData.setFxygP(rawEnergyData.getFxygP());
					energyData.setZxwgP(rawEnergyData.getZxwgP());
					energyData.setFxwgP(rawEnergyData.getFxwgP());
					
					energyData.setZxygG(rawEnergyData.getZxygG());
					energyData.setFxygG(rawEnergyData.getFxygG());
					energyData.setZxwgG(rawEnergyData.getZxwgG());
					energyData.setFxwgG(rawEnergyData.getFxwgG());
					
					energyData.setSyZxygZ(rawEnergyData.getSYZxygZ());
					energyData.setSyFxygZ(rawEnergyData.getSYFxygZ());
					energyData.setSyZxwgZ(rawEnergyData.getSYZxwgZ());
					energyData.setSyFxwgZ(rawEnergyData.getSYFxwgZ());
					
					energyData.setSave_time(Calendar.getInstance().getTime());
					
					rawEnergyData.setNull();//清空数据
					
					new EnergyDataDao().writeToDatabase(energyData);	//写库
				} else {
					log.debug("【EnergyData】未到采集时间或数据不合法，未能写入数据库");
				}
			}
			
			//ElecData写库
			Set<Map.Entry<String, RawElecData>> elecMapEntry = rawElecDataMap.entrySet();
			for(Map.Entry<String, RawElecData> entry : elecMapEntry) {
				if(entry.getValue().isValid()) {
					RawElecData rawElecData = entry.getValue();	//原始数据
					ElecData elecData = new ElecData();
					elecData.setWell_num(rawElecData.getWell_num());//设置井号
					
					elecData.setUa(rawElecData.getUA());
					elecData.setUb(rawElecData.getUB());
					elecData.setUc(rawElecData.getUC());
					elecData.setUab(rawElecData.getUab());
					elecData.setUbc(rawElecData.getUbc());
					elecData.setUca(rawElecData.getUca());
					elecData.setIa(rawElecData.getIA());
					elecData.setIb(rawElecData.getIB());
					elecData.setIc(rawElecData.getIC());
					
					elecData.setShygglZ(rawElecData.getSHYgglZ());
					elecData.setShygglA(rawElecData.getSHYgglA());
					elecData.setShygglB(rawElecData.getSHYgglB());
					elecData.setShygglC(rawElecData.getSHYgglC());
					
					elecData.setShwgglZ(rawElecData.getSHWgglZ());
					elecData.setShwgglA(rawElecData.getSHWgglA());
					elecData.setShwgglB(rawElecData.getSHWgglB());
					elecData.setShwgglC(rawElecData.getSHWgglC());
					
					elecData.setGlysZ(rawElecData.getGLYSZ());
					elecData.setGlysA(rawElecData.getGLYSA());
					elecData.setGlysB(rawElecData.getGLYSB());
					elecData.setGlysC(rawElecData.getGLYSC());
					
					elecData.setPinlv(rawElecData.getPinLv());
					elecData.setLxdl(rawElecData.getLXDL());
					elecData.setLxdy(rawElecData.getLXDY());
					elecData.setDlbphd(rawElecData.getDLBPHD());
					elecData.setDybphd(rawElecData.getDYBPHD());
					
					elecData.setSave_time(Calendar.getInstance().getTime());
					
					rawElecData.setNull();//清空数据
					
					new ElecDataDao().writeToDatabase(elecData);	//写库
				} else {
					log.debug("【ElecData】未到采集时间或数据不合法，未能写入数据库");
				}
			}
		}
	}

}
