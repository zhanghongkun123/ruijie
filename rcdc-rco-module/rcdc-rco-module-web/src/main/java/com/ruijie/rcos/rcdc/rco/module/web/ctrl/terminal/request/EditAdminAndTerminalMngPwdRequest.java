package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.ModifyAdminPwdWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: 修改管理员和终端管理密码请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月27日
 *
 * @author yxq
 */
public class EditAdminAndTerminalMngPwdRequest {

    @ApiModelProperty(value = "修改终端管理密码请求")
    @NotNull
    private EditAdminPwdWebRequest modifyTerminalMngPwdRequest;

    @ApiModelProperty(value = "修改超级管理员密码请求")
    @NotNull
    private ModifyAdminPwdWebRequest modifyAdminPwdWebRequest;

    public EditAdminPwdWebRequest getModifyTerminalMngPwdRequest() {
        return modifyTerminalMngPwdRequest;
    }

    public void setModifyTerminalMngPwdRequest(EditAdminPwdWebRequest modifyTerminalMngPwdRequest) {
        this.modifyTerminalMngPwdRequest = modifyTerminalMngPwdRequest;
    }

    public ModifyAdminPwdWebRequest getModifyAdminPwdWebRequest() {
        return modifyAdminPwdWebRequest;
    }

    public void setModifyAdminPwdWebRequest(ModifyAdminPwdWebRequest modifyAdminPwdWebRequest) {
        this.modifyAdminPwdWebRequest = modifyAdminPwdWebRequest;
    }
}
