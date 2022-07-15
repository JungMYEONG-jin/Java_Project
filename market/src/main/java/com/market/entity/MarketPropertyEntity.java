package com.market.entity;

import com.market.base.BaseTime;
import com.market.daemon.dao.MarketPropertyDao;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
}
