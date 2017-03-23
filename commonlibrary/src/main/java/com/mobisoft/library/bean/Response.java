package com.mobisoft.library.bean;

import java.io.Serializable;

/**
 * 返回值结果Entity
 * 
 * @author jason
 *
 */
public class Response implements Serializable {

	private static final long serialVersionUID = -8841657612025694890L;

	private long t1;
	private long t2;
	private long t3;
	private String ts;
	private String cmd;
	private Object payload;
	private boolean result = true;
	private String error;

	private int localErrorCode;
	private String localErrorMsg;

	public Response(boolean result, int localErrorCode, String localErrorMsg) {
		super();
		this.result = result;
		this.localErrorCode = localErrorCode;
		this.localErrorMsg = localErrorMsg;
	}

	public long getT1() {
		return t1;
	}

	public long getT2() {
		return t2;
	}

	public long getT3() {
		return t3;
	}

	public String getTs() {
		return ts;
	}

	public String getCmd() {
		return cmd;
	}

	public Object getPayload() {
		return payload;
	}

	public boolean isResult() {
		return result;
	}

	public String getError() {
		return error;
	}

	public void setT1(long t1) {
		this.t1 = t1;
	}

	public void setT2(long t2) {
		this.t2 = t2;
	}

	public void setT3(long t3) {
		this.t3 = t3;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getLocalErrorMsg() {
		return localErrorMsg;
	}

	public void setLocalErrorMsg(String localErrorMsg) {
		this.localErrorMsg = localErrorMsg;
	}

	public int getLocalErrorCode() {
		return localErrorCode;
	}

	public void setLocalErrorCode(int localErrorCode) {
		this.localErrorCode = localErrorCode;
	}

}
