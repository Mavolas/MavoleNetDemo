package com.mavole.mavolenet.exception;


public class CommonHttpException {

	private int ecode;

	private String emsg;

	public CommonHttpException(int ecode, String emsg) {
		this.ecode = ecode;
		this.emsg = emsg;
	}

	public int getEcode() {
		return ecode;
	}

	public String getEmsg() {
		return emsg;
	}
}