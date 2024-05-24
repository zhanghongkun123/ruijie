package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 完成桌面测试任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/13
 *
 * @author zhiweiHong
 */
public class CompleteDesktopTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final TestTaskStateEnum state;

    private final UUID desktopId;


    private final UUID testId;


    public TestTaskStateEnum getState() {
        return state;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public CompleteDesktopTaskItem(UUID itemId, String itemName, TestTaskStateEnum state, UUID desktopId, UUID testId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.state = state;
        this.desktopId = desktopId;
        this.testId = testId;
    }

    /**
     * 构建CompleteDesktopTaskItemBuilder对象
     *
     * @return 返回CompleteDesktopTaskItemBuilder对象
     */
    public static CompleteDesktopTaskItemBuilder builder() {
        return new CompleteDesktopTaskItemBuilder();
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
    public static class CompleteDesktopTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private TestTaskStateEnum state;

        private UUID desktopId;

        private UUID testId;


        private CompleteDesktopTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CompleteDesktopTaskItemBuilder itemId(UUID itemId) {
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
        public CompleteDesktopTaskItemBuilder itemName(String itemName) {
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
        public CompleteDesktopTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 状态赋值
         * 
         * @param state 状态
         * @return 实体
         */
        public CompleteDesktopTaskItemBuilder state(TestTaskStateEnum state) {
            Assert.notNull(state, "state can not be null");
            this.state = state;
            return this;
        }

        /**
         * 桌面id 赋值
         * 
         * @param desktopId 桌面id
         * @return 实体
         */
        public CompleteDesktopTaskItemBuilder desktopId(UUID desktopId) {
            Assert.notNull(desktopId, "desktopId can not be null");
            this.desktopId = desktopId;
            return this;
        }

        /**
         * 测试id 赋值
         * 
         * @param testId 测试id
         * @return 实体
         */
        public CompleteDesktopTaskItemBuilder testId(UUID testId) {
            Assert.notNull(testId, "testId can not be null");
            this.testId = testId;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         *
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public CompleteDesktopTaskItem build() {
            return new CompleteDesktopTaskItem(this.itemId, this.itemName, this.state, this.desktopId, this.testId);
        }
    }
}
