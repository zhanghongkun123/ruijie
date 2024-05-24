package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryImageTemplateEditStateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryVmVncURLResultDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import mockit.Capturing;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/23 09:04
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class VncPreControllerTest {

    @Tested
    private VncPreController vncPreController;

    @Injectable
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Capturing
    private SessionContext sessionContext;

    /**
     * 查询VNC地址，正常流程
     * @throws BusinessException 业务异常
     */
    @Test
    public void testQueryVmVncURL() throws BusinessException {
        IdWebRequest request = new IdWebRequest();
        request.setId(UUID.randomUUID());

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.queryVmVncURL((UUID) any);
                result = new CbbQueryVmVncURLResultDTO();
            }
        };

        DefaultWebResponse webResponse = vncPreController.queryVmVncURL(request);

        Assert.assertEquals(WebResponse.Status.SUCCESS, webResponse.getStatus());
    }

    /**
     * 查询VNC地址，查询异常
     * @throws BusinessException 业务异常
     */
    @Test
    public void testQueryVmVncURLQueryException() throws BusinessException {
        IdWebRequest request = new IdWebRequest();
        request.setId(UUID.randomUUID());

        new Expectations(LocaleI18nResolver.class) {
            {
                cbbImageTemplateMgmtAPI.queryVmVncURL((UUID) any);
                result = new BusinessException("xxx");
                LocaleI18nResolver.resolve(anyString, (String[]) any);
            }
        };

        DefaultWebResponse webResponse = vncPreController.queryVmVncURL(request);

        Assert.assertEquals(WebResponse.Status.ERROR, webResponse.getStatus());
    }

    /**
     * 查询编辑中的镜像状态，正常流程
     * @throws BusinessException 业务异常
     */
    @Test
    public void testQueryImageTemplateEditState() throws BusinessException {
        IdWebRequest request = new IdWebRequest();
        request.setId(UUID.randomUUID());

        new Expectations() {
            {
                cbbImageTemplateMgmtAPI.queryImageTemplateEditState((CbbQueryImageTemplateEditStateDTO) any);
                sessionContext.getUserId();
                result = UUID.randomUUID();
            }
        };

        DefaultWebResponse webResponse = vncPreController.queryImageTemplateEditState(request, sessionContext);

        Assert.assertEquals(WebResponse.Status.SUCCESS, webResponse.getStatus());
    }
}