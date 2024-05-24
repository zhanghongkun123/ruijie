package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.imagetemplate;

/**
 * Description: CheckSpaceResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年06月04日
 *
 * @author fang
 */
public class CheckSpaceResponse {

    private boolean hasError;

    private String errorMessage;

    public CheckSpaceResponse() {
        this.hasError = false;
    }

    public CheckSpaceResponse(String errorMessage) {
        this.hasError = true;
        this.errorMessage = errorMessage;
    }

    public boolean getHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
