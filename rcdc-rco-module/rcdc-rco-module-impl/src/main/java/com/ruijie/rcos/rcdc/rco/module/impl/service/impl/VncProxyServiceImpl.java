package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterVirtualIpDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistInfoOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.RemoteAssistInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.VncConnectionInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.VncProxyService;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.filesystem.SkyengineFile;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Description: VNC代理管理实现类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/16 17:18
 *
 * @author ketb
 */
@Service("rcoVncProxyServiceImpl")
public class VncProxyServiceImpl implements VncProxyService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(VncProxyServiceImpl.class);

    @Autowired
    private ConfigFacade configFacade;

    @Autowired
    private CbbClusterServerMgmtAPI clusterServerMgmtAPI;

    @Autowired
    private ComputerBusinessDAO computerDAO;
    
    @Autowired
    private RemoteAssistInfoOperateAPI remoteAssistInfoOperateAPI;

    @Override
    public VncConnectionInfoDTO addVncConfig(UUID businessId) throws BusinessException {
        Assert.notNull(businessId, "businessId cannot be null!");

        // 获取目标桌面的VNC配置
        ComputerEntity entity = computerDAO.findComputerEntityById(businessId);
        RemoteAssistInfoResponse response = remoteAssistInfoOperateAPI.queryRemoteAssistInfo(new IdRequest(businessId));
        // 创建/修改token配置文件（配置文件中需要存pc的ip和pc的vnc端口）
        createOrModifyTokenFile(businessId, entity.getIp(), response.getAssistPort());
        
        // 获取websocket服务连接参数
        final CbbClusterVirtualIpDTO cbbClusterVirtualIpDTO = clusterServerMgmtAPI.getClusterVirtualIp();

        String rcdcIP = cbbClusterVirtualIpDTO.getClusterVirtualIp();

        return new VncConnectionInfoDTO(rcdcIP, Constants.VNC_WEBSOCKET_PORT, businessId.toString());
    }

    private void createOrModifyTokenFile(UUID businessId, String ip, Integer port) throws BusinessException {
        final String vncTokenRoot = configFacade.read(Constants.COMPUTER_VNC_FILE_PATH);
        new File(vncTokenRoot).mkdirs();

        Path tokenFilePath = Paths.get(vncTokenRoot + businessId.toString());
        try {
            if (!Files.exists(tokenFilePath)) {
                Files.createFile(tokenFilePath);
            }
            String fileContent = businessId.toString() + ": " + ip + ":" + port.toString();
            Files.write(tokenFilePath, fileContent.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_CREATE_OR_MODIFY_TOKEN_FILE_FAIL, ex);
        }
    }

    @Override
    public void removeVncConfig(UUID businessId) {
        Assert.notNull(businessId, "businessId cannot be null!");

        Path tokenFilePath = Paths.get(configFacade.read(Constants.COMPUTER_VNC_FILE_PATH) + businessId.toString());
        if (Files.exists(tokenFilePath)) {
            SkyengineFile skyengineFile = new SkyengineFile(tokenFilePath.toString());
            skyengineFile.delete(false);
        }
    }

}
