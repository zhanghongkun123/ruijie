package com.ruijie.rcos.rcdc.rco.module.impl.imagetemplate.api;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_CLOUDDESKTOP_NOT_ALLOW_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Splitter;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ImageDeskPatternRefDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbAbortEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCancelUploadImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbRcaImageProgramSPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ControlStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AllowCreateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTypeSupportOsVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ImageNotCreateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListImageIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.FtpConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.ViewTerminalWithImageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.enums.TaskCycleEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalWithImageEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.servermodel.ServerModelBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ImageTypeSupportOsVersionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryImageTerminalListService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler.OnceCronExpressionConvert;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalFtpAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalFtpConfigInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: Function Description
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/3 18:46
 *
 * @author coderLee23
 */
public class ImageTemplateAPIImpl implements ImageTemplateAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTemplateAPIImpl.class);

    private static final Integer DEFAULT_LIMIT = 50;

    private static final String DATE_PLACE_HOLDER = " ";


    private static final Map<UUID, Lock> CHECK_IS_ALLOW_CREATE_LOCK_MAP = new ConcurrentHashMap<>(50);

    private static final String IMAGE_ROLE_TYPE = "imageRoleType";

    private static final String IMAGE_PLATFORM_ID = "platformId";

    /**
     * terminal_ftp_config配置信息
     */
    private static final String TERMINAL_FTP_CONFIG = "terminal_ftp_config";

    /**
     * rcdc服务器IP地址
     */
    private static final String RCDC_CLUSTER_VIRTUAL_IP_GLOBAL_PARAMETER_KEY = "cluster_virtual_ip";


    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private UserTerminalGroupMgmtAPI userTerminalGroupMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private CbbTerminalFtpAPI cbbTerminalFtpAPI;

    @Autowired
    private ImageTypeSupportOsVersionService imageTypeSupportOsVersionService;

    @Autowired
    private AdminPermissionAPI adminPermissionAPI;

    @Autowired
    private CbbRcaImageProgramSPI cbbRcaImageProgramSPI;

    @Autowired
    private ImageService imageService;

    @Autowired
    private QueryImageTerminalListService queryImageTerminalListService;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public AllowCreateImageTemplateDTO checkIsAllowCreateAndHasImage(UUID adminId, @Nullable UUID imageTemplateId, @Nullable UUID platformId)
            throws BusinessException {
        Assert.notNull(adminId, "adminId is not null");
        AllowCreateImageTemplateDTO allowCreateImageTemplateDTO = checkIsAllowCreate(imageTemplateId, platformId);
        // 设置是否拥有镜像权限ID
        if (!allowCreateImageTemplateDTO.getEnableCreate()) {
            allowCreateImageTemplateDTO
                    .setHasImage(adminDataPermissionAPI.hasImageByAdminIdAndImageId(adminId, allowCreateImageTemplateDTO.getImageTemplateId()));
        }

        return allowCreateImageTemplateDTO;
    }

    @Override
    public AllowCreateImageTemplateDTO checkIsAllowCreate(@Nullable UUID imageTemplateId, @Nullable UUID platformId) throws BusinessException {
        UUID lockKey = platformId;
        if (null == platformId) {
            // 当前CDC未与CCP分离，使用默认云平台ID进行限制，后期引入sp平台需要调整成其他值
            CloudPlatformDTO defaultCloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
            lockKey = defaultCloudPlatform.getId();
            platformId = defaultCloudPlatform.getId();
        }
        Lock lock = obtainLock(lockKey);
        lock.lock();
        try {
            PageWebRequest pageWebRequest = new PageWebRequest();
            pageWebRequest.setLimit(DEFAULT_LIMIT);
            AllowCreateImageTemplateDTO allowCreateImageTemplateDTO = new AllowCreateImageTemplateDTO();
            int page = 0;
            while (true) {
                pageWebRequest.setPage(page);
                LocalImageTemplatePageRequest apiRequest = new LocalImageTemplatePageRequest(pageWebRequest);
                apiRequest.appendCustomMatchEqual(new MatchEqual(IMAGE_ROLE_TYPE, new ImageRoleType[] {ImageRoleType.TEMPLATE}));
                apiRequest.appendCustomMatchEqual(new MatchEqual(IMAGE_PLATFORM_ID, new Object[] {platformId}));
                DefaultPageResponse<CbbImageTemplateDetailDTO> response = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(apiRequest);
                if (response.getTotal() == 0 || response.getItemArr() == null) {
                    // 允许创建
                    allowCreateImageTemplateDTO.setEnableCreate(Boolean.TRUE);
                    return allowCreateImageTemplateDTO;
                }
                //
                for (CbbImageTemplateDetailDTO dto : response.getItemArr()) {
                    boolean isOtherCreate = !dto.getId().equals(imageTemplateId);
                    // 不允许创建
                    if (ImageTemplateState.CREATING == dto.getImageState() && isOtherCreate) {
                        allowCreateImageTemplateDTO.setEnableCreate(Boolean.FALSE);
                        allowCreateImageTemplateDTO.setImageTemplateId(dto.getId());
                        allowCreateImageTemplateDTO.setImageNotCreateEnum(ImageNotCreateEnum.CREATING);
                        return allowCreateImageTemplateDTO;
                    }

                    // 备份中的镜像不允许创建
                    if (ControlStateEnum.BACKUP == dto.getControlState()) {
                        LOGGER.info("镜像[{}]正在备份中,不允许编辑", dto.getImageName());
                        allowCreateImageTemplateDTO.setEnableCreate(Boolean.FALSE);
                        allowCreateImageTemplateDTO.setImageTemplateId(dto.getId());
                        allowCreateImageTemplateDTO.setImageNotCreateEnum(ImageNotCreateEnum.BACKUP);
                        return allowCreateImageTemplateDTO;
                    }
                }
                LOGGER.debug("查询镜像模板第{}页，长度[{}]", page + 1, response.getTotal());
                page++;
            }
        } finally {
            lock.unlock();
        }
    }

    private synchronized Lock obtainLock(UUID platformId) {

        if (CHECK_IS_ALLOW_CREATE_LOCK_MAP.containsKey(platformId)) {
            return CHECK_IS_ALLOW_CREATE_LOCK_MAP.get(platformId);
        }

        Lock lock = new ReentrantLock();
        CHECK_IS_ALLOW_CREATE_LOCK_MAP.put(platformId, lock);

        return lock;
    }


    @Override
    public boolean isImageCanEditSystemDiskSize(UUID imageId) {
        Assert.notNull(imageId, "imageTemplateId is null");
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        ImageDeskPatternRefDTO imageDeskPatternRefDTO =
                cbbImageTemplateMgmtAPI.getImageDeskPatternRefDTOByImageIdAndDiskType(imageId, CbbDiskType.SYSTEM, false);

        if (imageDeskPatternRefDTO.getAllDeskRefNum() > 0) {
            LOGGER.info("存在镜像[{}]与云桌面绑定，不允许编辑系统盘", imageId);
            return false;
        }

        Boolean hasImageBindTerminalGroup = userTerminalGroupMgmtAPI.hasImageBindTerminalGroup(imageId);
        if (Boolean.TRUE.equals(hasImageBindTerminalGroup)) {
            LOGGER.info("存在镜像[{}]与终端组绑定，不允许编辑系统盘", imageId);
            return false;
        }

        boolean hasImageBindUserGroup = userDesktopConfigAPI.hasImageBindUserGroup(imageId);
        if (hasImageBindUserGroup) {
            LOGGER.info("存在镜像[{}]与用户组或者用户绑定，不允许编辑系统盘", imageId);
            return false;
        }

        // 镜像创建从ISO制作 并且最后的还原点为空,说明未发布过，不允许编辑系统盘，由于RCCP扩容修改会有问题，
        if (CbbImageType.VDI == imageTemplateDetail.getCbbImageType() && CbbImageTemplateCreateMode.BY_ISO == imageTemplateDetail.getCreateMode()
                && imageTemplateDetail.getLastRecoveryPointId() == null) {
            LOGGER.info("镜像[{}]创建从ISO制作，并且最后的还原点为空，不允许编辑系统盘", imageId);
            return false;
        }


        return true;
    }

    @Override
    public boolean isImageCanEditDataDiskSize(UUID imageId) {
        Assert.notNull(imageId, "imageTemplateId is null");
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);

        if (imageTemplateDetail.getClouldDeskopNum() > 0) {
            LOGGER.info("存在镜像[{}]与云桌面绑定，不允许编辑数据盘", imageId);
            return false;
        }

        Boolean hasImageBindTerminalGroup = userTerminalGroupMgmtAPI.hasImageBindTerminalGroup(imageId);
        if (Boolean.TRUE.equals(hasImageBindTerminalGroup)) {
            LOGGER.info("存在镜像[{}]与终端组绑定，不允许编辑数据盘", imageId);
            return false;
        }

        boolean hasImageBindUserGroup = userDesktopConfigAPI.hasImageBindUserGroup(imageId);
        if (hasImageBindUserGroup) {
            LOGGER.info("存在镜像[{}]与用户组或者用户绑定，不允许编辑数据盘", imageId);
            return false;
        }

        return true;
    }

    @Override
    public String generateExpression(String date, String time) {

        Assert.notNull(date, "date cannot be null");
        Assert.notNull(time, "time cannot be null");
        RcoScheduleTaskRequest rcoScheduleTaskRequest = new RcoScheduleTaskRequest();
        rcoScheduleTaskRequest.setScheduleDate(date);
        rcoScheduleTaskRequest.setScheduleTime(time);
        rcoScheduleTaskRequest.setScheduleTypeCode("scheduleTypeCode");
        rcoScheduleTaskRequest.setTaskCycle(TaskCycleEnum.ONCE);
        rcoScheduleTaskRequest.setTaskName("RcoScheduleTask");

        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> scheduleDataDTO = new ScheduleDataDTO<>();
        scheduleDataDTO.setDeskArr(new IdLabelEntry[] {IdLabelEntry.build(UUID.randomUUID(), "label")});
        rcoScheduleTaskRequest.setData(scheduleDataDTO);
        return new OnceCronExpressionConvert().generateExpression(rcoScheduleTaskRequest);
    }

    @Override
    public String parseCronExpression(String cronExpression) throws BusinessException {

        Assert.notNull(cronExpression, "time cannot be null");
        OnceCronExpressionConvert onceCronExpressionConvert = new OnceCronExpressionConvert();
        List<String> stringList = Splitter.on(DATE_PLACE_HOLDER).splitToList(cronExpression);
        CronConvertDTO cronConvertDTO = onceCronExpressionConvert.parseCronExpression(stringList);
        return cronConvertDTO.getScheduleDate() + DATE_PLACE_HOLDER + cronConvertDTO.getScheduleTime();
    }

    @Override
    public String getImageTemplateUsedMessageByUserProfileStrategyId(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId cannot be null");
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        CbbOsType osType = imageTemplateDetail.getOsType();

        // 1. 判断是否为WIN7及以上系统
        if (!CbbOsType.isWin7UpOS(osType)) {
            return LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DISABLE_IMAGE_TEMPLATE);
        }

        return StringUtils.EMPTY;
    }

    @Override
    public Boolean isLockImage(UUID imageId) {
        Assert.notNull(imageId, "imageId can not be null");

        return imageService.isLockImage(imageId);
    }

    /**
     * 快照恢复校验
     *
     * @param imageId 镜像id
     * @throws BusinessException 异常
     */
    @Override
    public void vaildImageRestoreForSnapshot(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId cannot be null");

        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);

        // 检查是否能运行还原镜像 当还原任务超过5个 （不包括自身） 不允许
        cbbImageTemplateMgmtAPI.checkAllowToRecoveryImage(imageId);
        // 存在链接克隆桌面，进行校验
        if (imageTemplateDetail.getClouldDeskopNum() > 0) {
            LOGGER.info("VDI镜像[{}]，关联了链接克隆的云桌面，需要校验该镜像是否绑定个性桌面", imageId);
            cbbImageTemplateMgmtAPI.checkVDINotBindPersonalDeskForRestore(imageId);
        }

    }

    @Override
    public void checkHasImageRunning(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");
        PageWebRequest pageWebRequest = new PageWebRequest();
        pageWebRequest.setLimit(DEFAULT_LIMIT);
        int page = 0;
        while (true) {
            pageWebRequest.setPage(page);
            LocalImageTemplatePageRequest apiRequest = new LocalImageTemplatePageRequest(pageWebRequest);
            DefaultPageResponse<CbbImageTemplateDetailDTO> response = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(apiRequest);
            if (response.getTotal() == 0 || response.getItemArr() == null) {
                return;
            }
            boolean hasImageRunning = Stream.of(response.getItemArr()).anyMatch(dto -> ImageVmHost.TERMINAL_LOCAL_EDIT != dto.getImageVmHost()
                    && ImageTemplateState.EDITING == dto.getImageState() && !imageId.equals(dto.getId()));
            if (hasImageRunning) {
                try {
                    cbbImageTemplateMgmtAPI.returnIp(imageId);
                } catch (Exception ex) {
                    LOGGER.error("调用回收 IP 接口失败", ex);
                }
                throw new BusinessException(ServerModelBusinessKey.RCO_ONLY_ONE_IMAGE_ALLOWED_TO_RUN);
            }
            LOGGER.debug("查询镜像模板第{}页，长度[{}]", page + 1, response.getTotal());
            page++;
        }
    }

    @Override
    public void abortRecoveryLocalTerminalEdit(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        CbbGetImageTemplateInfoDTO image = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        validateIsAllowAbortLocalEditImageTemplate(image);


        String terminalId = image.getTerminalId();
        // 1. 通知RCDC-CBB取消上传
        if (image.getFtpUploadState() != null && image.getFtpUploadState() == FtpUploadState.UPLOADING) {
            cbbImageTemplateMgmtAPI.cancelLocalUploadImageTemplate(new CbbCancelUploadImageTemplateDTO(imageId));
        }
        // 2. 通知RCDC-CBB放弃编辑
        cbbImageTemplateMgmtAPI.abortLocalEditImageTemplate(new CbbAbortEditImageTemplateDTO(imageId));
        // 3. 通知Shine-RCO放弃编辑
        terminalService.noticeIdvAbortLocalEditImageTemplate(terminalId, imageId);

        // 清除已上传的应用软件信息
        cbbRcaImageProgramSPI.deleteImageProgramByGroupIdAndRestorePointIdIsNull(imageId);
    }

    private void validateIsAllowAbortLocalEditImageTemplate(CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO) throws BusinessException {
        ImageTemplateState imageState = cbbGetImageTemplateInfoDTO.getImageState();
        ImageVmHost imageVmHost = cbbGetImageTemplateInfoDTO.getImageVmHost();
        if (ImageVmHost.TERMINAL_LOCAL_EDIT != imageVmHost || ImageTemplateState.EDITING != imageState) {
            throw new BusinessException(RCDC_CLOUDDESKTOP_NOT_ALLOW_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE, cbbGetImageTemplateInfoDTO.getImageName());
        }
    }


    @Override
    public void abortLocalTerminalImageExtract(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId can not be null");

        CbbGetImageTemplateInfoDTO image = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        validateIsAllowAbortLocalEditNewImageTemplate(image);

        String terminalId = image.getTerminalId();
        try {
            // 1. 通知RCDC-CBB取消上传
            cbbImageTemplateMgmtAPI.cancelLocalUploadImageTemplate(new CbbCancelUploadImageTemplateDTO(imageId));
        } finally {
            // 3. 通知Shine-RCO放弃编辑
            terminalService.noticeIdvAbortLocalEditImageTemplate(terminalId, imageId);
        }
    }

    private void validateIsAllowAbortLocalEditNewImageTemplate(CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO) throws BusinessException {
        ImageTemplateState imageState = cbbGetImageTemplateInfoDTO.getImageState();
        ImageVmHost imageVmHost = cbbGetImageTemplateInfoDTO.getImageVmHost();
        if (ImageVmHost.TERMINAL_LOCAL_EDIT != imageVmHost || ImageTemplateState.CREATING != imageState) {
            throw new BusinessException(RCDC_CLOUDDESKTOP_NOT_ALLOW_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE, cbbGetImageTemplateInfoDTO.getImageName());
        }
    }

    @Override
    public FtpConfigInfoDTO getFtpAccount() throws BusinessException {
        // 只有使用shine ftp账号才能拿到具体的镜像信息
        TerminalFtpConfigInfo terminalFtpConfigInfo = cbbTerminalFtpAPI.getTerminalFtpConfigInfo();

        if (terminalFtpConfigInfo == null) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_GET_FTP_INFO_FAIL);
        }
        FtpConfigInfoDTO ftpConfigInfoDTO = new FtpConfigInfoDTO();
        BeanUtils.copyProperties(terminalFtpConfigInfo, ftpConfigInfoDTO);
        // 服务器 vip
        String serverIp = globalParameterAPI.findParameter(RCDC_CLUSTER_VIRTUAL_IP_GLOBAL_PARAMETER_KEY);
        ftpConfigInfoDTO.setServerIp(serverIp);
        return ftpConfigInfoDTO;
    }

    @Override
    public List<ImageTypeSupportOsVersionDTO> findAllImageTypeSupportOsVersionConfig() {

        return imageTypeSupportOsVersionService.findAllImageTypeSupportOsVersionConfig();
    }

    @Override
    public boolean hasImageSupportOsVersion(@Nullable List<ImageTypeSupportOsVersionDTO> imageTypeSupportOsVersionDTOList,
            ImageTypeSupportOsVersionDTO imageTypeSupportOsVersionDTO) {
        Assert.notNull(imageTypeSupportOsVersionDTO, "cbbImageTemplateDetailDTO must not be null");

        return imageTypeSupportOsVersionService.hasImageSupportOsVersion(imageTypeSupportOsVersionDTOList, imageTypeSupportOsVersionDTO);

    }

    @Override
    public PageQueryResponse<CbbImageTemplateDTO> queryByAdmin(UUID adminId, PageQueryBuilderFactory.RequestBuilder requestBuilder)
            throws BusinessException {
        Assert.notNull(adminId, "adminId must not be null");
        Assert.notNull(requestBuilder, "requestBuilder must not be null");

        PageQueryRequest pageQueryRequest = null;

        if (adminPermissionAPI.roleIsAdminOrAdminNameIsSysadmin(adminId)) {
            // 如果是超级管理员 查询全部
            pageQueryRequest = requestBuilder.build();
        } else {
            // 进行分页条件查询
            ListImageIdRequest listImageIdRequest = new ListImageIdRequest();
            listImageIdRequest.setAdminId(adminId);
            // 获取镜像权限列表
            List<String> imageIdStrList = adminDataPermissionAPI.listImageIdByAdminId(listImageIdRequest).getImageIdList();

            if (CollectionUtils.isEmpty(imageIdStrList)) {
                // 没有镜像关联权限
                return new PageQueryResponse<>();
            } else {
                UUID[] uuidArr = imageIdStrList.stream().map(UUID::fromString).toArray(UUID[]::new);
                // 有数据权限 设置权限数组
                pageQueryRequest = requestBuilder.in("id", uuidArr).build();
            }
        }

        return cbbImageTemplateMgmtAPI.pageQuery(pageQueryRequest);
    }

    @Override
    public DefaultPageResponse<ViewTerminalWithImageDTO> queryTerminalList(PageSearchRequest request) {
        Assert.notNull(request, "PageSearchRequest不能为null");

        // 根据终端IP、MAC进行搜索
        if (StringUtils.isNotBlank(request.getSearchKeyword())) {
            request.setSearchKeyword(request.getSearchKeyword().toUpperCase());
        }

        Page<ViewTerminalWithImageEntity> page = queryImageTerminalListService.pageQuery(request);
        List<ViewTerminalWithImageEntity> entityList = page.getContent();

        DefaultPageResponse<ViewTerminalWithImageDTO> response = new DefaultPageResponse<>();
        response.setItemArr(entityList.stream().map(item -> convertEntity2Dto(item)).toArray(ViewTerminalWithImageDTO[]::new));
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public ViewTerminalWithImageDTO getTerminalWithImage(String imageId, String terminalId) {
        Assert.hasText(imageId, "imageId must not be empty");
        Assert.hasText(terminalId, "terminalId must not be empty");
        ViewTerminalWithImageEntity viewTerminalWithImageEntity = queryImageTerminalListService.getTerminalWithImage(imageId, terminalId);
        ViewTerminalWithImageDTO viewTerminalWithImageDTO = new ViewTerminalWithImageDTO();
        BeanUtils.copyProperties(viewTerminalWithImageEntity, viewTerminalWithImageDTO);
        return viewTerminalWithImageDTO;
    }

    /**
     * 把entity转换成 DTO
     *
     * @param viewTerminalWithImageEntity
     * @return
     */
    private ViewTerminalWithImageDTO convertEntity2Dto(ViewTerminalWithImageEntity viewTerminalWithImageEntity) {
        ViewTerminalWithImageDTO viewTerminalWithImageDTO = new ViewTerminalWithImageDTO();
        BeanUtils.copyProperties(viewTerminalWithImageEntity, viewTerminalWithImageDTO);
        return viewTerminalWithImageDTO;
    }
}
