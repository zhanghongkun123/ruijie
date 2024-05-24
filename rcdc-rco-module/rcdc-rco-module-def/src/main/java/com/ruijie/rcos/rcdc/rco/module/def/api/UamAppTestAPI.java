package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月29日
 *
 * @author zhk
 */
public interface UamAppTestAPI {

    /**
     * 创建应用测试
     * 
     * @param dto 测试信息
     * @return 任务id
     * @throws BusinessException 业务异常
     */
    UUID createAppTest(UamAppTestDTO dto) throws BusinessException;

    /**
     *
     * @param testId 测试id
     * @param deskIdList 桌面id
     * @throws BusinessException 业务异常
     */
    void addAppTestDesk(UUID testId, List<UUID> deskIdList) throws BusinessException;

    /**
     * 分页查询软件测试桌面列表
     *
     * @param pageRequest 入参
     * @return 测试桌面列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<AppTestDesktopInfoDTO> pageQueryAppTestDesktop(PageQueryRequest pageRequest) throws BusinessException;

    /**
     * 分页查询软件测试应用列表
     *
     * @param pageRequest 入参
     * @return 测试应用列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<AppTestApplicationInfoDTO> pageQueryAppTestApp(PageQueryRequest pageRequest) throws BusinessException;

    /**
     * 分页查询软件测试桌面应用列表
     *
     * @param pageRequest 入参
     * @return 测试应用列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<AppTestDeskAppInfoDTO> pageQueryAppTestDeskApp(PageQueryRequest pageRequest) throws BusinessException;

    /**
     * 根据桌面id和测试id查询信息
     * 
     * @param deskId 桌面id
     * @param desktopTestStateEnumList 测试状态
     * @return 返回值
     */
    List<AppTestDesktopInfoDTO> findByDeskIdAndTestStateIn(UUID deskId, List<DesktopTestStateEnum> desktopTestStateEnumList);

    /**
     * 根据桌面id和测试id查询信息
     * 
     * @param testId 测试id
     * @param deskId 桌面id
     * @return 返回值
     */
    AppTestDesktopInfoDTO findByAppTestIdAndDeskId(UUID testId, UUID deskId);

    /**
     * 根据状态获取桌面测试信息
     * 
     * @param desktopTestStateEnumList 测试状态
     * @return 返回值
     */
    List<AppTestDesktopInfoDTO> findByTestStateIn(List<DesktopTestStateEnum> desktopTestStateEnumList);

    /**
     * 根据交付组名称和云桌面名称查找
     *
     * @param searchDeliveryTestDTO 查询参数
     * @param pageable 分页参数
     * @return 返回值
     */
    DefaultPageResponse<RcoUamAppTestDTO> pageUamDeliveryTest(SearchDeliveryTestDTO searchDeliveryTestDTO, Pageable pageable);

    /**
     * 批量查询桌面测试信息
     *
     * @param deskIdList 桌面id
     * @return 实体类
     */
    boolean existTestingDesk(List<UUID> deskIdList);

    /**
     * 基于桌面ID删除测试任务
     * @param deskId 桌面ID
     * @throws BusinessException 业务异常
     */
    void deleteCompletedTestDeskWhenStrategyModify(UUID deskId) throws BusinessException;


    /**
     * 基于桌面ID集合和桌面测试状态查询测试列表
     * @param deskIdList deskIdList
     * @param desktopTestStateEnumList desktopTestStateEnumList
     * @return 查询测试列表
     */
    List<AppTestDesktopInfoDTO> findAllByDeskIdInAndTestStateIn(List<UUID> deskIdList, List<DesktopTestStateEnum> desktopTestStateEnumList);

    /**
     * 检查桌面是否测试中
     *
     * @param deskId 桌面id
     * @throws BusinessException 业务异常
     */
    void checkTestingDesk(UUID deskId) throws BusinessException;
}
