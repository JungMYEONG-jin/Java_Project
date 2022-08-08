package com.market.daemon.dao;


import com.market.crawling.data.CrawlingData;
import com.market.entity.Market;

/**
 * @author parkyk
 * �� ���� Class
 */
public class MarketInfo implements CrawlingData {
	public static String OS_TYPE_AND = "0";
	public static String OS_TYPE_IOS = "1";
	public static String OS_TYPE_IOS_API = "3";
	private String seq;
	private String appId;
	private String appPkg;
	private String osType;
	private String storeUrl;
	private String titleNode;
	private String versionNode;
	private String updateNode;
	private String regDt;
	private String uptDt;
	private String etc1Node;
	private String etc2Node;
	private String etc3Node;
	private String etc4Node;
	private String etc5Node;

	
	public MarketInfo(String seq, String appId, String appPkg,String osType, String storeUrl,
			 String titleNode, String versionNode, String updateNode,
			String regDt, String uptDt, String methodType) {
		super();
		this.seq = seq;
		this.appId = appId;
		this.appPkg = appPkg;
		this.osType = osType;
		this.storeUrl = storeUrl;
		this.titleNode = titleNode;
		this.versionNode = versionNode;
		this.updateNode = updateNode;
		this.regDt = regDt;
		this.uptDt = uptDt;
	}

	public MarketInfo(Market market){
		this.seq = market.getId().toString();
		this.appId = market.getAppId();
		this.appPkg = market.getAppPkg();
		this.osType = market.getOsType();
		this.storeUrl = market.getStoreUrl();
		this.titleNode = market.getTitleNode();
		this.versionNode = market.getVersionNode();
		this.updateNode = market.getUpdateNode();
		this.regDt = market.getRegDt();
		this.uptDt = market.getUptDt();
	}


	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppPkg() {
		return appPkg;
	}
	public void setAppPkg(String appPkg) {
		this.appPkg = appPkg;
	}
	public String getStoreUrl() {
		return storeUrl;
	}
	public void setStoreUrl(String storeUrl) {
		this.storeUrl = storeUrl;
	}
	public String getTitleNode() {
		return titleNode;
	}
	public void setTitleNode(String titleNode) {
		this.titleNode = titleNode;
	}
	public String getVersionNode() {
		return versionNode;
	}
	public void setVersionNode(String versionNode) {
		this.versionNode = versionNode;
	}
	public String getUpdateNode() {
		return updateNode;
	}
	public void setUpdateNode(String updateNode) {
		this.updateNode = updateNode;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	public String getUptDt() {
		return uptDt;
	}
	public void setUptDt(String uptDt) {
		this.uptDt = uptDt;
	}
	public String getEtc1Node() {
		return etc1Node;
	}
	public void setEtc1Node(String etc1Node) {
		this.etc1Node = etc1Node;
	}
	public String getEtc2Node() {
		return etc2Node;
	}
	public void setEtc2Node(String etc2Node) {
		this.etc2Node = etc2Node;
	}
	public void setMethodType(String etc3Node) {
		this.etc3Node = etc3Node;
	}
	public String getEtc4Node() {
		return etc4Node;
	}
	public void setEtc4Node(String etc4Node) {
		this.etc4Node = etc4Node;
	}
	public String getEtc5Node() {
		return etc5Node;
	}
	public void setEtc5Node(String etc5Node) {
		this.etc5Node = etc5Node;
	}
	@Override
	public String getOsType() {
		return osType;
	}
	@Override
	public String toString() {
		return "MarketInfo [appId=" + appId + ", appPkg=" + appPkg
				+ ", osType=" + osType + ", storeUrl=" + storeUrl
				+ ", titleNode=" + titleNode + ", versionNode=" + versionNode
				+ ", updateNode=" + updateNode + ", regDt=" + regDt
				+ ", uptDt=" + uptDt + ", etc1Node=" + etc1Node + ", etc2Node="
				+ etc2Node + ", etc3Node=" + etc3Node + ", etc4Node="
				+ etc4Node + ", etc5Node=" + etc5Node + "]";
	}
	@Override
	public String getMethodType() {
		return etc3Node;
	}

}
