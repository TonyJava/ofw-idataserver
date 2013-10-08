package com.ht.dl645.service;

public interface IoAcceptor {
	public int getTimeout();

	public void setTimeout(int timeout);

	/**
	 * 发送数据
	 * 
	 * @param message
	 */
	public void write(Object message);

	/**
	 * 读取数据
	 * 
	 * @return 数据对象或异常类型，如果超时返回 null
	 */
	public Object read();

	/**
	 * 打开连接
	 */
	public void open() throws Exception;

	/**
	 * 关闭连接
	 */
	public void close();

	/**
	 * @return
	 */
	public boolean isOpen();

	/**
	 * 获取当前连接的主站数
	 * 
	 * @return
	 */
	public int getConnectedCount();

	/**
	 * 获取最大允许连接的主站数
	 * 默认值为2
	 * @return
	 */
	public int getMaxConnectionSize();

	/**
	 * 设置最大可连接的主站数
	 * 
	 * @param maxSize
	 */
	public void setMaxConnectionSize(int maxSize);

	IoSession getIoSession();

	IoHandler getIoHandler();

	IDL645Decoder getDecoder();

	IDL645Encoder getEncoder();

}
