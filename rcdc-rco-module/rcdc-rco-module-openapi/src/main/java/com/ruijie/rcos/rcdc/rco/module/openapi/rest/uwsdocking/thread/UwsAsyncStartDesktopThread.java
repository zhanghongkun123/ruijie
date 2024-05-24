package com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.thread;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.WakeUpTerminalInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread.AbstractAsyncOperateDesktopThread;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.UUID;

/**
 * Description: uws异步线程启动云桌面
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/10 10:58
 *
 * @author zjy
 */
public class UwsAsyncStartDesktopThread extends AbstractAsyncOperateDesktopThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(UwsAsyncStartDesktopThread.class);

    // 时间间隔暂定
    private static final int IDV_WAKEUP_CHECK_INTERVAL = 5000;

    private static final int IDV_WAKEUP_TIMEOUT_MILLISECOND = 60000;

    private static final int IDV_WAKEUP_CHECK_TIMEOUT_MILLISECOND = 90000;

    private static final int PAGE_QUERY_DEFAULT_PAGE = 0;

    private static final int PAGE_QUERY_DEFAULT_LIMIT = 100;

    private static final String WAKE_UP_BY_SAME_NETWORK_NUMBER_TERMINAL = "wake_up_by_same_network_number_terminal";

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private PageQueryBuilderFactory pageQueryBuilderFactory;

    private TerminalWakeUpAPI terminalWakeUpAPI;

    //  历史版本支持异构直接启动，保持接口实现不变
    private Boolean supportCrossCpuVendor = Boolean.TRUE;

    public UwsAsyncStartDesktopThread(UUID deskId, AsyncTaskEnum action,
                                      OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                      UserDesktopOperateAPI cloudDesktopOperateAPI) throws BusinessException {
        super(deskId, action, openApiTaskInfoAPI, cloudDesktopOperateAPI);
    }

    public void setCloudDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    public void setUserTerminalMgmtAPI(UserTerminalMgmtAPI userTerminalMgmtAPI) {
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    public void setPageQueryBuilderFactory(PageQueryBuilderFactory pageQueryBuilderFactory) {
        this.pageQueryBuilderFactory = pageQueryBuilderFactory;
    }

    public void setTerminalWakeUpAPI(TerminalWakeUpAPI terminalWakeUpAPI) {
        this.terminalWakeUpAPI = terminalWakeUpAPI;
    }

    @Override
    public void run() {
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopMgmtAPI.getDesktopDetailById(deskId);
            CloudDesktopStartRequest request = new CloudDesktopStartRequest(deskId, customTaskId, CbbCloudDeskState.RUNNING);
            if (CbbCloudDeskType.VDI.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType())) {
                request.setSupportCrossCpuVendor(this.supportCrossCpuVendor);
                cloudDesktopOperateAPI.start(request);
            } else {
                wakeUpTerminal(cloudDesktopDetailDTO.getId(), cloudDesktopDetailDTO.getTerminalId());
                // 启动虚机
                cloudDesktopOperateAPI.startIdv(request);
            }
            saveTaskSuccess();
            LOGGER.info("uws启动云桌面正常，deskId:{},customTaskId:{}", deskId, customTaskId);
        } catch (BusinessException e) {
            LOGGER.error("AsyncStartDesktopThread error!", e);
            if (com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.
                    RCDC_HCIADAPTER_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY.equals(e.getKey())) {
                saveTaskException(new BusinessException(BusinessKey.RCDC_RCO_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY, e));
            } else {
                saveTaskException(e);
            }
        } catch (Exception e) {
            LOGGER.error("AsyncStartDesktopThread error!", e);
            saveTaskUnknownException(e);
        }
    }

    private void wakeUpTerminal(UUID deskId, final String terminalId) throws BusinessException {
        LOGGER.info("开始唤醒终端，云桌面id: [{}], 终端id : [{}]", deskId, terminalId);

        try {
            if (StringUtils.isEmpty(terminalId)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_NOT_EXIST, deskId.toString());
            }

            // 成功唤醒后，保存自动记录缓存
            cloudDesktopOperateAPI.updateDeskAutoStartVmCache(terminalId);
            // 唤醒
            terminalWakeUpAPI.wakeUpTerminal(terminalId);
        } catch (BusinessException ex) {
            // 唤醒失败时，清空缓存
            cloudDesktopOperateAPI.deleteDeskAutoStartVmCache(terminalId);
            throw ex;
        }
    }

    public Boolean getSupportCrossCpuVendor() {
        return supportCrossCpuVendor;
    }

    public void setSupportCrossCpuVendor(Boolean supportCrossCpuVendor) {
        this.supportCrossCpuVendor = supportCrossCpuVendor;
    }
}
