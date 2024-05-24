package com.ruijie.rcos.rcdc.rco.module.impl.computer.impl;


import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUpdateThirdDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbHostOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolThirdPartyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateThirdPartyDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolThirdPartyBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.CreatePoolComputerDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.dto.NotifyComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.enums.RcoComputerActionNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.service.NotifyComputerChangeService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolComputerService;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.UUID;


/**
 * Description: pc终端操作通知
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17
 *
 * @author zqj
 */
@Service
public class NotifyComputerChangeServiceImpl implements NotifyComputerChangeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyComputerChangeServiceImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DesktopPoolComputerService desktopPoolComputerService;

    @Autowired
    private DesktopPoolService desktopPoolService;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolThirdPartyMgmtAPI desktopPoolThirdPartyMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Override
    public void notifyComputerChange(NotifyComputerDTO notifyComputerDTO) throws BusinessException {
        Assert.notNull(notifyComputerDTO, "notifyComputerDTO can not be null");
        LOGGER.info("PC终端[{}]操作通知[{}]", notifyComputerDTO.getId(), JSON.toJSONString(notifyComputerDTO));

        if (notifyComputerDTO.getActionNotifyEnum() == RcoComputerActionNotifyEnum.ADD_COMPUTER
                || notifyComputerDTO.getActionNotifyEnum() == RcoComputerActionNotifyEnum.COMPUTER_BIND_USER) {
            // 首次创建并绑定用户自动创建属于该用户的第三方普通云桌面
            dealAddComputerOrBindUser(notifyComputerDTO);
        } else if (notifyComputerDTO.getActionNotifyEnum() == RcoComputerActionNotifyEnum.UPDATE_COMPUTER
                && (StringUtils.hasText(notifyComputerDTO.getOs()) || StringUtils.hasText(notifyComputerDTO.getAgentVersion()))) {
            updateDeskSpec(notifyComputerDTO);
        } else if (notifyComputerDTO.getActionNotifyEnum() == RcoComputerActionNotifyEnum.MOVE_COMPUTER) {
            moveGroup(notifyComputerDTO);
        }

    }

    private void moveGroup(NotifyComputerDTO notifyComputerDTO) throws BusinessException {
        //所属分组已被桌面池绑定，创建成功后，也会自动纳管到对应桌面池
        if (notifyComputerDTO.getTerminalGroupId() != null) {
            if (notifyComputerDTO.getWorkModel() != null) {
                //已存在桌面跳过
                return;
            }
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(notifyComputerDTO.getId());
            if (deskDTO != null) {
                //已存在桌面跳过
                return;
            }
            DesktopPoolComputerEntity poolComputerEntity = desktopPoolComputerService.findByRelatedId(notifyComputerDTO.getTerminalGroupId());
            if (poolComputerEntity != null) {
                DesktopPoolBasicDTO poolBasicDTO = desktopPoolService.getDesktopPoolBasicById(poolComputerEntity.getDesktopPoolId());
                CbbHostOsType osType = CbbHostOsType.getOsType(notifyComputerDTO.getOs());
                if (osType == null) {
                    String failTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_COMPUTER_OS_NOT_SUPPORT);
                    String errorMsg = LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_COMPUTER_MOVE_GROUP_BIND_DESKTOP_POOL_FAIL,
                            notifyComputerDTO.getName(), poolBasicDTO.getName(), failTip);
                    saveSystemLog(errorMsg);
                    return;
                }
                if (poolBasicDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE && (!CbbHostOsType.isMultiSession(osType))) {
                    String failTip = LocaleI18nResolver.resolve(BusinessKey.RCDC_COMPUTER_MULTI_SESSION_OS);
                    String errorMsg = LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.RCDC_COMPUTER_MOVE_GROUP_BIND_DESKTOP_POOL_FAIL,
                            notifyComputerDTO.getName(), poolBasicDTO.getName(), failTip);
                    saveSystemLog(errorMsg);
                    return;
                }
                CreatePoolComputerDesktopRequest request = new CreatePoolComputerDesktopRequest();
                request.setDesktopId(notifyComputerDTO.getId());
                request.setDesktopName(notifyComputerDTO.getName());
                request.setPoolId(poolBasicDTO.getId());
                request.setPoolName(poolBasicDTO.getName());
                request.setStrategyId(poolBasicDTO.getStrategyId());
                request.setPoolDeskType(DesktopPoolType.convertToPoolDeskType(poolBasicDTO.getPoolModel()));
                request.setSoftwareStrategyId(poolBasicDTO.getSoftwareStrategyId());
                request.setUserProfileStrategyId(poolBasicDTO.getUserProfileStrategyId());
                request.setOsName(notifyComputerDTO.getOs());

                desktopPoolMgmtAPI.createThirdPartyDesktop(request);

                //添加终端与桌面池关系
                UpdatePoolThirdPartyBindObjectDTO updatePoolThirdPartyBindObjectDTO = new UpdatePoolThirdPartyBindObjectDTO();
                updatePoolThirdPartyBindObjectDTO.setPoolId(poolBasicDTO.getId());
                updatePoolThirdPartyBindObjectDTO.setAddComputerByIdList(Collections.singletonList(request.getDesktopId()));
                desktopPoolThirdPartyMgmtAPI.updatePoolBindObject(updatePoolThirdPartyBindObjectDTO);

                updateDeskSpec(notifyComputerDTO);
                String msg = LocaleI18nResolver.resolve(com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey.
                        RCDC_COMPUTER_MOVE_GROUP_BIND_DESKTOP_POOL_SUCCESS, notifyComputerDTO.getName(), poolBasicDTO.getName());
                saveSystemLog(msg);
            }
        }
    }

    private void saveSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }

    private void dealAddComputerOrBindUser(NotifyComputerDTO notifyComputerDTO) throws BusinessException {
        if (notifyComputerDTO.getUserId() != null) {
            CreateThirdPartyDesktopRequest createCloudDesktopRequest = new CreateThirdPartyDesktopRequest();
            createCloudDesktopRequest.setDeskId(notifyComputerDTO.getId());
            createCloudDesktopRequest.setStrategyId(notifyComputerDTO.getDesktopStrategyId());
            createCloudDesktopRequest.setUserId(notifyComputerDTO.getUserId());
            createCloudDesktopRequest.setDesktopName(notifyComputerDTO.getName());
            createCloudDesktopRequest.setOsName(notifyComputerDTO.getOs());
            createCloudDesktopRequest.setPoolDeskType(DesktopPoolType.COMMON);
            createCloudDesktopRequest.setAgentVersion(notifyComputerDTO.getAgentVersion());
            userDesktopMgmtAPI.createThirdParty(createCloudDesktopRequest);
            updateDeskSpec(notifyComputerDTO);
        }
    }

    private void updateDeskSpec(NotifyComputerDTO notifyComputerDTO) {
        CbbUpdateThirdDeskSpecDTO cbbUpdateDeskSpecDTO = new CbbUpdateThirdDeskSpecDTO();
        cbbUpdateDeskSpecDTO.setDeskId(notifyComputerDTO.getId());
        cbbUpdateDeskSpecDTO.setCpu(notifyComputerDTO.getCpu());
        cbbUpdateDeskSpecDTO.setMemory(notifyComputerDTO.getMemory());
        cbbUpdateDeskSpecDTO.setPersonSize(notifyComputerDTO.getPersonSize());
        cbbUpdateDeskSpecDTO.setSystemSize(notifyComputerDTO.getSystemSize());
        cbbUpdateDeskSpecDTO.setOsType(notifyComputerDTO.getOs());
        cbbUpdateDeskSpecDTO.setOsVersion(notifyComputerDTO.getOs());
        cbbUpdateDeskSpecDTO.setDeskIp(notifyComputerDTO.getDeskIp());
        cbbUpdateDeskSpecDTO.setMac(notifyComputerDTO.getMac());
        cbbDeskMgmtAPI.updateDeskSpec(cbbUpdateDeskSpecDTO);
    }
}
