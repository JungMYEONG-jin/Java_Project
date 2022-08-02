package com.market.entity;

import com.market.base.BaseTime;
import com.market.daemon.dao.MarketPropertyDao;
import javax.persistence.*;

@Entity
public class MarketPropertyEntity extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    @Column(name = "PROPERTY_VERSION")
    private String propertyVersion;
    @Column(name = "PROPERTY_STATUS")
    private String propertyStatus;
    @Column(name = "PROPERTY_DATA_TYPE")
    private String dataType;
    @Column(name = "PROPERTY_DATA")
    private String propertyData;
    // 생성일, 업데이트일 공통으로 묶음
    @Column(name = "REG_USER_ID")
    private String userId;
    @Column(name = "IS_SETTING")
    private String isSetting;
    private String etc1;
    private String etc2;
    private String etc3;

    // 원래 안좋은거지만 어쩔수없이..
    public MarketPropertyDao of(){
        MarketPropertyDao marketPropertyDao = new MarketPropertyDao();
        marketPropertyDao.setSeq(this.id.toString());
        marketPropertyDao.setPropertyVersion(this.propertyVersion);
        marketPropertyDao.setPropertyStatus(this.propertyStatus);
        marketPropertyDao.setPropertyDataType(this.dataType);
        marketPropertyDao.setPropertyData(this.propertyData);
        marketPropertyDao.setRegDt(this.getRegDt());
        marketPropertyDao.setUptDt(this.getUptDt());
        marketPropertyDao.setRegUserId(this.userId);
        marketPropertyDao.setIsSetting(this.isSetting);
        return marketPropertyDao;
    }

    public MarketPropertyEntity(Long id, String propertyVersion, String propertyStatus, String dataType, String propertyData, String userId, String isSetting, String etc1, String etc2, String etc3) {
        this.id = id;
        this.propertyVersion = propertyVersion;
        this.propertyStatus = propertyStatus;
        this.dataType = dataType;
        this.propertyData = propertyData;
        this.userId = userId;
        this.isSetting = isSetting;
        this.etc1 = etc1;
        this.etc2 = etc2;
        this.etc3 = etc3;
    }

    public MarketPropertyEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyVersion() {
        return propertyVersion;
    }

    public void setPropertyVersion(String propertyVersion) {
        this.propertyVersion = propertyVersion;
    }

    public String getPropertyStatus() {
        return propertyStatus;
    }

    public void setPropertyStatus(String propertyStatus) {
        this.propertyStatus = propertyStatus;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getPropertyData() {
        return propertyData;
    }

    public void setPropertyData(String propertyData) {
        this.propertyData = propertyData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsSetting() {
        return isSetting;
    }

    public void setIsSetting(String isSetting) {
        this.isSetting = isSetting;
    }

    public String getEtc1() {
        return etc1;
    }

    public void setEtc1(String etc1) {
        this.etc1 = etc1;
    }

    public String getEtc2() {
        return etc2;
    }

    public void setEtc2(String etc2) {
        this.etc2 = etc2;
    }

    public String getEtc3() {
        return etc3;
    }

    public void setEtc3(String etc3) {
        this.etc3 = etc3;
    }
}
