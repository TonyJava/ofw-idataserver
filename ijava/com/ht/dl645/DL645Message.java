package com.ht.dl645;

import com.ht.dl645.msg.IDL645Message;

public class DL645Message {
	private IDL645Message message;
	private int cs;

	public DL645Message(IDL645Message message, int cs) {
		super();
		this.message = message;
		this.cs = cs;
	}

	public IDL645Message getMessage() {
		return message;
	}

	public void setMessage(IDL645Message message) {
		this.message = message;
	}

	public int getCS() {
		return cs;
	}

	public void setCS(int cs) {
		this.cs = cs;
	}

}
