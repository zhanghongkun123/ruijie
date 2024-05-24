package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import com.ruijie.rcos.sk.base.batch.*;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 
 * Description: 批处理任务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月15日
 * 
 * @author Ghang
 */
public class BatchTaskBuilderMock implements BatchTaskBuilder {

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
    public BatchTaskBuilder setUniqueId(UUID arg0) {
        return this;
    }

    @Override
    public BatchTaskBuilder enableSynchronousMode() {
        return this;
    }

    @Override
    public BatchTaskBuilder unblockInMaintenance() {
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
    public BatchTaskSubmitResult start() {

        int successCount = 0;
        int failCount = 0;
        List<String> resultList = new ArrayList<>();

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
     * 校验结果
     * 
     * @param successCount 成功次数
     * @param failCount 失败次数
     * @param results 结果
     */
    public void assertResult(int successCount, int failCount, String... results) {

        Assert.assertEquals(successCount, this.successCount);
        Assert.assertEquals(failCount, this.failCount);
        Assert.assertArrayEquals(results, this.resultList.toArray(new String[0]));
    }

}
