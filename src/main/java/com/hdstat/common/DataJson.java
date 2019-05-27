package com.hdstat.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DataJson {
    private Map returnMap = new HashMap<String, Object>();

    private String resultCode;

    private String resultMesg;


    public DataJson(String resultCode, String resultMesg) {
        this.resultCode = resultCode;
        this.resultMesg = resultMesg;
    }

}
