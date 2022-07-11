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
        Map<String, String> map = appleService.getCrawlingInfo(id);
        model.addAttribute("map", map); // version name ceratedDate
        return "apple/appinfo";
    }





    @GetMapping("/apple/builds/{id}")
    public String getAppBuildInfo(@PathVariable String id, Model model) throws MalformedURLException {
        String jwt = appleService.createJWT();
        String buildInfo = appleService.getBuildInfo(jwt, id);
        System.out.println("buildInfo = " + buildInfo);
        model.addAttribute("build", buildInfo);
        return "apple/build";
    }


}
