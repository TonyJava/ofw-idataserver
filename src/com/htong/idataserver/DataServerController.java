package com.htong.idataserver;

import org.apache.log4j.Logger;

import com.ht.dl645.dtu.DL645DTUServer;
import com.ht.dl645.service.dtu.DL645DTUConnector;
import com.htong.communication.CommunicationController;
import com.htong.status.DTUStatus;
import com.htong.status.RunStatus;
import com.htong.ws.WebServiceClass;
import com.htong.ws.WebServiceController;

public enum DataServerController {
	INSTANCE;
	private final Logger log = Logger
			.getLogger(DataServerController.class);
	

	private DataServerController() {
		init();
	}

	/**
	 * 实例化对象后初始化数据库和数据采集程序
	 */
	public void init() {
		try {
			TagDataBase.INSTANCE.init();
			//CommunicationController.INSTANCE.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化数据采集程序后启动数据服务程序
	 */
	public void startServer() {
		log.info("启动数据采集程序");
		CommunicationController.INSTANCE.startService();
		//启动WebService
		log.info("启动WebService");
//		WebServiceController.instance.startWebService();
		log.info("启动WebService成功");
		
		RunStatus.Instance.setRun(true);
//		log.info("启动原始数据存储程序");
//		TagRecorderModel.startTimer();
//		log.info("启动自定义数据存储程序");
//		CustomDataRecorderModel.startTimer();
//		log.info("启动统计数据处理程序");
//		StatisticRecorderModel.startTimer();
//		log.info("启动内存数据计算程序");
//		GeneratorModel.startTimer();
	}

	public void stopServer() {

		log.info("停止数据采集程序");
		CommunicationController.INSTANCE.stopService();
		
		//清除
		DL645DTUServer.serverPortMap.clear();
		DL645DTUConnector.dtuMap.clear();
		
		//清除DTU状态
		DTUStatus.instance.getDtuStatusMap().clear();
		DTUStatus.instance.getCommStatusMap().clear();
		DTUStatus.instance.getHeartBeatMap().clear();
		
		//关闭WebService
//		WebServiceController.instance.stopWebService();
//		log.info("关闭WebService");
		
		RunStatus.Instance.setRun(false);
//		log.info("停止原始数据存储程序");
//		TagRecorderModel.stopTimer();
//		log.info("停止自定义数据存储程序");
//		CustomDataRecorderModel.stopTimer();
//		log.info("停止统计数据处理程序");
//		StatisticRecorderModel.stopTimer();
//		log.info("停止内存数据计算程序");
//		GeneratorModel.stopTimer();
	}
	
	public static void main(String args[]) {
		DataServerController.INSTANCE.startServer();
	}


}
