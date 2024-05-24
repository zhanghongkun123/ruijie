package com.ruijie.rcos.rcdc.rco.module.def.api;


import com.ruijie.rcos.gss.sdk.iac.module.def.callback.SyncThirdPartyUserCallBack;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * 第三方用户相关操作API
 *
 * Description: 第三方用户相关操作API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/15 15:58
 *
 * @author zjy
 */
public interface RcoThirdPartyUserMgmtAPI {


    /**
     * 同步第三方用户
     *
     * @param syncUserCallBack 同步回调
     * @return 同步结果
     * @throws BusinessException 业务异常
     */
    BatchTaskSubmitResult syncThirdPartyUser(SyncThirdPartyUserCallBack syncUserCallBack) throws BusinessException;

    /**
     * 创建或更新第三方同步任务
     *
     * @param cbbThirdPartyAuthPlatformConfigDTO 配置信息
     * @throws BusinessException 业务异常
     * @author zjy
     */
    void createOrUpdateSyncSchedule(BaseThirdPartyAuthPlatformConfigDTO cbbThirdPartyAuthPlatformConfigDTO) throws BusinessException;
}
