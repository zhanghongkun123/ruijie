package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ThemePictureTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.GetPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.RecoverPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rcdctheme.UploadPictureRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetPictureResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.config.ConfigFacade;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.Response;

import mockit.*;
import mockit.integration.junit4.JMockit;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/15 18:37
 *
 * @author conghaifeng
 */
@RunWith(JMockit.class)
public class RcdcThemeAPIImplTest {

    @Tested
    RcdcThemeAPIImpl rcdcThemeAPI;

    @Injectable
    ConfigFacade configFacade;


    /**
     * 测试所有的输入参数为空的情况
     */
    @Test
    public void testParamNullError() {
        try {
            ThrowExceptionTester.throwIllegalArgumentException(() -> rcdcThemeAPI.uploadPicture(null),
                    "request can not be null");
            ThrowExceptionTester.throwIllegalArgumentException(() -> rcdcThemeAPI.getPicturePath(null),
                    "request can not be null");
            ThrowExceptionTester.throwIllegalArgumentException(() -> rcdcThemeAPI.recoverThemeToDefault(null),
                    "request can not be null");
        } catch (Exception ex) {
            Assert.fail();
        }
    }

    /**
     * 测试上传文件成功
     *
     * @throws Exception 异常
     */
    @Test
    public void testUploadPicture() throws Exception {
        UploadPictureRequest request = new UploadPictureRequest("picturePath",
                ThemePictureTypeEnum.RCDC_LOGIN_LOGO);

        new MockUp<File>() {

            @Mock
            boolean mkdir() {
                return false;
            }

        };

        new Expectations(Files.class) {
            {
                configFacade.read((String) any);
                result = "/opt/web/theme/";
                Files.move((Path) any,(Path) any);
            }
        };

        DefaultResponse response = rcdcThemeAPI.uploadPicture(request);
        Assert.assertEquals(response.getStatus(), Response.Status.SUCCESS);
    }

    /**
     * 测试上传文件抛出异常
     *
     * @throws Exception 异常
     */
    @Test
    public void testUploadPictureWithException() throws Exception {
        UploadPictureRequest request = new UploadPictureRequest("picturePath",
                ThemePictureTypeEnum.RCDC_LOGIN_LOGO);
        IOException ex = new IOException();

        new MockUp<File>() {

            @Mock
            boolean mkdir() {
                return false;
            }

        };

        new Expectations(Files.class) {
            {
                configFacade.read((String) any);
                result = "/opt/web/theme/";
                Files.move((Path) any,(Path) any);
                result = ex;
            }
        };

        try {
            rcdcThemeAPI.uploadPicture(request);
            Assert.fail();
        } catch (BusinessException bex) {
            Assert.assertEquals(BusinessKey.RCDC_RCO_UPLOAD_PICTURE_FAIL, bex.getKey());
        }
    }

    /**
     * 测试主题初始化
     */
    @Test
    public void testRecoverRcdcThemeToDefault() {
        ArrayList<ThemePictureTypeEnum> themePictureTypeEnumsList = Lists.newArrayList(ThemePictureTypeEnum.RCDC_LOGIN_LOGO);
        RecoverPictureRequest request = new RecoverPictureRequest(themePictureTypeEnumsList);
        new MockUp<File>() {

            @Mock
            boolean mkdir() {
                return false;
            }

            @Mock
            File[] listFiles() {
                File file = new File("/opt/web/theme/test.txt");
                return new File[]{file};
            }

        };

        new Expectations() {
            {
                configFacade.read((String) any);
                result = "/opt/web/theme/";
            }
        };

        DefaultResponse response = rcdcThemeAPI.recoverThemeToDefault(request);
        Assert.assertEquals(response.getStatus(), Response.Status.SUCCESS);
    }

    /**
     * 测试正常获取图片路径
     */
    @Test
    public void testGetPicturePath() {
        GetPictureRequest request = new GetPictureRequest(ThemePictureTypeEnum.RCDC_LOGIN_LOGO);
        new MockUp<File>() {

            @Mock
            boolean exists() {
                return true;
            }

        };

        new Expectations() {
            {
                configFacade.read((String) any);
                result = "/xxx/";
            }
        };

        GetPictureResponse response = rcdcThemeAPI.getPicturePath(request);
        Assert.assertEquals(response.getPicturePath(),"/xxx/loginLogo.png");
    }

    /**
     * 测试获取图片时图片路径不存在
     */
    @Test
    public void testGetPicturePathNull() {
        GetPictureRequest request = new GetPictureRequest(ThemePictureTypeEnum.RCDC_LOGIN_LOGO);
        new MockUp<File>() {

            @Mock
            boolean exists() {
                return false;
            }

        };

        new Expectations() {
            {
                configFacade.read((String) any);
                result = "xxx";
            }
        };

        GetPictureResponse response = rcdcThemeAPI.getPicturePath(request);
        Assert.assertEquals(response.getPicturePath(),null);
    }


}
