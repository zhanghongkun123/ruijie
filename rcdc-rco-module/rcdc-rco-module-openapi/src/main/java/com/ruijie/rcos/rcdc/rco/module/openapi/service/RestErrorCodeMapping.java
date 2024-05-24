package com.ruijie.rcos.rcdc.rco.module.openapi.service;

import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.terminal.module.def.PublicBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_STATE_ERROR;
import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.*;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public class RestErrorCodeMapping {

    private static final ConcurrentMap<String, String> CODE_MAP = Maps.newConcurrentMap();

    private static final List<String> WHITE_LIST = Arrays.asList("98010001", "99020002");

    // fixme 后续需要抽出 key
    static {
        // SK 错误
        CODE_MAP.put("sk-resource_locked", RestErrorCode.RCDC_CODE_SYSTEM_BUSY);
        // CDC 错误
        CODE_MAP.put("RCDC_CLOUDDESKTOP_OS_FILENAME_EXISTS", RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        CODE_MAP.put("RCDC_TERMINALGROUP_GROUP_LEVEL_EXCEED_LIMIT", RestErrorCode.RCDC_RCO_TERMINALGROUP_GROUP_LEVEL_EXCEED_LIMIT);
        CODE_MAP.put(BusinessKey.RCDC_RCO_WIFI_GT_FOUR, RestErrorCode.RCDC_CODE_TERMINAL_SSID_SUM_ERROR);
        CODE_MAP.put(BusinessKey.RCDC_RCO_WIFI_DUPLICATE, RestErrorCode.RCDC_RCO_WIFI_DUPLICATE);
        CODE_MAP.put(BusinessKey.RCDC_USER_TERMINAL_IDV_TERMINAL_GROUP_CONFIG_NOT_COMPLETED, RestErrorCode.RCDC_RCO_TERMINAL_PARAMETER_ERROR);
        CODE_MAP.put(BusinessKey.RCDC_USER_CLOUDDESKTOP_IDV_IMAGE_TEMPLATE_NOT_FOUND, RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND);
        CODE_MAP.put(PublicBusinessKey.RCDC_TERMINALGROUP_GROUP_NAME_DUPLICATE, RestErrorCode.RCDC_RCO_TERMINALGROUP_GROUP_NAME_DUPLICATE);
        CODE_MAP.put(PublicBusinessKey.RCDC_TERMINALGROUP_SUB_GROUP_NUM_EXCEED_LIMIT, RestErrorCode.RCDC_OPENAPI_TERMINAL_SUB_GROUP_EXCEED_LIMIT);
        CODE_MAP.put(PublicBusinessKey.RCDC_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT, RestErrorCode.RCDC_OPENAPI_TERMINALGROUP_GROUP_NUM_EXCEED_LIMIT);
        CODE_MAP.put(PublicBusinessKey.RCDC_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP,
                RestErrorCode.RCDC_OPENAPI_DELETE_TERMINAL_GROUP_SUB_GROUP_HAS_DUPLICATION_WITH_MOVE_GROUP);
        CODE_MAP.put("23260056",
                RestErrorCode.RCDC_OPENAPI_TERMINALGROUP_GROUP_PARENT_CAN_NOT_SELECT_ITSELF_OR_SUB);
        CODE_MAP.put("23250179", RestErrorCode.RCDC_CODE_FILE_NOT_EXISTS);
        CODE_MAP.put("23250185", RestErrorCode.RCDC_CODE_OS_FILE_STATE_UNAVAILABLE);
        CODE_MAP.put("23250250", RestErrorCode.RCDC_CODE_CONFIG_IMAGE_VALIDATION);
        CODE_MAP.put("23250245", RestErrorCode.RCDC_CODE_VM_DISK_SIZE_LARGE_THAN_BEFORE);
        CODE_MAP.put("23250036", RestErrorCode.RCDC_CODE_NETWORK_NOT_EXIST);
        CODE_MAP.put("23250037", RestErrorCode.RCDC_CODE_NETWORK_UNAVAILABLE);
        CODE_MAP.put("23250255", RestErrorCode.RCDC_CODE_PUBLISH_IMAGE_VALIDATION);
        CODE_MAP.put("23250263", RestErrorCode.RCDC_CODE_PUBLISH_BUSINESS_VALIDATION);
        CODE_MAP.put("23250223", RestErrorCode.RCDC_CODE_GUESTTOOL_STAGE_ERROR);
        CODE_MAP.put("23250222", RestErrorCode.RCDC_CODE_IDV_IMAGE_TEMPLATE_NO_DRIVER);
        CODE_MAP.put("23250014", RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_ALREADY_EXIST);
        CODE_MAP.put("23250550", RestErrorCode.RCDC_CODE_IMAGE_DISK_TYPE_NOT_MATCH_OSFILE_TYPE);
        CODE_MAP.put("23250307", RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_SIZE_SMALL_THAN_OS_FILE);
        CODE_MAP.put("23250008", RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NAME_EXISTS);
        CODE_MAP.put("23200509", RestErrorCode.RCDC_RCO_CLOUDDESKTOP_STRATEGY_SYSTEM_DISK_OUT_RANGE);
        CODE_MAP.put("23260057", RestErrorCode.RCDC_RCO_TERMINAL_USERGROUP_NOT_ALLOW_RESERVE_NAME);
        CODE_MAP.put("23250203", RestErrorCode.RCDC_CODE_NETWORK_NAME_EXIST);
        CODE_MAP.put("23250178", RestErrorCode.RCDC_CODE_FILMD5_EXISTS);
        CODE_MAP.put("23250270", RestErrorCode.RCDC_CODE_IMAGE_NOT_EXIST);
        CODE_MAP.put("23250188", RestErrorCode.RCDC_CODE_FILE_SIZE_ERROR);
        CODE_MAP.put(RCDC_RCO_APPCENTER_TEST_DESKTOP_STATE_ERROR, RCDC_OPEN_API_REST_APPCENTER_TEST_DESKTOP_STATE_ERROR);
        CODE_MAP.put("23200611", RCDC_OPEN_API_REST_IMAGE_NOT_SUPPORT_GPU);
        CODE_MAP.put("23200612", RCDC_OPEN_API_REST_IMAGE_NOT_SUPPORT_GPU_MODEL);
        CODE_MAP.put("23200049", RCDC_OPEN_API_REST_CLUSTER_NOT_EXIST_GPU_RESOURCES);
        CODE_MAP.put("23200610", RCDC_OPEN_API_REST_IMAGE_NOT_EXIST_GPU_DRIVER);
        CODE_MAP.put("23200026", RCDC_OPEN_API_USER_PRIMARY_CERTIFICATION_CONFIG_FAIL);
        CODE_MAP.put(RCDC_OPEN_API_NOT_OPEN_CAS_FAIL_RESULT, RCDC_OPEN_API_NOT_OPEN_CAS_FAIL_RESULT);
        CODE_MAP.put(RCDC_OPEN_API_NOT_OPEN_OTP_FAIL_RESULT, RCDC_OPEN_API_NOT_OPEN_OTP_FAIL_RESULT);
        CODE_MAP.put(RCDC_OPEN_API_NOT_HARDWARE_FAIL_RESULT, RCDC_OPEN_API_NOT_HARDWARE_FAIL_RESULT);
        CODE_MAP.put(RCDC_OPEN_API_NOT_OPEN_SMS_AUTH_FAIL_RESULT, RCDC_OPEN_API_NOT_OPEN_SMS_AUTH_FAIL_RESULT);
        CODE_MAP.put(RCDC_OPEN_API_USER_TYPE_NOT_ALLOW_CHANGE, RCDC_OPEN_API_USER_TYPE_NOT_ALLOW_CHANGE);
        CODE_MAP.put(RCDC_OPEN_API_USER_NAME_NOT_ALLOW_CHANGE, RCDC_OPEN_API_USER_NAME_NOT_ALLOW_CHANGE);
        CODE_MAP.put("23200089", RCDC_OPEN_API_USER_INVALID_TIME_VALIDATE_ERROR);
        CODE_MAP.put("23200090", RCDC_OPEN_API_USER_DESCRIPTION_INVALIDATE_ERROR);


        // 用户、用户组
        CODE_MAP.put("rcdc_user_not_exist", RestErrorCode.OPEN_API_USER_NOT_EXISTS);
        CODE_MAP.put("rcdc_user_usergroup_not_exist", RestErrorCode.RCDC_OPEN_API_REST_USER_GROUP_NOT_EXIST);
        CODE_MAP.put("23200537", RestErrorCode.RCDC_USER_USERGROUP_NUM_OVER);
        CODE_MAP.put("23200540", RestErrorCode.RCDC_USER_SUB_USERGROUP_NUM_OVER);
        CODE_MAP.put("rcdc_user_usergroup_hierarchy_over", RestErrorCode.RCDC_USER_USERGROUP_HIERARCHY_OVER);
        CODE_MAP.put("23200541", RestErrorCode.RCDC_USER_USERGROUP_HAS_DUPLICATION_NAME);
        CODE_MAP.put("rcdc_user_usergroup_not_allow_reserve_name", RestErrorCode.RCDC_USER_USERGROUP_NOT_ALLOW_RESERVE_NAME);
        CODE_MAP.put("rcdc_user_name_has_exist", RestErrorCode.RCDC_USER_USER_NAME_HAS_EXIST);
        CODE_MAP.put(RCDC_OPEN_API_REST_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT,
                RCDC_OPEN_API_REST_DESKTOP_EDIT_REMARK_DYNAMIC_POOL_NOT_SUPPORT);
        CODE_MAP.put(RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, RestErrorCode.OPEN_API_DESK_NOT_EXISTS);
        CODE_MAP.put("23200088", RCDC_OPEN_API_USER_ACCOUNT_EXPIRE_TIME_ERROR);

        // 桌面策略
        CODE_MAP.put("23250079", RestErrorCode.RCDC_CLOUDDESKTOP_DESK_STRATEGY_NAME_REPEATED);
        CODE_MAP.put("23250086", RestErrorCode.RCDC_CLOUDDESKTOP_DESK_LOCAL_MODE_STRATEGY_NOT_LEGAL);

        CODE_MAP.put("23200503", RestErrorCode.RCDC_RCO_STRATEGY_IDV_NOT_FOUND);
        CODE_MAP.put("23200505", RestErrorCode.RCDC_RCO_IMAGE_TEMPLATE_NOT_FOUND);
        CODE_MAP.put("23250131", RestErrorCode.RCDC_RCO_TERMINAL_DESKINFO_EXIST);
        CODE_MAP.put("23250114",
                RestErrorCode.RCDC_RCO_USBDEVICE_FIRM_AND_PRODUCT_ALL_UNKNOW_FORBID);
        CODE_MAP.put("23200365", RestErrorCode.RCDC_CODE_ONLY_ONE_IMAGE_ALLOWED_TO_RUN);
        CODE_MAP.put(RCDC_OPEN_API_REST_DESKTOP_EDIT_STRATEGY_CONFLICT_WITH_UPM, RCDC_OPEN_API_REST_DESKTOP_EDIT_STRATEGY_CONFLICT_WITH_UPM);
        CODE_MAP.put(RCDC_RCO_DESKTOP_EDIT_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT, RCDC_RCO_DESKTOP_EDIT_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT);
        CODE_MAP.put(RCDC_RCO_STRATEGY_NOT_FOUND, RCDC_RCO_STRATEGY_NOT_FOUND);

        // 迁移准备
        CODE_MAP.put("base_sys_manage_samba_not_config", RestErrorCode.RCDC_OPEN_API_REST_CHECK_SAMBA_NOT_CONFIG);
    }

    /**
     * 映射公共业务错误
     *
     * @param key 业务异常
     * @return 返回异常码
     */
    public static String convert(String key) {
        Assert.notNull(key, "key must not be null");
        return convert(key, RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
    }

    /**
     * 映射公共业务错误
     *
     * @param ex 业务异常
     * @return 返回异常码
     */
    public static BusinessException convert(Exception ex) {
        Assert.notNull(ex, "ex must not be null");

        if ((ex instanceof BusinessException)) {
            BusinessException businessException = (BusinessException) ex;
            return new BusinessException(RestErrorCodeMapping.convert(businessException.getKey()), businessException.getArgArr());
        } else {
            return new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        }
    }

    /**
     * 转换业务异常
     *
     * @param throwable 异常
     * @return 业务异常
     */
    public static BusinessException convert2BusinessException(Throwable throwable) {

        Assert.notNull(throwable, "throwable must not be null");

        return convert2BusinessException(throwable, RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
    }

    /**
     * 转换业务异常
     *
     * @param throwable 异常
     * @param defaultErrorCode 默认异常码
     * @return 业务异常
     */
    public static BusinessException convert2BusinessException(Throwable throwable, String defaultErrorCode) {
        Assert.notNull(throwable, "throwable must not be null");
        Assert.notNull(defaultErrorCode, "defaultErrorCode can not be null");
        if (throwable instanceof BusinessException) {
            BusinessException businessException = (BusinessException) throwable;
            String errorCode = convert(businessException.getKey(), defaultErrorCode);
            String[] argArr = Optional.ofNullable(businessException.getArgArr()).orElse(new String[0]);
            if (RestErrorCode.RCDC_CODE_NORMAL_BUSINESS_EXCEPTION.equals(errorCode)) {
                argArr = new String[] {businessException.getI18nMessage()};
            }
            return new BusinessException(errorCode, throwable, argArr);
        }
        return new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, throwable);
    }

    private static String convert(String key, String defaultErrorCode) {
        Assert.notNull(key, "key must not be null");
        Assert.notNull(defaultErrorCode, "defaultErrorCode can not be null");
        if (WHITE_LIST.contains(key)) {
            return key;
        }
        boolean enable = Arrays.stream(RestErrorCode.class.getDeclaredFields()).anyMatch(field -> {
            try {
                return field.get(Field.class).equals(key);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                return false;
            }
        });

        if (enable) {
            return key;
        } else {
            return Optional.ofNullable(CODE_MAP.get(key)).orElse(defaultErrorCode);
        }
    }
}
