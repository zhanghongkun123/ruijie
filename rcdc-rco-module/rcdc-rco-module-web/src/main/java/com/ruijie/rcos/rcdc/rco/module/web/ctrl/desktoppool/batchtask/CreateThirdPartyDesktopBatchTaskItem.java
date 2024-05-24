package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ComputerDTO;
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
public class CreateThirdPartyDesktopBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private ComputerDTO computerDTO;


    public CreateThirdPartyDesktopBatchTaskItem(UUID itemId, String itemName, ComputerDTO computerDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.computerDTO = computerDTO;
    }

    /**
     * 构建CreateUserBatchTaskItemBuilder对象
     *
     * @return 返回CreateUserBatchTaskItemBuilder对象
     */
    public static CreateThirdPartyDesktopBatchTaskItemBuilder builder() {
        return new CreateThirdPartyDesktopBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ComputerDTO getComputerDTO() {
        return computerDTO;
    }

    public void setComputerDTO(ComputerDTO computerDTO) {
        this.computerDTO = computerDTO;
    }

    /**
     * 创建PC终端批处理对象
     */
    public static class CreateThirdPartyDesktopBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private ComputerDTO computerDTO;


        private CreateThirdPartyDesktopBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateThirdPartyDesktopBatchTaskItemBuilder itemId(UUID itemId) {
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
        public CreateThirdPartyDesktopBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置Computer
         *
         * @param computerDTO PC终端对象
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateThirdPartyDesktopBatchTaskItemBuilder itemComputer(ComputerDTO computerDTO) {
            Assert.notNull(computerDTO, "computerDTO can not be null");
            this.computerDTO = computerDTO;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回CreateUserBatchTaskItemBuilder
         */
        public CreateThirdPartyDesktopBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        public UUID getItemId() {
            return itemId;
        }

        public void setItemId(UUID itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public ComputerDTO getComputerDTO() {
            return computerDTO;
        }

        public void setComputerDTO(ComputerDTO computerDTO) {
            this.computerDTO = computerDTO;
        }

        /**
         * 构建CreateUserBatchTaskItem
         *
         * @return 返回CreateUserBatchTaskItem
         */
        public CreateThirdPartyDesktopBatchTaskItem build() {
            return new CreateThirdPartyDesktopBatchTaskItem(this.itemId, this.itemName, this.computerDTO);
        }
    }
}
