package kakao.getCI.apple;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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



    @GetMapping("/apple")
    public String getAppInfo(Model model) throws MalformedURLException {
        System.out.println(" = " );
        String jwt = new AppleService().createJWT();
        String appInfos = new AppleService().getAppInfos(jwt);
        // json parsing...

        System.out.println("appInfos = " + appInfos);
        return "apple/appinfo";
    }



}
