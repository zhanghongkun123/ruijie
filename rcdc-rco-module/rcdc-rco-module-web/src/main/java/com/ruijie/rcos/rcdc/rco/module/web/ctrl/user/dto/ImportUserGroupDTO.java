package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacImportUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/23
 *
 * @author zhangyichi
 */
public class ImportUserGroupDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportUserGroupDTO.class);

    private static final String PERSIST_NOT_EXPIRE = "永不过期";

    private static final String PERSIST_NOT_INVALID = "永不失效";


    private static final String EXPIRE_LABEL = "0";

    private Integer rowNum;

    private String groupNames;

    private String vdiImageTemplateName;

    private String vdiStrategyName;

    private String vdiNetworkName;

    private String idvImageTemplateName;

    private String idvStrategyName;

    private String voiImageTemplateName;

    private String vdiStoragePoolName;

    private String vdiClusterName;

    private String voiStrategyName;

    private String accountExpireDate;

    private String invalidTime;

    private String cloudPlatformName;

    private String vdiCpu;

    private String vdiMemory;

    private String vdiSystemSize;

    private String vdiPersonSize;

    private String vdiPersonDiskStoragePoolName;

    private String vdiVgpuModel;

    /**
     * 参数转换
     *
     * @param dto request
     * @return cbbImportUserGroupDTO
     * @throws BusinessException 业务异常
     */
    public static IacImportUserGroupDTO convertFor(ImportUserGroupDTO dto) throws BusinessException {
        Assert.notNull(dto, "dto cannot null");
        IacImportUserGroupDTO request = new IacImportUserGroupDTO();
        request.setGroupNames(dto.groupNames);
        request.setImageTemplateName(dto.vdiImageTemplateName);
        request.setNetworkName(dto.vdiNetworkName);
        request.setStoragePoolName(dto.vdiStoragePoolName);
        request.setStrategyName(dto.vdiStrategyName);
        request.setIdvImageTemplateName(dto.idvImageTemplateName);
        request.setIdvStrategyName(dto.idvStrategyName);
        request.setInvalidTime(convertInvalidTime(dto.invalidTime));
        Long expireDate = convertExpireDate(dto.getAccountExpireDate());
        request.setAccountExpireDate(expireDate);
        return request;
    }

    private static Long convertExpireDate(String expireDate) throws BusinessException {
        if (ObjectUtils.isEmpty(expireDate) || expireDate == PERSIST_NOT_EXPIRE || expireDate == EXPIRE_LABEL) {
            return 0L;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(expireDate).getTime();
        } catch (ParseException e) {
            LOGGER.error("输入的日期[{}]格式解析错误", expireDate);
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_IMPORT_RESOLVE_ERROR, e, expireDate);
        }
    }

    private static Integer convertInvalidTime(String invalidTime) throws BusinessException {
        if (ObjectUtils.isEmpty(invalidTime) || invalidTime == PERSIST_NOT_INVALID || invalidTime == EXPIRE_LABEL) {
            return 0;
        }
        try {
            return Integer.parseInt(invalidTime);
        } catch (NumberFormatException e) {
            LOGGER.error("输入的日期[{}]格式解析错误", invalidTime);
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_GROUP_IMPORT_RESOLVE_FAIL, e, invalidTime);
        }
    }

    public String getVoiImageTemplateName() {
        return voiImageTemplateName;
    }

    public void setVoiImageTemplateName(String voiImageTemplateName) {
        this.voiImageTemplateName = voiImageTemplateName;
    }

    public String getVoiStrategyName() {
        return voiStrategyName;
    }

    public void setVoiStrategyName(String voiStrategyName) {
        this.voiStrategyName = voiStrategyName;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getGroupNames() {
        return groupNames;
    }

    public void setGroupNames(String groupNames) {
        this.groupNames = groupNames;
    }

    public String getVdiImageTemplateName() {
        return vdiImageTemplateName;
    }

    public void setVdiImageTemplateName(String vdiImageTemplateName) {
        this.vdiImageTemplateName = vdiImageTemplateName;
    }

    public String getVdiStrategyName() {
        return vdiStrategyName;
    }

    public void setVdiStrategyName(String vdiStrategyName) {
        this.vdiStrategyName = vdiStrategyName;
    }

    public String getVdiNetworkName() {
        return vdiNetworkName;
    }

    public void setVdiNetworkName(String vdiNetworkName) {
        this.vdiNetworkName = vdiNetworkName;
    }

    public String getIdvImageTemplateName() {
        return idvImageTemplateName;
    }

    public void setIdvImageTemplateName(String idvImageTemplateName) {
        this.idvImageTemplateName = idvImageTemplateName;
    }

    public String getIdvStrategyName() {
        return idvStrategyName;
    }

    public void setIdvStrategyName(String idvStrategyName) {
        this.idvStrategyName = idvStrategyName;
    }

    public String getVdiStoragePoolName() {
        return vdiStoragePoolName;
    }

    public void setVdiStoragePoolName(String vdiStoragePoolName) {
        this.vdiStoragePoolName = vdiStoragePoolName;
    }

    public String getVdiClusterName() {
        return vdiClusterName;
    }

    public void setVdiClusterName(String vdiClusterName) {
        this.vdiClusterName = vdiClusterName;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getCloudPlatformName() {
        return cloudPlatformName;
    }

    public void setCloudPlatformName(String cloudPlatformName) {
        this.cloudPlatformName = cloudPlatformName;
    }

    public String getVdiCpu() {
        return vdiCpu;
    }

    public void setVdiCpu(String vdiCpu) {
        this.vdiCpu = vdiCpu;
    }

    public String getVdiMemory() {
        return vdiMemory;
    }

    public void setVdiMemory(String vdiMemory) {
        this.vdiMemory = vdiMemory;
    }

    public String getVdiSystemSize() {
        return vdiSystemSize;
    }

    public void setVdiSystemSize(String vdiSystemSize) {
        this.vdiSystemSize = vdiSystemSize;
    }

    public String getVdiPersonSize() {
        return vdiPersonSize;
    }

    public void setVdiPersonSize(String vdiPersonSize) {
        this.vdiPersonSize = vdiPersonSize;
    }

    public String getVdiPersonDiskStoragePoolName() {
        return vdiPersonDiskStoragePoolName;
    }

    public void setVdiPersonDiskStoragePoolName(String vdiPersonDiskStoragePoolName) {
        this.vdiPersonDiskStoragePoolName = vdiPersonDiskStoragePoolName;
    }

    public String getVdiVgpuModel() {
        return vdiVgpuModel;
    }

    public void setVdiVgpuModel(String vdiVgpuModel) {
        this.vdiVgpuModel = vdiVgpuModel;
    }
}
