package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbClusterBasicInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 大屏监控平台信息API实现单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:05
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class PlatformAPIImplTest {

    @Tested
    private PlatformAPIImpl platformAPI;

    @Injectable
    private CbbClusterServerMgmtAPI clusterMgmtAPI;

    /**
     * 获取系统时间信息
     * @throws BusinessException 异常
     */
    @Test
    public void testGetSystemTime() throws BusinessException {
        CbbClusterBasicInfoDTO dto = new CbbClusterBasicInfoDTO();
        dto.setRunTime(10L);
        new Expectations() {
            {
                clusterMgmtAPI.getClusterBasicInfo();
                result = dto;
            }
        };
        SystemTimeResponse systemTimeResponse = platformAPI.getSystemTime();
        Assert.assertEquals(dto.getRunTime(), systemTimeResponse.getSystemWorkTime());
        new Verifications() {
            {
                clusterMgmtAPI.getClusterBasicInfo();
                times = 1;
            }
        };
    }

}
