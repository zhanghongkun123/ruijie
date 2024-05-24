package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 创建用户分组请求参数对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/18
 *
 * @author Jarman
 */
public class UpdateUserGroupWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    @NotBlank
    @TextMedium
    @TextName
    private String groupName;

    @Nullable
    private IdLabelEntry parent;

    @Nullable
    private VdiDesktopConfigVO vdiDesktopConfig;

    /** 登录权限等级 */
    @NotNull
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    @Nullable
    private IdvDesktopConfigVO idvDesktopConfig;

    /**
     * VOI云桌面配置
     */
    @Nullable
    private VoiDesktopConfigVO voiDesktopConfig;

    /**
     * 主要认证
     */
    @NotNull
    private PrimaryCertificationVO primaryCertificationVO;

    /** 辅助认证 */
    @Nullable
    private AssistCertificationVO assistCertification;

    /**
     * 随机密码
     */
    @Nullable
    @ApiModelProperty(value = "随机密码开关")
    private Boolean passwordRandom;

    @Nullable
    @ApiModelProperty(value = "到期时间")
    private Long accountExpireDate;

    @Nullable
    @Range(min = "0", max = "1000")
    @ApiModelProperty(value = "失效时间")
    private Integer invalidTime;
    
    /**
     * 对象转换
     *
     * @param request UpdateUserGroupWebRequest
     * @return UpdateUserGroupRequest
     */
    public static IacUpdateUserGroupDTO convertFor(UpdateUserGroupWebRequest request) {
        IacUpdateUserGroupDTO apiRequest = new IacUpdateUserGroupDTO();
        apiRequest.setId(request.getId());
        apiRequest.setName(request.getGroupName());
        apiRequest.setEnableRandomPassword(request.getPasswordRandom());
        if (request.getParent() == null) {
            apiRequest.setParentId(null);
        } else {
            apiRequest.setParentId(request.getParent().getId());
        }
        if (ObjectUtils.isNotEmpty(request.getAccountExpireDate())) {
            apiRequest.setAccountExpires(request.getAccountExpireDate());
        }
        apiRequest.setInvalidTime(request.getInvalidTime());
        return apiRequest;
    }

    public Boolean getPasswordRandom() {
        return passwordRandom;
    }

    public void setPasswordRandom(Boolean passwordRandom) {
        this.passwordRandom = passwordRandom;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    @Nullable
    public IdLabelEntry getParent() {
        return parent;
    }

    public void setParent(@Nullable IdLabelEntry parent) {
        this.parent = parent;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public VdiDesktopConfigVO getVdiDesktopConfig() {
        return vdiDesktopConfig;
    }

    public void setVdiDesktopConfig(VdiDesktopConfigVO vdiDesktopConfig) {
        this.vdiDesktopConfig = vdiDesktopConfig;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    @Nullable
    public IdvDesktopConfigVO getIdvDesktopConfig() {
        return idvDesktopConfig;
    }

    public void setIdvDesktopConfig(@Nullable IdvDesktopConfigVO idvDesktopConfigVO) {
        this.idvDesktopConfig = idvDesktopConfigVO;
    }

    @Nullable
    public VoiDesktopConfigVO getVoiDesktopConfig() {
        return voiDesktopConfig;
    }

    public void setVoiDesktopConfig(@Nullable VoiDesktopConfigVO voiDesktopConfig) {
        this.voiDesktopConfig = voiDesktopConfig;
    }

    @Nullable
    public AssistCertificationVO getAssistCertification() {
        return assistCertification;
    }

    public void setAssistCertification(@Nullable AssistCertificationVO assistCertification) {
        this.assistCertification = assistCertification;
    }

    public PrimaryCertificationVO getPrimaryCertificationVO() {
        return primaryCertificationVO;
    }

    public void setPrimaryCertificationVO(PrimaryCertificationVO primaryCertificationVO) {
        this.primaryCertificationVO = primaryCertificationVO;
    }

    @Nullable
    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(@Nullable Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    @Nullable
    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(@Nullable Integer invalidTime) {
        this.invalidTime = invalidTime;
    }
}
