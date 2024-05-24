package com.ruijie.rcos.rcdc.rco.module.def.mail.dto;

import com.ruijie.rcos.sk.base.annotation.Email;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

/**
 * Description: 邮件DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/07
 *
 * @author liyonghua1
 */
public class MailDTO {

    /**
     * 接收方邮箱
     */
    @Email
    private String toMailAccount;

    /**
     * 主题
     */
    @NotBlank
    private String subject;

    /**
     * 消息
     */
    @NotBlank
    private String message;

    /**
     * 图片路径
     */
    @Nullable
    private String imgPath;

    /**
     * 图片cid
     */
    @Nullable
    private String imgCid;

    /**
     * Instantiates a new Mail dto.
     */
    public MailDTO() {
    }

    /**
     * Instantiates a new Mail dto.
     *
     * @param toMailAccount the to mail account
     * @param subject       the subject
     * @param message       the message
     */
    public MailDTO(String toMailAccount, String subject, String message) {
        this.toMailAccount = toMailAccount;
        this.subject = subject;
        this.message = message;
    }

    /**
     * Gets to mail account.
     *
     * @return the to mail account
     */
    public String getToMailAccount() {
        return toMailAccount;
    }

    /**
     * Sets to mail account.
     *
     * @param toMailAccount the to mail account
     */
    public void setToMailAccount(String toMailAccount) {
        this.toMailAccount = toMailAccount;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(@Nullable String imgPath) {
        this.imgPath = imgPath;
    }

    @Nullable
    public String getImgCid() {
        return imgCid;
    }

    public void setImgCid(@Nullable String imgCid) {
        this.imgCid = imgCid;
    }
}
