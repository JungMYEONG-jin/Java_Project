package com.shinhan;

public enum SAAuthType
{
    FINGERPRINT("1"), PIN("2"), PATTERN("3"), FACEID("4"), NONE("5");
    // String Code 4는 아이폰이 사용

    SAAuthType(String setting)
    {
        this.setting = setting;
    }

    private String setting;

    public String getSetting()
    {
        return setting;
    }

}