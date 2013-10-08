package com.htong.status;

public enum RunStatus {
	Instance;
	private boolean isRun = false;

	public boolean isRun() {
		return isRun;
	}

	public void setRun(boolean isRun) {
		this.isRun = isRun;
	}
	

}
