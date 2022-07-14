package com.market.entity;

import com.market.base.BaseTime;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Market extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ")
    private Long id;

    @Column(name = "APP_ID")
    private String appId;
    @Column(name = "APP_PACKAGE")
    private String appPkg;

    @Column(name = "OS_TYPE")
    private String osType;
    @Column(name = "STORE_URL")
    private String storeUrl;
    @Column(name = "TITLE_NODE")
    private String titleNode;
    @Column(name = "VERSION_NODE")
    private String versionNode;
    @Column(name = "UPDATE_NODE")
    private String updateNode;
    @Column(name = "ETC1_NODE")
    private String etc1Node;
    @Column(name = "ETC2_NODE")
    private String etc2Node;
    @Column(name = "ETC3_NODE")
    private String etc3Node;
    @Column(name = "ETC4_NODE")
    private String etc4Node;
    @Column(name = "ETC5_NODE")
    private String etc5Node;


}
