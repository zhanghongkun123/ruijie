package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPoolAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaPoolAppDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.CopyRcaAppRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.DeleteRcaAppRequest;
import com.ruijie.rcos.rcdc.rca.module.def.api.request.EditRcaAppRequest;
import com.ruijie.rcos.rcdc.rca.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.batchtask.RcaPoolAppPublishAppBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.batchtask.RcaPoolAppWithdrawAppBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request.CopyRcaAppWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request.DeleteRcaAppWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request.EditRcaAppWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request.PoolAppPageRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.webmvc.api.optlog.ProgrammaticOptLogRecorder;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 池应用
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月15日
 *
 * @author zhengjingyong
 */

@Api(tags = "池应用管理")
@Controller
@RequestMapping("/rca/poolApp")
public class RcaPoolAppController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaPoolAppController.class);

    private static final String GROUP_ID = "appGroupId";

    @Autowired
    private RcaPoolAppAPI rcaPoolAppAPI;

    @Autowired
    private RcaAppPoolAPI rcaAppPoolAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcaAppGroupAPI rcaAppGroupAPI;

    /**
     * 应用列表查询
     *
     * @param request   请求参数
     * @param sessionContext    session信息
     * @return 主机列表
     * @throws BusinessException 业务异常
     *
     */
    @ApiOperation("应用列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用列表"})})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse list(PageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");

        // 当存在分组查询条件时，加入过滤条件，默认分组应用列表，无需传入groupId
        PoolAppPageRequest pageReq = new PoolAppPageRequest(request);
        if (ArrayUtils.isEmpty(pageReq.getSortArr())) {
            pageReq.setSortArr(generateDefaultSortArr());
        }
        List<UUID> groupIdList = getGroupIdList(request);
        DefaultPageResponse<RcaPoolAppDTO> defaultPageResponse = new DefaultPageResponse<>();
        if (!CollectionUtils.isEmpty(groupIdList)) {
            List<RcaPoolAppDTO> groupAppList = rcaPoolAppAPI.getAllAppByGroupId(groupIdList);
            if (!CollectionUtils.isEmpty(groupAppList)) {
                pageReq.appendCustomMatchEqual(new MatchEqual("id",
                        groupAppList.stream().map(RcaPoolAppDTO::getId).toArray()));
                defaultPageResponse = rcaPoolAppAPI.pageQuery(pageReq);
            } else {
                defaultPageResponse.setTotal(0);
                defaultPageResponse.setItemArr(new RcaPoolAppDTO[0]);
            }
        } else {
            defaultPageResponse = rcaPoolAppAPI.pageQuery(pageReq);
        }

        return DefaultWebResponse.Builder.success(defaultPageResponse);
    }

    private Sort[] generateDefaultSortArr() {
        Sort firstSort = new Sort();
        firstSort.setSortField("createTime");
        firstSort.setDirection(Sort.Direction.DESC);

        Sort secondSort = new Sort();
        secondSort.setSortField("id");
        secondSort.setDirection(Sort.Direction.DESC);
        return ArrayUtils.toArray(firstSort, secondSort);
    }

    private List<UUID> getGroupIdList(PageWebRequest pageReq) {
        ExactMatch[] exactMatchArr = pageReq.getExactMatchArr();
        if (ArrayUtils.isEmpty(exactMatchArr)) {
            return new ArrayList<>();
        }

        for (ExactMatch exactMatch : exactMatchArr) {
            if (!GROUP_ID.equals(exactMatch.getName())) {
                continue;
            }
            return Arrays.stream(exactMatch.getValueArr()).map(item ->
                    UUID.fromString(item.toString())).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    /**
     * 获取应用详情
     * @param request request
     * @return 应用池详情
     * @throws BusinessException ex
     */
    @ApiOperation("应用详情")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"应用详情"})})
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public DefaultWebResponse detail(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        return DefaultWebResponse.Builder.success(rcaPoolAppAPI.getDetail(request.getId()));
    }

    /**
     * 编辑应用
     * @param request 编辑应用请求
     * @param optLogRecorder optLogRecorder
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("编辑")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑应用"})})
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse edit(EditRcaAppWebRequest request, ProgrammaticOptLogRecorder optLogRecorder) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(optLogRecorder, "optLogRecorder can not be null");

        RcaPoolAppDTO oldPoolAppDTO = rcaPoolAppAPI.getDetail(request.getId());
        EditRcaAppRequest editRcaAppRequest = new EditRcaAppRequest();
        BeanUtils.copyProperties(request, editRcaAppRequest);
        rcaPoolAppAPI.editPoolApp(editRcaAppRequest);

        if (oldPoolAppDTO.getIsPublished() && (editRcaAppRequest.getEnabled() || oldPoolAppDTO.getEnabled())) {
            try {
                rcaPoolAppAPI.notifyAppPublishToOc(Lists.newArrayList(editRcaAppRequest.getId()));
            } catch (BusinessException ex) {
                LOGGER.error("应用变更发送推送到OC失败，ex: ", ex);
            }
        }

        RcaPoolAppDTO poolAppDTO = rcaPoolAppAPI.getDetail(request.getId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolAppDTO.getPoolId());
        optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_POOL_APP_EDIT_SUCCESS, appPoolBaseDTO.getName(), request.getName());
        return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_EDIT_SUCCESS,
                new String[]{appPoolBaseDTO.getName(), request.getName()});
    }

    /**
     * 编辑应用(包含图标)
     * @param chunkUploadFile file
     * @param optLogRecorder optLogRecorder
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("编辑应用(包含图标)")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑应用(包含图标)"})})
    @RequestMapping(value = "/editWithIcon", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editWithIcon(ChunkUploadFile chunkUploadFile, ProgrammaticOptLogRecorder optLogRecorder) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile can not be null");
        Assert.notNull(optLogRecorder, "optLogRecorder can not be null");

        rcaPoolAppAPI.editPoolAppWithPic(chunkUploadFile);

        try {
            JSONObject customData = chunkUploadFile.getCustomData();
            EditRcaAppRequest editRcaAppRequest = customData.toJavaObject(EditRcaAppRequest.class);
            rcaPoolAppAPI.notifyAppPublishToOc(Lists.newArrayList(editRcaAppRequest.getId()));
        } catch (BusinessException ex) {
            LOGGER.error("应用变更发送推送到OC失败，ex: ", ex);
        }

        JSONObject customData = chunkUploadFile.getCustomData();
        RcaPoolAppDTO poolAppDTO = rcaPoolAppAPI.getDetail(UUID.fromString(customData.getString("id")));
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolAppDTO.getPoolId());
        optLogRecorder.saveOptLog(RcaBusinessKey.RCDC_RCA_POOL_APP_EDIT_SUCCESS, appPoolBaseDTO.getName(), poolAppDTO.getName());
        return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_EDIT_SUCCESS,
                new String[]{appPoolBaseDTO.getName(), poolAppDTO.getName()});
    }

    /**
     * 批量发布应用
     *
     * @param request 应用id数组
     * @param builder 批任务
     * @return 批任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("发布应用")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"发布应用"})})
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public DefaultWebResponse publishApp(IdArrWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(idArr)
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_PUBLISH_APP_TASK_ITEM, new String[]{}).build()).iterator();

        RcaPoolAppPublishAppBatchTaskHandler handler =
                new RcaPoolAppPublishAppBatchTaskHandler(iterator, auditLogAPI, rcaPoolAppAPI, rcaAppPoolAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_PUBLISH_APP_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_PUBLISH_APP_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 批量下架应用
     *
     * @param request 应用id数组
     * @param builder 批任务
     * @return 批任务信息
     * @throws BusinessException 业务异常
     */
    @ApiOperation("下架应用")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"下架应用"})})
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public DefaultWebResponse withdrawApp(IdArrWebRequest request, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(builder, "builder can not be null");

        UUID[] idArr = request.getIdArr();
        Iterator<DefaultBatchTaskItem> iterator =
                Arrays.stream(idArr)
                        .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                                .itemName(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_ITEM, new String[]{}).build()).iterator();

        RcaPoolAppWithdrawAppBatchTaskHandler handler =
                new RcaPoolAppWithdrawAppBatchTaskHandler(iterator, auditLogAPI, rcaPoolAppAPI, rcaAppPoolAPI);
        BatchTaskSubmitResult result = builder.setTaskName(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_NAME)
                .setTaskDesc(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_DESC).registerHandler(handler).start();
        return DefaultWebResponse.Builder.success(result);
    }

    /**
     * 复制应用
     * @param request 复制应用请求
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("复制应用")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"复制应用"})})
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse copy(CopyRcaAppWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        CopyRcaAppRequest copyRcaAppRequest = new CopyRcaAppRequest();
        BeanUtils.copyProperties(request, copyRcaAppRequest);
        RcaPoolAppDTO copyPoolApp = rcaPoolAppAPI.copyPoolApp(copyRcaAppRequest);

        try {
            rcaPoolAppAPI.notifyAppPublishToOc(Lists.newArrayList(copyPoolApp.getId()));
        } catch (BusinessException ex) {
            LOGGER.error("应用变更发送推送到OC失败，ex: ", ex);
        }

        RcaPoolAppDTO poolAppDTO = rcaPoolAppAPI.getDetail(request.getId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolAppDTO.getPoolId());
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_POOL_APP_COPY_SUCCESS, appPoolBaseDTO.getName(), request.getName());
        return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_COPY_SUCCESS,
                new String[]{appPoolBaseDTO.getName(), request.getName()});
    }

    /**
     * 复制应用(包含图标)
     * @param chunkUploadFile file
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("复制应用(包含图标)")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"复制应用(包含图标)"})})
    @RequestMapping(value = "/copyWithIcon", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse copyWithIcon(ChunkUploadFile chunkUploadFile) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile can not be null");

        RcaPoolAppDTO copyPoolApp = rcaPoolAppAPI.copyPoolAppWithPic(chunkUploadFile);
        try {
            rcaPoolAppAPI.notifyAppPublishToOc(Lists.newArrayList(copyPoolApp.getId()));
        } catch (BusinessException ex) {
            LOGGER.error("应用变更发送推送到OC失败，ex: ", ex);
        }

        JSONObject customData = chunkUploadFile.getCustomData();
        RcaPoolAppDTO poolAppDTO = rcaPoolAppAPI.getDetail(UUID.fromString(customData.getString("id")));
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolAppDTO.getPoolId());
        auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_POOL_APP_COPY_SUCCESS, appPoolBaseDTO.getName(), poolAppDTO.getName());
        return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_COPY_SUCCESS,
                new String[]{appPoolBaseDTO.getName(), poolAppDTO.getName()});
    }

    /**
     * 删除应用
     * @param request 复制应用请求
     * @return res
     * @throws BusinessException ex
     */
    @ApiOperation("删除应用")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"删除应用"})})
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse delete(DeleteRcaAppWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");


        DeleteRcaAppRequest deleteRcaAppRequest = new DeleteRcaAppRequest();
        BeanUtils.copyProperties(request, deleteRcaAppRequest);
        rcaPoolAppAPI.deleteGroupApp(deleteRcaAppRequest);

        try {
            rcaPoolAppAPI.notifyAppPublishToOc(deleteRcaAppRequest.getAppIdList());
        } catch (BusinessException ex) {
            LOGGER.error("应用变更发送推送到OC失败，ex: ", ex);
        }

        RcaGroupDTO groupDTO = rcaAppGroupAPI.getGroupDTO(request.getGroupId());
        RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(groupDTO.getPoolId());
        List<RcaPoolAppDTO> rcaPoolAppDTOList = rcaPoolAppAPI.findAllByIdIn(request.getAppIdList());
        for (RcaPoolAppDTO rcaPoolAppDTO : rcaPoolAppDTOList) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_POOL_APP_DELETE_SUCCESS_ITEM, appPoolBaseDTO.getName(),
                    groupDTO.getName(), rcaPoolAppDTO.getName());
        }
        return DefaultWebResponse.Builder.success(RcaBusinessKey.RCDC_RCA_POOL_APP_DELETE_SUCCESS,
                new String[]{appPoolBaseDTO.getName(), groupDTO.getName()});
    }

}
