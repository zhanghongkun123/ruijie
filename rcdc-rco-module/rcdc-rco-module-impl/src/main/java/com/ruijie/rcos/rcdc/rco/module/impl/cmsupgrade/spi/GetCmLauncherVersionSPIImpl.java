package com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.spi;

import java.util.Objects;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.enums.AppTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.CmsUpgradeBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.dto.GetCmLauncherVersionDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.cmsupgrade.service.CmsUpgradeService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description: 镜像安装或升级完CMS，发送版本号事件给RCDC
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
@DispatcherImplemetion(CmsCmdId.RCO_CMD_ID_GET_CM_LAUNCHER_VERSION)
public class GetCmLauncherVersionSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCmLauncherVersionSPIImpl.class);

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CmsUpgradeService cmsUpgradeService;

    private static final String TEMPVMID_FLAG = "tempVmId";

    private static final String CMS = "cms-windows-app";

    private static final String UWS = "uws-windows-app";

    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getDto(), "dto can not be null");
        UUID tempVmId = request.getDto().getDeskId();
        PageWebRequest pageWebRequest = new PageWebRequest();
        pageWebRequest.setLimit(1);
        pageWebRequest.setPage(0);
        LocalImageTemplatePageRequest apiRequest = new LocalImageTemplatePageRequest(pageWebRequest);
        MatchEqual matchEqual = new MatchEqual();
        matchEqual.setName(TEMPVMID_FLAG);
        matchEqual.setValueArr(new Object[] {tempVmId});
        MatchEqual[] matchEqualArr = new MatchEqual[] {matchEqual};
        apiRequest.setMatchEqualArr(matchEqualArr);
        DefaultPageResponse<CbbImageTemplateDetailDTO> localPageImageTemplate = cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(apiRequest);
        if (Objects.isNull(localPageImageTemplate) || localPageImageTemplate.getItemArr().length == 0) {
            LOGGER.error("镜像[{}]不存在,不保存镜像应用软件版本关联表", tempVmId);
            throw new BusinessException(CmsUpgradeBusinessKey.RCDC_RCO_IMAGE_TEMPLATE_NOT_EXIT, tempVmId.toString());
        }
        UUID imageId = localPageImageTemplate.getItemArr()[0].getId();
        GetCmLauncherVersionDTO getCmLauncherVersionDTO = parseGuestToolMsg(request.getDto().getBody(), GetCmLauncherVersionDTO.class);
        LOGGER.info("更新镜像{}应用软件版本关联表<{}>", imageId, getCmLauncherVersionDTO.getVersion());
        if (getCmLauncherVersionDTO.getClient().equals(CMS)) {
            cmsUpgradeService.saveIsoVersionRecord(imageId, getCmLauncherVersionDTO.getVersion(), AppTypeEnum.CMLAUNCHER);
        }
        if (getCmLauncherVersionDTO.getClient().equals(UWS)) {
            cmsUpgradeService.saveIsoVersionRecord(imageId, getCmLauncherVersionDTO.getVersion(), AppTypeEnum.UWSLAUNCHER);
        }

        final CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = new CbbGuesttoolMessageDTO();
        cbbGuesttoolMessageDTO.setCmdId(request.getDto().getCmdId());
        cbbGuesttoolMessageDTO.setPortId(request.getDto().getPortId());
        LOGGER.info("receive success");
        return cbbGuesttoolMessageDTO;
    }

    private <T> T parseGuestToolMsg(String msgBody, Class<T> clz) {
        T bodyMsg;
        try {
            bodyMsg = JSON.parseObject(msgBody, clz);
        } catch (Exception e) {
            throw new IllegalArgumentException("guest tool报文格式错误.data:" + msgBody, e);
        }
        return bodyMsg;
    }
}
