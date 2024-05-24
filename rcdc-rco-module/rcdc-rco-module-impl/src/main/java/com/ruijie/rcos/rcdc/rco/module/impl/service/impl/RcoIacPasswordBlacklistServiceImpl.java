package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacPasswordBlacklistMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoIacPasswordBlacklistService;
import com.ruijie.rcos.sk.base.crypto.AesUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月13日
 *
 * @author zdc
 */
@Service
public class RcoIacPasswordBlacklistServiceImpl implements RcoIacPasswordBlacklistService {

    @Autowired
    private IacPasswordBlacklistMgmtAPI iacPasswordBlacklistMgmtAPI;

    @Override
    public Boolean isPasswordBlackList(String pwd) {
        Assert.hasText(pwd, "pwd must not be null");
        String encryptPwd = AesUtil.encrypt(pwd, RedLineUtil.getRealAdminRedLine());
        return iacPasswordBlacklistMgmtAPI.isPasswordBlackList(encryptPwd, SubSystem.CDC);
    }
}
