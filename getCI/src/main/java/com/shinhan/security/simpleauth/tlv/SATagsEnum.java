package com.shinhan.security.simpleauth.tlv;

public enum SATagsEnum {
    TAG_INIT_REG(15873),
    TAG_REG(15874),
    TAG_INIT_AUTH(

            15889),
    TAG_AUTH(15890),
    TAG_UNREG(

            15905),
    TAG_ERR(

            15921),
    TAG_CHANGE_ACCOUNT(15937);

    public final int id;

    SATagsEnum(int id) {
        this.id = id;
    }

    public static SATagsEnum get(int id) {
        byte b;
        int i;
        SATagsEnum[] arrayOfSATagsEnum;
        for (i = (arrayOfSATagsEnum = values()).length, b = 0; b < i; ) {
            SATagsEnum tag = arrayOfSATagsEnum[b];
            if (tag.id == id)
                return tag;
            b++;
        }
        return null;
    }
}
