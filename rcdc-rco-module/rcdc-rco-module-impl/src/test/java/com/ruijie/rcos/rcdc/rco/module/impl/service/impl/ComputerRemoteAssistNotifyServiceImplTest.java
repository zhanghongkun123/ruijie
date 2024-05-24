package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;


import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.RemoteAssistReportStateNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ComputerBusinessDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ComputerRemoteAssistMgmtService;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-03-27 17:50
 */
@RunWith(JMockit.class)
public class ComputerRemoteAssistNotifyServiceImplTest {

    @Tested
    private ComputerRemoteAssistNotifyServiceImpl computerRemoteAssistNotifyService;

    @Injectable
    private ComputerRemoteAssistMgmtService remoteAssistMgmtService;

    @Injectable
    private ComputerBusinessDAO computerBusinessDAO;

    @Before
    public void before() {

        new MockUp<LocaleI18nResolver>() {

            /**
             *
             * @param key key
             * @param args args
             * @return key
             */
            @Mock
            public String resolve(String key, String... args) {
                return key;
            }

        };
    }

    @Test
    public void testRemoteAssistReportState() throws Exception {
        RemoteAssistReportStateNotifyRequest request = new RemoteAssistReportStateNotifyRequest();
        new Expectations() {
            {
                remoteAssistMgmtService.updateRemoteAssistState((UUID) any,(RemoteAssistState) any);
            }
        };

        try {
            computerRemoteAssistNotifyService.remoteAssistReportState(request);
        } catch (Exception e) {
            Assert.fail();
        }

    }

}
