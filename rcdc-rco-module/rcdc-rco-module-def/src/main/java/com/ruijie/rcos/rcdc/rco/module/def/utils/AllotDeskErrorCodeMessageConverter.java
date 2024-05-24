package com.ruijie.rcos.rcdc.rco.module.def.utils;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.AllotDesktopPoolFaultTypeEnum;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 将分配桌面桌面给用户失败的错误码转换成错误类型和提示语
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/24 15:46
 *
 * @author yxq
 */
public class AllotDeskErrorCodeMessageConverter {

    private AllotDeskErrorCodeMessageConverter() {
        throw new IllegalStateException("Util class AllotDeskErrorCodeMessageConverter should not use constructor");
    }

    /**
     * 错误码映射失败类型枚举
     */
    private static final Map<Integer, AllotDesktopPoolFaultTypeEnum> ERROR_CODE_TO_FAULT_TYPE = new HashMap<>();

    /**
     * 错误码映射失败原因
     */
    private static final Map<Integer, String> ERROR_CODE_TO_FAULT_DESC = new HashMap<>();

    static {
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.NO_AVAILABLE_DESKTOP, AllotDesktopPoolFaultTypeEnum.NOT_ENOUGH_RESOURCE);
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.DESKTOP_POOL_ASSIGN_ERROR, AllotDesktopPoolFaultTypeEnum.NOT_ENOUGH_RESOURCE);
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.DESKTOP_UNDER_MAINTENANCE, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT);
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.USER_NOT_ASSIGN_POOL, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT);
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.DESKTOP_POOL_STATUS_ERROR, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT);
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.DESKTOP_POOL_IMAGE_STATUS_ERROR, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT);
        ERROR_CODE_TO_FAULT_TYPE.put(DesktopPoolConstants.DESKTOP_USER_DISK_STATUS_ERROR, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT);

        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.NO_AVAILABLE_DESKTOP, BusinessKey.RCDC_RCO_DESKTOP_POOL_NOT_DESKTOP_IN_POOL);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.DESKTOP_POOL_ASSIGN_ERROR,
                BusinessKey.RCDC_RCO_DESKTOP_POOL_OTHER_ALLOCATE_EXCEPTION);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.DESKTOP_UNDER_MAINTENANCE, BusinessKey.RCDC_RCO_DESKTOP_POOL_UNDER_MAINTENANCE);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.USER_NOT_ASSIGN_POOL, BusinessKey.RCDC_RCO_DESKTOP_POOL_USER_NOT_ALLOCATE_POOL);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.DESKTOP_POOL_STATUS_ERROR,
                BusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_POOL_STATUS_ERROR);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.DESKTOP_POOL_IMAGE_STATUS_ERROR,
                BusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_POOL_IMAGE_STATUS_ERROR);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.DESKTOP_USER_DISK_STATUS_ERROR,
                BusinessKey.RCDC_RCO_DESKTOP_USER_DISK_NOT_READY);
        ERROR_CODE_TO_FAULT_DESC.put(DesktopPoolConstants.DESKTOP_LOGIN_TIME_LIMIT,
                BusinessKey.RCDC_RCO_DESKTOP_POOL_LOGIN_TIME_LIMIT);
    }

    /**
     * 根据错误码，获取失败类型
     *
     * @param errorCode 错误码
     * @return 失败类型
     */
    public static AllotDesktopPoolFaultTypeEnum getAllocateFaultTypeByErrorCode(int errorCode) {
        return ERROR_CODE_TO_FAULT_TYPE.getOrDefault(errorCode, AllotDesktopPoolFaultTypeEnum.OTHER_FAULT);
    }

    /**
     * 根据错误码，获取国际化失败原因
     *
     * @param errorCode 错误码
     * @return 失败原因
     */
    public static String getAllocateFaultDescByErrorCode(int errorCode) {
        return LocaleI18nResolver.resolve(ERROR_CODE_TO_FAULT_DESC.getOrDefault(errorCode,
                BusinessKey.RCDC_RCO_DESKTOP_POOL_OTHER_ALLOCATE_EXCEPTION));
    }
}
