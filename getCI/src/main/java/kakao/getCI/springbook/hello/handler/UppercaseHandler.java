package kakao.getCI.springbook.hello.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    Object target;


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(target, args);
        if(result instanceof String && method.getName().startsWith("say"))
        {
            return ((String) result).toUpperCase();
        }else
        {
            return result;
        }
    }
}
