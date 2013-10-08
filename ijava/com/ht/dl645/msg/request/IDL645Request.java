package com.ht.dl645.msg.request;

import com.ht.dl645.msg.IDL645Message;

public interface IDL645Request extends IDL645Message {
	public String getSlave();
	public int getFunCode();
	public int getLen();
	
}
