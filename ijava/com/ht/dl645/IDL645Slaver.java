package com.ht.dl645;

public interface IDL645Slaver extends Runnable {

	public boolean isOpened();
	public void stop();
	public int getConnectedMasterCount();
}
