package hello.hellospring.kakao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/member")
public class KaKaoController {

    @Autowired
    KaKaoService kService;


    @GetMapping("/kakao")
    public String kakaoLogin(@RequestParam(required = false) String code) throws Exception
    {
        System.out.println("code = " + code);
        String access_token = kService.getToken(code);
        String logId = kService.logout(access_token);
        //kService.getUserInfo(access_token);

        return "kakao/test";
    }




}
