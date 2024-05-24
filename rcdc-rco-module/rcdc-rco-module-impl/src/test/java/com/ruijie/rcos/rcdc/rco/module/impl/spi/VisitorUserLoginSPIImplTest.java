package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response.Status;

import mockit.Tested;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-03-04 16:04
 */
@RunWith(SkyEngineRunner.class)
public class VisitorUserLoginSPIImplTest {

    @Tested
    private VisitorUserLoginSPIImpl visitorUserLoginSPI;

    @Test
    public void testNotify() {
        UserLoginNoticeDTO request = new UserLoginNoticeDTO();
        DtoResponse dtoResponse = visitorUserLoginSPI.notify(request);
        assertEquals(dtoResponse.getStatus(), Status.SUCCESS);
    }

}
