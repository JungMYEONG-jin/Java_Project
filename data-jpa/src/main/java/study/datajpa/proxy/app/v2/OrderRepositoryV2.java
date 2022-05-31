package study.datajpa.proxy.app.v2;

public class OrderRepositoryV2 {

    public void save(String itemId){
        if (itemId.equals("ex")){
            throw new IllegalStateException("예외 발생2");
        }
        sleep(1000);
    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

}
