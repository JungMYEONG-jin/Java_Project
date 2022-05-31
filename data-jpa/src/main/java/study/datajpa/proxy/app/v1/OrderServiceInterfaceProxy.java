package study.datajpa.proxy.app.v1;

import lombok.RequiredArgsConstructor;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderServiceInterfaceProxy implements OrderServiceV1{

    private final OrderServiceV1 target;
    private final LogTrace trace;



    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderService.orderItem()");
            target.orderItem(itemId);
            trace.end(status);
        }catch (Exception e){

            trace.exception(status, e);
            throw e;

        }
    }
}
