package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportVDIDeskDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 用户组导入批处理任务
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/24
 *
 * @author zhangyichi
 */
public class CreateVDIDeskBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final ImportVDIDeskDTO importVDIDeskDTO;


    public CreateVDIDeskBatchTaskItem(UUID itemId, String itemName, ImportVDIDeskDTO importVDIDeskDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.importVDIDeskDTO = importVDIDeskDTO;
    }

    /**
     * 构建CreateVDIDeskBatchTaskItemBuilder对象
     *
     * @return 返回CreateVDIDeskBatchTaskItemBuilder对象
     */
    public static CreateVDIDeskBatchTaskItemBuilder builder() {
        return new CreateVDIDeskBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public ImportVDIDeskDTO getImportVDIDeskDTO() {
        return this.importVDIDeskDTO;
    }

    /**
     * 创建用户批处理对象
     */
    public static class CreateVDIDeskBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private ImportVDIDeskDTO importVDIDeskDTO;


        private CreateVDIDeskBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CreateVDIDeskBatchTaskItemBuilder itemId(UUID itemId) {
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
        public CreateVDIDeskBatchTaskItemBuilder itemName(String itemName) {
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
        public CreateVDIDeskBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置UserGroup
         *
         * @param importVDIDeskDTO 用户组对象
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CreateVDIDeskBatchTaskItemBuilder itemUser(ImportVDIDeskDTO importVDIDeskDTO) {
            Assert.notNull(importVDIDeskDTO, "itemId can not be null");
            this.importVDIDeskDTO = importVDIDeskDTO;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         * 
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public CreateVDIDeskBatchTaskItem build() {
            return new CreateVDIDeskBatchTaskItem(this.itemId, this.itemName, this.importVDIDeskDTO);
        }
    }
}
