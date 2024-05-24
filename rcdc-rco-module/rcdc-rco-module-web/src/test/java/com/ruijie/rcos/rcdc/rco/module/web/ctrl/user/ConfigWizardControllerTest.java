package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.base.sysmanage.module.def.api.BaseLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoTrialLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ConfigurationWizardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ConfigurationWizardCustomDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.ConfigWizardController;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.GetConfigurationWizardWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.SetConfigurationWizardWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * <br>
 * Description:  Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd.  <br>
 * Create Time: 2019/4/8  <br>
 *
 * @author yyz
 */
@RunWith(JMockit.class)
public class ConfigWizardControllerTest {

    @Tested
    private ConfigWizardController configurationWizardController;

    @Injectable
    private ConfigurationWizardAPI configurationWizardAPI;
    
    @Injectable
    private BaseLicenseMgmtAPI baseLicenseMgmtAPI;
    
    @Injectable
    private AutoTrialLicenseAPI autoTrialLicenseAPI;

    @Injectable
    private AdminMgmtAPI adminMgmtAPI;

    /**
     * 测试获取配置向导
     */
    @Test
    public void testGetConfigurationWizard() {
        GetConfigurationWizardWebRequest request = new GetConfigurationWizardWebRequest();
        new Expectations() {
            {
                configurationWizardAPI.getConfigurationWizard((GetConfigurationWizardRequest) any);
            }
        };
        configurationWizardController.getConfigurationWizard(request);
        new Verifications() {
            {
                configurationWizardAPI.getConfigurationWizard((GetConfigurationWizardRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 测试设置配置向导
     */
    @Test
    public void testSetConfigurationWizard() throws BusinessException {
        SetConfigurationWizardWebRequest webRequest = new SetConfigurationWizardWebRequest();
        webRequest.setIndex(1);
        webRequest.setShow(true);
        ConfigurationWizardCustomDataDTO customDataDTO = new ConfigurationWizardCustomDataDTO();
        customDataDTO.setKey("userIndex");
        customDataDTO.setValue(0);
        ConfigurationWizardCustomDataDTO[] dtoArr = {customDataDTO};
        webRequest.setCustomDataArr(dtoArr);
        new Expectations() {
            {
                configurationWizardAPI.setConfigurationWizard((SetConfigurationWizardRequest) any);
            }
        };
        configurationWizardController.setConfigurationWizard(webRequest);
        new Verifications() {
            {
                configurationWizardAPI.setConfigurationWizard((SetConfigurationWizardRequest) any);
                times = 1;
            }
        };
    }
}