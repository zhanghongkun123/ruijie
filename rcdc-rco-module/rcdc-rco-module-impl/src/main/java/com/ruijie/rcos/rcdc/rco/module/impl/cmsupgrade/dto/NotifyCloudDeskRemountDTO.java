package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dto;

/**
 *
 * Description: NotifyCloudDeskRemountDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年12月12日
 *
 * @author wjp
 */
public class NotifyCloudDeskRemountDTO {

    private Integer code;

    private String message;

    private Content content;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Description: content
     * Copyright: Copyright (c) 2020
     * Company: Ruijie Co., Ltd.
     * Create Time: 2020年12月12日
     *
     * @author wjp
     */
    public static class Content {
    }
}
