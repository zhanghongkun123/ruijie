package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ProtocolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsAdminTokenVerifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsCloudDeskUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsTerminalStateUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.uws.UwsAdminTokenVerifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.uws.UwsCloudDestOpearteTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BaseAdminRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.uws.AdminTokenRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAdminPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RandomTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.CmsUpgradeService;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoViewUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.UwsCloudDeskProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api.UwsUserProducerAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.uws.service.UwsComponentService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: UWS 拓展API 实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-19 14:02:00
 *
 * @author zjy
 */
public class UwsDockingAPIImpl implements UwsDockingAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsDockingAPIImpl.class);

    @Autowired
    private RcoViewUserDAO rcoViewUserDAO;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private UwsCloudDeskProducerAPI uwsCloudDeskProducerAPI;

    @Autowired
    private UwsUserProducerAPI uwsUserProducerAPI;

    @Autowired
    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    @Autowired
    private AdminManageAPI adminManageAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private UwsComponentService uwsComponentService;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private CmsUpgradeService cmsUpgradeService;

    @Autowired
    private ConfigFacade configFacade;

    @Autowired
    private CbbImageDriverMgmtAPI imageDriverMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    /**
     * 用于存放临时令牌 key：临时令牌， value：管理员数据
     */
    private ConcurrentHashMap<String, UwsAdminTokenVerifyDTO> tokenMap = new ConcurrentHashMap<>();


    @Override
    public void notifyDesktopAdd(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId is not null");

        UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO = getUwsCloudDeskUpdateDTO(desktopId);
        if (uwsCloudDeskUpdateDTO == null) {
            return;
        }
        uwsCloudDeskUpdateDTO.setOpearteType(UwsCloudDestOpearteTypeEnum.ADD);

        try {
            LOGGER.info("uws新增桌面通知，桌面id:[{}]", desktopId);
            uwsCloudDeskProducerAPI.notifyAdd(uwsCloudDeskUpdateDTO);
        } catch (Exception ex) {
            LOGGER.error("发送uws新增桌面通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public void notifyDesktopStateUpdate(UUID desktopId, CbbCloudDeskState cloudDeskState) {
        Assert.notNull(desktopId, "desktopId is not null");
        Assert.notNull(cloudDeskState, "cloudDeskState is not null");

        UserDesktopEntity cloudDesktopDetailDTO = userDesktopDAO.findByCbbDesktopId(desktopId);

        if (cloudDesktopDetailDTO == null) {
            LOGGER.error("查询桌面失败，桌面不存在，桌面id:[{}]", desktopId);
            // 不存在桌面
            return;
        }

        UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO = new UwsCloudDeskUpdateDTO();
        uwsCloudDeskUpdateDTO.setUuid(cloudDesktopDetailDTO.getUserId());
        uwsCloudDeskUpdateDTO.setOpearteType(UwsCloudDestOpearteTypeEnum.UPDATE);
        uwsCloudDeskUpdateDTO.setStatus(cloudDeskState.toString());
        UwsDesktopDTO uwsDesktopDTO = new UwsDesktopDTO();
        uwsDesktopDTO.setDesktopId(desktopId);
        uwsCloudDeskUpdateDTO.setDesktop(uwsDesktopDTO);

        try {
            LOGGER.info("uws更新桌面通知，桌面id:[{}], 状态：[{}]", desktopId, cloudDeskState.toString());
            uwsCloudDeskProducerAPI.notifyUpdate(uwsCloudDeskUpdateDTO);
        } catch (Exception ex) {
            LOGGER.error("发送uws更新桌面通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public void notifyDesktopRecover(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId is not null");

        UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO = getUwsCloudDeskUpdateDTO(desktopId);
        if (uwsCloudDeskUpdateDTO == null) {
            return;
        }
        uwsCloudDeskUpdateDTO.setOpearteType(UwsCloudDestOpearteTypeEnum.RECOVER);

        try {
            LOGGER.info("uws恢复桌面通知，桌面id:[{}]", uwsCloudDeskUpdateDTO.getUuid());
            uwsCloudDeskProducerAPI.notifyRecover(uwsCloudDeskUpdateDTO);
        } catch (Exception ex) {
            LOGGER.error("发送uws恢复桌面通知发生异常，ex: {}", ex);
        }
    }

    private UwsCloudDeskUpdateDTO getUwsCloudDeskUpdateDTO(UUID desktopId) {
        CloudDesktopDetailDTO cloudDesktopDetailDTO = null;
        try {
            cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("查询桌面详情发生异常，桌面id:[{}]，异常信息：{}", desktopId, e);
        }
        if (cloudDesktopDetailDTO == null) {
            LOGGER.error("查询桌面失败，桌面不存在，桌面id:[{}]", desktopId);
            // 不存在桌面
            return null;
        }

        UwsCloudDeskUpdateDTO uwsCloudDeskUpdateDTO = new UwsCloudDeskUpdateDTO();
        uwsCloudDeskUpdateDTO.setUuid(cloudDesktopDetailDTO.getUserId());
        uwsCloudDeskUpdateDTO.setStatus(cloudDesktopDetailDTO.getDesktopState());
        UwsDesktopDTO uwsDesktopDTO = new UwsDesktopDTO();
        BeanUtils.copyProperties(cloudDesktopDetailDTO, uwsDesktopDTO);
        uwsDesktopDTO.setDesktopId(cloudDesktopDetailDTO.getId());
        if (cloudDesktopDetailDTO.getDesktopType() != null &&
                !Objects.equals(cloudDesktopDetailDTO.getDesktopType(), CbbCloudDeskType.THIRD.name())) {
            uwsDesktopDTO.setDesktopType(DesktopType.valueOf(cloudDesktopDetailDTO.getDesktopType()));
        }
        uwsDesktopDTO.setDesktopCategory(UserCloudDeskTypeEnum.valueOf(cloudDesktopDetailDTO.getDesktopCategory()));
        uwsDesktopDTO.setDesktopState(CbbCloudDeskState.valueOf(cloudDesktopDetailDTO.getDesktopState()));
        if (!StringUtils.isEmpty(cloudDesktopDetailDTO.getUserType())) {
            uwsDesktopDTO.setUserType(IacUserTypeEnum.valueOf(cloudDesktopDetailDTO.getUserType()));
        }
        uwsDesktopDTO.setSystemType(cloudDesktopDetailDTO.getDesktopImageType());
        uwsDesktopDTO.setRemoteWakeUp(CbbCloudDeskType.VDI.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType()));
        if (!StringUtils.isEmpty(cloudDesktopDetailDTO.getTerminalId())) {
            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(cloudDesktopDetailDTO.getTerminalId());
                uwsDesktopDTO.setRemoteWakeUp(terminalDTO.getSupportRemoteWake() == null ? false : terminalDTO.getSupportRemoteWake());
            } catch (BusinessException e) {
                LOGGER.error(String.format("查询终端信息发生异常，终端id: %s", cloudDesktopDetailDTO.getTerminalId()), e);
            }
        }
        uwsCloudDeskUpdateDTO.setDesktop(uwsDesktopDTO);
        return uwsCloudDeskUpdateDTO;
    }

    @Override
    public void notifyDesktopDelete(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId is not null");
        try {
            LOGGER.info("uws删除桌面通知，桌面id:[{}]", desktopId);
            uwsCloudDeskProducerAPI.notifyDelete(desktopId);
        } catch (Exception ex) {
            LOGGER.error("发送uws删除桌面通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public void notifyUserDeleted(List<UUID> userIds) {
        Assert.notEmpty(userIds, "userIds can not be null");
        try {
            LOGGER.info("uws用户删除通知，用户id:[{}]", JSON.toJSONString(userIds));
            uwsUserProducerAPI.notifyUserDeleted(userIds);
        } catch (Exception ex) {
            LOGGER.error("发送用户删除通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public void notifyUserUpdatePwd(List<UUID> userIds) {
        Assert.notEmpty(userIds, "userIds can not be null");
        try {
            LOGGER.info("uws用户更新密码通知，用户id:[{}]", JSON.toJSONString(userIds));
            uwsUserProducerAPI.notifyUserUpdatePwd(userIds);
        } catch (Exception ex) {
            LOGGER.error("发送用户更新密码通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public void notifyUserDisabled(List<UUID> userIds) {
        Assert.notEmpty(userIds, "userIds can not be null");
        try {
            LOGGER.info("uws用户禁用通知，用户id:[{}]", JSON.toJSONString(userIds));
            uwsUserProducerAPI.notifyUserDisabled(userIds);
        } catch (Exception ex) {
            LOGGER.error("发送用户禁用通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public void notifyModifyPwdConfigChanged(Boolean allowChangePwd) {
        Assert.notNull(allowChangePwd, "allowChangePwd can not be null");

        try {
            LOGGER.info("uws密码开关修改通知，修改结果:[{}]", allowChangePwd);
            uwsUserProducerAPI.notifyModifyPwdConfigChanged(allowChangePwd);
        } catch (Exception ex) {
            LOGGER.error("发送密码开关修改通知发生异常，ex: {}", ex);
        }
    }

    @Override
    public RandomTokenResponse getRandomToken(BaseAdminRequest baseAdminRequest) throws BusinessException {
        Assert.notNull(baseAdminRequest, "baseAdminRequest is null");
        Assert.notNull(baseAdminRequest.getId(), "id is null");

        IacAdminDTO baseAdminDTO = null;
        try {
            baseAdminDTO = baseAdminMgmtAPI.getAdmin(baseAdminRequest.getId());
        } catch (Exception ex) {
            LOGGER.error("UWS, 管理员id对应数据不存在，id: [{}]", baseAdminRequest.getId());
        }

        if (baseAdminDTO == null) {
            throw new BusinessException(AaaBusinessKey.RCDC_AAA_ADMIN_NOT_EXIST, baseAdminRequest.getId().toString());
        }

        UwsAdminTokenVerifyDTO uwsAdminTokenVerifyDTO = new UwsAdminTokenVerifyDTO();
        uwsAdminTokenVerifyDTO.setCreateTime(System.currentTimeMillis());
        uwsAdminTokenVerifyDTO.setId(baseAdminDTO.getId());
        uwsAdminTokenVerifyDTO.setDisplayName(baseAdminDTO.getRealName());
        uwsAdminTokenVerifyDTO.setUserName(baseAdminDTO.getUserName());

        IdRequest idRequest = new IdRequest();
        idRequest.setId(baseAdminRequest.getId());
        GetAdminPasswordResponse getAdminPasswordResponse = adminManageAPI.getAdminPassword(idRequest);
        uwsAdminTokenVerifyDTO.setPassword(getAdminPasswordResponse.getPassword());

        clearTokenMapByOvertime();
        UUID token = UUID.randomUUID();
        tokenMap.put(token.toString(), uwsAdminTokenVerifyDTO);
        return new RandomTokenResponse(token);
    }

    @Override
    public UwsAdminTokenVerifyDTO verifyAdminToken(AdminTokenRequest adminTokenRequest) {
        Assert.notNull(adminTokenRequest, "adminTokenRequest is not null");
        Assert.notNull(adminTokenRequest.getToken(), "token is not null");

        UwsAdminTokenVerifyDTO response = tokenMap.get(adminTokenRequest.getToken());
        if (response == null) {
            response = new UwsAdminTokenVerifyDTO();
            response.setCode(UwsAdminTokenVerifyEnum.TOKEN_NOT_EXIST.getCode());
            LOGGER.error("UWS 管理员登入验证失败。原因：令牌无效。token = {}", adminTokenRequest.getToken());
            return response;
        }

        Long time = System.currentTimeMillis() - Constants.ONE_MINUTE_MILLIS;
        if (response.getCreateTime() < time) {
            response = new UwsAdminTokenVerifyDTO();
            response.setCode(UwsAdminTokenVerifyEnum.TOKEN_TIMEOUT.getCode());
            LOGGER.error("UWS 管理员登入验证失败。原因：令牌超时。token = {}", adminTokenRequest.getToken());
            tokenMap.remove(adminTokenRequest.getToken());
            return response;
        }

        tokenMap.remove(adminTokenRequest.getToken());
        response.setCode(UwsAdminTokenVerifyEnum.SUCCESS.getCode());
        LOGGER.info("UWS 管理员登入验证成功。token = {}, userName = {}", adminTokenRequest.getToken(), response.getUserName());
        return response;
    }

    private void clearTokenMapByOvertime() {
        Long time = System.currentTimeMillis() - Constants.ONE_MINUTE_MILLIS;
        for (Map.Entry<String, UwsAdminTokenVerifyDTO> entry : tokenMap.entrySet()) {
            if (entry.getValue().getCreateTime() < time) {
                tokenMap.remove(entry.getKey());
                LOGGER.info("UWS 令牌 [{}] 超时，清除该令牌。userName = {}", entry.getKey(), entry.getValue().getUserName());
            }
        }
    }

    @Override
    public void initCmApp() {
        uwsComponentService.initCmApp();
    }

    @Override
    public String getUwsComponentFlag() {
        return uwsComponentService.getUwsComponentFlag().getValue();
    }

    @Override
    public void notifyTerminalStateUpdate(UUID desktopId, String terminalId, CbbTerminalStateEnums terminalState) {
        Assert.notNull(desktopId, "desktopId is not null");
        Assert.notNull(terminalId, "terminalId is not null");
        Assert.notNull(terminalState, "terminalState is not null");

        try {
            LOGGER.info("uws终端状态变更通知，桌面id:[{}]，终端Id:[{}]", desktopId, terminalId);
            UwsTerminalStateUpdateDTO uwsTerminalStateUpdateDTO = new UwsTerminalStateUpdateDTO();
            uwsTerminalStateUpdateDTO.setDesktopId(desktopId);
            uwsTerminalStateUpdateDTO.setTerminalState(terminalState);
            uwsTerminalStateUpdateDTO.setTerminalId(terminalId);
            uwsCloudDeskProducerAPI.terminalStateUpdate(uwsTerminalStateUpdateDTO);
        } catch (Exception ex) {
            LOGGER.error("发送uws终端状态变更通知发生异常，ex: {}", ex);
        }

    }

    @Override
    public void initCmISO() {
        try {
            cmsUpgradeService.copyIsoToSamba(cmsUpgradeService.getUwsNewestIsoFromConfig(ProtocolType.FILE), FilePathContants.UWS_ISO_SAMBA_PATH);
        } catch (BusinessException e) {
            LOGGER.error("复制samba模板文件[{}]异常，进行重试", FilePathContants.UWS_ISO_SAMBA_PATH, e);
        }
    }
}
