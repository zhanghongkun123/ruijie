package com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.api;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClientOpLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.dto.ClientOptLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.enums.ClientOperateLogType;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.request.ClientOpLogPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.dao.ClientOpLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.entity.ClientOpLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.service.ClientOpLogService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
public class ClientOpLogAPIImpl implements ClientOpLogAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientOpLogAPIImpl.class);


    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private ClientOpLogService clientOpLogService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private ClientOpLogDAO clientOpLogDAO;

    @Override
    public void saveUserOperateLog(String terminalId, UUID userId, ClientOperateLogType logType) {
        Assert.hasText(terminalId, "terminalId can not be blank");
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(logType, "logType can not be null");

        try {
            saveOperateLog(terminalId, userId, logType);
        } catch (Exception e) {
            LOGGER.error("记录操作日志出现异常", e);
        }
    }

    private void saveOperateLog(String terminalId, UUID userId, ClientOperateLogType logType) throws BusinessException {
        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        if (terminalBasicInfoDTO.getTerminalPlatform() != CbbTerminalPlatformEnums.APP) {
            LOGGER.info("非软客户端（OC），不需要记录客户端操作日志");
            return;
        }
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        ClientOpLogEntity opLogEntity = new ClientOpLogEntity();
        opLogEntity.setIp(terminalBasicInfoDTO.getIp());
        opLogEntity.setMac(terminalBasicInfoDTO.getMacAddr());
        opLogEntity.setUserId(userDetail.getId());
        opLogEntity.setUserName(userDetail.getUserName());
        opLogEntity.setOperMsg(LocaleI18nResolver.resolve(logType.getType()));
        Date current = new Date();
        opLogEntity.setOperTime(current);
        opLogEntity.setCreateTime(current);
        opLogEntity.setUpdateTime(current);

        clientOpLogDAO.save(opLogEntity);
    }

    @Override
    public DefaultPageResponse<ClientOptLogDTO> pageQuery(ClientOpLogPageRequest request) {
        Assert.notNull(request, "request can not be null");
        Page<ClientOpLogEntity> page = clientOpLogService.pageQuery(request);


        List<ClientOpLogEntity> clientOpLogEntityList = page.getContent();
        ClientOptLogDTO[] clientOptLogDTOArr = null;
        if (!CollectionUtils.isEmpty(clientOpLogEntityList)) {
            clientOptLogDTOArr = clientOpLogEntityList.stream().map(clientOpLogEntity -> {
                ClientOptLogDTO optLogDTO = new ClientOptLogDTO();
                BeanUtils.copyProperties(clientOpLogEntity, optLogDTO);
                return optLogDTO;
            }).toArray(ClientOptLogDTO[]::new);
        }


        DefaultPageResponse<ClientOptLogDTO> response = new DefaultPageResponse<>();
        response.setItemArr(clientOptLogDTOArr);
        response.setTotal(page.getTotalElements());

        return response;
    }

    @Override
    public void clear(Date specifiedDate) {
        Assert.notNull(specifiedDate, "specifiedDate can not be null");

        clientOpLogDAO.deleteByOperTimeLessThan(specifiedDate);
    }
}
