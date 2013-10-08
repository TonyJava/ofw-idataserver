package com.htong.communication;


public interface IChannel extends Runnable {
	/**
	 * 打开采集通道
	 */
	public void open();
	
	/**
	 * 关闭通道
	 */
	public void close();
	
	/**
	 * 只能支持05码遥控
	 * @param tagPath
	 */
	public void executeYK(String tagPath);
	
	/**
	 * 将在下一个版本中支持遥调功能
	 * @param tagPath
	 * @param value
	 */
	public void executeYT(String tagPath, double value);
	
	/**
	 * 发送广播命令
	 * 
	 * @param broadCastCode AllReset:广播复位, XiaoYin:广播消音
	 */
	public void executeBroadCast(String broadCastCode);
	
}
