package study.datajpa.trace.template;

import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

public abstract class AbstractTemplate<T>{

    private final LogTrace trace;

    public AbstractTemplate(LogTrace trace) {
        this.trace = trace;
    }

    public T execute(String message){
        TraceStatus status = null;
        try{
            status = trace.begin(message);

            T result = call();
            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }

    // 해당 부분을 이제 구현하게끔 만들면됨.
    protected abstract T call();
}
