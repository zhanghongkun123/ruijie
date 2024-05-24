package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskApplySupportEnum;

/**
 * Description: 挂载磁盘通知消息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/20
 *
 * @author TD
 */
public class AttachDiskMessageDTO {

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
     * 磁盘支持的应用类型
     */
    private List<DiskApplySupportEnum> supportEnumList;

    /**
     * 磁盘数量
     */
    private Integer diskNum;

    /**
     *  磁盘所属用户名称
     */
    private String userName;

    /**
     *  磁盘所属用户类型
     */
    private IacUserTypeEnum userType;

    /**
     * 构建成功内容
     * 
     * @param userDiskDetailDTO 构建类
     * @param diskLetter 盘符
     * @param supportEnumList 支持的类型：UPM
     * @return AttachDiskMessageDTO
     */
    public static AttachDiskMessageDTO success(UserDiskDetailDTO userDiskDetailDTO, String diskLetter,
            @Nullable List<DiskApplySupportEnum> supportEnumList) {
        Assert.notNull(userDiskDetailDTO, "userDiskDetailDTO can not be null");
        Assert.hasText(diskLetter, "diskLetter can not be null");
        AttachDiskMessageDTO attachDiskMessageDTO = new AttachDiskMessageDTO();
        attachDiskMessageDTO.setCode(DiskPoolConstants.SUCCESS);
        BeanUtils.copyProperties(userDiskDetailDTO, attachDiskMessageDTO);
        attachDiskMessageDTO.setDiskLetter(Objects.equals(DiskPoolConstants.DEFAULT_LETTER, diskLetter) ? StringUtils.EMPTY : diskLetter);
        attachDiskMessageDTO.setSupportEnumList(supportEnumList);
        return attachDiskMessageDTO;
    }

    /**
     * 构建失败返回
     * 
     * @param errorMsg 异常提示信息
     * @return AttachDiskMessageDTO
     */
    public static AttachDiskMessageDTO fail(@Nullable String errorMsg) {
        AttachDiskMessageDTO attachDiskMessageDTO = new AttachDiskMessageDTO();
        attachDiskMessageDTO.setCode(DiskPoolConstants.FAIL);
        attachDiskMessageDTO.setErrorMsg(errorMsg);
        return attachDiskMessageDTO;
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

    public List<DiskApplySupportEnum> getSupportEnumList() {
        return supportEnumList;
    }

    public void setSupportEnumList(List<DiskApplySupportEnum> supportEnumList) {
        this.supportEnumList = supportEnumList;
    }

    public Integer getDiskNum() {
        return diskNum;
    }

    public void setDiskNum(Integer diskNum) {
        this.diskNum = diskNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
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
                ", supportEnumList=" + supportEnumList +
                ", diskNum=" + diskNum +
                '}';
    }
}
