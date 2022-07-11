package kakao.getCI.apple;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/getInfo")
public class AppleController {

    @Autowired
    AppleService appleService;

    @GetMapping("/apple/{id}")
    public String getAppInfoByJsonParsing(@PathVariable String id, Model model) throws MalformedURLException, ParseException {
        String jwt = appleService.createJWT();
        String appVersions = appleService.getAppVersions(jwt, id);

        JSONObject obj = parseStrToJson(appVersions);
        JSONArray data = (JSONArray)obj.get("data");
        JSONObject temp = (JSONObject) data.get(0);
        JSONObject attributes = (JSONObject)temp.get("attributes");

        Map<String, String> map = new HashMap<>(attributes);

        String appTitle = appleService.getAppTitle(jwt, id);

        JSONObject obj2 = parseStrToJson(appTitle);
        JSONObject data1 = (JSONObject)obj2.get("data");
        JSONObject attributes1 = (JSONObject)data1.get("attributes");

        map.put("name", attributes1.get("name").toString());
        model.addAttribute("map", map); // version name ceratedDate

        return "apple/appinfo";
    }

    private JSONObject parseStrToJson(String str) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(str);
        return obj;
    }

}
