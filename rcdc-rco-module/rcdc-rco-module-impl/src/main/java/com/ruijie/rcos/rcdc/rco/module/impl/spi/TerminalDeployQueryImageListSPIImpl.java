package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ConfigWizardForIDVCode;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.constant.ImageDispatcherConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.ImagePageQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo.ImageTemplateDetailVO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalWorkModeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 终端部署时获取镜像列表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/10
 *
 * @author WuShengQiang
 */
@DispatcherImplemetion(ImageDispatcherConstants.TERMINAL_DEPLOY_QUERY_IMAGE_LIST)
public class TerminalDeployQueryImageListSPIImpl extends AbstractTerminalDeploySPI implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalDeployQueryImageListSPIImpl.class);

    private static final String CREATE_TIME = "createTime";

    private static final String CBB_IMAGE_TYPE = "cbbImageType";

    private static final String CAN_SUPPORT_TC = "canSupportTC";

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(request.getTerminalId(), "terminalId cannot be null!");
        String data = request.getData();
        Assert.notNull(data, "terminalId cannot be null!");
        JSONObject dataJson = JSONObject.parseObject(data);
        boolean canSupportTC = dataJson.getBooleanValue(CAN_SUPPORT_TC);
        CbbResponseShineMessage<?> cbbResponseShineMessage;

        // 查询终端支持的工作模式和驱动类型
        CbbTerminalBasicInfoDTO terminalDTO = new CbbTerminalBasicInfoDTO();
        try {
            terminalDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
        } catch (BusinessException e) {
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, ConfigWizardForIDVCode.TERMINAL_NOT_EXIST);
            messageHandlerAPI.response(cbbResponseShineMessage);
            return;
        }

        CbbTerminalWorkModeEnums[] supportWorkModeArr = terminalDTO.getSupportWorkModeArr();
        List<CbbTerminalWorkModeEnums> terminalModeList = new ArrayList<>();
        for (CbbTerminalWorkModeEnums terminalWorkModeEnums : supportWorkModeArr) {
            if (CbbTerminalWorkModeEnums.VOI == terminalWorkModeEnums || CbbTerminalWorkModeEnums.IDV == terminalWorkModeEnums) {
                terminalModeList.add(terminalWorkModeEnums);
            }
        }

        try {
            // 查询镜像列表
            PageQueryRequest pageQueryRequest = pageQueryBuilderFactory.newRequestBuilder(new ImagePageQueryRequest())
                    .in(CBB_IMAGE_TYPE, terminalModeList.toArray(new CbbTerminalWorkModeEnums[0])).desc(CREATE_TIME).build();
            PageQueryResponse<CbbImageTemplateDTO> pageQueryResponse = cbbImageTemplateMgmtAPI.pageQuery(pageQueryRequest);

            // 处理不可选择的错误提示
            List<ImageTemplateDetailVO> imageList = buildImageVOList(terminalDTO, pageQueryResponse, canSupportTC);

            JSONObject result = new JSONObject();
            result.put("imageList", imageList);
            cbbResponseShineMessage = ShineMessageUtil.buildResponseMessage(request, result);
        } catch (Exception e) {
            LOGGER.error("终端部署时获取镜像列表,出现异常", e);
            cbbResponseShineMessage = ShineMessageUtil.buildErrorResponseMessage(request, Constants.FAILURE);
        }
        messageHandlerAPI.response(cbbResponseShineMessage);
    }

    private List<ImageTemplateDetailVO> buildImageVOList(CbbTerminalBasicInfoDTO terminalDTO, PageQueryResponse<CbbImageTemplateDTO> response,
                                                         boolean canSupportTC) {
        List<ImageTemplateDetailVO> imageList = new ArrayList<>();
        if (response.getItemArr() == null || response.getItemArr().length == 0) {
            return imageList;
        }

        for (CbbImageTemplateDTO cbbImageTemplateDTO : response.getItemArr()) {
            ImageTemplateDetailVO imageTemplate = new ImageTemplateDetailVO();
            imageTemplate.setImageId(cbbImageTemplateDTO.getId());
            imageTemplate.setImageName(cbbImageTemplateDTO.getImageTemplateName());
            imageTemplate.setCbbOsType(cbbImageTemplateDTO.getOsType());
            imageTemplate.setSupportGoldenImage(cbbImageTemplateDTO.isSupportGoldenImage());
            imageTemplate.setCbbImageType(cbbImageTemplateDTO.getCbbImageType());
            imageTemplate.setImageTemplateState(cbbImageTemplateDTO.getImageTemplateState());
            if (AVAILABLE_STATE.contains(imageTemplate.getImageTemplateState())) {
                imageTemplate.setCanUsed(true);
                try {
                    validateImage(cbbImageTemplateDTO, terminalDTO, canSupportTC);
                } catch (BusinessException e) {
                    imageTemplate.setCanUsed(false);
                    imageTemplate.setCanUsedMessage(e.getI18nMessage());
                }
            } else {
                imageTemplate.setCanUsed(false);
                String message = LocaleI18nResolver.resolve(BusinessKey.RCDC_IMAGE_TEMPLATE_STATE_DISABLED);
                imageTemplate.setCanUsedMessage(message);
            }
            imageList.add(imageTemplate);
        }
        return imageList;
    }

}
