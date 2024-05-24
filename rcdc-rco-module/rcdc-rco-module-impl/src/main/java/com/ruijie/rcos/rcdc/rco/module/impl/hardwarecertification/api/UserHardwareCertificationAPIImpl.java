package com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacUserHardwareCheckDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacUserMacBindingDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacViewUserHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacValidateUserHardwareResultEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.RcoViewUserHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserHardwareCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserMacBindingDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.HardwareCertificationBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.entity.RcoViewUserHardwareCertificationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service.QueryUserHardwareCertificationListService;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service.TerminalFeatureCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 *
 * Description: 硬件特征码认证策略API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public class UserHardwareCertificationAPIImpl implements UserHardwareCertificationAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHardwareCertificationAPIImpl.class);

    @Autowired
    private QueryUserHardwareCertificationListService queryUserHardwareCertificationListService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private IacUserHardwareCertificationAPI iacUserHardwareCertificationAPI;

    @Autowired
    private IacHardwareCertificationAPI iacHardwareCertificationAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI iacUserIdentityConfigMgmtAPI;

    @Autowired
    private TerminalFeatureCodeService terminalFeatureCodeService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public DefaultPageResponse<RcoViewUserHardwareCertificationDTO> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "PageSearchRequest can not be null");

        Page<RcoViewUserHardwareCertificationEntity> page =
                queryUserHardwareCertificationListService.pageQuery(request, RcoViewUserHardwareCertificationEntity.class);
        List<RcoViewUserHardwareCertificationEntity> entityList = page.getContent();
        RcoViewUserHardwareCertificationDTO[] dtoArr = buildListDTO(entityList);
        DefaultPageResponse<RcoViewUserHardwareCertificationDTO> response = new DefaultPageResponse<>();
        response.setItemArr(dtoArr);
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public void deleteById(UUID id) throws BusinessException {
        Assert.notNull(id, "deleteById()的id can not be null");
        LOGGER.warn("delete hardware certification, id[{}]", id.toString());
        iacUserHardwareCertificationAPI.deleteById(id);
    }

    @Override
    public void checkNumAndUpdateState(UUID id, IacUserHardwareCertificationStateEnum stateEnum) throws BusinessException {
        Assert.notNull(id, "updateStateById()的id can not be null");
        Assert.notNull(stateEnum, "stateEnum can not be null");
        LOGGER.warn("update certification state, id[{}], state[{}]", id.toString(), stateEnum.name());
        iacUserHardwareCertificationAPI.checkNumAndUpdateState(id, stateEnum);
    }

    @Override
    public RcoViewUserHardwareCertificationDTO getById(UUID id) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        RcoViewUserHardwareCertificationDTO rcoViewUserHardwareCertificationDTO = new RcoViewUserHardwareCertificationDTO();
        IacViewUserHardwareCertificationDTO iacViewUserHardwareCertificationDTO = iacUserHardwareCertificationAPI.getById(id);
        BeanUtils.copyProperties(iacViewUserHardwareCertificationDTO, rcoViewUserHardwareCertificationDTO);
        return rcoViewUserHardwareCertificationDTO;
    }

    @Override
    public IacValidateUserHardwareResultEnum validateCertification(UserHardwareCheckDTO userHardwareCheckDTO) throws BusinessException {
        Assert.notNull(userHardwareCheckDTO, "userHardwareCheckDTO can not be null");
        String terminalId = userHardwareCheckDTO.getTerminalId();
        if (rccmManageService.isUnifiedLoginAndNotAssistAuth()) {
            return IacValidateUserHardwareResultEnum.SUCCESS;
        }
        CbbTerminalBasicInfoDTO terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        // 目前仅校验软终端和VDI终端
        if (CbbTerminalPlatformEnums.VDI != terminalDTO.getTerminalPlatform() && CbbTerminalPlatformEnums.APP != terminalDTO.getTerminalPlatform()) {
            return IacValidateUserHardwareResultEnum.SUCCESS;
        }
        IacHardwareCertificationDTO certificationConfig = iacHardwareCertificationAPI.getHardwareCertification();
        // 全局开关是否开启
        boolean enableOpenHardware = BooleanUtils.isTrue(certificationConfig.getOpenHardware());
        if (!enableOpenHardware) {
            return IacValidateUserHardwareResultEnum.SUCCESS;
        }

        IacUserIdentityConfigRequest userIdentityConfigRequest =
                new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userHardwareCheckDTO.getUserId());
        IacUserIdentityConfigResponse response = iacUserIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
        // 用户个人是否开启
        boolean enableUserOpenHardware = BooleanUtils.isTrue(response.getOpenHardwareCertification());
        if (!enableUserOpenHardware) {
            return IacValidateUserHardwareResultEnum.SUCCESS;
        }
        IacUserHardwareCheckDTO iacUserHardwareCheckDTO =
                new IacUserHardwareCheckDTO(userHardwareCheckDTO.getUserId(), userHardwareCheckDTO.getTerminalId());
        iacUserHardwareCheckDTO.setMacAddr(terminalDTO.getMacAddr());
        IacValidateUserHardwareResultEnum hardwareResultEnum = iacUserHardwareCertificationAPI.validateCertification(iacUserHardwareCheckDTO);
        if (IacValidateUserHardwareResultEnum.FAIL_OVER_MAX == hardwareResultEnum) {
            return hardwareResultEnum;
        }
        buildAndSave(userHardwareCheckDTO.getUserName(), terminalId, hardwareResultEnum);
        return hardwareResultEnum;
    }

    @Override
    public void delRecordByUserId(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId can not be null");
        iacUserHardwareCertificationAPI.delRecordByUserId(userId);
    }

    @Override
    public void createUserMacBinding(UserMacBindingDTO userMacBindingDTO) throws BusinessException {
        Assert.notNull(userMacBindingDTO, "userHardwareDTO can not be null");

        IacUserMacBindingDTO iacUserMacBindingDTO = new IacUserMacBindingDTO();
        iacUserMacBindingDTO.setTerminalMac(userMacBindingDTO.getTerminalMac());
        iacUserMacBindingDTO.setUserName(userMacBindingDTO.getUserName());

        iacUserHardwareCertificationAPI.createUserMacBinding(iacUserMacBindingDTO);

    }

    @Override
    public void clearTerminalBinding(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId can not be null");
        iacUserHardwareCertificationAPI.clearTerminalBinding(terminalId);
    }

    private RcoViewUserHardwareCertificationDTO[] buildListDTO(List<RcoViewUserHardwareCertificationEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            return new RcoViewUserHardwareCertificationDTO[] {};
        }
        return entityList.stream().map(item -> {
            RcoViewUserHardwareCertificationDTO certificationDTO = new RcoViewUserHardwareCertificationDTO();
            BeanUtils.copyProperties(item, certificationDTO);
            return certificationDTO;
        }).toArray(RcoViewUserHardwareCertificationDTO[]::new);
    }

    /**
     * 新增审批记录
     *
     * @param userName userName
     * @param terminalId terminalId
     * @param stateEnum UserHardwareCertificationStateEnum
     * @throws BusinessException BusinessException
     */
    private void buildAndSave(String userName,String terminalId, IacValidateUserHardwareResultEnum stateEnum) throws BusinessException {
        String featureCode = terminalFeatureCodeService.saveAndGetFeatureCode(terminalId);
        // 记录系统日志
        auditLogAPI.recordLog(HardwareCertificationBusinessKey.USER_HARDWARE_CERTIFICATION_RECORD_ADD, userName,
                stateEnum.getMessage(), featureCode);
    }
}
