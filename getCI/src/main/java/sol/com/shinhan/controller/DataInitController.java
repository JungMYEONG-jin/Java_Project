package sol.com.shinhan.controller;

import org.json.simple.JSONObject;

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
        w.msg = e.getMessage();

        if(w.msg == null || w.msg.trim().equals("")){
            w.msg = "Exception occurred";
            w.detail = RES.EXCEPTION_DETAIL_MSG;
        }else if(w.msg.indexOf("ORA-") > -1){
            w.msg = "DB Error occurred";
            w.detail = "Retry it";
        }else{
            w.msg = "Exception occurred";
            w.detail = RES.EXCEPTION_DETAIL_MSG;
        }

        JSONObject errorObj = new JSONObject();

        errorObj.put("msg", w.msg);
        errorObj.put("detail", w.detail);


    }

    public JSONObject noDataException()
    {
        JSONObject head = new JSONObject();
        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();

        head.put("result", "FAIL");
        head.put("resultMSG", "param data null");
        data.put("header", head);
        data.put("body", body);

        return data;
    }


}
