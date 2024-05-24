package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.dto;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.enums.FunTypes;
import com.ruijie.rcos.rcdc.rco.module.def.service.tree.TreeNodeVO;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/30 13:51
 *
 * @author linrenjian
 */
public class PermissionTreeDTO extends TreeNodeVO implements Comparable<PermissionTreeDTO> {

    @ApiModelProperty(value = "允许删除")
    private boolean allowDelete;

    @ApiModelProperty(value = "是否默认")
    private boolean enableDefault;

    @ApiModelProperty(value = "是否禁用")
    private boolean disabled;

    @ApiModelProperty(value = "标签")
    private JSONObject tags;

    @ApiModelProperty(value = "权限码")
    private String permissionCode;

    public JSONObject getTags() {
        return tags;
    }

    public void setTags(JSONObject tags) {
        this.tags = tags;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        this.allowDelete = allowDelete;
    }

    public boolean isEnableDefault() {
        return enableDefault;
    }

    public void setEnableDefault(boolean enableDefault) {
        this.enableDefault = enableDefault;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    /**
     * 获取排序
     *
     * @return Integer
     */
    public Integer getOrder() {
        if (getTags() == null || getTags().getString(FunTypes.ORDER) == null) {
            return -1;
        }
        return Integer.parseInt(getTags().getString(FunTypes.ORDER));
    }

    @Override
    public int compareTo(PermissionTreeDTO o2) {
        Assert.notNull(o2, "o2 can not be null");

        return compare(this, o2);
    }

    private int compare(PermissionTreeDTO o1, PermissionTreeDTO o2) {
        // 两个都没有排序 就平等
        if ((o1.getTags() == null || o1.getTags().getString(FunTypes.ORDER) == null)
                && (o2.getTags() == null || o2.getTags().getString(FunTypes.ORDER) == null)) {
            return 0;
        }
        // 如果前面一个空 1 后面的大
        if (o1.getTags() == null || o1.getTags().getString(FunTypes.ORDER) == null) {
            return 1;
        }
        // 后面一个空 -1 前面的大
        if (o2.getTags() == null || o2.getTags().getString(FunTypes.ORDER) == null) {
            return -1;
        }
        // 比较值
        int o1Intger = Integer.parseInt(o1.getTags().getString(FunTypes.ORDER));
        int o2Intger = Integer.parseInt(o2.getTags().getString(FunTypes.ORDER));
        // 如果后面的大
        if (o1Intger < o2Intger) {
            return -1;
        } else if (o1Intger > o2Intger) {
            // 如果 前面的大
            return 1;
        } else {
            // 相等
            return 0;
        }
    }
}


