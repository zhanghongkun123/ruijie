package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.enums;

/**
 * Description: 镜像产品端状态枚举
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/10
 *
 * @author wjp
 */
public enum ImageTemplateStateEnum {

    /** 检查镜像状态异常 */
    CHECK_ERROR(""),

    /** 发布所需空间不足 */
    PUBLISH_NEED_SPACE_LOW("检测到当前空间不足，为保证发布成功，请删减无用镜像或扩容"),

    /** 已是最新版本 */
    VERSION_SUCCESS("");

    private String message;

    ImageTemplateStateEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
