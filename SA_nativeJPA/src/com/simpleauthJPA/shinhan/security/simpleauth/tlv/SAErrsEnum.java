package com.simpleauthJPA.shinhan.security.simpleauth.tlv;

public enum SAErrsEnum {
    ERR_NO_SUCH_ALGORITHM(11777),
    ERR_NO_SUCH_PADDING(11778),
    ERR_INVALID_KEY(11779),
    ERR_CERTIFICATE(11780),
    ERR_IO(11781),
    ERR_KEY_STORE(11782),
    ERR_KEY_ENTRY(11783),
    ERR_ILLEGAL_BLOCK_SIZE(11784),
    ERR_BAD_PADDING(11785),
    ERR_INVALID_KEY_SPEC(11792),
    ERR_UNRECOVERBLE_KEYSOTRE_ENTRY(11793),
    ERR_DATA_IS_NULL(11794),
    ERR_UNSUPPORTED_ENCODING(11795),
    ERR_ILLEGAL_ARGUMENT(11796),
    ERR_PARSE_JSON(11797),
    ERR_TO_JSON(11798),
    ERR_INIT_SERVER(19969),
    ERR_REG_SERVER(19970),
    ERR_INIT_AUTH_SERVER(19985),
    ERR_AUTH_SERVER(19986),
    ERR_UNREG_SERVER(20001),
    ERR_JDBC_D(20017),
    ERR_JDBC_AUTHINIT_S(20018),
    ERR_JDBC_AUTH_S(20034),
    ERR_JDBC_UNREG_S(20050),
    ERR_JDBC_I(20019),
    ERR_JDBC_LOG(20020);

    public final int id;

    SAErrsEnum(int id) {
        this.id = id;
    }

    public static SATagsEnum get(int id) {
        byte b;
        int i;
        SATagsEnum[] arrayOfSATagsEnum;
        for (i = (arrayOfSATagsEnum = SATagsEnum.values()).length, b = 0; b < i; ) {
            SATagsEnum tag = arrayOfSATagsEnum[b];
            if (tag.id == id)
                return tag;
            b++;
        }
        return null;
    }
}
