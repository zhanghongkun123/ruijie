package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Description: 清理用户配置数据的GT消息对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
public class UserProfileCleanGuestToolMsgDTO {
    private Integer code = 0;

    private String message;

    private BodyMessage content;

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

    public UserProfileCleanGuestToolMsgDTO.BodyMessage getContent() {
        return content;
    }

    public void setContent(UserProfileCleanGuestToolMsgDTO.BodyMessage content) {
        this.content = content;
    }

    /**
     * body对象
     */
    public static class BodyMessage {
        @JSONField(name = "config_file")
        private List<String> configFileList;

        @JSONField(name = "config_dir")
        private List<String> configDirList;

        @JSONField(name = "config_key")
        private List<String> configKeyList;

        @JSONField(name = "config_value")
        private List<String> configValueList;

        private String md5;

        public List<String> getConfigDirList() {
            return configDirList;
        }

        public void setConfigDirList(List<String> configDirList) {
            this.configDirList = configDirList;
        }

        public List<String> getConfigFileList() {
            return configFileList;
        }

        public void setConfigFileList(List<String> configFileList) {
            this.configFileList = configFileList;
        }

        public List<String> getConfigKeyList() {
            return configKeyList;
        }

        public void setConfigKeyList(List<String> configKeyList) {
            this.configKeyList = configKeyList;
        }

        public List<String> getConfigValueList() {
            return configValueList;
        }

        public void setConfigValueList(List<String> configValueList) {
            this.configValueList = configValueList;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }
    }
}
