package study.datajpa.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.hellotrace.HelloTraceV1;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {

    private final OrderRepositoryV1 orderRepository;
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
