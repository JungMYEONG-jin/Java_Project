package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    // 해당 기능 쓰면 해당 값을 가져옴
    @Value("#{target.username+' '+target.age}")
    String getUsername();

}
