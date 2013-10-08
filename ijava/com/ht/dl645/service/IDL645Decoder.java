package com.ht.dl645.service;

import java.nio.ByteBuffer;

public interface IDL645Decoder {
	public Object decode(IoSession session, ByteBuffer byteBuffer);
}
