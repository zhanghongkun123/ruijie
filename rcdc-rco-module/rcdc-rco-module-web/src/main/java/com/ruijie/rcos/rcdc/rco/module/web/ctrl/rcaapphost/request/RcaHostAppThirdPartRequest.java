package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto.RcaHostSegmentIpDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto.RcaHostSingleIpDTO;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description: 第三方主机纳管request
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月27日
 *
 * @author liuwc
 */
public class RcaHostAppThirdPartRequest implements WebRequest {

    // 云主机名称
    @NotBlank
    @TextName
    private String name;

    // 会话类型
    @NotNull
    private RcaEnum.HostSessionType hostSessionType;

    // 最大会话数
    @NotNull
    @Range(min = "0", max = "200")
    private Integer maxSessionCount;

    // 应用主机IP-单IP添加
    @Nullable
    private List<RcaHostSingleIpDTO> ipList;

    // 应用主机IP-IP段添加
    @Nullable
    private List<RcaHostSegmentIpDTO> segmentIpDTOList;

    @Nullable
    @TextMedium
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RcaEnum.HostSessionType getHostSessionType() {
        return hostSessionType;
    }

    public void setHostSessionType(RcaEnum.HostSessionType hostSessionType) {
        this.hostSessionType = hostSessionType;
    }

    public Integer getMaxSessionCount() {
        return maxSessionCount;
    }

    public void setMaxSessionCount(Integer maxSessionCount) {
        this.maxSessionCount = maxSessionCount;
    }

    @Nullable
    public List<RcaHostSingleIpDTO> getIpList() {
        return ipList;
    }

    public void setIpList(@Nullable List<RcaHostSingleIpDTO> ipList) {
        this.ipList = ipList;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public List<RcaHostSegmentIpDTO> getSegmentIpDTOList() {
        return segmentIpDTOList;
    }

    public void setSegmentIpDTOList(@Nullable List<RcaHostSegmentIpDTO> segmentIpDTOList) {
        this.segmentIpDTOList = segmentIpDTOList;
    }
}
