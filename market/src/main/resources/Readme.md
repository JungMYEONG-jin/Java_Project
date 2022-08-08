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

## 실행법
- jar 파일을 서버에서 백그라운드 실행해주자.
- nohup java -jar ${lib_name}.jar &



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



> spring boot 1.5.22로 다운그레이드하기
> 왜 하는지...?
> 최대 자바6까지 서버에서 사용할 수 있을것 같음... 7~8로 변경시 기존 프로젝트에 사이드 이펙트 영향 상당해서...
> 하지만 spring boot 2 이상부터 최소 자바8을 사용해야함
> 따라서 다운그레이드해야 spring boot 사용이 가능...

우선 버전 확인을 해야한다.
https://github.com/spring-projects/spring-boot 접속해서
tag를 비교해 가장 최근 1.5.22 버전으로 하기로 결정

gradle도 버전 변경을 해야함. 자바6을 지원해야 하기 때문...

terminal에 접속해 

```shell
./gradlew wrapper --gradle-version 4.10
만약 실패시 gradle/gradle-wrapper.properties 직접 버전 변경

# 3.xx 자바7부터 지원
# 2.xx는 intellij에서 지원안함...
# compile level을 java6으로 수정하는 방향으로 가야할듯..?
```


```groovy
// gradle 해당 코드 복사

plugins {
	id 'org.springframework.boot' version '1.5.22.RELEASE'
	id 'java'
}

compileJava {
	sourceCompatibility = 1.6
	targetCompatibility = 1.6
}

jar {
	baseName = 'market'
	version =  '0.0.1-SNAPSHOT'
}

ext['tomcat.version']='7.0.109' // for java 6

repositories {
	mavenCentral()
	mavenLocal()
}

dependencies {
//	implementation 'org.junit.jupiter:junit-jupiter:5.8.1'
    compile("org.springframework.boot:spring-boot-starter-web")
	compile('org.springframework.boot:spring-boot-starter-data-jpa')
	compile('org.springframework.boot:spring-boot-starter-jdbc')
	compile('com.googlecode.json-simple:json-simple:1.1.1')
	compile('com.nimbusds:nimbus-jose-jwt:3.10')
	compile('commons-dbcp:commons-dbcp:1.4')
	compile('log4j:log4j:1.2.11')
	compile('org.jsoup:jsoup:1.10.3')
	compile('org.apache.httpcomponents:httpclient:4.2.4')
	compile('org.modelmapper:modelmapper:2.4.4')
//	compile('org.projectlombok:lombok:1.18.24')
//	annotationProcessor 'org.projectlombok:lombok:1.18.24'
	compile fileTree(dir: 'src/main/resources/lib/', include:['*.jar'])
	compile 'com.h2database:h2:1.4.200'
	testCompile("org.springframework.boot:spring-boot-starter-test")
}
```

버전 변경 완료


> tomcat 버전 변경하기

- 왜 버전을 변경하는지?
- 기본 내장 tomcat은 8 이상을 사용.. 
- java6에서도 사용 가능하려면 7.xx 사용해야함
- Building Apache Tomcat requires a Java 6 JDK to be installed and optionally a Java 7 JDK installed in parallel with Java 6 one. The Java 7 JDK is only required if you wish to build Tomcat with JSR-356 (Java WebSocket 1.1) support.

```shell
ext['tomcat.version']='7.0.109' # for java 6
# 해당 문구 build.gradle에 추가해주자..
```
