package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request;

import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserMacBindingDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Description: 创建用户-终端绑定关系请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/21 9:21
 *
 * @author yxq
 */
@ApiModel("创建用户-终端绑定关系请求")
public class CreateUserMacBindingWebRequest {

    @NotNull
    @ApiModelProperty("用户-终端绑定关系列表")
    private List<UserMacBindingDTO> userTerminalBindingList;

    public List<UserMacBindingDTO> getUserTerminalBindingList() {
        return userTerminalBindingList;
    }

    public void setUserTerminalBindingList(List<UserMacBindingDTO> userTerminalBindingList) {
        this.userTerminalBindingList = userTerminalBindingList;
    }
}
