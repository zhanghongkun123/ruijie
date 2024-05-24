package com.ruijie.rcos.rcdc.rco.module.impl.sms.auth.service;

import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;

/**
 * Description: SmsCertificationService
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/15
 *
 * @author TD
 */
public interface SmsCertificationService {

    /**
     * 获取短信认证策略
     * @return 短信认证策略信息
     */
    SmsCertificationDTO getSmsCertificationStrategy();

    /**
     * 编辑短信认证策略
     * @param smsCertificationDTO 短信认证策略DTO
     */
    void editSmsCertificationStrategy(SmsCertificationDTO smsCertificationDTO);

    /**
     * 获取密码找回策略
     * @return 密码找回策略信息
     */
    SmsPwdRecoverDTO getSmsPwdRecoverStrategy();

    /**
     * 编辑密码找回策略
     * @param pwdRecoverDTO 密码找回策略DTO
     */
    void editSmsPwdRecoverStrategy(SmsPwdRecoverDTO pwdRecoverDTO);
}
