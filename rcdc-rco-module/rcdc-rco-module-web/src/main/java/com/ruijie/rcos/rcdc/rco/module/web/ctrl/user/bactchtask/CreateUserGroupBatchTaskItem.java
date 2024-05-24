package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserGroupDTO;
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
public class CreateUserGroupBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final ImportUserGroupDTO importUserGroupDTO;


    public CreateUserGroupBatchTaskItem(UUID itemId, String itemName, ImportUserGroupDTO importUserGroupDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.importUserGroupDTO = importUserGroupDTO;
    }

    /**
     * 构建CreateUserBatchTaskItemBuilder对象
     *
     * @return 返回CreateUserBatchTaskItemBuilder对象
     */
    public static CreateUserGroupBatchTaskItemBuilder builder() {
        return new CreateUserGroupBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public ImportUserGroupDTO getImportUserGroupDTO() {
        return this.importUserGroupDTO;
    }

    /**
     * 创建用户批处理对象
     */
    public static class CreateUserGroupBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private ImportUserGroupDTO importUserGroupDTO;


        private CreateUserGroupBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CreateUserGroupBatchTaskItemBuilder itemId(UUID itemId) {
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
        public CreateUserGroupBatchTaskItemBuilder itemName(String itemName) {
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
        public CreateUserGroupBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置UserGroup
         *
         * @param importUserGroupDTO 用户组对象
         * @return 返回CreateUserGroupBatchTaskItemBuilder
         */
        public CreateUserGroupBatchTaskItemBuilder itemUser(ImportUserGroupDTO importUserGroupDTO) {
            Assert.notNull(importUserGroupDTO, "itemId can not be null");
            this.importUserGroupDTO = importUserGroupDTO;
            return this;
        }

        /**
         * 构建CreateUserGroupBatchTaskItem
         * 
         * @return 返回CreateUserGroupBatchTaskItem
         */
        public CreateUserGroupBatchTaskItem build() {
            return new CreateUserGroupBatchTaskItem(this.itemId, this.itemName, this.importUserGroupDTO);
        }
    }
}
