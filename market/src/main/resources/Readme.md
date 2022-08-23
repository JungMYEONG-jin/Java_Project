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
	sourceCompatibility = 1.6 // 해당 프로젝트 소스가 무슨 자바 버전으로 간주할지
	targetCompatibility = 1.6 // 최소 실행이 가능하게 할 자바 버전
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


> Mac Java6 설치 방법

- 현재 mac에서 지원하는 최소한 버전은 java7 임..
- java6 설치시 이미 설치된 java가 있다며 거부당함.(기본 java8)
- brew로 해결이 가능함.. 하지만 결국 작동 불가인듯?

```shell
brew install cask
brew info java6 --cask # java6 있는지 확인
brew install java6 --cask # java6 설치
# 설치 완료
```


> Gradle offline build
- gradle/wrapper/gradle-wrapper.properties를 수정하면됨
- distributionURL=gradle-version.bin.zip 으로 변경하자.
- 그리고 해당 폴더에 실제로 bin.zip 폴더를 넣어주면 됨.
- 하지만 이렇게 해서는 해결불가...cache 파일이 없다.
```shell
I have found a solution. A few files in C:\Users"USER_NAME".gradle folder contains paths to libs with <USER_NAME>. I tried to replace “USER_NAME” to my user name, however it wasn’t succesfull. I decided change path to .gradle folder from C:\Users<USER_NAME>.gradle to D:.gradle, download libs on PC-1 and then move .gradle folder from PC-1 to PC-2. On PC-2 I launch folowing command:

gradle myTask --gradle-user-home D:\.gradle --offline
and it worked. In order to change gradle folder on PC-1 you have to use the following command:

gradle --gradle-user-home D:\.gradle --offline
```
- 기본적으로 gradle은 username/.gradle 로 cache 파일을 저장한다.
- 각 pc마다 username이 다르기 때문에 여기서 문제가 발생!
- 그래서 gradle home directory를 설정해주고 cache 생성후 옮기면 됨.
```shell
# online PC
gradle bootRepackage -g C:\\Gradle\\.gradle
# 해당 폴더 복사해서 offline PC에 똑같은 경로에 설정하고
gradle bootRepackage -g C:\\Gradle\\.gradle --offline 하면 됨.

gradle clean bootRun -g C:\\Gradle\\.gradle --offline
```

## Checked Exception vs Unchecked Exception
- 체크 익셉션은 정말 치명적인 경우에만 사용하자.
- 왜 그래야 하나?
- 무조건 try catch 로 처리하거나 현재 메소드에서 처리하기 싫다면 throws를 해야함
- 이렇게 되면 만약 다른 메소드에서 해당 메소드를 사용한다면 예외를 처리하거나 또 throws 해서 던져야됨...
- 악순환의 반복이 된다.
- 예외에서 회복할 수 있다는 장점이 있지 않은가?
- 회복할 수 있다는 장점이 있긴 하지만... 그런 경우는 거의 찾기가 힘들다고 한다.
- 언체크 익셉셥의 장점은?
- try catch 안해도됨. 메소드에 throws 안해도됨. 
- 만약 기존 프로젝트에 checked exception을 추가했다면 해당 메소드를 사용하는 모든 메소드에 throws를 추가하거나 try catch 로 예외 처리를 해야함.
- 하지만 runtime은 아무것도 할 필요가 없다!!
- 둘중 고민이 된다면 우선 runtime exception 사용하자.

## ThreadPoolTaskExecutor vs ThreadPoolExecutor
- 쓰레드풀 이라는 공통점이 있다.
- 실제로 기능은 똑같으며 스프링에서 직접 제공하는 ThreadPoolTaskExecutor과 자바에서 concurrency 에서 제공하는 ThreadPoolExecutor 이라는 차이점이 있을뿐이다.
- 쓰레드풀의 장점은 미리 쓰레드를 생성해놔서 적절하게 제공한다는 점이다.
- 사용할때마다 재 생성할 필요가 없으니 메모리 절약이 가능하며 성능도 향상된다.

```java

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class MyThreadPoolConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setQueueCapacity(100);
        executor.setMaxPoolSize(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("MJ");
        executor.setRejectedExecutionHandler(new UserRejectHandler());
        return executor;
    }

    static class MyClass {
        @Autowired
        ThreadPoolTaskExecutor taskExecutor;

        void doMultiJob() {
            taskExecutor.execute(() -> {
//                run something...
            });
        }

        void doMultiJobByThreadPoolExecutor() {
            BlockingQueue queue = new LinkedBlockingQueue(100);
		    UserThreadFactory factory = new UserThreadFactory("MJ");
		    UserRejectHandler handler = new UserRejectHandler();
		    threadPoolExecutor = new ThreadPoolExecutor(50, 100, 60, TimeUnit.SECONDS, queue, factory, handler);
            ThreadPoolExecutor.execute(()->{
                // run something...
            });
        }
    }
}

```
- 쓰레드가 모두 안전하게 작업을 마친후 어떤 행동을 하고 싶다면 CountDownLatch를 사용하면 된다.
- init 할때 작업하는 스레드의 개수 만큼 생성해주면됨.

```java
import java.util.concurrent.CountDownLatch;

static class CountDown {
    void setCountDown() {
        int threadSize = 100;
        CountDownLatch countDownLatch = new CountDownLatch(threadSize);
        
        // 스레드가 모든 작업을 완료할때까지 대기
        try {
            countDownLatch.await(); // 크롤링 다 돌때까지 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // 모든 스레드가 마무리 됨. 이제 thread safe하게 작업 가능
        // do something...
    }
}
```


## Logback config
- RollingFileAppender/TimeBasedRollingPolicy combination should not have %i token in the FileNamePattern.
The following config snippet.
- %d와 %i가 필수라면서 넣으면 계속 오류가 나는 현상 발생.. 알고보니 저 두개의 조합에서 %i를 사용하면 안된다고함.