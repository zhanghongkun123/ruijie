package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateEditingInfoDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageIdRequestDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

/**
 * Description: 关闭镜像模板
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 16:36
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.VDI_EDIT_IMAGE_STOP_ACTION)
public class StopEditImageVmHandlerSPIImpl extends AbstractVDIEditImageHandlerSPIImpl<VDIEditImageIdRequestDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StopEditImageVmHandlerSPIImpl.class);

    private static final String CLOSE_VM_THREAD_NAME = "close_vm";

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private VDIEditImageHelper helper;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, VDIEditImageIdRequestDTO requestDTO, UUID adminId) {
        // 查询传入的镜像是否允许被当前管理员操作，并获取编辑信息
        CbbImageTemplateEditingInfoDTO imageTemplateEditingInfoDTO;
        try {
            imageTemplateEditingInfoDTO = helper.getImageEditingInfoIfPresent(requestDTO.getId(), adminId, request.getTerminalId());
            LOGGER.debug("获取镜像编辑信息：[{}]", imageTemplateEditingInfoDTO == null ? "null" : imageTemplateEditingInfoDTO.toString());
        } catch (BusinessException e) {
            LOGGER.error("当前管理员不可操作目标镜像", e);
            return buildErrorResponseMessage(request);
        }

        LOGGER.info("开始执行关闭镜像虚机任务");
        ThreadExecutors.execute(this.getClass().getName() + CLOSE_VM_THREAD_NAME,
            () -> closeVm(requestDTO.getId(), adminId, request.getTerminalId()));

        return buildResponseMessage(request, new Object());
    }

    private void closeVm(UUID imageId, UUID adminId, String terminalId) {
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        try {
            cbbImageTemplateMgmtAPI.closeVm(imageId, Boolean.TRUE);
            LOGGER.info("关闭镜像成功");
            helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_STOP_VM_SUCCESS, adminId, imageId, terminalAddr);
        } catch (BusinessException e) {
            LOGGER.error("关闭镜像失败", e);
            helper.recordLog(BusinessKey.RCDC_RCO_VDI_EDIT_IMAGE_STOP_VM_FAIL, e, adminId, imageId, terminalAddr);
        }
    }

    @Override
    VDIEditImageIdRequestDTO resolveJsonString(String dataJsonString) {
        return JSONObject.parseObject(dataJsonString, VDIEditImageIdRequestDTO.class);
    }
}
