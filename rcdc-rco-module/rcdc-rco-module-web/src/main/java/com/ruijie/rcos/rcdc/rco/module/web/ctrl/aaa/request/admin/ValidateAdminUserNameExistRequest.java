package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import java.util.UUID;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月24日
 * 
 * @author zhuangchenwu
 */
public class ValidateAdminUserNameExistRequest implements WebRequest {

    @ApiModelProperty(value = "管理员账号", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    @ApiModelProperty(value = "管理员ID", required = false)
    @Nullable
    private UUID id;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
