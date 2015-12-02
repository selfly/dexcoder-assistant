//package com.dexcoder.assistant.persistence;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by liyd on 2015-11-18.
// */
//public class ParameterMappingTokenHandler implements TokenHandler {
//
//    /** 变量列表 按出现顺序*/
//    private List<String> parameters = new ArrayList<String>();
//
//    public String handleToken(String content) {
//        parameters.add(content);
//        return "?";
//    }
//
//    public List<String> getParameters() {
//        return parameters;
//    }
//}
