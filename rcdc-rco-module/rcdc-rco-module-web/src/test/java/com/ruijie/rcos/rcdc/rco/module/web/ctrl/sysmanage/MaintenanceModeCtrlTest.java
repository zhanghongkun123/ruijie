package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.api.MaintenanceModeMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto.BaseMaintenanceStateDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/23
 *
 * @author XiaoJiaXin
 */
@RunWith(SkyEngineRunner.class)
public class MaintenanceModeCtrlTest {

    @Tested
    private MaintenanceModeCtrl ctrl;

    @Injectable
    private MaintenanceModeMgmtAPI maintenanceModeMgmtAPI;

    /**
     * test getinfo
     */
    @Test
    public void testGetInfo() {

        new Expectations() {
            {
                maintenanceModeMgmtAPI.getMaintenanceMode();
                result = SystemMaintenanceState.UNDER_MAINTENANCE;
            }
        };
        DefaultWebResponse info = ctrl.getInfo(new DefaultWebRequest());

        BaseMaintenanceStateDTO dto = JSON.parseObject(JSON.toJSONString(info.getContent()),
                BaseMaintenanceStateDTO.class);
        Assert.assertEquals(dto.getState(), SystemMaintenanceState.UNDER_MAINTENANCE);
    }

    /**
     * test start
     * @throws BusinessException ex
     */
    @Test
    public void testStart() throws BusinessException {
        DefaultWebResponse info = ctrl.start(new DefaultWebRequest());
        Assert.assertEquals(info.getStatus(), WebResponse.Status.SUCCESS);
        
        new Verifications() {
            {
                maintenanceModeMgmtAPI.startMaintenanceMode((BaseUpgradeDTO) any);
                times = 1;
            }
        };
    }

    /**
     * test stop
     * @throws BusinessException ex
     */
    @Test
    public void testStop() throws BusinessException {
        DefaultWebResponse info = ctrl.stop(new DefaultWebRequest());
        Assert.assertEquals(info.getStatus(), WebResponse.Status.SUCCESS);
        
        new Verifications() {
            {
                maintenanceModeMgmtAPI.quitMaintenanceMode((BaseUpgradeDTO) any);
                times = 1;
            }
        };
    }
}
