package com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy;

/**
 * Description: 管理员登陆失败时返回IAC业务异常提示语
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月22日
 *
 * @author zdc
 */
public class RcdcIacAdminLoginFailDTO {

    private String iacBusinessMsg;

    public String getIacBusinessMsg() {
        return iacBusinessMsg;
    }

    public void setIacBusinessMsg(String iacBusinessMsg) {
        this.iacBusinessMsg = iacBusinessMsg;
    }
}
