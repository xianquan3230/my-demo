package com.lcoil.drools.demo.model;

/**
 * @Classname Policy
 * @Description Policy
 * @Date 2022/1/15 3:51 PM
 * @Created by l-coil
 */
public class Policys {
    /**
     * 性别 ： 男、女
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 单身：是/否
     */
    private Boolean userSingle;
    /**
     * 结婚：是/否
     */
    private Boolean userMarry;
    /**
     * 育儿：是/否
     */
    private Boolean userParenting;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getUserSingle() {
        return userSingle;
    }

    public void setUserSingle(Boolean userSingle) {
        this.userSingle = userSingle;
    }

    public Boolean getUserMarry() {
        return userMarry;
    }

    public void setUserMarry(Boolean userMarry) {
        this.userMarry = userMarry;
    }

    public Boolean getUserParenting() {
        return userParenting;
    }

    public void setUserParenting(Boolean userParenting) {
        this.userParenting = userParenting;
    }
}
