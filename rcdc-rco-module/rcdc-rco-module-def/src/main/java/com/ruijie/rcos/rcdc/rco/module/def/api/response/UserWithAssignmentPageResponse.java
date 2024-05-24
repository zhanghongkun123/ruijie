package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.util.Assert;

/**
 * Description: 用户列表返回的结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/22 14:12
 *
 * @author linke
 */
public class UserWithAssignmentPageResponse extends DefaultPageResponse<UserListDTO> {

    /**
     * 总用户数量
     */
    private Long totalUserNum;

    /**
     * 组内已分配用户数量
     */
    private Integer assignedUserNum;

    public UserWithAssignmentPageResponse() {
    }

    /**
     * 构造
     *
     * @param pageResponse    pageResponse
     */
    public UserWithAssignmentPageResponse(DefaultPageResponse<UserListDTO> pageResponse) {
        Assert.notNull(pageResponse, "pageResponse is null");
        this.setItemArr(pageResponse.getItemArr());
        this.setTotal(pageResponse.getTotal());
        this.totalUserNum = pageResponse.getTotal();
        this.assignedUserNum = 0;
    }

    public Long getTotalUserNum() {
        return totalUserNum;
    }

    public void setTotalUserNum(Long totalUserNum) {
        this.totalUserNum = totalUserNum;
    }

    public Integer getAssignedUserNum() {
        return assignedUserNum;
    }

    public void setAssignedUserNum(Integer assignedUserNum) {
        this.assignedUserNum = assignedUserNum;
    }
}
