# Google Play Developer API 가이드

Google Play Developer API는 API key를 이용하는 방식과 OAuth 인증을 통해 권한을 획득하는 방식이 있습니다.
본 가이드라인은 OAuth 인증을 이용한 샘플입니다.
1. 사용자는 인증 코드를 획득합니다.
2. 인증 코드를 다시 google에 보내 유효한지 검증받습니다.
3. 유효한 사용자라면 google에서 redirect_uri에 code를 보내줍니다.
4. 해당 코드를 가지고 다시 google에 보내 access_token과 refresh_token을 획득합니다.
5. refresh_token을 얻게 되면 위와 같은 인증 과정을 거치지 않고 access_token을 계속 획득 가능합니다.
6. token을 api 요청시 보내 인증을 받고 값을 return 받으면 완료입니다.

사용자는 인증 코드를 획득하고 그 인증코드를 다시 보내 구글에서는 그 코드를 가지고 해당 유저의 정보가 유효한지 확인합니다. 그리고 유효하다면 redirect_uri뒤에 code=값 형태로 return 하게 됩니다.
인증 코드를 획득하려면 해당 주소에 parameter를 채워 GET 호출을 해야합니다. 만약 자체적으로 운영중인 로그인 페이지가 있다면 redirect_uri를 저에게 회신해주시면 계정 생성시 해당 uri를 넣어드리겠습니다.
scopee는 Google의 여러 API중 저희는 play store와 관련된 API만 호출할 것이므로 해당 scope{scope=https://www.googleapis.com/auth/androidpublisher}를 그대로 사용하시면 됩니다.

https://accounts.google.com/o/oauth2/auth?scope=https://www.googleapis.com/auth/androidpublisher&response_type=code&access_type=offline&redirect_uri={redirect_uri}&client_id={client_id}

GET 하는 방법은 java code로 구현해도 되고 PostMan을 이용해 획득해도 됩니다. 단 java code를 이용해 구현하려면 redirect_uri 페이지를 생성하고 연결해줘야 합니다.
이 과정이 귀찮다면 인터넷 PC에서 PostMan을 이용해 token을 얻었습니다. (refresh_token 한번 얻으면 6개월간 위 과정 없이 토큰 생성 가능.)

위 주소에 client_json의 값을 채워서 GET 하면 아래와 같이 본인에게 맞는 Code가 생성됩니다.
제 경우 4/0AdQt8qhBtjt-Q4UWzEP2XH1GSnQx0aQm4fm5mTqAOUC-IsNBppln_rnrYj847zRbv6XP6A 입니다.

이제 이 code를 이용해 access_token을 얻어야 합니다.

```shell
POST: https://oauth2.googleapis.com/token
form-data
redirect_uri: ${redirect_uri}
code: 아까 얻은 code 저의 경우 4/0AdQt8qhBtjt-Q4UWzEP2XH1GSnQx0aQm4fm5mTqAOUC-IsNBppln_rnrYj847zRbv6XP6A
client_id: ${client_id}
client_secret: ${client_secret}
grant_type: authorization_code
```
성공시 아래와 같은 결과를 얻습니다.

```json
{
    "access_token": "~~~~~~~~~~~",
    "expires_in": 3599,
    "refresh_token": "~~~~~~~~~",
    "scope": "https://www.googleapis.com/auth/androidpublisher",
    "token_type": "Bearer"
}
```
이제 이 refresh_token을 저장하고 간직하면 위 과정을 거치지 않고 access_token 생성이 가능합니다.

```shell
POST: https://www.googleapis.com/oauth2/v4/token
form-data
redirect_uri: ${redirect_uri}
refresh_token: ${refresh_token}
client_id: ${client_id}
client_secret: ${client_secret}
grant_type: refresh_token
```

```json
{
    "access_token": "ya29.a0AVA9y1v7oTDTa2mBSCkAtaJDo_sDMGMMetcDDLJ8ihRnowSvOq9JdZCCRjj1wmWzFeIUhwGccVScIMDuiFpQV95J66es9JdGIHxnt9TdVrwIMXOxXj2YDQ3svuih_fyFrYdfDuu_W03dMrGf6NCwoCjPb6fZaCgYKATASAQASFQE65dr8w8YAR7TckN-IS4NtYQ00FA0163",
    "expires_in": 3599,
    "scope": "https://www.googleapis.com/auth/androidpublisher",
    "token_type": "Bearer"
}
```

##sample code
```java
package crawler;

public class Sample {

    private static final String keyPath = "my.json";
    private static final String refresh_token = "my_value";
    private static final int CONN_TIME_OUT = 1000 * 30;
    // 권한 획득 범위 https://www.googleapis.com/auth/androidpublisher

    public Map<String, String> getClientInfo(){
        JSONObject attr = (JSONObject) readJson(keyPath).get("web");
        JSONArray redirect_uris = (JSONArray) attr.get("redirect_uris");
        attr.remove("redirect_uris");
        attr.put("redirect_uri", redirect_uris.get(0).toString());
        return new HashMap<String, String>(attr);
    }

    /**
     * refresh token을 이용해 access token 발급
     * @return
     */
    public String getAccessToken(){
        Map<String, String> clientInfo = getClientInfo(); // client json parsing
        URL url = null;
        try {
            url = new URL("https://www.googleapis.com/oauth2/v4/token");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String result = null;
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



    public List<JSONObject> getReviewDetails(String packageName, String token) throws MalformedURLException {
        String link = "https://www.googleapis.com/androidpublisher/v3/applications/"+packageName+"/reviews?access_token="+token;
        List<JSONObject> res = commonJsonTask(link);
        return res;
    }


    private List<JSONObject> commonJsonTask(String link) throws MalformedURLException {
        URL url = null;
        String reviewDetails = "";
        String nextToken = "";
        List<JSONObject> res = new ArrayList<>();
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
            while(!nextToken.isEmpty()) {
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

    /**
     * 리뷰 크롤링을 하다보면 최대 100개까지 나오고 나머지는 다음 페이지에서 가져와야함.
     * 다음 페이지 리뷰를 호출하려면 nextPageToken을 얻어야 함.
     * 해당 기능 구현
     * @param parseResult
     * @param next
     * @param nextToken
     * @return
     */
    private String getNextToken(JSONObject parseResult, JSONObject next, String nextToken) {
        if(parseResult.containsKey("tokenPagination"))
            next = (JSONObject) parseResult.get("tokenPagination");
        if (next !=null && next.containsKey("nextPageToken"))
            nextToken = next.get("nextPageToken").toString();
        return nextToken;
    }

    /**
     * refresh token으로 access token 생성하기
     * @param url
     * @param token
     * @param clientId
     * @param clientSecret
     * @param redirectURI
     * @return
     * @throws NoSuchAlgorithmException
     */
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
            throw new AppleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * api 호출
     * @param url
     * @return
     * @throws NoSuchAlgorithmException
     */
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
            throw new AppleAPIException(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }

    /**
     * client json 파일을 읽어오는 기능
     * @param keyPath
     * @return
     */
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

}

```