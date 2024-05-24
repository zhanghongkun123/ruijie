package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage;

import com.google.common.collect.Lists;
import com.ruijie.rcos.sk.base.batch.*;
import org.junit.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 批量任务生成器测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月25日
 *
 * @author fyq
 */

public class BatchTaskBuilderTest implements BatchTaskBuilder {

    private BatchTaskHandler<BatchTaskItem> batchTaskHandler;

    private int successCount = 0;

    private int failCount = 0;

    private List<String> resultList;

    @Override
    public BatchTaskBuilder setTaskName(String s, String... strings) {
        return this;
    }

    @Override
    public BatchTaskBuilder setTaskDesc(String s, String... strings) {
        return this;
    }

    @Override
    public BatchTaskBuilder enableProgressBar(int i) {
        return this;
    }

    @Override
    public BatchTaskBuilder registerHandler(BatchTaskHandler<BatchTaskItem> batchTaskHandler) {
        this.batchTaskHandler = batchTaskHandler;
        return this;
    }

    @Override
    public BatchTaskBuilder enableParallel() {
        return this;
    }

    @Override
    public BatchTaskBuilder enablePerformanceMode(UUID taskPoolId) {
        //
        return null;
    }

    @Override
    public BatchTaskBuilder enablePerformanceMode(UUID taskPoolId, int threadNum) {
        //
        return null;
    }

    @Override
    public BatchTaskBuilder setUniqueId(UUID uuid) {
        //
        return null;
    }

    @Override
    public BatchTaskSubmitResult start() {

        int successCount = 0;
        int failCount = 0;
        List<String> resultList = Lists.newArrayList();

        while (batchTaskHandler.hasNext()) {
            BatchTaskItem batchTaskItem = batchTaskHandler.next();
            try {
                BatchTaskItemResult batchTaskItemResult = batchTaskHandler.processItem(batchTaskItem);
                resultList.add(batchTaskItemResult.getMsgKey());
                successCount++;
            } catch (Exception e) {
                batchTaskHandler.afterException(batchTaskItem, e);
                resultList.add(e.getMessage());
                failCount++;
            }
        }

        batchTaskHandler.onFinish(successCount, failCount);

        this.successCount = successCount;
        this.failCount = failCount;
        this.resultList = resultList;
        //
        return null;
    }

    /**
     * 断言结果
     * 
     * @param successCount 成功数量
     * @param failCount 失败数量
     * @param results 结果
     */
    public void assertResult(int successCount, int failCount, String... results) {

        Assert.assertEquals(successCount, this.successCount);
        Assert.assertEquals(failCount, this.failCount);
        Assert.assertArrayEquals(results, this.resultList.toArray(new String[0]));
    }
    
    @Override
    public BatchTaskBuilder enableSynchronousMode() {
        return this;
    }

    @Override
    public BatchTaskBuilder unblockInMaintenance() {
        return this;
    }
}
