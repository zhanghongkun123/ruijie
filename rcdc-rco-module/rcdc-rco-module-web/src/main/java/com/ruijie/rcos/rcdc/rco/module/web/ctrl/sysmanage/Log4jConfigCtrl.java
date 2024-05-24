package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.base.sysmanage.module.def.api.Log4jConfigAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.log4jconfig.BaseAddLog4jConfigRequest;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.log4jconfig.BaseEditLog4jConfigRequest;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.log4jconfig.BaseListLog4jConfigRequest;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.log4j.BaseDetailLog4jConfigResponse;
import com.ruijie.rcos.base.sysmanage.module.def.dto.Log4jConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask.Log4jConfigDeleteBatchTask;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig.BaseAddLog4jConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig.BaseDetailLog4jConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig.BaseEditLog4jConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig.BaseListLog4jConfigWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.log4jconfig.BaseRemoveLog4jConfigWebRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年02月18日
 *
 * @author GuoZhouYue
 */
@Controller
@RequestMapping("rco/systemConfig/log4j")
public class Log4jConfigCtrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log4jConfigCtrl.class);

    @Autowired
    Log4jConfigAPI log4jConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 列出所有log配置
     *
     * @param webRequest webRequest
     * @return DefaultWebResponse
     */
    @RequestMapping(value = "list")
    public DefaultWebResponse listLog4jConfig(BaseListLog4jConfigWebRequest webRequest) {
        final BaseListLog4jConfigRequest request = new BaseListLog4jConfigRequest();
        BeanUtils.copyProperties(webRequest, request);
        DefaultPageResponse<Log4jConfigDTO> response = log4jConfigAPI.getAll(request);

        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 列出一个log配置
     *
     * @param webRequest webRequest
     * @return DefaultWebResponse
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "detail")
    public DefaultWebResponse detailLog4jConfig(BaseDetailLog4jConfigWebRequest webRequest) throws BusinessException {

        final BaseDetailLog4jConfigResponse result = log4jConfigAPI.getOne(webRequest.getId());

        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 添加log配置
     *
     * @param webRequest webRequest 请求对象
     * @throws BusinessException 业务异常
     * @return DefaultWebResponse 默认响应对象
     */
    @RequestMapping(value = "create")
    public DefaultWebResponse addLog4jConfig(BaseAddLog4jConfigWebRequest webRequest) throws BusinessException {
        final BaseAddLog4jConfigRequest request = new BaseAddLog4jConfigRequest();
        BeanUtils.copyProperties(webRequest, request);

        try {
            log4jConfigAPI.addConfig(request);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_CREATE_LOG_GRADE_DO_SUCCESS,
                    request.getLoggerName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS,
                    StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("创建日志配置失败", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_CREATE_LOG_GRADE_DO_FAIL,
                    request.getLoggerName(), e.getI18nMessage());
            throw e;
        }

    }

    /**
     * 删除log配置
     *
     * @param webRequest webRequest 请求对象
     * @param taskBuilder 任务建造者
     * @return DefaultWebResponse 默认响应对象
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public DefaultWebResponse deleteLog4jConfig(BaseRemoveLog4jConfigWebRequest webRequest,
            BatchTaskBuilder taskBuilder) throws BusinessException {

        final UUID[] idArr = webRequest.getIdArr();
        List<UUID> idList = Arrays.asList(idArr);
        boolean isBatch = idList.size() > 1;
        if (isBatch) {
            return batchDeleteLog4jConfig(taskBuilder, idArr);
        } else {
            return deleteOneLog4jConfig(idList.get(0));
        }
    }

    /**
     * 编辑log配置
     *
     * @param webRequest webRequest 请求对象
     * @return DefaultWebResponse 默认响应对象
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "edit")
    public DefaultWebResponse editLog4jConfig(BaseEditLog4jConfigWebRequest webRequest) throws BusinessException {
        final BaseEditLog4jConfigRequest request = new BaseEditLog4jConfigRequest();
        BeanUtils.copyProperties(webRequest, request);
        BaseDetailLog4jConfigResponse detailLog4jConfigResponse = null;
        try {
            detailLog4jConfigResponse = log4jConfigAPI.getOne(webRequest.getId());
            log4jConfigAPI.editConfigLevel(request);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_EDIT_LOG_GRADE_DO_SUCCESS,
                    detailLog4jConfigResponse.getLoggerName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS,
                    StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("编辑日志配置失败", e);
            String name = detailLog4jConfigResponse == null ? webRequest.getId().toString()
                    : detailLog4jConfigResponse.getLoggerName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_EDIT_LOG_GRADE_DO_FAIL, name,
                    e.getI18nMessage());
            throw e;
        }
    }


    private DefaultWebResponse batchDeleteLog4jConfig(BatchTaskBuilder taskBuilder, UUID[] idArr) throws BusinessException {
        String itemName =
                LocaleI18nResolver.resolve(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_ITEM_NAME);
        final Iterator<BatchTaskItem> iterator = Stream.of(idArr)
                .map(id -> (BatchTaskItem) DefaultBatchTaskItem.builder()//
                        .itemId(id)//
                        .itemName(itemName)//
                        .build()) //
                .iterator();

        final Log4jConfigDeleteBatchTask log4jConfigDeleteBatchTask =
                new Log4jConfigDeleteBatchTask(iterator, log4jConfigAPI, auditLogAPI);

        BatchTaskSubmitResult batchTaskSubmitResult =
                taskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_NAME)//
                        .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_DESC) //
                        .registerHandler(log4jConfigDeleteBatchTask) //
                        .start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    private DefaultWebResponse deleteOneLog4jConfig(UUID id)
            throws BusinessException {
        BaseDetailLog4jConfigResponse detailLog4jConfigResponse = null;
        try {
            detailLog4jConfigResponse = log4jConfigAPI.getOne(id);

            log4jConfigAPI.removeConfig(id);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_GRADE_DO_SUCCESS,
                    detailLog4jConfigResponse.getLoggerName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS,
                    StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("删除日志配置失败", e);
            String name = detailLog4jConfigResponse == null ? id.toString() : detailLog4jConfigResponse.getLoggerName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_GRADE_DO_FAIL, name,
                    e.getI18nMessage());
            throw e;
        }
    }
}
