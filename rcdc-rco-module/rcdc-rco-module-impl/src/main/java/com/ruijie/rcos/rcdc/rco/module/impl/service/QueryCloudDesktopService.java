package com.ruijie.rcos.rcdc.rco.module.impl.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RcaHostDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewRcaHostDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description: 云桌面信息查询接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 *
 * @author chenzj
 */
public interface QueryCloudDesktopService {

    /**
     ** 校验并获取云桌面对象
     *
     * @param cbbDesktopId 云桌面ID
     * @return 产品业务组件维护的云桌面对象.
     * @throws BusinessException 云桌面不存在异常.
     */
    UserDesktopEntity checkAndFindById(UUID cbbDesktopId) throws BusinessException;

    /**
     ** 查询云桌面明细信息
     *
     * @param cbbDesktopId entity id
     * @return dto
     * @throws BusinessException 云桌面不存在异常.
     */
    CloudDesktopDetailDTO queryDeskDetail(UUID cbbDesktopId) throws BusinessException;

    /**
     * *转换页面数据，并从CBB获取额外数据
     *
     * @param page spring page
     * @return page
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<CloudDesktopDTO> convertPageInfoAndQuery(Page<ViewUserDesktopEntity> page) throws BusinessException;

    /**
     * *rca桌面列表转换页面数据，并从CBB获取额外数据
     *
     * @param page spring page
     * @return page
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<RcaHostDesktopDTO> convertRcaHostPageInfoList(Page<ViewRcaHostDesktopEntity> page) throws BusinessException;

    /**
     * 转换云桌面信息
     * @param viewList ViewUserDesktopEntity
     * @return CloudDesktopDTO
     * @throws BusinessException BusinessException
     */
    List<CloudDesktopDTO> convertCloudDesktop(List<ViewUserDesktopEntity> viewList) throws BusinessException;

    /**
     * * 查询云桌面状态
     *
     * @param cbbDesktopId entity id
     * @return state
     * @throws BusinessException 业务异常
     */
    CbbCloudDeskState queryState(UUID cbbDesktopId) throws BusinessException;

    /**
     * *根据云桌面策略id获取策略对象
     *
     * @param id 云桌面策略id
     * @return 策略
     * @throws BusinessException 业务异常
     */
    CbbDeskStrategyVDIDTO checkAndGetVDIStrategyById(UUID id) throws BusinessException;

    /**
     * *根据云桌面策略id获取idv策略对象
     *
     * @param id 云桌面策略id
     * @return 策略
     * @throws BusinessException 业务异常
     */
    CbbDeskStrategyIDVDTO checkAndGetIDVStrategyById(UUID id) throws BusinessException;

    /**
     * *根据云桌面镜像id、镜像类型获取镜像对象
     *
     * @param id 云桌面镜像id
     * @param imageType 镜像类型
     * @return 镜像对象
     * @throws BusinessException 业务异常
     */
    CbbImageTemplateDetailDTO checkAndGetImageByIdAndImageType(UUID id, CbbImageType imageType) throws BusinessException;

    /**
     * *根据云桌面网络id获取网络策略
     *
     * @param id 云桌面网络id
     * @return 网络配置
     * @throws BusinessException 业务异常
     */
    CbbDeskNetworkDetailDTO checkAndGetNetworkById(UUID id) throws BusinessException;

    /**
     * 根据云桌面id查询云桌面信息
     *
     * @param cbbDesktopId 桌面id
     * @return 结果
     * @throws BusinessException 业务异常
     */
    CbbDeskDTO getDeskInfo(UUID cbbDesktopId) throws BusinessException;


    /**
     * 判断视图中是否存在对应云桌面信息
     *
     * @param cbbDesktopId UUID
     * @return ViewUserDesktopEntity 云桌面视图对象
     * @throws BusinessException 不存在异常
     */
    ViewUserDesktopEntity checkDesktopExistInDeskViewById(UUID cbbDesktopId) throws BusinessException;

    /**
     * *rca桌面列表转换页面数据，并从CBB获取额外数据
     *
     * @param desktopEntityList 桌面详情列表
     * @return list
     * @throws BusinessException 业务异常
     */
    List<RcaHostDesktopDTO> convertRcaHostPageInfoList(List<ViewRcaHostDesktopEntity> desktopEntityList) throws BusinessException;

    /**
     * 云桌面列表补充RCA参数
     *
     * @param cloudDesktopList 云桌面列表
     * @param rcaHostViewList 云应用列表
     * @return list
     * @throws BusinessException 业务异常
     */
    List<RcaHostDesktopDTO> convertToRcaHostDesktopList(List<CloudDesktopDTO> cloudDesktopList,
                                                        List<ViewRcaHostDesktopEntity> rcaHostViewList);
}
