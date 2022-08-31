>@Component 빈 스캐닝 방식은 클래스 이름의 첫글자만 소문자로 변경해서 id로 등록한다.
> 만약 Hello 클래스에 해당 어노테이션을 붙으면 id는 hello가 되는것


## 애노테이션 적용하는법
- 우선 핵심 3가지로 구분하자.
- @Repository : 리포지토리 클래스에 적용
- @Service : service 클래스에 적용
- @Controller : controller 클래스에 적용
- 이 3가지 외에 구분하기 어려운 것들은 @Component로 등록하자.
- 아니면 커스텀 애노테이션을 만들어 적용도 가능하다.


## 자바 코드에 의한 빈 등록 
1. @Configuration 클래스의 @Bean method
@Bean의 메소드 이름이 빈의 아이디가 된다. 이점 유의!
```java
import com.spring.vol2.chapter1.di.AnnotatedHello;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Config {
    @Bean
    public AnnotatedHello annotatedHello(){
        return new AnnotatedHello();
    }
}
```

## 자바 코드에 의한 빈 등록이 외부 설정파일을 이용하는 것에 대한 장점은?

1. 컴파일러나 IDE를 통해 타입 검증이 가능하다.
2. IDE의 자동완성 기능을 최대한 이용할 수 있다.
3. 이해가 쉽다.
4. 복잡한 빈 설정이나 초기화 작업을 손쉽게 적용할 수 있다.

## Configuration등록을 안한 일반 클래스에서 Bean 등록시 주의점

1. 해당 클래스에 만약 A, B Bean이 있고 printer라는 빈을 각각의 빈에 주입한다고 하자.
2. Spring에서 컴포넌트는 singleton이 보장되어야 하는데 이렇게 일반 클래스에서(Configuration이 아닌) 사용하면 매번 주입시 다른 printer를 받게 된다.
3. 이는 singleton이 아니게됨!!
4. 해결법은 Printer를 해당 클래스가 직접 주입받으면됨...

## @Autowired
- required=false로 하면 해당 빈이 존재하지 않아도 오류가 발생하지 않음.


## @ComponentScan
- ComponentScan은 Component를 자동으로 스캔하는것이다.
- @Configuration 사용시 주의해야함. 자기 자신을 또 빈등록 할 수 있기 때문
- 이럴땐 excludeFilters를 사용해 자기를 제외하자.

```java

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.spring.vol2.chapter1.di",
        excludeFilters = @ComponentScan.Filter(Configuration.class))
public class AppConfig{
    
}

```

> EntityManager는 스프링 빈으로 등록되지 않는다. 빈으로 등록한것은 EntityManagerFactory  타입의 빈을 생성하는 LocalContaìnerEntìtyManagerFactoryBean이다.
> 따라서 @Autowired와 같은 스프링의 DI 방법으로는 EntityManager를 주입받을 수 없다.
> @PersistenceContext를 사용하면된다.
> HttpRequestHandler 모델과 뷰 개념이 없는 http 기반의 로우레벨 서비스를 개발할 때 이용 가능.

## 핸들러 매핑 전략
1. BeanNameUrlHandlerMapping
빈의 이름에 있는 URL을 HTTP 요청의 URL과 비교해서 일치하는 빈을 찾아준다. 가장 직관적이고 쉬움.
*, **, ? 같은 와일드 카드 사용이 가능하다. 하지만 컨트롤러의 개수가 많아지면 전체적인 매핑구조를 한눈에 파악하고 관리하기 불편하다. 복잡한 앱에서는 잘 사용하지 않는다.
2. ControllerBeanNameHandlerMapping
빈의 아이디나 이름을 이용해 매핑해주는 핸들러 전략이다. 디폴트 매핑이 아니므로 사용하려면 전략 빈으로 등록이 필요.
3. ControllerClassNameHandlerMapping
빈 이름 대신 클래스 이름을 URL에 매핑해주는 클래스이다.
4. SimpleUrlHandlerMapping
URL과 매핑정보를 한곳에 모아놓을 수 있는 매핑 전략이다.
5. DefaultAnnotationHandlerMapping
@RequestMapping 이라는 애노테이션을 컨트롤러 클래스나 메소드에 직접 부여하고 매핑하는 전략이다.
메소드 단위 매핑이 가능해서 컨트롤러의 개수를 획기적으로 줄일 수 있다는 장점이 있다.
GET, POST 등 분리해서 사용 가능. 요즘에는 PostMapping, GetMapping 등등
디폴트 핸들러 매핑이므로 기본 설정을 바꾸지 않는한 그냥 써도 됨.


