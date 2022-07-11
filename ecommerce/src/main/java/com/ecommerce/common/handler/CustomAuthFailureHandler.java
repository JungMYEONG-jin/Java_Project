package com.ecommerce.common.handler;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@Component
public class CustomAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String msg = "";
        if (exception instanceof BadCredentialsException){
            msg = "비밀번호나 패스워드가 일치하지 않습니다.";
        }else if (exception instanceof InternalAuthenticationServiceException){
            msg = "내부 문제가 발생했습니다. 관리자에게 문의하세요.";
        }else if (exception instanceof UsernameNotFoundException){
            msg = "계정이 존재하지 않습니다. 회원가입을 진행해주세요.";
        }else if (exception instanceof AuthenticationCredentialsNotFoundException){
            msg = "알 수 없는 이유로 로그인에 실패하였습니다. 관리자에게 문의하세요.";
        }
        msg = URLEncoder.encode(msg, "UTF-8");
        setDefaultFailureUrl("/login?error=true&exception="+ msg);
        super.onAuthenticationFailure(request, response, exception);
    }
}
