package com.simpleauthJPA.controller;

import com.simpleauthJPA.entity.User;
import com.simpleauthJPA.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}/{type}")
    public String findUser(@PathVariable("id") String id, @PathVariable("type") String type){
        List<User> users = userService.findByIdAndType(id, type);
        JSONArray arr = new JSONArray();

        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("ID", user.getId());
            obj.put("APPID", user.getAppid());
            obj.put("CUSNO", user.getCusno());
            obj.put("TYPE", user.getType());
            obj.put("REG_DTTM", user.getRegdate());
            obj.put("DROP_DTTM", user.getUnregdate());
            arr.add(obj);
        }

        return arr.toString();
    }


    @GetMapping("/users/authinfo/{id}")
    public String getAuthInfo(@PathVariable("id") String id){
        List<User> users = userService.getUserInfo(id);
        JSONArray arr = new JSONArray();

        for (User user : users) {
            JSONObject obj = new JSONObject();
            obj.put("ID", user.getId());
            obj.put("APPID", user.getAppid());
            obj.put("CUSNO", user.getCusno());
            obj.put("TYPE", user.getType());
            obj.put("REG_DTTM", user.getRegdate());
            obj.put("DROP_DTTM", user.getUnregdate());
            arr.add(obj);
        }

        return arr.toString();
    }


//    @PostConstruct
    private void init()
    {
        String id = UUID.randomUUID().toString();
        for(int i=0;i<10;i++)
        {
            userService.save(new User(id, "sbank", "0987654321", id.substring(4,12), "3", "9", "939939999", LocalDateTime.now().toString(), "999999999", null));
        }

    }


}
