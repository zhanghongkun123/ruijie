package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月30日
 *
 * @author linrenjian
 */
public interface PermissionConstants {

    /**
     * 根节点
     */
    String ROOT_ID = "root";

    /**
     * 跟节点名称
     */
    String ROOT_NAME = "总览";

    /**
     * 权限查询异常
     */
    Integer PERMISSSION_QUERY_EXCEPTION = -6;

    /**
     * -5 没有编辑权限
     */
    Integer NO_EDIT_PERMISSSION = -10;

    /**
     * -5 没有发布权限
     */
    Integer NO_PUBLISH_PERMISSSION = -10;


    /**
     * -5 没有启动权限
     */
    Integer NO_START_PERMISSSION = -10;

    /**
     * 会话不存在
     */
    Integer SESSION_NOT_EXIST = -20;

    /**
     * 失败
     */
    Integer FAILURE = -1;

    /**
     * 超级管理员账号
     */
    String ADMIN_NAME = "admin";
}
