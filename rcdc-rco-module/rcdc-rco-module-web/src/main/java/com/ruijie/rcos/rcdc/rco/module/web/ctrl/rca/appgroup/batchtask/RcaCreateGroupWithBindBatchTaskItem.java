package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request.RcaGroupBindAppGroupWebRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 创建分组并绑定用户批任务
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月24日
 *
 * @author zhengjingyong
 */
public class RcaCreateGroupWithBindBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final UUID poolId;

    private final RcaGroupBindAppGroupWebRequest bindAppGroupRequest;

    public RcaCreateGroupWithBindBatchTaskItem(UUID itemId, String itemName, UUID poolId,
                                               RcaGroupBindAppGroupWebRequest bindAppGroupRequest) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.poolId = poolId;
        this.bindAppGroupRequest = bindAppGroupRequest;
    }

    /**
     * 构造器
     *
     * @return 构造器
     */
    public static RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder builder() {
        return new RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder();
    }

    /**
     * 创建分组并更新分配批处理对象
     */
    public static class RcaCreateGroupAndBindUserBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private UUID poolId;

        private RcaGroupBindAppGroupWebRequest bindAppGroupWebRequest;

        private RcaCreateGroupAndBindUserBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回RcaCreateGroupAndBindUserBatchTaskItemBuilder
         */
        public RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         *
         * @param itemName label
         * @return 返回RcaCreateGroupAndBindUserBatchTaskItemBuilder
         */
        public RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回RcaCreateGroupAndBindUserBatchTaskItemBuilder
         */
        public RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder itemName(
                String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置UserGroup
         *
         * @param bindAppGroupWebRequest 绑定信息
         * @return 返回RcaCreateGroupAndBindUserBatchTaskItemBuilder
         */
        public RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder itemBind(
                RcaGroupBindAppGroupWebRequest bindAppGroupWebRequest) {
            Assert.notNull(bindAppGroupWebRequest, "itemId can not be null");
            this.bindAppGroupWebRequest = bindAppGroupWebRequest;
            return this;
        }

        /**
         * 设置poolId
         *
         * @param poolId label
         * @return 返回RcaCreateGroupAndBindUserBatchTaskItemBuilder
         */
        public RcaCreateGroupWithBindBatchTaskItem.RcaCreateGroupAndBindUserBatchTaskItemBuilder itemPoolId(UUID poolId) {
            Assert.notNull(poolId, "poolId can not be null");
            this.poolId = poolId;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         *
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public RcaCreateGroupWithBindBatchTaskItem build() {
            return new RcaCreateGroupWithBindBatchTaskItem(this.itemId, this.itemName, this.poolId, this.bindAppGroupWebRequest);
        }
    }

    @Override
    public UUID getItemID() {
        return itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public UUID getPoolId() {
        return poolId;
    }

    public RcaGroupBindAppGroupWebRequest getBindAppGroupRequest() {
        return bindAppGroupRequest;
    }

}
