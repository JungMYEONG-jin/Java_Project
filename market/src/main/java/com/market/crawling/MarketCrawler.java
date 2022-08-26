package com.market.crawling;

import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dto.SendInfo;

public interface MarketCrawler {

    public CrawlingResultData getData(SendInfo sendInfo) throws Exception;
}
