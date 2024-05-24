package com.ruijie.rcos.rcdc.rco.module.web.ctrl.userprofile.dto;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileChildPathDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 处理数据导入的中间工作类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/27
 *
 * @author zwf
 */
public class UserProfileChildPathToolDTO {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileChildPathToolDTO.class);

    /**
     * 配置方式(同步/排除)
     */
    @NotNull
    private UserProfilePathModeEnum mode;

    /**
     * 类型(文件夹/文件/注册表)
     */
    @NotNull
    private UserProfilePathTypeEnum type;

    /**
     * 路径列表
     */
    @NotNull
    private List<String> pathList = new ArrayList<>();


    public UserProfilePathModeEnum getMode() {
        return mode;
    }

    public void setMode(UserProfilePathModeEnum mode) {
        this.mode = mode;
    }

    public UserProfilePathTypeEnum getType() {
        return type;
    }

    public void setType(UserProfilePathTypeEnum type) {
        this.type = type;
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    /**
     * 将中间工作类转化为子路径类型对象
     *
     * @return 子路径类型对象
     */
    public UserProfileChildPathDTO changeToUserProfileChildPathDTO() {
        UserProfileChildPathDTO childPathDTO = new UserProfileChildPathDTO();
        childPathDTO.setMode(mode);
        childPathDTO.setType(type);
        childPathDTO.setPathArr(pathList.toArray(new String[pathList.size()]));

        LOGGER.debug("转化后的子路径类型对象：{}", JSONObject.toJSONString(childPathDTO));
        return childPathDTO;
    }

    /**
     * 增加子路径
     *
     * @param path 路径
     */
    public void addPath(String path) {
        Assert.hasText(path, "path must have text");

        pathList.add(path);
    }
}
