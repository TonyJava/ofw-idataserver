package com.ht.dl645.codec;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.dl645.DL645Message;
import com.ht.dl645.msg.IDL645Message;
import com.ht.dl645.service.IoSession;
import com.ht.dl645.service.IDL645Encoder;
import com.ht.dl645.util.HexMessageDumper;

public class DL645ResponseEncoder implements IDL645Encoder {
	private final static Logger LOGGER = LoggerFactory.getLogger(DL645ResponseEncoder.class);
	
	@Override
	public byte[] encode(IoSession session, Object message) {
		if (message instanceof DL645Message) {
			DL645Message rtuMessage = (DL645Message) message;
			IDL645Message modbusMessage = rtuMessage.getMessage();

			byte[] data = Arrays.copyOf(modbusMessage.getMessageData(),
					modbusMessage.getMessageDataSize() + 2);

			data[data.length - 2] = (byte) ((rtuMessage.getCS() >>> 8) & 0xff);
			data[data.length - 1] = (byte) (rtuMessage.getCS() & 0xff);

			LOGGER.debug(">>" + HexMessageDumper.getHexdump(data));
			return data;
		}
		return null;
	}

}
