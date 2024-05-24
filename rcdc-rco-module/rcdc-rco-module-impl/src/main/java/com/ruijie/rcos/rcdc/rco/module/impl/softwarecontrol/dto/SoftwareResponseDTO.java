package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.dto;


import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: SoftwareResponseDTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/28
 *
 * @author chenl
 */
public class SoftwareResponseDTO {

    /**
     * 软件名称
     */
    private String name;

    /**
     * 厂商数字签名
     */
    private String digitalSign;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 进程名
     */
    private String processName;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * 自定义md5值
     */
    private String fileCustomMd5;

    /**
     *
     * 获取白名单响应数组
     * @param tempList 子分组
     * @return SoftwareResponseDTO
     */
    public static SoftwareResponseDTO[] getWhiteSoftwareResponseDTOArr(List<SoftwareDTO> tempList) {
        Assert.notNull(tempList, "tempList not null");
        SoftwareResponseDTO[] softwareResponseDTOArr = new SoftwareResponseDTO[tempList.size()];
        for (int index = 0; index < tempList.size(); index++) {
            softwareResponseDTOArr[index] = convertFromEntity(tempList.get(index));
        }
        return softwareResponseDTOArr;
    }

    /**
     *
     * 获取黑名单
     * @param tempList 子分组
     * @return SoftwareResponseDTO[]
     */
    public static SoftwareResponseDTO[] getBlackSoftwareResponseDTOArr(List<SoftwareDTO> tempList) {
        Assert.notNull(tempList, "tempList not null");
        SoftwareResponseDTO[] softwareResponseDTOArr = new SoftwareResponseDTO[tempList.size()];
        for (int index = 0; index < tempList.size(); index++) {
            softwareResponseDTOArr[index] = convertBlackFromEntity(tempList.get(index));
        }
        return softwareResponseDTOArr;
    }


    /**
     *
     *  按白名单转换
     * @param softwareEntity 实体
     * @return SoftwareResponseDTO
     */
    public static SoftwareResponseDTO convertFromEntity(SoftwareDTO softwareEntity) {
        Assert.notNull(softwareEntity, "softwareEntity not null");
        SoftwareResponseDTO softwareResponseDTO = new SoftwareResponseDTO();
        softwareResponseDTO.setName(softwareEntity.getName());
        //md5一定会下发
        softwareResponseDTO.setFileCustomMd5(softwareEntity.getFileCustomMd5());

        if (Boolean.TRUE.equals(softwareEntity.getDigitalSignFlag())) {
            softwareResponseDTO.setDigitalSign(softwareEntity.getDigitalSign());
        }
        if (Boolean.TRUE.equals(softwareEntity.getProductNameFlag())) {
            softwareResponseDTO.setProductName(softwareEntity.getProductName());
        }
        if (Boolean.TRUE.equals(softwareEntity.getProcessNameFlag())) {
            softwareResponseDTO.setProcessName(softwareEntity.getProcessName());
        }
        if (Boolean.TRUE.equals(softwareEntity.getOriginalFileNameFlag())) {
            softwareResponseDTO.setOriginalFileName(softwareEntity.getOriginalFileName());
        }
        return softwareResponseDTO;
    }

    /**
     * 按黑名单转换
     * @param softwareEntity 实体
     * @return SoftwareResponseDTO
     */
    public static SoftwareResponseDTO convertBlackFromEntity(SoftwareDTO softwareEntity) {
        Assert.notNull(softwareEntity, "softwareEntity not null");
        SoftwareResponseDTO softwareResponseDTO = new SoftwareResponseDTO();
        softwareResponseDTO.setName(softwareEntity.getName());

        if (Boolean.TRUE.equals(softwareEntity.getFileCustomMd5BlackFlag())) {
            softwareResponseDTO.setFileCustomMd5(softwareEntity.getFileCustomMd5());
        }
        if (Boolean.TRUE.equals(softwareEntity.getDigitalSignBlackFlag())) {
            softwareResponseDTO.setDigitalSign(softwareEntity.getDigitalSign());
        }
        if (Boolean.TRUE.equals(softwareEntity.getProductNameBlackFlag())) {
            softwareResponseDTO.setProductName(softwareEntity.getProductName());
        }
        if (Boolean.TRUE.equals(softwareEntity.getProcessNameBlackFlag())) {
            softwareResponseDTO.setProcessName(softwareEntity.getProcessName());
        }
        if (Boolean.TRUE.equals(softwareEntity.getOriginalFileNameBlackFlag())) {
            softwareResponseDTO.setOriginalFileName(softwareEntity.getOriginalFileName());
        }
        return softwareResponseDTO;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDigitalSign() {
        return digitalSign;
    }

    public void setDigitalSign(String digitalSign) {
        this.digitalSign = digitalSign;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileCustomMd5() {
        return fileCustomMd5;
    }

    public void setFileCustomMd5(String fileCustomMd5) {
        this.fileCustomMd5 = fileCustomMd5;
    }
}
