package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOneAgentConnectTypeEnums;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.common.dto.BaseResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcoUserDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.OaUserLoginCache;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.dto.NotifyComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.enums.RcoComputerActionNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.service.NotifyComputerChangeService;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ChangeComputerWorkModelDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerBindUserDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.ComputerReportSystemInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetComputerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.response.GetComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.ComputerActionServer;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolComputerService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.THIRD_PARTY_STRATEGY_ID;


/**
 * Description: PC终端相关功能
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
public class ComputerActionServerImpl implements ComputerActionServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerActionServerImpl.class);


    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private NotifyComputerChangeService notifyComputerChangeService;

    @Autowired
    private DesktopPoolComputerService desktopPoolComputerService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopPoolUserService desktopPoolUserService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public GetComputerInfoResponse getComputerInfo(String terminalId, GetComputerInfoDTO computerInfoDTO) throws BusinessException {
        Assert.notNull(terminalId, "terminalId is not be null");
        LOGGER.info("获取PC终端[{}]信息", terminalId);
        ComputerEntity computerEntity = computerBusinessService.getComputerById(UUID.fromString(terminalId));
        GetComputerInfoResponse response = new GetComputerInfoResponse();
        if (computerEntity != null) {
            response = computerBusinessService.getComputerInfoDTO(computerEntity);
        }
        LOGGER.info("获取PC终端[{}]信息[{}]", terminalId, JSON.toJSONString(response));
        return response;
    }

    @Override
    public void reportSystemInfo(String terminalId, ComputerReportSystemInfoDTO reportSystemInfoDTO) throws BusinessException {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(reportSystemInfoDTO, "reportSystemInfoDTO is not be null");
        LOGGER.info("上报PC终端[{}]系统信息", terminalId);
        computerBusinessService.updateComputerSystemInfo(terminalId, reportSystemInfoDTO);

    }

    @Override
    public BaseResultDTO computerBindUser(String terminalId, ComputerBindUserDTO computerBindUserDTO) throws BusinessException {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(computerBindUserDTO, "computerBindUserDTO is not be null");
        LOGGER.info("PC终端[{}]绑定用户信息,绑定信息[{}]", terminalId, JSON.toJSONString(computerBindUserDTO));
        UUID cache = OaUserLoginCache.getCache(computerBindUserDTO.getUserId());
        BaseResultDTO defaultResponse = new BaseResultDTO();
        if (cache == null) {
            LOGGER.warn("PC终端[{}]绑定用户信息请先登录", terminalId);
            defaultResponse.setCode(CommonMessageCode.FAIL);
            defaultResponse.setMessage(LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_PLEASE_LOG_IN_FIRST));
            return defaultResponse;
        }
        ComputerEntity computerEntity = computerBusinessService.getComputerById(UUID.fromString(terminalId));
        if (computerEntity == null) {
            defaultResponse.setCode(CommonMessageCode.SUCCESS);
            return defaultResponse;
        }
        RcoUserDesktopDTO userDesktopDTO = null;
        CbbDeskDTO deskDTO = null;
        try {
            userDesktopDTO = userDesktopMgmtAPI.findByDesktopId(computerEntity.getId());
            deskDTO = cbbDeskMgmtAPI.findById(computerEntity.getId());
            if (deskDTO != null && (deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE
                    || deskDTO.getDesktopPoolType() == DesktopPoolType.DYNAMIC)) {
                defaultResponse.setCode(CommonMessageCode.FAIL);
                defaultResponse.setMessage(LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_MULTI_DYNAMIC_NOT_SUPPORT));
                return defaultResponse;
            }
        } catch (BusinessException e) {
            LOGGER.info("桌面[{}]不存在", computerEntity.getId());
        }
        if (computerBindUserDTO.getUserId() != null) {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserDetail(computerBindUserDTO.getUserId());
            if (userDetailDTO == null) {
                defaultResponse.setCode(CommonMessageCode.FAIL);
                defaultResponse.setMessage(LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_BIND_USER_NOT_FOUNT_BY_ID));
                return defaultResponse;
            }
            if (userDesktopDTO == null) {
                //PC终端或者终端组未被第三方桌面池纳管,绑定用户自动创建属于该用户的第三方普通云桌面，并绑定默认第三方默认桌面策略
                computerBindUser(computerBindUserDTO, computerEntity);
            } else if (deskDTO != null) {
                thirdPartyPoolDesktopBindUser(computerBindUserDTO, computerEntity, userDesktopDTO, deskDTO, userDetailDTO);
            }
        }

        defaultResponse.setCode(CommonMessageCode.SUCCESS);
        return defaultResponse;
    }

    private void thirdPartyPoolDesktopBindUser(ComputerBindUserDTO computerBindUserDTO,
                                               ComputerEntity computerEntity,
                                               RcoUserDesktopDTO userDesktopDTO, CbbDeskDTO deskDTO, IacUserDetailDTO userDetailDTO) throws BusinessException {
        if (deskDTO.getDesktopPoolType() == DesktopPoolType.STATIC && userDesktopDTO.getUserId() == null) {
            // 未绑定用户，PC终端或者终端组已被第三方单会话静态池纳管。此时oneAgent自行绑定用户时，静态池会分配该用户并绑定桌面
            DesktopPoolComputerEntity poolComputerEntity = desktopPoolComputerService.findByRelatedId(computerEntity.getTerminalGroupId());
            poolComputerEntity = poolComputerEntity == null ?
                    desktopPoolComputerService.findByRelatedId(computerEntity.getId()) : poolComputerEntity;
            if (poolComputerEntity != null) {
                LOGGER.info("添加用户[{}]到桌面池[{}]", computerBindUserDTO.getUserId(), poolComputerEntity.getDesktopPoolId());
                desktopPoolUserService.addUserToDesktopPool(poolComputerEntity.getDesktopPoolId(), computerBindUserDTO.getUserId());

                CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(deskDTO.getDesktopPoolId());
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_COMPUTER_BIND_USER_POOL_DESK_ADD_USER, deskDTO.getName(), userDetailDTO.getUserName(),
                        desktopPoolDTO.getName());
                UserDesktopBindUserRequest userDesktopBindUserRequest = new UserDesktopBindUserRequest();
                userDesktopBindUserRequest.setDesktopId(deskDTO.getDeskId());
                userDesktopBindUserRequest.setUserId(computerBindUserDTO.getUserId());
                desktopPoolUserService.thirdPartyPoolDesktopBindUser(userDesktopBindUserRequest);
            }
        }
    }

    private void computerBindUser(ComputerBindUserDTO computerBindUserDTO, ComputerEntity computerEntity) throws BusinessException {
        NotifyComputerDTO notifyComputerDTO = new NotifyComputerDTO();
        BeanUtils.copyProperties(computerEntity, notifyComputerDTO);
        notifyComputerDTO.setId(computerEntity.getId());
        notifyComputerDTO.setUserId(computerBindUserDTO.getUserId());
        notifyComputerDTO.setDesktopStrategyId(UUID.fromString(THIRD_PARTY_STRATEGY_ID));
        notifyComputerDTO.setActionNotifyEnum(RcoComputerActionNotifyEnum.COMPUTER_BIND_USER);
        buildSpecInfo(computerEntity, notifyComputerDTO);
        notifyComputerDTO.setDeskIp(computerEntity.getIp());
        notifyComputerDTO.setMac(computerEntity.getMac());
        notifyComputerChangeService.notifyComputerChange(notifyComputerDTO);
    }

    private void buildSpecInfo(ComputerEntity computerEntity, NotifyComputerDTO notifyComputerDTO) {
        notifyComputerDTO.setMemory(StringUtils.isNotBlank(computerEntity.getMemory()) ?
                Integer.parseInt(computerEntity.getMemory()) : 0);
        notifyComputerDTO.setCpu(org.apache.commons.lang3.StringUtils.isNotBlank(computerEntity.getCpu()) ?
                Integer.parseInt(computerEntity.getCpu()) : 0);
        notifyComputerDTO.setPersonSize(computerEntity.getPersonDisk());
        notifyComputerDTO.setSystemSize(computerEntity.getSystemDisk());
    }

    @Override
    public BaseResultDTO oaNotifyCdcComputerWorkModel(String terminalId, ChangeComputerWorkModelDTO changeComputerWorkModelDTO) {
        Assert.notNull(terminalId, "terminalId is not be null");
        Assert.notNull(changeComputerWorkModelDTO, "changeComputerWorkModelDTO is not be null");
        LOGGER.info("oa变更PC终端[{}]工作模式数据[{}]", terminalId, JSON.toJSONString(changeComputerWorkModelDTO));
        if (CbbOneAgentConnectTypeEnums.APP_HOST.name().equals(changeComputerWorkModelDTO.getWorkModel())) {
            ComputerEntity computerEntity = computerBusinessService.getComputerById(changeComputerWorkModelDTO.getHostId());
            if (computerEntity != null) {
                // 应用主机不在管理此pc终端记录
                computerBusinessService.removeById(UUID.fromString(terminalId));
            }
        }
        BaseResultDTO defaultResponse = new BaseResultDTO();
        defaultResponse.setCode(CommonMessageCode.SUCCESS);
        return defaultResponse;
    }
}
