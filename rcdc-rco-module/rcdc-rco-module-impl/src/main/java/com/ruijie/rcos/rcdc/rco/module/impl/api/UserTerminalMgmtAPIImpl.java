package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DownloadStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TerminalAuthorizationEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.DefaultTerminalDeskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.NotifyStartModeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.api.dto.ShineConfigNicWorkModeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewDesktopDetailDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewUsetTerminalDeskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.*;
import com.ruijie.rcos.rcdc.rco.module.impl.hardwarecertification.service.TerminalFeatureCodeService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.*;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.SoftTerminalListServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.TerminalListServiceImpl;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSettingDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.CapacityUnitUtils;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalDetectAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalDetectDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbNicWorkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalStartMode;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response.Status;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;

/**
 * Description: 终端管理API接口实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月20日
 *
 * @author nt
 */
public class UserTerminalMgmtAPIImpl implements UserTerminalMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserTerminalMgmtAPIImpl.class);

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private TerminalListServiceImpl terminalListService;

    @Autowired
    private SoftTerminalListServiceImpl softTerminalListService;

    @Autowired
    private CbbTerminalDetectAPI recordAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private ViewUsetTerminalDeskDAO viewUsetTerminalDeskDAO;

    @Autowired
    private TerminalGroupService terminalGroupService;

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private TerminalFeatureCodeService terminalFeatureCodeService;

    @Autowired
    private ImageDownloadStateService imageDownloadStateService;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    /**
     * 默认磁盘总大小
     */
    public static final String DEFAULT_DEV_TOTAL_SIZE = "0";

    @Override
    public DefaultPageResponse<TerminalDTO> pageQuery(PageSearchRequest request) {
        Assert.notNull(request, "pageQuery方法的request can not be null");
        Page<ViewTerminalEntity> terminalPage = terminalListService.pageQuery(request, ViewTerminalEntity.class);
        List<ViewTerminalEntity> entityList = terminalPage.getContent();
        TerminalDTO[] dataArr = new TerminalDTO[entityList.size()];
        // vdi终端 可初始化名单
        for (int i = 0; i < entityList.size(); i++) {
            ViewTerminalEntity entity = entityList.get(i);
            TerminalDTO dto = entity.convertFor();
            boolean isWhiteList = StringUtils.isNotBlank(entity.getOcsSn());

            // 不是ocs免授权并且产品型号不为空，需要查看是否在白名单免授权
            if (!isWhiteList && StringUtils.isNotBlank(dto.getProductType())) {
                CbbShineTerminalBasicInfo cbbShineTerminalBasicInfo = new CbbShineTerminalBasicInfo();
                cbbShineTerminalBasicInfo.setProductType(entity.getProductType());
                cbbShineTerminalBasicInfo.setTerminalId(entity.getTerminalId());
                cbbShineTerminalBasicInfo.setAllDiskInfo(entity.getAllDiskInfo());
                cbbShineTerminalBasicInfo.setPlatform(entity.getPlatform());
                cbbShineTerminalBasicInfo.setSerialNumber(entity.getSerialNumber());
                isWhiteList = cbbTerminalConfigAPI.isTerminalInWhiteList(entity.getProductType(), cbbShineTerminalBasicInfo);
            }

            dto.setAuthorization(TerminalAuthorizationEnum.statusCode(dto.getAuthed()));

            if (isWhiteList) {
                dto.setAuthorization(TerminalAuthorizationEnum.TERMINAL_NOT_NEED_AUTHORIZED.getCode());
            }

            calcTerminalOnlineTime(dto);
            dto.setDownloadPromptMessage(DownloadStateEnum.getDownloadPromptMessage(dto.getFailCode()));

            // IDV、TCI填充磁盘信息：

            TerminalCloudDesktopDTO cloudDesktopDTO = dto.getTerminalCloudDesktop();

            if (cloudDesktopDTO != null) {
                if (cloudDesktopDTO.getSystemSize() != null) {
                    fillTerminalImageDiskInfo(entity.getImageId(), dto);
                }
            }

            dto.setCanTerminalInit(Boolean.TRUE);

            dataArr[i] = dto;
        }
        DefaultPageResponse<TerminalDTO> response = new DefaultPageResponse<>();
        response.setTotal(terminalPage.getTotalElements());
        response.setItemArr(dataArr);
        response.setStatus(Status.SUCCESS);
        return response;
    }



    @Override
    public DefaultPageResponse<TerminalDTO> softTerminalPageQuery(PageSearchRequest request) throws BusinessException {
        Assert.notNull(request, "pageQuery方法的request can not be null");
        Page<ViewSoftTerminalEntity> terminalPage = softTerminalListService.pageQuery(request, ViewSoftTerminalEntity.class);
        List<ViewSoftTerminalEntity> entityList = terminalPage.getContent();


        // 缓存终端UUID
        List<String> terminalIdList = entityList.stream().map(ViewSoftTerminalEntity::getTerminalId).collect(Collectors.toList());
        Map<String, List<ViewUserTerminalDeskEntity>> stringListMap = buildUserTerminalDeskMap(terminalIdList);
        TerminalDTO[] dataArr = new TerminalDTO[entityList.size()];
        for (int i = 0; i < entityList.size(); i++) {
            ViewSoftTerminalEntity entity = entityList.get(i);
            TerminalDTO dto = entity.convertFor();
            List<ViewUserTerminalDeskEntity> userTerminalDeskEntityList = stringListMap.get(dto.getId());
            // 集合不為空
            if (!CollectionUtils.isEmpty(userTerminalDeskEntityList)) {
                List<String> deskNameList =
                        userTerminalDeskEntityList.stream().map(ViewUserTerminalDeskEntity::getDesktopName).collect(Collectors.toList());
                String deskName = String.join("，", deskNameList);
                dto.setDesktopName(deskName);
            }
            calcTerminalOnlineTime(dto);
            dataArr[i] = dto;
        }
        DefaultPageResponse<TerminalDTO> response = new DefaultPageResponse<>();
        response.setTotal(terminalPage.getTotalElements());
        response.setItemArr(dataArr);
        response.setStatus(Status.SUCCESS);
        return response;
    }

    private Map<String, List<ViewUserTerminalDeskEntity>> buildUserTerminalDeskMap(List<String> terminalIdList) {
        List<ViewUserTerminalDeskEntity> userTerminalDeskList = viewUsetTerminalDeskDAO.findByterminalIdIn(terminalIdList);
        // 过滤掉终端未在线的桌面信息
        userTerminalDeskList = userTerminalDeskList.stream().filter(item -> item.getHasTerminalRunning()).collect(Collectors.toList());
        Map<String, List<ViewUserTerminalDeskEntity>> map = new HashMap<>();
        for (int i = 0; i < userTerminalDeskList.size(); i++) {
            ViewUserTerminalDeskEntity entity = userTerminalDeskList.get(i);
            if (Objects.isNull(map.get(entity.getTerminalId()))) {
                List<ViewUserTerminalDeskEntity> dtoList = new ArrayList<>();
                dtoList.add(entity);
                map.put(entity.getTerminalId(), dtoList);
            } else {
                List<ViewUserTerminalDeskEntity> dtoList = map.get(entity.getTerminalId());
                dtoList.add(entity);
            }
        }
        return map;
    }


    private void fillTerminalImageDiskInfo(@Nullable UUID imageId, TerminalDTO dto) {
        if (imageId == null) {
            return;
        }

        if (dto.isIDVModel() || dto.isVOIModel()) {
            try {
                List<CbbImageDiskInfoDTO> imageDiskInfoList = cbbImageTemplateMgmtAPI.getPublishedImageDiskInfoList(imageId);
                dto.setCbbImageDiskInfoDTOList(imageDiskInfoList);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }

        }
    }

    private void calcTerminalOnlineTime(TerminalDTO dto) {
        if (dto.getTerminalState() != CbbTerminalStateEnums.ONLINE) {
            return;
        }
        if (dto.getLastOnlineTime() == null) {
            return;
        }

        long onlineTime = Instant.now().toEpochMilli() - dto.getLastOnlineTime().getTime();
        dto.setOnlineTime(onlineTime);
    }

    @Override
    public TerminalDTO findByTerminalIdAndBindUserId(String terminalId, UUID userId) {
        Assert.hasText(terminalId, "findByTerminalIdAndBindUserId方法的terminalId can not be null");
        Assert.notNull(userId, "userId can not be null");

        UserTerminalEntity entity = userTerminalDAO.findByTerminalIdAndBindUserId(terminalId, userId);
        if (null == entity) {
            // 没有用户终端信息，返回null
            return null;
        }
        return buildTerminalDTO(entity);
    }

    @Override
    public TerminalDTO findByTerminalId(String terminalId) {

        Assert.hasText(terminalId, "terminalId can not be null");

        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (null == entity) {
            // 没有用户终端信息，返回null
            return null;
        }
        return buildTerminalDTO(entity);
    }

    private TerminalDTO buildTerminalDTO(UserTerminalEntity entity) {
        TerminalDTO terminalDTO = new TerminalDTO();
        terminalDTO.setId(entity.getTerminalId());
        terminalDTO.setBindUserId(entity.getBindUserId());
        terminalDTO.setBindUserName(entity.getBindUserName());
        terminalDTO.setBindDeskId(entity.getBindDeskId());
        terminalDTO.setIdvTerminalMode(entity.getTerminalMode());
        return terminalDTO;
    }

    @Override
    public void delete(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be null");

        LOGGER.warn("userHardwareCertificationAPI to clear certification binding info, terminalId[{}]", terminalId);
        userHardwareCertificationAPI.clearTerminalBinding(terminalId);

        LOGGER.warn("deleteDeskCompletely terminal by id[{}]", terminalId);
        terminalService.delete(terminalId);

        LOGGER.warn("terminalFeatureCodeService to delete featureCode record, terminalId[{}]", terminalId);
        terminalFeatureCodeService.deleteByTerminalId(terminalId);


        LOGGER.info("imageDownloadStateService to delete download state, terminalId[{}]", terminalId);
        imageDownloadStateService.deleteByTerminalId(terminalId);

        LOGGER.info("systemBusinessMapping to delete  record, terminalId[{}]", terminalId);
        systemBusinessMappingAPI.deleteBySrcId(terminalId);
    }

    @Override
    public TerminalDTO getTerminalById(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "request can not be null");

        ViewTerminalEntity terminal = terminalService.getViewByTerminalId(terminalId);
        TerminalDTO terminalDTO = new TerminalDTO();
        terminal.convertTo(terminalDTO);
        completeDetectInfo(terminalId, terminalDTO);

        completeNetworkInfo(terminalDTO, terminal);
        // 获取终端分组，包括父分组
        String[] groupNameArr = terminalGroupService.getTerminalGroupNameArr(terminal.getTerminalGroupId());
        terminalDTO.setTerminalGroupNameArr(groupNameArr);
        // 下载提示语
        terminalDTO.setDownloadPromptMessage(DownloadStateEnum.getDownloadPromptMessage(terminalDTO.getFailCode()));

        return terminalDTO;
    }

    private void completeDetectInfo(String terminalId, TerminalDTO terminalDTO) throws BusinessException {
        CbbTerminalDetectDTO detectInfo = recordAPI.getRecentDetect(terminalId);
        if (detectInfo == null) {
            return;
        }
        terminalDTO.setAccessInternet(detectInfo.getAccessInternet());
        terminalDTO.setBandwidth(detectInfo.getBandwidth());
        terminalDTO.setDelay(detectInfo.getDelay());
        terminalDTO.setIpConflict(detectInfo.getIpConflict());
        terminalDTO.setPacketLossRate(detectInfo.getPacketLossRate());
        terminalDTO.setDetectTime(detectInfo.getDetectTime());
        if (detectInfo.getCheckState() != null) {
            terminalDTO.setDetectState(detectInfo.getCheckState().getState());
        }
    }

    private void completeNetworkInfo(TerminalDTO terminalDTO, ViewTerminalEntity terminal) throws BusinessException {
        if (terminalDTO.getBindDeskId() == null) {
            LOGGER.info("终端{}未绑定桌面，没有网络信息", terminalDTO.getTerminalName());
            return;
        }
        UUID desktopId = terminalDTO.getBindDeskId();
        ViewUserDesktopEntity userDesktopEntity = viewDesktopDetailDAO.findByCbbDesktopId(desktopId);
        if (Objects.isNull(userDesktopEntity)) {
            // 未查询到桌面信息，返回
            return;
        }

        List<CloudDesktopDTO> cloudDesktopDTOList = queryCloudDesktopService.convertCloudDesktop(Arrays.asList(userDesktopEntity));
        CloudDesktopDTO cloudDesktopDTO = cloudDesktopDTOList.stream().findFirst().orElseGet(CloudDesktopDTO::new);

        TerminalCloudDesktopDTO terminalCloudDesktopDTO = new TerminalCloudDesktopDTO();
        terminalCloudDesktopDTO.setAllowLocalDisk(terminal.getAllowLocalDisk());
        terminalCloudDesktopDTO.convertFor(cloudDesktopDTO);
        terminalDTO.setTerminalCloudDesktop(terminalCloudDesktopDTO);
    }

    /**
     * 更新终端访客登录设置
     * nieting 后续可以扩展vdi的实现
     *
     * @param request 请求参数
     * @throws BusinessException 业务异常
     */
    @Override
    public void updateVisitorSetting(VisitorSettingDTO request) throws BusinessException {
        Assert.notNull(request, "IdvUpdateVisitorSettingRequest can not be null");
        // 校验idv访客参数是否配置正确
        checkVistorConfig(request);
        // 通知idv终端进行访客登录设置
        terminalService.syncTerminalVisitorSetting(request);
    }

    private void checkVistorConfig(VisitorSettingDTO request) throws BusinessException {
        // 获取终端信息同时校验终端是否存在
        CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());

        // 校验终端是否在线
        if (response.getState() == CbbTerminalStateEnums.OFFLINE) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_NOT_ONLINE_NOT_ALLOW_SETTING, response.getUpperMacAddrOrTerminalId());
        }
        // 校验是否idv终端 //VOI 不做处理 由于前端的功能已经屏蔽 连这个接口都无法调用！！
        if (response.getTerminalPlatform() != CbbTerminalPlatformEnums.IDV) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_NOT_IDV_NOT_ALLOW_SETTING, response.getUpperMacAddrOrTerminalId());
        }

        // 校验是否是public模式
        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(request.getTerminalId());
        if (entity.getTerminalMode() != IdvTerminalModeEnums.PUBLIC) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_NOT_PUBLIC_NOT_ALLOW_SETTING, response.getUpperMacAddrOrTerminalId());
        }
        // 访客登录未开启时不能开启自动登录
        Boolean enableVisitorLogin = request.getEnableVisitorLogin();
        Boolean enableAutoLogin = request.getEnableAutoLogin();
        if (Boolean.FALSE.equals(enableVisitorLogin) && Boolean.TRUE.equals(enableAutoLogin)) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_IDV_VISITOR_SETTING_CONFIG_WRONG, response.getUpperMacAddrOrTerminalId());
        }
    }

    @Override
    public void initialize(String terminalId, Boolean retainImage) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be blank");
        Assert.notNull(retainImage, "retainImage can not be blank");

        LOGGER.warn("initializeTerminalCompletely terminal by terminalId[{}] retainImage:[{}]", terminalId, retainImage);
        terminalService.initialize(terminalId, retainImage);
    }

    @Override
    public void editTerminalSetting(UserTerminalRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        TerminalSettingDTO terminalSettingDTO = new TerminalSettingDTO();
        BeanUtils.copyProperties(request, terminalSettingDTO);
        terminalService.editTerminalSetting(terminalSettingDTO);
    }

    @Override
    public List<TerminalDTO> queryListByPlatformAndGroupIdAndState(CbbTerminalPlatformEnums platform, UUID terminalGroupId,
            CbbTerminalStateEnums terminalState) {
        Assert.notNull(platform, "platform must be present");
        Assert.notNull(terminalGroupId, "terminalGroupId must be present");
        Assert.notNull(terminalState, "terminalState must be present");

        List<ViewTerminalEntity> viewTerminalEntityList =
                this.viewTerminalDAO.findViewTerminalEntitiesByPlatformAndTerminalGroupIdAndTerminalState(platform, terminalGroupId, terminalState);
        if (CollectionUtils.isEmpty(viewTerminalEntityList)) {
            return Lists.newArrayList();
        }
        return viewTerminalEntityList.stream().map(ViewTerminalEntity::convertFor).collect(Collectors.toList());
    }

    @Override
    public DefaultPageResponse<TerminalDeskInfoDTO> getTerminalDeskInfoList(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be null");

        List<TerminalDeskInfoDTO> resultList = Lists.newArrayList();
        TerminalDTO terminalDTO = this.getTerminalById(terminalId);
        String allDiskInfo = terminalDTO.getAllDiskInfo();
        if (StringUtils.isNotEmpty(allDiskInfo)) {
            List<DefaultTerminalDeskInfoDTO> terminalDeskInfoList = JSONArray.parseArray(allDiskInfo, DefaultTerminalDeskInfoDTO.class);
            terminalDeskInfoList.stream().forEach(item -> {
                TerminalDeskInfoDTO terminalDeskInfoDTO = new TerminalDeskInfoDTO();
                BeanUtils.copyProperties(item, terminalDeskInfoDTO);
                String devTotalSize =
                        Optional.ofNullable(terminalDeskInfoDTO.getDevTotalSize()).filter(StringUtils::isNotEmpty).orElse(DEFAULT_DEV_TOTAL_SIZE);
                terminalDeskInfoDTO.setDevTotalSize(CapacityUnitUtils.dynamicChange(new BigDecimal(devTotalSize)));
                resultList.add(terminalDeskInfoDTO);
            });
        }
        DefaultPageResponse response = new DefaultPageResponse();
        response.setItemArr(resultList.toArray());
        response.setTotal(resultList.size());

        return response;
    }

    @Override
    public void verifyTerminalIdExist(String[] terminalIdArr) throws BusinessException {
        Assert.notNull(terminalIdArr, "terminalIdArr can not be null");

        for (String terminalId : terminalIdArr) {
            cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        }

    }

    @Override
    public void noticeIdvAbortLocalEditImageTemplate(String terminalId, UUID imageId) {
        Assert.hasText(terminalId, "noticeIdvAbortLocalEditImageTemplate()的terminalId must not be null or empty");
        Assert.notNull(imageId, "imageId can not be null");
        terminalService.noticeIdvAbortLocalEditImageTemplate(terminalId, imageId);
    }

    @Override
    public void checkIdvExistsLocalEditImageTemplate(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "checkIdvExistsLocalEditImageTemplate()的terminalId must not be null or empty");
        terminalService.checkIdvExistsLocalEditImageTemplate(terminalId);
    }

    @Override
    public String getTerminalFeatureCode(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "getTerminalFeatureCode()的terminalId must not be null or empty");
        return terminalFeatureCodeService.saveAndGetFeatureCode(terminalId);
    }

    @Override
    public List<TerminalDTO> listByState(CbbTerminalStateEnums terminalState) {
        Assert.notNull(terminalState, "terminalState must be present");

        List<ViewTerminalEntity> viewTerminalEntityList = this.viewTerminalDAO.findByTerminalState(terminalState);
        return viewTerminalEntityList.stream().distinct().map(ViewTerminalEntity::convertFor).collect(Collectors.toList());
    }

    @Override
    public Boolean getSupportTcByBindDeskIdAndPlatform(UUID bindDeskId, CbbTerminalPlatformEnums platform) {
        Assert.notNull(bindDeskId, "bindDeskId must not null");
        Assert.notNull(platform, "platform must not null");

        ViewTerminalEntity terminalEntity = viewTerminalDAO.findByBindDeskIdAndPlatform(bindDeskId, platform);

        // 如果查询结果为空，则认为支持TC引导
        return Optional.ofNullable(terminalEntity).map(ViewTerminalEntity::getSupportTcStart).orElse(Boolean.TRUE);
    }

    @Override
    public String getTerminalIdByBindDeskId(UUID bindDeskId) {
        Assert.notNull(bindDeskId, "bindDeskId must not null");

        ViewTerminalEntity terminalEntity = viewTerminalDAO.findByBindDeskId(bindDeskId);
        return Optional.ofNullable(terminalEntity).map(ViewTerminalEntity::getTerminalId).orElse(null);
    }

    @Override
    public void wakeUpTerminal(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId must not null");
        terminalService.wakeUpTerminal(terminalId);
    }

    @Override
    public void importUserTerminal(CbbShineTerminalBasicInfo cbbShineTerminalBasicInfo) {
        Assert.notNull(cbbShineTerminalBasicInfo, "cbbShineTerminalBasicInfo must not null");
        terminalService.saveTerminal(cbbShineTerminalBasicInfo);
    }

    @Override
    public void configTerminalNicWorkMode(String terminalId, CbbNicWorkModeEnums nicWorkMode) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(nicWorkMode, "nicWorkMode must not be null");

        CbbTerminalBasicInfoDTO terminalBasicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

        // 校验是否允许修改
        checkCanConfigNicWorkMode(terminalBasicInfo);

        // 通知SHINE进行修改
        notifyTerminal(terminalId, nicWorkMode, terminalBasicInfo.getUpperMacAddrOrTerminalId());

        // 修改CBB表
        cbbTerminalOperatorAPI.updateTerminalNicWorkMode(terminalId, nicWorkMode);
    }

    private void notifyTerminal(String terminalId, CbbNicWorkModeEnums nicWorkMode, String terminalMacAddr) throws BusinessException {
        CbbShineMessageRequest<String> shineMessageRequest = CbbShineMessageRequest.create(ShineAction.SET_NETWORK_MODEL, terminalId);
        shineMessageRequest.setContent(JSON.toJSONString(new ShineConfigNicWorkModeDTO(nicWorkMode.getName())));

        CbbShineMessageResponse<?> cbbShineMessageResponse;
        try {
            LOGGER.info("通知终端[{}]修改网卡工作模式为[{}]", terminalId, nicWorkMode);
            cbbShineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(shineMessageRequest);
        } catch (Exception e) {
            LOGGER.error("修改终端[" + terminalId + "]网卡工作模式失败，失败原因：", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_REQUEST_TERMINAL_TIME_OUT, e, terminalMacAddr);
        }

        // 如果shine返回失败
        if (cbbShineMessageResponse == null || cbbShineMessageResponse.getCode() != 0) {
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_SHINE_OPERATE_FAIL, terminalMacAddr);
        }
    }

    private void checkCanConfigNicWorkMode(CbbTerminalBasicInfoDTO terminalBasicInfo) throws BusinessException {
        // 是否IDV模式
        if (terminalBasicInfo.getTerminalPlatform() != CbbTerminalPlatformEnums.IDV) {
            LOGGER.error("当前终端部署模式为[{}]，不允许修改网卡工作", terminalBasicInfo.getTerminalPlatform());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_ONLY_SUPPORT_IDV);
        }

        // 是否在线
        if (CbbTerminalStateEnums.ONLINE != terminalBasicInfo.getState()) {
            LOGGER.error("当前终端状态为[{}]，不允许修改网卡工作模式", terminalBasicInfo.getState());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_ONLY_SUPPORT_ONLINE);
        }
    }

    @Override
    public void configTerminalFullSystemDisk(String terminalId, ShineConfigFullSystemDiskDTO request) throws BusinessException {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        TerminalDTO terminalDTO = this.getTerminalById(terminalId);
        request.setDeskId(terminalDTO.getBindDeskId());
        terminalService.configTerminalFullSystemDisk(terminalDTO, request);
    }

    @Override
    public void bindUser(String terminalId, UUID bindUserId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId must not be null");
        Assert.notNull(bindUserId, "bindUserId must not be null");
        terminalService.bindUser(terminalId, bindUserId);
    }


    // 解绑关联终端的云桌面
    @Override
    public void unbindDesktopTerminal(String terminalId) {
        Assert.notNull(terminalId, "terminalId must not null");
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (userTerminalEntity == null) {
            LOGGER.error("终端[{}]不存在", terminalId);
            return;
        }
        cloudDesktopOperateService.unbindDesktopTerminal(userTerminalEntity);
    }

    @Override
    public List<TerminalDTO> queryListByPlatformAndState(CbbTerminalPlatformEnums platform, CbbTerminalStateEnums terminalState) {
        Assert.notNull(platform, "platform must be present");
        Assert.notNull(terminalState, "terminalState must be present");

        List<ViewTerminalEntity> viewTerminalEntityList =
                this.viewTerminalDAO.findViewTerminalEntitiesByPlatformAndTerminalState(platform, terminalState);
        if (CollectionUtils.isEmpty(viewTerminalEntityList)) {
            return Lists.newArrayList();
        }
        return viewTerminalEntityList.stream() //
                .map(ViewTerminalEntity::convertFor) //
                .collect(Collectors.toList());
    }

    @Override
    public void changeTerminalServerIp(String terminalId, String serverIp) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be blank");
        Assert.hasText(serverIp, "serverIp can not be blank");

        terminalService.changeTerminalServerIp(terminalId, serverIp);
    }

    @Override
    public ImageDownloadStateDTO getDownImageByTerminalId(String terminalId) {
        Assert.hasText(terminalId, "terminalId must not be empty");
        return imageDownloadStateService.getByTerminalId(terminalId);
    }

    @Override
    public void configTerminalStartMode(String terminalId, CbbTerminalStartMode terminalStartMode) throws BusinessException {
        Assert.notNull(terminalId, "terminalId cant be null");
        Assert.notNull(terminalStartMode, "terminalStartMode cant be null");
        TerminalDTO terminalDTO = getTerminalById(terminalId);
        if (terminalDTO.getPlatform() != CbbTerminalPlatformEnums.VOI) {
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_FAIL_PLATFORM_ERROR);
        }
        CbbTerminalBasicInfoDTO terminal = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        // 不支持TC
        if (terminalStartMode == CbbTerminalStartMode.TC && terminal.getSupportTcStart() != null && !terminal.getSupportTcStart()) {
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_FAIL_NOT_SUPPORT_TC);
        }
        notifyTerminalStartMode(terminalId, terminalStartMode);
        updateStartMode(terminalStartMode, terminalId);
    }

    @Override
    public void updateIDVBootTypeToNull() {
        List<String> idvTerminalIdList = cbbTerminalOperatorAPI.findByPlatform(CbbTerminalPlatformEnums.IDV).stream()
                .map(CbbTerminalBasicInfoDTO::getTerminalId).collect(Collectors.toList());
        userTerminalDAO.updateBootType(null, idvTerminalIdList);
    }

    private void notifyTerminalStartMode(String terminalId, CbbTerminalStartMode cbbTerminalStartMode) throws BusinessException {
        Assert.hasText(terminalId, "Param [terminalId] must not be null");
        Assert.notNull(cbbTerminalStartMode, "cbbTerminalStartMode [cbbTerminalStartMode] must not be null");

        NotifyStartModeDTO notifyStartModeDTO = new NotifyStartModeDTO();
        notifyStartModeDTO.setStartMode(cbbTerminalStartMode.getMode());
        CbbShineMessageResponse cbbShineMessageResponse = notifyTerminal(terminalId, ShineAction.NOTIFY_CHANGE_START_MODE, notifyStartModeDTO);
        // 如果shine返回失败
        if (cbbShineMessageResponse == null || cbbShineMessageResponse.getCode() != 0) {
            ConfigStartModeCode errorCode = ConfigStartModeCode.getByCode(cbbShineMessageResponse.getCode());
            throw new BusinessException(errorCode.getMessage());
        }
    }

    private CbbShineMessageResponse notifyTerminal(String terminalId, String action, Object object) throws BusinessException {
        CbbShineMessageRequest<String> shineMessageRequest = CbbShineMessageRequest.create(action, terminalId);
        shineMessageRequest.setContent(JSON.toJSONString(object));
        CbbTerminalBasicInfoDTO basicInfoByTerminalId = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

        String logTerminalId = basicInfoByTerminalId.getMacAddr().toUpperCase();
        CbbShineMessageResponse<?> cbbShineMessageResponse;
        try {
            LOGGER.info("通知终端[{}]action:[{}],消息[{}]", logTerminalId, action, JSON.toJSONString(object));
            cbbShineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(shineMessageRequest);
        } catch (Exception e) {
            LOGGER.error("通知终端[" + logTerminalId + "]失败，失败原因：", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_REQUEST_TERMINAL_TIME_OUT, e, logTerminalId);
        }
        return cbbShineMessageResponse;

    }

    private void updateStartMode(CbbTerminalStartMode voiStartMode, String terminalId) throws BusinessException {
        if (org.springframework.util.StringUtils.hasText(terminalId) && voiStartMode != null) {
            LOGGER.info("终端[{}]开机模式设置为[{}]", terminalId, voiStartMode);
            cbbTerminalOperatorAPI.setTerminalStartMode(terminalId, voiStartMode);
            updateSeatStartMode(terminalId, voiStartMode);
        }
    }

    private void updateSeatStartMode(String terminalId, CbbTerminalStartMode terminalStartMode) throws BusinessException {
        UserTerminalEntity terminal = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (terminal == null) {
            LOGGER.info("数据库不存在终端[{}]对应信息", terminalId);
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_START_MODE_FAIL_TERMINAL_NOT_EXIST);
        }
        terminal.setBootType(terminalStartMode.getMode());
        userTerminalDAO.save(terminal);
    }


}
