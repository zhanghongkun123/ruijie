package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ProtocolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateCmLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateUwsLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FilePathContants;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.CmsUpgradeBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dao.CloudDeskAppConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dao.ImageTemplateAppVersionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity.CloudDeskAppConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.entity.ImageTemplateAppVersionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.CmsUpgradeService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.FileUtil;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.*;

/**
 * Description: CmsUpgradeServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@Service
public class CmsUpgradeServiceImpl implements CmsUpgradeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUpgradeServiceImpl.class);

    @Autowired
    private ImageTemplateAppVersionDAO imageTemplateAppVersionDAO;

    @Autowired
    private CloudDeskAppConfigDAO cloudDeskAppConfigDAO;

    @Value("${cms.path}")
    private String cmsIsoPath;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private CbbImageDriverMgmtAPI imageDriverMgmtAPI;

    @Autowired
    private ConfigFacade configFacade;

    private static final String CM_ISO_VERSION_FILE_NAME = "version.ini";

    private static final String CM_LAUNCHER_VERSION_PROPERTIES = "version";

    private static final String CM_ISO_CONFIG_FILE_NAME = "config.ini";

    private static final String CM_CONFIG_PATH_PROPERTIES = "path";

    private static final int UWS = 1;

    private static final int CMS = 0;

    /**
     * 单个挂载ISO
     */
    private static final int ONLY = 0;

    private static final int NONE = 0;

    private static final String CMS_PATH = "CM-CDROM";

    private static final String UWS_PATH = "UWS-CDROM";

    private static final int LENGTH = 2;

    @Override
    public ImageTemplateCmLauncherStateEnum getCmLauncherState(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");

        String[] latestCmLauncherVersionArr = null;
        try {
            latestCmLauncherVersionArr = getLauncherVersionFromConfig();
        } catch (Exception e) {
            LOGGER.error("从Launcher版本配置文件获取版本号失败", e);
        }
        if (latestCmLauncherVersionArr == null || isNotVersionOfInputIso(CMS_PATH)) {
            return ImageTemplateCmLauncherStateEnum.CONFIG_VERSION_NOT_EXIST;
        }

        String currentCmLauncherVersion = getCmLauncherVersionFromDb(imageId, AppTypeEnum.CMLAUNCHER);
        if (!StringUtils.hasText(currentCmLauncherVersion)) {
            return ImageTemplateCmLauncherStateEnum.DB_VERSION_NOT_EXIST;
        }
        LOGGER.debug("镜像{},当前CMS版本号{},最新CMS版本号{}", imageId, currentCmLauncherVersion, latestCmLauncherVersionArr[CMS]);

        if (currentCmLauncherVersion.equals(latestCmLauncherVersionArr[CMS])) {
            return ImageTemplateCmLauncherStateEnum.VERSION_SUCCESS;
        }
        return ImageTemplateCmLauncherStateEnum.VERSION_LOW;
    }

    @Override
    public ImageTemplateUwsLauncherStateEnum getUwsLauncherState(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");

        String[] latestCmLauncherVersionArr = null;
        try {
            latestCmLauncherVersionArr = getLauncherVersionFromConfig();
        } catch (Exception e) {
            LOGGER.error("从Launcher版本配置文件获取版本号失败", e);
        }
        if (latestCmLauncherVersionArr == null || isNotVersionOfInputIso(UWS_PATH)) {
            return ImageTemplateUwsLauncherStateEnum.CONFIG_VERSION_NOT_EXIST;
        }
        String currentUwsLauncherVersion = getCmLauncherVersionFromDb(imageId, AppTypeEnum.UWSLAUNCHER);
        if (!StringUtils.hasText(currentUwsLauncherVersion)) {
            return ImageTemplateUwsLauncherStateEnum.DB_VERSION_NOT_EXIST;
        }

        int index = UWS;
        if (latestCmLauncherVersionArr.length < LENGTH) {
            LOGGER.info("ISO路径配置文件中，只存在UWS的版本号");
            index = ONLY;
        }

        LOGGER.info("镜像{},当前UWS版本号{},最新UWS版本号{}", imageId, currentUwsLauncherVersion, latestCmLauncherVersionArr[index]);

        if (currentUwsLauncherVersion.equals(latestCmLauncherVersionArr[index])) {
            return ImageTemplateUwsLauncherStateEnum.VERSION_SUCCESS;
        }
        return ImageTemplateUwsLauncherStateEnum.VERSION_LOW;
    }


    @Override
    public String getCmLauncherVersionFromDb(UUID imageId, AppTypeEnum type) {
        Assert.notNull(imageId, "imageId is not null");
        Assert.notNull(type, "type is not null");

        ImageTemplateAppVersionEntity imageTemplateAppVersionEntity = imageTemplateAppVersionDAO.findByImageIdAndAppType(imageId, type);

        if (Objects.nonNull(imageTemplateAppVersionEntity)) {
            return imageTemplateAppVersionEntity.getAppVersion();
        }
        return "";
    }

    @Override
    public void deleteCmIsoVersionRecord(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        try {
            // CMS删除
            deleteImageTemplateAppVersion(imageId, AppTypeEnum.CMLAUNCHER);
            deleteCloudDeskAppConfig(imageId, AppTypeEnum.CMLAUNCHER);

            // UWS删除
            deleteImageTemplateAppVersion(imageId, AppTypeEnum.UWSLAUNCHER);
            deleteCloudDeskAppConfig(imageId, AppTypeEnum.UWSLAUNCHER);
        } catch (Exception e) {
            LOGGER.error("删除关联表记录失败", e);
        }
    }

    private void deleteImageTemplateAppVersion(UUID imageId, AppTypeEnum type) {
        ImageTemplateAppVersionEntity imageTemplateAppVersionEntity = imageTemplateAppVersionDAO.findByImageIdAndAppType(imageId, type);
        if (Objects.nonNull(imageTemplateAppVersionEntity)) {
            imageTemplateAppVersionDAO.delete(imageTemplateAppVersionEntity);
        }
    }

    private void deleteCloudDeskAppConfig(UUID imageId, AppTypeEnum type) {
        CloudDeskAppConfigEntity cloudDeskAppConfigEntity = cloudDeskAppConfigDAO.findByDeskIdAndAppType(imageId, type);
        if (Objects.nonNull(cloudDeskAppConfigEntity)) {
            cloudDeskAppConfigDAO.delete(cloudDeskAppConfigEntity);
        }
    }

    @Override
    public void saveIsoVersionRecord(UUID imageId, String launcherVersion, AppTypeEnum type) {
        Assert.notNull(imageId, "imageId is not null");
        Assert.hasText(launcherVersion, "cmLauncherVersion can not empty");
        Assert.notNull(type, "type is not null");

        ImageTemplateAppVersionEntity imageTemplateAppVersionEntity = imageTemplateAppVersionDAO.findByImageIdAndAppType(imageId, type);
        if (Objects.isNull(imageTemplateAppVersionEntity)) {
            imageTemplateAppVersionEntity = new ImageTemplateAppVersionEntity();
            imageTemplateAppVersionEntity.setImageId(imageId);
            imageTemplateAppVersionEntity.setLastAppVersion("");
            imageTemplateAppVersionEntity.setAppVersion(launcherVersion);
            imageTemplateAppVersionEntity.setUpdateTime(new Date());
            imageTemplateAppVersionEntity.setAppType(type);
            imageTemplateAppVersionDAO.save(imageTemplateAppVersionEntity);
        } else {
            imageTemplateAppVersionEntity.setAppVersion(launcherVersion);
            imageTemplateAppVersionEntity.setUpdateTime(new Date());
            imageTemplateAppVersionDAO.save(imageTemplateAppVersionEntity);
        }
    }

    @Override
    public void setCmIsoVersionRecord(UUID imageId, AppTypeEnum type) {
        Assert.notNull(imageId, "imageId is not null");
        Assert.notNull(type, "type is not null");

        ImageTemplateAppVersionEntity imageTemplateAppVersionEntity = imageTemplateAppVersionDAO.findByImageIdAndAppType(imageId, type);
        if (Objects.nonNull(imageTemplateAppVersionEntity)) {
            String lastestAppVersion = imageTemplateAppVersionEntity.getAppVersion();
            imageTemplateAppVersionEntity.setLastAppVersion(lastestAppVersion);
            imageTemplateAppVersionEntity.setUpdateTime(new Date());
            imageTemplateAppVersionDAO.save(imageTemplateAppVersionEntity);
        }
    }

    @Override
    public void fallbackCmIsoVersionRecord(UUID imageId, AppTypeEnum type) {
        Assert.notNull(imageId, "imageId is not null");
        Assert.notNull(type, "type is not null");

        ImageTemplateAppVersionEntity imageTemplateAppVersionEntity = imageTemplateAppVersionDAO.findByImageIdAndAppType(imageId, type);

        if (Objects.nonNull(imageTemplateAppVersionEntity)) {
            String lastAppVersion = imageTemplateAppVersionEntity.getLastAppVersion();
            imageTemplateAppVersionEntity.setAppVersion(lastAppVersion);
            imageTemplateAppVersionEntity.setUpdateTime(new Date());
            imageTemplateAppVersionDAO.save(imageTemplateAppVersionEntity);
        }
    }

    @Override
    public String getCmNewestIsoFromConfig(ProtocolType protocolType) throws BusinessException {
        Assert.notNull(protocolType, "protocolType is not null");

        if (ProtocolType.SAMBA == protocolType) {
            return getIsoSambaPath(AppTypeEnum.CMLAUNCHER);
        }

        String[] pathArr = getNewestIsoPathArray();
        if (pathArr.length < LENGTH) {
            if (pathArr[ONLY].contains(CMS_PATH)) {
                LOGGER.info("ISO路径配置文件中，只存在CMS的ISO路径");
                return pathArr[ONLY];
            }
            LOGGER.error("ISO路径配置文件中，CMS的ISO路径不存在");
            throw new BusinessException(RCDC_RCO_CMS_ISO_PATH_NOT_EXIT);
        }

        return pathArr[CMS];
    }

    @Override
    public String getUwsNewestIsoFromConfig(ProtocolType protocolType) throws BusinessException {
        Assert.notNull(protocolType, "protocolType is not null");

        if (ProtocolType.SAMBA == protocolType) {
            return getIsoSambaPath(AppTypeEnum.UWSLAUNCHER);
        }

        String[] pathArr = getNewestIsoPathArray();
        if (pathArr.length < LENGTH) {
            if (pathArr[ONLY].contains(UWS_PATH)) {
                LOGGER.info("ISO路径配置文件中，只存在UWS的ISO路径");
                return pathArr[ONLY];
            }
            LOGGER.error("ISO路径配置文件中，UWS的ISO路径不存在");
            throw new BusinessException(RCDC_RCO_UWS_ISO_PATH_NOT_EXIT);
        }

        return pathArr[UWS];
    }

    @Override
    public UUID getCmIsoId(UUID deskId, AppTypeEnum type) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(type, "type is not null");

        CloudDeskAppConfigEntity cloudDeskAppConfigEntity = cloudDeskAppConfigDAO.findByDeskIdAndAppType(deskId, type);
        if (Objects.isNull(cloudDeskAppConfigEntity)) {
            LOGGER.error("获取应用ISO挂载ID失败，云桌面ID为{}", deskId);
            throw new BusinessException(CmsUpgradeBusinessKey.RCDC_RCO_CLOUD_DESK_APP_CONFIG_NOT_EXIT, deskId.toString());
        }
        return cloudDeskAppConfigEntity.getAppIsoId();
    }

    @Override
    public void saveCmIsoConfigRecord(UUID deskId, UUID cmIsoId, AppTypeEnum type, String isoVersion) {
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(cmIsoId, "cmIsoId is not null");
        Assert.notNull(type, "type is not null");
        Assert.hasText(isoVersion, "isoVersion must not be null or empty");

        CloudDeskAppConfigEntity cloudDeskAppConfigEntity = cloudDeskAppConfigDAO.findByDeskIdAndAppType(deskId, type);
        if (Objects.isNull(cloudDeskAppConfigEntity)) {
            cloudDeskAppConfigEntity = new CloudDeskAppConfigEntity();
            cloudDeskAppConfigEntity.setDeskId(deskId);
            cloudDeskAppConfigEntity.setAppIsoId(cmIsoId);
            cloudDeskAppConfigEntity.setAppType(type);
            cloudDeskAppConfigEntity.setIsoVersion(isoVersion);
            cloudDeskAppConfigDAO.save(cloudDeskAppConfigEntity);
        } else {
            cloudDeskAppConfigEntity.setAppIsoId(cmIsoId);
            cloudDeskAppConfigEntity.setIsoVersion(isoVersion);
            cloudDeskAppConfigDAO.save(cloudDeskAppConfigEntity);
        }
    }

    @Override
    public String getCmsLauncherVersionFromConfig() {
        String[] latestCmLauncherVersionArr = null;
        try {
            latestCmLauncherVersionArr = getLauncherVersionFromConfig();
        } catch (Exception e) {
            LOGGER.error("从Launcher版本配置文件获取版本号失败", e);
        }

        if (latestCmLauncherVersionArr == null || isNotVersionOfInputIso(CMS_PATH)) {
            LOGGER.warn("cms组件不存在");
            return "";
        }

        return latestCmLauncherVersionArr[CMS];
    }

    @Override
    public String getUwsLauncherVersionFromConfig() {
        String[] latestCmLauncherVersionArr = null;
        try {
            latestCmLauncherVersionArr = getLauncherVersionFromConfig();
        } catch (Exception e) {
            LOGGER.error("从Launcher版本配置文件获取版本号失败", e);
        }
        if (latestCmLauncherVersionArr == null || isNotVersionOfInputIso(UWS_PATH)) {
            LOGGER.warn("uws组件不存在");
            return "";
        }

        int index = UWS;
        if (latestCmLauncherVersionArr.length < LENGTH) {
            LOGGER.info("ISO路径配置文件中，只存在UWS的版本号");
            index = ONLY;
        }
        return latestCmLauncherVersionArr[index];
    }

    @Override
    public void updateByDeskId(UUID deskId, AppTypeEnum appType, String isoVersion) {
        Assert.notNull(deskId, "deskId must not be null");
        Assert.notNull(appType, "appType must not be null");
        Assert.hasText(isoVersion, "isoVersion must not be null");
        cloudDeskAppConfigDAO.updateByDeskId(deskId, appType, isoVersion);
    }

    @Override
    public void copyIsoToSamba(String sourcePath, String sambaPath) {
        Assert.notNull(sourcePath, "sourcePath must not be null");
        Assert.notNull(sambaPath, "sambaPath must not be null");

        if (!serverModelAPI.isVdiModel()) {
            LOGGER.info("非VDI服务器不走samba挂载，不需要拷贝到samba");
            return;
        }

        if (FileUtil.isEqualByMetaData(sourcePath, sambaPath)) {
            LOGGER.info("文件元数据一致，无需拷贝");
            return;
        }

        boolean isSuccess = false;
        while (!isSuccess) {
            try {
                File sourceFile = new File(sourcePath);
                checkFileSpaceSpaceIsEnough(configFacade.read(FilePathContants.CM_ISO_FIX), sourceFile.length());
                // 拷贝文件元数据，方便判断文件是否一致
                FileUtil.copyFile(sourcePath, sambaPath, StandardCopyOption.COPY_ATTRIBUTES);
                isSuccess = true;

            } catch (Exception e) {
                File sambaFile = new File(sambaPath);
                if (sambaFile.exists()) {
                    sambaFile.delete();
                }
                LOGGER.error("复制samba模板文件[{}]异常，进行重试", sambaPath, e);
            }
        }
    }

    private String[] getLauncherVersionFromConfig() throws BusinessException {
        Properties verIni = FileUtil.fillProperties(cmsIsoPath, CM_ISO_VERSION_FILE_NAME);
        if (Objects.isNull(verIni)) {
            LOGGER.error("读取Launcher版本配置文件失败，路径{}", cmsIsoPath + CM_ISO_VERSION_FILE_NAME);
            throw new BusinessException(CmsUpgradeBusinessKey.RCDC_RCO_CM_ISO_VERSION_READ_ERROR);
        }
        String version = verIni.getProperty(CM_LAUNCHER_VERSION_PROPERTIES);
        if (!StringUtils.hasText(version)) {
            LOGGER.error("Launcher版本配置文件中没有相应版本");
            throw new BusinessException(CmsUpgradeBusinessKey.RCDC_RCO_VERSION_NOT_EXIT);
        }

        int index = version.indexOf("=");
        String trueVersion = version.substring(index + 1);

        if (!StringUtils.hasText(trueVersion)) {
            LOGGER.error("Launcher版本配置文件中没有相应版本");
            throw new BusinessException(CmsUpgradeBusinessKey.RCDC_RCO_VERSION_NOT_EXIT);
        }

        return trueVersion.split(":");
    }

    private boolean isNotVersionOfInputIso(String paramIso) {
        String[] pathArr;
        try {
            pathArr = getNewestIsoPathArray();
        } catch (BusinessException e) {
            LOGGER.error("读取ISO路径配置文件失败");
            return true;
        }

        for (String path : pathArr) {
            if (path.contains(paramIso)) {
                return false;
            }
        }

        return true;
    }

    private String[] getNewestIsoPathArray() throws BusinessException {
        Properties verIni = FileUtil.fillProperties(cmsIsoPath, CM_ISO_CONFIG_FILE_NAME);
        if (Objects.isNull(verIni)) {
            LOGGER.error("读取ISO路径配置文件失败，路径{}", cmsIsoPath + CM_ISO_CONFIG_FILE_NAME);
            throw new BusinessException(RCDC_RCO_CM_ISO_CONFIG_READ_ERROR);
        }

        String path = verIni.getProperty(CM_CONFIG_PATH_PROPERTIES);
        if (!StringUtils.hasText(path)) {
            LOGGER.error("ISO路径配置文件中，路径不存在");
            throw new BusinessException(RCDC_RCO_ISO_PATH_NOT_EXIT);
        }

        int index = path.indexOf("=");
        String truePath = path.substring(index + 1);

        if (!StringUtils.hasText(truePath)) {
            LOGGER.error("ISO路径配置文件中，路径不存在");
            throw new BusinessException(RCDC_RCO_ISO_PATH_NOT_EXIT);
        }

        return truePath.split(":");
    }

    private String getIsoSambaPath(AppTypeEnum appType) throws BusinessException {
        String sambaFilePath = AppTypeEnum.CMLAUNCHER == appType ?
                FilePathContants.CMS_ISO_SAMBA_PATH : FilePathContants.UWS_ISO_SAMBA_PATH;

        File sambaFile = new File(sambaFilePath);
        if (!sambaFile.exists()) {
            LOGGER.error("ISO路径配置文件中，路径不存在");
            throw new BusinessException(RCDC_RCO_ISO_PATH_NOT_EXIT);
        }
        return sambaFilePath;
    }

    private boolean checkFileSpaceSpaceIsEnough(String path, Long fileSize) {
        File packageDir = new File(path);
        final long usableSpace = packageDir.getUsableSpace();
        return usableSpace >= fileSize;
    }


}
