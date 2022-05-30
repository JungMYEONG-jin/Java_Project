package study.datajpa.trace.logtrace;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import study.datajpa.trace.TraceStatus;
import study.datajpa.trace.hellotrace.HelloTraceV2;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FieldLogTraceTest {

    FieldLogTrace trace = new FieldLogTrace();

    @Test
    void begin_end_level2(){

        TraceStatus status1 = trace.begin("hello1");
        TraceStatus status2 = trace.begin("Hello2");
        trace.end(status2);
        trace.end(status1);
    }

    @Test
    void begin_exception_level2(){
        TraceStatus hello1 = trace.begin("Hello1");
        TraceStatus hello2 = trace.begin("Hello2");
        trace.exception(hello2, new IllegalStateException());
        trace.exception(hello1, new IllegalStateException());
    }

}