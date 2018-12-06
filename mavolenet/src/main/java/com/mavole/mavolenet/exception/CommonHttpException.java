package com.mavole.mavolenet.exception;


public class CommonHttpException {

	private int ecode;

	private Object emsg;

	public CommonHttpException(int ecode, Object emsg) {
		this.ecode = ecode;
		this.emsg = emsg;
	}

	public int getEcode() {
		return ecode;
	}

	public Object getEmsg() {
		return emsg;
	}
}