package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/13 11:29
 *
 * @author linrenjian
 */
public class FunTypes {

    /**
     * 功能类型
     */
    public static final  String FUNTYPE = "FUNTYPE";

    /**
     * 菜单
     */
    public static final  String MENU = "MENU";

    /**
     * 功能
     */
    public static final  String FUN = "FUN";

    /**
     * 是否必须 这个标识为用于前端是否必须选中 父节点不选 一般用于查看
     */
    public static final String ENABLE_NEED = "ENABLE_NEED";

    /**
     * 后端是否是否必须 用于接口防测试 避免前端少传
     */
    public static final String ENABLE_RCDC_NEED = "ENABLE_RCDC_NEED";

    /**
     * 是否是属于超级管理员
     */
    public static final String ENABLE_SUPER_ADMIN = "ENABLE_SUPER_ADMIN";


    /**
     * 是否是属于内置超级管理员
     */
    public static final String ENABLE_DEFAULT_SUPER_ADMIN = "ENABLE_DEFAULT_SUPER_ADMIN";

    /**
     * 是否是属于审计管理员
     */
    public static final String ENABLE_AUD_ADMIN = "ENABLE_AUD_ADMIN";

    /**
     * 是否是属于安全管理员
     */
    public static final String ENABLE_SEC_ADMIN = "ENABLE_SEC_ADMIN";

    /**
     * 属于系统管理员
     */
    public static final String ENABLE_SYS_ADMIN = "ENABLE_SYS_ADMIN";

    /**
     * 不属于系统管理员
     */
    public static final String NO_ENABLE_SYS_ADMIN = "NO_ENABLE_SYS_ADMIN";

    /**
     * 属于内置管理员
     */
    public static final String ENABLE_INNER_ADMIN = "ENABLE_INNER_ADMIN";

    /**sk_webmvckit_no_login
     * 是
     */
    public static final String YES = "YES";

    /**
     * 否
     */
    public static final String NO = "NO";

    /**
     * 服务器部署- VDI模式式
     */
    public static final String VDI_SERVER_MODEL = "vdi";


    /**
     * 服务器部署- IDV
     */
    public static final String IDV_SERVER_MODEL = "rcm";


    /**
     * 服务器部署- MINI模式
     */
    public static final String MINI_SERVER_MODEL = "mini";

    /**
     * 是否是查看
     */
    public static final String ENABLE_LOOK = "ENABLE_LOOK";

    /**
     * 排序 一个大菜单 100个冗余
     */
    public static final String ORDER = "ORDER";



    /**
     * 自由选择元素
     */
    public static final String FREE_DOM = "FREE_DOM";

    /**
     * 自由选择元素 结束
     */
    public static final String FREE_DOM_END = "FREE_DOM_END";

    /**
     * 是否配置了业务全局开关
     */
    public static final String GLOBAL_ENABLE_SEARCH = "GLOBAL_ENABLE_SEARCH";

    /**
     * 配置了业务全局开关-对应的key
     */
    public static final String GLOBAL_ENABLE_SEARCH_KEY = "GLOBAL_ENABLE_SEARCH_KEY";

    /**
     * 联动勾选
     */
    public static final String CASCADING_SELECTION = "CASCADING_SELECTION";

}
