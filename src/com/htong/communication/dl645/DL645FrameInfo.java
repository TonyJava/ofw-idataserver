package com.htong.communication.dl645;

import org.apache.log4j.Logger;

import com.ht.dl645.AbstractDL645Master;
import com.ht.dl645.exception.DL645TimeOutException;
import com.ht.dl645.exception.ErrorResponseException;
import com.ht.dl645.exception.TransportException;
import com.ht.dl645.msg.response.IDL645Response;
import com.htong.communication.CommunicationController;
import com.htong.communication.ICommFrame;
import com.htong.domain.DeviceModel;
import com.htong.idataserver.RawElecData;
import com.htong.idataserver.RawEnergyData;
import com.htong.idataserver.RawWellData;
import com.htong.status.DTUStatus;
import com.htong.util.HandleString;

/**
 * Modbus请求
 * 
 * @author 赵磊
 * 
 */
public class DL645FrameInfo implements ICommFrame {
	private static final Logger log = Logger.getLogger(DL645FrameInfo.class);

	private DL645Channel channel;
	private DeviceModel device; // 设备
	private RawWellData rawWellData;
	private RawEnergyData rawEnergyData;
	private RawElecData rawElecData;

	private String type; // 内部标识 遥测：YC 遥信：YX 遥调：YT
	private String idAddress = "0000"; // 起始地址
	private String deviceAddress = "0000-0000-0001"; // 子站地址

	private Integer functionCode = 0; // 功能码
	private Integer length = 0; // 读取长度

	private Dl645FrameType frameType; // 帧类型
	
	private long endTime;	//结束时间
	private int frameLoopInterval;	//帧循环间隔

	public DL645FrameInfo(DL645Channel channel, DeviceModel deviceModel,
			RawWellData rawWellData, RawEnergyData rawEnergyData,RawElecData rawElecData) {
		this.channel = channel;
		this.device = deviceModel;
		this.rawWellData = rawWellData;
		this.rawEnergyData = rawEnergyData;
		this.rawElecData = rawElecData;
	}

	/**
	 * 初始化
	 * 
	 * @param idAddress
	 * @param deviceAddress
	 * @param functionCode
	 * @param length
	 * @param frameType
	 */
	public void init(String idAddress, String deviceAddress,
			Integer functionCode, Integer length, Dl645FrameType frameType) {
		this.idAddress = idAddress;
		this.deviceAddress = deviceAddress;
		this.functionCode = functionCode;
		this.length = length;
		this.frameType = frameType;
	}
	
	public void init(String idAddress, String deviceAddress,
			Integer functionCode, Integer length, Dl645FrameType frameType, int frameLoopInterval, long endTime) {
		this.idAddress = idAddress;
		this.deviceAddress = deviceAddress;
		this.functionCode = functionCode;
		this.length = length;
		this.frameType = frameType;
		this.frameLoopInterval = frameLoopInterval;
		this.endTime = endTime;
	}

	public void executeReadRequest() {

		AbstractDL645Master master = channel.getMaster();
		if (master == null) {
			log.error("dl645 master == null!!!");
			channel.open();
			return;
		}

		master.setTimeout(Integer.parseInt(device.getTimeout()));

		int retryCount = 0;
		int maxRetry = Integer.parseInt(device.getRetry());
		do {
			if (!CommunicationController.INSTANCE.isRuning()) {
				return;
			}

			try {
				IDL645Response response = master.sendReadRequest(
						HandleString.handleSlaveIdTo12(deviceAddress),
						functionCode, idAddress);

				handleReturnValue(response.getDatas());

				break;
			} catch (ErrorResponseException e) {
				log.error(e);
				// log.error("			>>" + e.getOriginalRequest().getHexdump());
				// log.error("			<<" + e.getErrorResponse().getHexdump());
			} catch (DL645TimeOutException e) {
				log.error(e);
			} catch (TransportException e) {
				log.error(e);
			} catch (Exception e) {
				log.error("未知异常。", e);
			}
			retryCount++;
		} while (retryCount < maxRetry);

		handleTimeout(retryCount, retryCount >= maxRetry);

	}

	private void handleReturnValue(String datasStrings) {

		Dl645FrameType frameType = getFrameType();
		
		log.debug("数据项：" + datasStrings);

		switch (frameType) {
		case BASIC:
			rawWellData.initBasicData(datasStrings);
			break;
		case ZAIHE_1:
			log.debug("载荷1");
			rawWellData.setZaihe_1(datasStrings);
			break;
		case ZAIHE_2:
			log.debug("载荷2");
			rawWellData.setZaihe_2(datasStrings);
			break;
		case ZAIHE_3:
			log.debug("载荷3");
			rawWellData.setZaihe_3(datasStrings);
			break;
		case ZAIHE_4:
			log.debug("载荷4");
			rawWellData.setZaihe_4(datasStrings);
			break;
		case WEIYI_1:
			rawWellData.setWeiyi_1(datasStrings);
			break;
		case WEIYI_2:
			rawWellData.setWeiyi_2(datasStrings);
			break;
		case WEIYI_3:
			rawWellData.setWeiyi_3(datasStrings);
			break;
		case WEIYI_4:
			rawWellData.setWeiyi_4(datasStrings);
			break;
		case IB_1:
			rawWellData.setIb_1(datasStrings);
			break;
		case IB_2:
			rawWellData.setIb_2(datasStrings);
			break;
		case IB_3:
			rawWellData.setIb_3(datasStrings);
			break;
		case IB_4:
			rawWellData.setIb_4(datasStrings);
			break;
		case POWER_1:
			rawWellData.setPower_1(datasStrings);
			break;
		case POWER_2:
			rawWellData.setPower_2(datasStrings);
			break;
		case POWER_3:
			rawWellData.setPower_3(datasStrings);
			break;
		case POWER_4:
			rawWellData.setPower_4(datasStrings);
			break;
		case PF_1:
			rawWellData.setPf_1(datasStrings);
			break;
		case PF_2:
			rawWellData.setPf_2(datasStrings);
			break;
		case PF_3:
			rawWellData.setPf_3(datasStrings);
			break;
		case PF_4:
			rawWellData.setPf_4(datasStrings);
			break;
		case DGT_1:
			rawWellData.setDgt_1(datasStrings);
			break;
		case DGT_2:
			rawWellData.setDgt_2(datasStrings);
			break;
		case DGT_3:
			rawWellData.setDgt_3(datasStrings);
			break;
		case DGT_4:
			rawWellData.setDgt_4(datasStrings);
			break;
			
		case ZXYG:
			rawEnergyData.setZxyg(datasStrings);
			break;
		case FXYG:
			rawEnergyData.setFxyg(datasStrings);
			break;
		case ZXWG:
			rawEnergyData.setZxwg(datasStrings);
			break;
		case FXWG:
			rawEnergyData.setFxwg(datasStrings);
			break;
		case SY_ZXYGZ:
			rawEnergyData.setSyZxygZ(datasStrings);
			break;
		case SY_FXYGZ:
			rawEnergyData.setSyFxygZ(datasStrings);
			break;
		case SY_ZXWGZ:
			rawEnergyData.setSyZxwgZ(datasStrings);
			break;
		case SY_FXWGZ:
			rawEnergyData.setSyFxwgZ(datasStrings);
			break;
			
		case XDY:
			rawElecData.setXdy(datasStrings);
			break;
		case XDL:
			rawElecData.setXdl(datasStrings);
			break;
		case SH_YGGL:
			rawElecData.setShyggl(datasStrings);
			break;
		case SH_WGGL:
			rawElecData.setShwggl(datasStrings);
			break;
		case GLYS:
			rawElecData.setGlys(datasStrings);
			break;
//		case OTHER_D:
//			rawElecData.setOtherElec(datasStrings);
//			break;
		case PINLV:
			rawElecData.setPinlv(datasStrings);
			break;
		case LXDL:
			rawElecData.setLxdl(datasStrings);
			break;
		case LXDY:
			rawElecData.setLxdy(datasStrings);
			break;
		case DLBPHD:
			rawElecData.setDlbphd(datasStrings);
			break;
		case DYBPHD:
			rawElecData.setDybphd(datasStrings);
			break;
		case XIANDY:
			rawElecData.setXiandy(datasStrings);
			break;
			
		default:
			break;
		}

	}

	/**
	 * 处理通讯超时
	 * 
	 * @param retryCount
	 * @param isTimeout
	 * @param device
	 */
	private void handleTimeout(int retryCount, boolean isTimeout) {
		
		String dtuId = channel.getChannel().getDtuId();

		//Object oldValue = channel.getDeviceStateMap().get(device.getOid());
		Object oldValue = DTUStatus.instance.getCommStatusMap().get(dtuId);

		if (isTimeout) {
			log.debug(device.getOid() + "通讯超时");
			device.retryCount++;
			if (device.retryCount >= Integer.parseInt(channel.getChannel()
					.getOffline())) {// 离线
				device.retryCount = 0;// 记数清零
				if (oldValue == null || oldValue.equals(Boolean.TRUE)) {
					// 更新实时数据库
					// for (String id : tagVar.getIds()) {
					// CacheManager.INSTANCE.getTagValueCache().put(id, true);
					// }
					// CacheManager.INSTANCE.getTagValueCache().put(
					// tagVar.getPath(), true);
					// tagVar.getScriptContext().setAttribute(tagVar.getName(),
					// true, ScriptContext.ENGINE_SCOPE);
					
					//channel.getDeviceStateMap().put(device.getOid(), false);
					DTUStatus.instance.putCommStatusMap(dtuId, false);
				}

			}
		} else {// 在线
			// 更新实时数据库
			if (oldValue == null || oldValue.equals(Boolean.FALSE)) {

				// for (String id : tagVar.getIds()) {
				// CacheManager.INSTANCE.getTagValueCache().put(id, false);
				// }
				// CacheManager.INSTANCE.getTagValueCache().put(tagVar.getPath(),
				// true);
				// tagVar.getScriptContext().setAttribute(tagVar.getName(),
				// false,
				// ScriptContext.ENGINE_SCOPE);
				
				//channel.getDeviceStateMap().put(device.getOid(), true);
				DTUStatus.instance.putCommStatusMap(dtuId, true);
				device.retryCount = 0;// 记数清零
			}
		}
	}

	public String getIdAddress() {
		return idAddress;
	}

	public void setIdAddress(String idAddress) {
		this.idAddress = idAddress;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public Integer getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Integer functionCode) {
		this.functionCode = functionCode;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public DeviceModel getDevice() {
		return device;
	}

	public void setDevice(DeviceModel device) {
		this.device = device;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Dl645FrameType getFrameType() {
		return frameType;
	}

	public void setFrameType(Dl645FrameType frameType) {
		this.frameType = frameType;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getFrameLoopInterval() {
		return frameLoopInterval;
	}

	public void setFrameLoopInterval(int frameLoopInterval) {
		this.frameLoopInterval = frameLoopInterval;
	}

}
