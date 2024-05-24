package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Description: GT=> RCDC 获取用户配置策略响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18
 *
 * @author WuShengQiang
 */
public class UserProfileStrategyGuestToolMsgDTO {

    private Integer code = 0;

    private String message;

    private UserProfileStrategyGuestToolMsgDTO.BodyMessage content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BodyMessage getContent() {
        return content;
    }

    public void setContent(BodyMessage content) {
        this.content = content;
    }

    /**
     * body对象
     */
    public static class BodyMessage {

        @JSONField(name = "config_dir_to_save")
        private List<String> configDirToSaveList;

        @JSONField(name = "config_dir_no_save")
        private List<String> configDirNoSaveList;

        @JSONField(name = "config_file_to_save")
        private List<String> configFileToSaveList;

        @JSONField(name = "config_file_no_save")
        private List<String> configFileNoSaveList;

        @JSONField(name = "config_key_to_save")
        private List<String> configKeyToSaveList;

        @JSONField(name = "config_key_no_save")
        private List<String> configKeyNoSaveList;

        @JSONField(name = "config_value_to_save")
        private List<String> configValueToSaveList;

        @JSONField(name = "config_value_no_save")
        private List<String> configValueNoSaveList;

        private List<JSONObject> extraConfigList;

        private String md5;

        public List<String> getConfigDirToSaveList() {
            return configDirToSaveList;
        }

        public void setConfigDirToSaveList(List<String> configDirToSaveList) {
            this.configDirToSaveList = configDirToSaveList;
        }

        public List<String> getConfigDirNoSaveList() {
            return configDirNoSaveList;
        }

        public void setConfigDirNoSaveList(List<String> configDirNoSaveList) {
            this.configDirNoSaveList = configDirNoSaveList;
        }

        public List<String> getConfigFileToSaveList() {
            return configFileToSaveList;
        }

        public void setConfigFileToSaveList(List<String> configFileToSaveList) {
            this.configFileToSaveList = configFileToSaveList;
        }

        public List<String> getConfigFileNoSaveList() {
            return configFileNoSaveList;
        }

        public void setConfigFileNoSaveList(List<String> configFileNoSaveList) {
            this.configFileNoSaveList = configFileNoSaveList;
        }

        public List<String> getConfigKeyToSaveList() {
            return configKeyToSaveList;
        }

        public void setConfigKeyToSaveList(List<String> configKeyToSaveList) {
            this.configKeyToSaveList = configKeyToSaveList;
        }

        public List<String> getConfigKeyNoSaveList() {
            return configKeyNoSaveList;
        }

        public void setConfigKeyNoSaveList(List<String> configKeyNoSaveList) {
            this.configKeyNoSaveList = configKeyNoSaveList;
        }

        public List<String> getConfigValueToSaveList() {
            return configValueToSaveList;
        }

        public void setConfigValueToSaveList(List<String> configValueToSaveList) {
            this.configValueToSaveList = configValueToSaveList;
        }

        public List<String> getConfigValueNoSaveList() {
            return configValueNoSaveList;
        }

        public void setConfigValueNoSaveList(List<String> configValueNoSaveList) {
            this.configValueNoSaveList = configValueNoSaveList;
        }

        public List<JSONObject> getExtraConfigList() {
            return extraConfigList;
        }

        public void setExtraConfigList(List<JSONObject> extraConfigList) {
            this.extraConfigList = extraConfigList;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }
    }
}