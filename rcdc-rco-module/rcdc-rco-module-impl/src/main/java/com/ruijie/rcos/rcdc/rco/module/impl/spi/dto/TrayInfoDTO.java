package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/28
 *
 * @author chenjiehui
 */
public class TrayInfoDTO {

    private int code;

    private String message;

    private TrayInfoDTO.BodyMessage content;

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

    public BodyMessage getContent() {
        return content;
    }

    public void setContent(BodyMessage content) {
        this.content = content;
    }

    /**
     * 消息体
     */
    public static class BodyMessage {
        /**
         * 是否显示托盘
         * (true 显示托盘，默认true)
         */
        private String enable;

        /**
         *  悬浮信息类型
         */
        private Integer floatType;


        private List<JSONObject> trayList;

        public String getEnable() {
            return enable;
        }

        public void setEnable(String enable) {
            this.enable = enable;
        }

        public Integer getFloatType() {
            return floatType;
        }

        public void setFloatType(Integer floatType) {
            this.floatType = floatType;
        }

        public List<JSONObject> getTrayList() {
            return trayList;
        }

        public void setTrayList(List<JSONObject> trayList) {
            this.trayList = trayList;
        }
    }
}
