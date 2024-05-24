package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportUserDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/21
 *
 * @author Jarman
 */
public class CreateUserBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private ImportUserDTO importUserDTO;


    public CreateUserBatchTaskItem(UUID itemId, String itemName, ImportUserDTO importUserDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.importUserDTO = importUserDTO;
    }

    /**
     * 构建CreateUserBatchTaskItemBuilder对象
     * 
     * @return 返回CreateUserBatchTaskItemBuilder对象
     */
    public static CreateUserBatchTaskItem.CreateUserBatchTaskItemBuilder builder() {
        return new CreateUserBatchTaskItem.CreateUserBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public ImportUserDTO getImportUserDTO() {
        return this.importUserDTO;
    }

    /**
     * 创建用户批处理对象
     */
    public static class CreateUserBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private ImportUserDTO importUserDTO;


        private CreateUserBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         * 
         * @param itemId id
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateUserBatchTaskItem.CreateUserBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         * 
         * @param itemName label
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateUserBatchTaskItem.CreateUserBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         * 
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateUserBatchTaskItem.CreateUserBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置User
         * 
         * @param importUserDTO 用户对象
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateUserBatchTaskItem.CreateUserBatchTaskItemBuilder itemUser(ImportUserDTO importUserDTO) {
            Assert.notNull(importUserDTO, "itemId can not be null");
            this.importUserDTO = importUserDTO;
            return this;
        }

        /**
         * 构建CreateUserBatchTaskItem
         * 
         * @return 返回CreateUserBatchTaskItem
         */
        public CreateUserBatchTaskItem build() {
            return new CreateUserBatchTaskItem(this.itemId, this.itemName, this.importUserDTO);
        }
    }
}
