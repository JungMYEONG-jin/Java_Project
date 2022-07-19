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



## 작동원리
1. daemon init을 한다.
2. daemon init시 sender도 init
3. db에서 sendList를 가져와 크롤링을 시작한다.
4. 크롤링이 끝난후 MarketProperty에서 설정한 FILE_UPDATE_LIMIT_SEC 만큼 기다린다.
5. 해당 시간 초과후 결과를 xml로 지정한 경로에 저장한다.
6. 값을 -1로 init 시킨다.
7. 끝


