package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.batchtask;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.dto.CreateAdGroupWebDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 用户组导入批处理任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-09-27
 *
 * @author zqj
 */
public class CreateAdGroupBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final CreateAdGroupWebDTO createAdGroupDTO;


    public CreateAdGroupBatchTaskItem(UUID itemId, String itemName, CreateAdGroupWebDTO createAdGroup) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.createAdGroupDTO = createAdGroup;
    }

    /**
     * 构建CreateAdGroupBatchTaskItemBuilder对象
     *
     * @return 返回CreateAdGroupBatchTaskItemBuilder对象
     */
    public static CreateAdGroupBatchTaskItemBuilder builder() {
        return new CreateAdGroupBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public CreateAdGroupWebDTO getCreateAdGroupDTO() {
        return createAdGroupDTO;
    }

    /**
     * 创建AD域组批处理对象
     */
    public static class CreateAdGroupBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private CreateAdGroupWebDTO createAdGroupDTO;


        private CreateAdGroupBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CreateAdGroupBatchTaskItemBuilder itemId(UUID itemId) {
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
        public CreateAdGroupBatchTaskItemBuilder itemName(String itemName) {
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
        public CreateAdGroupBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置adGroup
         *
         * @param createAdGroup 对象
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CreateAdGroupBatchTaskItemBuilder item(CreateAdGroupWebDTO createAdGroup) {
            Assert.notNull(createAdGroup, "createAdGroup can not be null");
            this.createAdGroupDTO = createAdGroup;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         * 
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public CreateAdGroupBatchTaskItem build() {
            return new CreateAdGroupBatchTaskItem(this.itemId, this.itemName, this.createAdGroupDTO);
        }
    }
}
