package study.datajpa.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.trace.TraceId;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.hellotrace.HelloTraceV1;
import study.datajpa.trace.hellotrace.HelloTraceV2;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {

    private final HelloTraceV2 trace;

    public void save(TraceId traceId, String itemId){
        TraceStatus status = null;
        try{
            status = trace.beginSync(traceId,"OrderRepository.save()");
            if(itemId.equals("ex")){
                throw new IllegalArgumentException("Exception Occurred");
            }
            sleep(1000);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }

    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
