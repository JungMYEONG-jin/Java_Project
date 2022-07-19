package com.market.api.apple;

public enum AppleAppId {

    smarttax_ios("1180862826"),
    noface_ios("1194492265"),
    poney_ios("1201584268"),
    senior_ios("1174226278"),
    missionplus_ios("486789090"),
    safe2chssf_ios("896037244"),
    ebond_ios("481951052"),
    mfolio_ios("1169292742"),
    salimi_ios("486872386"),
    sbizbank_ios("587766126"),
    sbank_ios("357484932"),
    sunnyalarm_ios("1163682534"),
    smailid_ios("1163682534"),
    sunnyclub_ios("1064933073"),
    shinhanvn_ios("1071033810"),
    sunnycalculator_ios("1177867277"),
    sunnyswatch_ios("1062479593"),
    shinhancn_ios("1143462205"),
    sregister_ios("1052014390"),
    sunnybank_ios("1006963773"),
    shinhanid_ios("1287409348"),
    shinhanca_ios("1093365921"),
    smailvn_ios("1016762804"),
    s_tongjang_ios("1039324990"),
    shinhansa_ios("1093362779"),
    ssurtax_ios("725400385"),
    shinhangi_ios("1131925580"),
    shinhankh_ios("1071033100"),
    smailcn_ios("1249194034"),
    sbankmini_ios("458061594"),
    foreignerbank_ios("1190468026");

    private String appPkg;

    AppleAppId(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getAppPkg(){
        return appPkg;
    }

}
