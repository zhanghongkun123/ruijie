package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.batchtask;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.dto.ImportSoftwareDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public class CreateSoftwareBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private ImportSoftwareDTO importSoftwareDTO;


    public CreateSoftwareBatchTaskItem(UUID itemId, String itemName, ImportSoftwareDTO importSoftwareDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.importSoftwareDTO = importSoftwareDTO;
    }

    /**
     * 构建CreateSoftwareBatchTaskItemBuilder对象
     *
     * @return 返回CreateSoftwareBatchTaskItemBuilder对象
     */
    public static CreateSoftwareBatchTaskItem.CreateSoftwareBatchTaskItemBuilder builder() {
        return new CreateSoftwareBatchTaskItem.CreateSoftwareBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public ImportSoftwareDTO getImportSoftwareDTO() {
        return this.importSoftwareDTO;
    }

    /**
     * 创建用户批处理对象
     */
    public static class CreateSoftwareBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private ImportSoftwareDTO importSoftwareDTO;


        private CreateSoftwareBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateSoftwareBatchTaskItemBuilder
         */
        public CreateSoftwareBatchTaskItem.CreateSoftwareBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         *
         * @param itemName label
         * @return 返回CreateSoftwareBatchTaskItemBuilder
         */
        public CreateSoftwareBatchTaskItem.CreateSoftwareBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回CreateSoftwareBatchTaskItemBuilder
         */
        public CreateSoftwareBatchTaskItem.CreateSoftwareBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置Software
         *
         * @param importSoftwareDTO 用户对象
         * @return 返回CreateSoftwareBatchTaskItemBuilder
         */
        public CreateSoftwareBatchTaskItem.CreateSoftwareBatchTaskItemBuilder itemSoftware(ImportSoftwareDTO importSoftwareDTO) {
            Assert.notNull(importSoftwareDTO, "itemId can not be null");
            this.importSoftwareDTO = importSoftwareDTO;
            return this;
        }

        /**
         * 构建CreateSoftwareBatchTaskItem
         * 
         * @return 返回CreateSoftwareBatchTaskItem
         */
        public CreateSoftwareBatchTaskItem build() {
            return new CreateSoftwareBatchTaskItem(this.itemId, this.itemName, this.importSoftwareDTO);
        }
    }
}
