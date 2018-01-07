package com.douyu.bean;
/**
 * zjt 2018年1月7日15:59:30
 */
public class DanMuServer {
	private String ip;

	private String port;

	public DanMuServer(String ip, String port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	public String getIp() {
		return ip;
	}

	public String getPort() {
		return port;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "DanMuServer [ip=" + ip + ", port=" + port + "]";
	}
}
