package com.market.api.apple;

import com.market.crawling.data.CrawlingResultData;
import com.market.daemon.dao.MarketInfo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sun.security.ec.ECPrivateKeyImpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.interfaces.ECPrivateKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppleApi {

    public static final String issuer_Id = "69a6de70-3bc8-47e3-e053-5b8c7c11a4d1";
    public static final String keyId = "7JL62P566N";
    public static final String keyPath = "static/apple/AuthKey_7JL62P566N.p8";
    public static String appId = "357484932";

    public String getAppVersions(String jwt, String id) throws MalformedURLException {
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps"+"/"+ id +"/appStoreVersions"+"?limit=1"); // 버전 업데이트날짜
        return getConnectResult(jwt, id, url);
    }

    public String getAppTitle(String jwt, String id) throws MalformedURLException{
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/"+id); // 이름
        return getConnectResult(jwt, id, url);
    }

    public String getBuildInfo(String jwt, String id) throws MalformedURLException{
        URL url = new URL("https://api.appstoreconnect.apple.com/v1/apps/"+id+"/builds?limit=1"); // 이름
        return getConnectResult(jwt, id, url);
    }
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
            throw new RuntimeException("An Error Occurred. IO failed... " + e);
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
            throw new RuntimeException("Key Format is invalid!! " + e);
        }catch (JOSEException e)
        {
            throw new RuntimeException("JWT Transformation failed! "+e);
        }

        return jwt.serialize();

    }

    public Map<String, String> getCrawlingInfo(String id) throws MalformedURLException, ParseException {
        String jwt = createJWT();
        String appVersions = getAppVersions(jwt, id);

        JSONObject obj = parseStrToJson(appVersions);
        JSONArray data = (JSONArray)obj.get("data");
        JSONObject temp = (JSONObject) data.get(0);
        JSONObject attributes = (JSONObject)temp.get("attributes");

        Map<String, String> map = new HashMap<String, String>(attributes);

        String appTitle = getAppTitle(jwt, id);

        JSONObject obj2 = parseStrToJson(appTitle);
        JSONObject data1 = (JSONObject)obj2.get("data");
        JSONObject attributes1 = (JSONObject)data1.get("attributes");

        map.put("name", attributes1.get("name").toString());
        return map;
    }

    public CrawlingResultData getCrawlingResult(String id) throws MalformedURLException, ParseException {
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
            throw new RuntimeException("Private Key read Failed... " + e);
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
}
