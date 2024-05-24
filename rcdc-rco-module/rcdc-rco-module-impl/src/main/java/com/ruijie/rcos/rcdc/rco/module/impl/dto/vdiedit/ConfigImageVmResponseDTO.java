package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import org.springframework.util.Assert;

/**
 * Description: 配置镜像编辑虚机响应结果
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/6 10:20
 *
 * @author zhangyichi
 */
public class ConfigImageVmResponseDTO {

    private static final int SUCCESS_CODE = 0;

    private static final int FAIL_CODE = -1;

    private Integer code;

    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 构建成功消息
     * @return 成功消息
     */
    public static ConfigImageVmResponseDTO buildSuccessDTO() {
        ConfigImageVmResponseDTO configImageVmResponseDTO = new ConfigImageVmResponseDTO();
        configImageVmResponseDTO.setCode(SUCCESS_CODE);
        return configImageVmResponseDTO;
    }

    /**
     * 构建失败消息
     * @param message 失败异常国际化信息
     * @return 失败消息
     */
    public static ConfigImageVmResponseDTO buildFailDTO(String message) {
        Assert.notNull(message, "message cannot be null!");

        ConfigImageVmResponseDTO configImageVmResponseDTO = new ConfigImageVmResponseDTO();
        configImageVmResponseDTO.setCode(FAIL_CODE);
        configImageVmResponseDTO.setMessage(message);
        return configImageVmResponseDTO;
    }
}
