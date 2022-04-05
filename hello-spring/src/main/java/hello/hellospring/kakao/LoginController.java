package hello.hellospring.kakao;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @RequestMapping("/a")
    public @ResponseBody String getAuthUrl(HttpServletRequest request) throws Exception
    {

        return "https://kauth.kakao.com/oauth/authorize?client_id=2aad40910868e3c5fa9594f8de34a07b&redirect_uri=http://localhost:8080/member/kakao&response_type=code";
    }
}
