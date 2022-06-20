package hello.aop.member;

import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService{
    @Override
    @MethodAop
    public String hello(String param) {
        return "OK";
    }

    public String internal(String param){
        return "OK";
    }


}
