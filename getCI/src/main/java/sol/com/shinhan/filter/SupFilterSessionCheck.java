package sol.com.shinhan.filter;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sol.com.shinhan.superplatform.util.SUPSecureUtil;
import sol.com.shinhan.superplatform.util.SUPStringUtil;
import sun.misc.BASE64Encoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SupFilterSessionCheck implements Filter {

    Logger logger = LoggerFactory.getLogger(SupFilterSessionCheck.class);
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
                String hashData = SUPStringUtil.null2StrTrim(req.getHeader("body-vdata"));

                String bodyData = IOUtils.toString(req.getInputStream(), "UTF-8");

                logger.info("doFilter bodyData : "+bodyData);

                byte[] hash = SUPSecureUtil.hashSHA256(bodyData);

                BASE64Encoder base64Encoder = new BASE64Encoder();
                String hashBodyData = new String(base64Encoder.encode(hash));
            }

        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
