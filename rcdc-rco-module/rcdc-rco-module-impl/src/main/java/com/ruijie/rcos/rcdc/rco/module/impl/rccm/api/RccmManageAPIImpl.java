package com.ruijie.rcos.rcdc.rco.module.impl.rccm.api;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.base.iac.module.exception.LoginFailException;
import com.ruijie.rcos.gss.base.iac.module.exception.LoginFailLockException;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdGroupAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacValidateAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacEncryptionMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskStrategyCommonAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskStrategyState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ImageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.SystemVersionMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.image.replication.DeleteReplicationRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.image.replication.QueryImageSyncTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.RccmUnifiedManageRoleEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ForwardRcdcResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm.RccmCheckExitManageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm.RccmManageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.RccmRestKey;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.UserInfoService;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.RequestParamDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.RccmManageBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.RccmManageConstant;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.ViewRccmSyncUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.ViewRccmSyncUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service.UserLicenseService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.RestUtil;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.connectkit.api.data.base.RemoteResponse;
import com.ruijie.rcos.sk.connectkit.api.data.base.RemoteResponseContent;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.DEFAULT_CLUSTER_ID;

/**
 * Description: RccmManageAPIImpl
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月8日
 *
 * @author lihengjing
 */
public class RccmManageAPIImpl implements RccmManageAPI {

    private static final ThreadExecutor DELETE_REPLICATION_POOL =
            ThreadExecutors.newBuilder("rccm_delete_replication_pool").maxThreadNum(10).queueSize(1000).build();

    private static final Logger LOGGER = LoggerFactory.getLogger(RccmManageAPIImpl.class);

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RestUtil restUtil;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    @Autowired
    private IacAdGroupAPI cbbAdGroupAPI;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private SystemVersionMgmtAPI systemVersionMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    @Autowired
    private CbbDeskStrategyCommonAPI cbbDeskStrategyCommonAPI;

    @Autowired
    private ImageMgmtAPI imageMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private ViewRccmSyncUserService viewRccmSyncUserService;

    @Autowired
    private UserLicenseService userLicenseService;

    private static final ReentrantLock RCCM_SYNC_USER_LOCK = new ReentrantLock();

    /**
     * 创建一个单一一个队列的线程，用于跑镜像备份业务逻辑
     */
    private ExecutorService rccmSyncUserExecutorService;

    /**
     * 最大线程数
     */
    private static final int MAX_THREAD_NUM = 10;

    /**
     * 队列长度
     */
    private static final int QUEUE_SIZE = 100;

    /**
     * 推送用户数据到rccm,每次处理1千条
     */
    private static final int PUSH_USER_MAX_NUM = 1000;

    /**
     * 线程池名称
     */
    private static final String THREAD_POOL_NAME = "push-user-to-rccm";

    private static final String UNIFIED_MANAGE_THREAD_POOL_NAME = "unified-manage-to-rccm";

    private static final String CLUSTER_ID = "clusterId";

    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    private static final ThreadExecutor UNIFIED_MANAGE_THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(UNIFIED_MANAGE_THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Override
    public RccmManageResponse joinManage(RccmManageRequest rccmManageRequest) throws BusinessException {
        // 这个方法不处理统一登入开关变更，如果加入前是RCCM统一登入开关为开启会接收
        Assert.notNull(rccmManageRequest, "JoinManageRequest must be not null");

        if (StringUtils.hasText(rccmManageRequest.getPassword())) {
            try {
                AesUtil.descrypt(rccmManageRequest.getPassword(), RedLineUtil.getRealAdminRedLine());
            } catch (Exception ex) {
                LOGGER.warn("password信息解密发生异常");
                return new RccmManageResponse(CommonMessageCode.FAIL_CODE,
                        LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_RCCM_PASSWORD_ERROR));
            }
        }

        RccmServerConfigDTO newRccmServerConfigDTO = initRccmServerConfigDTO(rccmManageRequest);

        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        // 存在纳管记录，比对纳管是否同一个服务器
        // 是否已经加入纳管
        if (rccmServerConfig != null && rccmServerConfig.getHealth()) {
            // 是否R-Center发起心跳检测
            boolean isHeartbeat = true;
            // 比对IP、clusterId
            if (rccmServerConfig.getServerIp().equals(rccmManageRequest.getServerIp())) {
                if (rccmServerConfig.getClusterId().equals(rccmManageRequest.getClusterId())) {
                    // 同一服务器 非多次部署，可能是触发编辑
                    boolean hasNotifyListen = false;
                    if (checkServerConfigChange(rccmServerConfig, rccmManageRequest)) {
                        // 检查Rccm配置是否变化 如果变化则进行更新
                        hasNotifyListen = true;
                        // 更新连接信息 网关端口、账号、密码、代理开关、代理信息
                        // 不更新统一登入开关
                        RccmServerConfigDTO target = new RccmServerConfigDTO();
                        target.setGatewayPort(rccmManageRequest.getGatewayPort());
                        target.setAccount(rccmManageRequest.getAccount());
                        target.setPassword(rccmManageRequest.getPassword());
                        target.setHasProxy(rccmManageRequest.getHasProxy());
                        target.setProxyIp(rccmManageRequest.getProxyIp());
                        target.setProxyPort(rccmManageRequest.getProxyPort());
                        // 收到纳管请求,主动设置健康为true
                        target.setHealth(true);
                        target.setEnableAssistAuth(rccmManageRequest.getEnableAssistAuth());
                        rccmManageService.updateRccmServerConfig(target);
                        isHeartbeat = false;
                    }
                    if (!rccmServerConfig.getListen() || hasNotifyListen) {
                        // 监听未建立 或者rccm配置有变化，通知建立反向连接
                        rccmManageService.notifyListenRccmServer();
                    }
                } else {
                    // 同一台服务器 重新部署RCCM需要重新建立连接
                    rccmManageService.updateRccmServerConfig(newRccmServerConfigDTO);
                    // 通知建立反向连接
                    rccmManageService.notifyListenRccmServer();
                    isHeartbeat = false;
                }
                LOGGER.info(String.format("成功加入RCCM（服务器IP：%s）纳管，是否心跳请求：%s", rccmManageRequest.getServerIp(), isHeartbeat));
                // 推送用户信息到rccm
                if (!isHeartbeat) {
                    pushAllUserToRccm();
                }
                return new RccmManageResponse(CommonMessageCode.SUCCESS);
            }

            // 已加入纳管通知 纳管失败
            LOGGER.warn(String.format("加入RCCM（服务器IP：%s）纳管失败，由于已经加入RCCM纳管（服务器IP：%s），",
                    rccmManageRequest.getServerIp(), rccmServerConfig.getServerIp()));
            return new RccmManageResponse(CommonMessageCode.FAIL_CODE,
                    LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_ALREADY_JOIN_OTHER_RCCM));
        }
        rccmManageService.saveOrUpdateRccmServerConfig(newRccmServerConfigDTO);
        rccmManageService.notifyListenRccmServer();
        LOGGER.info(String.format("成功加入RCCM（服务器IP：%s）纳管", rccmManageRequest.getServerIp()));
        // 推送用户信息到rccm
        pushAllUserToRccm();
        return new RccmManageResponse(CommonMessageCode.SUCCESS);
    }

    @Override
    public void pushUserToRccm(List<String> userNameList, Boolean isDirectPush) {
        Assert.notEmpty(userNameList, "userNameList is empty");
        Assert.notNull(isDirectPush, "isDirectPush is null");
        if (CollectionUtils.isEmpty(userNameList)) {
            return;
        }
        List<List<String>> userNamePartitionList = Lists.partition(userNameList, PUSH_USER_MAX_NUM);
        // 分批推送
        for (List<String> nameList : userNamePartitionList) {
            THREAD_EXECUTOR.execute(() -> {
                // 推送用户数据
                rccmManageService.pushUser(nameList, isDirectPush);
            });
        }
    }

    /**
     * 推送用户信息到rccm
     */
    @Override
    public void pushAllUserToRccm() throws BusinessException {
        RCCM_SYNC_USER_LOCK.lock();
        try {
            // 如果存在任务执行，先强制关闭（前面任务停止，后面任务执行）
            shutdownNow(rccmSyncUserExecutorService);

            ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(RccmManageConstant.RCCM_SYNC_USER).build();
            rccmSyncUserExecutorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(1), threadFactory);

            rccmSyncUserExecutorService.execute(this::pushRccmUser);
        } finally {
            RCCM_SYNC_USER_LOCK.unlock();
        }
    }

    @SuppressWarnings({"PMD.AvoidDoWhileRule"})
    private void pushRccmUser() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        pushAdGroupToRccm();
        // 1.查询有池桌面资源的用户 2.查询有普通桌面的用
        Page<ViewRccmSyncUserEntity> viewRccmSyncUserPage;
        int pageSize = 0;
        do {
            Pageable pageable = PageRequest.of(pageSize++, PUSH_USER_MAX_NUM);
            viewRccmSyncUserPage = viewRccmSyncUserService.pageViewRccmSyncUser(pageable);
            if (!viewRccmSyncUserPage.hasContent()) {
                break;
            }
            List<String> subUserList = viewRccmSyncUserPage.stream().map(ViewRccmSyncUserEntity::getUserName).collect(Collectors.toList());
            LOGGER.info("推送有桌面资源的用户信息到RCCM,集合大小:{},当前页码：{}", viewRccmSyncUserPage.getTotalElements(), pageSize);
            rccmManageService.pushUser(subUserList, true);
        } while (viewRccmSyncUserPage.hasNext());

        stopWatch.stop();
        LOGGER.info("推送有桌面资源的用户信息到RCCM,集合大小:{}, 耗时：{} 毫秒", viewRccmSyncUserPage.getTotalElements(), stopWatch.getTotalTimeMillis());
    }

    private void shutdownNow(ExecutorService executorService) {
        if (executorService == null || executorService.isShutdown()) {
            // 不存在线程，直接返回
            return;
        }
        executorService.shutdownNow();
        while (!executorService.isTerminated()) {
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("等待结束流程 awaitTermination error", e);
                Thread.currentThread().interrupt();
            }
        }
    }


    private RccmServerConfigDTO initRccmServerConfigDTO(RccmManageRequest rccmManageRequest) {
        RccmServerConfigDTO rccmServerConfigDTO = new RccmServerConfigDTO();
        rccmServerConfigDTO.setHasJoin(true);
        rccmServerConfigDTO.setListen(false);
        rccmServerConfigDTO.setServerIp(rccmManageRequest.getServerIp());
        rccmServerConfigDTO.setGatewayPort(rccmManageRequest.getGatewayPort());
        rccmServerConfigDTO.setAccount(rccmManageRequest.getAccount());
        rccmServerConfigDTO.setPassword(rccmManageRequest.getPassword());
        rccmServerConfigDTO.setHasProxy(rccmManageRequest.getHasProxy());
        rccmServerConfigDTO.setProxyIp(rccmManageRequest.getProxyIp());
        rccmServerConfigDTO.setProxyPort(rccmManageRequest.getProxyPort());
        rccmServerConfigDTO.setClusterId(rccmManageRequest.getClusterId());
        // 收到纳管请求,主动设置健康为true
        rccmServerConfigDTO.setHealth(true);
        rccmServerConfigDTO.setHasNewJoin(true);
        rccmServerConfigDTO.setHasUnifiedLogin(rccmManageRequest.getHasUnifiedLogin() != null && rccmManageRequest.getHasUnifiedLogin());
        rccmServerConfigDTO.setEnableAssistAuth(rccmManageRequest.getEnableAssistAuth() != null && rccmManageRequest.getEnableAssistAuth());
        return rccmServerConfigDTO;
    }

    private boolean checkServerConfigChange(RccmServerConfigDTO dto, RccmManageRequest request) {
        if (dto == null) {
            return true;
        }
        return !dto.getServerIp().equals(request.getServerIp()) || !dto.getGatewayPort().equals(request.getGatewayPort())
                || (request.getAccount() != null && !dto.getAccount().equals(request.getAccount()))
                || (request.getEnableAssistAuth() != null && !dto.getEnableAssistAuth().equals(request.getEnableAssistAuth()))
                || (request.getPassword() != null && !dto.getPassword().equals(request.getPassword()))
                || !request.getHasProxy().equals(dto.getHasProxy())
                || (request.getProxyIp() != null && dto.getHasProxy() && dto.getProxyIp() != null && !dto.getProxyIp().equals(request.getProxyIp()))
                || (request.getProxyPort() != null && dto.getHasProxy() && dto.getProxyPort() != null
                && !dto.getProxyPort().equals(request.getProxyPort()));
    }

    @Override
    public RccmManageResponse updateUnifiedLogin(RccmManageRequest rccmManageRequest) throws BusinessException {
        Assert.notNull(rccmManageRequest, "JoinManageRequest must be not null");

        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        if (checkSameServer(rccmServerConfig, rccmManageRequest)) {
            rccmManageService.updateUnifiedLogin(rccmManageRequest.getHasUnifiedLogin(), rccmManageRequest.getEnableAssistAuth());
            LOGGER.info(String.format("RCCM（服务器IP：%s）更新统一登入开关为：%s",
                    rccmManageRequest.getServerIp(), rccmManageRequest.getHasUnifiedLogin()));
            return new RccmManageResponse(CommonMessageCode.SUCCESS);
        }

        // 不存在纳管记录，无法进行统一登入开关变更
        LOGGER.error(String.format("未加入RCCM（服务器IP：%s）纳管无法进行统一登入开关变更", rccmManageRequest.getServerIp()));
        return new RccmManageResponse(CommonMessageCode.FAIL_CODE,
                LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_NOT_JOIN_RCCM));
    }

    @Override
    public RccmManageResponse exitManage(RccmManageRequest rccmManageRequest) throws BusinessException {
        Assert.notNull(rccmManageRequest, "JoinManageRequest must be not null");

        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        if (checkSameServer(rccmServerConfig, rccmManageRequest)) {
            exitManageConfig(rccmServerConfig);
            return new RccmManageResponse(CommonMessageCode.SUCCESS);
        }

        // 不存在纳管记录，无法进行统一登入开关变更
        LOGGER.error(String.format("未加入RCCM（服务器IP：%s）纳管无法进行退出纳管", rccmManageRequest.getServerIp()));

        return new RccmManageResponse(CommonMessageCode.FAIL_CODE,
                LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_NOT_JOIN_RCCM));
    }

    @Override
    public void activeExitManage() throws BusinessException {
        RccmServerConfigDTO serverConfig = rccmManageService.getRccmServerConfig();

        // 当前没有镜像同步任务时才允许退出
        if (Objects.nonNull(serverConfig) && serverConfig.hasHealth()) {
            // 在线:请求rcenter,判断是否存在镜像同步任务
            RccmCheckExitManageRequest exitManageRequest = new RccmCheckExitManageRequest();
            exitManageRequest.setClusterId(serverConfig.getClusterId());
            RequestParamDTO<RccmCheckExitManageRequest> requestParam =
                    restUtil.buildRccmRequestParamDTO(serverConfig, RccmRestKey.CHECK_EXIT_MANAGE, exitManageRequest);
            RccmCheckExitManageResponse checkResponse = new RccmCheckExitManageResponse();
            try {
                RemoteResponse<JSONObject> remoteResponse = restUtil.onceRequest(requestParam);
                RemoteResponseContent<JSONObject> content = remoteResponse.getContent();
                if (CommonMessageCode.SUCCESS == content.getCode()) {
                    checkResponse = content.getData().toJavaObject(RccmCheckExitManageResponse.class);
                }
            } catch (Exception ex) {
                LOGGER.error("请求RCCM[{}]检查是否可以退出纳管接口异常", serverConfig.getServerIp(), ex);
                checkUnifiedManageDataTaskAndExitManageConfig(serverConfig);
                return;
            }
            if (!checkResponse.isAllow()) {
                auditLogAPI.recordLog(RccmManageBusinessKey.RCDC_RCCM_UNIFIED_MANAGE_EXIST_IMAGE_SYNC);
                throw new BusinessException(RccmManageBusinessKey.RCDC_RCCM_UNIFIED_MANAGE_EXIST_IMAGE_SYNC);
            }
        }
        checkUnifiedManageDataTaskAndExitManageConfig(serverConfig);
    }

    private void checkUnifiedManageDataTaskAndExitManageConfig(RccmServerConfigDTO serverConfig) throws BusinessException {
        // 根据manageData关联表,查询是否存在状态机任务
        List<UnifiedManageDataEntity> manageDataList = unifiedManageDataService.findByRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
        if (manageDataList.stream().anyMatch(manageData -> isExistStateMachineTask(manageData.getRelatedId()))) {
            auditLogAPI.recordLog(RccmManageBusinessKey.RCDC_RCCM_UNIFIED_MANAGE_EXIST_IMAGE_SYNC);
            throw new BusinessException(RccmManageBusinessKey.RCDC_RCCM_UNIFIED_MANAGE_EXIST_IMAGE_SYNC);
        }
        exitManageConfig(serverConfig);
    }

    private void exitManageConfig(RccmServerConfigDTO rccmServerConfig) throws BusinessException {
        //退出纳管前 恢复辅助认证策略  原设置信息
        boolean hasUnifiedLogin = false;
        rccmManageService.updateUnifiedLogin(hasUnifiedLogin, hasUnifiedLogin);

        // 删除同步策略配置,清理数据
        RccmUnifiedManageNotifyConfigRequest unifiedManageNotifyConfig = new RccmUnifiedManageNotifyConfigRequest();
        unifiedManageNotifyConfig.setClusterId(rccmServerConfig.getClusterId());
        unifiedManageNotifyConfig.setClusterUnifiedManageStrategyDTOList(new ArrayList<>());
        notifyStrategy(unifiedManageNotifyConfig);

        rccmManageService.exitJoinRccmServer();
        LOGGER.info("退出集群管理中心[{}]纳管成功", rccmServerConfig.getServerIp());

        // 退出纳管，网页版客户端用户会话终端ID恢复默认ID
        userLicenseService.updateWebClientUserSessionInfo(rccmServerConfig.getClusterId(), DEFAULT_CLUSTER_ID);
    }

    @Override
    public RccmManageResponse existingManage(String clusterId) {
        Assert.notNull(clusterId, "clusterId must be not null");
        return testHealthState(new RccmManageStateRequest(UUID.fromString(clusterId), ""));
    }

    @Override
    public boolean canModifyGlobalSettings() {
        return rccmManageService.canModifyGlobalSettings();
    }

    @Override
    public void delRccmUserCLuster(List<String> usernameList, Boolean force) {
        Assert.notNull(usernameList, "usernameList must not be empty");
        Assert.notNull(force, "force must not be null");
        if (CollectionUtils.isEmpty(usernameList)) {
            return;
        }

        RccmServerConfigDTO rccmServerConfig = getRccmServerConfig();
        if (rccmServerConfig == null || !rccmServerConfig.getHasJoin() || !rccmServerConfig.getHealth()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("RCCM服务未就绪，取消删除用户数据推送");
            }
            return;
        }

        List<List<String>> userNamePartitionList = Lists.partition(usernameList, PUSH_USER_MAX_NUM);
        for (List<String> nameList : userNamePartitionList) {
            doDelRccmUserCLuster(nameList, force, rccmServerConfig);
        }
    }

    private void doDelRccmUserCLuster(List<String> usernameList, Boolean force, RccmServerConfigDTO rccmServerConfig) {
        if (CollectionUtils.isEmpty(usernameList)) {
            return;
        }
        // 需要判断用户是否有桌面资源
        if (!force) {
            // 根据用户名查询是否有桌面资源,保留无桌面资源的用户
            List<String> existList = userInfoService.getUserDesktopResource(usernameList);
            usernameList = usernameList.stream().filter(item -> !existList.contains(item)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(usernameList)) {
                return;
            }
        }

        RcdcUserClusterRestRequest rcdcUserClusterRestRequest = new RcdcUserClusterRestRequest();
        rcdcUserClusterRestRequest.setClusterId(rccmServerConfig.getClusterId());
        rcdcUserClusterRestRequest.setUsernameList(usernameList);
        RequestParamDTO<RcdcUserClusterRestRequest> requestParamDTO =
                restUtil.buildRccmRequestParamDTO(rccmServerConfig, RccmRestKey.NOTICE_RCCM_DEL_USER_CLUSTER, rcdcUserClusterRestRequest);
        try {
            restUtil.onceRequest(requestParamDTO);
        } catch (BusinessException ex) {
            LOGGER.error("通知到RCCM删除用户集群缓存异常" + ex.getMessage());
        }
    }

    @Override
    public boolean isReadyToPush() {
        RccmServerConfigDTO serverConfig = rccmManageService.getRccmServerConfig();
        if (Objects.nonNull(serverConfig) && serverConfig.hasHealth()) {
            return true;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("RCCM服务未就绪，取消删除用户数据推送");
        }
        return false;
    }

    @Override
    public boolean isUnifiedLogin() {
        return rccmManageService.isUnifiedLogin();
    }

    @Override
    public RccmServerConfigDTO getRccmServerConfig() {
        return rccmManageService.getRccmServerConfig();
    }

    @Override
    public boolean getRccmServerHasUnifiedLoginConfig() {

        return rccmManageService.getRccmServerHasUnifiedLoginConfig();
    }


    @Override
    public boolean existUser(String username) {
        Assert.notNull(username, "username is not null");
        try {
            UUID userId = userInfoService.getUserIdByUserName(username);
            if (userId != null) {
                return true;
            }
        } catch (BusinessException e) {
            return false;
        }
        return false;
    }

    @Override
    public RccmManageResponse testHealthState(RccmManageStateRequest request) {
        Assert.notNull(request, "request must be not null");
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        if (rccmServerConfig == null) {
            return new RccmManageResponse(CommonMessageCode.EXIT_RCCM_MANAGED, LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_NOT_JOIN_RCCM));
        }
        if (Boolean.FALSE.equals(rccmServerConfig.getHasJoin())) {
            return new RccmManageResponse(CommonMessageCode.EXIT_RCCM_MANAGED, LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_NOT_JOIN_RCCM));
        }
        if (Boolean.TRUE.equals(rccmServerConfig.getHasJoin()) && rccmServerConfig.getClusterId().equals(request.getClusterId())) {
            if (!rccmServerConfig.getServerIp().equals(request.getServerIp())) {
                return new RccmManageResponse(CommonMessageCode.RCCM_VIP_ERROR,
                        LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_RCCM_VIP_ERROR));
            }
            return new RccmManageResponse(CommonMessageCode.SUCCESS);
        } else {
            // 被其他rccm纳管了
            return new RccmManageResponse(CommonMessageCode.OTHER_RCCM_MANAGED, LocaleI18nResolver.resolve
                    (RccmManageBusinessKey.RCDC_ALREADY_JOIN_OTHER_RCCM));
        }
    }

    @Override
    public void updateRccmVIP(UUID clusterId, String serverIp) {
        Assert.notNull(clusterId, "clusterId must be not null");
        Assert.notNull(serverIp, "serverIp must be not null");
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        if (rccmServerConfig == null) {
            return;
        }
        if (Boolean.TRUE.equals(rccmServerConfig.getHasJoin()) && rccmServerConfig.getClusterId().equals(clusterId)) {
            if (!rccmServerConfig.getServerIp().equals(serverIp)) {
                RccmServerConfigDTO target = new RccmServerConfigDTO();
                target.setServerIp(serverIp);
                rccmManageService.updateRccmServerConfig(target);
            }
        }
    }

    @Override
    public IacAdminDTO validateAdminPwd(VerifyAdminRequest request) throws BusinessException {
        Assert.notNull(request, "request must be not null");
        IacValidateAdminPwdRequest validateRequest = new IacValidateAdminPwdRequest();
        validateRequest.setUserName(request.getUserName());
        validateRequest.setPwd(request.getPassword());
        validateRequest.setEncryptionMode(IacEncryptionMode.SHA256);
        validateRequest.setLoginIp(null);
        validateRequest.setSubSystem(SubSystem.CDC);
        baseAdminMgmtAPI.validateAdminPwd(validateRequest);

        IacAdminDTO iacAdminDTO = adminMgmtAPI.getAdminByUserName(request.getUserName());
        // 为了获取needUpdatePassword，身份中心只能在getAdmin方法获取needUpdatePassword这些信息
        return baseAdminMgmtAPI.getAdmin(iacAdminDTO.getId());
    }

    @Override
    public IacAdminDTO loginAdminAuth(VerifyAdminRequest request) throws BusinessException {
        Assert.notNull(request, "request must be not null");
        String userName = request.getUserName();
        try {
            // 此处token获取保留只校验账号密码，不去触发登录，防止出现内嵌界面登录失效的情况
            IacValidateAdminPwdRequest validateRequest = new IacValidateAdminPwdRequest();
            validateRequest.setUserName(request.getUserName());
            validateRequest.setPwd(request.getPassword());
            validateRequest.setSubSystem(SubSystem.CDC);
            validateRequest.setKey(RedLineUtil.getRealAdminRedLine());
            validateRequest.setEncryptionMode(IacEncryptionMode.SHA256);
            baseAdminMgmtAPI.validateAdminPwd(validateRequest);

            return adminMgmtAPI.getAdminByUserName(request.getUserName());
        } catch (BusinessException ex) {
            LOGGER.error("管理员[{}]登录验证异常，异常原因：{}", userName, ex.getI18nMessage(), ex);

            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_LOGIN_FAIL_BY_ADMIN_NAME, ex, userName,
                    ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));
            throw ex;
        }
    }

    @Override
    public boolean isOtherClusterRequest(@Nullable WebClientRequest webClientRequest) {
        return rccmManageService.isUnifiedLogin() && webClientRequest != null && webClientRequest.getClusterId() != null
                && !rccmManageService.getRccmServerConfig().getClusterId().equals(webClientRequest.getClusterId());
    }

    @Override
    public ForwardRcdcResponse forwardRequestByClusterId(ForwardRcdcRequest requestData) throws BusinessException {
        Assert.notNull(requestData, "requestData must be not null");
        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();

        RequestParamDTO<ForwardRcdcRequest> requestParamDTO =
                restUtil.buildRccmRequestParamDTO(serverConfigDTO, RccmRestKey.FORWARD_RCDC_REQUEST_PATH, requestData);
        ForwardRcdcResponse forwardRcdcResponse;
        try {
            RemoteResponse<JSONObject> remoteResponse = restUtil.onceRequest(requestParamDTO);
            RemoteResponseContent<JSONObject> clusterResponse = remoteResponse.getContent();
            forwardRcdcResponse = clusterResponse.getData().toJavaObject(ForwardRcdcResponse.class);
            if (forwardRcdcResponse.getResultCode() == CommonMessageCode.SUCCESS) {
                forwardRcdcResponse.getContent().put(CLUSTER_ID, requestData.getClusterId());
                return forwardRcdcResponse;
            } else {
                LOGGER.warn("通过RCenter转发OPENAPI接口请求返回错误信息，请求信息为:{}，错误码：{}", JSONObject.toJSONString(requestParamDTO),
                        forwardRcdcResponse.getResultCode());
            }
        } catch (BusinessException ex) {
            LOGGER.error("通过RCenter转发OPENAPI接口请求出现业务异常，请求信息为:{}", JSONObject.toJSONString(requestParamDTO), ex);
            throw ex;
        }
        return forwardRcdcResponse;
    }

    @Override
    public void broadcastWebclientNotify(WebclientNotifyRequest webclientNotifyRequest) {
        Assert.notNull(webclientNotifyRequest, "webclientNotifyRequest is null.");
        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();
        RequestParamDTO<WebclientNotifyRequest> requestParamDTO =
                restUtil.buildRccmRequestParamDTO(serverConfigDTO, RccmRestKey.BROADCAST_WEBCLIENT_NOTIFY_PATH, webclientNotifyRequest);
        try {
            restUtil.onceRequest(requestParamDTO);
        } catch (BusinessException e) {
            LOGGER.error("通过RCenter广播网页版客户端通知信息出现业务异常，请求信息为:{}", JSONObject.toJSONString(requestParamDTO), e);
        }
    }

    @Override
    public void pushAdGroupToRccm() {
        THREAD_EXECUTOR.execute(() -> {
            long count = cbbAdGroupAPI.count();
            rccmManageService.pushAdGroupToRccm(count > 0);
        });
    }

    @Override
    public void saveUnifiedManage(UnifiedManageDataRequest request) {
        Assert.notNull(request, "UnifiedManageDataRequest is not null.");
        unifiedManageDataService.saveUnifiedManage(request);
    }

    @Override
    public void deleteUnifiedManage(UnifiedManageDataRequest request) {
        Assert.notNull(request, "deleteUnifiedManage request can not null.");
        unifiedManageDataService.deleteUnifiedManage(request);
    }


    @Override
    public UnifiedManageClusterVersionInfoDTO getVersionInfo() throws BusinessException {
        DtoResponse<String> stringDtoResponse = systemVersionMgmtAPI.obtainSystemReleaseVersion(new DefaultRequest());
        UnifiedManageClusterVersionInfoDTO clusterVersionInfoDTO = new UnifiedManageClusterVersionInfoDTO();
        clusterVersionInfoDTO.setVersion(stringDtoResponse.getDto());
        return clusterVersionInfoDTO;
    }

    @Override
    public RccmManageResponse notifyStrategy(RccmUnifiedManageNotifyConfigRequest request) {
        Assert.notNull(request, "request is null.");
        RccmServerConfigDTO serverConfigDTO = rccmManageService.getRccmServerConfig();
        if (!Objects.equals(request.getClusterId(), serverConfigDTO.getClusterId())) {
            LOGGER.error("统一管理同步模式失败,集群ID[{}]与入参集群ID[{}]不一致", serverConfigDTO.getClusterId(), request.getClusterId());
            return new RccmManageResponse(CommonMessageCode.FAIL_CODE,
                    LocaleI18nResolver.resolve(RccmManageBusinessKey.RCDC_RCCM_UNIFIED_MANAGE_CLUSTER_ID_ERROR));
        }
        // 1.保存同步配置
        List<RccmClusterUnifiedManageStrategyDTO> strategyList = request.getClusterUnifiedManageStrategyDTOList();
        rccmManageService.saveClusterUnifiedManageStrategy(strategyList);

        // 2.删除主从多余数据
        deleteUnwantedData(strategyList);

        // 3.修改同步中镜像状态
        updateSyncingImageState(strategyList);

        // 4.是否需要主集群推送全量数据
        masterClusterRefreshAllData(strategyList);

        return new RccmManageResponse(CommonMessageCode.SUCCESS);
    }

    private void updateSyncingImageState(List<RccmClusterUnifiedManageStrategyDTO> strategyList) {

        // 查询cdc同步中的镜像
        List<CbbImageTemplateDTO> cdcSyncingImageList =
                cbbImageTemplateMgmtAPI.listImageTemplateByTemplateStates(Lists.newArrayList(ImageTemplateState.SYNCING));

        List<UUID> syncingImageUnifiedDataIdList = Lists.newArrayList();
        Set<UUID> rcenterSyncingImageIdSet = Sets.newHashSet();
        for (RccmClusterUnifiedManageStrategyDTO strategyDTO : strategyList) {
            List<UnifiedManageSyncingImageDTO> syncingImageList = strategyDTO.getSyncingImageList();
            if (CollectionUtils.isEmpty(syncingImageList)) {
                continue;
            }
            // 主集群使用imageTemplateId,从集群使用imageUnifiedDataId
            if (RccmUnifiedManageRoleEnum.MASTER == strategyDTO.getRole()) {
                rcenterSyncingImageIdSet.addAll(syncingImageList.stream().map(UnifiedManageSyncingImageDTO::getImageTemplateId)
                        .collect(Collectors.toSet()));
            } else {
                // 任务刚开始时,UnifiedDataId可能为空
                syncingImageUnifiedDataIdList.addAll(strategyDTO.getSyncingImageList().stream()
                        .map(UnifiedManageSyncingImageDTO::getImageUnifiedDataId)
                        .filter(Objects::nonNull).collect(Collectors.toList()));
            }
        }

        // 查找r-center传递的同步中的镜像
        Set<UUID> syncingImageIdSet = unifiedManageDataService.findByUnifiedManageDataIdIn(syncingImageUnifiedDataIdList)
                .stream().filter(entity -> UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE == entity.getRelatedType())
                .map(UnifiedManageDataEntity::getRelatedId).collect(Collectors.toSet());
        rcenterSyncingImageIdSet.addAll(syncingImageIdSet);

        // 查找r-center状态不是同步，并且CDC是同步
        cdcSyncingImageList = cdcSyncingImageList.stream().filter(image -> !rcenterSyncingImageIdSet.contains(image.getId()))
                .collect(Collectors.toList());

        // 判断是否存在同步任务，不存在则修改状态
        cdcSyncingImageList.forEach(image -> {
            UUID imageId = image.getId();
            QueryImageSyncTaskRequest queryImageRequest = new QueryImageSyncTaskRequest();
            queryImageRequest.setPlatformId(image.getPlatformId());
            queryImageRequest.setImageId(imageId);
            try {
                if (ImageTemplateState.SYNCING == image.getImageTemplateState()
                        && BooleanUtils.isFalse(imageMgmtAPI.isExistSyncTask(queryImageRequest))
                        && BooleanUtils.isFalse(isExistStateMachineTask(imageId))) {
                    ImageTemplateState state = obtainImageState(imageId);
                    LOGGER.info("镜像[{}]状态为同步中，并且不存在远程复制关系，修改云桌面状态为[{}]", imageId, state);
                    cbbImageTemplateMgmtAPI.updatePointedState(imageId, state);
                }
            } catch (Exception e) {
                LOGGER.error("修改同步中的镜像[{}]状态失败，失败原因：", imageId, e);
            }
        });
    }

    /**
     * 判断是否存在状态机,主集群使用imageId作为资源锁,从集群使用UnifiedManageDataId作为资源锁
     *
     * @param imageId 镜像ID
     * @return true:存在 false:不存在
     */
    private boolean isExistStateMachineTask(UUID imageId) {
        StateMachineMgmtAgent[] stateMachineArr = stateMachineFactory.findByResourceId(imageId.toString());
        if (ArrayUtils.isNotEmpty(stateMachineArr)) {
            return true;
        }
        UnifiedManageDataEntity unifiedManageDataEntity =
                unifiedManageDataService.findByRelatedIdAndRelatedType(new UnifiedManageDataRequest(imageId,
                        UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE));
        if (Objects.nonNull(unifiedManageDataEntity)) {
            stateMachineArr = stateMachineFactory.findByResourceId(unifiedManageDataEntity.getUnifiedManageDataId().toString());
            return ArrayUtils.isNotEmpty(stateMachineArr);
        }
        return false;
    }

    private ImageTemplateState obtainImageState(UUID imageId) {
        List<CbbImageTemplateDetailDTO> imageVersionList = cbbImageTemplateMgmtAPI.listImageTemplateVersionByRootImageId(imageId);
        // 如果是多版本，并且已经发布过，则修改为已发布；否则修改为错误
        return CollectionUtils.isEmpty(imageVersionList) ? ImageTemplateState.ERROR : ImageTemplateState.AVAILABLE;
    }

    private void masterClusterRefreshAllData(List<RccmClusterUnifiedManageStrategyDTO> strategyList) {
        if (CollectionUtils.isEmpty(strategyList)) {
            return;
        }
        for (RccmClusterUnifiedManageStrategyDTO strategyDTO : strategyList) {
            // 主集群开启云桌面策略同步,并且需要推送全量数据
            if (RccmUnifiedManageRoleEnum.MASTER != strategyDTO.getRole() || !strategyDTO.getNeedMasterClusterRefreshAllData()
                    || !strategyDTO.getEnableSyncDesktopStrategy()) {
                continue;
            }
            // 主集群查询同步标识的云桌面策略,进行逐一推送给RCCM
            List<UnifiedManageDataEntity> unifiedManageDataList =
                    unifiedManageDataService.findByRelatedType(UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
            for (UnifiedManageDataEntity unifiedManageDataEntity : unifiedManageDataList) {
                UNIFIED_MANAGE_THREAD_EXECUTOR.execute(() -> {
                    UUID deskStrategyId = unifiedManageDataEntity.getRelatedId();
                    try {
                        unifiedManageDataService.pushSyncUnifiedManage(deskStrategyId);
                    } catch (Exception e) {
                        LOGGER.error("主集群[{}]推送全量云桌面策略[{}]异常:", strategyDTO.getMasterClusterId(), deskStrategyId, e);
                    }
                });
            }
        }
    }

    @Override
    public boolean isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum functionKey) {
        List<RccmClusterUnifiedManageStrategyDTO> strategyList = strategyList();
        // 查询配置是主集群并且启用同步
        return strategyList.stream().anyMatch(strateg -> strateg.getEnableSyncDesktopStrategy()
                && RccmUnifiedManageRoleEnum.MASTER == strateg.getRole());
    }

    @Override
    public List<RccmClusterUnifiedManageStrategyDTO> strategyList() {
        return rccmManageService.getClusterUnifiedManageStrategy();
    }

    @Override
    public boolean needUnifiedManageLock(UnifiedManageFunctionKeyEnum functionKey) {
        Assert.notNull(functionKey, "functionKey not be null");
        // 集群是否被纳管
        RccmServerConfigDTO rccmServerConfig = rccmManageService.getRccmServerConfig();
        boolean enableNanotube = rccmServerConfig != null && rccmServerConfig.hasHealth();
        // 判断是否是从集群并开启同步
        boolean enableLock = strategyList().stream()
                .anyMatch(strategy -> RccmUnifiedManageRoleEnum.SLAVE == strategy.getRole() && strategy.getEnableSyncDesktopStrategy());
        return enableNanotube && enableLock;
    }

    @Override
    public List<UnifiedManageForMasterClusterAllDataDTO> collectAllDataByType(@Nullable UnifiedManageFunctionKeyEnum... relateTypeArr) {

        // 如果传空，则查询所有类型
        UnifiedManageFunctionKeyEnum[] searchTypeArr = Optional.ofNullable(relateTypeArr).orElse(UnifiedManageFunctionKeyEnum.values());

        List<UnifiedManageForMasterClusterAllDataDTO> resultList = Lists.newArrayList();
        for (UnifiedManageFunctionKeyEnum type : searchTypeArr) {
            resultList.add(getUnifiedManageForMasterClusterAllDataDTO(type));
        }

        return resultList;
    }

    private UnifiedManageForMasterClusterAllDataDTO getUnifiedManageForMasterClusterAllDataDTO(UnifiedManageFunctionKeyEnum type) {
        List<UUID> unifiedDataIdList = unifiedManageDataService.findByRelatedType(type).stream()
                .map(UnifiedManageDataEntity::getUnifiedManageDataId).collect(Collectors.toList());
        UnifiedManageForMasterClusterAllDataDTO dto = new UnifiedManageForMasterClusterAllDataDTO();
        dto.setFunctionKey(type);
        dto.setUnifiedManageIdList(unifiedDataIdList);

        return dto;
    }

    @Override
    public UnifiedManageDataDTO findByUnifiedManageDataId(UUID unifiedManageDataId) {
        Assert.notNull(unifiedManageDataId, "unifiedManageDataId not be null");
        UnifiedManageDataEntity manageDataEntity = unifiedManageDataService.findByUnifiedManageDataId(unifiedManageDataId);
        if (Objects.nonNull(manageDataEntity)) {
            UnifiedManageDataDTO unifiedManageDataDTO = new UnifiedManageDataDTO();
            BeanUtils.copyProperties(manageDataEntity, unifiedManageDataDTO);
            return unifiedManageDataDTO;
        }
        // 统一管理的数据为空，返回null
        return null;
    }

    @Override
    public void createNotify(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId not be null");
        unifiedManageDataService.createNotify(deskStrategyId);
    }

    @Override
    public void updateNotify(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId not be null");
        unifiedManageDataService.updateNotify(deskStrategyId);
    }

    private void deleteUnwantedData(List<RccmClusterUnifiedManageStrategyDTO> strategyList) {

        // rcenter没有同步策略,删除所有数据
        if (CollectionUtils.isEmpty(strategyList)) {
            for (UnifiedManageFunctionKeyEnum functionKeyEnum : UnifiedManageFunctionKeyEnum.getNeedDeleteUnwantedDataKey()) {
                deleteUnifiedManageData(unifiedManageDataService.findByRelatedType(functionKeyEnum));
            }
            return;
        }

        // 处理每个策略的多余数据
        for (RccmClusterUnifiedManageStrategyDTO manageStrategyDTO : strategyList) {
            // 处理标记为false,主从都无需处理多余数据
            if (BooleanUtils.isFalse(manageStrategyDTO.getSyncMasterClusterAllData())) {
                continue;
            }

            List<UnifiedManageForMasterClusterAllDataDTO> masterClusterAllDataList = manageStrategyDTO.getMasterClusterAllDataList();
            // 归集统一管理数据ID
            Set<UUID> deskStrategyIdList = Sets.newHashSet();
            Set<UUID> imageIdList = Sets.newHashSet();
            if (CollectionUtils.isNotEmpty(masterClusterAllDataList)) {
                masterClusterAllDataList.stream().forEach(unifiedManageData -> {
                    // 云桌面策略需要判断是否启用
                    if (UnifiedManageFunctionKeyEnum.DESK_STRATEGY == unifiedManageData.getFunctionKey()
                            && BooleanUtils.isTrue(manageStrategyDTO.getEnableSyncDesktopStrategy())) {
                        deskStrategyIdList.addAll(unifiedManageData.getUnifiedManageIdList());
                    } else if (UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE == unifiedManageData.getFunctionKey()) {
                        imageIdList.addAll(unifiedManageData.getUnifiedManageIdList());
                    }
                });
            }

            // 找出多余的云桌面策略进行删除 1.主集群并且未开启云桌面策略同步时也要清理数据(此时同步功能已关闭,不存在并发误删) 2.从集群都会进行清理
            List<UnifiedManageDataEntity> deskStrategyList = unifiedManageDataService.findByRelatedType(UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
            deskStrategyList = deskStrategyList.stream().filter(entity -> (RccmUnifiedManageRoleEnum.SLAVE == manageStrategyDTO.getRole()
                    || BooleanUtils.isFalse(manageStrategyDTO.getEnableSyncDesktopStrategy()))
                    && !deskStrategyIdList.contains(entity.getUnifiedManageDataId())).collect(Collectors.toList());
            deleteUnifiedManageData(deskStrategyList);

            // 找出多余的镜像进行删除 1.主集群不处理多余的镜像,避免并发时误删 2.从集群都会进行清理
            List<UnifiedManageDataEntity> imageList = unifiedManageDataService.findByRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_TEMPLATE);
            imageList = imageList.stream().filter(entity -> RccmUnifiedManageRoleEnum.MASTER != manageStrategyDTO.getRole()
                    && !imageIdList.contains(entity.getUnifiedManageDataId())).collect(Collectors.toList());
            deleteUnifiedManageData(imageList);
        }
    }


    private void deleteUnifiedManageData(List<UnifiedManageDataEntity> dataList) {
        for (UnifiedManageDataEntity unifiedManageEntity : dataList) {
            UUID relatedId = unifiedManageEntity.getRelatedId();
            UnifiedManageFunctionKeyEnum functionKey = unifiedManageEntity.getRelatedType();
            LOGGER.info("统一管理-开始删除多余的功能[{}] 数据ID[{}][{}] ", functionKey, relatedId, unifiedManageEntity.getUnifiedManageDataId());
            switch (functionKey) {
                case DESK_STRATEGY: {
                    try {
                        // 删除策略成功,正常也会删除unifiedManageData
                        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(relatedId);
                        deskStrategyAPI.deleteDeskStrategy(relatedId);
                        auditLogAPI.recordLog(RccmManageBusinessKey.RCDC_RCO_CLOUDDESKTOP_STRATEGY_DELETE_SUCCESS_LOG, deskStrategy.getName());
                    } catch (Exception ex) {
                        LOGGER.error("统一管理-删除多余的云桌面策略ID[{}]失败,直接删除统一管理同步标识 异常:", relatedId, ex);
                        UnifiedManageDataRequest unifiedManageDataRequest = new UnifiedManageDataRequest(relatedId, functionKey);
                        cbbDeskStrategyCommonAPI.updateDeskStrategyState(relatedId, CbbDeskStrategyState.AVAILABLE);
                        unifiedManageDataService.deleteUnifiedManage(unifiedManageDataRequest);
                    }
                    break;
                }
                case IMAGE_TEMPLATE: {
                    // 删除远程复制关系成功后,再删除unifiedManageData
                    // 考虑到删除远程复制可能是耗时操作，所以使用线程池处理
                    DELETE_REPLICATION_POOL.execute(() -> deleteImageRelateData(unifiedManageEntity));
                    break;
                }
            }
        }
    }

    private void deleteImageRelateData(UnifiedManageDataEntity unifiedManageEntity) {
        UUID imageId = unifiedManageEntity.getRelatedId();

        try {
            // 删除远程复制关系，镜像存在的情况下才有必要删除
            if (cbbImageTemplateMgmtAPI.existsImageTemplate(imageId)) {
                DeleteReplicationRequest deleteReplicationRequest = new DeleteReplicationRequest();
                deleteReplicationRequest.setImageId(imageId);
                CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(imageId);
                deleteReplicationRequest.setPlatformId(cbbImageTemplateDTO.getPlatformId());
                imageMgmtAPI.deleteReplication(deleteReplicationRequest);
            }
        } catch (Exception e) {
            // 异常情况下无需特殊处理，RCenter每分钟会请求一次次接口，会再次尝试删除
            LOGGER.error("删除镜像[{}]-[{}]远程复制关系失败，失败原因", unifiedManageEntity.getUnifiedManageDataId(), imageId, e);
        }

        try {
            // 删除统一管理数据
            LOGGER.info("镜像[{}]，删除统一管理表中的镜像和快照信息", imageId);
            unifiedManageDataService.deleteImageAndRelateData(imageId);
        } catch (Exception e) {
            LOGGER.error("删除镜像[{}]-[{}]统一管理数据失败，失败原因", unifiedManageEntity.getUnifiedManageDataId(), imageId, e);
        }
    }

    /**
     * 处理密码错误异常
     *
     * @param ex 异常信息
     */
    private void handleLoginFailException(LoginFailException ex, String userName) throws BusinessException {
        LOGGER.error("管理员登录密码错误，message={}", ex.getMessage());
        int remainLoginCount = Objects.isNull(ex.getRemainLoginCount()) ? 0 : ex.getRemainLoginCount();
        // 剩余次数为0，则被锁定
        if (remainLoginCount == 0) {
            try {
                adminMgmtAPI.getAdminByUserName(userName);
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_VALIDATE_PASSWORD_FAIL_READY_LOCK_WITH_NAME, userName);
            } catch (BusinessException e) {
                LOGGER.error("管理员[{}]不存在，无需写锁定审核日志", userName);
            }
            throw new BusinessException(BusinessKey.RCDC_RCO_ADMIN_LOGIN_FAIL_READY_LOCK, ex, userName, Integer.toString(remainLoginCount));
        }

        try {
            adminMgmtAPI.getAdminByUserName(userName);
            if ("rcdc_aaa_login_admin_not_exist_or_pwd_error".equals(ex.getMessage())) {
                auditLogAPI.recordLog(BusinessKey.RCDC_AAA_ADMIN_LOGIN_FAIL_WITH_USER_EXISTS, ex, userName);
            } else {
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_LOGIN_FAIL_BY_ADMIN_NAME, ex, userName, ex.getI18nMessage());
            }
        } catch (BusinessException e) {
            LOGGER.error("管理员[{}]不存在，无需写登录审核日志", userName);
        }

        throw new BusinessException(BusinessKey.RCDC_RCO_ADMIN_LOGIN_FAIL_REMAIN_TIME, ex, userName, Integer.toString(remainLoginCount));
    }

    /**
     * 处理管理员被锁定异常
     *
     * @param ex 异常信息
     */
    private void handleLoginFailLockException(LoginFailLockException ex, String userName) throws BusinessException {
        LOGGER.error("管理员被锁定，message={}", ex.getMessage());

        long remainLockTime = ex.getRemainLockTime() == null ? 0L : ex.getRemainLockTime();
        long remainMinutes = remainLockTime == 0L ? 0L : remainLockTime / 60;
        long remainSeconds = remainLockTime == 0L ? 0L : remainLockTime % 60;
        try {
            adminMgmtAPI.getAdminByUserName(userName);
        } catch (BusinessException e) {
            LOGGER.error("管理员[{}]不存在，无需写登录审核日志", userName);
        }
        throw new BusinessException(BusinessKey.RCDC_RCO_ADMIN_LOCKED, ex, userName, Long.toString(remainMinutes), Long.toString(remainSeconds));
    }

    private boolean checkSameServer(RccmServerConfigDTO rccmServerConfigDTO, RccmManageRequest rccmManageRequest) {
        if (rccmServerConfigDTO == null) {
            return false;
        }
        return rccmServerConfigDTO.getServerIp().equals(rccmManageRequest.getServerIp())
                && rccmServerConfigDTO.getClusterId().equals(rccmManageRequest.getClusterId());
    }

    @Override
    public UUID getUnifiedManageDataId(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType) {
        Assert.notNull(relatedId, "relatedId not be null");
        Assert.notNull(relatedType, "relatedType not be null");

        return unifiedManageDataService.getUnifiedManageDataId(relatedId, relatedType);
    }

    @Override
    public boolean existsUnifiedData(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType) {
        Assert.notNull(relatedId, "relatedId not be null");
        Assert.notNull(relatedType, "relatedType not be null");

        return unifiedManageDataService.existsUnifiedData(relatedId, relatedType);
    }

    @Override
    public boolean isMaster() {
        return rccmManageService.isMaster();
    }

    @Override
    public boolean isSlave() {
        return rccmManageService.isSlave();
    }

    @Override
    public List<UnifiedManageDataDTO> findByRelatedTypeAndRelatedIdIn(UnifiedManageFunctionKeyEnum relatedType, List<UUID> relatedIdList) {
        Assert.notNull(relatedType, "relatedType not be null");
        Assert.notNull(relatedIdList, "relatedIdList not be null");

        return unifiedManageDataService.findByRelatedTypeAndRelatedIdIn(relatedType, relatedIdList).stream().map(entity -> {
            UnifiedManageDataDTO unifiedManageDataDTO = new UnifiedManageDataDTO();
            BeanUtils.copyProperties(entity, unifiedManageDataDTO);
            return unifiedManageDataDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void validateRcdcVip(String clusterVip) throws BusinessException {
        Assert.notNull(clusterVip, "clusterVip is not null.");
        String clusterVirtualIp = globalParameterAPI.findParameter(Constants.VIP_PARAM_KEY);
        if (!Objects.equals(clusterVip, clusterVirtualIp)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_CLUSTER_VIP_ERROR, clusterVirtualIp);
        }
    }

    @Override
    public void addSyncUserPasswordCache(String userName) {
        Assert.hasText(userName, "userName not be null");
        rccmManageService.addSyncUserPasswordCache(userName);
    }

    @Override
    public void delSyncUserPasswordCache(String userName) {
        Assert.hasText(userName, "userName not be null");
        rccmManageService.addSyncUserPasswordCache(userName);
    }

    @Override
    public void pushUserByUserIdList(List<UUID> userIdList) {
        Assert.notEmpty(userIdList, "userIdList is not empty.");
        rccmManageService.pushUserByUserIdList(userIdList);
    }
}
