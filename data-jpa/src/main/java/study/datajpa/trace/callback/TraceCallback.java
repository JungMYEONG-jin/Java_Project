package study.datajpa.trace.callback;

public interface TraceCallback<T> {

    T call();
}
