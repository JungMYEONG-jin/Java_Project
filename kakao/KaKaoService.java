package hello.hellospring.kakao;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.catalina.connector.InputBuffer;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

@Service
public class KaKaoService {

    public String getToken(String code)
    {
        String access_token = "";
        String refresh_token = "";
        String host = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(); // connect

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=2aad40910868e3c5fa9594f8de34a07b");
            sb.append("&redirect_uri=http://localhost:8080/member/kakao");
            sb.append("&code="+code);

            bw.write(sb.toString());
            bw.flush();

            int response_code = urlConnection.getResponseCode();
            System.out.println("response_code = " + response_code);

            // 응답 읽기

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line="";
            String result="";

            while((line=br.readLine())!=null)
            {
                result+=line;
            }
            System.out.println("result = " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_token = element.getAsJsonObject().get("refresh_token").getAsString();
            br.close();
            bw.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_token;
    }


    public HashMap<String, Object> getUserInfo(String access_token)
    {
        String host = "https://kapi.kakao.com/v2/user/me";
        HashMap<String, Object> userInfo = new HashMap<>();
        try{
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
//            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);
            int responseCode = urlConnection.getResponseCode();

            System.out.println("responseCode = " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result= "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            System.out.println("result = " + result);

            JsonParser parser = new JsonParser();
            JsonElement elem = parser.parse(result);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }


    public String logout(String access_token)
    {
        String host = "https://kapi.kakao.com/v1/user/logout";
        String res="";
        try{
            URL url = new URL(host);

            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
//            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);
            int responseCode = urlConnection.getResponseCode();

            System.out.println("responseCode = " + responseCode);
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result= "";
            while((line=br.readLine())!=null)
            {
                result+=line;
            }

            System.out.println("result = " + result);

            JsonParser parser = new JsonParser();
            JsonElement elem = parser.parse(result);
            String id = elem.getAsJsonObject().get("id").getAsString();
            System.out.println("id = " + id);
            res=id;


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }


}
