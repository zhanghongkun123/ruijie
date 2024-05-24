package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.enums;


/**
 * Description: 前端来请求应用列表时告知当前是请求哪种主机的应用
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月25日
 *
 * @author liuwc
 */
public enum QueryAppHostTypeEnum {

    // 通过"镜像卡片"获取应用列表
    IMAGE,

    // 通过"镜像版本列表"获取应用列表
    IMAGE_VERSION,

    // 获取第三方主机的应用列表
    THIRD_HOST
}
