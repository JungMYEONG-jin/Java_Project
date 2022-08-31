package com.market.crawling;

import com.market.api.apple.AppleApi;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dto.SendInfo;
import com.market.exception.CrawlingException;

public class IntegratedCrawler implements MarketCrawler {

    private Crawling crawling;
    private AppleApi appleApi;
    
    public IntegratedCrawler() {
        crawling = new Crawling();
        appleApi = new AppleApi();
    }

    @Override
    public CrawlingResultData getData(SendInfo sendInfo) throws CrawlingException, Exception {
        if(sendInfo.getOsType().equals(SendInfo.OS_TYPE_IOS_API)){
            return appleApi.getCrawlingResult(sendInfo.getAppPkg());
        } else {
            return crawling.crawling(sendInfo);
        }
    }
}
