package study.datajpa.proxy.app.v2;

import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2{

    private final OrderControllerV2 target;
    private final LogTrace trace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace trace) {
        super(null);
        this.target = target;
        this.trace = trace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderController.request()");
            String result = target.request(itemId);
            trace.end(status);
            return result;
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }

    @Override
    public String noLog() {
        return super.noLog();
    }
}
