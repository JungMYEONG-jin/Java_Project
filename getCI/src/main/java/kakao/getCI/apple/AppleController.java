package kakao.getCI.apple;


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


@Controller
@RequestMapping("/getInfo")
public class AppleController {


    @Autowired
    AppleService appleService;


    @GetMapping("/apple/{id}")
    public String getAppInfo(@PathVariable String id, Model model) throws MalformedURLException {
        String jwt = appleService.createJWT();
        String appVersions = appleService.getAppVersions(jwt, id);
        System.out.println("appVersions = " + appVersions);
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        String appTitle = appleService.getAppTitle(jwt, id);
        System.out.println("appTitle = " + appTitle);

        model.addAttribute("appVersion", appVersions);
        model.addAttribute("appTitle", appTitle);

        return "apple/appinfo";
    }



}
