package com.ht.dl645.msg;

public interface IDL645Message {
	public int getCS();
	
	public int getMessageDataSize();
	public byte[] getMessageData();
	
	public String toHexString();
}
