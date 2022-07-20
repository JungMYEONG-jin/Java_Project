> File 경로가 정상적으로 설정인데 read 시 오류가 나면 build가 먼저 진행완료인지 확인!!!
> 빌드후 해당 상대 경로에서 가져오는것이기때문...



수동 주입이 필요할때 해당 코드 이용하자...
```java
package com.market.provider;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
            applicationContext = ac;
    }

    private static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

}

```


> JPA Entity use defualt lock setting vs Java Synchronized
> JPA는 기본적으로 영속성 Lock을 제공한다. 해당 기능만 써도 충분하므로 굳이 synchronized를 사용할 필요는 없다.
> CRUD 에 Transactional 사용하자!


## Spring Scheduler
- fixedDelay : 해당 함수 로직이 끝난후 몇초후에 재시작 할건지
- fixedRate : 해당 함수 로직 시작후 몇초후에 재시작 할건지
- cron : linux cron 이랑 같음. 초 
- 주의할점은 스케줄을 돌릴 메소드는 void 여야 하며 파라미터를 받을 수 없다!!






## 작동원리
1. daemon init을 한다.
2. daemon init시 sender도 init
3. db에서 sendList를 가져와 크롤링을 시작한다.
4. 크롤링이 끝난후 MarketProperty에서 설정한 FILE_UPDATE_LIMIT_SEC 만큼 기다린다.
5. 해당 시간 초과후 결과를 xml로 지정한 경로에 저장한다.
6. 값을 -1로 init 시킨다.
7. 끝


## 고민사항
1. market 수정일은 수정일 수동 세팅후 다시 자동 추적하여 해결 가능 할 것으로 보임.
2. send는 작동후 삭제되는데 이를 어떻게 다시 가져오는지 원리가?
3. 기존 테이블과 어떻게 연결할지..?

