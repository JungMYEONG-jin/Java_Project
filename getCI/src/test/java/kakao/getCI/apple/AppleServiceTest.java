package kakao.getCI.apple;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AppleServiceTest {

    @Autowired
    AppleService appleService;


    @Test
    void getClassResultTest() throws MalformedURLException, ParseException {
        CrawlingResultData crawlingResult = appleService.getCrawlingResult("357484932");
        System.out.println("crawlingResult = " + crawlingResult);
    }
}