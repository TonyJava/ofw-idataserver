package com.ht.dl645.exception;


/**
 * 08 内存奇偶校验错误 从机读扩展内存中的数据时，发现有奇 偶校验错误，主机按从机的要求重新发 送数据请求。
 * 
 * @author BoChengwen
 * 
 */
public class ParityErrorException extends TransportException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ParityErrorException() {
		super();
	}

	public ParityErrorException(String message) {
		super(message);
	}

}
