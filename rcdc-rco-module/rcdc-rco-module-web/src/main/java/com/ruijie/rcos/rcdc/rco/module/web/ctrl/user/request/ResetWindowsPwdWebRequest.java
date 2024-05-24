package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年8月25日
 *
 * @author zjy
 */
public class ResetWindowsPwdWebRequest implements WebRequest {


    @ApiModelProperty(value = "桌面ID", required = false)
    @Nullable
    private UUID deskId;

    @ApiModelProperty(value = "镜像ID", required = false)
    @Nullable
    private UUID imageTemplateId;

    /**
     * windows账户名称
     */
    @ApiModelProperty(value = "账户名称", required = true)
    @NotBlank
    @Size(min = 1, max = 20)
    private String account;

    @ApiModelProperty(value = "新密码", required = true)
    @NotBlank
    @Size(min = 1)
    private String newPwd;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    @Nullable
    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(@Nullable UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }
}