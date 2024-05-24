package com.ruijie.rcos.rcdc.rco.module.impl.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.callback.SyncThirdPartyUserCallBack;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyAuthPlatformConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoThirdPartyUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ScheduleAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.thirdparty.service.RcoThirdPartyUserService;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 第三方用户相关操作APIImpl
 *
 * Description: 第三方用户相关操作APIImpl
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/15 16:03
 *
 * @author zjy
 */
public class RcoThirdPartyUserMgmtAPIImpl implements RcoThirdPartyUserMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoThirdPartyUserMgmtAPIImpl.class);

    @Autowired
    private RcoThirdPartyUserService rcoThirdPartyUserService;

    @Autowired
    private ScheduleAPI scheduleAPI;

    @Override
    public BatchTaskSubmitResult syncThirdPartyUser(SyncThirdPartyUserCallBack syncUserCallBack) throws BusinessException {
        Assert.notNull(syncUserCallBack, "syncAdUserCallBack not be null");

        return rcoThirdPartyUserService.syncThirdPartyUser(syncUserCallBack);
    }

    @Override
    public void createOrUpdateSyncSchedule(BaseThirdPartyAuthPlatformConfigDTO cbbThirdPartyAuthPlatformConfigDTO) throws BusinessException {

        Assert.notNull(cbbThirdPartyAuthPlatformConfigDTO, "cbbThirdPartyAuthPlatformConfigDTO not be null");

        rcoThirdPartyUserService.createOrUpdateSyncSchedule(cbbThirdPartyAuthPlatformConfigDTO);
    }
}
