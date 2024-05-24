package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 处理数据导入的中间工作类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/27
 *
 * @author zwf
 */
public class UserProfilePathToolDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfilePathToolDTO.class);

    /**
     * 组名称
     */
    private String groupName;

    /**
     * 组描述
     */
    private String groupDescribe;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 子路径对象
     */
    private List<UserProfileChildPathToolDTO> childPathList = new ArrayList<>();

    /**
     * 描述
     **/
    private String description;

    /**
     * 创建人
     */
    private String creatorUserName;

    /**
     * 是否系统自带
     */
    private Boolean isDefault;

    private Date createTime;

    private Date updateTime;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UserProfileChildPathToolDTO> getChildPathList() {
        return childPathList;
    }

    public void setChildPathList(List<UserProfileChildPathToolDTO> childPathList) {
        this.childPathList = childPathList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorUserName() {
        return creatorUserName;
    }

    public void setCreatorUserName(String creatorUserName) {
        this.creatorUserName = creatorUserName;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGroupDescribe() {
        return groupDescribe;
    }

    public void setGroupDescribe(String groupDescribe) {
        this.groupDescribe = groupDescribe;
    }

    /**
     * 将中间工作类转化为路径对象
     *
     * @return 路径对象
     */
    public UserProfilePathDTO changeToUserProfilePathDTO() {
        UserProfilePathDTO userProfilePathDTO = new UserProfilePathDTO();
        userProfilePathDTO.setName(name);
        userProfilePathDTO.setDescription(description);
        userProfilePathDTO.setGroupDescribe(groupDescribe);
        userProfilePathDTO.setGroupName(groupName);
        userProfilePathDTO.setChildPathArr(convertTo());

        LOGGER.debug("转化后的路径对象：{}", JSONObject.toJSONString(userProfilePathDTO));
        return userProfilePathDTO;
    }

    private UserProfileChildPathDTO[] convertTo() {
        List<UserProfileChildPathDTO> childPathDTOList = new ArrayList<>();
        for (UserProfileChildPathToolDTO childPathTool : childPathList) {
            childPathDTOList.add(childPathTool.changeToUserProfileChildPathDTO());
        }

        return childPathDTOList.toArray(new UserProfileChildPathDTO[childPathDTOList.size()]);
    }

    /**
     * 增加子路径
     *
     * @param mode 配置方式
     * @param type 类型
     * @param path 子路径
     */
    public void addUserProfileChildPath(UserProfilePathModeEnum mode, UserProfilePathTypeEnum type,
                                        String path) {
        Assert.notNull(mode, "mode must not be null");
        Assert.notNull(type, "type must not be null");
        Assert.notNull(path, "path must not be null");

        for (UserProfileChildPathToolDTO childPathToolDTO : childPathList) {
            if (mode.equals(childPathToolDTO.getMode()) && type.equals(childPathToolDTO.getType())) {
                childPathToolDTO.addPath(path);
                return;
            }
        }

        UserProfileChildPathToolDTO childPathToolDTO = new UserProfileChildPathToolDTO();
        childPathToolDTO.setType(type);
        childPathToolDTO.setMode(mode);
        childPathToolDTO.addPath(path);
        childPathList.add(childPathToolDTO);
    }
}
