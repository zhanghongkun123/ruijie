package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.EditCommonConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ConfigKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.EditCommonConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.ServerForecastCache;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.CommonConfigDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.CommonConfigEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import java.util.UUID;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 大屏配置Tx单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 13:12
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class CommonConfigTxImplTest {

    @Tested
    private CommonConfigTxImpl commonConfigTx;

    @Injectable
    private CommonConfigDAO commonConfigDAO;

    @Injectable
    private ServerForecastCache serverForecastCache;

    /**
     * 配置修改，正常流程
     * @throws BusinessException 异常
     */
    @Test
    public void editTest() throws BusinessException {
        UUID id = UUID.randomUUID();
        EditCommonConfigRequest request = new EditCommonConfigRequest();
        EditCommonConfigDTO dto = new EditCommonConfigDTO();
        dto.setId(id);
        dto.setConfigValue("90");
        request.setConfigArr(new EditCommonConfigDTO[]{dto});

        CommonConfigEntity entity = new CommonConfigEntity();
        entity.setId(id);
        entity.setConfigValue("80");
        entity.setConfigKey(ConfigKeyEnum.BIGSCREEN_SERVER_FORECAST_LIMIT_CPU);

        new Expectations() {
            {
                commonConfigDAO.getOne((UUID) any);
                result = entity;
            }
        };

        commonConfigTx.edit(request.getConfigArr());
        
        new Verifications() {
            {
                commonConfigDAO.getOne((UUID) any);
                times = 1;
            }
        };
    }

    /**
     * 跳过未修改的配置项
     * @throws BusinessException 异常
     */
    @Test
    public void editSkipUnchangedConfigTest() throws BusinessException {
        UUID id = UUID.randomUUID();
        EditCommonConfigRequest request = new EditCommonConfigRequest();
        EditCommonConfigDTO dto = new EditCommonConfigDTO();
        dto.setId(id);
        dto.setConfigValue("90");
        request.setConfigArr(new EditCommonConfigDTO[]{dto});

        CommonConfigEntity entity = new CommonConfigEntity();
        entity.setId(id);
        entity.setConfigValue("90");

        new Expectations() {
            {
                commonConfigDAO.getOne((UUID) any);
                result = entity;
                commonConfigDAO.save((CommonConfigEntity) any);
            }
        };

        commonConfigTx.edit(request.getConfigArr());

        new Verifications() {
            {
                commonConfigDAO.getOne((UUID) any);
                times = 1;
                commonConfigDAO.save((CommonConfigEntity) any);
                times = 1;
            }
        };
    }

}
