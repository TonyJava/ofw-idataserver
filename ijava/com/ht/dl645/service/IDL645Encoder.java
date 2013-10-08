package com.ht.dl645.service;


public interface IDL645Encoder {
	public byte[] encode(IoSession session, Object message);
}
