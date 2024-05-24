package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbClipBoardSupportTypeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUsbBandwidthDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbClipboardMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DeskStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ViewDeskStrategyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月1日
 *
 * @author wjp
 */
public interface DeskStrategyAPI extends PageQueryAPI<ViewDeskStrategyDTO> {

    /**
     * 分页查询云桌面策略
     *
     * @param pageQueryRequest 分页查询参数
     * @return 分页查询结果PageQueryResponse<DeskStrategyDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DeskStrategyDTO> pageDeskStrategyQuery(PageQueryRequest pageQueryRequest) throws BusinessException;

    /**
     * 获取云桌面策略名称
     *
     * @param strategyId 云桌面策略Id
     * @return 响应
     */
    String obtainStrategyName(UUID strategyId);

    /**
     * 根据用户配置策略id查询云桌面策略是否可用
     *
     * @param deskStrategyId        云桌面策略ID
     * @return 云桌面策略是否可用信息
     * @throws BusinessException 业务异常
     */
    String getDeskStrategyUsedMessageByUserProfileStrategyId(UUID deskStrategyId) throws BusinessException;

    /**
     * 查询云桌面策略的Pattern类型
     *
     * @param strategyId 云桌面策略ID
     * @return Pattern类型
     */
    CbbCloudDeskPattern findPatternById(UUID strategyId);

    /**
     * 查询云桌面策略的session类型
     *
     * @param strategyId 云桌面策略ID
     * @return 会话类型
     */
    CbbDesktopSessionType findSessionTypeById(UUID strategyId);

    /**
     * 处理云桌面策略水印功能
     *
     * @param strategyId         云桌面策略ID
     * @param oldWatermarkConfig 修改前云桌面策略水印策略
     */
    void doWatermarkAfterUpdateStrategy(UUID strategyId, @Nullable CbbWatermarkConfigDTO oldWatermarkConfig);

    /**
     * 指定桌面发送云桌面策略水印策略，云桌面策略未开启水印默认就发全局策略的水印配置
     *
     * @param cloudDesktopDetailDTO 桌面详情
     */
    void sendDesktopStrategyWatermark(CloudDesktopDetailDTO cloudDesktopDetailDTO);

    /**
     * 获取指定云桌面策略水印策略
     *
     * @param strategyId 云桌面策略ID
     * @return CbbWatermarkConfigDTO 云桌面策略水印策略
     */
    CbbWatermarkConfigDTO getStrategyWatermarkConfig(UUID strategyId);

    /**
     * 创建云桌面策略校验
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    void createDeskStrategyValidate(DeskStrategyCheckDTO request) throws BusinessException;

    /**
     * 修改云桌面策略校验
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    void updateDeskStrategyValidate(DeskStrategyCheckDTO request) throws BusinessException;

    /**
     * 校验云桌面策略是否可修改和删除
     * @param deskStrategyId 云桌面策略ID
     * @param unifiedManageDataId 统一集群管理ID
     * @throws BusinessException 业务异常
     */
    void checkDeskStrategyCanChange(UUID deskStrategyId, @Nullable UUID unifiedManageDataId) throws BusinessException;

    /**
     * 删除云桌面策略
     *
     * @param deskStrategyId 云桌面策略ID
     * @throws BusinessException 业务异常
     */
    void deleteDeskStrategy(UUID deskStrategyId) throws BusinessException;

    /**
     * 安全审计策略变更通知在线桌面
     *
     * @param id                    云桌面策略ID
     */
    void doAuditUpdateStrategy(UUID id);

    /**
     * 剪贴板数据格式校验
     *
     * @param clipBoardSupportTypeArr clipBoardSupportTypeArr
     * @param allowArrEmpty           临时权限剪切板允许clipBoardSupportTypeArr空
     * @return 根据clipBoardSupportTypeArr给clipBoardMode赋值
     * @throws BusinessException 业务异常
     */
    CbbClipboardMode clipBoardArrValidate(CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr, Boolean allowArrEmpty) throws BusinessException;

    /**
     * 协议配置校验
     *
     * @param type          连接类型
     * @param agreementInfo 协议配置
     * @throws BusinessException 业务异常
     */
    void agreementConfigValidate(CbbEstProtocolType type, @Nullable AgreementDTO agreementInfo) throws BusinessException;

    /**
     * 带宽控制校验
     * @param enableUsbBandwidth 是否开启带宽控制
     * @param usbBandwidthInfo 带宽配置
     * @throws BusinessException 业务异常
     */
    void usbBandWidthValidation(@Nullable Boolean enableUsbBandwidth,@Nullable CbbUsbBandwidthDTO usbBandwidthInfo) throws BusinessException;
}
