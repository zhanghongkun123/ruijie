package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.special.dto.SpecialConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 * Description: 用户配置特殊配置API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/8
 *
 * @author zwf
 */
public interface UserProfilePathSpecialConfigAPI {
    /**
     * 导入用户特殊配置
     *
     * @param file 文件
     * @return 用户特殊配置
     * @throws BusinessException 异常处理
     */
    String importUserProfilePathSpecialConfig(ChunkUploadFile file) throws BusinessException;

    /**
     * 获取用户特殊配置
     *
     * @return 用户特殊配置
     */
    SpecialConfigDTO getUserProfilePathSpecialConfig();
}
