package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;
import java.util.UUID;

/**
 *
 * Description: 用户消息传输对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月5日
 *
 * @author nt
 */
public class UserMessageSendDTO {

    private Integer code;

    private String message;

    private Content content;

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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Description: content
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年1月5日
     *
     * @author nt
     */
    public static class Content {
        private UUID id;

        private String title;

        private String content;

        @JSONField(format = "yyyy-MM-dd HH:mm:ss")
        private Date time;

        private UUID multiSessionId;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public UUID getMultiSessionId() {
            return multiSessionId;
        }

        public void setMultiSessionId(UUID multiSessionId) {
            this.multiSessionId = multiSessionId;
        }

        @Override
        public String toString() {
            return "Content{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", content='" + content + '\'' +
                    ", time=" + time +
                    ", multiSessionId=" + multiSessionId +
                    '}';
        }
    }
}
