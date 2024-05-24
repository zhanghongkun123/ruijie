package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupDetailAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbCloudPlatformRecoverAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVswitchDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.NetworkType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.NetworkMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.externalstorage.ExternalStorageDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.network.VswitchDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.network.VswitchListDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.RecoverCloudPlatformResourceSingleTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask.RecoverNetworkStrategyBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.CloudPlatformResourceRecoverRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.NetworkStrategyRecoverRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request.PlatformIdRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.INT_1000;
import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey.*;

/**
 * Description: 云平台恢复Controller
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月25日
 *
 * @author lanzf
 */
@Api(tags = "云平台恢复")
@Controller
@RequestMapping("/rco/cloudPlatform")
public class CloudPlatformRecoverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudPlatformRecoverController.class);

    private static final String CLOUD_PLATFORM_RECOVER_TASK_ID_NAME = "CLOUD_PLATFORM_RECOVER_TASK_ID_NAME";

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private NetworkMgmtAPI networkMgmtAPI;

    @Autowired
    private CbbCloudPlatformRecoverAPI cbbCloudPlatformRecoverAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private ExternalStorageMgmtAPI externalStorageMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory factory;

    @Autowired
    private CbbBackupDetailAPI cbbBackupDetailAPI;

    /**
     * 待恢复的网络交换机列表
     *
     * @param platformIdRequest platformIdRequest
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "查询恢复的网络交换机列表")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"查询恢复的网络交换机列表"})})
    @RequestMapping(value = "/networkStrategy/recover/vswitch", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse listVswitch(PlatformIdRequest platformIdRequest) throws BusinessException {
        Assert.notNull(platformIdRequest, "idWebRequest");

        final Map<UUID, CbbVswitchDTO> vswitchIdMap = cbbNetworkMgmtAPI.getNetworkRelateVswitchByPlatformId(platformIdRequest.getPlatformId());

        final com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest commonPageQueryRequest = new CommonPageQueryRequest();
        commonPageQueryRequest.setPage(0);
        commonPageQueryRequest.setLimit(INT_1000);
        final PageResponse<ExternalStorageDTO> externalStorageDTOPageResponse = externalStorageMgmtAPI.listExternalStorageInfo(commonPageQueryRequest);
        final List<ExternalStorageDTO> externalList = Arrays.stream(externalStorageDTOPageResponse.getItems())
                .filter(dto -> dto.getState() == ExternalStorageHealthStateEnum.HEALTHY).collect(Collectors.toList());
        final List<UUID> vswitchIdList = Lists.newArrayList(vswitchIdMap.keySet());
        for (ExternalStorageDTO externalStorageDTO : externalList) {
            if (vswitchIdList.isEmpty()) {
                break;
            }

            final List<VswitchListDTO> vswitchBackupList = getVswitchListDTOS(vswitchIdList, externalStorageDTO);
            vswitchBackupList.forEach(item -> {
                if (vswitchIdMap.containsKey(item.getId())) {
                    final CbbVswitchDTO vswitchDTO = vswitchIdMap.get(item.getId());
                    if (null == vswitchDTO) {
                        LOGGER.info("不存在交换机ID[{}]信息", item.getId());
                        return;
                    }

                    vswitchDTO.setName(item.getName());
                    vswitchIdList.remove(item.getId());
                }
            });
        }

        final PageQueryResponse<Object> response = new PageQueryResponse<>();
        response.setTotal(vswitchIdMap.size());
        response.setItemArr(vswitchIdMap.values().toArray(new CbbVswitchDTO[0]));
        return DefaultWebResponse.Builder.success(response);
    }

    private List<VswitchListDTO> getVswitchListDTOS(List<UUID> vswitchIdList, ExternalStorageDTO externalStorageDTO) {
        try {
            return cbbBackupDetailAPI.findVswitchBackupInfo(externalStorageDTO.getExtStorageId(), vswitchIdList);
        } catch (Exception e) {
            LOGGER.error("查询外置存储的交换机失败，失败原因：", e);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 网络策略恢复
     *
     * @param request 请求参数
     * @param builder 批任务
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "网络策略恢复")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"网络策略恢复"})})
    @RequestMapping(value = "/networkStrategy/recover", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse recoverNetworkStrategy(NetworkStrategyRecoverRequest request, BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "networkStrategyRecoverRequest参数不能为空");
        Assert.notNull(builder, "builder can not be null");

        cloudPlatformManageAPI.getInfoById(request.getPlatformId());

        final List<CbbDeskNetworkDTO> cbbDeskNetworkDTOList = cbbNetworkMgmtAPI.getByVswitchId(request.getVswitchId());
        final VswitchDTO vswitchDTO = matchVswtichNetworkStrategy(cbbDeskNetworkDTOList, request.getPlatformId());
        request.setVswitchId(vswitchDTO.getId());

        String itemName = LocaleI18nResolver.resolve(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_ITEM_NAME);
        final List<DefaultBatchTaskItem> itemList = cbbDeskNetworkDTOList.stream() //
                .map(item -> new DefaultBatchTaskItem(item.getId(), itemName)).collect(Collectors.toList());
        final RecoverNetworkStrategyBatchTaskHandler batchTaskHandler = new RecoverNetworkStrategyBatchTaskHandler(itemList.iterator(),
                auditLogAPI, cbbNetworkMgmtAPI, cbbCloudPlatformRecoverAPI, request);

        final BatchTaskSubmitResult result = builder.setTaskName(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_TASK_NAME)
                // 同一时间只有一个恢复任务在进行
                .setUniqueId(UUID.nameUUIDFromBytes(CLOUD_PLATFORM_RECOVER_TASK_ID_NAME.getBytes(StandardCharsets.UTF_8)))
                .setTaskDesc(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_TASK_DESC)
                .registerHandler(batchTaskHandler)
                .start();

        return DefaultWebResponse.Builder.success(result);
    }

    private VswitchDTO matchVswtichNetworkStrategy(List<CbbDeskNetworkDTO> networkStrategyEntryList, UUID platformId) throws BusinessException {
        if (networkStrategyEntryList.isEmpty()) {
            throw new BusinessException(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_BIND_DIFF_VSWITCH);
        }

        final CbbDeskNetworkDTO cbbDeskNetworkDTO = networkStrategyEntryList.get(0);
        final List<VswitchDTO> vswitchDTOList = networkMgmtAPI.queryVswitchs(platformId, null).getDto();
        if (CollectionUtils.isEmpty(vswitchDTOList)) {
            throw generateBusinessException(cbbDeskNetworkDTO);
        }

        final Optional<VswitchDTO> optional = vswitchDTOList.stream().filter(item -> isMatch(cbbDeskNetworkDTO, item)).findFirst();
        return optional.orElseThrow(() -> generateBusinessException(cbbDeskNetworkDTO));

    }

    private boolean isMatch(CbbDeskNetworkDTO cbbDeskNetworkDTO, VswitchDTO item) {
        if (!Objects.equals(item.getPlatformId(), cbbDeskNetworkDTO.getPlatformId())) {
            return false;
        }

        if (cbbDeskNetworkDTO.getNetworkType() == NetworkType.FLAT) {
            return Objects.equals(String.valueOf(cbbDeskNetworkDTO.getNetworkType()), String.valueOf(item.getNetworkType()));
        }

        return Objects.equals(String.valueOf(cbbDeskNetworkDTO.getNetworkType()), String.valueOf(item.getNetworkType())) &&
                        Objects.equals(String.valueOf(cbbDeskNetworkDTO.getVlan()), String.valueOf(item.getVlan()));
    }

    private BusinessException generateBusinessException(CbbDeskNetworkDTO cbbDeskNetworkDTO) {
        if (cbbDeskNetworkDTO.getNetworkType() == NetworkType.FLAT) {
            return new BusinessException(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_NOT_MATCH_FLAT_VSWITCH, String.valueOf(cbbDeskNetworkDTO.getNetworkType()));
        }

        return new BusinessException(RCDC_CLOUD_PLATFORM_RECOVER_NETWORK_BIND_DIFF_PLATFORM, String.valueOf(cbbDeskNetworkDTO.getNetworkType()), String.valueOf(cbbDeskNetworkDTO.getVlan()));
    }

    /**
     * 云平台资源关系恢复
     *
     * @param request 请求参数
     * @param builder 批任务builder
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "云平台资源关系恢复")
    @ApiVersions({@ApiVersion(value = Version.V6_0_50, descriptions = {"云平台资源关系恢复"})})
    @RequestMapping(value = "/resource/recover", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse recoverPlatformResource(CloudPlatformResourceRecoverRequest request, //
                                                      BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "cloundPlatformResourceRecoverRequest参数不能为空");
        Assert.notNull(builder, "builder can not be null");

        checkCloudPlatformExist(request);

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(request.getFromPlatformEntry().getId(),
                LocaleI18nResolver.resolve(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_ITEM_NAME));

        final RecoverCloudPlatformResourceSingleTaskHandler batchTaskTaskHandler = new RecoverCloudPlatformResourceSingleTaskHandler(batchTaskItem,
                auditLogAPI, request, cbbCloudPlatformRecoverAPI, cloudPlatformManageAPI);
        final BatchTaskSubmitResult result = builder.setTaskName(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_TASK_NAME)
                .setUniqueId(UUID.nameUUIDFromBytes(CLOUD_PLATFORM_RECOVER_TASK_ID_NAME.getBytes(StandardCharsets.UTF_8)))
                .setTaskDesc(RCDC_CLOUD_PLATFORM_RECOVER_RESOURCE_RELATIONSHIP_TASK_DESC)
                .registerHandler(batchTaskTaskHandler)
                .start();
        return DefaultWebResponse.Builder.success(result);
    }

    private void checkCloudPlatformExist(CloudPlatformResourceRecoverRequest request) throws BusinessException {
        cloudPlatformManageAPI.getInfoById(request.getFromPlatformEntry().getId());
    }

}

