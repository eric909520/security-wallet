package org.secwallet.commonmodel.enums;

public enum EnablelEnum {

    close(0,"close"),
    open(1,"open");

    private int value;

    private String desc;

    EnablelEnum(int value,String desc){
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

}
