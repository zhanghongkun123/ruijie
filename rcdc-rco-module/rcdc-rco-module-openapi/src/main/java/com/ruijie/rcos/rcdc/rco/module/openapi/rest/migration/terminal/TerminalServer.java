package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.ImportTerminalRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.TerminalTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto.TerminalTaskResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@OpenAPI
@Path("/v1/terminal")
public interface TerminalServer {

    /**
     * 导入终端组
     *
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/importTerminalGroup")
    void importTerminalGroup(ImportTerminalGroupRequest request) throws BusinessException;

    /**
     * 导入终端
     *
     * @param request 请求
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/importTerminal")
    void importTerminal(ImportTerminalRequest request) throws BusinessException;

    /**
     * 终端任务查询
     *
     * @param request 请求
     * @throws BusinessException 业务异常
     * @return TerminalTaskResponse 查询结果
     */
    @POST
    @Path("/selectTerminalTask")
    TerminalTaskResponse selectTerminalTask(TerminalTaskRequest request) throws BusinessException;

}
