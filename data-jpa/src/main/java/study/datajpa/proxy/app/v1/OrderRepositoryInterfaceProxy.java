package study.datajpa.proxy.app.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;

@RequiredArgsConstructor
public class OrderRepositoryInterfaceProxy implements OrderRepositoryV1{

    private final OrderRepositoryV1 target;
    private final LogTrace trace;

    @Override
    public void save(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderRepository.save()");
            target.save(itemId);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
