package sol.com.shinhan.util;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sol.com.shinhan.superplatform.util.SUPSecureUtil;
import sol.com.shinhan.superplatform.util.SUPStringUtil;

@Slf4j
public class SUPJsonUtil {

    /**
     * find dataHeader key
     * return String
     */
    public static String getJSONStringParserToHeaderString(String jsonString, String key){
        String result = "";

        JSONParser parser = null;
        JSONObject reqStrObj = null;

        try{
            parser = new JSONParser();
            reqStrObj = new JSONObject();

            // 줄 바꿈 및 문자변환
            jsonString = jsonString.replace(System.getProperty("line.separator"), "").replace("\n", "");
            jsonString = SUPSecureUtil.invalidXSS(jsonString);

            reqStrObj = (JSONObject) parser.parse(jsonString);
            JSONObject reqHeader = (JSONObject) reqStrObj.get(RES.JSON_DATA_HEADER);
            for (Object it : reqHeader.keySet()) {
                String tmpKey = it.toString();

                if(tmpKey.toLowerCase().equals(key.toLowerCase())){
                    result = reqHeader.get(tmpKey) + SUPStringUtil.EMPTY;
                    break;
                }
            }
        }catch (ParseException e){
            log.info("SUPJsonUtil getJSONStringParserToHeaderString : " + e);
            result = "";
        }finally {
            parser = null;
            reqStrObj = null;
        }

        return result;
    }


}
