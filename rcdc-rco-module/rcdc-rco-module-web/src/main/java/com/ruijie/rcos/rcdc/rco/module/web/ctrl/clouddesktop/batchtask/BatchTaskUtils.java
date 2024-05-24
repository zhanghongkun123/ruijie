package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/14
 *
 * @author Jarman
 */
public class BatchTaskUtils {

    private BatchTaskUtils() {
        throw new IllegalStateException("BatchTaskUtils Utility class");
    }

    /**
     * 构建批量任务处理结果对象
     * 
     * @param successCount 成功处理任务个数
     * @param failCount 失败处理任务个数
     * @param businessKey 国际化业务key
     * @return 返回结果
     */
    public static BatchTaskFinishResult buildBatchTaskFinishResult(int successCount, int failCount, String businessKey) {
        Assert.hasText(businessKey, "businessKey不能为空");
        final String[] strArr = new String[] {String.valueOf(successCount), String.valueOf(failCount)};
        BatchTaskStatus batchTaskStatus;
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().msgKey(businessKey).msgArgs(strArr).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        }

        if (successCount == 0) {
            batchTaskStatus = BatchTaskStatus.FAILURE;
        } else {
            batchTaskStatus = BatchTaskStatus.PARTIAL_SUCCESS;
        }
        return DefaultBatchTaskFinishResult.builder().msgKey(businessKey).msgArgs(strArr).batchTaskStatus(batchTaskStatus).build();
    }
}
