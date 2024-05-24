package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOneAgentTcpSendAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CdcNotifyOaDisJoinHostRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OneAgentConnectTypeEnums;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaTrusteeshipHostAPI;
import com.ruijie.rcos.rcdc.rco.module.common.utils.BroadcastWakeUpUtil;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ExportComputerFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportCloudDesktopDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetAssistantAppPathResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetExportCloudDesktopFileResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.TerminalWakeUpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ExportComputerDataCacheMgt;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.dto.ExportComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.dto.NotifyComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.enums.RcoComputerActionNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.computer.service.NotifyComputerChangeService;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api.ComputerTcpAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.ComputerListServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ExportUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbWakeUpTerminalInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalStatisticsItem;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.HibernateUtil;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.Response;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/4 18:54
 *
 * @author ketb
 */
public class ComputerBusinessAPIImpl implements ComputerBusinessAPI, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerBusinessAPIImpl.class);

    private static final int MAX_COMPUTER_QUERY_NUM = 500;

    @Autowired
    private ComputerListServiceImpl computerListService;

    @Autowired
    private ComputerBusinessDAO computerBusinessDAO;

    @Autowired
    private ComputerBusinessService computerBusinessService;

    @Autowired
    private CbbTerminalGroupMgmtAPI terminalGroupMgmtAPI;


    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private NotifyComputerChangeService notifyComputerChangeService;


    @Autowired
    private ExportComputerDataCacheMgt exportComputerDataCacheMgt;

    @Autowired
    private ComputerTcpAPI computerTcpAPI;


    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private RcaTrusteeshipHostAPI rcaTrusteeshipHostAPI;

    @Autowired
    private CbbOneAgentTcpSendAPI cbbOneAgentTcpSendAPI;

    @Autowired
    private CbbGuestToolLogAPI cbbGuestToolLogAPI;

    private static final String MATCH_EQUAL_FIELD = "faultState";

    private static final String SORT_FIELD = "faultTime";


    private static final String FILE_PREFIX = "export_computer";

    private static final String FILE_POSTFIX = ".xlsx";

    /**
     * 移除PC终端线程池
     */
    private static final ExecutorService REMOVE_PC_THREAD_POOL =
            ThreadExecutors.newBuilder("remove_pc_thread_pool").maxThreadNum(5).queueSize(1000).build();


    /**
     * 发送唤醒报文、检查终端上线时间间隔
     */
    private Integer wakeupCheckInterval;

    /**
     * 唤醒重试时间
     */
    private Integer wakeupTimeoutMillisecond;

    /**
     * 唤醒等待时间
     */
    private Integer wakeupCheckTimeoutMillisecond;

    /**
     * 正在唤醒的终端缓存
     */
    private Cache<UUID, UUID> wakingCache;

    private static final int DEFAULT_EXPORT_PAGE_SIZE = 1000;

    private static final String THREAD_POOL_NAME = "desk-wake-notify-agent-oa-op";

    private static final int MAX_THREAD_NUM = 30;

    private static final int QUEUE_SIZE = 1000;


    private static final ThreadExecutor THREAD_EXECUTOR =
            ThreadExecutors.newBuilder(THREAD_POOL_NAME).maxThreadNum(MAX_THREAD_NUM).queueSize(QUEUE_SIZE).build();

    @Override
    public void afterPropertiesSet() {
        String paramValue = globalParameterService.findParameter(Constants.TERMINAL_WAKE_UP_CONFIG_KEY);
        TerminalWakeUpConfigDTO wakeUpConfigDTO;
        if (StringUtils.isEmpty(paramValue)) {
            wakeUpConfigDTO = new TerminalWakeUpConfigDTO();
        } else {
            wakeUpConfigDTO = JSON.parseObject(paramValue, TerminalWakeUpConfigDTO.class);
        }
        wakeupCheckInterval = Optional.ofNullable(wakeUpConfigDTO.getWakeCheckInterval()).orElse(5000);
        wakeupTimeoutMillisecond = Optional.ofNullable(wakeUpConfigDTO.getWakeupTimeout()).orElse(60000);
        wakeupCheckTimeoutMillisecond = Optional.ofNullable(wakeUpConfigDTO.getWakeupCheckTimeout()).orElse(240000);
        wakingCache = CacheBuilder.newBuilder().expireAfterWrite(wakeupCheckTimeoutMillisecond, TimeUnit.MILLISECONDS).maximumSize(5000).build();
    }

    @Override
    public DefaultPageResponse<ComputerDTO> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "request can not be null");
        Page<ComputerEntity> terminalPage = computerListService.pageQuery(request, ComputerEntity.class);
        List<ComputerEntity> entityList = terminalPage.getContent();
        // 应该要创建一个视图
        ComputerDTO[] dataArr = new ComputerDTO[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            ComputerDTO dto = new ComputerDTO();
            BeanUtils.copyProperties(entityList.get(i), dto);
            try {
                CbbTerminalGroupDetailDTO terminalGroupDTO = terminalGroupMgmtAPI.loadById(dto.getTerminalGroupId());
                if (terminalGroupDTO.getGroupName() == null) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_GROUP_NOT_EXIST);
                }
                dto.setTerminalGroupName(terminalGroupDTO.getGroupName());
            } catch (Exception e) {
                LOGGER.error("get terminal group error", e);
            }
            dataArr[i] = dto;
        }

        DefaultPageResponse<ComputerDTO> response = new DefaultPageResponse<>();
        response.setTotal(terminalPage.getTotalElements());
        response.setItemArr(dataArr);
        response.setStatus(Response.Status.SUCCESS);
        return response;
    }

    @Override
    public ComputerInfoResponse getComputerInfoByComputerId(ComputerIdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        ComputerEntity baseInfoEntity = computerBusinessDAO.findComputerEntityById(request.getComputerId());
        if (baseInfoEntity == null) {
            LOGGER.error("未找到对应PC记录: ComputerId = {}", request.getComputerId());
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER_NAME);
        }
        ComputerInfoResponse baseInfoResponse = new ComputerInfoResponse();
        BeanUtils.copyProperties(baseInfoEntity, baseInfoResponse);
        baseInfoResponse.setGroupId(baseInfoEntity.getTerminalGroupId());
        CbbTerminalGroupDetailDTO cbbTerminalGroupDetailDTO = terminalGroupMgmtAPI.loadById(baseInfoEntity.getTerminalGroupId());
        baseInfoResponse.setGroupName(cbbTerminalGroupDetailDTO.getGroupName());
        return baseInfoResponse;
    }

    @Override
    public void relieveFault(ComputerIdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        computerBusinessService.relieveFault(request.getComputerId());
    }

    @Override
    public void editComputer(EditComputerRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ComputerEntity computerEntity = computerBusinessDAO.findComputerEntityById(request.getId());
        if (computerEntity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER, new String[]{request.getId().toString()});
        }
        // 判读终端分组是否存在 如果不存在 CBB会抛出异常
        terminalGroupMgmtAPI.loadById(request.getTerminalGroupName().getId());
        computerEntity.setAlias(request.getAlias());
        if (!computerEntity.getTerminalGroupId().equals(request.getTerminalGroupName().getId())) {
            //移动分组通知
            computerEntity.setTerminalGroupId(request.getTerminalGroupName().getId());
            computerBusinessService.moveGroupNotify(computerEntity);
        }
        computerBusinessService.update(computerEntity, 0);
    }

    @Override
    public void deleteComputer(ComputerIdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ComputerEntity computerEntity = computerBusinessDAO.findComputerEntityById(request.getComputerId());
        if (ComputerStateEnum.ONLINE == computerEntity.getState()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_ONLINE_NOT_ALLOW_DELETE, new String[]{computerEntity.getName()});
        } else {
            LOGGER.warn("delete computer info, computerId[{}]", request.getComputerId());
            try {
                computerBusinessDAO.deleteById(request.getComputerId());
            } catch (Exception e) {
                LOGGER.error("delete computer info fail, computerId [{}]", request.getComputerId());
                throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_DELETE_COMPUTER_FAIL, e);
            }
        }
    }

    @Override
    public GetAssistantAppPathResponse getAssistantAppDownloadUrl(DefaultRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        File fileDir = new File(Constants.ASSISTANT_APP_COMPONENT_PATH);
        if (!fileDir.exists()) {
            LOGGER.error("升级包路径不存在，path[{}]", Constants.ASSISTANT_APP_COMPONENT_PATH);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_ASSISTANTAPP_UPGRADE_PACKAGE_NOT_EXIST);
        }

        File assistantFile = new File(Constants.ASSISTANT_APP_COMPONENT_PATH + Constants.ASSISTANT_APP_PACKAGE_NAME);
        if (!assistantFile.exists()) {
            LOGGER.error("升级包文件不存在");
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_ASSISTANTAPP_UPGRADE_PACKAGE_NOT_EXIST);
        }

        return new GetAssistantAppPathResponse(Constants.ASSISTANT_APP_COMPONENT_PATH + Constants.ASSISTANT_APP_PACKAGE_NAME);
    }

    @Override
    public GetAssistantAppPathResponse getHaloAppDownloadUrl(DefaultRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        File fileDir = new File(Constants.HALO_APP_COMPONENT_PATH);
        if (!fileDir.exists()) {
            LOGGER.error("升级包路径不存在，path[{}]", Constants.HALO_APP_COMPONENT_PATH);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_HALO_UPGRADE_PACKAGE_NOT_EXIST);
        }

        File assistantFile = new File(Constants.HALO_APP_COMPONENT_PATH + Constants.HALO_APP_PACKAGE_NAME);
        if (!assistantFile.exists()) {
            LOGGER.error("升级包文件不存在，file[{}]", Constants.HALO_APP_PACKAGE_NAME);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_HALO_UPGRADE_PACKAGE_NOT_EXIST);
        }

        return new GetAssistantAppPathResponse(Constants.HALO_APP_COMPONENT_PATH + Constants.HALO_APP_PACKAGE_NAME);
    }

    @Override
    public void moveComputerGroup(MoveComputerGroupRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        ComputerEntity entity = computerBusinessDAO.findComputerEntityById(request.getComputerId());
        if (entity == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER, new String[]{request.getComputerName()});
        }
        CbbTerminalGroupDetailDTO terminalGroupDTO = terminalGroupMgmtAPI.loadById(request.getGroupId());
        if (terminalGroupDTO.getId() == null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_GROUP_NOT_EXIST);
        }

        entity.setTerminalGroupId(request.getGroupId());
        computerBusinessService.update(entity, 0);
        computerBusinessService.moveGroupNotify(entity);
    }


    /**
     * 编辑终端信息
     *
     * @param groupIdArr 请求消息体
     * @return 请求结果
     * @throws BusinessException 业务异常
     */
    @Override
    public TerminalStatisticsItem statisticsComputer(UUID[] groupIdArr) throws BusinessException {
        Assert.notNull(groupIdArr, "groupIdArr can not be null");

        List<ComputerEntity> resultList;
        if (ArrayUtils.isEmpty(groupIdArr)) {
            resultList = computerBusinessDAO.findAll();
        } else {
            List<UUID> terminalGroupIdList = HibernateUtil.handleQueryIncludeList(Arrays.asList(groupIdArr));
            resultList = computerBusinessDAO.findByTerminalGroupIdList(terminalGroupIdList);
        }

        if (CollectionUtils.isEmpty(resultList)) {
            LOGGER.info("没有终端类型为PC的数据");
            return new TerminalStatisticsItem();
        }

        return buildTerminalStatisticsItem(resultList);
    }

    @Override
    public DefaultPageResponse<ComputerDTO> computerFaultPageQuery(PageSearchRequest pageRequest) {
        Assert.notNull(pageRequest, "pageRequest can not be null");
        buildSearchRequest(pageRequest);
        return pageQuery(pageRequest);
    }

    @Override
    public ComputerInfoResponse getComputerInfoByIp(String ip) {
        Assert.hasText(ip, "ip can not be empty");
        ComputerEntity computerEntity = computerBusinessDAO.findByIp(ip);
        ComputerInfoResponse computerInfoResponse = new ComputerInfoResponse();
        if (computerEntity != null) {
            BeanUtils.copyProperties(computerEntity, computerInfoResponse);
        }
        return computerInfoResponse;
    }

    @Override
    public void saveComputer(CreateComputerRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be empty");
        ComputerEntity computerEntity = new ComputerEntity();
        BeanUtils.copyProperties(request, computerEntity);

        computerBusinessService.create(computerEntity);
        NotifyComputerDTO notifyComputerDTO = new NotifyComputerDTO();
        BeanUtils.copyProperties(computerEntity, notifyComputerDTO);
        notifyComputerDTO.setActionNotifyEnum(RcoComputerActionNotifyEnum.ADD_COMPUTER);
        notifyComputerChangeService.notifyComputerChange(notifyComputerDTO);
    }

    @Override
    public void deleteThirdPartyComputer(ComputerIdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        CbbDeskDTO cbbDeskDTO = cbbDeskMgmtAPI.findById(request.getComputerId());
        if (cbbDeskDTO != null) {
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_ALLOW_DELETE);
        }
        ComputerEntity computerEntity = computerBusinessService.getComputerById(request.getComputerId());
        computerBusinessDAO.deleteById(request.getComputerId());
        notifyOneAgentRemoveComputer(request, computerEntity);
    }

    private void notifyOneAgentRemoveComputer(ComputerIdRequest request, ComputerEntity computerEntity) {
        REMOVE_PC_THREAD_POOL.execute(() -> {
            try {
                if (computerEntity != null && computerEntity.getState() == ComputerStateEnum.ONLINE) {
                    CdcNotifyOaDisJoinHostRequest removeComputerDTO = new CdcNotifyOaDisJoinHostRequest();
                    removeComputerDTO.setType(OneAgentConnectTypeEnums.THIRD_HOST);
                    removeComputerDTO.setPlatformId(rcaTrusteeshipHostAPI.getPlatformId());
                    LOGGER.info("通知OA移除PC终端[{}]信息", request.getComputerId());
                    cbbOneAgentTcpSendAPI.notifyOaDisJoinHost(request.getComputerId().toString(), removeComputerDTO);
                }
            } catch (BusinessException e) {
                LOGGER.error("通知移除PC[{}]信息失败", request.getComputerId(), e);
            }
        });
    }

    @Override
    public void exportDataAsync(PageSearchRequest pageReq, String userId) throws BusinessException {
        Assert.notNull(pageReq, "pageReq can not be null");
        Assert.notNull(userId, "userId can not be null");
        ExportComputerFileInfoDTO cache = exportComputerDataCacheMgt.getCache(userId);
        if (Objects.nonNull(cache) && ExportCloudDesktopDataStateEnums.DOING.equals(cache.getState())) {
            LOGGER.info("导出任务正在运行，不要重复进行导出操作");
        } else {
            String tmpFileName = getTmpFileName(userId);
            // 清空旧缓存
            exportComputerDataCacheMgt.deleteCache(userId);
            // 在删除一次，此处是为了防止缓存不存在而目录文件中已有文件，确保只有一个文件
            try {
                deleteOldFile(tmpFileName);
            } catch (IOException e) {
                LOGGER.error("删除文件异常", e);
                throw new BusinessException(BusinessKey.RCDC_RCO_DELETE_FILE_ERROR, e);
            }
            ExportComputerFileInfoDTO newCache = new ExportComputerFileInfoDTO();
            exportComputerDataCacheMgt.save(userId, newCache);
            // 开启线程导出excel
            ThreadExecutors.submit("exportComputer", () -> {
                try {
                    pageReq.setPage(0);
                    pageReq.setLimit(DEFAULT_EXPORT_PAGE_SIZE);
                    Page<ComputerEntity> terminalPage = computerListService.pageQuery(pageReq, ComputerEntity.class);
                    List<ExportComputerDTO> computerList = convertComputerDTO(terminalPage.getContent());
                    int totalPages = terminalPage.getTotalPages();
                    if (totalPages > 1) {
                        for (int i = 1; i < totalPages; i++) {
                            pageReq.setPage(i);
                            terminalPage = computerListService.pageQuery(pageReq, ComputerEntity.class);
                            computerList.addAll(convertComputerDTO(terminalPage.getContent()));
                        }
                    }

                    LOGGER.info("准备导出PC终端数据，导出条目为{}", computerList.size());
                    String exportFilePath = exportAndGetResultPath(computerList, tmpFileName);
                    newCache.setState(ExportCloudDesktopDataStateEnums.DONE);
                    newCache.setCreateTimestamp(System.currentTimeMillis());
                    newCache.setExportFilePath(exportFilePath);
                    newCache.setFileName(tmpFileName);
                    LOGGER.info("导出PC终端成功，导出路径是{}", exportFilePath);
                    exportComputerDataCacheMgt.save(userId, newCache);
                } catch (Exception e) {
                    LOGGER.error("导出文件出错，错误原因是{}", e);
                    exportComputerDataCacheMgt.updateState(userId, ExportCloudDesktopDataStateEnums.FAULT);
                }
            });
        }
    }

    @Override
    public ExportComputerFileInfoDTO getExportDataCache(String userId) {
        Assert.notNull(userId, "userId is null");
        ExportComputerFileInfoDTO cache = exportComputerDataCacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            cache = new ExportComputerFileInfoDTO();
            cache.setState(ExportCloudDesktopDataStateEnums.DOING);
        }
        return cache;
    }

    @Override
    public GetExportCloudDesktopFileResponse getExportFile(String userId) throws BusinessException {
        Assert.notNull(userId, "userId is null");
        ExportComputerFileInfoDTO cache = exportComputerDataCacheMgt.getCache(userId);
        if (Objects.isNull(cache)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_DATA_NOT_EXIST);
        }
        String exportFilePath = cache.getExportFilePath();
        File file = new File(exportFilePath);
        if (!file.exists()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_EXPORT_DATA_NOT_EXIST);
        }

        return new GetExportCloudDesktopFileResponse(file);
    }

    @Override
    public void wakeUpComputer(UUID computerId, Boolean enableParallelWake) throws BusinessException {
        Assert.notNull(computerId, "computerId must not be null");
        Assert.notNull(enableParallelWake, "enableParallelWake must not be null");

        if (Boolean.TRUE.equals(enableParallelWake)) {
            // 如果允许同时唤醒，则不用加入缓存，直接调用唤醒逻辑即可
            this.wakeUpComputer(computerId);
        }

        if (wakingCache.getIfPresent(computerId) != null) {
            LOGGER.debug("终端[{}]缓存中数据不为空，不允许重复唤醒", computerId);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_EXISTS_WAKE_UP_TASK);
        }

        wakingCache.put(computerId, computerId);
        try {
            this.wakeUpComputer(computerId);
        } finally {
            // 无论是否唤醒成功，都从缓存中移除终端信息
            wakingCache.invalidate(computerId);
        }

    }

    @Override
    public List<ComputerDTO> getComputerInfoByGroupIdList(List<UUID> groupIdList) {
        Assert.notEmpty(groupIdList, "groupIdList must not be empty");
        List<ComputerEntity> computerEntityList = computerBusinessService.findComputerInfoByGroupIdList(groupIdList);
        return buildComputerDTOS(computerEntityList);
    }

    @Override
    public List<ComputerDTO> getComputerInfoByIdList(List<UUID> computerIdList) {
        Assert.notEmpty(computerIdList, "computerIdList must not be empty");
        List<ComputerEntity> computerEntityList = computerBusinessService.findComputerInfoComputerInfoByIdList(computerIdList);
        return buildComputerDTOS(computerEntityList);
    }

    @Override
    public boolean checkAllComputerExistByIds(List<UUID> idList) {
        Assert.notEmpty(idList, "idList must not be empty");
        int num = computerBusinessService.countComputerByIdList(idList);
        return idList.size() == num;
    }

    @Override
    public List<UUID> listGroupIdByComputerIdList(List<UUID> computerIdList) {
        Assert.notEmpty(computerIdList, "computerIdList must not be null");
        if (CollectionUtils.isEmpty(computerIdList)) {
            return Lists.newArrayList();
        }

        List<List<UUID>> tempUserIdList = Lists.partition(computerIdList, MAX_COMPUTER_QUERY_NUM);
        Set<UUID> groupIdSet = new HashSet<>();
        for (List<UUID> idList : tempUserIdList) {
            List<ComputerEntity> computerEntityList = computerBusinessService.findComputerInfoComputerInfoByIdList(idList);
            if (CollectionUtils.isEmpty(computerEntityList)) {
                continue;
            }
            groupIdSet.addAll(computerEntityList.stream().map(ComputerEntity::getTerminalGroupId).collect(Collectors.toList()));
        }
        return Lists.newArrayList(groupIdSet);
    }

    @Override
    public void wakeUpDesk(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");
        ComputerInfoResponse computerInfoResponse = getComputerInfoResponse(deskId);
        // 唤醒超时时间内，继续发送唤醒报文
        // WOL唤醒
        wakeUpByWol(computerInfoResponse);

        // 定向广播唤醒
        BroadcastWakeUpUtil.wakeUp(computerInfoResponse.getMac(), computerInfoResponse.getIp());
    }

    @Override
    public void collectLog(UUID id) throws BusinessException {
        Assert.notNull(id, "id must not be null");
        checkComputerRunning(id);
        cbbGuestToolLogAPI.collectComputerLog(id);
    }

    private void checkComputerRunning(UUID id) throws BusinessException {
        ComputerEntity computerEntity = computerBusinessService.getComputerById(id);
        if (Objects.isNull(computerEntity)) {
            LOGGER.error("PC不存在，ID=[{}]", id);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_NOT_FOUND_COMPUTER_NAME, id.toString());
        }
        if (computerEntity.getState() != ComputerStateEnum.ONLINE) {
            LOGGER.error("PC未运行，ID=[{}]", id);
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_GT_LOG_COMPUTER_NOT_RUNNING, computerEntity.getName());
        }
    }

    private static List<ComputerDTO> buildComputerDTOS(List<ComputerEntity> computerEntityList) {
        List<ComputerDTO> computerDTOList = computerEntityList.stream().map(s -> {
            ComputerDTO dto = new ComputerDTO();
            BeanUtils.copyProperties(s, dto);
            return dto;
        }).collect(Collectors.toList());
        return computerDTOList;
    }

    private void wakeUpComputer(UUID computerId) throws BusinessException {
        Assert.notNull(computerId, "computerId must not be null");

        long startTime = System.currentTimeMillis();
        try {
            ComputerInfoResponse computerInfoResponse = getComputerInfoResponse(computerId);
            boolean isOnline = computerInfoResponse.getState() == ComputerStateEnum.ONLINE;
            while (!isOnline) {
                long currentTime = System.currentTimeMillis();
                // 超过90S终端还没上线，则认为唤醒失败
                if (currentTime - startTime > wakeupCheckTimeoutMillisecond) {
                    LOGGER.info("唤醒PC终端[{}]超时，不再等待", computerId);
                    throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_WAKE_UP_TIMEOUT);
                }

                // 每隔5S重试一次，最多重试60S
                if (currentTime - startTime <= wakeupTimeoutMillisecond) {
                    // 唤醒超时时间内，继续发送唤醒报文
                    // WOL唤醒
                    wakeUpByWol(computerInfoResponse);

                    // 定向广播唤醒
                    BroadcastWakeUpUtil.wakeUp(computerInfoResponse.getMac(), computerInfoResponse.getIp());
                }

                // 休眠5S
                Thread.sleep(wakeupCheckInterval);
                computerInfoResponse = getComputerInfoResponse(computerId);
                isOnline = computerInfoResponse.getState() == ComputerStateEnum.ONLINE;
            }
        } catch (BusinessException | InterruptedException ex) {
            LOGGER.error("唤醒PC终端[{}]发生异常, 异常信息：", computerId, ex);
            Thread.currentThread().interrupt();
            throw new BusinessException(BusinessKey.RCDC_RCO_COMPUTER_WAKE_UP_TIMEOUT, ex);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("成功PC唤醒终端[{}]，耗时[{}]ms", computerId, (System.currentTimeMillis() - startTime));
        }
    }

    private void wakeUpByWol(ComputerInfoResponse computerInfoResponse) throws BusinessException {
        LOGGER.info("wakeUpByWol开始通过同网段终端进行唤醒, PC终端id {}", computerInfoResponse.getId());

        if (StringUtils.isEmpty(computerInfoResponse.getNetworkNumber())) {
            LOGGER.info("wakeUpByWol终端网络号为空，不进行补偿唤醒，PC终端Id : {}", computerInfoResponse.getId());
            return;
        }

        //查找随机在线终端作为唤醒者
        ComputerEntity onlineComputer = findRandomOnlineComputer(computerInfoResponse);
        if (onlineComputer == null) {
            LOGGER.info("PC终端[{}]同网段[{}]在线PC终端数量为0", computerInfoResponse.getIp(), computerInfoResponse.getNetworkNumber());
            return;
        }

        CbbWakeUpTerminalInfoDTO wakeUpTerminalInfoDTO = new CbbWakeUpTerminalInfoDTO();
        wakeUpTerminalInfoDTO.setIp(computerInfoResponse.getIp());
        wakeUpTerminalInfoDTO.setMac(computerInfoResponse.getMac());
        THREAD_EXECUTOR.execute(() -> {
            LOGGER.info("唤醒终端[{}],唤醒参数：[{}]", onlineComputer.getId(), JSON.toJSONString(wakeUpTerminalInfoDTO));
            try {
                notifyOnlineComputerSendWol(onlineComputer.getId().toString(), wakeUpTerminalInfoDTO);
            } catch (Exception e) {
                LOGGER.error("唤醒终端[{}]失败", onlineComputer.getId(), e);
            }
        });
    }

    private ComputerEntity findRandomOnlineComputer(ComputerInfoResponse computerInfoResponse) {
        List<ComputerEntity> computerList = computerBusinessDAO.findByStateAndNetworkNumber(ComputerStateEnum.ONLINE,
                computerInfoResponse.getNetworkNumber());
        computerList = computerList.stream().filter(s -> s.getId() != computerInfoResponse.getId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(computerList)) {
            LOGGER.info("终端[{}]同网段[{}]在线终端数量为0", computerInfoResponse.getIp(), computerInfoResponse.getNetworkNumber());
            //不存在
            return null;
        }
        // 分摊压力，避免所有唤醒压力都压在同一台终端上
        ComputerEntity computerEntity = computerList.get(new Random().nextInt(computerList.size()));
        return computerEntity;
    }

    private void notifyOnlineComputerSendWol(String id, CbbWakeUpTerminalInfoDTO wakeUpTerminalInfoDTO) throws BusinessException {
        computerTcpAPI.wakeUpComputer(id, wakeUpTerminalInfoDTO);
    }

    private ComputerInfoResponse getComputerInfoResponse(UUID id) throws BusinessException {
        ComputerIdRequest computerIdRequest = new ComputerIdRequest();
        computerIdRequest.setComputerId(id);
        ComputerInfoResponse computerInfoResponse = getComputerInfoByComputerId(computerIdRequest);
        return computerInfoResponse;
    }

    private List<ExportComputerDTO> convertComputerDTO(List<ComputerEntity> entityList) {
        List<ExportComputerDTO> exportList = new ArrayList<>();
        for (int i = 0; i < entityList.size(); i++) {
            ComputerDTO dto = new ComputerDTO();
            BeanUtils.copyProperties(entityList.get(i), dto);
            try {
                CbbTerminalGroupDetailDTO terminalGroupDTO = terminalGroupMgmtAPI.loadById(entityList.get(i).getTerminalGroupId());
                if (terminalGroupDTO.getGroupName() == null) {
                    throw new BusinessException(BusinessKey.RCDC_RCO_GROUP_NOT_EXIST);
                }
                dto.setTerminalGroupName(terminalGroupDTO.getGroupName());
            } catch (Exception e) {
                LOGGER.error("get terminal group error", e);
            }
            exportList.add(new ExportComputerDTO(dto));
        }
        return exportList;
    }

    /**
     * 导出excel并且获取excel存放路径
     *
     * @param fileName 文件名
     * @return 路径
     */
    private String exportAndGetResultPath(List<ExportComputerDTO> dataList, String fileName) throws Exception {

        Assert.notNull(fileName, "fileName is null");

        String tmpFilePath = com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.EXPORT_TMP_DIRECTORY + File.separator + fileName;
        checkDirectory();
        ExportUtils.generateExcel(dataList, tmpFilePath, ExportComputerDTO.class);
        return tmpFilePath;
    }

    /**
     * 检查目录是否存在
     */
    private void checkDirectory() {
        File file = new File(com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.EXPORT_TMP_DIRECTORY);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }

    /**
     * 删除旧的缓存
     *
     * @param fileName
     */
    private void deleteOldFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (Files.exists(path)) {
            Files.delete(path);
            LOGGER.info("旧缓存存在，删除文件[{}]", fileName);
        }
    }

    /**
     * 生产文件名
     *
     * @param userId
     * @return 文件名
     */
    private String getTmpFileName(String userId) {

        return StringUtils.join(FILE_PREFIX, userId, FILE_POSTFIX);
    }

    private void buildSearchRequest(PageSearchRequest pageSearchRequest) {
        MatchEqual stateMatch = new MatchEqual();
        stateMatch.setName(MATCH_EQUAL_FIELD);
        stateMatch.setValueArr(new Object[]{Boolean.TRUE});
        Sort sort = new Sort();
        sort.setSortField(SORT_FIELD);
        sort.setDirection(Sort.Direction.DESC);
        pageSearchRequest.setMatchEqualArr(new MatchEqual[]{stateMatch});
        pageSearchRequest.setSortArr(new Sort[]{sort});
    }

    private TerminalStatisticsItem buildTerminalStatisticsItem(List<ComputerEntity> resultList) {
        AtomicInteger online = new AtomicInteger();
        AtomicInteger offline = new AtomicInteger();
        resultList.forEach(item -> {
            if (item.getState() == ComputerStateEnum.ONLINE) {
                online.incrementAndGet();
            } else {
                offline.incrementAndGet();
            }
        });

        TerminalStatisticsItem item = new TerminalStatisticsItem();
        item.setOnline(online.get());
        item.setOffline(offline.get());
        item.setTotal(offline.get() + online.get());
        return item;
    }


}
