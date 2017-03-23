package com.mobisoft.library.http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.mobisoft.common.gateway.Req;
import com.mobisoft.library.AppConfig;
import com.mobisoft.library.Constants;
import com.mobisoft.library.util.JsonUtil;

public class BasePacket {

	private Map<String, Object> body = null;
	private List<NameValuePair> params = null;
	private  Req req = new Req();

	public BasePacket(Object objs) {
		super();
		setBody(new HashMap<String, Object>());
		req.setT1(System.currentTimeMillis() / 1000);
		req.setTs(System.currentTimeMillis() / 1000 + "");
		req.setAccount(Constants.ACCOUNT);
		req.setUdid(AppConfig.UUID);
		req.setOstype(Constants.OS_SYSTEM);
		req.setOsmodel(Constants.MODEL);
		req.setOsversion(Constants.RELEASE);
		req.setAppversion(Constants.VERSION_CODE + "");
		req.setPayload(objs);

		getBody().put("ts", System.currentTimeMillis() / 1000);
		getBody().put("t1", System.currentTimeMillis() / 1000);
		getBody().put("account", Constants.ACCOUNT);
		getBody().put("gzip", false);
		getBody().put("udid", AppConfig.UUID);
		getBody().put("mudid", "");
		getBody().put("ostype", Constants.OS_SYSTEM);
		getBody().put("osmodel", Constants.MODEL);
		getBody().put("osversion", Constants.RELEASE);
		getBody().put("appversion", Constants.VERSION_CODE);
		getBody().put("payload", getPayload(objs));

		getBody().put("digest", Md5(getPayload(objs) + getBody().get("ts") + Md5(Constants.PASSWORD)));
		setParams(new ArrayList<NameValuePair>());
	}

	private String Md5(String request) {

		String md5Str = request;
		MessageDigest md5 = null;

		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			return "";
		}

		md5.reset();

		try {
			md5.update(md5Str.getBytes(HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		byte[] b = md5.digest();
		String s = makeString(b);
		// String b64result = Base64.encodeToString(s.getBytes(),
		// Base64.DEFAULT);
		return s;
	}

	private String makeString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(String.format("%02x", b[i]));
		}
		return sb.toString();
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public String getPayload(Object objs) {
		return JsonUtil.obj2Str(objs);
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
		params.add(new BasicNameValuePair("req", toJson()));
	}

	public String toJson() {
		// return JsonUtil.map2json(getBody());
		String json = JsonUtil.obj2Str(req);
		System.out.print(json);
		return json;
	}

}
