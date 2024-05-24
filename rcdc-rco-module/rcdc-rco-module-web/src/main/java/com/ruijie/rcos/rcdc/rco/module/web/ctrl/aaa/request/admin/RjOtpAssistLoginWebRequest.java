package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月13日
 *
 * @author jarman
 */
public class RjOtpAssistLoginWebRequest implements WebRequest {

    @NotNull
    private Long timestamp;

    @NotBlank
    private String code;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
