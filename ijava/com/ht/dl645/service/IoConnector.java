package com.ht.dl645.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface IoConnector {
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

	IoSession getIoSession();

	InputStream getInputStream();

	OutputStream getOutputStream();

	IDL645Decoder getDecoder();

	IDL645Encoder getEncoder();
}
