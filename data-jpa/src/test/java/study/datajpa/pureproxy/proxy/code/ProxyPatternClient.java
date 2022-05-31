package study.datajpa.pureproxy.proxy.code;

import lombok.RequiredArgsConstructor;
import lombok.Setter;


public class ProxyPatternClient {

    private Subject subject;

    public ProxyPatternClient(Subject subject) {
        this.subject = subject;
    }

    public void execute(){
        subject.operation();
    }


}
