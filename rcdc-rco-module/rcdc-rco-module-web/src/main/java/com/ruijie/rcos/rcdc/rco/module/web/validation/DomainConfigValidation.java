package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainBaseDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLdapConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdCoverTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.DomainConfigBaseWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.SaveDomainMappingConfigWebRequest;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/10
 *
 * @author songxiang
 */
@Service
public class DomainConfigValidation {


    private static final ImmutableList FORBIDDEN_KEY_WORD_LIST = ImmutableList.<String>of("OU");

    private static final ImmutableList OBJECT_CLASS_FORBIDDEN_KEY_WORD_LIST = ImmutableList.<String>of("ORGANIZATIONALUNIT");

    private static final int MAX_PASSWORD_LENGTH = 64;

    /**
     * 验证保存域映射的请求
     *
     * 
     * @param saveDomainMappingConfigWebRequest 域保存请求
     * @throws AnnotationValidationException 注解验证异常
     * @throws BusinessException 业务异常
     */
    public void validSaveDomainMappingRequest(SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest)
            throws AnnotationValidationException, BusinessException {

        Assert.notNull(saveDomainMappingConfigWebRequest, "saveDomainMappingConfigWebRequest must not null");

        validADBaseRequest(saveDomainMappingConfigWebRequest);
        validLDAPBaseRequest(saveDomainMappingConfigWebRequest);

        // LDAP覆盖类型不能为AD
        if (saveDomainMappingConfigWebRequest.getServerType() == DomainServerType.LDAP
                && saveDomainMappingConfigWebRequest.getCoverType() == IacAdCoverTypeEnum.AD) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_AD_COVER_ERROR);
        }

        if (saveDomainMappingConfigWebRequest.getServerType() == DomainServerType.AD) {
            // AD覆盖类型不能为LDAP
            if (saveDomainMappingConfigWebRequest.getCoverType() == IacAdCoverTypeEnum.LDAP) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_LDAP_COVER_ERROR);
            }
            // AD域不能没有加域参数:
            if (ObjectUtils.isEmpty(saveDomainMappingConfigWebRequest.getAutoJoin())) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_AD_AUTO_JOIN_NULL);
            }
        }
    }

    /**
     * 验证 DomainConfigBaseWebRequest 请求
     *
     * @param baseWebRequest 请求
     * @throws AnnotationValidationException 注解验证异常
     * @throws BusinessException 业务异常
     */
    public void validDomainBaseRequest(DomainConfigBaseWebRequest baseWebRequest) throws AnnotationValidationException, BusinessException {

        Assert.notNull(baseWebRequest, "baseWebRequest must not null");

        validADBaseRequest(baseWebRequest);
        validLDAPBaseRequest(baseWebRequest);
    }

    /**
     * 验证DomainConfigBaseWebRequest请求
     * 
     * @param baseWebRequest 请求
     * @throws AnnotationValidationException 注解验证异常
     * @throws BusinessException 业务异常
     */
    public void validLDAPBaseRequest(DomainConfigBaseWebRequest baseWebRequest) throws AnnotationValidationException, BusinessException {

        Assert.notNull(baseWebRequest, "baseWebRequest must not null");

        if (baseWebRequest.getServerType() != DomainServerType.LDAP) {
            return;
        }
        validManagerPasswordLength(baseWebRequest);
        validLdap(baseWebRequest);

        // LDAP的时候，用户、邮箱、电话属性名不能为以下关键字:
        List<String> toBeCheckList = Lists.newArrayList();

        if (baseWebRequest.getUsernameAttr() != null) {
            toBeCheckList.add(baseWebRequest.getUsernameAttr());
        }
        if (baseWebRequest.getMobileAttr() != null) {
            baseWebRequest.setMobileAttr(baseWebRequest.getMobileAttr().trim());
            toBeCheckList.add(baseWebRequest.getMobileAttr());
        }
        if (baseWebRequest.getMailAttr() != null) {
            baseWebRequest.setMailAttr(baseWebRequest.getMailAttr().trim());
            toBeCheckList.add(baseWebRequest.getMailAttr());
        }

        for (String tobeCheckStr : toBeCheckList) {
            if (FORBIDDEN_KEY_WORD_LIST.contains(tobeCheckStr.toUpperCase())) {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_LDAP_ATTR_NAME_CAN_NOT_CONTAIN_KEY_WORD, tobeCheckStr);
            }
        }

        // objectClass不能包含关键字：
        if (baseWebRequest.getObjectClass() != null && OBJECT_CLASS_FORBIDDEN_KEY_WORD_LIST.contains(baseWebRequest.getObjectClass().toUpperCase())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_LDAP_ATTR_NAME_CAN_NOT_CONTAIN_KEY_WORD, baseWebRequest.getObjectClass());
        }

        // ldap 非匿名访问情况下，必须填写用户和密码
        if (!baseWebRequest.getEnableAnonymous()
                && (!StringUtils.hasText(baseWebRequest.getManagerName()) || !StringUtils.hasText(baseWebRequest.getManagerPassword()))) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_LDAP_NON_ANONYMOUS_USER_AND_PASSWORD_MUST_NOT_BE_NULLABLE);
        }
    }

    /**
     * 验证DomainConfigBaseWebRequest请求
     *
     * @param baseWebRequest 请求
     * @throws AnnotationValidationException 注解验证异常
     */
    private void validADBaseRequest(DomainConfigBaseWebRequest baseWebRequest) throws AnnotationValidationException, BusinessException {
        Assert.notNull(baseWebRequest, "baseWebRequest must not null");

        if (baseWebRequest.getServerType() != DomainServerType.AD) {
            return;
        }
        validManagerPasswordLength(baseWebRequest);
        validAd(baseWebRequest);
    }

    private void validLdap(DomainConfigBaseWebRequest request) throws AnnotationValidationException {
        IacLdapConfigDTO ldapConfigDTO = new IacLdapConfigDTO();
        BeanUtils.copyProperties(request, ldapConfigDTO);
        BeanValidationUtil.validateBean(IacDomainBaseDTO.class, ldapConfigDTO);
        BeanValidationUtil.validateBean(IacLdapConfigDTO.class, ldapConfigDTO);
    }

    private void validAd(DomainConfigBaseWebRequest request) throws AnnotationValidationException {
        IacAdConfigDTO adConfigDTO = new IacAdConfigDTO();
        BeanUtils.copyProperties(request, adConfigDTO);
        BeanValidationUtil.validateBean(IacDomainBaseDTO.class, adConfigDTO);
        BeanValidationUtil.validateBean(IacAdConfigDTO.class, adConfigDTO);
    }

    private void validManagerPasswordLength(DomainConfigBaseWebRequest baseWebRequest) throws BusinessException {
        if (!StringUtils.hasText(baseWebRequest.getManagerPassword())) {
            return;
        }
        String password = AesUtil.descrypt(baseWebRequest.getManagerPassword(), RedLineUtil.getRealAdminRedLine());
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_AD_MANAGER_PASSWORD_LENGTH);
        }
    }
}
