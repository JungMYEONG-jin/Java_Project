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