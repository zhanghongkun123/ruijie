package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdCoverTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DomainServerType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.SaveDomainMappingConfigWebRequest;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import mockit.Tested;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/10
 *
 * @author songxiang
 */
public class DomainConfigValidationTest {

    @Tested
    private DomainConfigValidation domainConfigValidation;


    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws BusinessException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequest() throws AnnotationValidationException, BusinessException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.LDAP);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        Assert.assertTrue(true);
    }

    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws BusinessException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhenContainOu() throws AnnotationValidationException, BusinessException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.LDAP);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("OU");
        try {
            domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_LDAP_ATTR_NAME_CAN_NOT_CONTAIN_KEY_WORD);
        }
    }

    /**
     * 测试2
     *
     * @throws AnnotationValidationException 异常
     * @throws BusinessException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhenContainObjectClass()
            throws AnnotationValidationException, BusinessException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.LDAP);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("abc");
        saveDomainMappingConfigWebRequest.setObjectClass("organizationUnit");
        try {
            domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_LDAP_ATTR_NAME_CAN_NOT_CONTAIN_KEY_WORD);
        }
    }

    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws BusinessException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhenAdCoverLdap() throws AnnotationValidationException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.AD);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("OU");
        saveDomainMappingConfigWebRequest.setCoverType(IacAdCoverTypeEnum.LDAP);
        try {
            domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_LDAP_COVER_ERROR);
        }
    }

    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws BusinessException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhenLdapCoverAD() throws AnnotationValidationException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.LDAP);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("111");
        saveDomainMappingConfigWebRequest.setCoverType(IacAdCoverTypeEnum.AD);
        try {
            domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_AD_COVER_ERROR);
        }
    }

    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhenAd() throws AnnotationValidationException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.AD);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("333");
        saveDomainMappingConfigWebRequest.setCoverType(IacAdCoverTypeEnum.AD);
        try {
            domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_LDAP_COVER_ERROR);
        }
    }

    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws IllegalAccessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhen() throws AnnotationValidationException, IllegalAccessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.AD);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("OU");
        saveDomainMappingConfigWebRequest.setCoverType(IacAdCoverTypeEnum.AD);
        saveDomainMappingConfigWebRequest.setAutoJoin(null);
        try {
            domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        } catch (BusinessException e) {
            Assert.assertEquals(e.getKey(), UserBusinessKey.RCDC_RCO_AD_AUTO_JOIN_NULL);
        }

    }

    /**
     * 测试
     * 
     * @throws AnnotationValidationException 异常
     * @throws IllegalAccessException 异常
     * @throws BusinessException 异常
     */
    @Test
    public void validSaveDomainMappingRequestWhenNull() throws AnnotationValidationException, IllegalAccessException, BusinessException {
        SaveDomainMappingConfigWebRequest saveDomainMappingConfigWebRequest = new SaveDomainMappingConfigWebRequest();
        generateDataByReflect(saveDomainMappingConfigWebRequest);
        saveDomainMappingConfigWebRequest.setServerIp("192.168.1.10");
        saveDomainMappingConfigWebRequest.setServerType(DomainServerType.LDAP);
        saveDomainMappingConfigWebRequest.setServerPort("12345");
        saveDomainMappingConfigWebRequest.setUsernameAttr("34324");
        saveDomainMappingConfigWebRequest.setCoverType(IacAdCoverTypeEnum.LDAP);
        saveDomainMappingConfigWebRequest.setAutoJoin(null);
        saveDomainMappingConfigWebRequest.setMailAttr(null);
        saveDomainMappingConfigWebRequest.setMobileAttr(null);
        domainConfigValidation.validSaveDomainMappingRequest(saveDomainMappingConfigWebRequest);
        Assert.assertTrue(true);
    }

    private void generateDataByReflect(Object object) throws IllegalAccessException {
        Class clazz = object.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Random random = new Random();
        Field[] fieldArr = new Field[fieldList.size()];
        fieldList.toArray(fieldArr);
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (field.getType().isEnum()) {
                Object objectArr[] = field.getType().getEnumConstants();
                field.set(object, objectArr[0]);
                continue;
            }
            if (field.getType().getName().equals(UUID.class.getName())) {
                field.set(object, UUID.randomUUID());
                continue;
            }
            if (field.getType().getName().equals(Integer.class.getName())) {
                field.set(object, new Integer(10));
                continue;
            }
            if (field.getType().getName().equals("int")) {
                field.setInt(object, 10);
                continue;
            }
            if (field.getType().getName().equals("long")) {
                field.setLong(object, 123L);
                continue;
            }
            if (field.getType().getName().equals(String.class.getName())) {

                field.set(object, "abc123" + String.valueOf(random.nextInt(1024)));
                continue;
            }
            if (field.getType().getName().equals(Boolean.class.getName())) {
                field.set(object, true);
                continue;
            }
            if (field.getType().getName().equals("boolean")) {
                field.set(object, true);
                continue;
            }
        }
    }
}
