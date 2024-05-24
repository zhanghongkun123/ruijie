package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 通知用户是否存在个人盘
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/04
 *
 * @author zwf
 */
public class NotifyDiskMessageDTO {
    /**
     * 0:成功，其它失败
     */
    private Integer code;

    /**
     * 磁盘ID
     */
    private UUID diskId;

    /**
     * 磁盘名称
     */
    private String diskName;

    /**
     * 盘符
     */
    private String diskSn;

    /**
     * 符号
     */
    private String diskLetter;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 磁盘数量
     */
    private Integer diskNum;

    private DiskStatus diskState;

    private CbbDiskType diskType;

    /**
     * 生成失败对象
     *
     * @param errorMsg 失败消息
     * @return 失败对象
     */
    public static NotifyDiskMessageDTO fail(@Nullable String errorMsg) {
        NotifyDiskMessageDTO notifyDiskMessageDTO = new NotifyDiskMessageDTO();
        notifyDiskMessageDTO.setCode(DiskPoolConstants.FAIL);
        String errorMessage = StringUtils.hasText(errorMsg) ? errorMsg : DiskPoolConstants.DEFAULT_FAIL_MESSAGE_WITHOUT_DISK;
        notifyDiskMessageDTO.setErrorMsg(errorMessage);
        return notifyDiskMessageDTO;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getDiskSn() {
        return diskSn;
    }

    public void setDiskSn(String diskSn) {
        this.diskSn = diskSn;
    }

    public String getDiskLetter() {
        return diskLetter;
    }

    public void setDiskLetter(String diskLetter) {
        this.diskLetter = diskLetter;
    }

    public Integer getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(Integer diskNum) {
        this.diskNum = diskNum;
    }

    public DiskStatus getDiskState() {
        return diskState;
    }

    public void setDiskState(DiskStatus diskState) {
        this.diskState = diskState;
    }

    public CbbDiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(CbbDiskType diskType) {
        this.diskType = diskType;
    }

    @Override
    public String toString() {
        return "AttachDiskMessageDTO{" +
                "code=" + code +
                ", diskId=" + diskId +
                ", diskName='" + diskName + '\'' +
                ", diskSn='" + diskSn + '\'' +
                ", diskLetter='" + diskLetter + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ",diskState=" + diskState + '\'' +
                ",diskType" + diskType + '\'' +
                ", diskNum=" + diskNum +
                '}';
    }
}
