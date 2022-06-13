package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {
    private final Object target;
    private final LogTrace trace;
    private final String[] patterns;

    public LogTraceFilterHandler(Object target, LogTrace trace, String[] patterns) {
        this.target = target;
        this.trace = trace;
        this.patterns = patterns;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

        String name = method.getName();

        if (!PatternMatchUtils.simpleMatch(patterns, name)){
            return method.invoke(target, objects);
        }


        TraceStatus status = null;
        try{

            String msg = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = trace.begin(msg);

            // logic call
            Object result = method.invoke(target, objects);
            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
