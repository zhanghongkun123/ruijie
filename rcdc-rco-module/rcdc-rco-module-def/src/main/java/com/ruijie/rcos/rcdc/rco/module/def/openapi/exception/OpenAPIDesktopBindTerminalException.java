package com.ruijie.rcos.rcdc.rco.module.def.openapi.exception;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.10.18
 *
 * @author LinHJ
 */
public class OpenAPIDesktopBindTerminalException extends RuntimeException {

    private final String key;

    public OpenAPIDesktopBindTerminalException(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
