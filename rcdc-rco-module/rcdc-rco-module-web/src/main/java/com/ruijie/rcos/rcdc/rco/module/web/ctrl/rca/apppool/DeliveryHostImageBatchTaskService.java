package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask.RcaUpdateAppPoolImageBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request.EditImageTemplateWebRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 多版本交付-触发应用主机的镜像变更批任务
 * Copyright: Copyright (c) 202
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月27日
 *
 * @author liuwc
 */
@Service
public class DeliveryHostImageBatchTaskService {

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcaHostAppAPI rcaHostAppAPI;

    /**
     * 执行应用主机镜像变更批任务的 相关操作
     * @param  appPoolBaseDTO RcaAppPoolBaseDTO
     * @param hostDTOList 应用主机集合
     * @param imageTemplateId 镜像ID
     * @param builder    批处理器
     * @return 响应信息
     * @throws BusinessException 业务异常
     */
    public DefaultWebResponse startChangeHostImage(BatchTaskBuilder builder, RcaAppPoolBaseDTO appPoolBaseDTO
            , List<RcaHostDTO> hostDTOList, UUID imageTemplateId)
            throws BusinessException {
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(appPoolBaseDTO, "appPoolBaseDTO must not be null");
        Assert.notNull(hostDTOList, "hostDTOList must not be null");
        Assert.notNull(imageTemplateId, "imageTemplateId must not be null");

        // 创建主机变更的批任务
        List<UUID> hostIdList = hostDTOList.stream().map(RcaHostDTO::getId).collect(Collectors.toList());
        final Iterator<DefaultBatchTaskItem> iterator = hostIdList.stream().map(item -> DefaultBatchTaskItem.builder().itemId(item)
                .itemName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_ITEM, new String[]{}).build()).iterator();
        EditImageTemplateWebRequest editImageTemplateWebRequest = new EditImageTemplateWebRequest();
        editImageTemplateWebRequest.setAppPoolId(appPoolBaseDTO.getId());
        editImageTemplateWebRequest.setImageTemplateId(imageTemplateId);
        final UUID taskPoolId = UUID.nameUUIDFromBytes("editHostImagePool".getBytes());
        final RcaUpdateAppPoolImageBatchTaskHandler handler = buildUpdateAppPoolImageHandler(editImageTemplateWebRequest, iterator);
        BatchTaskSubmitResult editHostImageresult = builder.setTaskName(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_APP_POOL_EDIT_IMAGE_BATCH_TASK_DESC)
                .enableParallel()
                .registerHandler(handler)
                .enablePerformanceMode(taskPoolId, 30)
                .start();
        return DefaultWebResponse.Builder.success(editHostImageresult);
    }

    private RcaUpdateAppPoolImageBatchTaskHandler buildUpdateAppPoolImageHandler(EditImageTemplateWebRequest request,
                                                                                 Iterator<DefaultBatchTaskItem> iterator) throws BusinessException {
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(request.getAppPoolId());

        final RcaUpdateAppPoolImageBatchTaskHandler handler = new RcaUpdateAppPoolImageBatchTaskHandler(iterator);
        handler.setAppPoolBaseDTO(appPoolBaseDTO);
        handler.setTemplateImageId(request.getImageTemplateId());
        handler.setRcaAppPoolAPI(rcaAppPoolAPI);
        handler.setCbbDeskMgmtAPI(cbbDeskMgmtAPI);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbImageTemplateMgmtAPI(cbbImageTemplateMgmtAPI);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setRcaHostAppAPI(rcaHostAppAPI);
        return handler;
    }


}
