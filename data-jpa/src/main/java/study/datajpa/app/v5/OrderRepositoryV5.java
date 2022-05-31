package study.datajpa.app.v5;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.trace.callback.TraceTemplate;
import study.datajpa.trace.logtrace.LogTrace;
import study.datajpa.trace.template.AbstractTemplate;

@Repository
public class OrderRepositoryV5 {

    private final TraceTemplate template;

    public OrderRepositoryV5(LogTrace trace) {
        this.template = new TraceTemplate(trace);
    }

    public void save(String itemId){

        template.execute("OrderRepository.save()", () -> {
            if(itemId.equals("ex")){
                throw new IllegalArgumentException("Error Occurred!");
            }
            sleep(1000);
            return null;
        });

    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
