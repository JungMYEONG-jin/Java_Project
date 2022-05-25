package study.datajpa.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.hellotrace.HelloTraceV1;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV1 trace;

    @GetMapping("/v2/request")
    public String request(String itemId){

        TraceStatus status = null;
        try{
            status = trace.begin("OrderController.request()");
            orderService.orderItem(itemId);
            trace.end(status);
            return "OK";
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }

    }
}
