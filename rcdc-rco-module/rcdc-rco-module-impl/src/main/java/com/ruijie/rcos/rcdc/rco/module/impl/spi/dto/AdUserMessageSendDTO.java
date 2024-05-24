package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

/**
 *
 * Description: 用户消息传输对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年6月18日
 *
 * @author zouqi
 */
public class AdUserMessageSendDTO {

    private int code;

    private String message;

    private Body content;

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

    public Body getContent() {
        return content;
    }

    public void setContent(Body content) {
        this.content = content;
    }

    /**
     * Description: Body
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年6月18日
     *
     * @author zouqi
     */
    public static class Body {
        // ad域用户权限
        private String adUserAuthority;

        public String getAdUserAuthority() {
            return adUserAuthority;
        }

        public void setAdUserAuthority(String adUserAuthority) {
            this.adUserAuthority = adUserAuthority;
        }

    }

}
