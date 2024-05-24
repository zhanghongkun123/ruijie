package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response;

import java.io.Serializable;
import java.util.List;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdDataTreeNodeDTO;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月09日
 *
 * @author zhanghongkun
 */
@ApiModel("域用户返回参数")
public class DomainConfigPageResponse implements Serializable {

    private static final long serialVersionUID = -790429335822048337L;

    @ApiModelProperty(value = "用户分页信息")
    private PageQueryResponse userPage;

    @ApiModelProperty(value = "组信息")
    private List<IacAdDataTreeNodeDTO> groupList;

    @ApiModelProperty(value = "已映射用户数量")
    private Integer checkedNum;



    public List<IacAdDataTreeNodeDTO> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<IacAdDataTreeNodeDTO> groupList) {
        this.groupList = groupList;
    }

    public PageQueryResponse getUserPage() {
        return userPage;
    }

    public void setUserPage(PageQueryResponse userPage) {
        this.userPage = userPage;
    }

    public Integer getCheckedNum() {
        return checkedNum;
    }

    public void setCheckedNum(Integer checkedNum) {
        this.checkedNum = checkedNum;
    }
}
