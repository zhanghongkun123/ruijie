package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums;

/**
 * Description: GT上报申请单变更状态Action
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/28
 *
 * @author WuShengQiang
 */
public enum AuditApplyNotifyActionEnum {

    /**
     * 撤回申请
     */
    CANCELLED,

    /**
     * 上传文件完成
     */
    UPLOADED,

    /**
     * 打印文件完成
     */
    PRINTED,

    /**
     * 操作失败
     */
    FAIL,

    /**
     * 失败处理完成
     */
    FAIL_SUCCESS

}
