package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import java.util.Arrays;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.UserMessageObjTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 创建用户消息请求参数对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/26
 *
 * @author Jarman
 */
public class CreateUserMessageWebRequest implements WebRequest {

    @NotEmpty
    private IdLabelStringEntry[] sendingObjArr;

    @NotBlank
    @TextMedium
    private String title;

    @NotBlank
    @Size(max = 1000)
    private String content;
    
    @NotNull
    private UserMessageObjTypeEnum type;

    public IdLabelStringEntry[] getSendingObjArr() {
        return sendingObjArr;
    }

    public void setSendingObjArr(IdLabelStringEntry[] sendingObjArr) {
        this.sendingObjArr = sendingObjArr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserMessageObjTypeEnum getType() {
        return type;
    }

    public void setType(UserMessageObjTypeEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CreateUserMessageWebRequest [sendingObjArr=" + Arrays.toString(sendingObjArr) + ", title=" + title + ", content=" + content
                + ", type=" + type + "]";
    }
    
}
