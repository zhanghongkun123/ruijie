package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 16:10
 *
 * @author zhangyichi
 */
public class PublishImageResponseDTO {

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
    public static PublishImageResponseDTO buildSuccessDTO() {
        PublishImageResponseDTO responseDTO = new PublishImageResponseDTO();
        responseDTO.setCode(SUCCESS_CODE);
        return responseDTO;
    }

    /**
     * 构建失败消息
     * @param message 失败异常国际化信息
     * @return 失败消息
     */
    public static PublishImageResponseDTO buildFailDTO(String message) {
        Assert.notNull(message, "message cannot be null!");

        PublishImageResponseDTO responseDTO = new PublishImageResponseDTO();
        responseDTO.setCode(FAIL_CODE);
        responseDTO.setMessage(message);
        return responseDTO;
    }
}
