package sol.com.shinhan.superplatform.filter;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sol.com.shinhan.superplatform.util.SUPSecureUtil;
import sol.com.shinhan.superplatform.util.SUPStringUtil;
import sun.misc.BASE64Encoder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class SupFilterSessionCheck implements Filter {

    Logger logger = LoggerFactory.getLogger(SupFilterSessionCheck.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
        logger.info("init ----- SUPFilter");
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

                if(hashData == null)
                {
                    isBodyCheck = false;
                }

                logger.info("doFilter hashData : [ "+hashData+" ], hashBodyData : ["+hashBodyData+"]");
                if(!hashData.equals(hashBodyData))
                {
                    isBodyCheck = false;
                }
                logger.info("isBodyCheck : [ "+isBodyCheck+" ], req.getRequestURI : ["+req.getRequestURI()+"]");

                if(isBodyCheck){
                    chain.doFilter(requestWrapper, res);
                }else{
                    OutputStream os = null;

                    try{
                        JSONObject dataObj = new JSONObject();
                        JSONObject headObj = new JSONObject();
                        JSONObject bodyObj = new JSONObject();

                        headObj.put("result", "FAIL");
                        headObj.put("resultCode", "8000");
                        headObj.put("resultMSG", "CHECK DATA FAIL");
                        headObj.put("resultDetail", "");


                        dataObj.put("header", headObj);
                        dataObj.put("body", bodyObj);

                        os = response.getOutputStream();
                        os.write(dataObj.toString().getBytes());

                        os.flush();
                        os = null;

                    }catch(Exception e){
                        logger.info("doFilter Warning Exception: "+e);
                    }finally {
                        if(os!=null){
                            os.close();
                            os = null;
                        }
                    }
                    return;



                }



            }else{
                chain.doFilter(req, res);
            }

        }catch (UnsupportedEncodingException e){
            logger.info("doFilter UnsupportedEncodingException : "+e);
        }catch (IOException e){
            logger.info("doFilter IOException: "+e);
        }catch(ServletException e){
            logger.info("doFilter  ServletException: "+e);
        }catch (Exception e){
            logger.info("doFilter  Exception: "+e);
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
