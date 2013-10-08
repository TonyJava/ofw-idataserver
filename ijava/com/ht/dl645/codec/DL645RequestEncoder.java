package com.ht.dl645.codec;

import com.ht.dl645.msg.IDL645Message;
import com.ht.dl645.service.IDL645Encoder;
import com.ht.dl645.service.IoSession;

public class DL645RequestEncoder implements IDL645Encoder {

	@Override
	public byte[] encode(IoSession session, Object message) {
		if (message instanceof IDL645Message) {
			return ((IDL645Message) message).getMessageData();
		} else if (message instanceof byte[]){
			return (byte[]) message;
		}
		return null;
	}

}
