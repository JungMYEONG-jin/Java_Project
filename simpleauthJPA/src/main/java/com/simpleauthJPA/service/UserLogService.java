package com.simpleauthJPA.service;

import com.simpleauthJPA.entity.UserLog;
import com.simpleauthJPA.repository.UserLogRepository;
import com.simpleauthJPA.repository.UserRepository;
import com.simpleauthJPA.shinhan.security.imple.SAProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLogService {

    private final UserLogRepository userLogRepository;

    public void fine(String msg){
        if(SAProperty.isLog){
            System.out.println("[simpleauth]" + msg);
        }
    }

    public static String getSAStackTrace(Exception e) {
        String result = "";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);
        e.printStackTrace(ps);
        result = out.toString();
        return result;
    }

    public int SALog(String cusno, String id, String tag, String injson, String outjson, String msg, String errorcode, String stacktrace){
        UserLog userLog = new UserLog(cusno, id, tag, injson, outjson, msg, errorcode, stacktrace, LocalDateTime.now().toString());
        UserLog saved = userLogRepository.save(userLog);
        Optional<UserLog> byId = userLogRepository.findById(saved.getSeq());
        if(byId.isEmpty()){
            return 0;
        }
        return 1;
    }

    public int SALog(String cusno, String id, String tag, String msg, String errorcode, String stacktrace){
        UserLog userLog = new UserLog(cusno, id, tag, null, null, msg, errorcode, stacktrace, LocalDateTime.now().toString());
        UserLog saved = userLogRepository.save(userLog);
        Optional<UserLog> byId = userLogRepository.findById(saved.getSeq());
        if(byId.isEmpty()){
            return 0;
        }
        return 1;
    }

    public UserLog save(UserLog userLog){
        return userLogRepository.save(userLog);
    }

    public List<UserLog> findByCusno(String cusno){
        return userLogRepository.findByCusno(cusno);
    }



}
