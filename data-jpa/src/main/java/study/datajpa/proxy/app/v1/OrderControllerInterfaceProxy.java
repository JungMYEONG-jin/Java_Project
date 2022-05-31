package study.datajpa.proxy.app.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1{

    private final OrderControllerV1 target;
    private final LogTrace trace;



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
        return target.noLog();
    }
}
