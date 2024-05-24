package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.OtherCertificationVO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 批量设置用户或用户组
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/22
 *
 * @author TD
 */
@ApiModel("批量应用用户或用户组策略请求体")
public class BatchUserOrGroupCertificationRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "用户ID集合",required = true)
    private UUID[] userIdArr;

    @NotNull
    @ApiModelProperty(value = "用户组ID集合",required = true)
    private UUID[] groupIdArr;

    @NotNull
    @ApiModelProperty(value = "用户所在组ID集合",required = true)
    private UUID[] userSortGroupIdArr;

    @NotNull
    @ApiModelProperty(value = "过滤用户ID集合",required = true)
    private UUID[] filterUserIdArr;

    /**
     * 主要认证策略
     */
    @NotNull
    @ApiModelProperty(value = "主要认证策略")
    private PrimaryCertificationVO primaryCertificationVO;

    @Nullable
    @ApiModelProperty(value = "辅助认证策略")
    private AssistCertificationVO assistCertificationVO;

    @Nullable
    @ApiModelProperty(value = "其它认证")
    private OtherCertificationVO otherCertificationVO;

    @Nullable
    private Long accountExpireDate;

    @Nullable
    @Range(min = "0", max = "1000")
    private Integer invalidTime;

    public UUID[] getUserIdArr() {
        return userIdArr;
    }

    public void setUserIdArr(UUID[] userIdArr) {
        this.userIdArr = userIdArr;
    }

    public UUID[] getGroupIdArr() {
        return groupIdArr;
    }

    public void setGroupIdArr(UUID[] groupIdArr) {
        this.groupIdArr = groupIdArr;
    }

    public PrimaryCertificationVO getPrimaryCertificationVO() {
        return primaryCertificationVO;
    }

    public void setPrimaryCertificationVO(PrimaryCertificationVO primaryCertificationVO) {
        this.primaryCertificationVO = primaryCertificationVO;
    }

    @Nullable
    public AssistCertificationVO getAssistCertificationVO() {
        return assistCertificationVO;
    }

    public void setAssistCertificationVO(@Nullable AssistCertificationVO assistCertificationVO) {
        this.assistCertificationVO = assistCertificationVO;
    }

    @Nullable
    public OtherCertificationVO getOtherCertificationVO() {
        return otherCertificationVO;
    }

    public void setOtherCertificationVO(@Nullable OtherCertificationVO otherCertificationVO) {
        this.otherCertificationVO = otherCertificationVO;
    }

    public UUID[] getUserSortGroupIdArr() {
        return userSortGroupIdArr;
    }

    public void setUserSortGroupIdArr(UUID[] userSortGroupIdArr) {
        this.userSortGroupIdArr = userSortGroupIdArr;
    }

    public UUID[] getFilterUserIdArr() {
        return filterUserIdArr;
    }

    public void setFilterUserIdArr(UUID[] filterUserIdArr) {
        this.filterUserIdArr = filterUserIdArr;
    }

    @Nullable
    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(@Nullable Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }
}
