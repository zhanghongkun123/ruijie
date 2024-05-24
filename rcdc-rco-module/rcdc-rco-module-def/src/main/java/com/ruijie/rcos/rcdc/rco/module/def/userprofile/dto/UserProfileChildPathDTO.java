package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathModeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.UserProfilePathTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 创建路径请求对象下的子路径
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/21
 *
 * @author zwf
 */
public class UserProfileChildPathDTO {
    @Nullable
    private UUID id;

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
    private String[] pathArr;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

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

    public String[] getPathArr() {
        return pathArr;
    }

    public void setPathArr(String[] pathArr) {
        this.pathArr = pathArr;
    }

    /**
     * 判断是否为文件、文件夹类型的配置
     *
     * @return 结果
     */
    public boolean isRoute() {
        return type == UserProfilePathTypeEnum.DOCUMENT || type == UserProfilePathTypeEnum.FOLDER;
    }
}
