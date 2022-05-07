package com.lcoil.common.command;

/**
 * @Classname DemoCommand
 * @Description TODO
 * @Date 2022/2/19 8:44 PM
 * @Created by l-coil
 */
public class BaseCommand implements Command{
    private Long companyId;
    private String operator;

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
