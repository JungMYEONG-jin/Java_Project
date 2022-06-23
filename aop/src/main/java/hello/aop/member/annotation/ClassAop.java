package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE) // aop에는 type이란게 필요함
@Retention(RetentionPolicy.RUNTIME) // 실제 실행할때까지 annotation 생존
public @interface ClassAop {
}
