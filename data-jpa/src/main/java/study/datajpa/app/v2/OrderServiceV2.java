package study.datajpa.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.hellotrace.HelloTraceV1;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {

    private final OrderRepositoryV2 orderRepository;
    private final HelloTraceV1 trace;

    public void orderItem(String itemId){
        TraceStatus status = null;
        try{
            status = trace.begin("OrderService.orderItem()");
            orderRepository.save(itemId);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
