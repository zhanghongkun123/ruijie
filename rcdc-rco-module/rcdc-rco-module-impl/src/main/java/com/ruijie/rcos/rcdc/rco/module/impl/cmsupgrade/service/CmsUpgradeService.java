package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service;

import java.util.UUID;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ProtocolType;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateCmLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.cmsupgrade.enums.ImageTemplateUwsLauncherStateEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: CmsUpgradeService
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public interface CmsUpgradeService {
    /**
     * 获取UwsLauncher状态
     * 
     * @param imageId 镜像ID
     * @return 返回UwsLauncher状态
     */
    ImageTemplateUwsLauncherStateEnum getUwsLauncherState(UUID imageId);

    /**
     * 获取CmLauncher状态
     * 
     * @param imageId 镜像ID
     * @return 返回CmLauncher状态
     */
    ImageTemplateCmLauncherStateEnum getCmLauncherState(UUID imageId);

    /**
     * 获取关联表记录的当前CmLauncher版本号
     *
     * @param imageId 镜像ID
     * @param type ISO类型
     * @return 返回CmLauncher版本号
     */
    String getCmLauncherVersionFromDb(UUID imageId, AppTypeEnum type);

    /**
     * 删除关联表记录
     * 
     * @param imageId 镜像ID
     */
    void deleteCmIsoVersionRecord(UUID imageId);

    /**
     * 保存关联表记录
     * 
     * @param imageId 镜像ID
     * @param launcherVersion 当前CmLauncher版本号
     * @param type ISO类型
     */
    void saveIsoVersionRecord(UUID imageId, String launcherVersion, AppTypeEnum type);

    /**
     * 设置关联表记录，用于发布时保存上次版本信息
     * 
     * @param imageId 镜像ID
     * @param type ISO类型
     */
    void setCmIsoVersionRecord(UUID imageId, AppTypeEnum type);

    /**
     * 回退关联表记录，用于取消发布时回退到上次版本信息
     * 
     * @param imageId 镜像ID
     * @param type ISO类型
     */
    void fallbackCmIsoVersionRecord(UUID imageId, AppTypeEnum type);

    /**
     * 获取最新CmISO路径
     * @param protocolType 挂载协议
     * @return 返回最新CmISO路径
     * @throws BusinessException 业务异常
     */
    String getCmNewestIsoFromConfig(ProtocolType protocolType) throws BusinessException;

    /**
     * 获取最新Uws的ISO路径
     * @param protocolType 挂载协议
     * @return 返回最新UwsISO路径
     * @throws BusinessException 业务异常
     */
    String getUwsNewestIsoFromConfig(ProtocolType protocolType) throws BusinessException;

    /**
     * 获取应用ISO挂载ID
     * 
     * @param deskId 云桌面ID
     * @param type ISO类型
     * @return 结果
     * @throws BusinessException 业务异常
     */
    UUID getCmIsoId(UUID deskId, AppTypeEnum type) throws BusinessException;

    /**
     * 保存关联表记录
     * 
     * @param deskId 云桌面ID
     * @param cmIsoId 应用ISO挂载ID
     * @param type ISO类型
     * @param isoVersion iso版本号
     */
    void saveCmIsoConfigRecord(UUID deskId, UUID cmIsoId, AppTypeEnum type, String isoVersion);

    /**
     * 从配置文件获取cms的版本号
     *
     * @return 版本号
     * @throws BusinessException 业务异常
     */
    String getCmsLauncherVersionFromConfig();


    /**
     * 从配置文件获取uws的版本号
     * 
     * @return 版本号
     * @throws BusinessException 业务异常
     */
    String getUwsLauncherVersionFromConfig();

    /**
     * 根据桌面id和应用类型更新iso version
     *
     * @param deskId 云桌面id
     * @param appType 应用类型
     * @param isoVersion iso版本号
     */
    void updateByDeskId(UUID deskId, AppTypeEnum appType, String isoVersion);

    /**
     * 复制ISO到samba
     * @param source 源文件
     * @param sambaPath samba目录
     */
    void copyIsoToSamba(String source, String sambaPath);

}
