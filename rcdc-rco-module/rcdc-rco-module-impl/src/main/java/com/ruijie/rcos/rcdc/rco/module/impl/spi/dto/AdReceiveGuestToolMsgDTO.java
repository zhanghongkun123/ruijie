package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 * Description: 加域消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/19
 *
 * @author Jarman
 */
public class AdReceiveGuestToolMsgDTO {

    private Integer cmdId;

    private Integer portId;

    private Content content;

    public Integer getCmdId() {
        return cmdId;
    }

    public void setCmdId(Integer cmdId) {
        this.cmdId = cmdId;
    }

    public Integer getPortId() {
        return portId;
    }

    public void setPortId(Integer portId) {
        this.portId = portId;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Description: 业务消息体
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019/1/16
     *
     * @author Jarman
     */
    public static class Content {
        private int code;

        private String message;

        private BodyMessage content;

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
    }

    /**
     * 消息体
     */
    public static class BodyMessage {
        private String username;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
