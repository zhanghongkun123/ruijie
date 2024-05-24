package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalConfigAPI;

import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.RcoGlobalParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalConfigChangeDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalConfigImportRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.compress.utils.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.Set;

/**
 * Description: 终端硬件配置接口实现
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/16
 *
 * @author luocl
 */
public class TerminalConfigAPIImpl implements TerminalConfigAPI {

    /**
     * 允许变更模式的虚机状态
     */
    private static final Set<CbbCloudDeskState> NOT_RUNNING_LIST = Sets.newHashSet(CbbCloudDeskState.CLOSE, CbbCloudDeskState.DELETING,
            CbbCloudDeskState.COMPLETE_DELETING, CbbCloudDeskState.SLEEP, CbbCloudDeskState.RECYCLE_BIN,
            CbbCloudDeskState.OFF_LINE,CbbCloudDeskState.MOVING,CbbCloudDeskState.ERROR);

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalConfigAPIImpl.class);

    @Autowired
    private RcoGlobalParameterDAO rcoGlobalParameterDAO;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserDesktopService userDesktopService;


    /**
     * 上传&预览
     * @param request CbbTerminalConfigImportRequest
     * @throws BusinessException ex
     */
    @Override
    public void checkTerminalConfigUpload(CbbTerminalConfigImportRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(request.getFilePath(), "getFilePath cannot be null");
        Assert.notNull(request.getFileName(), "getFileName cannot be null");
        // 上传zip
        cbbTerminalConfigAPI.uploadTerminalConfig(request);
    }


    /**
     * 确认导入
     * @throws BusinessException ex
     */
    @Override
    public void importTerminalConfig() throws BusinessException {
        cbbTerminalConfigAPI.importTerminalConfig();
    }

}
