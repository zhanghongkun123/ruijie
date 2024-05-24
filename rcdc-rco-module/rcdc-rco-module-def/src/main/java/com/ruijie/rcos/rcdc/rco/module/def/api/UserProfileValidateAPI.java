package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 路径判断处理的API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/11
 *
 * @author zwf
 */
public interface UserProfileValidateAPI {

    /**
     * 根据路径ID计算其子路径总数是否超标
     *
     * @param idArr 路径ID组
     * @throws BusinessException 异常处理
     */
    void validatePathNum(UUID[] idArr) throws BusinessException;

    /**
     * 判断创建路径时子路径是否超标
     *
     * @param userProfileChildPathArr 子路径类型组
     * @param userProfilePathId       配置ID
     * @throws BusinessException 异常处理
     */
    void validateUserProfileChildPath(UserProfileChildPathDTO[] userProfileChildPathArr,
                                      @Nullable UUID userProfilePathId) throws BusinessException;

    /**
     * 校验必须为个人盘
     *
     * @param userProfileStrategyId UPM配置策略ID
     * @throws BusinessException 异常处理
     */
    void validateUserProfileStrategyMustStoragePersonal(UUID userProfileStrategyId) throws BusinessException;

    /**
     * 校验必须为本地盘
     *
     * @param userProfileStrategyId UPM配置策略ID
     * @throws BusinessException 异常处理
     */
    void validateUserProfileStrategyMustStorageLocal(UUID userProfileStrategyId) throws BusinessException;

    /**
     * 校验策略存储位置必须为文件服务器
     *
     * @param userProfileStrategyId UPM配置策略ID
     * @throws BusinessException 异常处理
     */
    void validateStorageMustFileServer(UUID userProfileStrategyId) throws BusinessException;

    /**
     * 编辑个性化配置时是否于镜像模板冲突
     *
     * @param imageId 镜像ID
     * @throws BusinessException 异常处理
     */
    void validateUserProfileStrategyImageRefuse(UUID imageId) throws BusinessException;

    /**
     * 判断配置库是否存在
     *
     * @param groupId 配置库ID
     * @throws BusinessException 异常处理
     */
    void validateGroupIdExist(UUID groupId) throws BusinessException;

    /**
     * 判断桌面是否为非绑定用户的池桌面
     *
     * @param desktopId       云桌面ID
     * @param desktopPoolType 云桌面的池状态
     * @return 判断结果
     */
    boolean isPoolDesktopWithoutUser(UUID desktopId, DesktopPoolType desktopPoolType);

    /**
     * 获取选定配置中，仍然存在的配置
     *
     * @param userProfilePathIdArr 配置ID列表
     * @return 仍然存在的配置ID列表
     */
    List<UUID> validateUserProfilePathExist(UUID[] userProfilePathIdArr);
}
