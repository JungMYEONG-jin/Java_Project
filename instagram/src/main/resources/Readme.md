> 처음 세팅하기

- h2 연결할때 처음에 로컬에 만들고 그다음 remote 연결 가능.

```shell
jdbc:h2:~/instagram 으로 연결
그 후 jdbc:h2:tcp://localhost/~/instagram 으로 접속
```

- spring data jpa는 application.properties에서 접속 정보 세팅 가능.
```shell
spring.datasource.url=jdbc:h2:tcp://localhost/~/datajpa
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

spring.jpa.hibernate.ddl-auto=create
spring.jpa.properties.show_sql=true
spring.jpa.properties.format_sql=true

logging.level.org.hibernate.SQL=debug
```

> @Transient
- 해당 어노테이션은 영속성 관리에서 제외하고 싶을때 사용한다.
- 메서드에 사용하지말고 필드에 사용하자.
- 영속성에서 제외되기 때문에 신중한 사용이 필요함.


> @NotBlank, @NotEmpty, @NotNull의 차이점
- @NotBlank : 빈 값, null, 빈 공백(스페이스)를 체크한다.
- @NotEmpty : 빈 값, null을 체크한다.
- @NotNull : null을 체크한다.


> @Around
- @Around는 Spring AOP중 하나다.
- 클라이언트 호출을 가로채서 실행이 된다. 따라서 종료후에 꼭 proceed를 실행해 넘겨줘야한다.
- 그렇지 않다면 클라이언트 호출을 가로챈 이후로 프로그램은 작동하지 않게 된다.

> @EnableJpaAuditing
- BaseEntity에 시간을 자동 관리하게 위임한다.
- 하지만 위임하고 나서 @EnableJpaAuditing 해당 어노테이션을 메인에 추가해야함.
- 추가하지 않으면 추적하지 않아 시간을 자동 관리 하지 않음.


```java
package com.instagram.config;

import com.instagram.config.oauth.OAuth2DetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    private final OAuth2DetailsService oAuth2DetailsService;
//    private final PasswordEncoder passwordEncoder;
    @Bean
    public BCryptPasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**")
//                .authenticated().anyRequest().permitAll()
//                .and().formLogin().loginPage("/auth/signin") // Get 인증이 필요한 페이지에 접근 시 호출 페이지
//                .loginProcessingUrl("/auth/signin")
//                .defaultSuccessUrl("/")
//                .and()
//                .oauth2Login()
//                .userInfoEndpoint()// code, access_token 받는 과정 생략하고 회원정보 바로 받을 수 있게
//                .userService(oAuth2DetailsService);
//        http.headers().frameOptions().sameOrigin();
//        return http.build();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super 삭제 -> 기존 시큐리티가 가지고 있는 기능이 모두 비활성화
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/auth/signin") //Get 인증이 필요한 페이지에 접근 시 호출하는 페이지
                .loginProcessingUrl("/auth/signin") //post로 이 주소가 요청되면 스프링 시큐리티가 요청을 낚아채서 로그인 대신 진행
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()//코드, 엑세스 토큰 받는 과정을 생략하고 회원정보를 바로 받을 수 있다.
                .userService(oAuth2DetailsService);
    }

}
```
- @EnableWebSecurity : Spring Security 설정을 활성화 시킨다.
- csrf().disable() : h2-console 화면을 사용하기 위해 해당 옵션을 disable함
- authorizeRequest : URL별 권한 관리를 설정하는 옵션의 시작점, 해당 옵션이 선언돼야만 antMatchers 옵션 사용 가능.
- antMatchers : 권한 관리 대상을 지정하는 옵션, 해당 주소를 가진 API는 권한을 가진 사용자만 사용 가능하다. 이런 의미
- anyRequest : 설정된 값들 이외 나머지 URL 나타냄, permitAll 해서 모두 허용시켜줌. authenticated() 사용시 인증된 사용자들에게만 허용.
- logout().logoutSuccessUrl("/) : 로그아웃시 / 주소로 이동
- oauth2Login : 로그인 기능에 대한 여러 설정 진입점을 의미.
- userInfoEndpoint : 로그인 성공 이후 사용자 정보를 가져올 때의 설정 담당.
- userService : 로그인 성공 시 후속 조치를 진행할 service 인터페이스 구현체를 등록하는 부분.
- 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있음.

> Entity 는 직렬화 기능을 넣지 않는게 좋다. 다른 Entity와 언제 관계를 형성할 지 모르고 성능 저하를 일으킬 수 있기 때문이다.

