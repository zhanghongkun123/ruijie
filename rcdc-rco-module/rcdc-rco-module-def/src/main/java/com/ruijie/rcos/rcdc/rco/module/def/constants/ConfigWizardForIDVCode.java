package com.ruijie.rcos.rcdc.rco.module.def.constants;

/**
 * Description: IDV配置向导业务code
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/3
 *
 * @author brq
 */
public interface ConfigWizardForIDVCode {

    int SUCCESS = 0;

    int CODE_ERR_OTHER = 99;

    /** 不支持的终端模式 */
    int UN_SUPPORT_TERMINAL_MODE = -7;

    /** 终端组未开启IDV特性 */
    int GROUP_NOT_OPEN_IDV_CONFIG = -8;

    /** 非IDV终端，不允许进行用户绑定 */
    int TERMINAL_NOT_IDV_NOT_ALLOW_BIND = -9;

    /** 终端信息不存在 */
    int TERMINAL_NOT_EXIST = -10;

    /** 用户信息不存在 */
    int USER_NOT_EXIST = -11;

    /** 终端分组信息不存在 */
    int TERMINAL_GROUP_NOT_EXIST = -13;

    /** 终端已经存在绑定用户或者桌面，请先解绑 */
    int TERMINAL_HAS_BIND_RELATION = -15;

    /** 修改终端模式需要先初始化终端 */
    int TERMINAL_MODE_CAN_NOT_MODIFY = -16;

    /** 取消公用终端原有绑定关系发生异常 */
    int CANCEL_PUBLIC_TERMINAL_BIND_RELATION_ERROR = -17;

    /** 镜像未就绪，请联系管理员 */
    int IMAGE_TEMPLATE_STATUS_NOT_AVAILABLE = 6;

    /**
     * 当前终端CPU型号不支持当前操作系统
     */
    int DESK_RELEASE_IMAGE_UN_SUPPORT_WITH_TERMINAL_CPU = 7;

    /** 用户状态为禁用，不能进行绑定操作 */
    int USER_STATE_DISABLE = -18;

    /** 用户未开启IDV特性 */
    int USER_NOT_OPEN_IDV_CONFIG = -19;


    /** 终端组未开启VOI特性 */
    int GROUP_NOT_OPEN_VOI_CONFIG = -28;

    /** 非VOI终端，不允许进行用户绑定 */
    int TERMINAL_NOT_VOI_NOT_ALLOW_BIND = -29;

    /** 用户未开启VOI特性 */
    int USER_NOT_OPEN_VOI_CONFIG = -30;

    /** 非IDV VOI终端，不允许进行用户绑定 */
    int TERMINAL_NOT_VOIANDIDV_NOT_ALLOW_BIND = -32;

    /** 原先终端 IDV 模式 刷机后 选择了VOI */
    int TERMINAL_PLATFORM_IDV_TO_VOI = -33;

    /** 原先终端 VOI模式 刷机后 选择了IDV */
    int TERMINAL_PLATFORM_VOI_TO_IDV = -34;

    /** G3 终端部署 TCI 不支持 Windows 7 */
    int TERMINAL_PLATFORM_VOI_G3_LIMIT = -35;

    /** 不支持TC引导模式的TCI不支持WIN7-32 */
    int NOT_SUPPORT_TC_CAN_NOT_USER_WIN7_32 = -36;

    /** 镜像驱动未安装 */
    int IMAGE_DRIVER_NOT_INSTALL = -37;

    /** 镜像不支持该终端 */
    int IMAGE_NOT_SUPPORT_TERMINAL = -38;

    /** 终端授权不足 */
    int TERMINAL_AUTH_FAIL = -60;
}
