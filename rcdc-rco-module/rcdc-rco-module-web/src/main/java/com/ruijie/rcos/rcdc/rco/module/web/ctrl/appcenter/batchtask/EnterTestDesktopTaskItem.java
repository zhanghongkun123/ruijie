package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

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
public class EnterTestDesktopTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;


    private final UUID desktopId;

    private final UUID adminId;


    private final UUID testId;


    public UUID getDesktopId() {
        return desktopId;
    }

    public EnterTestDesktopTaskItem(UUID itemId, String itemName, UUID desktopId, UUID testId, UUID adminId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.desktopId = desktopId;
        this.testId = testId;
        this.adminId = adminId;
    }

    /**
     * 构建CompleteDesktopTaskItemBuilder对象
     *
     * @return 返回CompleteDesktopTaskItemBuilder对象
     */
    public static EnterDesktopTaskItemBuilder builder() {
        return new EnterDesktopTaskItemBuilder();
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

    public UUID getAdminId() {
        return adminId;
    }

    /**
     * 创建AD域组批处理对象
     */
    public static class EnterDesktopTaskItemBuilder {

        private UUID itemId;

        private String itemName;


        private UUID desktopId;

        private UUID testId;

        private UUID adminId;


        private EnterDesktopTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public EnterDesktopTaskItemBuilder itemId(UUID itemId) {
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
        public EnterDesktopTaskItemBuilder itemName(String itemName) {
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
        public EnterDesktopTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }


        /**
         * 设置桌面id
         * 
         * @param desktopId 桌面id
         * @return 构建类
         */
        public EnterDesktopTaskItemBuilder desktopId(UUID desktopId) {
            Assert.notNull(desktopId, "desktopId can not be null");
            this.desktopId = desktopId;
            return this;
        }

        /**
         * 设置任务id
         * 
         * @param testId 任务id
         * @return 构建类
         */
        public EnterDesktopTaskItemBuilder testId(UUID testId) {
            Assert.notNull(testId, "testId can not be null");
            this.testId = testId;
            return this;
        }

        /**
         * 设置管理员id
         * 
         * @param adminId 管理员id
         * @return 构建类
         */
        public EnterDesktopTaskItemBuilder adminId(UUID adminId) {
            Assert.notNull(adminId, "adminId can not be null");
            this.adminId = adminId;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         *
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public EnterTestDesktopTaskItem build() {
            return new EnterTestDesktopTaskItem(this.itemId, this.itemName, this.desktopId, this.testId, this.adminId);
        }
    }
}
