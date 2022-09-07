package com.market.crawling;

import com.market.crawling.data.CrawlingData;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dto.SendInfo;

public interface ICrawling {
    CrawlingResultData crawling(SendInfo sendInfo);
}
