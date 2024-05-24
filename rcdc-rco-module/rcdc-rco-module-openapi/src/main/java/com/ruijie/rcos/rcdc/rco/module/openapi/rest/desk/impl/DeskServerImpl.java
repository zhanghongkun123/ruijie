package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbObtainDeskConnectionInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.ResourceTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.ex.CbbDesktopNotExistsException;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.StoragePoolServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformStoragePoolDTO;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopWithUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CloudDeskStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.DeskStateInfo;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.QueryPlatformTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.desk.DeskStateInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.desk.UserDeskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.ForwardRcdcRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebClientRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ForwardRcdcResponse;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.desktop.common.DesktopStrategyCommonHelper;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolAssignResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request.CreateConnectFaultLogRequest;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.UserLicenseBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.utils.UserLicenseHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.AllotDeskErrorCodeMessageConverter;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InterfaceMethodUtil;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.WebClientAsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.DeskServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.*;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response.*;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread.*;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_DESK_NOT_RUNNING_STATE_REBOOT_FORBID;



/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
public class DeskServerImpl implements DeskServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskServerImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private CbbVDIDeskOperateAPI cbbVDIDeskOperateAPI;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private OpenApiTaskInfoAPI openApiTaskInfoAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserRecycleBinMgmtAPI recycleBinMgmtAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private DesktopPoolDashboardAPI desktopPoolDashboardAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI clusterServerMgmtAPI;

    @Autowired
    private StoragePoolServerMgmtAPI storagePoolServerMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private IpLimitAPI ipLimitAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private UamAppTestAPI uamAppTestAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private DesktopStrategyCommonHelper desktopStrategyCommonHelper;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private UserLicenseAPI userLicenseAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private CbbThirdPartyDeskOperateMgmtAPI cbbThirdPartyDeskOperateMgmtAPI;

    private static final String AES_KEY = "ECI584C47E58605D";

    private static final String DESK_ID = "desk_id";

    private static final int MAX_NUM = 1000;

    private static final Integer SUCCEED = 0;

    private static final int MAX_BATCH_EDIT_STRATEGY_NUM = 1000;

    private static final int MAX_BATCH_EDIT_DESK_REMARK_NUM = 1000;

    /**
     * 时间格式
     */
    private static final String TIME_PATTERN = "HH:mm:ss";

    private static final ExecutorService THREAD_POOL = ThreadExecutors.newBuilder("DeskServerThreadPool").maxThreadNum(20).queueSize(1000).build();

    @Override
    public CreateVDIDesktopResponse createVDIDesktopRequest(CreateVDIDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        // 校验内存步长，必须为512整倍数（单位是MB）
        validateMemoryStep(request.getMemory());

        UUID userId = null;
        try {
            userId = userInfoAPI.getUserIdByUserName(request.getUserName());
            // 非必填字段，取默认计算集群和默认存储池
            CreateVDIDesktopRequest defaultClusterInfo = getDefaultClusterInfo();
            if (Objects.isNull(request.getClusterId())) {
                request.setClusterId(defaultClusterInfo.getClusterId());
            }
            if (Objects.isNull(request.getStoragePoolId())) {
                request.setStoragePoolId(defaultClusterInfo.getStoragePoolId());
            }
            if (Objects.isNull(request.getPlatformId())) {
                request.setPlatformId(defaultClusterInfo.getPlatformId());
            }
        } catch (BusinessException e) {
            if (e.getKey().equals(BusinessKey.RCDC_USER_USER_MESSAGE_NOT_EXIST)) {
                LOGGER.error("user[{}] is not exist!", request.getUserName());
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CREATE_DESK_USER_NOT_EXIST, e);
            }
        }

        AsyncCreateVDIDesktopThread thread =
                new AsyncCreateVDIDesktopThread(userId, AsyncTaskEnum.CREATE_VDI, openApiTaskInfoAPI, userDesktopMgmtAPI, request);
        thread.setCbbVDIDeskStrategyMgmtAPI(cbbVDIDeskStrategyMgmtAPI);
        THREAD_POOL.execute(thread);

        CreateVDIDesktopResponse response = new CreateVDIDesktopResponse();
        response.setTaskId(thread.getCustomTaskId());
        return response;
    }

    @Override
    public CreateVDIDesktopResponse batchCreateVDIDesktopRequest(BatchCreateVDIDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        if (request.getDeskInfoArr().length > MAX_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_CREATE_DESK_OVER_NUM);
        }
        for (CreateVDIDesktopRequest item : request.getDeskInfoArr()) {
            // 校验内存步长，必须为512整倍数（单位是MB）
            validateMemoryStep(item.getMemory());
        }
        // 非必填字段，取默认计算集群和默认存储池
        CreateVDIDesktopRequest defaultClusterInfo = getDefaultClusterInfo();
        Arrays.stream(request.getDeskInfoArr()).forEach(item -> {
            if (Objects.isNull(item.getClusterId())) {
                item.setClusterId(defaultClusterInfo.getClusterId());
            }
            if (Objects.isNull(item.getStoragePoolId())) {
                item.setStoragePoolId(defaultClusterInfo.getStoragePoolId());
            }
            if (Objects.isNull(item.getPlatformId())) {
                item.setPlatformId(defaultClusterInfo.getPlatformId());
            }
        });
        AsyncBatchCreateVDIDesktopThread thread =
                new AsyncBatchCreateVDIDesktopThread(AsyncTaskEnum.BATCH_CREATE_VDI, openApiTaskInfoAPI, userDesktopMgmtAPI, request);
        thread.setUserInfoAPI(userInfoAPI);
        thread.setRecycleBinMgmtAPI(recycleBinMgmtAPI);
        thread.setCbbVDIDeskStrategyMgmtAPI(cbbVDIDeskStrategyMgmtAPI);
        String batchCreateVdiDesktopThreadMain = "batch_create_vdi_desktop_thread_main";
        ThreadExecutors.execute(batchCreateVdiDesktopThreadMain, thread);
        CreateVDIDesktopResponse response = new CreateVDIDesktopResponse();
        response.setTaskId(thread.getCustomTaskId());
        return response;
    }

    /**
     * 分页查询云桌面列表api
     *
     * @param request 查询请求体
     * @return 响应体
     */
    @Override
    public AbstractDeskPageQueryResponse pageQuery(DeskPageQueryServerRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        AbstractDeskPageQueryResponse response = new AbstractDeskPageQueryResponse();
        try {
            PageSearchRequest searchRequest = request.convert();
            DefaultPageResponse<CloudDesktopDTO> pageResponse;
            // RCCM根据用户名分级分权,关键词搜索只查云桌面名称和用户名两个字段
            String adminName = request.getAdminName();
            if (QueryPlatformTypeEnum.RCCM == request.getPlatformType() && org.apache.commons.lang3.StringUtils.isNotBlank(adminName)) {
                IacAdminDTO admin = adminMgmtAPI.getAdminByUserName(adminName);
                if (!permissionHelper.isAllGroupPermission(admin.getId())) {
                    appendDesktopIdMatchEqual(searchRequest, admin.getId());
                }
                pageResponse = userDesktopMgmtAPI.pageQuery(searchRequest, request.getPlatformType());
            } else {
                pageResponse = userDesktopMgmtAPI.pageQuery(searchRequest);
            }
            response.convert(pageResponse);
        } catch (BusinessException e) {
            LOGGER.error(e.getI18nMessage(), e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        } catch (Exception e) {
            LOGGER.error("OpenAPI分页查询云桌面列表系统异常", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }

        return response;
    }

    private void appendDesktopIdMatchEqual(PageSearchRequest request, UUID adminId) throws BusinessException {
        UUID[] uuidArr = permissionHelper.getDesktopIdArr(adminId);
        request.appendCustomMatchEqual(new MatchEqual("cbbDesktopId", uuidArr));
    }

    @Override
    public WebClientAsyncTaskResponse start(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, WebClientAsyncTaskResponse.class);
        }

        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        if (!userDesktopMgmtAPI.isAllowDesktopLogin(cloudDesktopDetailDTO.getDesktopStrategyId(), cloudDesktopDetailDTO.getDesktopName(),
                Boolean.FALSE, cloudDesktopDetailDTO.getDeskType())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_LOGIN_TIME_LIMIT);
        }

        // ip访问规则
        if (!Objects.isNull(webClientRequest) && webClientRequest.getTerminalIp() != null) {
            Boolean enableIpLimit = ipLimitAPI.isIpLimitEnable(deskId);
            if (Boolean.TRUE.equals(enableIpLimit)) {
                boolean isIpLimited = ipLimitAPI.isIpLimited(webClientRequest.getTerminalIp(), deskId);
                if (isIpLimited) {
                    throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_LOGIN_IP_LIMIT, webClientRequest.getTerminalIp());
                }
            }
        }

        WebClientAsyncTaskResponse webClientAsyncTaskResponse = new WebClientAsyncTaskResponse();
        Boolean isSupportCrossCpuVendor = Boolean.TRUE;
        if (webClientRequest != null && webClientRequest.getSupportCrossCpuVendor() != null) {
            isSupportCrossCpuVendor = webClientRequest.getSupportCrossCpuVendor();
        }
        AsyncTaskResponse asyncTaskResponse = start(deskId, isSupportCrossCpuVendor);
        webClientAsyncTaskResponse.setTaskId(asyncTaskResponse.getTaskId());
        return webClientAsyncTaskResponse;
    }


    /**
     * 启动云桌面
     *
     * @param deskId 云桌面id
     * @return 响应
     */
    private AsyncTaskResponse start(UUID deskId, Boolean supportCrossCpuVendor) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        AsyncStartDesktopThread thread = new AsyncStartDesktopThread(deskId, AsyncTaskEnum.START_DESK, openApiTaskInfoAPI, cloudDesktopOperateAPI);
        thread.setUserDiskMgmtAPI(userDiskMgmtAPI);
        thread.setSupportCrossCpuVendor(supportCrossCpuVendor);
        THREAD_POOL.execute(thread);

        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public WebClientAsyncTaskResponse shutdown(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, WebClientAsyncTaskResponse.class);
        }
        WebClientAsyncTaskResponse webClientAsyncTaskResponse = new WebClientAsyncTaskResponse();
        AsyncTaskResponse asyncTaskResponse = shutdown(deskId);
        webClientAsyncTaskResponse.setTaskId(asyncTaskResponse.getTaskId());
        return webClientAsyncTaskResponse;
    }

    /**
     * 关闭云桌面
     *
     * @param deskId 云桌面id
     * @return 响应
     */
    private AsyncTaskResponse shutdown(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        AsyncShutdownDesktopThread thread =
                new AsyncShutdownDesktopThread(deskId, AsyncTaskEnum.SHUTDOWN_DESK, openApiTaskInfoAPI, cloudDesktopOperateAPI);
        THREAD_POOL.execute(thread);

        return new AsyncTaskResponse(thread.getCustomTaskId());
    }


    @Override
    public WebClientAsyncTaskResponse powerOff(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, WebClientAsyncTaskResponse.class);
        }
        WebClientAsyncTaskResponse webClientAsyncTaskResponse = new WebClientAsyncTaskResponse();
        AsyncTaskResponse asyncTaskResponse = powerOff(deskId);
        webClientAsyncTaskResponse.setTaskId(asyncTaskResponse.getTaskId());
        return webClientAsyncTaskResponse;
    }

    private AsyncTaskResponse powerOff(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        AsyncPowerOffDesktopThread thread =
                new AsyncPowerOffDesktopThread(deskId, AsyncTaskEnum.POWER_OFF_DESK, openApiTaskInfoAPI, cloudDesktopOperateAPI);
        THREAD_POOL.execute(thread);

        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public void restart(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.findById(deskId);
            if (deskDTO.getDeskType() == CbbCloudDeskType.VDI) {
                cbbVDIDeskOperateAPI.rebootDeskVDI(deskId);
            } else {
                cloudDesktopOperateAPI.rebootDeskThird(deskId);
            }
        } catch (BusinessException e) {
            LOGGER.error("OpenAPI重启云桌面失败", e);
            if (e.getKey().equals(RCDC_CLOUDDESKTOP_DESK_NOT_RUNNING_STATE_REBOOT_FORBID)) {
                throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_RUNNING_STATE_REBOOT_FORBID, e);
            } else {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
            }
        } catch (Exception e) {
            LOGGER.error("OpenAPI重启云桌面系统异常，云桌面id=" + deskId, e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }

    @Override
    public AsyncTaskResponse deskSoftDelete(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        AsyncSoftDeleteVDIDesktopThread thread =
                new AsyncSoftDeleteVDIDesktopThread(deskId, AsyncTaskEnum.SOFT_DELETE_VDI, openApiTaskInfoAPI, userDesktopMgmtAPI);
        THREAD_POOL.execute(thread);

        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse batchDeskSoftDelete(BatchSoftDeleteArrRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        if (request.getDeskInfoArr().length > MAX_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SOFT_DELETE_DESK_OVER_NUM);
        }
        AsyncBatchSoftDeleteVDIDesktopThread thread =
                new AsyncBatchSoftDeleteVDIDesktopThread(AsyncTaskEnum.BATCH_SOFT_DELETE_VDI, openApiTaskInfoAPI, userDesktopMgmtAPI, request);
        String batchSoftDeleteVdiDesktopThreadMain = "batch_soft_delete_vdi_desktop_thread_main";
        ThreadExecutors.execute(batchSoftDeleteVdiDesktopThreadMain, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse deskModifyConfigurationModify(UUID deskId, DeskConfigurationModifyRequest request) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(request, "request must not be null");

        // 校验内存步长，必须为512整倍数（单位是MB）
        validateMemoryStep(request.getMemory());

        AsyncModifyDeskConfigurationThread thread = new AsyncModifyDeskConfigurationThread(deskId, AsyncTaskEnum.MODIFY_CONFIGURATION,
                openApiTaskInfoAPI, cbbVDIDeskMgmtAPI, cbbVDIDeskStrategyMgmtAPI);
        thread.setRequest(request);
        thread.setCbbDeskSpecAPI(cbbDeskSpecAPI);
        thread.setDeskSpecAPI(deskSpecAPI);
        thread.setCbbVDIDeskDiskAPI(cbbVDIDeskDiskAPI);
        ThreadExecutors.execute(Constant.MODIFY_DESKTOP_CONFIGURATION_THREAD, thread);

        return new AsyncTaskResponse(thread.getCustomTaskId());

    }

    @Override
    public List<DeskStateInfo> listDeskState(DeskStateInfoRequest deskStateInfoRequest) {
        Assert.notNull(deskStateInfoRequest, "deskStateInfoRequest must not be null");
        Assert.notNull(deskStateInfoRequest.getDeskIdList(), "deskIdList must not be null");

        List<DeskStateInfo> deskStateInfoList = new ArrayList<>();
        List<CloudDesktopDTO> cloudDesktopDtoList = userDesktopMgmtAPI.listDesktopByDesktopIds(deskStateInfoRequest.getDeskIdList());

        if (!CollectionUtils.isEmpty(cloudDesktopDtoList)) {
            cloudDesktopDtoList.forEach(item -> {
                DeskStateInfo deskStateInfoItem = new DeskStateInfo();
                deskStateInfoItem.setId(item.getId());
                if (!StringUtils.isEmpty(item.getDesktopState())) {
                    deskStateInfoItem.setCbbCloudDeskState(CbbCloudDeskState.valueOf(item.getDesktopState()));
                    deskStateInfoList.add(deskStateInfoItem);
                }
            });
        }

        return deskStateInfoList;
    }

    @Override
    public DesktopWithUserInfoDTO getDesktopInfoById(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");

        DesktopWithUserInfoDTO desktopInfoDTO = new DesktopWithUserInfoDTO();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = null;
        try {
            cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取桌面信息发生异常，桌面id: %s", deskId), e);
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_EXISTS, e);
        }

        BeanUtils.copyProperties(cloudDesktopDetailDTO, desktopInfoDTO);
        desktopInfoDTO.setDesktopId(cloudDesktopDetailDTO.getId());
        desktopInfoDTO.setRemoteWakeUp(UserCloudDeskTypeEnum.VDI.name().equals(cloudDesktopDetailDTO.getDesktopCategory()));
        if (CbbCloudDeskType.THIRD.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            desktopInfoDTO.setDesktopCategory(UserCloudDeskTypeEnum.THIRD);
        } else {
            desktopInfoDTO.setDesktopCategory(UserCloudDeskTypeEnum.valueOf(cloudDesktopDetailDTO.getCbbImageType()));
        }
        if (StringUtils.hasText(cloudDesktopDetailDTO.getDesktopType())) {
            desktopInfoDTO.setDesktopType(DesktopType.valueOf(cloudDesktopDetailDTO.getDesktopType()));
        }
        if (cloudDesktopDetailDTO.getUserType() != null) {
            desktopInfoDTO.setUserType(IacUserTypeEnum.valueOf(cloudDesktopDetailDTO.getUserType()));
        }
        desktopInfoDTO.setDesktopState(CbbCloudDeskState.valueOf(cloudDesktopDetailDTO.getDesktopState()));
        desktopInfoDTO.setSystemType(cloudDesktopDetailDTO.getDesktopImageType());
        desktopInfoDTO.setDisplayName(cloudDesktopDetailDTO.getUserRealName());
        desktopInfoDTO.setIdvTerminalMode("");
        if (!StringUtils.isEmpty(cloudDesktopDetailDTO.getTerminalId())
                && !UserCloudDeskTypeEnum.VDI.name().equals(cloudDesktopDetailDTO.getDesktopCategory())) {
            TerminalDTO terminalDTO = null;
            try {
                terminalDTO = userTerminalMgmtAPI.getTerminalById(cloudDesktopDetailDTO.getTerminalId());
            } catch (BusinessException e) {
                LOGGER.error("获取桌面终端详情发生异常，终端id: {}, ex: ", cloudDesktopDetailDTO.getTerminalId(), e);
            }
            if (terminalDTO != null) {
                if (terminalDTO.getIdvTerminalMode() != null) {
                    desktopInfoDTO.setIdvTerminalMode(terminalDTO.getIdvTerminalMode().toString());
                }
                if (terminalDTO.getSupportRemoteWake() != null) {
                    desktopInfoDTO.setRemoteWakeUp(terminalDTO.getSupportRemoteWake());
                }
                desktopInfoDTO.setTerminalState(terminalDTO.getTerminalState());
            }
        }
        ClusterInfoDTO clusterInfo = cloudDesktopDetailDTO.getClusterInfo();
        if (Objects.nonNull(clusterInfo)) {
            desktopInfoDTO.setClusterId(clusterInfo.getId());
            desktopInfoDTO.setClusterName(clusterInfo.getClusterName());
        }
        return desktopInfoDTO;
    }

    @Override
    public List<DesktopInfoDTO> getDesktopInfoByUserId(UserDeskRequest request) {
        Assert.notNull(request, "request is not null");
        Assert.notNull(request.getUserId(), "userId must not be null");

        List<DesktopInfoDTO> desktopInfoDTOList = new ArrayList<>();
        List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.getAllDesktopByUserId(request.getUserId());
        if (!CollectionUtils.isEmpty(cloudDesktopDTOList)) {
            cloudDesktopDTOList.forEach(item -> {
                DesktopInfoDTO desktopInfoDTO = new DesktopInfoDTO();
                BeanUtils.copyProperties(item, desktopInfoDTO);
                desktopInfoDTO.setDesktopId(item.getId());
                desktopInfoDTO.setSystemType(item.getDesktopImageType());
                if (StringUtils.hasText(item.getDesktopType())) {
                    desktopInfoDTO.setDesktopType(DesktopType.valueOf(item.getDesktopType()));
                }
                if (CbbCloudDeskType.THIRD.name().equals(item.getDesktopCategory())) {
                    desktopInfoDTO.setDesktopCategory(UserCloudDeskTypeEnum.THIRD);
                } else {
                    desktopInfoDTO.setDesktopCategory(UserCloudDeskTypeEnum.valueOf(item.getCbbImageType()));
                }
                desktopInfoDTO.setDesktopState(CbbCloudDeskState.valueOf(item.getDesktopState()));
                if (item.getUserType() != null) {
                    desktopInfoDTO.setUserType(IacUserTypeEnum.valueOf(item.getUserType()));
                }
                desktopInfoDTO.setRemoteWakeUp(UserCloudDeskTypeEnum.VDI.name().equals(item.getDesktopCategory()));
                desktopInfoDTO.setIdvTerminalMode("");
                if (!UserCloudDeskTypeEnum.VDI.name().equals(item.getDesktopCategory())) {
                    String terminalId = userTerminalMgmtAPI.getTerminalIdByBindDeskId(item.getId());
                    if (!StringUtils.isEmpty(terminalId)) {
                        TerminalDTO terminalDTO = null;
                        try {
                            terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
                        } catch (BusinessException e) {
                            LOGGER.error("获取桌面终端详情发生异常，终端id: {}, ex: ", terminalId, e);
                        }
                        if (terminalDTO != null) {
                            if (terminalDTO.getIdvTerminalMode() != null) {
                                desktopInfoDTO.setIdvTerminalMode(terminalDTO.getIdvTerminalMode().toString());
                            }

                            if (terminalDTO.getSupportRemoteWake() != null) {
                                desktopInfoDTO.setRemoteWakeUp(terminalDTO.getSupportRemoteWake());
                            }
                            desktopInfoDTO.setTerminalState(terminalDTO.getTerminalState());
                        }
                    }
                }
                if (Objects.nonNull(item.getClusterId())) {
                    ClusterInfoDTO clusterInfoDTO = clusterAPI.queryAvailableClusterById(item.getClusterId());
                    if (Objects.nonNull(clusterInfoDTO)) {
                        desktopInfoDTO.setClusterName(clusterInfoDTO.getClusterName());
                        desktopInfoDTO.setClusterId(item.getClusterId());
                    }
                }
                desktopInfoDTOList.add(desktopInfoDTO);
            });
        }

        return desktopInfoDTOList;
    }

    @Override
    public UserDesktopInfoDTO queryDeskInfo(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        try {
            if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
                String methodName = InterfaceMethodUtil.getCurrentMethodName();
                // RCenter进行请求转发
                return getForwardRcdcResponse(deskId, webClientRequest, methodName, UserDesktopInfoDTO.class);
            } else {
                CloudDesktopDetailDTO cloudDesktopDetail = userDesktopMgmtAPI.getDesktopDetailById(deskId);
                UserDesktopInfoDTO userDesktopInfoDTO = new UserDesktopInfoDTO();
                BeanUtils.copyProperties(cloudDesktopDetail, userDesktopInfoDTO);
                userDesktopInfoDTO.setDesktopId(deskId.toString());
                userDesktopInfoDTO.setImageName(cloudDesktopDetail.getDesktopImageName());
                userDesktopInfoDTO.setOsName(cloudDesktopDetail.getDesktopImageType().name());
                ClusterInfoDTO clusterInfo = cloudDesktopDetail.getClusterInfo();
                if (Objects.nonNull(clusterInfo)) {
                    userDesktopInfoDTO.setClusterId(clusterInfo.getId());
                    userDesktopInfoDTO.setClusterName(clusterInfo.getClusterName());
                }
                return userDesktopInfoDTO;
            }
        } catch (BusinessException e) {
            LOGGER.error(e.getI18nMessage(), e);
            if (e.getKey().equals(BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID)) {
                throw new BusinessException(RestErrorCode.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, e, deskId.toString());
            }
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        } catch (Exception e) {
            LOGGER.error("OpenAPI查询云桌面Id[{}]的信息系统异常", deskId, e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }

    @Override
    public CloudDeskStateDTO queryDeskState(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");

        CloudDeskStateDTO cloudDeskStateDTO = new CloudDeskStateDTO();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = null;
        try {
            cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            cloudDeskStateDTO.setCbbCloudDeskState(CbbCloudDeskState.valueOf(cloudDesktopDetailDTO.getDesktopState()));
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取桌面信息发生异常，桌面id: %s", deskId), e);
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_EXISTS, e);
        }

        if (!StringUtils.isEmpty(cloudDesktopDetailDTO.getTerminalId())) {
            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(cloudDesktopDetailDTO.getTerminalId());
                cloudDeskStateDTO.setCbbTerminalState(terminalDTO.getTerminalState());
            } catch (BusinessException e) {
                LOGGER.info("获取终端信息发生异常，终端id: {}, ex: ", cloudDesktopDetailDTO.getTerminalId());
                throw new BusinessException(RestErrorCode.OPEN_API_DESK_NOT_EXISTS, e);
            }
        }

        return cloudDeskStateDTO;
    }

    @Override
    public RemoteAssistResponse requestRemoteAssist(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        RemoteAssistResponse remoteAssistResponse = new RemoteAssistResponse();
        remoteAssistResponse.setBusinessCode(CommonMessageCode.SUCCESS);
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, RemoteAssistResponse.class);
        } else {
            try {
                userDesktopMgmtAPI.requestRemoteAssist(deskId);
            } catch (BusinessException ex) {
                if (ex.getKey().equals(BusinessKey.RCDC_USER_CLOUDDESKTOP_DESK_STATE_NOT_ALLOW)) {
                    throw new BusinessException(RestErrorCode.RCDC_USER_CLOUDDESKTOP_DESK_STATE_NOT_ALLOW, ex);
                } else if (ex instanceof CbbDesktopNotExistsException) {
                    throw new BusinessException(RestErrorCode.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, ex, deskId.toString());
                }
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, ex);
            }
            return remoteAssistResponse;
        }
    }

    @Override
    public RemoteAssistResponse cancelRemoteAssist(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        RemoteAssistResponse remoteAssistResponse = new RemoteAssistResponse();
        remoteAssistResponse.setBusinessCode(CommonMessageCode.SUCCESS);
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, RemoteAssistResponse.class);
        } else {
            try {
                userDesktopMgmtAPI.cancelRemoteAssist(deskId);
            } catch (Exception ex) {
                remoteAssistResponse.setBusinessCode(CommonMessageCode.CODE_ERR_OTHER);
                LOGGER.error("取消协助异常,桌面Id：{}", deskId, ex);
            }
            return remoteAssistResponse;
        }
    }

    @Override
    public RemoteAssistStatusResponse queryRemoteAssistStatus(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        RemoteAssistStatusResponse remoteAssistStatusResponse = new RemoteAssistStatusResponse();
        remoteAssistStatusResponse.setBusinessCode(CommonMessageCode.SUCCESS);
        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, RemoteAssistStatusResponse.class);
        } else {
            remoteAssistStatusResponse.setHasRequest(userDesktopMgmtAPI.queryRemoteAssistStatus(deskId));
        }
        return remoteAssistStatusResponse;
    }

    @Override
    public DeskConnectionInfoResponse getDeskConnectionInfo(UUID deskId, @Nullable WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        DeskConnectionInfoResponse deskConnectionInfoResponse = new DeskConnectionInfoResponse();

        // 为接入集群字段赋值
        if (webClientRequest != null && webClientRequest.getAccessClusterId() == null) {
            UUID accessClusterId = getAccessClusterId(webClientRequest);
            webClientRequest.setAccessClusterId(accessClusterId);
        }

        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            return getForwardRcdcResponse(deskId, webClientRequest, methodName, DeskConnectionInfoResponse.class);
        } else {
            CloudDesktopDetailDTO cloudDesktopDetailDTO;
            try {
                cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
            } catch (BusinessException e) {
                if (e.getKey().equals(BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID)) {
                    throw new BusinessException(RestErrorCode.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID, e, deskId.toString());
                }
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
            }
            if (webClientRequest != null && BooleanUtils.isTrue(webClientRequest.getEnableProxy())
                    && BooleanUtils.isFalse(cloudDesktopDetailDTO.getEnableAgreementAgency())) {
                throw new BusinessException(RestErrorCode.RCDC_RCO_CLOUDDESKTOP_STRATEGY_NOT_ENABLE_AGREEMENT_AGENCY,
                        cloudDesktopDetailDTO.getDesktopStrategyName());
            }
            if (BooleanUtils.isFalse(cloudDesktopDetailDTO.getEnableWebClient())) {
                throw new BusinessException(RestErrorCode.RCDC_RCO_CLOUDDESKTOP_STRATEGY_NOT_ENABLE_ENABLEWEB_CLIENT,
                        cloudDesktopDetailDTO.getDesktopStrategyName());
            }
            // 桌面抢占操作
            Optional.ofNullable(webClientRequest).map(WebClientRequest::getTerminalId)
                    .ifPresent(terminalId -> userDesktopMgmtAPI.desktopIsRobbed(deskId, terminalId));
            CbbObtainDeskConnectionInfoDTO deskConnectionInfo = cbbDeskMgmtAPI.getDeskConnectionInfo(deskId);
            if (!StringUtils.isEmpty(deskConnectionInfo.getIp())) {
                deskConnectionInfoResponse.setDesktopId(deskConnectionInfo.getDeskId());
                deskConnectionInfoResponse.setIp(deskConnectionInfo.getIp());
                deskConnectionInfoResponse.setPort(deskConnectionInfo.getPort().toString());
                deskConnectionInfoResponse.setEnableSsl(Objects.equals(deskConnectionInfo.getIsEncryption(), Boolean.TRUE.toString()));
                if (deskConnectionInfo.getSslPwd() != null) {
                    deskConnectionInfoResponse.setSslPwd(AesUtil.encrypt(deskConnectionInfo.getSslPwd(), AES_KEY));
                }
            }
            // 用户并发授权模式开启时，进行授权占用
            if (cbbLicenseCenterAPI.isActiveUserLicenseMode()) {
                UserSessionDTO userSessionDTO = new UserSessionDTO();

                userSessionDTO.setTerminalId(String.valueOf(getAccessClusterId(webClientRequest)));
                userSessionDTO.setTerminalType(TerminalTypeEnum.WEB_CLIENT);
                userSessionDTO.setResourceId(deskId);
                userSessionDTO.setResourceType(ResourceTypeEnum.DESK);
                UserLicenseHelper.updateTerminalReportTime(TerminalTypeEnum.WEB_CLIENT, userSessionDTO.getTerminalId());

                // 通过UserName查询UserId，获取当前桌面连接真实用户使用人
                if (webClientRequest != null && StringUtils.hasText(webClientRequest.getUserName())) {
                    String userName = webClientRequest.getUserName();
                    try {
                        UUID userId = userInfoAPI.getUserIdByUserName(userName);
                        userSessionDTO.setUserId(userId);
                        // 需要给网页版客户端返回真实的用户ID，否则在统一登录场景下，网页版客户端那边用户ID是随机生成的
                        deskConnectionInfoResponse.setUserId(userId);
                    } catch (BusinessException e) {
                        LOGGER.error("用户并发授权模式下，OPENAPI获取桌面连接信息，通过用户名[{}]查询用户ID查询报错，异常：", userName, e);
                        throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_USERNAME_FIND_USER_ID_FAIL, e, userName);
                    }
                } else {
                    LOGGER.error("用户并发授权模式下，OPENAPI获取桌面连接信息，通过用户名查询用户ID，不存在用户名信息，请求为：{}：", JSONObject.toJSONString(webClientRequest));
                    throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_USERNAME_NOT_BE_NULL);
                }
                UUID sessionId = userLicenseAPI.createUserSessionAndLicense(userSessionDTO);
                deskConnectionInfoResponse.setSessionId(sessionId);
            }
        }
        return deskConnectionInfoResponse;
    }

    /**
     * 根据本地时间字符串获取本地时间
     *
     * @param localTimeStr 本地时间字符串
     * @return 本地时间
     */
    private LocalTime getLocalTimeByHMS(String localTimeStr) {
        Assert.hasText(localTimeStr, "localTimeStr must has text");
        return LocalTime.parse(localTimeStr, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    @Override
    public UserDesktopInfoDTO getDesktopPoolResource(UUID deskId, WebClientRequest webClientRequest) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(webClientRequest, "webClientRequest is not null");

        if (webClientRequest != null && rccmManageAPI.isOtherClusterRequest(webClientRequest)) {
            String methodName = InterfaceMethodUtil.getCurrentMethodName();
            // RCenter进行请求转发
            UserDesktopInfoDTO desktopInfoDTO = getForwardRcdcResponse(deskId, webClientRequest, methodName, UserDesktopInfoDTO.class);
            desktopInfoDTO.setClusterId(webClientRequest.getClusterId());
            return desktopInfoDTO;
        }
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(webClientRequest.getUserName());
        if (Objects.isNull(cbbUserDetailDTO)) {
            LOGGER.error("获取桌面池资源时失败，用户[{}]不存在", webClientRequest.getUserName());
            throw new BusinessException(RestErrorCode.OPEN_API_USER_NOT_EXISTS);
        }
        CbbDesktopPoolDTO cbbDesktopPoolDTO;
        try {
            cbbDesktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(deskId);
        } catch (BusinessException e) {
            LOGGER.error(String.format("获取桌面池[%s]信息失败，桌面池不存在", deskId), e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_NOT_EXIST, e);
        }

        // ip访问规则
        if (Objects.nonNull(webClientRequest.getTerminalIp())) {
            if (ipLimitAPI.isIpLimitedByDeskStrategy(cbbDesktopPoolDTO.getPoolType(),
                    webClientRequest.getTerminalIp(), cbbDesktopPoolDTO.getStrategyId())) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_LOGIN_IP_LIMIT, webClientRequest.getTerminalIp());
            }
        }

        DesktopPoolAssignResultDTO assignResultDTO = desktopPoolUserMgmtAPI.assignDesktop(cbbUserDetailDTO.getId(), deskId);
        if (Objects.equals(assignResultDTO.getCode(), SUCCEED)) {
            CloudDesktopDetailDTO cloudDesktopDetail = userDesktopMgmtAPI.getDesktopDetailById(assignResultDTO.getDesktopId());
            UserDesktopInfoDTO userDesktopInfoDTO = new UserDesktopInfoDTO();
            BeanUtils.copyProperties(cloudDesktopDetail, userDesktopInfoDTO);
            userDesktopInfoDTO.setDesktopId(assignResultDTO.getDesktopId().toString());
            userDesktopInfoDTO.setImageName(cloudDesktopDetail.getDesktopImageName());
            userDesktopInfoDTO.setOsName(cloudDesktopDetail.getDesktopImageType().name());
            userDesktopInfoDTO.setClusterId(webClientRequest.getClusterId());
            return userDesktopInfoDTO;
        }
        return handAssignFailResult(cbbUserDetailDTO, cbbDesktopPoolDTO, assignResultDTO);
    }

    private UserDesktopInfoDTO handAssignFailResult(IacUserDetailDTO userDetail, CbbDesktopPoolDTO desktopPool,
                                                    DesktopPoolAssignResultDTO assignResult) throws BusinessException {
        desktopPoolUserMgmtAPI.saveAssignFailWarnLog(userDetail.getUserName(), desktopPool);

        // 记录会话连接失败记录
        recordConnectFaultLog(userDetail, desktopPool, assignResult);

        switch (assignResult.getCode()) {
            case DesktopPoolConstants.DESKTOP_POOL_STATUS_ERROR: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_STATUS_ERROR, desktopPool.getName());
            }
            case DesktopPoolConstants.DESKTOP_UNDER_MAINTENANCE: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_UNDER_MAINTENANCE, desktopPool.getName());
            }
            case DesktopPoolConstants.NO_AVAILABLE_DESKTOP: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_NO_RESOURCE, desktopPool.getName());
            }
            case DesktopPoolConstants.USER_NOT_ASSIGN_POOL: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_USER_NO_AUTH, userDetail.getUserName(),
                        desktopPool.getName());
            }
            case DesktopPoolConstants.DESKTOP_POOL_IMAGE_STATUS_ERROR: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_IMAGE_STATUS_ERROR, userDetail.getUserName(),
                        desktopPool.getName());
            }
            case DesktopPoolConstants.DESKTOP_USER_DISK_STATUS_ERROR: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_USER_DISK_STATUS_ERROR, assignResult.getMessage());
            }
            case DesktopPoolConstants.DESKTOP_LOGIN_TIME_LIMIT: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_LOGIN_TIME_LIMIT);
            }
            default: {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DESKTOP_POOL_ASSIGN_ERROR, desktopPool.getName());
            }
        }
    }

    private void recordConnectFaultLog(IacUserDetailDTO userDetail, CbbDesktopPoolDTO desktopPool, DesktopPoolAssignResultDTO assignResult) {
        CreateConnectFaultLogRequest request = new CreateConnectFaultLogRequest();
        request.setUserId(userDetail.getId());
        request.setUserName(userDetail.getUserName());
        request.setRelatedId(desktopPool.getId());
        request.setDesktopPoolName(desktopPool.getName());
        request.setDesktopPoolType(DesktopPoolType.convertToPoolDeskType(desktopPool.getPoolModel()));
        request.setCbbDesktopPoolType(desktopPool.getPoolType());
        request.setRelatedType(RelateTypeEnum.DESKTOP_POOL);
        request.setFaultType(AllotDeskErrorCodeMessageConverter.getAllocateFaultTypeByErrorCode(assignResult.getCode()));
        request.setFaultDesc(AllotDeskErrorCodeMessageConverter.getAllocateFaultDescByErrorCode(assignResult.getCode()));
        UUID userGroupId = userDetail.getGroupId();
        String groupName = userGroupId.toString();
        IacUserGroupDetailDTO userGroupDetail;
        try {
            userGroupDetail = cbbUserGroupAPI.getUserGroupDetail(userGroupId);
            groupName = userGroupDetail.getName();
        } catch (BusinessException e) {
            LOGGER.warn("查询用户组[" + userGroupId + "]信息异常，e=", e);
        }
        request.setUserGroupId(userGroupId);
        request.setUserGroupName(groupName);

        desktopPoolDashboardAPI.recordConnectFaultLog(request);
    }

    private <T> T getForwardRcdcResponse(UUID deskId, WebClientRequest webClientRequest,
                                         String methodName, Class<T> responseType) throws BusinessException {
        UUID clusterId = webClientRequest.getClusterId();
        Map<String, String> pathParam = new HashMap<>();
        pathParam.put(DESK_ID, deskId.toString());
        ForwardRcdcRequest request = getForwardRcdcRequest(clusterId, pathParam, methodName, (JSONObject) JSON.toJSON(webClientRequest));
        ForwardRcdcResponse forwardRcdcResponse = rccmManageAPI.forwardRequestByClusterId(request);
        if (forwardRcdcResponse.getResultCode() == CommonMessageCode.SUCCESS) {
            return forwardRcdcResponse.getContent().toJavaObject(responseType);
        }
        throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_FORWARD_RCDC_REQUEST_ERROR, forwardRcdcResponse.getMessage());
    }

    private ForwardRcdcRequest getForwardRcdcRequest(UUID clusterId, Map<String, String> pathParam, String methodName, JSONObject body) {
        Class<?> openAPIInterface = getOpenAPIInterface(this.getClass());
        Method interfaceMethod = InterfaceMethodUtil.selectInterfaceMethod(openAPIInterface, methodName, UUID.class, WebClientRequest.class);

        String openApiUrl = InterfaceMethodUtil.getOpenAPIUrl(interfaceMethod, pathParam);
        String httpMethod = InterfaceMethodUtil.getHttpMethod(interfaceMethod);
        return new ForwardRcdcRequest(clusterId, openApiUrl, httpMethod, body);
    }

    /**
     * 获得类对应的OpenAPI接口类对象
     *
     * @param clazz 类
     * @return 类对应的OpenAPI接口类对象
     */
    private Class<?> getOpenAPIInterface(Class<? extends DeskServerImpl> clazz) {
        Class<?>[] interfaceArr = clazz.getInterfaces();
        for (Class<?> anInterface : interfaceArr) {
            OpenAPI annotation = anInterface.getAnnotation(OpenAPI.class);
            if (annotation != null) {
                return anInterface;
            }
        }
        // 存在找不到方法
        return null;
    }

    @Override
    public AsyncTaskResponse restore(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        AsyncRestoreDesktopThread thread =
                new AsyncRestoreDesktopThread(deskId, AsyncTaskEnum.RESTORE_DESK, openApiTaskInfoAPI, cloudDesktopOperateAPI);
        THREAD_POOL.execute(thread);

        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse batchEditVDIDeskRemark(BatchEditVDIDeskRemarkRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        LOGGER.info("/V1/desk/batchEditVDIDeskRemark入参：{}", JSON.toJSONString(request));
        request.setDeskInfoArr(Arrays.stream(request.getDeskInfoArr())
                .filter(distinctByKey(EditVDIDeskRemarkRequest::getDeskId)).toArray(EditVDIDeskRemarkRequest[]::new));

        if (request.getDeskInfoArr().length > MAX_BATCH_EDIT_DESK_REMARK_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_EDIT_DESK_REMARK_PARAMER_INVALID);
        }

        AsyncBatchEditVDIDeskRemarkThread thread =
                new AsyncBatchEditVDIDeskRemarkThread(AsyncTaskEnum.BATCH_MODIFY_DESK_REMARK, openApiTaskInfoAPI, request);
        thread.setCbbVDIDeskMgmtAPI(cbbVDIDeskMgmtAPI).setUserDesktopMgmtAPI(userDesktopMgmtAPI);

        String batchThreadMain = "batch_edit_vdi_desk_remark_thread_main";
        ThreadExecutors.execute(batchThreadMain, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());

    }

    @Override
    public AsyncTaskResponse batchEditStrategy(BatchEditStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        LOGGER.info("/V1/desk/batchEditStrategy入参：{}", JSON.toJSONString(request));
        request.setDeskInfoArr(Arrays.stream(request.getDeskInfoArr())
                .filter(distinctByKey(EditStrategyRequest::getDeskId)).toArray(EditStrategyRequest[]::new));

        if (request.getDeskInfoArr().length > MAX_BATCH_EDIT_STRATEGY_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_EDIT_DESK_STRATEGY_PARAMER_INVALID);
        }

        AsyncBatchEditStrategyThread thread =
                new AsyncBatchEditStrategyThread(AsyncTaskEnum.BATCH_EDIT_DESK_STRATEGY, openApiTaskInfoAPI, request);
        thread.setUserDesktopMgmtAPI(userDesktopMgmtAPI).setDesktopSpecAPI(deskStrategyAPI).setDeskStrategyTciNotifyAPI(deskStrategyTciNotifyAPI)
                .setAppDeliveryMgmtAPI(appDeliveryMgmtAPI).setCbbIDVDeskMgmtAPI(cbbIDVDeskMgmtAPI).setUamAppTestAPI(uamAppTestAPI)
                .setDesktopStrategyCommonHelper(desktopStrategyCommonHelper).setDesktopSpecAPI(deskSpecAPI);
        String batchThreadMain = "batch_edit_vdi_desk_strategy_thread_main";
        ThreadExecutors.execute(batchThreadMain, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    private CreateVDIDesktopRequest getDefaultClusterInfo() throws BusinessException {
        PlatformComputerClusterDTO computerClusterDTO = clusterServerMgmtAPI.getDefaultComputeCluster();
        UUID storagePoolId = storagePoolServerMgmtAPI.getStoragePoolByComputeClusterId(computerClusterDTO.getId())
                .stream().findFirst().map(PlatformStoragePoolDTO::getId).orElse(Constant.DEFAULT_STORAGE_POOL_ID);
        CreateVDIDesktopRequest request = new CreateVDIDesktopRequest();
        request.setClusterId(computerClusterDTO.getId());
        request.setStoragePoolId(storagePoolId);
        request.setPlatformId(computerClusterDTO.getPlatformId());
        return request;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private UUID getAccessClusterId(WebClientRequest webClientRequest) {
        UUID accessClusterId;
        if (webClientRequest != null && webClientRequest.getAccessClusterId() != null) {
            accessClusterId = webClientRequest.getAccessClusterId();
        } else {
            accessClusterId = getCurrentClusterId();
        }
        return accessClusterId;
    }

    private UUID getCurrentClusterId() {
        UUID currentClusterId = Constants.DEFAULT_CLUSTER_ID;
        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        if (rccmServerConfig != null && rccmServerConfig.getClusterId() != null) {
            currentClusterId = rccmServerConfig.getClusterId();
        }
        return currentClusterId;
    }

    private void validateMemoryStep(Integer memory) throws BusinessException {
        if (memory != null && memory % Constants.MEMORY_MB_STEP != NumberUtils.INTEGER_ZERO) {
            throw new BusinessException(RestErrorCode.OPEN_API_DESK_MEMORY_CONFIGURATION_PARAMER_LIMIT);
        }
    }
}
