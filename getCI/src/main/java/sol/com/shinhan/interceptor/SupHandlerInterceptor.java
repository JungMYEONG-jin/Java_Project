package sol.com.shinhan.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

// HandlerInterceptorAdapter deprecated 됨

public class SupHandlerInterceptor implements HandlerInterceptor {

    Logger logger = LoggerFactory.getLogger(SupHandlerInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 컨트롤러 진입 직전에 실행
        try{
            request.setCharacterEncoding("utf-8");
        }catch (UnsupportedEncodingException e)
        {
            logger.info("Interceptor: UnsupportedEncodingException: "+e.getMessage());
        }catch (Exception e)
        {
            logger.info("Interceptor Exception: "+e.getMessage());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        // 컽르롤러 진입후 view 렌더링 전에 수행
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        // 모든 view 렌더링을 마치고 수행
    }
}
