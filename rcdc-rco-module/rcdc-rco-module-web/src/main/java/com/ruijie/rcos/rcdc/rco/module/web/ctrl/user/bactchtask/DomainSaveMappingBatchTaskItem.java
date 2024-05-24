package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacSaveDomainMappingConfigDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/28
 *
 * @author zhanghk
 */
public class DomainSaveMappingBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private IacSaveDomainMappingConfigDTO cbbSaveDomainMappingConfigDTO;


    public DomainSaveMappingBatchTaskItem(UUID itemId, String itemName, IacSaveDomainMappingConfigDTO cbbSaveDomainMappingConfigDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.cbbSaveDomainMappingConfigDTO = cbbSaveDomainMappingConfigDTO;
    }

    /**
     * 构建DomainSaveDomainBatchTaskItemBuilder对象
     *
     * @return 返回DomainSaveMappingBatchTaskItemBuilder对象
     */
    public static DomainSaveMappingBatchTaskItem.DomainSaveMappingBatchTaskItemBuilder builder() {
        return new DomainSaveMappingBatchTaskItem.DomainSaveMappingBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public IacSaveDomainMappingConfigDTO getCbbSaveDomainMappingConfigDTO() {
        return this.cbbSaveDomainMappingConfigDTO;
    }

    /**
     * 创建AD域映射批处理对象
     */
    public static class DomainSaveMappingBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private IacSaveDomainMappingConfigDTO cbbSaveDomainMappingConfigDTO;


        private DomainSaveMappingBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回DomainSaveDomainBatchTaskItemBuilder
         */
        public DomainSaveMappingBatchTaskItem.DomainSaveMappingBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         *
         * @param itemName label
         * @return 返回DomainSaveMappingBatchTaskItemBuilder
         */
        public DomainSaveMappingBatchTaskItem.DomainSaveMappingBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey 国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回DomainSaveMappingBatchTaskItemBuilder
         */
        public DomainSaveMappingBatchTaskItem.DomainSaveMappingBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置映射对象
         *
         * @param cbbSaveDomainMappingConfigDTO 用户映射对象
         * @return 返回DomainSaveMappingBatchTaskItemBuilder
         */
        public DomainSaveMappingBatchTaskItem.DomainSaveMappingBatchTaskItemBuilder itemMapping(
                IacSaveDomainMappingConfigDTO cbbSaveDomainMappingConfigDTO) {
            Assert.notNull(cbbSaveDomainMappingConfigDTO, "itemId can not be null");
            this.cbbSaveDomainMappingConfigDTO = cbbSaveDomainMappingConfigDTO;
            return this;
        }

        /**
         * 构建DomainSaveDomainBatchTaskItem
         * 
         * @return 返回DomainSaveDomainBatchTaskItem
         */
        public DomainSaveMappingBatchTaskItem build() {
            return new DomainSaveMappingBatchTaskItem(this.itemId, this.itemName, this.cbbSaveDomainMappingConfigDTO);
        }
    }
}
