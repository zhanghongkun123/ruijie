package com.ruijie.rcos.rcdc.rco.module.def.userlicense;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年11月01日
 *
 * @author luojianmo
 */
public interface UserLicenseBusinessKey {

    /**
     * 用户并发授权模式下，用户申请用户并发授权失败
     */
    String RCDC_RCO_USER_LICENSE_OCCUPY_LICENSE_FAIL = "23201541";

    /**
     * 用户并发授权模式下，通过终端查询绑定用户信息失败，不允许进行会话与授权信息更新，请重新登录或联系管理员
     */
    String RCDC_RCO_USER_LICENSE_USER_BIND_TERMINAL_NOT_FIND = "23201542";

    /**
     * 用户并发授权模式下，通过终端查询终端类型为[{0}]，不符合预期[APP、VDI]类型，不允许进行会话与授权信息更新，请联系管理员
     */
    String RCDC_RCO_USER_LICENSE_USER_BIND_TERMINAL_TYPE_EXCEPTION = "23201543";

    /**
     * 用户并发授权模式下，更新客户端当前会话详情时，参数非法
     */
    String RCDC_RCO_USER_LICENSE_UPDATE_CURRENT_SESSION_INFO_PARAMETER_ILLEGAL = "23201544";

    /**
     * 用户并发授权模式下，OPENAPI获取桌面连接信息，用户名不允许为空
     */
    String RCDC_RCO_USER_LICENSE_USERNAME_NOT_BE_NULL = "23201545";

    /**
     * 用户并发授权模式下，OPENAPI获取桌面连接信息，用户名[{0}]查询用户ID失败
     */
    String RCDC_RCO_USER_LICENSE_USERNAME_FIND_USER_ID_FAIL = "23201546";

    /**
     * 用户并发授权模式下，OPENAPI转发网页版客户端在线会话信息时，统一登录状态异常，集群ID[{0}]消息无法转发
     */
    String RCDC_RCO_USER_LICENSE_UNIFIED_LOGIN_EXCEPTION_CANNOT_FORWARD_CLUSTER = "23201547";

    /**
     * 用户并发授权模式下，更新客户端当前会话详情时部分集群失败，集群ID列表[{0}]
     */
    String RCDC_RCO_USER_LICENSE_UPDATE_CURRENT_SESSION_INFO_PART_ERROR = "23201548";

    /**
     * 用户并发授权模式下，更新客户端当前会话详情时，终端当前绑定用户信息为空
     */
    String RCDC_RCO_USER_LICENSE_UPDATE_CURRENT_SESSION_INFO_USER_NULL = "23201549";

}
