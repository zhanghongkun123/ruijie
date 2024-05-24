package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.batchtask;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto.ImportUserProfilePathDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 导入批量创建处理任务创建对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/26
 *
 * @author WuShengQiang
 */
public class CreatePathBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private UserProfilePathDTO userProfilePathDTO;

    public CreatePathBatchTaskItem(UUID itemId, String itemName, UserProfilePathDTO userProfilePathDTO) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.userProfilePathDTO = userProfilePathDTO;
    }

    /**
     * 构建CreatePathBatchTaskItemBuilder对象
     *
     * @return 返回CreatePathBatchTaskItemBuilder对象
     */
    public static CreatePathBatchTaskItem.CreatePathBatchTaskItemBuilder builder() {
        return new CreatePathBatchTaskItem.CreatePathBatchTaskItemBuilder();
    }

    @Override
    public UUID getItemID() {
        return itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public UserProfilePathDTO getUserProfilePathDTO() {
        return userProfilePathDTO;
    }

    /**
     * 创建用户批处理对象
     */
    public static class CreatePathBatchTaskItemBuilder {

        private UUID itemId;

        private String itemName;

        private UserProfilePathDTO userProfilePathDTO;


        private CreatePathBatchTaskItemBuilder() {

        }

        /**
         * 设置id
         *
         * @param itemId id
         * @return 返回CreatePathBatchTaskItemBuilder
         */
        public CreatePathBatchTaskItem.CreatePathBatchTaskItemBuilder itemId(UUID itemId) {
            Assert.notNull(itemId, "itemId can not be null");
            this.itemId = itemId;
            return this;
        }

        /**
         * 设置name
         *
         * @param itemName label
         * @return 返回CreatePathBatchTaskItemBuilder
         */
        public CreatePathBatchTaskItem.CreatePathBatchTaskItemBuilder itemName(String itemName) {
            Assert.notNull(itemName, "itemName can not be null");
            this.itemName = itemName;
            return this;
        }

        /**
         * 设置国际化
         *
         * @param itemNameKey     国际化key
         * @param itemNameArgsArr 国际化参数
         * @return 返回CreatePathBatchTaskItemBuilder
         */
        public CreatePathBatchTaskItem.CreatePathBatchTaskItemBuilder itemName(String itemNameKey, String[] itemNameArgsArr) {
            Assert.notNull(itemNameKey, "itemNameKey can not be null");
            Assert.notNull(itemNameArgsArr, "itemNameArgsArr can not be null");
            this.itemName = LocaleI18nResolver.resolve(itemNameKey, itemNameArgsArr);
            return this;
        }

        /**
         * 设置Path
         *
         * @param userProfilePathDTO 用户对象
         * @return 返回CreatePathBatchTaskItemBuilder
         */
        public CreatePathBatchTaskItem.CreatePathBatchTaskItemBuilder itemPath(UserProfilePathDTO userProfilePathDTO) {
            Assert.notNull(userProfilePathDTO, "userProfilePathDTO can not be null");
            this.userProfilePathDTO = userProfilePathDTO;
            return this;
        }

        /**
         * 构建CreatePathBatchTaskItem
         *
         * @return 返回CreatePathBatchTaskItem
         */
        public CreatePathBatchTaskItem build() {
            return new CreatePathBatchTaskItem(this.itemId, this.itemName, this.userProfilePathDTO);
        }
    }
}