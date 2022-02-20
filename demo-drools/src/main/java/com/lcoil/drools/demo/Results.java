package com.lcoil.drools.demo;

/**
 * @Classname Result
 * @Description TODO
 * @Date 2022/1/15 3:51 PM
 * @Created by l-coil
 */
public class Results {

    private String code;
    private String info;

    public Results() {
    }

    public Results(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public static Results buildResult(String code, String info) {
        return new Results(code, info);
    }

    public void setResult(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("");
        sb.append("").append(code);
        sb.append("|").append(info);
        return sb.toString();
    }
}
