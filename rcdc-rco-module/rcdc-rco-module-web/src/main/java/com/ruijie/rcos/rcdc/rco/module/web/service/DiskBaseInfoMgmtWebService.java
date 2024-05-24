package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDiskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageDiskDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.DiskMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.disk.QueryDiskRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.disk.UpdateDiskBaseInfoRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.disk.DiskDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ImageFileMode;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL;
import static com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.RCDC_HCIADAPTER_IMAGE_DISK_BASE_INFO_UPDATE_FAIL;

/**
 * Description: 磁盘基础信息服务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/21
 *
 * @author artom
 */
@Service
public class DiskBaseInfoMgmtWebService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskBaseInfoMgmtWebService.class);

    @Autowired
    private BaseSystemLogMgmtAPI systemLogMgmtAPI;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private DiskMgmtAPI diskMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbImageDiskMgmtAPI cbbImageDiskMgmtAPI;

    /**
     * 更新磁盘关联用户信息
     *
     * @param platformId 平台ID
     * @param diskId 磁盘ID
     * @param userName 用户名称
     */
    public void remoteUpdateDiskBindUserInfo(UUID platformId, UUID diskId, @Nullable String userName) {
        Assert.notNull(diskId, "diskId can not be null");
        Assert.notNull(platformId, "platformId can not be null");

        try {
            updateDiskBindUserIfChange(platformId, diskId, userName);
        } catch (BusinessException e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, getDiskNameElseId(diskId), e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, getDiskNameElseId(diskId), e.getLocalizedMessage());
        }
    }

    /**
     * 更新桌面磁盘关联用户信息
     *
     * @param platformId 平台ID
     * @param desktopId 桌面ID
     * @param diskId 磁盘ID
     */
    public void remoteUpdateDesktopDiskBindUserInfo(UUID platformId, UUID desktopId, UUID diskId) {
        Assert.notNull(platformId, "platformId can not be null");
        Assert.notNull(desktopId, "desktopId can not be null");
        Assert.notNull(diskId, "diskId can not be null");

        try {
            final CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
            if (Objects.equals(desktopDetailDTO.getDeskType(), CbbCloudDeskType.VDI.name())) {
                updateDiskBindUserIfChange(platformId, diskId, desktopDetailDTO.getUserName());
            }
        } catch (BusinessException e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, getDiskNameElseId(diskId), e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, getDiskNameElseId(diskId), e.getLocalizedMessage());
        }
    }

    /**
     * 更新镜像关联的磁盘信息
     *
     * @param platformId 平台ID
     * @param imageId 镜像ID
     * @param imageDesc 镜像描述
     */
    public void remoteUpdateImageDiskDescInfo(UUID platformId, UUID imageId, String imageDesc) {
        Assert.notNull(platformId, "platformId can not be null");
        Assert.notNull(imageId, "imageId can not be null");
        Assert.notNull(imageDesc, "imageDesc can not be null");

        try {
            doUpdateImageDescInfo(platformId, imageId, imageDesc);
        } catch (BusinessException e) {
            LOGGER.error("更新云平台: [" + platformId + "]的镜像磁盘[" + imageId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_IMAGE_DISK_BASE_INFO_UPDATE_FAIL, imageDesc, e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("更新云平台: [" + platformId + "]的镜像磁盘[" + imageId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_IMAGE_DISK_BASE_INFO_UPDATE_FAIL, imageDesc, e.getLocalizedMessage());
        }
    }

    /**
     * 更新镜像关联的磁盘信息
     *
     * @param platformId 平台ID
     * @param diskId 磁盘ID
     * @param diskDesc 磁盘描述
     */
    public void remoteUpdateDiskDescInfo(UUID platformId, UUID diskId, @Nullable String diskDesc) {
        Assert.notNull(platformId, "platformId can not be null");
        Assert.notNull(diskId, "diskId can not be null");

        try {
            updateDiskDescIfChange(platformId, diskDesc, diskId);
        } catch (BusinessException e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_IMAGE_DISK_BASE_INFO_UPDATE_FAIL, Objects.toString(diskDesc), e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_IMAGE_DISK_BASE_INFO_UPDATE_FAIL, Objects.toString(diskDesc), e.getLocalizedMessage());
        }
    }

    /**
     * 更新磁盘的使用信息
     *
     * @param platformId 平台ID
     * @param diskId 磁盘ID
     * @param diskUseDesc 磁盘用途描述
     */
    public void remoteUpdateDiskUseDescInfo(UUID platformId, UUID diskId, @Nullable String diskUseDesc) {
        Assert.notNull(platformId, "platformId can not be null");
        Assert.notNull(diskId, "diskId can not be null");

        try {
            updateDiskUseDescIfChange(platformId, diskUseDesc, diskId);
        } catch (BusinessException e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]用途信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, Objects.toString(diskId), e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("更新云平台: [" + platformId + "]的磁盘[" + diskId + "]用途信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, Objects.toString(diskId), e.getLocalizedMessage());
        }
    }

    /**
     * 更新磁盘的使用信息
     *
     * @param platformId 平台ID
     * @param userName 用户名称
     * @param diskId 磁盘ID
     * @param diskUseDesc 磁盘用途描述
     * @param diskDesc 磁盘用途描述
     */
    public void remoteUpdateDiskFullDescInfo(UUID platformId, UUID diskId, String diskDesc, String diskUseDesc, @Nullable String userName) {
        Assert.notNull(diskId, "diskId can not be null");
        Assert.notNull(platformId, "platformId can not be null");
        Assert.notNull(diskDesc, "diskDesc can not be null");
        Assert.notNull(diskUseDesc, "diskUseDesc can not be null");

        try {
            updateDiskFullIfChange(platformId, diskId, diskDesc, diskUseDesc, userName);
        } catch (BusinessException e) {
            LOGGER.error("更新云平台的磁盘[" + diskId + "]用途信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, Objects.toString(diskId), e.getI18nMessage());
        } catch (Exception e) {
            LOGGER.error("更新云平台的磁盘[" + diskId + "]用途信息失败，失败原因：", e);
            recordDiskSystemLog(platformId, RCDC_HCIADAPTER_DISK_BASE_INFO_UPDATE_FAIL, Objects.toString(diskId), e.getLocalizedMessage());
        }
    }

    private void updateDiskFullIfChange(UUID platformId, UUID diskId, String diskDesc, String diskUse, String userName) throws BusinessException {
        final DiskDTO diskDTO = getRemoteDiskDTO(platformId, diskId);
        if (diskDTO == null) {
            LOGGER.warn("云平台[{}]不存在磁盘[{}]", platformId, diskId);
            return;
        }

        final UpdateDiskBaseInfoRequest request = buildRemoteUpdateReq(platformId, diskId, diskDTO.getDiskName());
        request.setDiskDesc(diskDesc);
        request.setDiskUseDesc(diskUse);
        request.setDiskBindUserDesc(userName);
        diskMgmtAPI.updateDiskBaseInfo(request);
    }

    private void recordDiskSystemLog(UUID platformId, String logKey, String diskName, String i18nMessage) {
        final String platformName = getPlatformNameElseId(platformId);
        systemLogMgmtAPI.createSystemLog(new Date(), logKey, platformName, diskName, i18nMessage);
    }

    private void doUpdateImageDescInfo(UUID platformId, UUID imageId, String imageDesc) throws BusinessException {
        final List<CbbImageDiskDetailDTO> imageDiskList = cbbImageDiskMgmtAPI.findByImageId(imageId);
        for (CbbImageDiskDetailDTO diskDetailDTO : imageDiskList) {
            updateDiskDescIfChange(platformId, imageDesc, diskDetailDTO.getId());
        }

    }

    private void updateDiskUseDescIfChange(UUID platformId, String diskUseDesc, UUID diskId) throws BusinessException {
        final DiskDTO diskDTO = getRemoteDiskDTO(platformId,diskId);
        if (Objects.isNull(diskDTO)) {
            return;
        }

        if (Objects.equals(diskUseDesc, diskDTO.getDiskDesc())) {
            return;
        }

        final UpdateDiskBaseInfoRequest request = buildRemoteUpdateReq(platformId, diskId, diskDTO.getDiskName());

        request.setDiskUseDesc(diskUseDesc);
        request.setDiskBindUserDesc(diskDTO.getDiskBindUserDesc());
        request.setDiskDesc(diskDTO.getDiskDesc());
        diskMgmtAPI.updateDiskBaseInfo(request);
    }

    private void updateDiskDescIfChange(UUID platformId, String imageDesc, UUID diskId) throws BusinessException {
        final DiskDTO diskDTO = getRemoteDiskDTO(platformId,diskId);
        if (Objects.isNull(diskDTO)) {
            return;
        }

        if (Objects.equals(imageDesc, diskDTO.getDiskDesc())) {
            return;
        }

        final UpdateDiskBaseInfoRequest request = buildRemoteUpdateReq(platformId, diskId, diskDTO.getDiskName());

        request.setDiskUseDesc(diskDTO.getDiskUseDesc());
        request.setDiskBindUserDesc(diskDTO.getDiskBindUserDesc());
        request.setDiskDesc(imageDesc);
        diskMgmtAPI.updateDiskBaseInfo(request);
    }

    private void updateDiskBindUserIfChange(UUID platformId, UUID diskId, String userName) throws BusinessException {
        final DiskDTO diskDTO = getRemoteDiskDTO(platformId, diskId);
        if (diskDTO == null) {
            LOGGER.warn("云平台[{}]不存在磁盘[{}]", platformId, diskId);
            return;
        }

        if (Objects.equals(userName, diskDTO.getDiskBindUserDesc())) {
            return;
        }

        final UpdateDiskBaseInfoRequest request = buildRemoteUpdateReq(platformId, diskId, diskDTO.getDiskName());
        request.setDiskDesc(diskDTO.getDiskDesc());
        request.setDiskUseDesc(diskDTO.getDiskUseDesc());
        request.setDiskBindUserDesc(userName);
        diskMgmtAPI.updateDiskBaseInfo(request);
    }

    private UpdateDiskBaseInfoRequest buildRemoteUpdateReq(UUID platformId, UUID diskId, String diskName) {
        final UpdateDiskBaseInfoRequest request = new UpdateDiskBaseInfoRequest();
        request.setPlatformId(platformId);
        request.setDiskId(diskId);
        request.setDiskName(diskName);
        return request;
    }

    private DiskDTO getRemoteDiskDTO(UUID platformId, UUID diskId) throws BusinessException {
        final QueryDiskRequest queryDiskRequest = new QueryDiskRequest(diskId, ImageFileMode.VDI);
        queryDiskRequest.setPlatformId(platformId);
        return diskMgmtAPI.queryById(queryDiskRequest).getDto();
    }

    private String getPlatformNameElseId(UUID platformId) {
        try {
            return cloudPlatformManageAPI.getInfoById(platformId).getName();
        } catch (Exception e) {
            return String.valueOf(platformId);
        }
    }

    private String getDiskNameElseId(UUID diskId) {
        try {
            return cbbVDIDeskDiskAPI.getDiskDetail(diskId).getName();
        } catch (Exception e) {
            return String.valueOf(diskId);
        }
    }

}
