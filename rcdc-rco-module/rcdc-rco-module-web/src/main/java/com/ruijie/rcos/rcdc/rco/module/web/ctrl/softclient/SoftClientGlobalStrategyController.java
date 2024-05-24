package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softclient;

import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbSoftClientGlobalStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.softclient.CbbSoftClientGlobalStrategyWithSlideDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.SlideShowInfoAPI;
import com.ruijie.rcos.rcdc.rca.module.def.request.SingleSlideInfoDTORequest;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.CommonUpgradeBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softclient.handler.SoftClientGlobalStrategyTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/11 14:08
 *
 * @author zhangsiming
 */
@Api(tags = "软终端全局策略管理")
@Controller
@RequestMapping("/rco/softClient/globalStrategy")
public class SoftClientGlobalStrategyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SoftClientGlobalStrategyController.class);

    @Autowired
    private CbbSoftClientGlobalStrategyAPI cbbSoftClientGlobalStrategyAPI;

    @Autowired
    private SlideShowInfoAPI slideShowInfoAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RcaNotifyAPI rcaNotifyAPI;

    @Autowired
    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    private static UUID SOFT_CLIENT_GLOBAL_STRATEGY_TASK_ID = UUID.fromString("801b41ea-59b5-4cb0-af2d-0f669b730818");

    /**
     * 获取软终端全局配置
     *
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException businessException
     */
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @ApiOperation(value = "获取软终端全局配置")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"获取软终端全局配置"})})
    public DefaultWebResponse getStrategyDetail() throws BusinessException {

        CbbSoftClientGlobalStrategyDTO globalStrategyDTO = cbbSoftClientGlobalStrategyAPI.getGlobalStrategy();
        CbbSoftClientGlobalStrategyWithSlideDTO withSlideDTO = new CbbSoftClientGlobalStrategyWithSlideDTO();
        withSlideDTO.setSoftClientGlobalConfig(globalStrategyDTO);
        withSlideDTO.setSlideShowInfo(slideShowInfoAPI.getSlideConfigDTO());
        return DefaultWebResponse.Builder.success(withSlideDTO);
    }

    /**
     * @param slideShowInfo 上传单张轮播图的信息
     * @param taskBuilder BatchTaskBuilder
     * @return 更新结果
     * @throws BusinessException businessException
     */
    @RequestMapping(value = "/add/singleSlideCache", method = RequestMethod.POST)
    @ApiOperation(value = "上传单张轮播图的信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"上传单张轮播图的信息"})})
    public DefaultWebResponse addSingleSlideCache(SingleSlideInfoDTORequest slideShowInfo
            , BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(slideShowInfo, "slideShowInfo can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        // 生成UUID并返回给前端
        UUID picId = UUID.randomUUID();
        slideShowInfo.setPictureId(picId);
        Assert.notNull(slideShowInfo.getPictureData(), "轮播图信息不能为空");
        slideShowInfoAPI.addSingleSlideCache(slideShowInfo);

        return DefaultWebResponse.Builder.success(slideShowInfo);
    }

    /**
     * 移除单张轮播图的信息
     * @param slideShowInfo 要移除的id
     * @param taskBuilder BatchTaskBuilder
     * @return 更新结果
     * @throws BusinessException businessException
     */
    @RequestMapping(value = "/remove/singleSlideCache", method = RequestMethod.POST)
    @ApiOperation(value = "移除单张轮播图的信息")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"移除单张轮播图的信息"})})
    public DefaultWebResponse removeSingleSlideCache(SingleSlideInfoDTORequest slideShowInfo
            , BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(slideShowInfo, "slideShowInfo can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        Assert.notNull(slideShowInfo.getPictureId(), "轮播图标识不能为空");
        slideShowInfoAPI.removeSingleSlideCache(slideShowInfo.getPictureId());
        return DefaultWebResponse.Builder.success();
    }

    /**
     * @param softClientGlobalStrategyWithSlideDTO 软终端全局策略
     * @param taskBuilder BatchTaskBuilder
     * @return 更新结果
     * @throws BusinessException businessException
     */
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑软终端全局配置")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑软终端全局配置"})})
    public CommonWebResponse<BatchTaskSubmitResult> edit(CbbSoftClientGlobalStrategyWithSlideDTO softClientGlobalStrategyWithSlideDTO,
                                                         BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(softClientGlobalStrategyWithSlideDTO, "softClientGlobalStrategyWithSlideDTO can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(SOFT_CLIENT_GLOBAL_STRATEGY_TASK_ID)
                .itemName(LocaleI18nResolver.resolve(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_ITEM_NAME))
                .build();

        SoftClientGlobalStrategyTaskHandler batchTaskHandler =
                new SoftClientGlobalStrategyTaskHandler(softClientGlobalStrategyWithSlideDTO, taskItem,
                        cbbSoftClientGlobalStrategyAPI, auditLogAPI, slideShowInfoAPI);
        batchTaskHandler.setRcaNotifyAPI(rcaNotifyAPI);
        batchTaskHandler.setBaseApplicationPacketAPI(baseApplicationPacketAPI);

        BatchTaskSubmitResult result = taskBuilder.setTaskName(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_TASK_NAME)
                .setTaskDesc(CommonUpgradeBusinessKey.RCDC_SOFT_CLIENT_GLOBAL_STRATEGY_TASK_DESC)
                .registerHandler(batchTaskHandler).setUniqueId(SOFT_CLIENT_GLOBAL_STRATEGY_TASK_ID)
                .start();
        return CommonWebResponse.success(result);
    }

}
