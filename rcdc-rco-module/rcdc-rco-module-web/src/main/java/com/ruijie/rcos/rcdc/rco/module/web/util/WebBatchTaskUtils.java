package com.ruijie.rcos.rcdc.rco.module.web.util;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月15日
 *
 * @author 徐国祥
 */
public class WebBatchTaskUtils {

    /**
     * 获取资源删除前缀
     *
     * @param idArr id列表
     * @param shouldOnlyDeleteDataFromDb 是否强制从数据库删除
     * @return 前缀
     */
    public static String getDeletePrefix(UUID[] idArr, @Nullable Boolean shouldOnlyDeleteDataFromDb) {
        Assert.notEmpty(idArr, "idArr can not be null");

        return getDeletePrefix(idArr) + getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    /**
     * 获取资源删除前缀
     *
     * @param idArr id列表
     * @return 前缀
     */
    public static String getDeletePrefix(UUID[] idArr) {
        Assert.notEmpty(idArr, "idArr can not be null");
        return idArr.length > 1 ? LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_BATCH_OPERATE) : StringUtils.EMPTY;
    }

    /**
     * 获取资源删除前缀
     *
     * @param shouldOnlyDeleteDataFromDb 是否强制从数据库删除
     * @return 前缀
     */
    public static String getDeletePrefix(@Nullable Boolean shouldOnlyDeleteDataFromDb) {
        return Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb) ? LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_FORCE_DELETE)
                : StringUtils.EMPTY;
    }


    /**
     * 提供默认实现
     *
     * @param successCount 成功数量
     * @param failCount 失败数量
     * @param businessKey 业务Key
     * @param extendArs 扩展参数
     * @return 默认实现
     */
    public static BatchTaskFinishResult buildDefaultFinishResult(int successCount, int failCount, String businessKey,
                                                                 @Nullable String... extendArs) {
        Assert.notNull(businessKey, "businessKey can not be null");

        final String[] strArr = new String[] {String.valueOf(successCount), String.valueOf(failCount)};
        String[] argArr = ArrayUtils.addAll(strArr, extendArs);
        BatchTaskStatus batchTaskStatus;
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder() //
                    .msgKey(businessKey) //
                    .msgArgs(argArr) //
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        }
        if (successCount == 0) {
            batchTaskStatus = BatchTaskStatus.FAILURE;
        } else {
            batchTaskStatus = BatchTaskStatus.PARTIAL_SUCCESS;
        }
        return DefaultBatchTaskFinishResult.builder() //
                .msgKey(businessKey) //
                .msgArgs(argArr) //
                .batchTaskStatus(batchTaskStatus) //
                .build();
    }
}
