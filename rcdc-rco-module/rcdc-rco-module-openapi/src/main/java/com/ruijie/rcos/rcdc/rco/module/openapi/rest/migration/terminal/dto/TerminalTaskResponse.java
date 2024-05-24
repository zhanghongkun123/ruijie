package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
public class TerminalTaskResponse {

    /**
     * 业务流程状态
     */
    private TaskStatus code;

    /**
     * 终端上报状态
     */
    private int shineCode;

    public int getShineCode() {
        return shineCode;
    }

    public void setShineCode(int shineCode) {
        this.shineCode = shineCode;
    }

    public TerminalTaskResponse(TaskStatus code) {
        this.code = code;
    }

    public TerminalTaskResponse(TaskStatus code, int shineCode) {
        this.code = code;
        this.shineCode = shineCode;
    }

    public TaskStatus getCode() {
        return code;
    }

    public void setCode(TaskStatus code) {
        this.code = code;
    }

    /**
     * Description: 任务流程状态
     * Copyright: Copyright (c) 2022
     * Company: Ruijie Co., Ltd.
     * Create Time: 2022.04.02
     *
     * @author linhj
     */
    public enum TaskStatus {

        STATUS_UNKNOWN(0),      // 未导入
        STATUS_IMPORT_FAILED(1),// 导入失败
        STATUS_IMPORTED(2),     // 已导入未上线
        STATUS_CONNECTED(3),    // 已上线
        STATUS_FAILED(4),       // 终端上报失败
        STATUS_SUCCESS(5);      // 终端上报成功

        final int code;

        TaskStatus(int code) {
            this.code = code;
        }
    }
}
