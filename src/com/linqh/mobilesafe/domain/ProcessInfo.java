package com.linqh.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
	private Drawable icon;
	private String appname;
	private String packname;
	private long memsize;
	private boolean usertask;
	private boolean checked;
	public Drawable getIcon() {
		return icon;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public long getMemsize() {
		return memsize;
	}
	public void setMemsize(long memsize) {
		this.memsize = memsize;
	}
	public boolean isUsertask() {
		return usertask;
	}
	public void setUsertask(boolean usertask) {
		this.usertask = usertask;
	}
	

}
