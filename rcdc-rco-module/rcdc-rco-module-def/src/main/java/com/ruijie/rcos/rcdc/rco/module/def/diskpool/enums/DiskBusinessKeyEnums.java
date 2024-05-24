package com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

/**
 * Description: 磁盘状态
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/9
 *
 * @author TD
 */
public enum DiskBusinessKeyEnums {
    /**
     * 可用
     */
    ACTIVE(BusinessKey.RCDC_RCO_DISK_STATE_ACTIVE),

    /**
     * 创建中
     */
    CREATING(BusinessKey.RCDC_RCO_DISK_STATE_CREATING),

    /**
     * 磁盘挂载中
     */
    ATTACHING(BusinessKey.RCDC_RCO_DISK_STATE_ATTACHING),

    /**
     * 扩容中
     */
    EXPANDING(BusinessKey.RCDC_RCO_DISK_STATE_EXPANDING),

    /**
     * 磁盘卸载中
     */
    DETACHING(BusinessKey.RCDC_RCO_DISK_STATE_DETACHING),

    /**
     * 删除中
     */
    DELETING(BusinessKey.RCDC_RCO_DISK_STATE_DELETING),

    /**
     * 磁盘已删除
     */
    DELETED(BusinessKey.RCDC_RCO_DISK_STATE_DELETED),

    /**
     * 还原中
     */
    RESETTING(BusinessKey.RCDC_RCO_DISK_STATE_RESETTING),

    /**
     * 快照创建中
     */
    SNAPSHOT_CREATING(BusinessKey.RCDC_RCO_DISK_STATE_SNAPSHOT_CREATING),

    /**
     * 快照恢复中
     */
    SNAPSHOT_RESTORING(BusinessKey.RCDC_RCO_DISK_STATE_SNAPSHOT_RESTORING),

    /**
     * 备份创建中
     */
    BACKUP_CREATING(BusinessKey.RCDC_RCO_DISK_STATE_BACKUP_CREATING),

    /**
     * 备份恢复中
     */
    BACKUP_RESTORING(BusinessKey.RCDC_RCO_DISK_STATE_BACKUP_RESTORING),

    /**
     * 使用中
     */
    IN_USE(BusinessKey.RCDC_RCO_DISK_STATE_IN_USE),

    /**
     * 禁用
     */
    DISABLE(BusinessKey.RCDC_RCO_DISK_STATE_DISABLE),

    /**
     * 故障状态
     */
    ERROR(BusinessKey.RCDC_RCO_DISK_STATE_ERROR),

    /**
     * 编辑中
     */
    UPDATING(BusinessKey.RCDC_RCO_DISK_STATE_UPDATING),

    /**
     * 可用
     */
    AVAILABLE(BusinessKey.RCDC_RCO_DISK_STATE_ACTIVE);

    private final String stateKey;

    DiskBusinessKeyEnums(String stateKey) {
        this.stateKey = stateKey;
    }

    /**
     * 获取对应状态的国际化
     * @param state 状态
     * @return 国际化内容
     * @throws BusinessException 业务异常
     */
    public static String obtainResolve(String state) throws BusinessException {
        Assert.notNull(state, "obtainResolve state can be not null");
        for (DiskBusinessKeyEnums keyEnum : DiskBusinessKeyEnums.values()) {
            if (keyEnum.name().equals(state)) {
                return LocaleI18nResolver.resolve(keyEnum.stateKey);
            }
        }
        throw new BusinessException(BusinessKey.RCDC_RCO_DISK_STATE_UNDEFINED, state);
    }
}
