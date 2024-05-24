package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 完成桌面测试任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/13
 *
 * @author zhiweiHong
 */
public class DeleteAppTestDesktopTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;


    private final UUID testId;


    public DeleteAppTestDesktopTaskItem(UUID itemId, String itemName, UUID testId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.testId = testId;
    }

    /**
     * 构建 builder
     * 
     * @return DeleteAppTestDesktopTaskItemBuilder
     */
    public static DeleteAppTestDesktopTaskItemBuilder builder() {
        return new DeleteAppTestDesktopTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public UUID getTestId() {
        return testId;
    }



    /**
     * 创建AD域组批处理对象
     */
    public static class DeleteAppTestDesktopTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private UUID testId;



        private DeleteAppTestDesktopTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public DeleteAppTestDesktopTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         *
         * @param itemName label
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public DeleteAppTestDesktopTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public DeleteAppTestDesktopTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }


        /**
         * 设置任务id
         * 
         * @param testId 任务id
         * @return 构建类
         */
        public DeleteAppTestDesktopTaskItemBuilder testId(UUID testId) {
            Assert.notNull(testId, "testId can not be null");
            this.testId = testId;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         *
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public DeleteAppTestDesktopTaskItem build() {
            return new DeleteAppTestDesktopTaskItem(this.itemId, this.itemName, this.testId);
        }
    }
}
