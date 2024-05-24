package com.ruijie.rcos.rcdc.rco.module.impl.remoteassist.dto;



/**
 * Description: 远程协助运行状态
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/16
 *
 * @author chenjiehui
 */
public class RemoteAssistStatusDTO {

    private Integer code;

    private String message;

    private RemoteAssistStatusDTO.Content content;

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

    public RemoteAssistStatusDTO.Content getContent() {
        return content;
    }

    public void setContent(RemoteAssistStatusDTO.Content content) {
        this.content = content;
    }


    /**
     * 内容
     */
    public static class Content {
        /**
         * 远程协助状态
         */
        private Integer connectStatus;

        public Integer getConnectStatus() {
            return connectStatus;
        }

        public void setConnectStatus(Integer connectStatus) {
            this.connectStatus = connectStatus;
        }
    }
}
