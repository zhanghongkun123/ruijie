package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbCreateExternalStorageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbLocalExternalStorageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.ExternalStorageTcpServer;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dao.RcoDeskInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.entity.RcoDeskInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileStrategyDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/25
 *
 * @author zqj
 */
public class ExternalStorageTcpServerImpl implements ExternalStorageTcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalStorageTcpServerImpl.class);

    @Autowired
    private RcoDeskInfoDAO rcoDeskInfoDAO;


    @Autowired
    private UserProfileStrategyDAO userProfileStrategyDAO;

    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    @Override
    public CbbCreateExternalStorageDTO getFileServerConfig(String terminalId, UUID deskId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be null");
        Assert.notNull(deskId, "deskId can not be null");
        LOGGER.info("收到桌面[{}]OA获取文件服务器信息", deskId);
        RcoDeskInfoEntity deskInfo = rcoDeskInfoDAO.findByDeskId(deskId);
        if (deskInfo == null) {
            LOGGER.error("无法找到deskId[{}]对应的个性化策略信息，直接跳过，不进行个性化策略的同步", deskId);
            throw new BusinessException(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_NOT_FOUND_DESKTOP, String.valueOf(deskId));
        }
        UUID strategyId = deskInfo.getUserProfileStrategyId();
        CbbCreateExternalStorageDTO cbbCreateExternalStorageDTO = new CbbCreateExternalStorageDTO();
        if (strategyId != null) {
            UserProfileStrategyEntity userProfileStrategy = userProfileStrategyDAO.getOne(strategyId);
            CbbLocalExternalStorageDTO extStorageDetail =
                    cbbExternalStorageMgmtAPI.getExternalStorageDetail(userProfileStrategy.getExternalStorageId());
            cbbCreateExternalStorageDTO.setName(extStorageDetail.getName());
            cbbCreateExternalStorageDTO.setDescription(extStorageDetail.getDescription());
            cbbCreateExternalStorageDTO.setProtocolType(extStorageDetail.getProtocolType());
            cbbCreateExternalStorageDTO.setServerName(extStorageDetail.getServerName());
            cbbCreateExternalStorageDTO.setShareName(extStorageDetail.getShareName());
            cbbCreateExternalStorageDTO.setUserName(extStorageDetail.getUserName());
            cbbCreateExternalStorageDTO.setPassword(extStorageDetail.getPassword());
            cbbCreateExternalStorageDTO.setPort(extStorageDetail.getPort());
        }
        LOGGER.info("响应桌面[{}]OA文件服务器信息[{}]", deskId, JSON.toJSONString(cbbCreateExternalStorageDTO));
        return cbbCreateExternalStorageDTO;
    }
}
