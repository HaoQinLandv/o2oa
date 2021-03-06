package com.x.base.core.project.config;

import java.io.File;
import java.util.LinkedHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.x.base.core.project.annotation.FieldDescribe;
import com.x.base.core.project.gson.GsonPropertyObject;
import com.x.base.core.project.gson.XGsonBuilder;
import com.x.base.core.project.tools.DefaultCharset;

public class CenterServer extends GsonPropertyObject {

	private static final Integer default_port = 20030;
	private static final Integer default_scanInterval = 0;

	public static CenterServer defaultInstance() {
		return new CenterServer();
	}

	public CenterServer() {
		this.sslEnable = false;
		this.redeploy = true;
		// this.host = "";
		this.port = default_port;
		this.httpProtocol = "";
		this.proxyHost = "";
		this.proxyPort = default_port;
		this.scanInterval = default_scanInterval;
	}

	@FieldDescribe("是否启用ssl")
	private Boolean sslEnable;
	@FieldDescribe("每次启动是否重新部署所有应用.")
	private Boolean redeploy;
	// @FieldDescribe("主机名")
	// private String host;
	@FieldDescribe("端口")
	private Integer port;
	@FieldDescribe("对外http访问协议")
	private String httpProtocol;
	@FieldDescribe("代理主机名")
	private String proxyHost;
	@FieldDescribe("代理端口")
	private Integer proxyPort;
	@FieldDescribe("重新扫描war包时间间隔(秒)")
	private Integer scanInterval;
	@FieldDescribe("其他参数")
	private LinkedHashMap<String, Object> config;

	public String getHttpProtocol() {
		return StringUtils.equals("https", this.httpProtocol) ? "https" : "http";
	}

	public Integer getScanInterval() {
		if (null != this.scanInterval && this.scanInterval > 0) {
			return this.scanInterval;
		}
		return default_scanInterval;
	}

	public Boolean getRedeploy() {
		return BooleanUtils.isTrue(this.redeploy);
	}

	public Boolean getSslEnable() {
		return BooleanUtils.isTrue(this.sslEnable);
	}

	public Integer getPort() {
		if (null != this.port && this.port > 0 && this.port < 65535) {
			return this.port;
		}
		return default_port;
	}

	public String getProxyHost() throws Exception {
		return StringUtils.isNotEmpty(this.proxyHost) ? this.proxyHost : "";
	}

	public Integer getProxyPort() {
		if (null != this.proxyPort && this.proxyPort > 0) {
			return this.proxyPort;
		}
		return this.getPort();
	}

	public LinkedHashMap<String, Object> getConfig() {
		if (null == this.config) {
			return new LinkedHashMap<String, Object>();
		}
		return this.config;
	}

	public void setSslEnable(Boolean sslEnable) {
		this.sslEnable = sslEnable;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setScanInterval(Integer scanInterval) {
		this.scanInterval = scanInterval;
	}

	public void setRedeploy(Boolean redeploy) {
		this.redeploy = redeploy;
	}

	public void setConfig(LinkedHashMap<String, Object> config) {
		this.config = config;
	}

	// public void setHost(String host) {
	// this.host = host;
	// }

	public void setHttpProtocol(String httpProtocol) {
		this.httpProtocol = httpProtocol;
	}

	public void save() throws Exception {
		File file = new File(Config.base(), Config.PATH_CONFIG_CENTERSERVER);
		FileUtils.write(file, XGsonBuilder.toJson(this), DefaultCharset.charset);
	}

}
