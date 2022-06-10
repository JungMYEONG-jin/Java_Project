package study.datajpa.config.v2_dynamicproxy.handler;

import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceBasicHandler implements InvocationHandler {

    private final Object target;
    private final LogTrace trace;

    public LogTraceBasicHandler(Object target, LogTrace trace) {
        this.target = target;
        this.trace = trace;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;
        try{
            String msg = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
            status = trace.begin(msg);
            Object result = method.invoke(target, trace);
            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
