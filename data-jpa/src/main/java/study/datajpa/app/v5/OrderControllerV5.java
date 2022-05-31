package study.datajpa.app.v5;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.trace.callback.TraceCallback;
import study.datajpa.trace.callback.TraceTemplate;
import study.datajpa.trace.logtrace.LogTrace;
import study.datajpa.trace.template.AbstractTemplate;

@RestController
public class OrderControllerV5 {

    private final OrderServiceV5 orderService;
    private final TraceTemplate template;

    public OrderControllerV5(OrderServiceV5 orderService, LogTrace trace) {
        this.orderService = orderService;
        this.template = new TraceTemplate(trace);
    }

    @GetMapping("/v5/request")
    public String request(String itemId){

        return template.execute("OrderController.request()", new TraceCallback<>(){
            @Override
            public String call(){
                orderService.orderItem(itemId);
                return "OK";
            }
        });
    }
}
