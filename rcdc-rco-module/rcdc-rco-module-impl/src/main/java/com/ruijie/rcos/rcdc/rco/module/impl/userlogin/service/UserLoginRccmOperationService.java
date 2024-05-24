package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedUserDesktopResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ChangeUserPasswordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.RccmUnifiedChangePwdResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.RccmUnifiedLoginResultDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;

/**
 *
 * Description: 多集群Server配置Service
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public interface UserLoginRccmOperationService {

    /**
     * 是否向rccm请求登录
     *
     * @param terminalId terminalId
     * @return true已开启； false未开启
     */
    boolean isUnifiedLoginOn(String terminalId);

    /**
     * 向r-center请求遍历校验登录信息
     *
     * @param request request
     * @param terminalId terminalId
     * @param shineLoginDTO shineLoginDTO
     * @return RccmUnifiedLoginResultDTO
     * @throws BusinessException 异常
     */
    RccmUnifiedLoginResultDTO requestLoginValidateInRccm(CbbDispatcherRequest request, String terminalId, ShineLoginDTO shineLoginDTO)
            throws BusinessException;

    /**
     * 向r-center查询用户绑定的云桌面列表信息
     *
     * @param terminalId terminalId
     * @param canFoundTerminal 终端表是否存在对应终端ID数据(网页版客户端不存在终端类型上报所以终端表不存在对应数据)
     * @return List<RccmUserDesktopResultDTO>
     * @throws Exception Exception
     */
    List<UnifiedUserDesktopResultDTO> requestUserVDIDesktopInRccm(String terminalId, Boolean canFoundTerminal) throws Exception;

    /**
     * 向r-center请求修改密码信息
     *
     * @param request request
     * @param terminalId terminalId
     * @param changeUserPasswordDTO changeUserPasswordDTO
     * @return RccmUnifiedChangePwdResultDTO
     * @throws BusinessException 异常
     */
    RccmUnifiedChangePwdResultDTO requestChangePwdInRccm(CbbDispatcherRequest request, String terminalId,
                                                                 ChangeUserPasswordDTO changeUserPasswordDTO) throws BusinessException;
}
