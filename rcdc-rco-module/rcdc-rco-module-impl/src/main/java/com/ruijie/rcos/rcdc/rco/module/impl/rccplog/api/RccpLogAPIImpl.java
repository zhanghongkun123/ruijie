package com.ruijie.rcos.rcdc.rco.module.impl.rccplog.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PhysicalServerDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpLogMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.log.RccpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccpLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.rccplog.dto.RccpLogCollectStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.rccplog.enums.RccpLogCollectState;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.RccpLogCollectStateCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.RccpLogCollectStateCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.rccplog.handler.CreateRccpLogHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.rccplog.handler.DeleteRccpLogHandler;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.log.CreateLogRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/14 16:58
 *
 * @author ketb
 */
public class RccpLogAPIImpl implements RccpLogAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(RccpLogAPIImpl.class);

    private static final int MAX_LIMIT = 2000;

    @Autowired
    private RccpLogMgmtAPI logMgmtAPI;

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Override
    public void collectRccpLog() throws BusinessException {
        List<PhysicalServerDTO> serverList = cbbPhysicalServerMgmtAPI.listAllPhysicalServer(true);
        UUID hostId = serverList.get(0).getId();

        PageResponse<RccpLogDTO> oldLogResponse = findRccpLog();
        if (oldLogResponse.getTotal() > 0) {
            LOGGER.info("已存在rccp日志，需要先清理旧日志文件");
            RccpLogDTO[] rccpLogDTOArr = oldLogResponse.getItems();
            for (RccpLogDTO rccpLogDTO : rccpLogDTOArr) {
                deleteRccpLog(rccpLogDTO.getId());
            }
        }

        RccpLogCollectStateCache collectStateCache = RccpLogCollectStateCacheManager.getCache(hostId);
        if (collectStateCache != null && collectStateCache.getState() == RccpLogCollectState.DOING) {
            LOGGER.debug("正在收集RCCP日志，不可重复请求，hostId=[{}]", hostId);
            return;
        }
        RccpLogCollectStateCacheManager.addCache(hostId);

        try {
            createRccpLog(hostId);
        } catch (BusinessException e) {
            RccpLogCollectStateCacheManager.updateState(hostId, RccpLogCollectState.FAULT);
            throw e;
        }
    }

    @Override
    public RccpLogCollectStateDTO getCollectRccpLogState() throws BusinessException {
        List<PhysicalServerDTO> serverList = cbbPhysicalServerMgmtAPI.listAllPhysicalServer(true);
        UUID hostId = serverList.get(0).getId();

        RccpLogCollectStateDTO response = new RccpLogCollectStateDTO();
        RccpLogCollectStateCache cache = RccpLogCollectStateCacheManager.getCache(hostId);

        if (cache == null) {
            throw new BusinessException(BusinessKey.RCDC_COLLECT_RCCP_LOG_TASK_NOT_EXIST);
        }

        PageResponse<RccpLogDTO> logResponse = findRccpLog();
        if (logResponse.getTotal() > 0) {
            RccpLogDTO rccpLogDTO = logResponse.getItems()[0];
            RccpLogCollectStateCacheManager.updateState(hostId, RccpLogCollectState.DONE, rccpLogDTO.getFileName());
        }
        response.setState(cache.getState());
        response.setLogFileName(cache.getLogFileName());
        response.setMessage(cache.getMessage());

        return response;
    }

    private void createRccpLog(UUID hostId) throws BusinessException {
        Assert.notNull(hostId, "hostId not be null");

        UUID taskId = UUID.randomUUID();

        CreateLogRequest createLogRequest = new CreateLogRequest();
        createLogRequest.setHostId(hostId);
        createLogRequest.setTaskId(taskId);
        stateMachineFactory.newBuilder(taskId, CreateRccpLogHandler.class)
                .initArg(CreateRccpLogHandler.CREATE_ID_CONTEXT, createLogRequest).start();

        StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
        stateMachineMgmtAgent.waitForAllProcessFinish();
        LOGGER.warn("创建rccp日志文件:{}", hostId);
    }

    private PageResponse<RccpLogDTO> findRccpLog() throws BusinessException {
        CommonPageQueryRequest request = new CommonPageQueryRequest();
        request.setPage(0);
        request.setLimit(MAX_LIMIT);
        return logMgmtAPI.pageQueryLog(request);
    }

    private void deleteRccpLog(UUID logId) throws BusinessException {
        Assert.notNull(logId, "logId not be null");

        UUID taskId = UUID.randomUUID();
        stateMachineFactory.newBuilder(taskId, DeleteRccpLogHandler.class)
                .initArg(DeleteRccpLogHandler.DELETE_ID_CONTEXT, logId).start();

        StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
        stateMachineMgmtAgent.waitForAllProcessFinish();
        LOGGER.warn("删除rccp日志文件:{}", logId);
    }
}
