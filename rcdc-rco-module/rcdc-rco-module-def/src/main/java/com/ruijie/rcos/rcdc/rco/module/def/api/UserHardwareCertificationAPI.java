package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacUserHardwareCertificationStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacValidateUserHardwareResultEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.RcoViewUserHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserHardwareCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserMacBindingDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;


/**
 *
 * Description: 用户硬件特征码管理API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
public interface UserHardwareCertificationAPI {

    /**
     * 分页获取用户硬件特征码信息
     *
     * @param request 请求参数
     * @return  用户硬件特征码信息分页列表
     */
    DefaultPageResponse<RcoViewUserHardwareCertificationDTO> pageQuery(PageSearchRequest request);

    /**
     * 根据记录ID删除用户硬件特征码记录
     *
     * @param id 记录id
     * @throws BusinessException 业务异常
     */
    void deleteById(UUID id) throws BusinessException;

    /**
     * 修改用户硬件特征码记录状态
     *
     * @param id     记录ID
     * @param stateEnum  状态枚举
     * @throws BusinessException 业务异常
     */
    void checkNumAndUpdateState(UUID id, IacUserHardwareCertificationStateEnum stateEnum) throws BusinessException;

    /**
     * 根据ID获取用户硬件特征码记录
     *
     * @param id 记录ID
     * @return RcoViewUserHardwareCertificationDTO
     * @throws BusinessException 业务异常
     */
    RcoViewUserHardwareCertificationDTO getById(UUID id) throws BusinessException;

    /**
     * 校验用户硬件特征码是否通过
     *
     * @param userHardwareCheckDTO userHardwareCheckDTO
     * @return ValidateUserHardwareResultEnum
     * @throws BusinessException 业务异常
     */
    IacValidateUserHardwareResultEnum validateCertification(UserHardwareCheckDTO userHardwareCheckDTO) throws BusinessException;

    /**
     * 根据用户ID删除审核表记录
     * 
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    void delRecordByUserId(UUID userId) throws BusinessException;

    /**
     * 创建用户和mac地址绑定关系
     *
     * @param userMacBindingDTO 请求参数
     * @throws BusinessException 业务异常
     */
    void createUserMacBinding(UserMacBindingDTO userMacBindingDTO) throws BusinessException;

    /**
     * 清空和终端的绑定关系
     *
     * @param terminalId 终端ID
     * @throws BusinessException 业务异常
     */
    void clearTerminalBinding(String terminalId) throws BusinessException;
}
