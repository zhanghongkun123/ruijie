package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.EditCommonConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.BigScreenDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CommonConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CommonConfigEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.CommonConfigTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response.Status;
import java.util.List;
import java.util.UUID;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 大屏配置API单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 12:37
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class CommonConfigMgmtAPIImplTest {

    @Tested
    private CommonConfigMgmtAPIImpl commonConfigMgmtAPI;

    @Injectable
    private CommonConfigDAO commonConfigDAO;

    @Injectable
    private CommonConfigTx commonConfigTx;

    /**
     * 查询配置，正常流程
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void bigScreenDetailTest() throws BusinessException {
        CommonConfigEntity entity = new CommonConfigEntity();
        entity.setConfigKey(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_CPU);
        entity.setConfigValue("90");
        List<CommonConfigEntity> entityList = Lists.newArrayList();
        entityList.add(entity);

        new Expectations() {
            {
                commonConfigDAO.findAll();
                result = entityList;
            }
        };

        BigScreenDetailResponse response = commonConfigMgmtAPI.getConfigParam(new DefaultRequest());

        Assert.assertNotNull(response.getConfigArr());
    }

    /**
     * 未查询到大屏配置
     */
    @Test
    public void bigScreenDetailConfigNotFoundTest() {
        new Expectations() {
            {
                commonConfigDAO.findAll();
                result = Lists.newArrayList();
            }
        };

        String exMsg = "";
        try {
            commonConfigMgmtAPI.getConfigParam(new DefaultRequest());
        } catch (BusinessException ex) {
            exMsg = ex.getMessage();
        }

        Assert.assertEquals(exMsg, BusinessKey.RCDC_RCO_BIGSCREEN_COMMON_CONFIG_NOT_FOUND);
    }

    /**
     * 测试编辑大屏配置
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void editTest() throws BusinessException {
        EditCommonConfigRequest request = new EditCommonConfigRequest();
        EditCommonConfigDTO dto = new EditCommonConfigDTO();
        dto.setId(UUID.randomUUID());
        dto.setConfigValue("90");
        request.setConfigArr(new EditCommonConfigDTO[]{dto});

        DefaultResponse response = commonConfigMgmtAPI.editConfigParam(request);

        Assert.assertEquals(response.getStatus(), Status.SUCCESS);
    }
}
