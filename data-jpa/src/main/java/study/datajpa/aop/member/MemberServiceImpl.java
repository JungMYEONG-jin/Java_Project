package study.datajpa.aop.member;

import org.springframework.stereotype.Component;
import study.datajpa.aop.member.annotation.ClassAop;
import study.datajpa.aop.member.annotation.MethodAop;

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
