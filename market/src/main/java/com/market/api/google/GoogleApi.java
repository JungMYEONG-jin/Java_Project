package com.market.api.google;


import com.market.crawling.ICrawling;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dto.SendInfo;
import com.market.exception.GooleAPIException;
import com.market.exception.KeyReadException;
import com.market.util.DateUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GoogleApi implements ICrawling {

    private static final String keyPath = "static/google/client_secret.json";
    @Value("${refresh_token}")
    private String refresh_token;
    private static final int CONN_TIME_OUT = 1000 * 30;
    // 권한 획득 범위 https://www.googleapis.com/auth/androidpublisher

    public Map<String, String> getClientInfo(){
        JSONObject attr = (JSONObject) readJson(keyPath).get("web");
        JSONArray redirect_uris = (JSONArray) attr.get("redirect_uris");
        attr.remove("redirect_uris");
        attr.put("redirect_uri", redirect_uris.get(0).toString());
        return new HashMap<String, String>(attr);
    }

    public String getAccessToken(){
        Map<String, String> clientInfo = getClientInfo(); // client json parsing
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/oauth2/v4/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String result = null;
        System.out.println("refresh_token = " + refresh_token);
        try {
            result = getAccessTokenX509Post(url, refresh_token, clientInfo.get("client_id"), clientInfo.get("client_secret"), clientInfo.get("redirect_uri"));
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        String resToken = "";
        try {
            JSONObject parse = (JSONObject)parser.parse(result);
            resToken = parse.get("access_token").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return resToken;
    }

    private String getEditID(String packageName, String token) {
        try {
            String editID =  postEditID(packageName, token);
            JSONParser parser = new JSONParser();
            JSONObject editJSON = (JSONObject) parser.parse(editID);
            if (editJSON.containsKey("id"))
            {
                return editJSON.get("id").toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    private List<JSONObject> commonJsonTask(String link) throws MalformedURLException {
        URL url = null;
        String reviewDetails = "";
        String nextToken = "";
        List<JSONObject> res = new ArrayList<JSONObject>();
        try {
            url = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            reviewDetails = getConnectResultByX509(url);
            JSONParser parser = new JSONParser();
            JSONObject parseResult = (JSONObject) parser.parse(reviewDetails);
            res.add(parseResult);
            JSONObject next = null;
            nextToken = getNextToken(parseResult, next, nextToken);
            System.out.println("nextToken = " + nextToken);
            while(nextToken!=null) {
                reviewDetails = getConnectResultByX509(new URL(link +"&token="+nextToken));
                parseResult = (JSONObject) parser.parse(reviewDetails);
                res.add(parseResult);

                next = null;
                nextToken = null;
                nextToken = getNextToken(parseResult, next, nextToken);
                if (nextToken==null)
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;
    }

    private String getNextToken(JSONObject parseResult, JSONObject next, String nextToken) {
        if(parseResult.containsKey("tokenPagination"))
            next = (JSONObject) parseResult.get("tokenPagination");
        if (next !=null && next.containsKey("nextPageToken"))
            nextToken = next.get("nextPageToken").toString();
        return nextToken;
    }

    private String getAccessTokenX509Post(URL url, String token, String clientId, String clientSecret, String redirectURI) throws NoSuchAlgorithmException {

        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");
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
                StringBody grantBody = new StringBody("refresh_token");
                StringBody tokenBdoy = new StringBody(refresh_token);
                StringBody idBody = new StringBody(clientId);
                StringBody secretBody = new StringBody(clientSecret);
                StringBody redirectBody = new StringBody(redirectURI);
                multipartEntity.addPart("refresh_token", tokenBdoy);
                multipartEntity.addPart("client_id", idBody);
                multipartEntity.addPart("client_secret", secretBody);
                multipartEntity.addPart("redirect_uri", redirectBody);
                multipartEntity.addPart("grant_type", grantBody);
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

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    private String postEditID(String packageName, String token) throws NoSuchAlgorithmException, MalformedURLException {

        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");
        URL url = new URL("https://androidpublisher.googleapis.com/androidpublisher/v3/applications/"+packageName+"/edits");
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
                http = new HttpPost(url.toURI());
                http.setHeader("Authorization", "Bearer "+token);
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

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    private String getConnectResultByX509(URL url) throws NoSuchAlgorithmException {
        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");
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
                http = new HttpGet(url.toURI());
            } catch (Exception e) {
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

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    private String getConnectResultByX509(URL url, String token) throws NoSuchAlgorithmException {
        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");
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
                http = new HttpGet(url.toURI());
                http.setHeader("Authorization", "Bearer "+token);
            } catch (Exception e) {
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

        } catch (Exception e) {
            throw new GooleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    private JSONObject readJson(String keyPath)
    {
        InputStream inputStream = null;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(keyPath); // in native read without spring core
        JSONObject content = null;
        try
        {
            JSONParser parser = new JSONParser();
            content = (JSONObject)parser.parse(new InputStreamReader(inputStream)); // jar 배포시 getFile은 에러 발생 가능성 높음. inputstream으로 읽어오기
        }catch(IOException e)
        {
            throw new KeyReadException("Private Key read Failed... " + e);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    public CrawlingResultData crawling(SendInfo sendInfo) {
        String accessToken = getAccessToken();
        String packageName = sendInfo.getAppPkg();
        String editId = getEditID(packageName, accessToken);
        String appVer = "";
        String title = "";
        try {
            URL url = new URL("https://androidpublisher.googleapis.com/androidpublisher/v3/applications/"+packageName+"/edits/"+editId+"/listings");
            String appDesc = getConnectResultByX509(url, accessToken);
            url = new URL("https://androidpublisher.googleapis.com/androidpublisher/v3/applications/"+packageName+"/edits/"+editId+"/tracks/production");
            String version = getConnectResultByX509(url, accessToken);

            JSONParser parser = new JSONParser();
            JSONObject parse = (JSONObject)parser.parse(version);
            JSONArray releases = (JSONArray)parse.get("releases");
            for (Object o : releases) {
                JSONObject obj = (JSONObject) o;
                if (obj.containsKey("name"))
                {
                    String name = obj.get("name").toString();
                    if (name.contains("(") && name.contains(")")) {
                        int startIdx = name.indexOf('(');
                        int lastIdx = name.lastIndexOf(')');
                        LoggerFactory.getLogger("mjError").error("{} {} {}", name, startIdx, lastIdx);
                        appVer = name.substring(startIdx + 1, lastIdx);
                    }else
                        appVer = name;
                    LoggerFactory.getLogger("mjError").error(appVer);
                    break;
                }
            }
            parse.clear();
            releases.clear();

            parse = (JSONObject) parser.parse(appDesc);
            releases = (JSONArray)parse.get("listings");
            for (Object release : releases) {
                JSONObject obj = (JSONObject) release;
                if(obj.containsKey("title")){
                    title = obj.get("title").toString();
                    break;
                }
            }
            return new CrawlingResultData(sendInfo.getAppId(), sendInfo.getAppPkg(), title, appVer, new DateUtil().getNow());

        } catch (MalformedURLException e) {
            throw new GooleAPIException("잘못된 주소 입니다.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new GooleAPIException("해당 알고리즘이 존재하지 않습니다.", e);
        } catch (ParseException e) {
            throw new GooleAPIException("JSON Parsing중 에러가 발생했습니다. 키값을 확인해주세요...", e);
        }
    }
}
