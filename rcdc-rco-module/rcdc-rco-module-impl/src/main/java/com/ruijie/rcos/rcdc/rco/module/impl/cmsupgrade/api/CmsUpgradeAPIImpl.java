package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.api;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskCdromAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbAddCdromRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbVDIDeskRemoveCdromIsoRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbImageCdromDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ProtocolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.SambaMountPoint;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateCmLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateUwsLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.CmsUpgradeService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbCpuArchType;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ruijie.rcos.rcdc.hciadapter.module.def.constants.Constants.DEFAULT_PLATFORM_ID;

/**
 * Description: CmsUpgradeAPIImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class CmsUpgradeAPIImpl implements CmsUpgradeAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsUpgradeAPIImpl.class);

    @Autowired
    private CmsUpgradeService cmsUpgradeService;

    @Autowired
    private CbbVDIDeskCdromAPI cbbVDIDeskCdromAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterServerMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Override
    public void addCmsIso(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        LOGGER.info("添加CMS应用软件ISO开始...{}", deskId);
        CbbAddCdromRequest cbbVDIDeskAddCdromRequest = new CbbAddCdromRequest();
        cbbVDIDeskAddCdromRequest.setDeskId(deskId);
        UUID cmIsoId = getCmIsoId(deskId, AppTypeEnum.CMLAUNCHER);
        cbbVDIDeskAddCdromRequest.setCdromId(cmIsoId);
        String filePath = cmsUpgradeService.getCmNewestIsoFromConfig(ProtocolType.FILE);
        cbbVDIDeskAddCdromRequest.setFilePath(filePath);
        cbbVDIDeskAddCdromRequest.setProtocolType(ProtocolType.FILE);
        LOGGER.info("添加CMS应用软件ISO调用...deskId={},cmIsoId={},filePath={}", deskId, cmIsoId, filePath);
        cbbVDIDeskCdromAPI.addCdrom(cbbVDIDeskAddCdromRequest);
        String cmsLauncherVersion = cmsUpgradeService.getCmsLauncherVersionFromConfig();
        cmsUpgradeService.saveCmIsoConfigRecord(deskId, cmIsoId, AppTypeEnum.CMLAUNCHER, cmsLauncherVersion);
        LOGGER.info("添加CMS应用软件ISO结束...{}", deskId);
    }

    @Override
    public void addUwsIso(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        LOGGER.info("添加UWS应用软件ISO开始...{}", deskId);
        CbbAddCdromRequest cbbVDIDeskAddCdromRequest = new CbbAddCdromRequest();
        cbbVDIDeskAddCdromRequest.setDeskId(deskId);
        UUID uwsIsoId = getCmIsoId(deskId, AppTypeEnum.UWSLAUNCHER);
        cbbVDIDeskAddCdromRequest.setCdromId(uwsIsoId);
        String filePath = cmsUpgradeService.getUwsNewestIsoFromConfig(ProtocolType.FILE);
        cbbVDIDeskAddCdromRequest.setFilePath(filePath);
        cbbVDIDeskAddCdromRequest.setProtocolType(ProtocolType.FILE);
        cbbVDIDeskAddCdromRequest.setSambaMountPoint(SambaMountPoint.ISO);
        LOGGER.info("添加UWS应用软件ISO调用...deskId={},uwsIsoId={},filePath={}", deskId, uwsIsoId, filePath);
        cbbVDIDeskCdromAPI.addCdrom(cbbVDIDeskAddCdromRequest);
        String uwsLauncherVersion = cmsUpgradeService.getUwsLauncherVersionFromConfig();
        cmsUpgradeService.saveCmIsoConfigRecord(deskId, uwsIsoId, AppTypeEnum.UWSLAUNCHER, uwsLauncherVersion);
        LOGGER.info("添加UWS应用软件ISO结束...{}", deskId);
    }


    @Override
    public List<CbbImageCdromDTO> getCmIso(UUID imageTemplateId, CbbOsType osType) {
        Assert.notNull(imageTemplateId, "imageTemplateIdis not null");
        Assert.notNull(osType, "osType is not null");

        // VDI服务器编辑镜像走SAMBA, 其他走本地
        CbbImageTemplateDetailDTO imageTemplateDetail = cbbImageTemplateMgmtAPI.getImageTemplateDetail(imageTemplateId);
        ProtocolType protocol = DEFAULT_PLATFORM_ID.equals(imageTemplateDetail.getPlatformId()) ? ProtocolType.FILE : ProtocolType.SAMBA;

        List<CbbImageCdromDTO> cdromList = Lists.newArrayList();

        if (osType != CbbOsType.UOS_64 && osType != CbbOsType.UBUNTU_64 && osType != CbbOsType.KYLIN_64) {
            if (!isInstallCmIso(imageTemplateDetail)) {
                LOGGER.info("当前镜像暂时不支持cms云空间：{}", JSON.toJSONString(imageTemplateDetail));
                // 当前镜像暂时不支持cms云空间
                return cdromList;
            }
            try {
                CbbImageCdromDTO cbbImageCdromDTO = getCmsIso(imageTemplateId);
                if (cbbImageCdromDTO != null) {
                    cdromList.add(cbbImageCdromDTO);
                }
            } catch (BusinessException e) {
                LOGGER.error("添加CMS应用软件ISO失败", e);
            }

            try {
                CbbImageCdromDTO cbbImageUwsCdromDTO = getUwsIso(imageTemplateId);
                cdromList.add(cbbImageUwsCdromDTO);
            } catch (BusinessException e) {
                LOGGER.error("添加UWS应用软件ISO失败", e);
            }
        }

        return cdromList;
    }

    @Override
    public CbbImageCdromDTO getCmsIso(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId is not null");
        CbbImageCdromDTO cbbImageCdromDTO = new CbbImageCdromDTO();
        UUID cmIsoId = getCmIsoId(imageId, AppTypeEnum.CMLAUNCHER);
        cbbImageCdromDTO.setCdromId(cmIsoId);
        String filePath = cmsUpgradeService.getCmNewestIsoFromConfig(ProtocolType.FILE);
        cbbImageCdromDTO.setFilePath(filePath);
        cbbImageCdromDTO.setProtocolType(ProtocolType.FILE);
        LOGGER.info("操作CMS应用软件ISO调用...{}, path={}", imageId, filePath);
        String cmsLauncherVersion = cmsUpgradeService.getCmsLauncherVersionFromConfig();
        cmsUpgradeService.saveCmIsoConfigRecord(imageId, cmIsoId, AppTypeEnum.CMLAUNCHER, cmsLauncherVersion);
        return cbbImageCdromDTO;
    }

    @Override
    public CbbImageCdromDTO getUwsIso(UUID imageId) throws BusinessException {
        Assert.notNull(imageId, "imageId is not null");
        CbbImageCdromDTO cbbImageCdromDTO = new CbbImageCdromDTO();
        UUID cmIsoId = getCmIsoId(imageId, AppTypeEnum.UWSLAUNCHER);
        cbbImageCdromDTO.setCdromId(cmIsoId);
        String filePath = cmsUpgradeService.getUwsNewestIsoFromConfig(ProtocolType.FILE);
        cbbImageCdromDTO.setFilePath(filePath);
        cbbImageCdromDTO.setProtocolType(ProtocolType.FILE);
        LOGGER.info("操作UWS应用软件ISO调用...{}, path={}", imageId, filePath);
        String uwsLauncherVersion = cmsUpgradeService.getUwsLauncherVersionFromConfig();
        cmsUpgradeService.saveCmIsoConfigRecord(imageId, cmIsoId, AppTypeEnum.UWSLAUNCHER, uwsLauncherVersion);
        return cbbImageCdromDTO;
    }

    private boolean isInstallCmIso(CbbImageTemplateDetailDTO imageTemplateDetail) {
        ClusterInfoDTO clusterInfo = imageTemplateDetail.getClusterInfo();
        // IDV镜像没有选择运行集群
        if (clusterInfo != null) {
            PlatformComputerClusterDTO clusterDetail = null;
            try {
                clusterDetail = computerClusterServerMgmtAPI.getComputerClusterInfoById(clusterInfo.getId());
            } catch (BusinessException e) {
                LOGGER.error("镜像[{}]安装cmISO、获取计算集群信息异常", imageTemplateDetail.getImageName(), e);
                return Boolean.FALSE;
            }

            if (Objects.equals(clusterDetail.getArchitecture(), CbbCpuArchType.ARM.toString())) {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    private UUID getCmIsoId(UUID deskId, AppTypeEnum type) {
        UUID cmIsoId = UUID.randomUUID();
        try {
            cmIsoId = cmsUpgradeService.getCmIsoId(deskId, type);
        } catch (BusinessException e) {
            LOGGER.warn("首次启动虚机关联表记录中不存在，使用随机数生成");
        }
        return cmIsoId;
    }

    @Override
    public void replaceCmsIso(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        LOGGER.info("替换CMS应用软件ISO开始...{}", deskId);
        CbbVDIDeskRemoveCdromIsoRequest cbbVDIDeskRemoveCdromIsoRequest = new CbbVDIDeskRemoveCdromIsoRequest();
        cbbVDIDeskRemoveCdromIsoRequest.setDeskId(deskId);
        UUID cmIsoId = cmsUpgradeService.getCmIsoId(deskId, AppTypeEnum.CMLAUNCHER);
        cbbVDIDeskRemoveCdromIsoRequest.setCdromId(cmIsoId);
        String filePath = cmsUpgradeService.getCmNewestIsoFromConfig(ProtocolType.FILE);
        cbbVDIDeskRemoveCdromIsoRequest.setFilePath(filePath);
        cbbVDIDeskRemoveCdromIsoRequest.setProtocolType(ProtocolType.FILE);
        String cmsLauncherVersion = cmsUpgradeService.getCmsLauncherVersionFromConfig();
        LOGGER.info("替换CMS应用软件ISO调用...deskId={},cmIsoId={},filePath={},isoVersion={}", deskId, cmIsoId, filePath, cmsLauncherVersion);
        cbbVDIDeskCdromAPI.replaceCdromISO(cbbVDIDeskRemoveCdromIsoRequest);
        cmsUpgradeService.updateByDeskId(deskId, AppTypeEnum.CMLAUNCHER, cmsLauncherVersion);
        LOGGER.info("替换CMS应用软件ISO结束...{}", deskId);
    }

    @Override
    public void replaceUwsIso(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is not null");
        LOGGER.info("替换UWS应用软件ISO开始...{}", deskId);
        CbbVDIDeskRemoveCdromIsoRequest cbbVDIDeskRemoveCdromIsoRequest = new CbbVDIDeskRemoveCdromIsoRequest();
        cbbVDIDeskRemoveCdromIsoRequest.setDeskId(deskId);
        UUID cmIsoId = cmsUpgradeService.getCmIsoId(deskId, AppTypeEnum.UWSLAUNCHER);
        cbbVDIDeskRemoveCdromIsoRequest.setCdromId(cmIsoId);
        String filePath = cmsUpgradeService.getUwsNewestIsoFromConfig(ProtocolType.FILE);
        cbbVDIDeskRemoveCdromIsoRequest.setFilePath(filePath);
        cbbVDIDeskRemoveCdromIsoRequest.setProtocolType(ProtocolType.FILE);
        String uwsLauncherVersion = cmsUpgradeService.getUwsLauncherVersionFromConfig();
        LOGGER.info("替换UWS应用软件ISO调用...deskId={},cmIsoId={},filePath={},isoVersion={}", deskId, cmIsoId, filePath, uwsLauncherVersion);
        cbbVDIDeskCdromAPI.replaceCdromISO(cbbVDIDeskRemoveCdromIsoRequest);
        cmsUpgradeService.updateByDeskId(deskId, AppTypeEnum.UWSLAUNCHER, uwsLauncherVersion);
        LOGGER.info("替换UWS应用软件ISO结束...{}", deskId);
    }

    @Override
    public ImageTemplateCmLauncherStateEnum getCmLauncherState(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        return cmsUpgradeService.getCmLauncherState(imageId);
    }

    @Override
    public ImageTemplateUwsLauncherStateEnum getUwsLauncherState(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        return cmsUpgradeService.getUwsLauncherState(imageId);
    }

    @Override
    public String getCmLauncherVersionFromDb(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        return cmsUpgradeService.getCmLauncherVersionFromDb(imageId, AppTypeEnum.CMLAUNCHER);
    }

    @Override
    public String getUwsLauncherVersionFromDb(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        return cmsUpgradeService.getCmLauncherVersionFromDb(imageId, AppTypeEnum.UWSLAUNCHER);
    }

    @Override
    public void deleteCmIsoRecord(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        cmsUpgradeService.deleteCmIsoVersionRecord(imageId);
    }

    @Override
    public void saveCmsIsoRecord(UUID imageId, String cmLauncherVersion) {
        Assert.notNull(imageId, "imageId is not null");
        Assert.hasText(cmLauncherVersion, "cmLauncherVersion can not empty");
        cmsUpgradeService.saveIsoVersionRecord(imageId, cmLauncherVersion, AppTypeEnum.CMLAUNCHER);
    }

    @Override
    public void saveUwsIsoRecord(UUID imageId, String uwsLauncherVersion) {
        Assert.notNull(imageId, "imageId is not null");
        Assert.hasText(uwsLauncherVersion, "cmLauncherVersion can not empty");
        cmsUpgradeService.saveIsoVersionRecord(imageId, uwsLauncherVersion, AppTypeEnum.UWSLAUNCHER);
    }

    @Override
    public void setCmIsoRecord(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        cmsUpgradeService.setCmIsoVersionRecord(imageId, AppTypeEnum.CMLAUNCHER);
        cmsUpgradeService.setCmIsoVersionRecord(imageId, AppTypeEnum.UWSLAUNCHER);
    }

    @Override
    public void fallbackCmIsoRecord(UUID imageId) {
        Assert.notNull(imageId, "imageId is not null");
        cmsUpgradeService.fallbackCmIsoVersionRecord(imageId, AppTypeEnum.UWSLAUNCHER);
        cmsUpgradeService.fallbackCmIsoVersionRecord(imageId, AppTypeEnum.CMLAUNCHER);
    }
}
