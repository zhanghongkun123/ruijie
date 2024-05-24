package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.request.CbbAppRelativeDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.CbbAppRelativeDeskInfo;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年03月31日
 *
 * @author xgx
 */
public interface UamAppDiskAPI {
    /**
     * 是否支持更新应用磁盘
     *
     * @param appId 应用磁盘ID
     * @return 是否支持
     * @throws BusinessException 业务异常
     */
    boolean canUpdateAppDisk(UUID appId) throws BusinessException;

    /**
     * 设置测试为暂停状态且下发通知
     *
     * @param appId 应用磁盘ID
     * @throws BusinessException 业务异常
     */
    void pauseTest(UUID appId) throws BusinessException;

    /**
     * 分页查询
     *
     * @param request 请求对象
     * @return 应用关联桌面列表
     * @throws BusinessException 业务异常
     */
    List<CbbAppRelativeDeskInfo> listAppRelativeDeskInfo(CbbAppRelativeDesktopRequest request) throws BusinessException;

    /**
     * 分页查询应用版本关联桌面列表
     *
     * @param pageQueryRequest 分页请求对象
     * @return 分页查询应用版本关联桌面列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<CbbAppRelativeDeskInfo> pageQueryRelativeDeskInfo(PageQueryRequest pageQueryRequest) throws BusinessException;


    /**
     * 是否存在临时虚机正在运行
     *
     * @param imageId 镜像ID
     * @throws BusinessException 业务异常
     */
    void isExistTempVmRunningThrowEx(UUID imageId) throws BusinessException;


    /**
     * 基于镜像ID查询是否存在关联的应用列表
     *
     * @param imageId 镜像Id
     * @throws BusinessException 业务异常
     */
    void isExistRelateAppByImageIdThrowEx(UUID imageId) throws BusinessException;

    /**
     * 基于网络ID查询是否存在关联的应用列表
     *
     * @param networkId 网络id
     * @throws BusinessException 业务异常
     */
    void isExistRelateAppByNetworkIdThrowEx(UUID networkId) throws BusinessException;
}
