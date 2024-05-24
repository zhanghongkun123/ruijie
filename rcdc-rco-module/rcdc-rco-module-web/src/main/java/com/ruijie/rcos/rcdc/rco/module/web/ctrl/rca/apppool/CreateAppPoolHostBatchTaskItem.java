package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool;

import com.ruijie.rcos.rcdc.rca.module.def.api.request.RcaCreateCloudDesktopRequest;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 创建三方应用主机批处理任务Item
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月28日
 *
 * @author liuwc
 */
public class CreateAppPoolHostBatchTaskItem implements BatchTaskItem {

    private final UUID itemId;

    private final String itemName;

    private final RcaCreateCloudDesktopRequest request;

    public CreateAppPoolHostBatchTaskItem(UUID itemId, String itemName, RcaCreateCloudDesktopRequest request) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.request = request;
    }

    /**
     * 构建CreateThirdPartAppHostBatchTaskItem对象
     *
     * @return 返回CreateThirdPartAppHostBatchTaskItem对象
     */
    public static CreateHostBatchTaskItemBuilder builder() {
        return new CreateHostBatchTaskItemBuilder();
    }

    public RcaCreateCloudDesktopRequest getRequest() {
        return request;
    }

    @Override
    public UUID getItemID() {
        return this.itemId;
    }

    @Override
    public String getItemName() {
        return this.itemName;
    }

    /**
     * 批处理对象
     */
    public static class CreateHostBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private RcaCreateCloudDesktopRequest createCloudDesktopRequest;

        public CreateHostBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         * @param itemId id
         * @return PublishAppBatchTaskItemBuilder
         */
        public CreateHostBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");

            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         * @param itemName label
         * @return PublishAppBatchTaskItemBuilder
         */
        public CreateHostBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");

            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         * @param itemNameKey 国际化Key
         * @param itemNameArgsArr 国际化参数
         * @return PublishAppBatchTaskItemBuilder
         */
        public CreateHostBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");

            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置发布应用信息
         * @param createHostAppDTO 应用更新对象
         * @return PublishAppBatchTaskItemBuilder
         */
        public CreateHostBatchTaskItemBuilder itemData(RcaCreateCloudDesktopRequest createHostAppDTO) {
            Assert.notNull(createHostAppDTO, "createHostAppDTO can not be null");

            this.createCloudDesktopRequest = createHostAppDTO;
            return this;
        }

        /**
         * 构建CreateAppPoolHostBatchTaskItem
         * @return CreateAppPoolHostBatchTaskItem
         */
        public CreateAppPoolHostBatchTaskItem build() {
            return new CreateAppPoolHostBatchTaskItem(this.itemId, this.itemName, this.createCloudDesktopRequest);
        }
    }

}
