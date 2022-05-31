package study.datajpa.pureproxy.proxy;

import org.junit.jupiter.api.Test;
import study.datajpa.pureproxy.proxy.code.CacheProxy;
import study.datajpa.pureproxy.proxy.code.ProxyPatternClient;
import study.datajpa.pureproxy.proxy.code.RealSubject;
import study.datajpa.pureproxy.proxy.code.Subject;

public class ProxyPatternTest {

    @Test
    void noProxyTest(){
        RealSubject realSubject = new RealSubject();
        ProxyPatternClient client = new ProxyPatternClient(realSubject);
        client.execute();
        client.execute();
        client.execute();

    }

    @Test
    void cacheProxyTest(){
        Subject realSubject = new RealSubject();
        Subject cacheProxy = new CacheProxy(realSubject);
        ProxyPatternClient client = new ProxyPatternClient(cacheProxy);
        client.execute();
        client.execute();
        client.execute();

    }
}
