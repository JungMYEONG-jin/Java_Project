package kakao.getCI.springbook.hello;

public class HelloTarget implements Hello{

    @Override
    public String sayHello(String name) {
        return "Hello "+name;
    }

    @Override
    public String sayHi(String name) {
        return "Hi "+name;
    }

    @Override
    public String sayThankYou(String name) {
        return "Thank you "+name;
    }

    @Override
    public String byeHello(String name) {
        return "Bye "+name;
    }
}
