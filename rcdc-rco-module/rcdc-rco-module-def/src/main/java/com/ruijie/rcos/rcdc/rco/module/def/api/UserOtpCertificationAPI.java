package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.otp.IacUserOtpCertificationConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.OtpParamRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.RcoViewUserOtpCertificationDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;


/**
 *
 * Description: 用户动态口令管理API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
public interface UserOtpCertificationAPI {

    /**
     * 分页获取用户动态口令信息
     *
     * @param request 请求参数
     * @return  用户动态口令信息分页列表
     */
    DefaultPageResponse<RcoViewUserOtpCertificationDTO> pageQuery(PageSearchRequest request);

    /**
     * 重置用户动态口令密钥（即解绑）
     *
     * @param userId     用户ID
     * @return 是否成功
     * @throws BusinessException 业务异常
     */
    boolean resetById(UUID userId) throws BusinessException;

    /**
     * 用户动态口令密钥绑定成功（即更新绑定）
     *
     * @param userId     用户ID
     * @return 是否成功
     * @throws BusinessException 业务异常
     */
    boolean bindById(UUID userId) throws BusinessException;

    /**
     * 用户动态口令校验
     *
     * @param userId     用户ID
     * @param code     动态码
     * @return 是否成功
     * @throws BusinessException 业务异常
     */
    boolean checkUserOtpCode(UUID userId, String code) throws BusinessException;

    /**
     * 获取用户动态口令配置
     *
     * @param userId     用户ID
     * @return 用户动态口令配置
     * @throws BusinessException 业务异常
     */
    IacUserOtpCertificationConfigDTO getUserOtpCertificationConfigById(UUID userId) throws BusinessException;


    /**
     * 获取Otp的额外参数
     * @param otpParamRequest 请求
     * @return 额外参数信息
     */
    String obtainOtpAttachmentParams(OtpParamRequest otpParamRequest);

}
