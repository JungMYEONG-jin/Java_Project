package com.simpleauthJPA.repository;

import com.simpleauthJPA.aop.SALogAspect;
import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.entity.UserDto;
import com.simpleauthJPA.service.UserLogService;
import com.simpleauthJPA.shinhan.SimpleAuthTask;
import com.simpleauthJPA.shinhan.security.imple.SAProperty;
import com.simpleauthJPA.shinhan.security.imple.SASimpleAuthAction;
import com.simpleauthJPA.shinhan.security.listener.SAListener;
import com.simpleauthJPA.shinhan.security.simpleauth.SAConst;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SAInvalidPasswordException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthException;
import com.simpleauthJPA.shinhan.security.simpleauth.exception.SASimpleAuthMessageException;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAErrorMessage;
import com.simpleauthJPA.shinhan.security.simpleauth.message.SAMessageUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SAErrsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.tlv.SATagsEnum;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAHexUtil;
import com.simpleauthJPA.shinhan.security.simpleauth.util.SAUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@Import(SALogAspect.class)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SASimpleAuthAction action;

    public String APP_ID 		 = "TEST_ALIAS";
    public String APP_ALIAS 	 = "SA_ALIAS";
    public String APP_VERSION 	 = "1.0";
    public String AUTH_TYPE 	 = "3";

    public String CUSNO			 = "0987654321";
    public String ID 			 = "1234567890";
    public long   currentTime    = 1630642197023L;

    // 클라이언트 고정값
    public String strChallenge   = "3E74C931086CF7B52FA22921E4718134433A0CE65045740540E303CA46D492F86D0F55667D5EA1EADDB68D0544F0F2A4B99F62498C4148F5E0E744DF710C0297";
    public String PUBKEY 		 = "30820122300D06092A864886F70D01010105000382010F003082010A0282010100997A4DE16E426664DB117B7EEBAD0F17BF1C317EEE8A4D1D441C60CD26598B03B3BCA3277BDCB0FDC26D3541575D14510103D7A127829A2CF2D2FAC6F61E1AD4D4685945C3AFF3F70B4261CBA77D07A60CB52217A25A1E7F412EBA83D44B4D5F5EC84D7B6AB438814A767637E2FBAFB73EE6442D85AF15A38DE59324ECB65FE58C864AA5B367DAD43C739C8B6562AE013406CF1985DD065FF5BB0D9C23CF118902ED7CB418718BA8B839340583A6BF9058AE04FDFD22AA2E265B5A83EBCB0397EB328D4A4FCC7F29AAAE528C3D18AE71475D3E574E3B53ABCD2747098EE2291F235C578788B72715768F2A6371C495693F543F15DDB2F518161BB7801307D0FD0203010001";
    public String PRIKEY 		 = "308204BE020100300D06092A864886F70D0101010500048204A8308204A40201000282010100997A4DE16E426664DB117B7EEBAD0F17BF1C317EEE8A4D1D441C60CD26598B03B3BCA3277BDCB0FDC26D3541575D14510103D7A127829A2CF2D2FAC6F61E1AD4D4685945C3AFF3F70B4261CBA77D07A60CB52217A25A1E7F412EBA83D44B4D5F5EC84D7B6AB438814A767637E2FBAFB73EE6442D85AF15A38DE59324ECB65FE58C864AA5B367DAD43C739C8B6562AE013406CF1985DD065FF5BB0D9C23CF118902ED7CB418718BA8B839340583A6BF9058AE04FDFD22AA2E265B5A83EBCB0397EB328D4A4FCC7F29AAAE528C3D18AE71475D3E574E3B53ABCD2747098EE2291F235C578788B72715768F2A6371C495693F543F15DDB2F518161BB7801307D0FD0203010001028201000DE31D2CEAB72360B8D9CA1CDFE256DD45339DDEE862A73BA0B018AA37701EBE05036A97E603401896011FB27BDF363966281CD8BE247922DC0DEF1715A9FFAF8650B2D33733B29585F549B80DBDA87583ECD7DADF9BDBEA93D1509B21187735CBF3BFDF0CF6A5AE3D389ED5CBBA0E5A3C5CC58A80F028BB15D9D208A34A953EA21394EF849A67A96CD32A24973C70A0E7EB6A28ED26BA9BB520B07D8AFE5E918A95D887DEF945147E255CB3BA56B5708EA073F5C88807DB071ABF05CCAC385FB7A53D98EC3736C864EC78859832FC71A04111AB3E890CC8B8D06C380ECE7398D0B80259516453A7D038D6502FCBC1B754D724A966D711EB02A62C8B7103EE8102818100D506BA94982C7927426AD5172DC3B03A368474BA3369AEF46C269C88EC688F0AB1B0F09A103F4769B24F43B9F77BB863AAE43233798D601FED7E62826BF7CD602E2CC3E49E424C5E3BB5AA7FE07FE9333B597070298CE88B904445AC068F104D23E54839BD11110DDAEDAE7BB47DEE15CBC3FF6A68011CD88B3764DAAFA0BC3102818100B87052B7AD68F53D3B1C13E041979265D558D1DA632111B96D969DE0361BEE29FFC1E537AB126791DB70C420C39475A2CA3CB7AB7F19FA675EDCE07F65A9B91CE2B64CB6DC9E2FB40C3F2263E146FA66DB1DB5B6C8A126C1057E33822EDA8C4A15885E3F8F7C144BFEB8C5DADEB138127FCDA88CD4900D8454E0BB389E3A4A8D0281810091F73C5D992FD00C309C574ABC96FBE7C777572C4712414945D9E7248328D9EAAFFAD7C21923E2E0EFC7145716D6F56F85A73B26BE413AFE659356189BB67E494B1A6D57F780E22727AEC1174253230D71BB4529F5AE91A11FC7481E299859C921B1779DE8890B307D34A37C2C1EA5C6D58A8536F9F0D93D1F976349DD51BBE1028181008684EDDFA76B90CA3AED6C95B8614B565877B27EFCA3E6E07E4C47137B837D7E465A6DCADF4BCA1C389793846E013E971FB78F2256CE64B2B61E94E2760C86C5C998BA74F88DAB2F3F6B60B2C660D54AE56F52D4609A20C0D137CD6B9AAEAD796F2109D483DB349684CED107A43A4F3236E291973D855F466FFD84AF73C002150281807B8D8D2E59033CBE8BDEF90CF60AEE1C46D2559989910D32F76BCDA061F7484CA82F7251347093B2487A8C5A6DCF6B512023FECFD52390176E6F02D29836D0202B8A4D75516390BBEDD270082C83F4DFACAFBE0FBA8BE9AF6706E3B38BF8BCC4A08CF0D36C8198D99F26F4704791AB5407C63296F471FA4CF5C5959051B84FE3";
    public String UUID 			 = "uuid";
    HttpSession session;
    private static JSONObject jsonObject = null;


    @BeforeEach
    void init()
    {
        session = new MockHttpSession();
        session.setAttribute(SAConst.TAG_CUSNO, CUSNO);
        session.setAttribute("sa_challenge", strChallenge);
        session.setAttribute("sa_authkey",AUTH_TYPE);
        action.setListener(new SAListener() {
            @Override
            public void onSetChallengeInSession(String paramString, HttpSession paramHttpSession) {
                paramHttpSession.setAttribute(SAConst.TAG_CUSNO, paramString);
            }

            @Override
            public boolean onSimpleAuthInfoReg(Object var) throws SASimpleAuthException {
                try{
                    if(var!=null)
                    {
                        User user = (User) var;
                        User savedUser = userRepository.save(user);
                        List<User> findUser = userRepository.findByIdAndTypeEquals(savedUser.getId(), "999999999");
                        int cnt = findUser.size(); // id random 생성하여 user 마다 고유함.
                        if(cnt==1)
                            return true;
                        else
                            return false;
                    }
                } catch (Exception e) {
                    throw new SASimpleAuthException(e.toString());
                }
                return false;
            }

            @Override
            public User onSimpleAuthUserSearch(Object var) throws SASimpleAuthException {
                try{
                    if(var != null){
                        User user = (User) var;
                        List<User> findUser = userRepository.findByIdAndTypeEquals(user.getId(), "999999999");
                        int cnt = findUser.size();
                        if(cnt==1){
                            return findUser.get(0);
                        }else{
                            return null;
                        }
                    }
                }catch (Exception e) {
                    throw new SASimpleAuthException(e.toString());
                }
                return null;
            }


            @Override
            public String processInit(String reqJson, HttpSession paramHttpSession) {

                SAProperty.setSA_Log(false);
                SAProperty.setSA_TBName("MBI_SIMPLEAUTH");
                SAProperty.setSA_ColumName("ID", "CUSNO", "PUBKEY", "UUID", "APP_ID", "TYPE", "STATUS", "REG_DTTM", "DROP_DTTM", "LAST_AUTH_DTTM");

                // log, sequence, index 생략

                SAProperty.setSA_DBPoolName("mbbPool");
                SAProperty.setSA_StatusValue("1", "9");

                String result = null;


                return null;
            }

            @Override
            public String registInit(String reqTag, String reqJson, HttpSession paramHttpSession) {
                SASimpleAuthAction action = new SASimpleAuthAction(this);
                String result = null;
                String regInitServerJson = null;

                String cusno = paramHttpSession.getAttribute("cusno").toString();

                try{

                    regInitServerJson = action.reg_init_server(reqTag, reqJson, cusno, paramHttpSession);
                    System.out.println("junit test reg init message: "+regInitServerJson);
                    JSONParser parser = new JSONParser();
                    JSONObject obj = null;

                    try{
                        obj =(JSONObject) parser.parse(regInitServerJson);
                        String id = obj.get("id").toString();
                    } catch (ParseException e) {
                        System.out.println("json exception: "+e.getMessage());
                        throw new SASimpleAuthMessageException(SAErrsEnum.ERR_PARSE_JSON, SAErrorMessage.ERR_MSG_PARSE_JSON, SAErrorMessage.ERR_CODE_PARSE_JSON);
                    }

                    if(regInitServerJson!=null)
                        return regInitServerJson;

                    String strSessionChallenge = paramHttpSession.getAttribute("challenge").toString();

                } catch (SASimpleAuthException e) {
                    System.out.println("main exception: "+e.getMessage());
                }finally {
                    if(action!=null)
                        action=null;
                }


                return result;
            }

            @Override
            public String regist(String reqTag, String reqJson, HttpSession paramHttpSession) {

                String sessionChallenge = paramHttpSession.getAttribute("challenge").toString();
                SASimpleAuthAction action = new SASimpleAuthAction(this);
                String result = null;

                String resultMsgJson = null;

                String cusno = paramHttpSession.getAttribute("cusno").toString();

                try{
                    String strSessionChallenge = paramHttpSession.getAttribute("challenge").toString();
                    System.out.println("strSessionChallenge = " + strSessionChallenge);

                    resultMsgJson = action.reg_server(reqTag, reqJson, cusno, strSessionChallenge, paramHttpSession);
                    System.out.println("resultMsgJson = " + resultMsgJson);

                    String strID = "";
                    String strAppID = "";
                    String strUUID = "";
                    String strType = "";
                    String err_yn = "";
                    String strCusno = "";

                    JSONObject obj = null;

                    obj = SAUtil.strToJSON(resultMsgJson);

                    strID = obj.get(SAConst.TAG_ID).toString();
                    strAppID = obj.get(SAConst.TAG_APPID).toString();
                    strUUID = obj.get(SAConst.TAG_UUID).toString();
                    strType = obj.get(SAConst.TAG_TYPE).toString();
                    strCusno = obj.get(SAConst.TAG_CUSNO).toString();
                    err_yn = obj.get(SAConst.TAG_YN).toString();

                    if(err_yn.equals("n"))
                        result = resultMsgJson;

                    System.out.println("regist auth id = " + strID);

                }catch (SASimpleAuthException e)
                {

                } catch (SAInvalidPasswordException e) {
                    e.printStackTrace();
                } finally {
                    String strSessionChallenge = paramHttpSession.getAttribute("challenge").toString();
                    if(strSessionChallenge!=null || strSessionChallenge.length()>0)
                    {
                        paramHttpSession.setAttribute("challenge", "");
                    }
                    if (action!=null)
                        action=null;
                }
                return result;

            }

            @Override
            public String authorizeInit(String reqTag, String reqJson, HttpSession paramHttpSession) {

                SASimpleAuthAction action = new SASimpleAuthAction(this);
                String result = null;

                String authInitJson = null;
                String authkey = null;

                try{
                    authkey = SAMessageUtil.getAuthKey(reqJson);
                    if (authkey!=null)
                    {
                        paramHttpSession.setAttribute("auth", authkey);
                    }else
                        throw new SASimpleAuthException(SAErrsEnum.ERR_INIT_AUTH_SERVER, SAErrorMessage.ERR_MSG_ID_NULL, SAErrorMessage.ERR_CODE_ID_NULL);


                    authInitJson = action.auth_Init_server(reqTag, reqJson, paramHttpSession);

                    JSONObject obj = null;
                    String err_YN = "";

                    obj = SAUtil.strToJSON(authInitJson);
                    err_YN = obj.get(SAConst.TAG_YN).toString();

                    if(err_YN.equals("n"))
                    {
                        result = authInitJson;
                    }
                } catch (SASimpleAuthException e) {
                    e.printStackTrace();
                } catch (SAInvalidPasswordException e) {
                    e.printStackTrace();
                } finally {
                    if (action!=null)
                        action=null;
                }
                return result;
            }

            @Override
            public String authorize(String reqTag, String reqJson, HttpSession paramHttpSession) {
                SASimpleAuthAction action = new SASimpleAuthAction(this);
                String result = null;
                String authResultJson = null;

                final String authkey = paramHttpSession.getAttribute("auth").toString();
                System.out.println("authkey = " + authkey);

                try{
                    String strSessionChallenge = paramHttpSession.getAttribute("challenge").toString();
                    System.out.println("strSessionChallenge = " + strSessionChallenge);
                    authResultJson = action.auth_server(reqTag, reqJson, strSessionChallenge, paramHttpSession);
                    System.out.println("authResultJson = " + authResultJson);

                    JSONObject obj = SAUtil.strToJSON(authResultJson);

                    String strID = obj.get(SAConst.TAG_ID).toString();
                    String strAppId = obj.get(SAConst.TAG_APPID).toString();
                    String strUUID = obj.get(SAConst.TAG_UUID).toString();
                    String strType = obj.get(SAConst.TAG_TYPE).toString();
                    String cusno = obj.get(SAConst.TAG_CUSNO).toString();
                    String err_YN = obj.get(SAConst.TAG_YN).toString();

                    if(err_YN.equals("n"))
                        result = authResultJson;
                } catch (SASimpleAuthException e) {

                }finally {
                    String strSessionChallenge = paramHttpSession.getAttribute("challenge").toString();
                    String strAuthKey = paramHttpSession.getAttribute("auth").toString();

                    if(strSessionChallenge!=null || strSessionChallenge.length()>0)
                    {
                        paramHttpSession.setAttribute("challenge", "");
                    }
                    if(strAuthKey!=null || strAuthKey.length()>0)
                    {
                        paramHttpSession.setAttribute("auth", "");
                    }
                    if(action!=null)
                        action=null;
                }
                return result;
            }

            @Override
            public String unregist(String reqTag, String reqJson, HttpSession paramHttpSession) {
                SASimpleAuthAction action = new SASimpleAuthAction(this);

                String result = null;
                String unregResultJson = null;

                final String cusno = paramHttpSession.getAttribute("cusno").toString();

                try{
                    unregResultJson = action.unreg(reqTag, reqJson, paramHttpSession);

                    JSONObject obj = null;
                    obj = SAUtil.strToJSON(unregResultJson);

                    String err_YN = obj.get(SAConst.TAG_YN).toString();

                    if (err_YN.equals("n"))
                        result = unregResultJson;

                } catch (SASimpleAuthException e) {
                    e.printStackTrace();
                }finally {
                    if (action!=null)
                        action=null;
                }
                return result;


            }

            @Override
            public boolean CheckPasswordValidation(String password, String authType, HttpSession paramHttpSession) {
                return false;
            }
        });
    }

    @Test
    void integatedTest() throws SASimpleAuthException {
        String reqJson = "{\"tag\":\"3E01\",\"tmppub\":\"30820122300D06092A864886F70D01010105000382010F003082010A0282010100997A4DE16E426664DB117B7EEBAD0F17BF1C317EEE8A4D1D441C60CD26598B03B3BCA3277BDCB0FDC26D3541575D14510103D7A127829A2CF2D2FAC6F61E1AD4D4685945C3AFF3F70B4261CBA77D07A60CB52217A25A1E7F412EBA83D44B4D5F5EC84D7B6AB438814A767637E2FBAFB73EE6442D85AF15A38DE59324ECB65FE58C864AA5B367DAD43C739C8B6562AE013406CF1985DD065FF5BB0D9C23CF118902ED7CB418718BA8B839340583A6BF9058AE04FDFD22AA2E265B5A83EBCB0397EB328D4A4FCC7F29AAAE528C3D18AE71475D3E574E3B53ABCD2747098EE2291F235C578788B72715768F2A6371C495693F543F15DDB2F518161BB7801307D0FD0203010001\",\"appid\":\"TEST_ALIAS\"}";
        System.out.println("reqJson = " + reqJson);

//        HttpSession session = new MockHttpSession();
//        session.setAttribute(SAConst.TAG_CUSNO, CUSNO);
        String serverResult = action.reg_init_server("3E01", reqJson, session.getAttribute(SAConst.TAG_CUSNO).toString() ,session);
    }





    @Test
    void FunctionTest1(){
//        log.info(userRepository.getClass().toString());
        List<User> saUserInfo = userRepository.getSAUserInfo("4b732627-a983-4e64-a74d-c765eca17996");
        for (User user : saUserInfo) {
            UserDto userDto = new UserDto(user.getId(), user.getPubkey(), user.getUuid(), user.getAppid(), user.getType());
            System.out.println("userDto = " + userDto);
        }
    }

    @Test
    void FunctionTest2(){
        List<User> saUserInfo = userRepository.getSAUserInfo("4b732627-a983-4e64-a74d-c765eca17996");
        User user = saUserInfo.get(0);
        user.setType("1");
        userRepository.save(user);

        List<User> byIdAndTypeEquals = userRepository.findByIdAndTypeEquals("4b732627-a983-4e64-a74d-c765eca17996", "1");
        for (User findUser : byIdAndTypeEquals) {
            UserDto userDto = new UserDto(findUser.getId(), findUser.getPubkey(), findUser.getUuid(), findUser.getAppid(), findUser.getType());
            System.out.println("userDto = " + userDto);
        }

    }

    @Test
    void ObjectTest(){
        List<User> saUserInfo = userRepository.getSAUserInfo("4b732627-a983-4e64-a74d-c765eca17996");
        Object a = saUserInfo.get(0);
        User user = (User) a;
        UserDto userDto = new UserDto(user.getId(), user.getPubkey(), user.getUuid(), user.getAppid(), user.getType());
        System.out.println("userDto = " + userDto);
    }

    @Test
    void PagingTest(){
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "regdate"));

        Page<User> byCusno = userRepository.findByCusno("20220333", pageRequest);
        List<User> users = byCusno.getContent();


        Page<UserDto> pageUserDto = byCusno.map(e ->
                new UserDto(e.getId(), e.getPubkey(), e.getUuid(), e.getAppid(), e.getType()));

        List<UserDto> result = pageUserDto.getContent(); // getContent 하면 list로 return 됨

        int idx = 1;
        for (UserDto userDto : result) {
            System.out.println("user " + idx++ + " = " + userDto);
        }

    }

    /**
     * PESSIMISTIC_WRITE 설정하여 readOnly 일때 테스트 실행 가능함.
     * 해당 옵션 없으면 실행 불가.
     */
    @Test
    @Transactional(readOnly = true)
    void LockModeTest(){
        List<User> result = userRepository.findLockByCusno("20220333");
        System.out.println("result.size() = " + result.size());

        List<UserDto> userDtoList = result.stream().map(e -> new UserDto(e.getId(), e.getPubkey(), e.getUuid(), e.getAppid(), e.getType())).collect(Collectors.toList());
        int idx = 1;
        for (UserDto userDto : userDtoList) {
            System.out.println("userDto" + idx++ +" = " + userDto);
        }
    }

}