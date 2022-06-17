package com.simpleauthJPA.entity;

import com.simpleauthJPA.shinhan.security.imple.SAProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    String id;
    String pubkey;
    String uuid;
    String appid;
    String type;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", pubkey='" + pubkey + '\'' +
                ", uuid='" + uuid + '\'' +
                ", appid='" + appid + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public UserDto(String id, String pubkey, String uuid, String appid, String type) {
        this.id = id;
        this.pubkey = pubkey;
        this.uuid = uuid;
        this.appid = appid;
        this.type = type;
    }
}
