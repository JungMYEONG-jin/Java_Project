package com.market.entity;

import com.market.base.BaseTime;

import javax.persistence.*;

@Entity
@Table(name = "MBM_MARKET_INFO")
public class Market extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    @Column(name = "APP_ID", length = 256, nullable = false)
    private String appId;
    @Column(name = "APP_PACKAGE", length = 512, nullable = false)
    private String appPkg;

    @Column(name = "OS_TYPE", length = 30, nullable = false)
    private String osType;
    @Column(name = "STORE_URL", length = 4000)
    private String storeUrl;
    @Column(name = "TITLE_NODE", length = 4000)
    private String titleNode;
    @Column(name = "VERSION_NODE", length = 4000)
    private String versionNode;
    @Column(name = "UPDATE_NODE", length = 4000)
    private String updateNode;
    @Column(name = "ETC1_NODE", length = 4000)
    private String etc1Node;
    @Column(name = "ETC2_NODE", length = 4000)
    private String etc2Node;
    @Column(name = "ETC3_NODE", length = 4000)
    private String etc3Node;
    @Column(name = "ETC4_NODE", length = 4000)
    private String etc4Node;
    @Column(name = "ETC5_NODE", length = 4000)
    private String etc5Node;

    public Market() {
    }

    public Market(Long id, String appId, String appPkg, String osType, String storeUrl, String titleNode, String versionNode, String updateNode, String etc1Node, String etc2Node, String etc3Node, String etc4Node, String etc5Node) {
        this.id = id;
        this.appId = appId;
        this.appPkg = appPkg;
        this.osType = osType;
        this.storeUrl = storeUrl;
        this.titleNode = titleNode;
        this.versionNode = versionNode;
        this.updateNode = updateNode;
        this.etc1Node = etc1Node;
        this.etc2Node = etc2Node;
        this.etc3Node = etc3Node;
        this.etc4Node = etc4Node;
        this.etc5Node = etc5Node;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
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

    public String getEtc3Node() {
        return etc3Node;
    }

    public void setEtc3Node(String etc3Node) {
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
}
