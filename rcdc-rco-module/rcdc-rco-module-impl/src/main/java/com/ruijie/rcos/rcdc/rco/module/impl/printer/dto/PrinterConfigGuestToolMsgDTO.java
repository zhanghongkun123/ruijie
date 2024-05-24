package com.ruijie.rcos.rcdc.rco.module.impl.printer.dto;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: 打印机配置消息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/22
 *
 * @author chenjiehui
 */
public class PrinterConfigGuestToolMsgDTO {
    private int code;

    private String message;

    private PrinterConfigGuestToolMsgDTO.BodyMessage content;

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

    public PrinterConfigGuestToolMsgDTO.BodyMessage getContent() {
        return content;
    }

    public void setContent(PrinterConfigGuestToolMsgDTO.BodyMessage content) {
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
         * 配置名称
         */
        @JSONField(name = "name")
        private String configName;

        /**
         * 配置名称数组
         */
        @JSONField(name = "array_name")
        private List<String> configNameList;

        /**
         * 打印机配置详情
         */
        @JSONField(name = "detail")
        private String configDetail;

        /**
         * 配置文件 md5
         */
        @JSONField(name = "md5")
        private String configMd5;

        /**
         * 打印机端口
         */
        @JSONField(name = "model")
        private String printerModel;

        /**
         * 打印机连接模式
         */
        @JSONField(name = "types")
        private List<String> printerConnectTypeList;

        /**
         * 配置支持的操作系统
         */
        @JSONField(name = "os")
        private String supportOs;

        /**
         * 是否覆盖
         */
        @JSONField(name = "cover")
        private Integer cover;

        /**
         * 配置文件的基本信息
         */
        @JSONField(name = "base_info")
        private PrinterConfigBaseInfoDTO baseInfoDTO;


        public Integer getConfigSerial() {
            return configSerial;
        }

        public void setConfigSerial(Integer configSerial) {
            this.configSerial = configSerial;
        }

        public String getConfigDetail() {
            return configDetail;
        }

        public void setConfigDetail(String configDetail) {
            this.configDetail = configDetail;
        }

        public String getConfigMd5() {
            return configMd5;
        }

        public void setConfigMd5(String configMd5) {
            this.configMd5 = configMd5;
        }

        public PrinterConfigBaseInfoDTO getBaseInfoDTO() {
            return baseInfoDTO;
        }

        public void setBaseInfoDTO(PrinterConfigBaseInfoDTO baseInfoDTO) {
            this.baseInfoDTO = baseInfoDTO;
        }

        public String getPrinterModel() {
            return printerModel;
        }

        public void setPrinterModel(String printerModel) {
            this.printerModel = printerModel;
        }


        public String getSupportOs() {
            return supportOs;
        }

        public void setSupportOs(String supportOs) {
            this.supportOs = supportOs;
        }

        public String getConfigName() {
            return configName;
        }

        public void setConfigName(String configName) {
            this.configName = configName;
        }

        public Integer getCover() {
            return cover;
        }

        public void setCover(Integer cover) {
            this.cover = cover;
        }

        public List<String> getConfigNameList() {
            return configNameList;
        }

        public void setConfigNameList(List<String> configNameList) {
            this.configNameList = configNameList;
        }

        public List<String> getPrinterConnectTypeList() {
            return printerConnectTypeList;
        }

        public void setPrinterConnectTypeList(List<String> printerConnectTypeList) {
            this.printerConnectTypeList = printerConnectTypeList;
        }
    }




}
