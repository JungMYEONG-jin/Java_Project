package com.market.api.google;

public enum GoogleAppId {

    smarttax_android("com.shinhan.smarttaxpaper"),
    noface_android("com.shinhan.noface"),
    poney_android("com.shinhan.poney"),
    senior_android("com.shinhan.senior"),
    missionplus_android("com.shinhan.missionbanking"),
    safe2chssf_android("com.shinhan.approval"),
    ebond_android("com.ebond"),
    mfolio_android("com.shinhan.smartfund"),
    salimi_android("com.shinhan.smartcaremgr"),
    sbizbank_android("com.shinhan.sbizbank"),
    sbank_android("com.shinhan.sbanking"),
    sunnyalarm_android("com.shinhan.sunnyalarm"),
    smailid_android("com.shinhan.smaild"),
    sunnyclub_android("com.shinhan.global.vn.sclub"),
    shinhanvn_android("com.shinhan.global.vn.bank"),
    sunnycalculator_android("com.shinhan.sunnycalculator"),
    sunnyswatch_android("com.shinhan.swatchbank"),
    shinhancn_android("com.shinhan.global.cn.bank"),
    sregister_android("com.shinhan.register"),
    sunnybank_android("com.shinhan.speedup"),
    shinhanid_android("com.shinhan.global.id.bank"),
    shinhanca_android("com.shinhan.global.ca.bank"),
    smailvn_android("com.shinhan.smailvn"),
    s_tongjang_android("com.shinhan.mobilebankbook"),
    shinhansa_android("com.shinhan.global.ca.bank"),
    ssurtax_android("com.shinhan.trade.copper"),
    shinhangi_android("com.shinhan.global.gi.bank"),
    shinhankh_android("com.shinhan.global.kh.bank"),
    sbankmini_android("com.shinhan.sbankmini"),
    foreignerbank_android("com.shinhan.foreignerbank");

    private String appPkg;

    GoogleAppId(String appPkg) {
        this.appPkg = appPkg;
    }

    public String getAppPkg(){
        return appPkg;
    }

}
