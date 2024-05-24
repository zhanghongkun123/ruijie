package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.ImportComputerDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author zqj
 */
public class CreateComputerBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private ImportComputerDTO importComputerDTO;


    public CreateComputerBatchTaskItem(UUID itemId, String itemName, ImportComputerDTO importComputerDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.importComputerDTO = importComputerDTO;
    }

    /**
     * 构建CreateUserBatchTaskItemBuilder对象
     *
     * @return 返回CreateUserBatchTaskItemBuilder对象
     */
    public static CreateComputerBatchTaskItem.CreateComputerBatchTaskItemBuilder builder() {
        return new CreateComputerBatchTaskItem.CreateComputerBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public ImportComputerDTO getImportComputerDTO() {
        return this.importComputerDTO;
    }

    /**
     * 创建PC终端批处理对象
     */
    public static class CreateComputerBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private ImportComputerDTO importComputerDTO;


        private CreateComputerBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateComputerBatchTaskItem.CreateComputerBatchTaskItemBuilder itemId(UUID itemId) {
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
        public CreateComputerBatchTaskItem.CreateComputerBatchTaskItemBuilder itemName(String itemName) {
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
        public CreateComputerBatchTaskItem.CreateComputerBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置Computer
         *
         * @param importComputerDTO PC终端对象
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateComputerBatchTaskItem.CreateComputerBatchTaskItemBuilder itemComputer(ImportComputerDTO importComputerDTO) {
            Assert.notNull(importComputerDTO, "importComputerDTO can not be null");
            this.importComputerDTO = importComputerDTO;
            return this;
        }

        /**
         * 构建CreateUserBatchTaskItem
         *
         * @return 返回CreateUserBatchTaskItem
         */
        public CreateComputerBatchTaskItem build() {
            return new CreateComputerBatchTaskItem(this.itemId, this.itemName, this.importComputerDTO);
        }
    }
}
