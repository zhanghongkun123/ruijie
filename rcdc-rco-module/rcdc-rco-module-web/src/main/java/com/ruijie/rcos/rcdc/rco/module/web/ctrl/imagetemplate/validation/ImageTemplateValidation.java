package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.validation;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImagePublishType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppDiskAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.ImageTemplateBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.SnapshotRestoreType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CloneImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ConfigVmForEditImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CreateImageTemplateByOsFileWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.CreateImageTemplatePublishTaskWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.ImageSnapshotRestoreTaskWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.TransferImageUsageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image.UpdateImageTemplateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.ServerModelBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.KYLIN_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.UBUNTU_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.UOS_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_10_32;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_10_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_11_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_7_32;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_7_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_SERVER_2016_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_SERVER_2019_64;
import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType.WIN_SERVER_2022_64;
import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.CHARSET_GB2312;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年9月4日
 *
 * @author wjp
 */
@Service
public class ImageTemplateValidation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTemplateValidation.class);

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private UamAppDiskAPI uamAppDiskAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    private static final int MIN_MEMORY = 1;

    private static final int MAX_MEMORY = 256;

    private static final int MAX_MEMORY_ONLY_FOR_IDV = 4;

    private static final int MAX_MEMORY_ONLY_FOR_MINI = 3;

    private static final int MIN_CPU = 1;

    private static final int MAX_CPU = 32;

    private static final int MAX_CPU_ONLY_FOR_IDV = 4;

    private static final int TIPS_MSG_MAX_LENGTH = 128;

    private static final int MIN_DATA_DISK_GB_SIZE = 5;

    private static final int MAX_DATA_DISK_GB_SIZE = 1024;

    private static final List<String> UN_SUPPORT_DISK_SYMBOL_LIST = //
            Collections.unmodifiableList(Lists.newArrayList("W", "S", "M", "N", "Y", "Z"));

    /**
     * 计算机名称:只能包含[a-z]、[A-Z]、[0-9]、中文或一,必须以字母数字或中文开头,不能全为数字,且不能以-结尾,长度不超过15个字符
     */
    private static final String COMPUTER_NAME_REG = "^(?![0-9]+$)(?!.*-$)[a-zA-Z0-9\\u4e00-\\u9fa5][-a-zA-Z0-9\\u4e00-\\u9fa5]{0,14}$";

    /**
     * 计算机名称最大长度
     */
    private static final int COMPUTER_NAME_SIZE = 15;

    /**
     * VDI支持的操作系统
     */
    private static final List<CbbOsType> VDI_OS_TYPE_LIST = Lists.newArrayList(WIN_7_32, WIN_7_64, WIN_10_32, WIN_10_64, WIN_11_64,
            WIN_SERVER_2016_64, WIN_SERVER_2019_64, WIN_SERVER_2022_64, UBUNTU_64, UOS_64, KYLIN_64);

    /**
     * TCI支持的操作系统
     */
    private static final List<CbbOsType> TCI_OS_TYPE_LIST = Lists.newArrayList(WIN_7_32, WIN_7_64, WIN_10_64, WIN_11_64, UOS_64, KYLIN_64);

    /**
     * IDV支持的操作系统
     */
    private static final List<CbbOsType> IDV_OS_TYPE_LIST = Lists.newArrayList(WIN_7_32, WIN_7_64, WIN_10_64, UOS_64);

    /**
     * 通过镜像文件创建镜像请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createImageTemplateByOsFileValidate(CreateImageTemplateByOsFileWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        imageTypeValueValidate(request.getCbbImageType());
        imageTypeAndImageOsTypeValidate(request.getCbbImageType(), request.getImageSystemType());
        rcaImageOsTypeValidate(request.getImageUsage(), request.getImageSystemType());
        cpuValueValidate(request.getAdvancedConfig().getCpu());
        memoryValueValidate(request.getAdvancedConfig().getMemory());
        systemSizeValidate(request.getAdvancedConfig().getSystemDisk());
        clusterStoragePoolValidation(request.getCbbImageType(), request.getAdvancedConfig(), null);
        dataSizeValidate(request.getAdvancedConfig().getImageDiskList());
        computerNameValidate(request.getAdvancedConfig().getComputerName());
        multipleVersionValidate(request.getCbbImageType(), request.getEnableMultipleVersion());
    }

    /**
     * 使用已有镜像克隆新镜像请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void createImageTemplateByCloneValidate(CloneImageTemplateWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        imageTypeValueValidate(request.getCbbImageType());
        cpuValueValidate(request.getAdvancedConfig().getCpu());
        memoryValueValidate(request.getAdvancedConfig().getMemory());
        systemSizeValidate(request.getAdvancedConfig().getSystemDisk());
        clusterStoragePoolValidation(request.getCbbImageType(), request.getAdvancedConfig(), request.getId());
        dataSizeValidate(request.getAdvancedConfig().getImageDiskList());
        computerNameValidate(request.getAdvancedConfig().getComputerName());
        CbbImageTemplateDTO cbbImageTemplateDTO = cbbImageTemplateMgmtAPI.getCbbImageTemplateDTO(request.getId());
        imageTypeAndImageOsTypeValidate(request.getCbbImageType(), cbbImageTemplateDTO.getOsType());
        multipleVersionValidate(request.getCbbImageType(), request.getEnableMultipleVersion());
    }

    /**
     * 更新镜像其他参数请求参数校验
     *
     * @param request 请求参数对象
     * @throws BusinessException 异常
     */
    public void updateImageTemplateValidate(UpdateImageTemplateWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        // request未包含镜像类型，只能通过id获取已有镜像模板信息判断
        cpuValueValidate(request.getAdvancedConfig().getCpu());
        memoryValueValidate(request.getAdvancedConfig().getMemory());
        systemSizeValidate(request.getAdvancedConfig().getSystemDisk());
        dataSizeValidate(request.getAdvancedConfig().getImageDiskList());
        computerNameValidate(request.getAdvancedConfig().getComputerName());
    }

    private void multipleVersionValidate(CbbImageType cbbImageType, Boolean enableMultipleVersion) throws BusinessException {
        // 非VDI,不支持多版本
        if (CbbImageType.VDI != cbbImageType && Boolean.TRUE.equals(enableMultipleVersion)) {
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_MULTIPLE_VERSION_ERROR);
        }
    }

    private void imageTypeValueValidate(CbbImageType cbbImageType) throws BusinessException {
        // 检验镜像类型
        if (!serverModelAPI.isVdiModel() && (cbbImageType == CbbImageType.VDI)) {
            LOGGER.error("invalid value imageType={}", cbbImageType.toString());
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_IMAGETYPE_ERROR);
        }
    }

    /**
     * 校验镜像模板类型是否支持对应操作系统
     * @param cbbImageType 镜像模板类型
     * @param cbbOsType 操作系统类型
     * @throws BusinessException 业务异常
     */
    public void imageTypeAndImageOsTypeValidate(CbbImageType cbbImageType, CbbOsType cbbOsType) throws BusinessException {
        Assert.notNull(cbbImageType, "CbbImageType cannot be null");
        Assert.notNull(cbbOsType, "cbbOsType cannot be null");
        // 校验操作类型
        if (cbbImageType == CbbImageType.VDI && VDI_OS_TYPE_LIST.contains(cbbOsType)) {
            return;
        }
        if (cbbImageType == CbbImageType.VOI && TCI_OS_TYPE_LIST.contains(cbbOsType)) {
            return;
        }
        if (cbbImageType == CbbImageType.IDV && IDV_OS_TYPE_LIST.contains(cbbOsType)) {
            return;
        }
        LOGGER.error("invalid value imageType={},imageSystemType={}", cbbImageType.toString(), cbbOsType.toString());
        throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_TEMPLATE_CREATE_BAN_OS,
                cbbImageType == CbbImageType.VOI ? "TCI" : cbbImageType.toString(),
                cbbOsType.toString());
    }

    private void rcaImageOsTypeValidate(ImageUsageTypeEnum imageUsageType, CbbOsType cbbOsType) throws BusinessException {
        Assert.notNull(imageUsageType, "imageUsageType cannot be null");
        Assert.notNull(cbbOsType, "cbbOsType cannot be null");
        // 校验操作类型
        if (imageUsageType == ImageUsageTypeEnum.APP && !CbbOsType.isRcaVdiSupportOs(cbbOsType)) {
            LOGGER.error("invalid value imageUsageType={},imageSystemType={}", imageUsageType.toString(), cbbOsType.toString());
            throw new BusinessException(BusinessKey.RCDC_RCA_VDI_IMAGE_TEMPLATE_CREATE_BAN_OS, cbbOsType.toString());
        }
    }

    private void cpuValueValidate(Integer cpu) throws BusinessException {
        // CPU值的范围
        if (serverModelAPI.isVdiModel()) {
            if (cpu < MIN_CPU || cpu > MAX_CPU) {
                LOGGER.error("invalid value cpu={}", cpu);
                throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_CPU_ERROR);
            }
        } else {
            if (cpu < MIN_CPU || cpu > MAX_CPU_ONLY_FOR_IDV) {
                LOGGER.error("invalid value cpu={}", cpu);
                throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_CPU_ERROR);
            }
        }
    }

    private void memoryValueValidate(Double memory) throws BusinessException {
        boolean isValidatePass = true;
        if (serverModelAPI.isMiniModel() && (memory < MIN_MEMORY || memory > MAX_MEMORY_ONLY_FOR_MINI)) {
            isValidatePass = false;
        } else if (serverModelAPI.isIdvModel() && (memory < MIN_MEMORY || memory > MAX_MEMORY_ONLY_FOR_IDV)) {
            isValidatePass = false;
        } else if ((serverModelAPI.isVdiModel() && (memory < MIN_MEMORY || memory > MAX_MEMORY))) {
            isValidatePass = false;
        }

        if (!isValidatePass) {
            LOGGER.error("invalid value memory={}", memory);
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_MEMORY_ERROR);
        }
    }

    private void systemSizeValidate(Integer systemSize) throws BusinessException {
        if (serverModelAPI.isMiniModel() && systemSize > Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER) {
            LOGGER.error("当前服务器为MINI环境，系统盘不允许超过[{}]GB", Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER);
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_SYSTEM_SIZE_ERROR);
        }
    }

    private void clusterStoragePoolValidation(CbbImageType cbbImageType, ConfigVmForEditImageTemplateWebRequest advancedConfig, UUID imageId)
            throws BusinessException {
        // 非VDI镜像模板不校验计算集群和存储池
        if (cbbImageType != CbbImageType.VDI) {
            if (Objects.nonNull(advancedConfig.getCluster()) || Objects.nonNull(advancedConfig.getStoragePool())
                    || Objects.nonNull(advancedConfig.getVmCluster()) || Objects.nonNull(advancedConfig.getVmStoragePool())) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_IMAGE_TEMPLATE_NOT_VDI_WITH_STORAGE_CLUSTRE_ERROR);
            }
            return;
        }
        IdLabelEntry cluster = advancedConfig.getCluster();
        if (Objects.isNull(cluster)) {
            throw new BusinessException(PublicBusinessKey.RCDC_RCO_CLUSTER_NOT_NULL_ERROR);
        }
        IdLabelEntry storagePool = advancedConfig.getStoragePool();
        if (Objects.isNull(storagePool)) {
            throw new BusinessException(PublicBusinessKey.RCDC_RCO_STORAGE_POOL_NOT_NULL_ERROR);
        }
        // 镜像ID不为空，则校验原镜像模板与计算集群cpu架构是否一致
        if (Objects.nonNull(imageId)) {
            clusterAPI.validateVDIImageTemplateFramework(cluster.getId(), imageId);
        }
        // 校验计算集群和存储池
        clusterAPI.validateComputerClusterStoragePool(cluster.getId(), storagePool.getId(), null);
        // 校验镜像计算集群和虚拟机计算集群CPU架构是否一致
        IdLabelEntry vmCluster = advancedConfig.getVmCluster();
        if (Objects.nonNull(vmCluster)) {
            clusterAPI.validateComputerClusterFramework(cluster.getId(), vmCluster.getId());
        }
        // 校验虚拟机计算集群和存储池
        IdLabelEntry vmStoragePool = advancedConfig.getVmStoragePool();
        if (Objects.nonNull(vmCluster) && Objects.nonNull(vmStoragePool)) {
            clusterAPI.validateComputerClusterStoragePool(vmCluster.getId(), vmStoragePool.getId(), null);
        }
    }

    private void dataSizeValidate(List<CbbImageDiskDTO> imageDiskList) throws BusinessException {

        if (CollectionUtils.isEmpty(imageDiskList)) {
            return;
        }
        List<CbbImageDiskDTO> dataImageList =
                imageDiskList.stream().filter(image -> image.getImageDiskType() == CbbImageDiskType.DATA).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(imageDiskList)) {
            return;
        }
        if (dataImageList.size() > 1) {
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_DATA_COUNT_ERROR);
        }

        CbbImageDiskDTO cbbImageDiskDTO = dataImageList.get(0);

        if (cbbImageDiskDTO.getVmDiskSize() < MIN_DATA_DISK_GB_SIZE || cbbImageDiskDTO.getVmDiskSize() > MAX_DATA_DISK_GB_SIZE) {
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_DATA_SIZE_ERROR, String.valueOf(MIN_DATA_DISK_GB_SIZE),
                    String.valueOf(MAX_DATA_DISK_GB_SIZE));
        }

        if (serverModelAPI.isMiniModel() && cbbImageDiskDTO.getVmDiskSize() > Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER) {
            LOGGER.error("当前服务器为MINI环境，数据盘不允许超过[{}]GB", Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER);
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_DATA_SIZE_ERROR, String.valueOf(MIN_DATA_DISK_GB_SIZE),
                    String.valueOf(Constants.MAX_SYSTEM_SIZE_FOR_MINI_SERVER));
        }

        // 盘符判定：
        if (UN_SUPPORT_DISK_SYMBOL_LIST.contains(cbbImageDiskDTO.getDiskSymbol())) {
            throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_DATA_DISK_SYMBOL_ERROR, cbbImageDiskDTO.getDiskSymbol());
        }

    }


    /**
     * 创建镜像模板定时发布任务请求参数校验
     *
     * @param request 验证请求的入参
     * @throws BusinessException 校验失败
     */
    public void validateCreateOrUpdatePublishTask(CreateImageTemplatePublishTaskWebRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");
        final String imageId = request.getImageId().toString();
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageId());
        final String imageName = Optional.ofNullable(imageTemplateDetail).map(CbbImageTemplateDetailDTO::getImageName).orElse(imageId);
        Date currDate = new Date();

        if (request.getPublishType() == ImagePublishType.CRON) {
            if (StringUtils.isEmpty(request.getScheduleTime())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIME_EMPTY_LOG, new String[] {imageName});
            }
            // 判断镜像发布时间是否小于当前服务器时间
            Date scheduleDateTime = DateUtil.parseDate(request.getScheduleTime(), DateUtil.YYYY_MM_DD_HH24MISS);
            if (scheduleDateTime == null || scheduleDateTime.before(currDate)) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIME_CHECK_ERROR_LOG,
                        new String[] {imageName});
            }

            // 纯空字符串内容判断
            if (!StringUtils.isEmpty(request.getTipMsg()) && StringUtils.isEmpty(request.getTipMsg().trim())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIP_MSG_EMPTY_LOG, new String[] {imageName});
            }

            // 通知时间和通知信息内容判断
            if (!StringUtils.isEmpty(request.getTipMsg()) && StringUtils.isEmpty(request.getNoticeTime())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_TIME_EMPTY_LOG, new String[] {imageName});
            }

            if (!StringUtils.isEmpty(request.getNoticeTime())) {

                // 需要通知前提下判断通知内容是否存在
                if (StringUtils.isEmpty(request.getTipMsg())) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_MESSAGE_EMPTY_LOG,
                            new String[] {imageName});
                }
                if (request.getTipMsg().length() > TIPS_MSG_MAX_LENGTH) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_MESSAGE_TOO_LONG_LOG,
                            new String[] {imageName});
                }
                // 判断发布通知时间是否小于当前服务器时间
                Date noticeDateTime = DateUtil.parseDate(request.getNoticeTime(), DateUtil.YYYY_MM_DD_HH24MISS);
                if (noticeDateTime == null || noticeDateTime.before(currDate)) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_TIME_CHECK_ERROR_LOG,
                            new String[] {imageName});
                }
                // 判断发布通知时间是否和发布时间间隔五分钟以上
                Calendar checkDate = Calendar.getInstance();
                checkDate.setTime(noticeDateTime);
                checkDate.add(Calendar.MINUTE, 5);

                if (checkDate.getTime().after(scheduleDateTime)) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_NOTICE_TIME_ERROR_LOG,
                            new String[] {imageName});
                }
            }
        }
    }


    /**
     * 镜像模板定时恢复任务请求参数校验
     *
     * @param request 验证请求的入参
     * @throws BusinessException 校验失败
     */
    public void validateCreateOrUpdateSnapshotRestoreTask(ImageSnapshotRestoreTaskWebRequest request) throws BusinessException {

        Assert.notNull(request, "request is not null");
        final String imageId = request.getImageTemplateId().toString();
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(request.getImageTemplateId());
        final String imageName = Optional.ofNullable(imageTemplateDetail).map(CbbImageTemplateDetailDTO::getImageName).orElse(imageId);
        Date currDate = new Date();

        if (request.getRestoreType() == SnapshotRestoreType.CRON) {
            if (StringUtils.isEmpty(request.getScheduleTime())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_TIME_EMPTY_LOG,
                        new String[] {imageTemplateDetail.getImageName(), imageName});
            }
            // 判断镜像恢复时间是否小于当前服务器时间
            Date scheduleDateTime = DateUtil.parseDate(request.getScheduleTime(), DateUtil.YYYY_MM_DD_HH24MISS);
            if (scheduleDateTime == null || scheduleDateTime.before(currDate)) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_IMAGE_SNAPSHOT_TASK_TIME_CHECK_ERROR_LOG,
                        new String[] {imageTemplateDetail.getImageName(), imageName});
            }

            // 纯空字符串内容判断
            if (!StringUtils.isEmpty(request.getTipMsg()) && StringUtils.isEmpty(request.getTipMsg().trim())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_PUBLISH_TASK_TIP_MSG_EMPTY_LOG, new String[] {imageName});
            }

            // 通知时间和通知信息内容判断
            if (!StringUtils.isEmpty(request.getTipMsg()) && StringUtils.isEmpty(request.getNoticeTime())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_TIME_EMPTY_LOG, new String[] {imageName});
            }

            if (!StringUtils.isEmpty(request.getNoticeTime())) {

                // 需要通知前提下判断通知内容是否存在
                if (StringUtils.isEmpty(request.getTipMsg())) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_MESSAGE_EMPTY_LOG,
                            new String[] {imageName});
                }
                if (request.getTipMsg().length() > TIPS_MSG_MAX_LENGTH) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_MESSAGE_TOO_LONG_LOG,
                            new String[] {imageName});
                }
                // 判断发布通知时间是否小于当前服务器时间
                Date noticeDateTime = DateUtil.parseDate(request.getNoticeTime(), DateUtil.YYYY_MM_DD_HH24MISS);
                if (noticeDateTime == null || noticeDateTime.before(currDate)) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_TIME_CHECK_ERROR_LOG,
                            new String[] {imageName});
                }
                // 判断发布通知时间是否和发布时间间隔五分钟以上
                Calendar checkDate = Calendar.getInstance();
                checkDate.setTime(noticeDateTime);
                checkDate.add(Calendar.MINUTE, 5);

                if (checkDate.getTime().after(scheduleDateTime)) {
                    throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_UPDATE_IMAGE_RESTORE_NOTICE_TIME_ERROR_LOG,
                            new String[] {imageName});
                }
            }
        }
    }

    /**
     * @param request 请求
     * @throws BusinessException 异常
     */
    public void transferImageUsageValidate(TransferImageUsageWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        UUID imageId = request.getId();
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageId);
        ImageTemplateState imageTemplateState = imageTemplateDetail.getImageState();
        if (!ImageTemplateState.isInSteadyState(imageTemplateState)) {
            throw new BusinessException(ServerModelBusinessKey.RCDC_IMAGE_USAGE_TRANSFER_FAIL_BY_IMAGE_NOT_STEADY,
                    imageTemplateDetail.getImageName());
        }
        if (ImageTemplateState.ERROR == imageTemplateState) {
            throw new BusinessException(ServerModelBusinessKey.RCDC_IMAGE_USAGE_TRANSFER_FAIL_BY_IMAGE_NOT_STEADY,
                    imageTemplateDetail.getImageName());
        }
        // 桌面镜像切换到应用镜像，判断是否有云桌面绑定
        if (imageTemplateDetail.getImageUsage() == ImageUsageTypeEnum.DESK && request.getToImageUsage() == ImageUsageTypeEnum.APP) {
            // UAM
            cbbUamAppTestAPI.isExistRelateAppTestByImageTemplateIdThrowEx(imageId);
            cbbAppDeliveryMgmtAPI.isExistRelateDeliveryGroupByImageTemplateIdThrowEx(imageId);
            uamAppDiskAPI.isExistRelateAppByImageIdThrowEx(imageId);
            // 桌面池
            desktopPoolMgmtAPI.isExistRelateDesktopPoolByImageIdThrowEx(imageId);
            return;
        }

        // 应用镜像切换到桌面镜像
        if (imageTemplateDetail.getImageUsage() == ImageUsageTypeEnum.APP && request.getToImageUsage() == ImageUsageTypeEnum.DESK) {
            // 应用池绑定检测
            rcaHostAPI.isExistRelateRcaPoolByImageIdThrowEx(imageId);
        }
    }

    private void computerNameValidate(String computerName) throws BusinessException {
        // 一个中文算两个字符,与前端保持一致
        if (computerName.matches(COMPUTER_NAME_REG) && computerName.getBytes(Charset.forName(CHARSET_GB2312)).length <= COMPUTER_NAME_SIZE) {
            return;
        }
        throw new BusinessException(ServerModelBusinessKey.RCO_IMAGE_TEMPLATE_VALID_COMPUTER_NAME_ERROR);
    }
}
