package com.ruijie.rcos.rcdc.rco.module.impl.upgrade.api;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.UpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.PromptVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.upgrade.connector.rest.RccpTaiyiClient;
import com.ruijie.rcos.rcdc.rco.module.impl.upgrade.dto.CancelPromptVersionDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/13 17:17
 *
 * @author ketb
 */
public class UpgradeAPIImpl implements UpgradeAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeAPIImpl.class);

    @Autowired
    private RccpTaiyiClient rccpTaiyiClient;

    @Override
    public PromptVersionDTO queryVersionList() throws BusinessException {

        PromptVersionDTO promptVersionDTO = rccpTaiyiClient.queryVersionsList();
        LOGGER.debug("获取到版本列表信息：{}", JSONObject.toJSONString(promptVersionDTO));
        return promptVersionDTO;
    }

    @Override
    public void cancelPromptVersion(String pkgName) throws BusinessException {
        Assert.notNull(pkgName, "pkgName can not be null");

        CancelPromptVersionDTO cancelPromptVersionDTO = new CancelPromptVersionDTO();
        cancelPromptVersionDTO.setPkgName(pkgName);
        rccpTaiyiClient.cancelPromptVersion(cancelPromptVersionDTO);
    }
}
