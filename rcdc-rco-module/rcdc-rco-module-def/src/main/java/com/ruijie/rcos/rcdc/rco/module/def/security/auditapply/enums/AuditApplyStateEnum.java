package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

import java.util.Arrays;
import java.util.List;

/**
 * 文件导出审批文件单状态枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public enum AuditApplyStateEnum {

    /** 待文件上传 */
    UPLOADING(BusinessKey.RCDC_RCO_AUDIT_APPLY_UPLOADING_STATE),

    /** 校验文件中 */
    COMPUTING(BusinessKey.RCDC_RCO_AUDIT_APPLY_COMPUTING_STATE),

    /** 待审批 */
    PENDING_APPROVAL(BusinessKey.RCDC_RCO_AUDIT_APPLY_PENDING_APPROVAL_STATE),

    /** 已撤回 */
    CANCELED(BusinessKey.RCDC_RCO_AUDIT_APPLY_CANCELED_STATE),

    /** 已批准 */
    APPROVED(BusinessKey.RCDC_RCO_AUDIT_APPLY_APPROVED_STATE),

    /** 已驳回 */
    REJECTED(BusinessKey.RCDC_RCO_AUDIT_APPLY_REJECTED_STATE),

    /** 
     * 失败 
     */
    FAIL(BusinessKey.RCDC_RCO_AUDIT_APPLY_FAIL_STATE),

    /**
     * 处理中
     */
    HANDLING(BusinessKey.RCDC_RCO_AUDIT_APPLY_HANDLING_STATE);

    private final String text;

    AuditApplyStateEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }

    /**
     * 申请单是否处于关闭状态
     * @param applyState 申请单状态
     * @return true：已关闭/false：未关闭
     */
    public static boolean enableApplyClose(AuditApplyStateEnum applyState) {
        return REJECTED == applyState || FAIL == applyState;
    }

    /**
     * 获取未完成申请单状态
     * @return 申请单列表
     */
    public static List<AuditApplyStateEnum> getNotCompleteStateList() {
        return Arrays.asList(AuditApplyStateEnum.UPLOADING, AuditApplyStateEnum.COMPUTING, AuditApplyStateEnum.PENDING_APPROVAL);
    }

    /**
     * 获取可废弃申请单状态集合
     * @return 申请单列表
     */
    public static List<AuditApplyStateEnum> getExpendableStateList() {
        return Arrays.asList(AuditApplyStateEnum.UPLOADING, AuditApplyStateEnum.COMPUTING);
    }

    /**
     * 申请单是否已经完成
     * @param applyState 申请单状态
     * @return true：已结束/false：未结束
     */
    public static boolean enableApplyFinish(AuditApplyStateEnum applyState) {
        return REJECTED == applyState || FAIL == applyState || APPROVED == applyState || CANCELED == applyState;
    }

    /**
     * 申请单是否支持更新为FAIL
     * @param applyState 申请单状态
     * @return true：支持/false：不支持
     */
    public static boolean enableApplyFail(AuditApplyStateEnum applyState) {
        return REJECTED == applyState || APPROVED == applyState || CANCELED == applyState;
    }
}
