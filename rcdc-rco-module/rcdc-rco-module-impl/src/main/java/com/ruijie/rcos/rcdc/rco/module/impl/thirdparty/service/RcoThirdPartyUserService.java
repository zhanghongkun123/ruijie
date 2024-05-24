package com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.service;

import com.ruijie.rcos.gss.sdk.iac.module.def.callback.SyncThirdPartyUserCallBack;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 第三方用户相关操作类
 *
 * Description: 第三方用户相关操作类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/15 15:58
 *
 * @author zjy
 */
public interface RcoThirdPartyUserService {

    /**
     * 同步第三方用户
     *
     * @param syncUserCallBack 回调信息
     * @return 同步结果
     * @throws BusinessException 业务异常
     */
    BatchTaskSubmitResult syncThirdPartyUser(SyncThirdPartyUserCallBack syncUserCallBack) throws BusinessException;

    /**
     * 创建或更新第三方同步任务
     *
     * @param cbbThirdPartyAuthPlatformConfigDTO 配置
     * @throws BusinessException 业务异常
     * @author zjy
     */
    void createOrUpdateSyncSchedule(BaseThirdPartyAuthPlatformConfigDTO cbbThirdPartyAuthPlatformConfigDTO) throws BusinessException;
}
