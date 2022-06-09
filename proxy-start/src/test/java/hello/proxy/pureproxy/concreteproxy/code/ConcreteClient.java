package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {

    private ConcreteLogic logic;

    public ConcreteClient(ConcreteLogic logic) {
        this.logic = logic;
    }

    public void execute(){
        logic.operation();
    }
}
