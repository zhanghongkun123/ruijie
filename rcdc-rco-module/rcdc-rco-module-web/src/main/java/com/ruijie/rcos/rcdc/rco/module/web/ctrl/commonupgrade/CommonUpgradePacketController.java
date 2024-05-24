package com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.ruijie.rcos.base.upgrade.module.def.enums.UpgradeRange;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.BaseApplicationPacketAPI;
import com.ruijie.rcos.base.upgrade.module.def.api.request.ApplicationPacketUploadCheckRequest;
import com.ruijie.rcos.base.upgrade.module.def.api.response.ApplicationPacketUploadCheckResponse;
import com.ruijie.rcos.base.upgrade.module.def.dto.AppPacketConfigDTO;
import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationPacketDTO;
import com.ruijie.rcos.base.upgrade.module.def.dto.ApplicationStorageCapacityDTO;
import com.ruijie.rcos.base.upgrade.module.def.dto.UpgradeTarget;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rco.module.common.enums.CommonUpgradeTargetType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.conversion.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.dto.RcoAppPacketConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.handler.PacketConfigTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.handler.PacketDeleteBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.handler.PacketUploadBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.commonupgrade.request.GetPacketRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: 升级包管理ctrl
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月21日
 *
 * @author chenl
 */
@Controller
@RequestMapping("/rco/app/packet")
public class CommonUpgradePacketController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUpgradePacketController.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private BaseApplicationPacketAPI baseApplicationPacketAPI;

    @Autowired
    private DeskIdLabelConversion deskIdLabelConversion;

    @Autowired
    private DeskPoolIdLabelConversion deskPoolIdLabelConversion;

    @Autowired
    private ImageIdLabelConversion imageIdLabelConversion;

    @Autowired
    private TerminalIdLabelConversion terminalIdLabelConversion;

    @Autowired
    private TerminalGroupIdLabelConversion terminalGroupIdLabelConversion;

    @Autowired
    private RcaPoolIdLabelConversion rcaPoolIdLabelConversion;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    @Autowired
    private RcaHostAPI rcaHostAPI;

    /**
     * 校验升级包是否允许上传接口
     *
     * @param webRequest 请求参数
     * @throws BusinessException 业务异常
     * @return 响应数据
     */
    @RequestMapping(value = "/checkUpload")
    public DefaultWebResponse checkUpload(ApplicationPacketUploadCheckRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        LOGGER.info("校验升级包是否允许上传接口：webRequest=[{}]", JSONObject.toJSONString(webRequest));

        ApplicationPacketUploadCheckResponse checkResponse = baseApplicationPacketAPI.checkUpload(webRequest);
        return DefaultWebResponse.Builder.success(checkResponse);
    }

    /**
     * 获取升级包可用存储容量接口
     * @throws BusinessException 业务异常
     * @return 响应数据
     */
    @RequestMapping(value = "/capacity")
    public DefaultWebResponse getCapacity() throws BusinessException {
        ApplicationStorageCapacityDTO capacity = baseApplicationPacketAPI.getCapacity();
        return DefaultWebResponse.Builder.success(capacity);
    }


    /**
     * 升级包上传接口
     * @param chunkUploadFile file信息
     * @param taskBuilder taskBuilder信息
     * @return 响应数据
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/upload")
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> packetUpload(ChunkUploadFile chunkUploadFile,
                                                                 BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(chunkUploadFile, "chunkUploadFile can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(UUID.randomUUID())
                .itemName(LocaleI18nResolver.resolve(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_ITEM_NAME))
                .build();

        PacketUploadBatchTaskHandler batchTaskHandler =
                new PacketUploadBatchTaskHandler(chunkUploadFile, taskItem, auditLogAPI, baseApplicationPacketAPI);

        BatchTaskSubmitResult result = taskBuilder.setTaskName(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_TASK_NAME)
                .setTaskDesc(CommonUpgradeBusinessKey.RCDC_PACKET_UPLOAD_TASK_DESC, chunkUploadFile.getFileName())
                .registerHandler(batchTaskHandler)
                .start();

        return CommonWebResponse.success(result);
    }


    /**
     * 升级包删除接口
     *
     * @param webRequest 请求参数
     * @param builder    taskBuilder信息
     * @throws BusinessException 业务异常
     * @return 响应数据
     */
    @RequestMapping(value = "/delete")
    public CommonWebResponse<BatchTaskSubmitResult> packetDelete(IdArrWebRequest webRequest, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(webRequest, "webRequest can not be null");
        Assert.notNull(builder, "taskBuilder can not be null");

        UUID[] idArr = webRequest.getIdArr();
        //批量删除
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(LocaleI18nResolver.resolve(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_ITEM_NAME)).build())
                .iterator();
        PacketDeleteBatchTaskHandler handler = new PacketDeleteBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setBaseApplicationPacketAPI(baseApplicationPacketAPI);
        BatchTaskSubmitResult result = builder.setTaskName(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_TASK_NAME)
                .setTaskDesc(CommonUpgradeBusinessKey.RCDC_PACKET_BATCH_DELETE_TASK_DESC).enableParallel().registerHandler(handler)
                .start();
        return CommonWebResponse.success(result);
    }

    /**
     * 获取升级包详情
     *
     * @param webRequest 请求参数
     * @return 应用详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取升级包详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public DefaultWebResponse getPacketDetail(GetPacketRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest cannot be null");

        ApplicationPacketDTO applicationPacketDTO = baseApplicationPacketAPI.detailPacket(webRequest.getId());
        return DefaultWebResponse.Builder.success(applicationPacketDTO);
    }


    /**
     * 查询升级包列表
     *
     * @param request 分页请求
     * @throws BusinessException 业务异常
     * @return 应用列表
     */
    @ApiOperation("查询升级包列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public DefaultWebResponse getPacketList(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request must not be null");

        PageQueryResponse<ApplicationPacketDTO> pageQueryResponse = baseApplicationPacketAPI.pagePacket(request);
        ApplicationPacketDTO[] itemArr = pageQueryResponse.getItemArr();
        Arrays.stream(itemArr).forEach(applicationPacketDTO -> {
            try {
                AppPacketConfigDTO appPacketConfigDTO = baseApplicationPacketAPI.detailPacketConfig(applicationPacketDTO.getId());
                int upGradeTargetCount = 0;
                if (UpgradeRange.ALL != appPacketConfigDTO.getUpgradeRange()) {
                    UpgradeTarget[] upgradeTargetArr = appPacketConfigDTO.getUpgradeTargetArr();
                    for (UpgradeTarget upgradeTarget : upgradeTargetArr) {
                        CommonUpgradeTargetType commonUpgradeTargetType = CommonUpgradeTargetType.convert(upgradeTarget.getType());
                        if (CommonUpgradeTargetType.DESKTOP_POOL == commonUpgradeTargetType) {
                            // 统计桌面池下的桌面
                            List<CbbDeskInfoDTO> cbbDeskInfoDTOList = cbbVDIDeskMgmtAPI.listDeskInfoByDesktopPoolId(UUID.fromString(upgradeTarget.getId()));
                            upGradeTargetCount = upGradeTargetCount + cbbDeskInfoDTOList.size();
                        } else if (CommonUpgradeTargetType.RCA_POOL == commonUpgradeTargetType) {
                            // 统计 rca池下的应用主机
                            List<RcaHostDTO> rcaHostDTOList = rcaHostAPI.findAllByPoolIdIn(Arrays.asList(UUID.fromString(upgradeTarget.getId())));
                            upGradeTargetCount = upGradeTargetCount + rcaHostDTOList.size();
                        } else {
                            upGradeTargetCount++;
                        }
                    }
                    applicationPacketDTO.setUpgradeTotalCount(upGradeTargetCount);
                }

            } catch (BusinessException e) {
                LOGGER.error("获取升级包[{}]配置信息报错", applicationPacketDTO.getId(), e );
            }
        });

        return DefaultWebResponse.Builder.success(pageQueryResponse);
    }


    /**
     * 保存升级策略接口
     *
     * @param request        入参
     * @param taskBuilder    BatchTaskBuilder
     * @return 返回值
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/updateConfig")
    @EnableAuthority
    public CommonWebResponse<BatchTaskSubmitResult> updatePacketConfig(RcoAppPacketConfigDTO request,
                                                                       BatchTaskBuilder taskBuilder) throws BusinessException {
        Assert.notNull(request, "chunkUploadFile can not be null");
        Assert.notNull(taskBuilder, "taskBuilder can not be null");

        DefaultBatchTaskItem taskItem = DefaultBatchTaskItem.builder().itemId(request.getId())
                .itemName(LocaleI18nResolver.resolve(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_ITEM_NAME))
                .build();

        ApplicationPacketDTO applicationPacketDTO = baseApplicationPacketAPI.detailPacket(request.getId());
        PacketConfigTaskHandler batchTaskHandler =
                new PacketConfigTaskHandler(request, applicationPacketDTO, taskItem, auditLogAPI, baseApplicationPacketAPI);

        BatchTaskSubmitResult result = taskBuilder.setTaskName(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_TASK_NAME, applicationPacketDTO.getName())
                .setTaskDesc(CommonUpgradeBusinessKey.RCDC_PACKET_CONFIG_TASK_DESC, applicationPacketDTO.getName())
                .registerHandler(batchTaskHandler)
                .start();
        return CommonWebResponse.success(result);
    }

    /**
     * 查询升级包配置接口
     *
     * @param request 请求参数
     * @return 升级包配置信息
     * @throws BusinessException 业务异常
     *
     */
    @RequestMapping(value = "/detailConfig")
    public CommonWebResponse<RcoAppPacketConfigDTO> detailPacketConfig(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        AppPacketConfigDTO packetConfigDTO = baseApplicationPacketAPI.detailPacketConfig(request.getId());

        RcoAppPacketConfigDTO rcoAppPacketConfigDTO = new RcoAppPacketConfigDTO();
        rcoAppPacketConfigDTO.setId(packetConfigDTO.getId());
        rcoAppPacketConfigDTO.setUpgradeMode(packetConfigDTO.getUpgradeMode());
        rcoAppPacketConfigDTO.setUpgradeRange(packetConfigDTO.getUpgradeRange());

        //todo 这里id转成idlabel需要重构下
        Map<String, List<UpgradeTarget>> upgradeTargetMap =
                Arrays.stream(packetConfigDTO.getUpgradeTargetArr()).collect(Collectors.groupingBy(UpgradeTarget::getType));

        UUID[] deskIdArr = upgradeTargetMap.getOrDefault(CommonUpgradeTargetType.DESKTOP.name(), Lists.newArrayList()).stream()
                .map(item -> UUID.fromString(item.getId()))
                .toArray(UUID[]::new);
        GenericIdLabelEntry<UUID>[] deskLabelArr = deskIdLabelConversion.convert(deskIdArr);
        rcoAppPacketConfigDTO.setDeskArr(deskLabelArr);

        UUID[] deskPoolIdArr = upgradeTargetMap.getOrDefault(CommonUpgradeTargetType.DESKTOP_POOL.name(), Lists.newArrayList()).stream()
                .map(item -> UUID.fromString(item.getId()))
                .toArray(UUID[]::new);
        GenericIdLabelEntry<UUID>[] deskPoolLabelArr = deskPoolIdLabelConversion.convert(deskPoolIdArr);
        rcoAppPacketConfigDTO.setDeskPoolArr(deskPoolLabelArr);

        UUID[] imageIdArr = upgradeTargetMap.getOrDefault(CommonUpgradeTargetType.IMAGE.name(), Lists.newArrayList())
                .stream().map(item -> UUID.fromString(item.getId()))
                .toArray(UUID[]::new);
        GenericIdLabelEntry<UUID>[] imageLabelArr = imageIdLabelConversion.convert(imageIdArr);
        rcoAppPacketConfigDTO.setImageArr(imageLabelArr);

        String[] terminalIdArr = upgradeTargetMap.getOrDefault(CommonUpgradeTargetType.TERMINAL.name(), Lists.newArrayList()).stream()
                .map(UpgradeTarget::getId)
                .toArray(String[]::new);
        GenericIdLabelEntry<String>[] terminalLabelArr = terminalIdLabelConversion.convert(terminalIdArr);
        rcoAppPacketConfigDTO.setTerminalArr(terminalLabelArr);

        UUID[] terminalGroupIdArr = upgradeTargetMap.getOrDefault(CommonUpgradeTargetType.TERMINAL_GROUP.name(), Lists.newArrayList()).stream()
                .map(item -> UUID.fromString(item.getId()))
                .toArray(UUID[]::new);
        GenericIdLabelEntry<UUID>[] terminalGroupLabelArr = terminalGroupIdLabelConversion.convert(terminalGroupIdArr);
        rcoAppPacketConfigDTO.setTerminalGroupArr(terminalGroupLabelArr);

        UUID[] rcaPoolIdArr = upgradeTargetMap.getOrDefault(CommonUpgradeTargetType.RCA_POOL.name(), Lists.newArrayList()).stream()
                .map(item -> UUID.fromString(item.getId()))
                .toArray(UUID[]::new);
        GenericIdLabelEntry<UUID>[] rcaPoolLabelArr = rcaPoolIdLabelConversion.convert(rcaPoolIdArr);
        rcoAppPacketConfigDTO.setRcaPoolArr(rcaPoolLabelArr);
        return CommonWebResponse.success(rcoAppPacketConfigDTO);
    }
}
