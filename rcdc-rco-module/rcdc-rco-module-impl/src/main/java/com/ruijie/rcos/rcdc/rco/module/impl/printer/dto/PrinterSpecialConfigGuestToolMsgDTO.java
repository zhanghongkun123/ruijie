package com.ruijie.rcos.rcdc.rco.module.impl.printer.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: 打印机特殊配置消息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/22
 *
 * @author chenjiehui
 */
public class PrinterSpecialConfigGuestToolMsgDTO {
    private int code;

    private String message;

    private PrinterSpecialConfigGuestToolMsgDTO.BodyMessage content;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PrinterSpecialConfigGuestToolMsgDTO.BodyMessage getContent() {
        return content;
    }

    public void setContent(PrinterSpecialConfigGuestToolMsgDTO.BodyMessage content) {
        this.content = content;
    }

    /**
     * 消息体
     */
    public static class BodyMessage {
        /**
         * 流水号
         */
        @JSONField(name = "serial")
        private Integer configSerial;

        /**
         * 特殊配置文件的基本信息
         */
        @JSONField(name = "spec")
        private PrinterSpecialConfigBaseInfoDTO baseInfoDTO;


        public PrinterSpecialConfigBaseInfoDTO getBaseInfoDTO() {
            return baseInfoDTO;
        }

        public void setBaseInfoDTO(PrinterSpecialConfigBaseInfoDTO baseInfoDTO) {
            this.baseInfoDTO = baseInfoDTO;
        }

        public Integer getConfigSerial() {
            return configSerial;
        }

        public void setConfigSerial(Integer configSerial) {
            this.configSerial = configSerial;
        }
    }




}
