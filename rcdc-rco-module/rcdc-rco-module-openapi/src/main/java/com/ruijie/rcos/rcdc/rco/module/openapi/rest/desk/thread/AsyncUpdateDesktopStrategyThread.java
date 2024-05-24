package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.OpenApiBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.help.DeskStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.UpdateDeskStrategyRequest;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: 异步修改云桌面策略
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/12
 *
 * @author TD
 */
public class AsyncUpdateDesktopStrategyThread extends AbstractAsyncTaskThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncUpdateDesktopStrategyThread.class);

    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private DeskStrategyAPI deskStrategyAPI;

    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    private DeskStrategyHelper deskStrategyHelper;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    private UserProfileMgmtAPI userProfileMgmtAPI;

    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    private final UpdateDeskStrategyRequest request;

    private BaseAuditLogAPI auditLogAPI;

    private RccmManageAPI rccmManageAPI;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 5000;

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(Constant.ASYNC_BATCH_APPLY_DESKTOP_STRATEGY_THREAD).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    public AsyncUpdateDesktopStrategyThread(UUID desktopStrategyId, AsyncTaskEnum action, OpenApiTaskInfoAPI openApiTaskInfoAPI,
                                            UpdateDeskStrategyRequest updateRequest) throws BusinessException {
        super(desktopStrategyId, action, openApiTaskInfoAPI);
        this.request = updateRequest;
    }

    @Override
    public void run() {
        CbbDeskStrategyDTO deskStrategyDTO = null;
        try {
            deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(request.getId());
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面策略出现异常:", request.getId(), e);
        }
        switch (Objects.requireNonNull(deskStrategyDTO).getStrategyType()) {
            case VDI:
                try {
                    CbbUpdateDeskStrategyVDIDTO updateDeskStrategyVDIDTO =
                            (CbbUpdateDeskStrategyVDIDTO) deskStrategyHelper.convertCbbUpdateDeskStrategyDTO(request);
                    updateVDIDesktopStrategy(updateDeskStrategyVDIDTO);
                } catch (BusinessException e) {
                    LOGGER.error("更新云桌面策略出错:", request.getId(), e);
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e, request.getStrategyName(),
                            e.getI18nMessage());
                    saveTaskException(e);
                } catch (Exception e) {
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e,
                            request.getStrategyName(), LocaleI18nResolver.resolve(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_SYSTEM_ERROR));
                    LOGGER.error("更新云桌面策略出现系统异常:", request.getId(), e);
                    saveTaskUnknownException(e);
                }
                break;
            case IDV:
                try {
                    CbbUpdateDeskStrategyIDVDTO updateDeskStrategyIDVRequest =
                            (CbbUpdateDeskStrategyIDVDTO) deskStrategyHelper.convertCbbUpdateDeskStrategyDTO(request);
                    cbbIDVDeskStrategyMgmtAPI.updateDeskStrategyIDV(updateDeskStrategyIDVRequest);
                    cloudDeskComputerNameConfigAPI.updateCloudDeskComputerNameConfig(request.getComputerName(), request.getId());
                    // 策略修订通知
                    rccmManageAPI.updateNotify(request.getId());
                    // 水印配置修改后操作
                    deskStrategyAPI.doWatermarkAfterUpdateStrategy(request.getId(), deskStrategyDTO.getWatermarkInfo());
                    deleteDeskRelatedUserProfileStrategy(request.getId(), updateDeskStrategyIDVRequest.getPattern());
                    saveTaskSuccess();
                } catch (BusinessException e) {
                    LOGGER.error("更新云桌面策略出错:", request.getId(), e);
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e, request.getStrategyName(),
                            e.getI18nMessage());
                    saveTaskException(e);
                } catch (Exception e) {
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e,
                            request.getStrategyName(), LocaleI18nResolver.resolve(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_SYSTEM_ERROR));
                    LOGGER.error("更新云桌面策略出现系统异常:", request.getId(), e);
                    saveTaskUnknownException(e);
                }
                break;
            case VOI:
                try {
                    CbbUpdateDeskStrategyVOIDTO updateDeskStrategyVOIRequest =
                            (CbbUpdateDeskStrategyVOIDTO) deskStrategyHelper.convertCbbUpdateDeskStrategyDTO(request);
                    cbbVOIDeskStrategyMgmtAPI.updateDeskStrategyVOI(updateDeskStrategyVOIRequest);
                    cloudDeskComputerNameConfigAPI.updateCloudDeskComputerNameConfig(request.getComputerName(), request.getId());
                    // 策略修订通知
                    rccmManageAPI.updateNotify(request.getId());
                    // 水印配置修改后操作
                    deskStrategyAPI.doWatermarkAfterUpdateStrategy(request.getId(), deskStrategyDTO.getWatermarkInfo());
                    deleteDeskRelatedUserProfileStrategy(request.getId(), updateDeskStrategyVOIRequest.getPattern());
                    // 通知TCI 在线公共终端，策略变更，需要重新获取启动参数
                    deskStrategyTciNotifyAPI.notifyFetchStartParams(request.getId());
                    saveTaskSuccess();
                } catch (BusinessException e) {
                    LOGGER.error("更新云桌面策略出错:", request.getId(), e);
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e, request.getStrategyName(),
                            e.getI18nMessage());
                    saveTaskException(e);
                } catch (Exception e) {
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e,
                            request.getStrategyName(), LocaleI18nResolver.resolve(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_SYSTEM_ERROR));
                    LOGGER.error("更新云桌面策略出现系统异常:", request.getId(), e);
                    saveTaskUnknownException(e);
                }
                break;
            case THIRD:
                try {
                    CbbUpdateDeskStrategyThirdPartyDTO updateDeskStrategyThirdPartyDTO =
                            (CbbUpdateDeskStrategyThirdPartyDTO) deskStrategyHelper.convertCbbUpdateDeskStrategyDTO(request);
                    cbbThirdPartyDeskStrategyMgmtAPI.updateDeskStrategyThirdParty(updateDeskStrategyThirdPartyDTO);
                    // 水印配置修改后操作
                    deskStrategyAPI.doWatermarkAfterUpdateStrategy(request.getId(), deskStrategyDTO.getWatermarkInfo());
                    saveTaskSuccess();
                } catch (BusinessException e) {
                    LOGGER.error("更新云桌面策略出错:", request.getId(), e);
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e, request.getStrategyName(),
                            e.getI18nMessage());
                    saveTaskException(e);
                } catch (Exception e) {
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e,
                            request.getStrategyName(), LocaleI18nResolver.resolve(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_SYSTEM_ERROR));
                    LOGGER.error("更新云桌面策略出现系统异常:", request.getId(), e);
                    saveTaskUnknownException(e);
                }
                break;
            default:
                try {
                    throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_STRATEGY_TYPE_NOT_EXIST,
                            deskStrategyDTO.getStrategyType().toString());
                } catch (BusinessException e) {
                    LOGGER.error("更新云桌面策略出错:", request.getId(), e);
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e, request.getStrategyName(),
                            e.getI18nMessage());
                    saveTaskException(e);
                } catch (Exception e) {
                    auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_FAIL_LOG, e,
                            request.getStrategyName(), LocaleI18nResolver.resolve(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_SYSTEM_ERROR));
                    LOGGER.error("更新云桌面策略出现系统异常:", request.getId(), e);
                    saveTaskUnknownException(e);
                }
        }
        auditLogAPI.recordLog(OpenApiBusinessKey.RCDC_OPENAPI_DESK_STRATEGY_EDIT_SUCCESS_LOG, request.getStrategyName());
    }

    private void updateVDIDesktopStrategy(CbbUpdateDeskStrategyVDIDTO updateDeskStrategyVDIDTO) throws BusinessException {
        CbbDeskStrategyVDIDTO oldDeskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(request.getId());
        // 修改保存策略信息
        cbbVDIDeskStrategyMgmtAPI.updateDeskStrategyVDI(updateDeskStrategyVDIDTO);
        // 计算机名称配置修改
        cloudDeskComputerNameConfigAPI.updateCloudDeskComputerNameConfig(request.getComputerName(), request.getId());
        // 策略修订通知
        rccmManageAPI.updateNotify(request.getId());

        // 水印功能：云桌面策略变了就发送给关联的在线桌面，如果云桌面策略关掉了水印功能，需要发送全局的水印策略
        deskStrategyAPI.doWatermarkAfterUpdateStrategy(request.getId(), oldDeskStrategyVDIDTO.getWatermarkInfo());

        // 如果配置有变化，才需要下发给桌面
        if (oldDeskStrategyVDIDTO.getPowerPlan() != request.getPowerPlan() ||
                !Objects.equals(request.getPowerPlanTime(), oldDeskStrategyVDIDTO.getPowerPlanTime())) {
            cbbVDIDeskStrategyMgmtAPI.sendPowerPlanConfigToDeskByStrategyId(request.getId(), CbbStrategyType.VDI);
        }

        // 安全审计
        deskStrategyAPI.doAuditUpdateStrategy(request.getId());
        saveTaskSuccess();
    }

    private void deleteDeskRelatedUserProfileStrategy(UUID deskStrategyId, CbbCloudDeskPattern cloudDeskPattern) {
        if (CbbCloudDeskPattern.RECOVERABLE == cloudDeskPattern) {
            return;
        }
        // 如果不是还原类型则删除用户配置策略关联记录
        userProfileMgmtAPI.deleteDeskRelatedUserProfileStrategy(deskStrategyId);
    }

    /**
     * 设置CbbVDIDeskStrategyMgmtAPI
     * @param cbbVDIDeskStrategyMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setCbbVDIDeskStrategyMgmtAPI(CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI) {
        this.cbbVDIDeskStrategyMgmtAPI = cbbVDIDeskStrategyMgmtAPI;
        return this;
    }

    /**
     * 设置DeskStrategyAPI
     * @param deskStrategyAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setDeskStrategyAPI(DeskStrategyAPI deskStrategyAPI) {
        this.deskStrategyAPI = deskStrategyAPI;
        return this;
    }

    /**
     * 设置CloudDeskComputerNameConfigAPI
     * @param cloudDeskComputerNameConfigAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setCloudDeskComputerNameConfigAPI(CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI) {
        this.cloudDeskComputerNameConfigAPI = cloudDeskComputerNameConfigAPI;
        return this;
    }

    /**
     * 设置DeskStrategyHelper
     * @param deskStrategyHelper API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setDeskStrategyHelper(DeskStrategyHelper deskStrategyHelper) {
        this.deskStrategyHelper = deskStrategyHelper;
        return this;
    }


    /**
     * 设置UserDesktopMgmtAPI
     * @param userDesktopMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
        return this;
    }

    /**
     * 设置CbbVDIDeskMgmtAPI
     * @param cbbVDIDeskMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
        return this;
    }

    /**
     * 设置CbbIDVDeskStrategyMgmtAPI
     * @param cbbIDVDeskStrategyMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setCbbIDVDeskStrategyMgmtAPI(CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI) {
        this.cbbIDVDeskStrategyMgmtAPI = cbbIDVDeskStrategyMgmtAPI;
        return this;
    }

    /**
     * 设置CbbThirdPartyDeskStrategyMgmtAPI
     * @param cbbThirdPartyDeskStrategyMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setCbbThirdPartyDeskStrategyMgmtAPI(CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI) {
        this.cbbThirdPartyDeskStrategyMgmtAPI = cbbThirdPartyDeskStrategyMgmtAPI;
        return this;
    }

    /**
     * 设置RccmManageAPI
     * @param rccmManageAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setRccmManageAPI(RccmManageAPI rccmManageAPI) {
        this.rccmManageAPI = rccmManageAPI;
        return this;
    }

    /**
     * 设置UserProfileMgmtAPI
     * @param userProfileMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setUserProfileMgmtAPI(UserProfileMgmtAPI userProfileMgmtAPI) {
        this.userProfileMgmtAPI = userProfileMgmtAPI;
        return this;
    }

    /**
     * 设置DeskStrategyTciNotifyAPI
     * @param deskStrategyTciNotifyAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setDeskStrategyTciNotifyAPI(DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI) {
        this.deskStrategyTciNotifyAPI = deskStrategyTciNotifyAPI;
        return this;
    }

    /**
     * 设置CbbVOIDeskStrategyMgmtAPI
     * @param cbbVOIDeskStrategyMgmtAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setCbbVOIDeskStrategyMgmtAPI(CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI) {
        this.cbbVOIDeskStrategyMgmtAPI = cbbVOIDeskStrategyMgmtAPI;
        return this;
    }

    /**
     * 设置AuditLogAPI
     * @param auditLogAPI API
     * @return AsyncUpdateDesktopStrategyThread
     */
    public AsyncUpdateDesktopStrategyThread setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
        return this;
    }
}
