package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal;

import com.ruijie.rcos.rcdc.rco.module.def.api.AppTerminalAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.EmptyDownloadWebRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.webmvc.api.response.DownloadWebResponse;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/27 10:50
 *
 * @author conghaifeng
 */
@RunWith(SkyEngineRunner.class)
public class TerminalAppDownLoadControllerTest {
    @Tested
    private TerminalAppDownLoadController controller;

    @Injectable
    private AppTerminalAPI appTerminalAPI;

    /**
     *测试downloadWindowsApp
     *
     *@throws Exception 异常
     */
    @Test
    public void testDownloadWindowsApp() throws Exception {
        String rootPath = this.getClass().getResource("/").getPath();
        File file = new File(rootPath + "RCO_Client_1.0.23_Setup.exe");
        file.createNewFile();
        String url = rootPath + "RCO_Client_1.0.23_Setup.exe";

        new Expectations() {
            {
                appTerminalAPI.getAppDownloadUrl(CbbTerminalTypeEnums.APP_WINDOWS);
                result = url;
            }
        };

        DownloadWebResponse webResponse = controller.downloadWindowsApp(new EmptyDownloadWebRequest());

        Assert.assertEquals("RCO_Client_1.0.23_Setup", webResponse.getFileName());

    }

    /**
     *测试downloadUosApp
     *
     *@throws Exception 异常
     */
    @Test
    public void testDownloadUosApp() throws Exception {
        String rootPath = this.getClass().getResource("/").getPath();
        File file = new File(rootPath + "RCO_Client_1.0.23_Setup.exe");
        file.createNewFile();
        String url = rootPath + "RCO_Client_1.0.23_Setup.exe";

        new Expectations() {
            {
                appTerminalAPI.getAppDownloadUrl(CbbTerminalTypeEnums.APP_UOS);
                result = url;
            }
        };

        DownloadWebResponse webResponse = controller.downloadUosApp(new EmptyDownloadWebRequest());

        Assert.assertEquals("RCO_Client_1.0.23_Setup", webResponse.getFileName());
    }

    /**
     *测试downloadNeoKylinApp
     *
     *@throws Exception 异常
     */
    @Test
    public void testDownloadNeoKylinApp() throws Exception {
        String rootPath = this.getClass().getResource("/").getPath();
        File file = new File(rootPath + "RCO_Client_1.0.23_Setup.exe");
        file.createNewFile();
        String url = rootPath + "RCO_Client_1.0.23_Setup.exe";

        new Expectations() {
            {
                appTerminalAPI.getAppDownloadUrl(CbbTerminalTypeEnums.APP_NEOKYLIN);
                result = url;
            }
        };

        DownloadWebResponse webResponse = controller.downloadNeoKylinApp(new EmptyDownloadWebRequest());

        Assert.assertEquals("RCO_Client_1.0.23_Setup", webResponse.getFileName());
    }

}
