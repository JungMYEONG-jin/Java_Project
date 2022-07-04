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
public class SecurityConfig{

    private final OAuth2DetailsService oAuth2DetailsService;
//    private final PasswordEncoder passwordEncoder;
    @Bean
    public BCryptPasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**")
                .authenticated().anyRequest().permitAll()
                .and().formLogin().loginPage("/auth/signin") // Get 인증이 필요한 페이지에 접근 시 호출 페이지
                .loginProcessingUrl("/auth/signin")
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()// code, access_token 받는 과정 생략하고 회원정보 바로 받을 수 있게
                .userService(oAuth2DetailsService);
        http.headers().frameOptions().sameOrigin();
        return http.build();
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //super 삭제 -> 기존 시큐리티가 가지고 있는 기능이 모두 비활성화
//        http.csrf().disable();
//        http.authorizeRequests()
//                .antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/**")
//                .authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .loginPage("/auth/signin") //Get 인증이 필요한 페이지에 접근 시 호출하는 페이지
//                .loginProcessingUrl("/auth/signin") //post로 이 주소가 요청되면 스프링 시큐리티가 요청을 낚아채서 로그인 대신 진행
//                .defaultSuccessUrl("/")
//                .and()
//                .oauth2Login()
//                .userInfoEndpoint()//코드, 엑세스 토큰 받는 과정을 생략하고 회원정보를 바로 받을 수 있다.
//                .userService(oAuth2DetailsService);
//    }

}
