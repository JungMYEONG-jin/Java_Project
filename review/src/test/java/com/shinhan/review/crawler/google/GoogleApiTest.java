package com.shinhan.review.crawler.google;

import com.shinhan.review.exception.GooleAPIException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleApiTest {

    GoogleApi controller = new GoogleApi();

    @Test
    void review() throws MalformedURLException {
        List<JSONObject> reviewList = controller.getReviewList(GoogleAppId.O2O.getAppPkg());
        int cnt = 0;
        for (JSONObject jsonObject : reviewList) {
            System.out.println("jsonObject = " + jsonObject.toJSONString());
            cnt++;
        }
        System.out.println("cnt = " + cnt);
    }

    @Test
    void getAccessCode() throws MalformedURLException, NoSuchAlgorithmException {
        int CONN_TIME_OUT = 1000 * 30;
        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");

        URL url = new URL("https://www.googleapis.com/oauth2/v4/token");

        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { trustManager },
                    new SecureRandom());
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext,
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            Scheme sch = new Scheme("https", 443, socketFactory);
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);

            HttpParams httpParam = httpClient.getParams();
            org.apache.http.params.HttpConnectionParams.setConnectionTimeout(httpParam, CONN_TIME_OUT);
            org.apache.http.params.HttpConnectionParams.setSoTimeout(httpParam, CONN_TIME_OUT);
            HttpRequestBase http = null;
            try {
                HttpPost httpPost = new HttpPost(url.toURI());
                MultipartEntity multipartEntity = new MultipartEntity();
                // set string body
                StringBody grantBody = new StringBody("refresh_token");
                StringBody tokenBdoy = new StringBody("1//0eHh6SNkcC1SPCgYIARAAGA4SNwF-L9IrO20uMiHCN2C-TL1xpKOl-J2-4RHi9VUaJ98jdnzfthNDFo5sbvzLA2cblzJNxQl1Le4");
                StringBody idBody = new StringBody("118014375029-3lfevtf7okr9mqrn7l8p41p4g3dus4ah.apps.googleusercontent.com");
                StringBody secretBody = new StringBody("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");
                StringBody redirectBody = new StringBody("https://localhost:8080/login/oauth2/code");

                // set parameter
                multipartEntity.addPart("refresh_token", tokenBdoy);
                multipartEntity.addPart("client_id", idBody);
                multipartEntity.addPart("client_secret", secretBody);
                multipartEntity.addPart("redirect_uri", redirectBody);
                multipartEntity.addPart("grant_type", grantBody);
                // set form data
                httpPost.setEntity(multipartEntity);
                http = httpPost;
            } catch (Exception e) {
                System.out.println(" error " );
                http = new HttpPost(url.toURI());
            }

            HttpResponse response = null;
            HttpEntity entity = null;
            HttpRequest request = null;
            String responseBody = null;
            /**
             * ??? ?? OUTPUT
             */
            // Time Out
            response = httpClient.execute(http);
            entity = response.getEntity();
            responseBody = EntityUtils.toString(entity, "UTF-8");
            result = responseBody; // json 형식
            System.out.println("responseBody = " + result);

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
    }

}