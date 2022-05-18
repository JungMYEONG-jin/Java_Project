package sol.com.shinhan.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

// Filter는 request와 response에 대한 정보들을 변경할 수 있게 개발자들에게 제공하는 서블릿 컨테이너이다.
// FilterChain은 이러한 Filter가 여러개 모인것이며 순서가 있다.


public class SupFilter implements Filter {

    Logger logger = LoggerFactory.getLogger(SupFilter.class);

    // 체인을 따라 계속 다음에 존재하는 필터로 이동하는 것
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try{
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            chain.doFilter(req, res);
        }catch (UnsupportedEncodingException e)
        {
            logger.info("UnsupportedEncodingException IP: "+request.getRemoteAddr()+" Error: "+e.getMessage());
        }catch (IOException e)
        {
            logger.info("IOException IP: "+request.getRemoteAddr()+" Error: "+e.getMessage());
        }catch (Exception e)
        {
            logger.info("Exception IP: "+request.getRemoteAddr()+" Error: "+e.getMessage());
        }
    }
}
