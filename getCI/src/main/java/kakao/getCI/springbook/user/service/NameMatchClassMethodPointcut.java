package kakao.getCI.springbook.user.service;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.util.PatternMatchUtils;

public class NameMatchClassMethodPointcut extends NameMatchMethodPointcut {

    public void setMapped(String mapped)
    {
        this.setClassFilter(new SimpleClassFilter(mapped));
    }

    static class SimpleClassFilter implements ClassFilter
    {
        String mapped;

        public SimpleClassFilter(String mapped) {
            this.mapped = mapped;
        }

        public boolean matches(Class<?> clazz)
        {
            return PatternMatchUtils.simpleMatch(mapped, clazz.getSimpleName());
        }
    }
}
