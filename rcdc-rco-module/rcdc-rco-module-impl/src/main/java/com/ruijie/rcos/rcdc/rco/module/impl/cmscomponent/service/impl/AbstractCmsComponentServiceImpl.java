package com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service.impl;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.FilesOperationType;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cmscomponent.enums.CmsComponentEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.CmsComponentBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cmscomponent.service.CmsComponentService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.io.File;

/**
 * Description: CMSComponentServiceImpl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-09-04
 *
 * @author wjp
 */
public abstract class AbstractCmsComponentServiceImpl implements CmsComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCmsComponentServiceImpl.class);

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CbbDeskSoftMgmtAPI deskSoftMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;


    private static final String CMS_COMPONENT = "cms_component";


    @Override
    public CmsComponentEnum getCMSComponent() throws BusinessException {
        LOGGER.info("调用平台组件获取CMS组件启用情况");
        if (serverModelAPI.isInitModel()) {
            LOGGER.error("获取服务器部署信息失败：未找到对应的模式");
            throw new BusinessException(CmsComponentBusinessKey.RCDC_RCO_GET_CMS_COMPONENT_STATUS_ERROR);
        }
        if (serverModelAPI.isMiniModel()) {
            return CmsComponentEnum.DISABLED_STATE;
        } else {
            return CmsComponentEnum.ENABLED_STATE;
        }
    }

    @Override
    public FindParameterResponse getCMSComponentFlag() {
        return rcoGlobalParameterAPI.findParameter(new FindParameterRequest(CMS_COMPONENT));
    }

    @Override
    public void updateCMSComponentFlag(String cmsComponent) {
        Assert.hasText(cmsComponent, "cmsComponent can not empty");
        rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(CMS_COMPONENT, cmsComponent));
    }

    @Override
    public void initCMSComponentFlag() {
        LOGGER.warn("执行初始化CMS组件启用情况标识");
        rcoGlobalParameterAPI.updateParameter(new UpdateParameterRequest(CMS_COMPONENT, CmsComponentEnum.INIT_STATE.getName()));
    }

    @Override
    public Boolean hasCMSPackage() {
        File assistantFile = new File(Constants.SUNNY_CLIENT_PACKAGE_PATH + Constants.SUNNY_CLIENT_PACKAGE_NAME);
        return assistantFile.exists();
    }

    @Override
    public void copyCMSPackage() throws BusinessException {
        CbbCreateDeskSoftDTO request = new CbbCreateDeskSoftDTO();
        CbbDeskSoftDTO existFile = deskSoftMgmtAPI.findByName(Constants.SUNNY_CLIENT_PACKAGE_NAME);
        if (existFile != null) {
            request.setDeskSoftId(existFile.getId());
        }
        request.setFilePath(Constants.SUNNY_CLIENT_PACKAGE_PATH + Constants.SUNNY_CLIENT_PACKAGE_NAME);
        request.setFileName(Constants.SUNNY_CLIENT_PACKAGE_NAME);
        request.setFilesOperationType(FilesOperationType.COPY);
        deskSoftMgmtAPI.createDeskSoft(request);
    }



}
