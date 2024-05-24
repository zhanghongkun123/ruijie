package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AllowCreateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ImageTypeSupportOsVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.FtpConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.ViewTerminalWithImageDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: ImageTemplateAPI 镜像模板api
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/3 18:42
 *
 * @author coderLee23
 */
public interface ImageTemplateAPI {

    /**
     * 检测是否允许创建新镜像模板
     *
     * @param imageTemplateId 镜像模板UUID（允许为空）
     * @param platformId 平台ID
     * @return true or false
     * @throws BusinessException ex
     */
    AllowCreateImageTemplateDTO checkIsAllowCreate(@Nullable UUID imageTemplateId, @Nullable UUID platformId) throws BusinessException;


    /**
     * 检测是否允许创建新镜像模板
     *
     * @param adminId 管理员ID
     * @param imageTemplateId 镜像模板UUID（允许为空）
     * @param platformId 平台ID
     * @return true or false
     * @throws BusinessException ex
     */
    AllowCreateImageTemplateDTO checkIsAllowCreateAndHasImage(UUID adminId, @Nullable UUID imageTemplateId, @Nullable UUID platformId)
            throws BusinessException;


    /**
     * 判断系统盘是否可以修订
     *
     * @param imageId 镜像模板UUID
     * @return 响应
     */
    boolean isImageCanEditSystemDiskSize(UUID imageId);

    /**
     * 判断数据盘是否可以修订
     *
     * @param imageId 镜像模板UUID
     * @return 响应
     */
    boolean isImageCanEditDataDiskSize(UUID imageId);

    /**
     * 生成Cron表达式
     *
     * @param date 日期
     * @param time 时间
     * @return Cron表达式
     */
    String generateExpression(String date, String time);

    /**
     * Cron表达式转 RcoScheduleTaskDTO
     *
     * @param cronExpression 入参
     * @return 响应
     * @throws BusinessException 转换错误
     */
    String parseCronExpression(String cronExpression) throws BusinessException;

    /**
     * 快照恢复校验
     *
     * @param imageId 镜像id
     * @throws BusinessException 异常
     */
    void vaildImageRestoreForSnapshot(UUID imageId) throws BusinessException;

    /**
     * 检验是否存在允许中的镜像
     * 
     * @param imageId 镜像ID
     * @throws BusinessException 业务异常
     */
    void checkHasImageRunning(UUID imageId) throws BusinessException;


    /**
     * 终止本地编辑镜像
     * 
     * @param imageId 镜像
     * @throws BusinessException 业务异常
     */
    void abortRecoveryLocalTerminalEdit(UUID imageId) throws BusinessException;

    /**
     * 终止本地镜像提取
     * 
     * @param imageId 镜像
     * @throws BusinessException 业务异常
     */
    void abortLocalTerminalImageExtract(UUID imageId) throws BusinessException;

    /**
     * 根据用户配置策略id查询镜像模板是否可用
     *
     * @param imageId 镜像模板ID
     * @return 镜像模板是否可用信息
     * @throws BusinessException 业务异常
     */
    String getImageTemplateUsedMessageByUserProfileStrategyId(UUID imageId) throws BusinessException;

    /**
     * 获取镜像ftp信息
     * 
     * @return FtpConfigInfoDTO
     * @throws BusinessException 业务异常
     */
    FtpConfigInfoDTO getFtpAccount() throws BusinessException;

    /**
     * 获取镜像类型支持的操作系统版本号
     *
     * @return List<ImageTypeSupportOsVersionDTO>
     */
    List<ImageTypeSupportOsVersionDTO> findAllImageTypeSupportOsVersionConfig();

    /**
     * 镜像是否支持版本
     *
     * @param imageTypeSupportOsVersionDTOList 镜像支持操作版本列表
     * @param imageTypeSupportOsVersionDTO 镜像
     * @return boolean
     */
    boolean hasImageSupportOsVersion(List<ImageTypeSupportOsVersionDTO> imageTypeSupportOsVersionDTOList,
            ImageTypeSupportOsVersionDTO imageTypeSupportOsVersionDTO);

    /**
     * 查询管理员拥有的镜像数据列表
     *
     * @param adminId 管理员id
     * @param requestBuilder 请求
     * @return PageQueryResponse<CbbImageTemplateDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<CbbImageTemplateDTO> queryByAdmin(UUID adminId, PageQueryBuilderFactory.RequestBuilder requestBuilder) throws BusinessException;


    /**
     * 镜像是否锁定
     * 
     * @param imageId 镜像ID
     * @return true 锁定
     */
    Boolean isLockImage(UUID imageId);

    /**
     * 查询终端列表
     *
     * @param request 请求
     * @return RemoteTerminalEditImageTerminalRsDTO
     */
    DefaultPageResponse<ViewTerminalWithImageDTO> queryTerminalList(PageSearchRequest request);

    /**
     * 查询终端镜像下载信息
     * 
     * @param imageId 镜像
     * @param terminalId 终端id
     * @return ViewTerminalWithImageDTO
     */
    ViewTerminalWithImageDTO getTerminalWithImage(String imageId, String terminalId);

}
