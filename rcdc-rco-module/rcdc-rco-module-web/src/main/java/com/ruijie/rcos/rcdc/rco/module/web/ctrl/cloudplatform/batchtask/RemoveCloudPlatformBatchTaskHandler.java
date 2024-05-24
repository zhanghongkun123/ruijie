package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbCloudPlatformInnerDataAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageSyncMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkBasicInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.ViewCbbImageSyncRecordDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageSyncTaskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cloudplatform.CloudPlatformBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description: 删除云平台批处理任务
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/8
 *
 * @author WuShengQiang
 */
public class RemoveCloudPlatformBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveCloudPlatformBatchTaskHandler.class);

    private static final String TARGET_PLATFORM_ID_FIELD_NAME = "targetPlatformId";

    private static final String TASK_STATE_FIELD_NAME = "taskState";

    private String name = "";

    private CloudPlatformManageAPI cloudPlatformManageAPI;

    private BaseAuditLogAPI auditLogAPI;

    private PageQueryBuilderFactory queryBuilderFactory;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CbbCloudPlatformInnerDataAPI cbbCloudPlatformInnerDataAPI;

    private UserDesktopConfigAPI userDesktopConfigAPI;

    private CbbImageSyncMgmtAPI cbbImageSyncMgmtAPI;


    public RemoveCloudPlatformBatchTaskHandler(BatchTaskItem batchTaskItem) {
        super(batchTaskItem);
        this.cloudPlatformManageAPI = SpringBeanHelper.getBean(CloudPlatformManageAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.queryBuilderFactory = SpringBeanHelper.getBean(PageQueryBuilderFactory.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.diskPoolMgmtAPI = SpringBeanHelper.getBean(DiskPoolMgmtAPI.class);
        this.cbbNetworkMgmtAPI = SpringBeanHelper.getBean(CbbNetworkMgmtAPI.class);
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.cbbAppSoftwarePackageMgmtAPI = SpringBeanHelper.getBean(CbbAppSoftwarePackageMgmtAPI.class);
        this.appDeliveryMgmtAPI = SpringBeanHelper.getBean(AppDeliveryMgmtAPI.class);
        this.cbbImageTemplateMgmtAPI  = SpringBeanHelper.getBean(CbbImageTemplateMgmtAPI.class);
        this.cbbCloudPlatformInnerDataAPI = SpringBeanHelper.getBean(CbbCloudPlatformInnerDataAPI.class);
        this.userDesktopConfigAPI = SpringBeanHelper.getBean(UserDesktopConfigAPI.class);
        this.cbbImageSyncMgmtAPI = SpringBeanHelper.getBean(CbbImageSyncMgmtAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        name = batchTaskItem.getItemName();
        UUID id = batchTaskItem.getItemID();
        try {
            CloudPlatformDTO cloudPlatform = cloudPlatformManageAPI.getInfoById(id);
            if (cloudPlatform.getShouldDefault()) {
                throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_NOT_SUPPORT_REMOVE);
            }

            // 非云平台离线状态，校验是否存在内置模板创建任务
            boolean shouldWaitFinishCreateInnerTemplateTaskFinish = cbbCloudPlatformInnerDataAPI.shouldWaitCreateInnerTemplateTaskFinish(id);
            if (shouldWaitFinishCreateInnerTemplateTaskFinish) {
                throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_INNER_TEMPLATE_CREATE_TASK);
            }

            // 云平台在线则判断是否存在数据
            isExistData(id);

            // 校验是否存在镜像同步任务
            checkExistImageSyncTask(id);

            checkUserGroupBindPlatform(cloudPlatform);

            cloudPlatformManageAPI.remove(batchTaskItem.getItemID());
            auditLogAPI.recordLog(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_TASK_SUCCESS, name);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_TASK_SUCCESS).msgArgs(name).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_FAIL, e, name, e.getI18nMessage());
            throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_FAIL, e, name, e.getI18nMessage());
        }
    }


    private void isExistData(UUID id) throws BusinessException {
        List<String> valueList = new ArrayList<>();
        if (isExistDeskPool(id)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_DESK_POOL));
        }
        if (isExistDiskPool(id)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_DISK_POOL));
        }
        if (isExistDesk(id, ImageUsageTypeEnum.DESK)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_DESK));
        }
        if (isExistDesk(id, ImageUsageTypeEnum.APP)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_RCA));
        }
        if (isExistUamAppDisk(id)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_APP_DISK));
        }
        if (isExistImageTemplate(id)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_IMAGE));
        }
        if (isExistNetwork(id)) {
            valueList.add(LocaleI18nResolver.resolve(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_NETWORK));
        }

        if (CollectionUtils.isEmpty(valueList)) {
            return;
        }

        throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_DATA, StringUtils.join(valueList, Constants.CAESURA));
    }

    private void checkExistImageSyncTask(UUID id) throws BusinessException {
        PageQueryRequest pageQueryRequest = queryBuilderFactory.newRequestBuilder() //
                .eq(TARGET_PLATFORM_ID_FIELD_NAME, id) //
                .in(TASK_STATE_FIELD_NAME, new ImageSyncTaskState[]{ImageSyncTaskState.RUNNING, ImageSyncTaskState.WAITING}) //
                .build();
        PageQueryResponse<ViewCbbImageSyncRecordDTO> imageSyncRecordPageResponse = cbbImageSyncMgmtAPI.pageQuery(pageQueryRequest);
        // 查询是否存在镜像同步任务
        if (imageSyncRecordPageResponse.getTotal() > 0) {
            throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_EXIST_SYNC_TASK);
        }
    }

    private boolean isExistDesk(UUID platformId, ImageUsageTypeEnum imageUsage) throws BusinessException {
        // 根据平台ID和用途判断云桌面或应用主机
        PageQueryRequest request = queryBuilderFactory.newRequestBuilder().eq(Constants.QUERY_PLATFORM_ID, platformId)
                .eq(Constants.QUERY_IMAGE_USAGE, imageUsage).build();
        DefaultPageResponse<CloudDesktopDTO> deskResponse = userDesktopMgmtAPI.pageQuery(request);
        return deskResponse.getTotal() > 0;
    }

    private boolean isExistDiskPool(UUID platformId) throws BusinessException {
        // 根据平台ID和池类型判断磁盘池
        PageQueryRequest request = queryBuilderFactory.newRequestBuilder().eq(Constants.QUERY_PLATFORM_ID, platformId)
                .eq(Constants.QUERY_DISK_POOL_TYPE, DiskPoolType.POOL).build();
        PageQueryResponse<UserDiskDetailDTO> diskResponse = diskPoolMgmtAPI.pagePoolDiskUser(request);
        return diskResponse.getTotal() > 0;
    }

    private boolean isExistDeskPool(UUID platformId) throws BusinessException {
        ConditionQueryRequestBuilder conditionQueryRequestBuilder = new DefaultConditionQueryRequestBuilder();
        ConditionQueryRequest conditionQueryRequest = conditionQueryRequestBuilder.eq(Constants.QUERY_PLATFORM_ID, platformId).build();
        return cbbDesktopPoolMgmtAPI.countByConditions(conditionQueryRequest) > 0L;
    }

    private boolean isExistUamAppDisk(UUID platformId) throws BusinessException {
        ConditionQueryRequestBuilder conditionQueryRequestBuilder = new DefaultConditionQueryRequestBuilder();
        ConditionQueryRequest conditionQueryRequest = conditionQueryRequestBuilder.eq(Constants.QUERY_PLATFORM_ID, platformId).build();
        return appDeliveryMgmtAPI.countByConditions(conditionQueryRequest) > 0L;
    }

    private boolean isExistImageTemplate(UUID platformId) throws BusinessException {
        ConditionQueryRequestBuilder conditionQueryRequestBuilder = new DefaultConditionQueryRequestBuilder();
        ConditionQueryRequest conditionQueryRequest = conditionQueryRequestBuilder.eq(Constants.QUERY_PLATFORM_ID, platformId).build();
        return cbbImageTemplateMgmtAPI.countByConditions(conditionQueryRequest) > 0L;
    }

    private boolean isExistNetwork(UUID platformId) throws BusinessException {
        // 根据平台ID和池类型判断磁盘池
        PageQueryRequest request = queryBuilderFactory.newRequestBuilder().eq(Constants.QUERY_PLATFORM_ID, platformId).build();
        PageQueryResponse<CbbDeskNetworkBasicInfoDTO> networkResponse = cbbNetworkMgmtAPI.pageQuery(request);
        return networkResponse.getTotal() > 0;
    }

    private void checkUserGroupBindPlatform(CloudPlatformDTO cloudPlatformDTO) throws BusinessException {
        UUID platformId = cloudPlatformDTO.getId();
        PageQueryRequest request = queryBuilderFactory.newRequestBuilder().eq(Constants.QUERY_PLATFORM_ID, platformId).build();
        PageQueryResponse<UserGroupDesktopConfigDTO> userGroupDesktopConfigDTOPageQueryResponse = userDesktopConfigAPI.pageQueryUserGroupDesktopConfigDTO(request);
        if (!ObjectUtils.isEmpty(userGroupDesktopConfigDTOPageQueryResponse.getItemArr())) {
            throw new BusinessException(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_BIND_USER_GROUP);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_TASK_SUCCESS).msgArgs(new String[]{name}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(CloudPlatformBusinessKey.RCDC_CLOUDPLATFORM_REMOVE_TASK_FAIL).msgArgs(new String[]{name}).build();
        }
    }

}
