package com.market.crawling;

import com.market.api.apple.AppleApi;
import com.market.api.google.GoogleApi;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dto.SendInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IntegratedCrawler{
    @Autowired
    GoogleApi googleApi;

    public CrawlingResultData getData(SendInfo sendInfo) {
        ICrawling crawling = null;
        if(sendInfo.getOsType().equals(SendInfo.OS_TYPE_IOS_API)){
            crawling = new AppleApi();
        } else if(sendInfo.getOsType().equals(SendInfo.OS_TYPE_AND_API)){
            crawling = googleApi;
        } else{
            crawling = new Crawling();
        }
        return crawling.crawling(sendInfo);
    }
}
