package com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ExcelHead;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * Description: 导出路径DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/27
 *
 * @author WuShengQiang
 */
public class ExportPathDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportPathDTO.class);

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH_NAME)
    private String pathName;

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH_DESC)
    private String pathDesc;

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH_GROUP_NAME)
    private String groupName;

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH_GROUP_DESC)
    private String groupDesc;

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH_MODE)
    private String mode;

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH_TYPE)
    private String type;

    @ExcelHead(UserProfileBusinessKey.RCDC_RCO_EXPORT_COLUMNS_PATH)
    private String path;

    public ExportPathDTO() {

    }

    /**
     * 格式化ExportPathDTO类
     */
    public void formatFor() {
        try {
            // 给属性为null的赋值为空字符串
            format();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            LOGGER.error("当前ExportPathDTO为{},格式化ExportPathDTO失败，失败原因是{}", JSONObject.toJSONString(this), e);
        }
    }

    private void format() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<? extends ExportPathDTO> exportPathClazz = this.getClass();
        Field[] fieldArr = exportPathClazz.getDeclaredFields();
        for (Field field : fieldArr) {
            field.setAccessible(true);
            if (Objects.isNull(field.get(this))) {
                field.set(this, "");
            }
        }
    }

    public String getPathName() {
        return pathName;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public String getPathDesc() {
        return pathDesc;
    }

    public void setPathDesc(String pathDesc) {
        this.pathDesc = pathDesc;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
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
}