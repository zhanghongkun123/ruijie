package com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.batchtask;

import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 用户组删除批处理任务
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/16
 *
 * @author wjp
 */
public class DeleteUserGroupBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final UUID moveGroupId;

    public DeleteUserGroupBatchTaskItem(UUID itemId, String itemName, UUID moveGroupId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.moveGroupId = moveGroupId;
    }

    /**
     * 构建对象
     *
     * @return 返回对象
     */
    public static DeleteUserGroupBatchTaskItemBuilder builder() {
        return new DeleteUserGroupBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public UUID getMoveGroupId() {
        return this.moveGroupId;
    }

    /**
     * 用户组删除批处理对象
     */
    public static class DeleteUserGroupBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private UUID moveGroupId;

        private DeleteUserGroupBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回值
         */
        public DeleteUserGroupBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         *
         * @param itemName label
         * @return 返回值
         */
        public DeleteUserGroupBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回值
         */
        public DeleteUserGroupBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置
         *
         * @param moveGroupId id
         * @return 返回值
         */
        public DeleteUserGroupBatchTaskItemBuilder itemUser(@Nullable UUID moveGroupId) {
            this.moveGroupId = moveGroupId;
            return this;
        }

        /**
         * 构建
         * 
         * @return 返回值
         */
        public DeleteUserGroupBatchTaskItem build() {
            return new DeleteUserGroupBatchTaskItem(this.itemId, this.itemName, this.moveGroupId);
        }
    }
}
