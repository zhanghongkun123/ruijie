package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.extstorage.CbbCreateExternalStorageDTO;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author linke
 */
public class UserProfileBodyMessageDTO implements Serializable {

    @Nullable
    @JSONField(name = "config_dir_to_save")
    private List<String> configDirToSaveList;

    @Nullable
    @JSONField(name = "config_dir_no_save")
    private List<String> configDirNoSaveList;

    @Nullable
    @JSONField(name = "config_file_to_save")
    private List<String> configFileToSaveList;

    @Nullable
    @JSONField(name = "config_file_no_save")
    private List<String> configFileNoSaveList;

    @Nullable
    @JSONField(name = "config_key_to_save")
    private List<String> configKeyToSaveList;

    @Nullable
    @JSONField(name = "config_key_no_save")
    private List<String> configKeyNoSaveList;

    @Nullable
    @JSONField(name = "config_value_to_save")
    private List<String> configValueToSaveList;

    @Nullable
    @JSONField(name = "config_value_no_save")
    private List<String> configValueNoSaveList;

    @Nullable
    private List<JSONObject> extraConfigList;

    @Nullable
    private String md5;

    @Nullable
    private String redirectLocation;

    @Nullable
    private CbbCreateExternalStorageDTO fileServerConfig;

    @Nullable
    public List<String> getConfigDirToSaveList() {
        return configDirToSaveList;
    }

    public void setConfigDirToSaveList(@Nullable List<String> configDirToSaveList) {
        this.configDirToSaveList = configDirToSaveList;
    }

    @Nullable
    public List<String> getConfigDirNoSaveList() {
        return configDirNoSaveList;
    }

    public void setConfigDirNoSaveList(@Nullable List<String> configDirNoSaveList) {
        this.configDirNoSaveList = configDirNoSaveList;
    }

    @Nullable
    public List<String> getConfigFileToSaveList() {
        return configFileToSaveList;
    }

    public void setConfigFileToSaveList(@Nullable List<String> configFileToSaveList) {
        this.configFileToSaveList = configFileToSaveList;
    }

    @Nullable
    public List<String> getConfigFileNoSaveList() {
        return configFileNoSaveList;
    }

    public void setConfigFileNoSaveList(@Nullable List<String> configFileNoSaveList) {
        this.configFileNoSaveList = configFileNoSaveList;
    }

    @Nullable
    public List<String> getConfigKeyToSaveList() {
        return configKeyToSaveList;
    }

    public void setConfigKeyToSaveList(@Nullable List<String> configKeyToSaveList) {
        this.configKeyToSaveList = configKeyToSaveList;
    }

    @Nullable
    public List<String> getConfigKeyNoSaveList() {
        return configKeyNoSaveList;
    }

    public void setConfigKeyNoSaveList(@Nullable List<String> configKeyNoSaveList) {
        this.configKeyNoSaveList = configKeyNoSaveList;
    }

    @Nullable
    public List<String> getConfigValueToSaveList() {
        return configValueToSaveList;
    }

    public void setConfigValueToSaveList(@Nullable List<String> configValueToSaveList) {
        this.configValueToSaveList = configValueToSaveList;
    }

    @Nullable
    public List<String> getConfigValueNoSaveList() {
        return configValueNoSaveList;
    }

    public void setConfigValueNoSaveList(@Nullable List<String> configValueNoSaveList) {
        this.configValueNoSaveList = configValueNoSaveList;
    }

    @Nullable
    public List<JSONObject> getExtraConfigList() {
        return extraConfigList;
    }

    public void setExtraConfigList(@Nullable List<JSONObject> extraConfigList) {
        this.extraConfigList = extraConfigList;
    }

    @Nullable
    public String getMd5() {
        return md5;
    }

    public void setMd5(@Nullable String md5) {
        this.md5 = md5;
    }

    @Nullable
    public String getRedirectLocation() {
        return redirectLocation;
    }

    public void setRedirectLocation(@Nullable String redirectLocation) {
        this.redirectLocation = redirectLocation;
    }

    @Nullable
    public CbbCreateExternalStorageDTO getFileServerConfig() {
        return fileServerConfig;
    }

    public void setFileServerConfig(@Nullable CbbCreateExternalStorageDTO fileServerConfig) {
        this.fileServerConfig = fileServerConfig;
    }
}
