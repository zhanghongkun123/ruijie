package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaMainStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPeripheralStrategyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaPeripheralStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaStrategyRelationshipDTO;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.batch.I18nBatchTaskItem;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.batch.AlterRcaStrategyBatchHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.request.RcaAlterStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.request.RcaObjectStrategyDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.response.RcaObjectStrategyDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23 17:24
 *
 * @author zhangsiming
 */
@Api(tags = "云应用策略公共接口管理")
@Controller
@RequestMapping("/rca/strategy")
public class RcaStrategyCommonController {

    @Autowired
    private RcaMainStrategyAPI rcaMainStrategyAPI;

    @Autowired
    private RcaPeripheralStrategyAPI rcaPeripheralStrategyAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * @param rcaStrategyDetailRequest 策略请求
     * @return 策略信息
     * @throws BusinessException 异常
     */
    @ApiOperation("获取特定云应用参与对象（应用池/用户/AD域安全组）的策略详情")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取特定云应用参与对象（应用池/用户/AD域安全组）的策略详情"})})
    public CommonWebResponse<RcaObjectStrategyDetailResponse> detail(RcaObjectStrategyDetailRequest rcaStrategyDetailRequest)
            throws BusinessException {
        Assert.notNull(rcaStrategyDetailRequest, "rcaStrategyDetailRequest can not be null");
        RcaStrategyRelationshipDTO relationshipDTO = new RcaStrategyRelationshipDTO();
        BeanUtils.copyProperties(rcaStrategyDetailRequest, relationshipDTO);
        RcaMainStrategyDTO rcaMainStrategyDTO = rcaMainStrategyAPI.getStrategyConfigDetail(relationshipDTO);
        RcaPeripheralStrategyDTO rcaPeripheralStrategyDTO = rcaPeripheralStrategyAPI.getStrategyDetailBy(relationshipDTO);
        RcaObjectStrategyDetailResponse objectStrategyDetailResponse = new RcaObjectStrategyDetailResponse();
        objectStrategyDetailResponse.setRcaMainStrategy(rcaMainStrategyDTO);
        objectStrategyDetailResponse.setRcaPeripheralStrategy(rcaPeripheralStrategyDTO);
        return CommonWebResponse.success(objectStrategyDetailResponse);
    }

    /**
     * @param webRequest 请求
     * @param builder builder
     * @return 变更策略结果
     * @throws BusinessException 异常
     */
    @ApiOperation("变更云应用相关策略")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"变更云应用相关策略"})})
    @RequestMapping(value = "/alter", method = RequestMethod.POST)
    public CommonWebResponse alterStrategy(RcaAlterStrategyRequest webRequest, BatchTaskBuilder builder)
            throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null!");
        Assert.notNull(builder, "builder cannot be null!");

        RcaObjectStrategyDetailRequest[] applyObjectArr = webRequest.getApplyObjectArr();

        final Iterator<I18nBatchTaskItem<RcaObjectStrategyDetailRequest>> iterator = Stream.of(applyObjectArr).distinct() //
                //todo zsm需补上云应用策略名
                .map(applyObject -> I18nBatchTaskItem.Builder.build(UUID.randomUUID(),
                        RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_BATCH_ALTER_TASK_NAME, applyObject))
                .iterator();
        final AlterRcaStrategyBatchHandler handler = new AlterRcaStrategyBatchHandler(iterator, rcaMainStrategyAPI, rcaPeripheralStrategyAPI,
                webRequest.getMainStrategyId(), webRequest.getPeripheralStrategyId());
        BatchTaskSubmitResult result = I18nBatchTaskBuilder.wrap(builder)
                .setAuditLogAPI(auditLogAPI)
                .setTaskName(RcaBusinessKey.RCDC_RCA_MAIN_STRATEGY_BATCH_ALTER_TASK_NAME)
                .registerHandler(handler)
                .start();
        return CommonWebResponse.success(result);

    }



}
