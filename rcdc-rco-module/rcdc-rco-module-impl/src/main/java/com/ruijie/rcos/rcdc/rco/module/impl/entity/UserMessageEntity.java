package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserMessageDTO;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 用户消息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/25
 *
 * @author Jarman
 */
@Entity
@Table(name = "t_rco_user_message")
public class UserMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    private String content;

    private Date createTime;

    @Version
    private int version;

    /**
     * entity转dto
     * 
     * @param userMessageEntity userMessageEntity
     * @return UserMessageDTO
     */
    public static UserMessageDTO convertFor(UserMessageEntity userMessageEntity) {
        Assert.notNull(userMessageEntity, "userMessageEntity cannot null");
        UserMessageDTO dto = new UserMessageDTO();
        dto.setId(userMessageEntity.getId());
        dto.setContent(userMessageEntity.getContent());
        dto.setTitle(userMessageEntity.getTitle());
        dto.setCreateTime(userMessageEntity.getCreateTime());
        return dto;
    }

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "UserMessageEntity [id=" + id + ", title=" + title + ", content=" + content + ", createTime=" + createTime + ", version=" + version
                + "]";
    }

}
