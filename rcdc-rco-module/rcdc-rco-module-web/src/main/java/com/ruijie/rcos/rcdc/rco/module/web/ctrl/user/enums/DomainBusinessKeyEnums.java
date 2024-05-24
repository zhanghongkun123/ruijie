package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/17
 *
 * @author songxiang
 */
public enum DomainBusinessKeyEnums {

    RCDC_RCO_SAVE_CONFIG_SUCCESS(//
            UserBusinessKey.RCDC_RCO_AD_SAVE_CONFIG_SUCCESS, //
            UserBusinessKey.RCDC_RCO_LDAP_SAVE_CONFIG_SUCCESS//
    ), //
    RCDC_RCO_SAVE_CONFIG_FAIL(//
            UserBusinessKey.RCDC_RCO_AD_SAVE_CONFIG_FAIL, //
            UserBusinessKey.RCDC_RCO_LDAP_SAVE_CONFIG_FAIL//
    ), //
    RCDC_RCO_TEST_CONNECTION_SUCCESS(//
            UserBusinessKey.RCDC_RCO_AD_TEST_CONNECTION_SUCCESS, //
            UserBusinessKey.RCDC_RCO_LDAP_TEST_CONNECTION_SUCCESS//
    ), //
    RCDC_RCO_TEST_CONNECTION_FAIL(//
            UserBusinessKey.RCDC_RCO_AD_TEST_CONNECTION_FAIL, //
            UserBusinessKey.RCDC_RCO_LDAP_TEST_CONNECTION_FAIL//
    ), //
    RCDC_RCO_DISABLE_SUCCESS(//
            UserBusinessKey.RCDC_RCO_AD_DISABLE_SUCCESS, //
            UserBusinessKey.RCDC_RCO_LDAP_DISABLE_SUCCESS//
    ), //
    RCDC_RCO_ENABLE_SUCCESS(//
            UserBusinessKey.RCDC_RCO_AD_ENABLE_SUCCESS, //
            UserBusinessKey.RCDC_RCO_LDAP_ENABLE_SUCCESS//
    ), //
    RCDC_RCO_ENABLE_FAIL(//
            UserBusinessKey.RCDC_RCO_AD_ENABLE_FAIL, //
            UserBusinessKey.RCDC_RCO_LDAP_ENABLE_FAIL), //

    RCDC_RCO_DISABLE_FAIL(//
            UserBusinessKey.RCDC_RCO_AD_DISABLE_FAIL, //
            UserBusinessKey.RCDC_RCO_LDAP_DISABLE_FAIL),

    RCDC_RCO_SAVE_MAPPING_CONFIG_FAIL(//
            UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_CONFIG_FAIL, //
            UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_CONFIG_FAIL//
    ), //
    RCDC_RCO_SAVE_MAPPING_CONFIG_SUCCESS(//
            UserBusinessKey.RCDC_RCO_AD_SAVE_MAPPING_CONFIG_SUCCESS, //
            UserBusinessKey.RCDC_RCO_LDAP_SAVE_MAPPING_CONFIG_SUCCESS//
    ), //
    RCDC_RCO_SYNC_USER_FAIL_BY_EXCEPTION(//
            UserBusinessKey.RCDC_RCO_AD_SYNC_USER_FAIL_BY_EXCEPTION, //
            UserBusinessKey.RCDC_RCO_LDAP_SYNC_USER_FAIL_BY_EXCEPTION//
    ), //
    RCDC_RCO_SYNC_USER_DISABLE_EXCEPTION(//
            UserBusinessKey.RCDC_RCO_AD_SYNC_USER_AD_DISABLE_EXCEPTION, //
            UserBusinessKey.RCDC_RCO_LDAP_SYNC_USER_LDAP_DISABLE_EXCEPTION//
    );//


    private Map<DomainServerType, String> keyMapping;

    DomainBusinessKeyEnums(String adKey, String ldapKey) {
        keyMapping = new ImmutableMap.Builder<DomainServerType, String>()//
                .put(DomainServerType.AD, adKey)//
                .put(DomainServerType.LDAP, ldapKey).build();
    }

    /**
     * 根据域服务器类型获取异常
     *
     * @param serverType 域服务器类型
     * @return 异常
     */
    public String obtainBusinessKeyByServerType(DomainServerType serverType) {
        Assert.notNull(serverType, "serverType is not null");
        return this.keyMapping.get(serverType);
    }

    /**
     * 根据域服务器类型抛出
     *
     * @param serverType 域服务器类型
     * @param args 异常的参数
     * @throws BusinessException 业务异常
     */
    public void throwDomainException(DomainServerType serverType, String... args) throws BusinessException {
        Assert.notNull(serverType, "serverType must not null");
        Assert.notNull(args, "args must not null");
        throw new BusinessException(this.keyMapping.get(serverType), args);
    }
}
