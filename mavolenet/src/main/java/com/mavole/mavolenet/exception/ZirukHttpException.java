package com.mavole.mavolenet.exception;


public class ZirukHttpException {

	private int ecode;

	private Object emsg;

	public ZirukHttpException(int ecode, Object emsg) {
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