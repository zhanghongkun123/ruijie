package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalConfigImportRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: TerminalConfigAPI
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月19日
 *
 * @author clone
 */
public interface TerminalConfigAPI {


    /**
     * 上传并校验配置内容
     *
     * @param request CbbTerminalConfigImportRequest
     * @throws BusinessException ex
     */
    void checkTerminalConfigUpload(CbbTerminalConfigImportRequest request) throws BusinessException;

    /**
     * 导入终端配置
     *
     * @throws BusinessException ex
     */
    void importTerminalConfig() throws BusinessException;

}
