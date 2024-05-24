package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import org.springframework.beans.BeanUtils;

/**
 * Description: 导入用户配置路径对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public class ImportUserProfilePathDTO {
    /**
     * 当前行
     */
    private Integer rowNum;

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 组描述
     */
    private String groupDescription;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 配置方式(同步/排除)
     */
    private String mode;

    /**
     * 类型(文件夹/文件/注册表)
     */
    private String type;

    /**
     * 路径
     */
    private String path;

    /**
     * 描述
     **/
    private String description;

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
