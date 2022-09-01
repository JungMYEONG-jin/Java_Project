package crawler.apple.api;


import crawler.dto.CrawlingResultData;
import crawler.exception.AppleAPIException;
import crawler.exception.JWTException;
import crawler.exception.KeyReadException;
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
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.util.Resources;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.interfaces.ECPrivateKey;
import java.util.*;


public class AppleApi {

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
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/"+id+"/customerReviews"+"?sort=createdDate&limit=200");
        return getConnectResultByX509(jwt, id, url);
    }

    /**
     * links에 next 키가 있다면 계속 크롤링하여 리뷰를 획득한다.
     * @param jwt
     * @param id
     * @return
     * @throws NoSuchAlgorithmException
     * @throws MalformedURLException
     */
    public String getNextReviews(String jwt, String id, String link) throws NoSuchAlgorithmException, MalformedURLException{
        URL url = new URL(link);
        return getConnectResultByX509(jwt, id, url);
    }


    /**
     * 리뷰를 전부 크롤링 하는 함수
     * @param jwt
     * @param id
     * @return
     * @throws MalformedURLException
     * @throws NoSuchAlgorithmException
     */
    public List<JSONObject> getAllReviews(String jwt, String id) throws MalformedURLException, NoSuchAlgorithmException {

        boolean isNext = false;
        List<JSONObject> result = new ArrayList<JSONObject>();
        String reviewDetails = getReviewDetails(jwt, id);
        result.addAll(getReviewList(reviewDetails));

        // 끝까지 작업 시작
        String nextURL = getNextURL(reviewDetails);
        while (nextURL!=null){
            String nextReviews = getNextReviews(jwt, id, nextURL);
            result.addAll(getReviewList(nextReviews)); // 계속 넣기
            nextURL = getNextURL(nextReviews);
            if (nextURL==null)
                break;
        }

        return result;
    }

    // for test
    public String getReviewSubmissions(String jwt, String id) throws NoSuchAlgorithmException, MalformedURLException{
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/"+id+"/reviewSubmissions?include=appStoreVersionForReview");
        return getConnectResultByX509(jwt, id, url);
    }

    // for test
    public String getReviewInfo(String jwt, String id) throws NoSuchAlgorithmException, MalformedURLException{
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/357484932/customerReviews?cursor=AMg.ANcXDEE&limit=200&sort=createdDate");
        return getConnectResultByX509(jwt, id, url);
    }

    // for test
    public String getBuildInfo(String jwt, String id) throws MalformedURLException{
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/"+id+"/builds?limit=1"); // 이름
        return getConnectResult(jwt, id, url);
    }

    /**
     * 테더링 사용할때 사용 가능
     * @param jwt
     * @param id
     * @param url
     * @return
     * @throws MalformedURLException
     */
    private String getConnectResult(String jwt, String id, URL url) throws MalformedURLException {
        String result = "";
        try{
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+ jwt);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String res = "";
            while((line=br.readLine())!=null)
            {
                res+=line;
            }

            result = res;
            urlConnection.disconnect();

        } catch (IOException e) {
            throw new AppleAPIException(e);
        }
        return result;
    }


    /**
     * 테더링 없이 사용 가능.
     * @param jwt
     * @param id
     * @param url
     * @return
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * jwt 인증 토큰 생성
     * @return
     */
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
        InputStream inputStream = null;
        inputStream = this.getClass().getClassLoader().getResourceAsStream(keyPath); // in native read without spring core
        byte[] content = null;
        try
        {
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

    private List<JSONObject> getReviewList(String reviewDetails){
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject) parser.parse(reviewDetails);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray data = (JSONArray)obj.get("data");
        List<JSONObject> result = new ArrayList<JSONObject>();
        for (Object datum : data) {
            JSONObject temp = (JSONObject) datum;
            result.add((JSONObject) temp.get("attributes"));
        }
        return result;
    }

    private String getNextURL(String reviewDetails){
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            obj = (JSONObject) parser.parse(reviewDetails);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONObject data = (JSONObject)obj.get("links");
        if(data.containsKey("next"))
            return data.get("next").toString();
        return null;
    }
}
