package com.ht.dl645.service.serial;

import com.ht.dl645.codec.DL645RequestEncoder;
import com.ht.dl645.codec.DL645ResponseDecoder;
import com.ht.dl645.service.IDL645Decoder;
import com.ht.dl645.service.IDL645Encoder;

public class DL645SerialConnector extends AbstractSerialIoConnector {
	private IDL645Decoder decoder;
	private IDL645Encoder encoder;

	public DL645SerialConnector(int timeout,
			SerialAddress serialAddress, boolean isMaster) {
		super(timeout, serialAddress);
		if (isMaster) {
			encoder = new DL645RequestEncoder();
			decoder = new DL645ResponseDecoder();
		} else {
			System.out.println("暂不支持从站模式");
			assert false;
		}
	}

	@Override
	public IDL645Decoder getDecoder() {
		return decoder;
	}

	@Override
	public IDL645Encoder getEncoder() {
		return encoder;
	}

}
