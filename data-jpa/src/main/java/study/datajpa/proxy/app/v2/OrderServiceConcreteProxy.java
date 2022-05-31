package study.datajpa.proxy.app.v2;

import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2{

    private final OrderServiceV2 orderService;
    private final LogTrace trace;

    public OrderServiceConcreteProxy(OrderServiceV2 orderService, LogTrace trace) {
        super(null);
        this.orderService = orderService;
        this.trace = trace;
    }




    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderService.orderItem()");
            orderService.orderItem(itemId);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
