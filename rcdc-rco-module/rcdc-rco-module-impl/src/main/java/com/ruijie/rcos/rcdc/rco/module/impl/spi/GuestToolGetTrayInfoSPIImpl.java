package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.LocalImageTemplatePageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbGuestToolMessageDispatcherSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbGuestToolSPIReceiveRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TrayInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: GT获取托盘的信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/28
 *
 * @author chenjiehui
 */
@DispatcherImplemetion(GuestToolCmdId.RCDC_GT_CMD_ID_GET_TRAY_INFO)
public class GuestToolGetTrayInfoSPIImpl implements CbbGuestToolMessageDispatcherSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GuestToolGetTrayInfoSPIImpl.class);

    private static final String TRUE = "true";

    private static final String RCDC = "1";

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private static final String TEMPVMID_FLAG = "tempVmId";


    @Override
    public CbbGuesttoolMessageDTO receive(CbbGuestToolSPIReceiveRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot null");
        LOGGER.info("收到托盘请求:{}", JSON.toJSONString(request));

        final CbbGuesttoolMessageDTO messageDTO = request.getDto();
        final CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();

        dto.setCmdId(Integer.valueOf(GuestToolCmdId.RCDC_GT_CMD_ID_GET_TRAY_INFO));
        dto.setPortId(GuestToolCmdId.RCDC_GT_PORT_ID_TRAY_INFO);
        dto.setDeskId(messageDTO.getDeskId());

        TrayInfoDTO trayInfoDTO = new TrayInfoDTO();
        TrayInfoDTO.BodyMessage bodyMessage = new TrayInfoDTO.BodyMessage();
        if (RCDC.equals(messageDTO.getDstType())) {
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
            DefaultPageResponse<CbbImageTemplateDetailDTO> localPageImageTemplate =
                    cbbImageTemplateMgmtAPI.pageQueryLocalPageImageTemplate(apiRequest);
            UUID imageId;
            if (Objects.isNull(localPageImageTemplate) || localPageImageTemplate.getTotal() == 0) {
                CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(messageDTO.getDeskId());
                imageId = deskDTO.getImageTemplateId();
            } else {
                imageId = localPageImageTemplate.getItemArr()[0].getId();
            }

            CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
            ArrayList<JSONObject> trayList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            if (ImageTemplateState.EDITING.equals(imageTemplateInfoDTO.getImageState())) {
                jsonObject.put("name", "VirtualDisk");
            }
            trayList.add(jsonObject);
            bodyMessage.setEnable(TRUE);
            bodyMessage.setFloatType(0);
            bodyMessage.setTrayList(trayList);

            trayInfoDTO.setCode(0);
            trayInfoDTO.setMessage("");
            trayInfoDTO.setContent(bodyMessage);
        }
        dto.setBody(JSON.toJSONString(trayInfoDTO));

        return dto;
    }

}
