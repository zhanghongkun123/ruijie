package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: 用户在GT登录的密码校验结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18 9:48
 *
 * @author yxq
 */
public class GtLoginDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码是否正确，true：正确，false：错误
     */
    private Boolean result;

    /**
     * 输入密码
     */
    private String input;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
