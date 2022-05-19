package sol.com.shinhan.controller;

public abstract class DataInitController {

    public boolean inOutLogData = false;

    public void init()
    {
        try{
            String configPath = "";

            configPath = XMLConfiguration.getInstance().get("/proworks/sfg/configPath@path");
            XMLConfiguration config = XMLConfiguration.getInstance(configPath);
            String inOutLogDataVal = config.get("/sol/inoutLog/log");

            if(inOutLogDataVal.equals("true")){
                this.inOutLogData = true;
            }
        }catch (Exception e)
        {
            this.inOutLogData = false;
        }
    }

    public void dataException(NoClassDefFoundError e)
    {
        WARNINGMsg w = new WARINGMsg();
    }


}
