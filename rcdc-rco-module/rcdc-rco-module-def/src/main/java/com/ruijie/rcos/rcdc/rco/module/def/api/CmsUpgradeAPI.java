package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbImageCdromDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateCmLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateUwsLauncherStateEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: CmsUpgradeAPI
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface CmsUpgradeAPI {

    /**
     * 添加应用软件ISO
     * 
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void addCmsIso(UUID deskId) throws BusinessException;

    /**
     * 添加应用软件ISO
     *
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void addUwsIso(UUID deskId) throws BusinessException;

    /**
     * 获取全部应用软件ISO
     * @param imageTemplateId 镜像ID
     * @param osType 操作系统类型
     * @return 返回应用软件ISO列表
     */
    List<CbbImageCdromDTO> getCmIso(UUID imageTemplateId, CbbOsType osType);

    /**
     * 获取CMS应用软件ISO
     * 
     * @param imageId 镜像ID
     * @return 返回应用软件ISO
     * @throws BusinessException 业务异常
     */
    CbbImageCdromDTO getCmsIso(UUID imageId) throws BusinessException;

    /**
     * 获取UWS应用软件ISO
     * @param imageId 镜像ID
     * @return 返回应用软件ISO
     * @throws BusinessException 业务异常
     */
    CbbImageCdromDTO getUwsIso(UUID imageId) throws BusinessException;


    /**
     * 替换应用软件ISO
     *
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void replaceCmsIso(UUID deskId) throws BusinessException;

    /**
     * 替换应用软件ISO
     * @param deskId 云桌面ID
     * @throws BusinessException 业务异常
     */
    void replaceUwsIso(UUID deskId) throws BusinessException;

    /**
     * 获取当前CmLauncher版本号
     * 
     * @param imageId 镜像
     * @return 返回当前CmLauncher版本号
     */
    String getCmLauncherVersionFromDb(UUID imageId);

    /**
     * 获取当前UwsLauncher版本号
     * @param imageId 镜像
     * @return 返回当前UwsLauncher版本号
     */
    String getUwsLauncherVersionFromDb(UUID imageId);

    /**
     * 获取CmLauncher状态
     * 
     * @param imageId 镜像
     * @return 返回CmLauncher状态
     */
    ImageTemplateCmLauncherStateEnum getCmLauncherState(UUID imageId);

    /**
     * 获取UwsLauncher状态
     * @param imageId 镜像
     * @return 返回UwsLauncher状态
     */
    ImageTemplateUwsLauncherStateEnum getUwsLauncherState(UUID imageId);

    /**
     * 删除关联表记录
     *
     * @param imageId 镜像
     */
    void deleteCmIsoRecord(UUID imageId);

    /**
     * 保存关联表记录
     *
     * @param imageId 镜像
     * @param cmLauncherVersion 当前CmLauncher版本号
     */
    void saveCmsIsoRecord(UUID imageId, String cmLauncherVersion);

    /**
     * 保存UWS关联表记录
     * @param imageId 镜像
     * @param uwsLauncherVersion 当前uwsLauncher版本号
     */
    void saveUwsIsoRecord(UUID imageId, String uwsLauncherVersion);

    /**
     * 设置关联表记录
     *
     * @param imageId 镜像
     */
    void setCmIsoRecord(UUID imageId);

    /**
     * 回退关联表记录
     *
     * @param imageId 镜像
     */
    void fallbackCmIsoRecord(UUID imageId);
}
