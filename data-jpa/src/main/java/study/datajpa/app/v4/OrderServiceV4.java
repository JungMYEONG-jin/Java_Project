package study.datajpa.app.v4;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.logtrace.LogTrace;
import study.datajpa.trace.template.AbstractTemplate;

@Service
@RequiredArgsConstructor
public class OrderServiceV4 {

    private final OrderRepositoryV4 orderRepository;
    private final LogTrace trace;

    public void orderItem(String itemId){

        AbstractTemplate<Void> template = new AbstractTemplate<Void>(trace) {
            @Override
            protected Void call() {
                orderRepository.save(itemId);
                return null;
            }
        };

        template.execute("OrderService.orderItem()");

    }
}
