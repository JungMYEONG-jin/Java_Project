package com.market.property;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketPropertyTest {

    @Autowired
    MarketProperty marketProperty;

    @Description(value = "프로퍼티 설정 확인")
    @Test
    void propertyTest() {

        String output_xml_file_name = marketProperty.getOutput_xml_file_name();
        System.out.println("output_xml_file_name = " + output_xml_file_name);
        String output_xml_path = marketProperty.getOutput_xml_path();
        System.out.println("output_xml_path = " + output_xml_path);

        Assertions.assertThat(output_xml_file_name).isEqualTo("market_appinfo_%s.xml");
        Assertions.assertThat(output_xml_path).isEqualTo("/static/output/");
    }
}