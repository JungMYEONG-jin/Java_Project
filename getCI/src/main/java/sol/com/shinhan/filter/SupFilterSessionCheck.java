package sol.com.shinhan.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SupFilterSessionCheck implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try{
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            req.setCharacterEncoding("utf-8");

            boolean isBodyCheck = true;

            isBodyCheck = true;

            if(req.getRequestURI().equals("/api/sp/test/bodyCheck.sp"))
            {
                SupHttpRequestWrapper requestWrapper = new SupHttpRequestWrapper(req);

                //hash check
            }

        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
