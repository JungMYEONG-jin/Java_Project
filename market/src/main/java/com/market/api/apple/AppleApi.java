package com.market.api.apple;

import com.market.crawling.ICrawling;
import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dto.SendInfo;
import com.market.exception.AppleAPIException;
import com.market.exception.JWTException;
import com.market.exception.KeyReadException;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import sun.security.ec.ECPrivateKeyImpl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppleApi implements ICrawling {

    public static final String issuer_Id = "69a6de70-3bc8-47e3-e053-5b8c7c11a4d1";
    public static final String keyId = "7JL62P566N";
    public static final String keyPath = "static/apple/AuthKey_7JL62P566N.p8";


    private int CONN_TIME_OUT = 1000 * 30;
    public String getAppVersions(String jwt, String id) throws NoSuchAlgorithmException, MalformedURLException {
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps"+"/"+ id +"/appStoreVersions"+"?limit=1"); // 버전 업데이트날짜
        return getConnectResultByX509(jwt, id, url);
    }

    public String getAppTitle(String jwt, String id) throws NoSuchAlgorithmException, MalformedURLException {
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/"+id); // 이름
        return getConnectResultByX509(jwt, id, url);
    }

    public String getReviewDetails(String jwt, String id) throws NoSuchAlgorithmException, MalformedURLException{
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/appStoreReviewDetails/"+id);
        return getConnectResultByX509(jwt, id, url);
    }

    private String getConnectResultByX509(String jwt, String id, URL url) throws NoSuchAlgorithmException {

        String result = "";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        SSLContext sslContext = SSLContext.getInstance("SSL");

        try {
            X509TrustManager trustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // LOGGER.debug("checkClientTrusted");

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                    // LOGGER.debug("checkServerTrusted");
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    // LOGGER.debug("getAcceptedIssuers");

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
                http.setHeader("Authorization", "Bearer "+ jwt);
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
            throw new AppleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;


    }


    public String createJWT( )
    {
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.ES256).keyID(keyId).type(JOSEObjectType.JWT).build();

        JWTClaimsSet claimsSet = new JWTClaimsSet();
        Date now = new Date();
        claimsSet.setIssuer(issuer_Id);
        claimsSet.setIssueTime(now);
        claimsSet.setExpirationTime(new Date(now.getTime()+900000)); // exp 15 minutes
        claimsSet.setAudience("appstoreconnect-v1");
//        claimsSet.setClaim("scope", "GET /v1/apps/"+appId+"/appInfos");

        SignedJWT jwt = new SignedJWT(header,claimsSet);

        try{
            ECPrivateKey ecPrivateKey = new ECPrivateKeyImpl(readPrivateKey(keyPath));
            JWSSigner jwsSigner = new ECDSASigner(ecPrivateKey.getS());
            jwt.sign(jwsSigner);

        }catch(InvalidKeyException e)
        {
            throw new KeyReadException("JWT Private Key read Failed... " + e);
        }catch (JOSEException e)
        {
            throw new JWTException("JWT Transformation failed! "+e);
        }

        return jwt.serialize();

    }

    public Map<String, String> getCrawlingInfo(String id) throws MalformedURLException, NoSuchAlgorithmException, ParseException {
        String jwt = createJWT();
        String appVersions = getAppVersions(jwt, id);

        JSONObject obj = parseStrToJson(appVersions);
        JSONArray data = (JSONArray)obj.get("data");
        JSONObject temp = (JSONObject) data.get(0);
        JSONObject attributes = (JSONObject)temp.get("attributes"); // 버전 업데이트일

        Map<String, String> map = new HashMap<String, String>(attributes);

        String appTitle = getAppTitle(jwt, id);

        JSONObject obj2 = parseStrToJson(appTitle);
        JSONObject data1 = (JSONObject)obj2.get("data");
        JSONObject nameAttributes = (JSONObject)data1.get("attributes"); // 이름

        map.put("name", nameAttributes.get("name").toString());
        return map;
    }

    /**
     * ssl 인증 가능한 곳에서 해당 메소드 사용
     * @param id
     * @return
     * @throws MalformedURLException
     * @throws ParseException
     * @throws NoSuchAlgorithmException
     */
    public CrawlingResultData getCrawlingResult(String id) throws MalformedURLException, NoSuchAlgorithmException, ParseException {
            Map<String, String> crawlingInfo = getCrawlingInfo(id);
            String realAppID = getRealAppID(id);
            return new CrawlingResultData(realAppID, id, crawlingInfo.get("name"), crawlingInfo.get("versionString"), crawlingInfo.get("createdDate"));
//        return new CrawlingResultData(id, MarketInfo.OS_TYPE_IOS_API, crawlingInfo.get("name"), crawlingInfo.get("versionString"), crawlingInfo.get("createdDate"));
    }



    private byte[] readPrivateKey(String keyPath)
    {
        Resource resource = new ClassPathResource(keyPath);

        byte[] content = null;
        try
        {
            InputStream inputStream = resource.getInputStream();
//            FileReader keyReader = new FileReader(resource.getFile());
//            FileReader keyReader = new FileReader(inputStream);
            PemReader pemReader = new PemReader(new InputStreamReader(inputStream)); // jar 배포시 getFile은 에러 발생 가능성 높음. inputstream으로 읽어오기
            PemObject pemObject = pemReader.readPemObject();
            content = pemObject.getContent();

        }catch(IOException e)
        {
            throw new KeyReadException("Private Key read Failed... " + e);
        }
        return content;
    }


    private JSONObject parseStrToJson(String str) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(str);
        return obj;
    }

    private String getRealAppID(String appPkg){
        for(AppleAppId value : AppleAppId.values()){
            if(appPkg.equals(value.getAppPkg()))
                return value.toString();
        }
        return "존재하지 않는 패키지입니다.";
    }

    public CrawlingResultData crawling(SendInfo sendInfo){
        try {
            return getCrawlingResult(sendInfo.getAppPkg());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
