package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.rco.module.impl.enums.UserMessageStateEnum;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public class GuestToolMsgDTO {

    private Integer cmdId;

    private String deskId;

    private Content content;

    public Integer getCmdId() {
        return cmdId;
    }

    public void setCmdId(Integer cmdId) {
        this.cmdId = cmdId;
    }

    public String getDeskId() {
        return deskId;
    }

    public void setDeskId(String deskId) {
        this.deskId = deskId;
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
    public class Content {

        /** 消息id */
        private UUID id;

        private String username;

        /** 消息状态 */
        @Enumerated(EnumType.STRING)
        private UserMessageStateEnum status;

        private UUID multiSessionId;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public UserMessageStateEnum getStatus() {
            return status;
        }

        public void setStatus(UserMessageStateEnum status) {
            this.status = status;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public UUID getMultiSessionId() {
            return multiSessionId;
        }

        public void setMultiSessionId(UUID multiSessionId) {
            this.multiSessionId = multiSessionId;
        }
    }
}
