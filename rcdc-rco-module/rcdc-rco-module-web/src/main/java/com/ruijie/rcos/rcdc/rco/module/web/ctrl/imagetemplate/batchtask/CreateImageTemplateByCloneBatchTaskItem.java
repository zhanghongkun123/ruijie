package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.batchtask;

import java.util.UUID;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.dto.CreateImageTemplateByCloneDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 复制镜像批处理任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/1
 *
 * @author wjp
 */
public class CreateImageTemplateByCloneBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final CreateImageTemplateByCloneDTO createImageTemplateByCloneDTO;

    public CreateImageTemplateByCloneBatchTaskItem(UUID itemId, String itemName, CreateImageTemplateByCloneDTO createImageTemplateByCloneDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.createImageTemplateByCloneDTO = createImageTemplateByCloneDTO;
    }

    /**
     * 构建对象
     *
     * @return 返回对象
     */
    public static CreateImageTemplateByCloneBatchTaskItemBuilder builder() {
        return new CreateImageTemplateByCloneBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    public CreateImageTemplateByCloneDTO getCreateImageTemplateByCloneDTO() {
        return this.createImageTemplateByCloneDTO;
    }

    /**
     * 批处理对象
     */
    public static class CreateImageTemplateByCloneBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private CreateImageTemplateByCloneDTO createImageTemplateByCloneDTO;

        private CreateImageTemplateByCloneBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回值
         */
        public CreateImageTemplateByCloneBatchTaskItemBuilder itemId(UUID itemId) {
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
        public CreateImageTemplateByCloneBatchTaskItemBuilder itemName(String itemName) {
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
        public CreateImageTemplateByCloneBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置
         *
         * @param createImageTemplateByCloneDTO 镜像信息
         * @return 返回值
         */
        public CreateImageTemplateByCloneBatchTaskItemBuilder itemData(CreateImageTemplateByCloneDTO createImageTemplateByCloneDTO) {
            this.createImageTemplateByCloneDTO = createImageTemplateByCloneDTO;
            return this;
        }

        /**
         * 构建
         * 
         * @return 返回值
         */
        public CreateImageTemplateByCloneBatchTaskItem build() {
            return new CreateImageTemplateByCloneBatchTaskItem(this.itemId, this.itemName, this.createImageTemplateByCloneDTO);
        }
    }
}
