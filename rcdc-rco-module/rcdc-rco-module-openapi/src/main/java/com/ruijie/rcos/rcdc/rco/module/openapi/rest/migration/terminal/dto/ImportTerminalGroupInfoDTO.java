package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;

import java.util.UUID;

/**
 * Description: 导入终端组转换类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.26
 *
 * @author linhj
 */
public class ImportTerminalGroupInfoDTO {

    /**
     * 终端组唯一标识
     */
    private UUID terminalGroupId;

    /**
     * 关联映射数据
     */
    private SystemBusinessMappingDTO systemBusinessMappingDTO;

    public ImportTerminalGroupInfoDTO(SystemBusinessMappingDTO systemBusinessMappingDTO, UUID terminalGroupId) {
        this.systemBusinessMappingDTO = systemBusinessMappingDTO;
        this.terminalGroupId = terminalGroupId;
    }

    public ImportTerminalGroupInfoDTO(SystemBusinessMappingDTO systemBusinessMappingDTO) {
        this.systemBusinessMappingDTO = systemBusinessMappingDTO;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public SystemBusinessMappingDTO getSystemBusinessMappingDTO() {
        return systemBusinessMappingDTO;
    }

    public void setSystemBusinessMappingDTO(SystemBusinessMappingDTO systemBusinessMappingDTO) {
        this.systemBusinessMappingDTO = systemBusinessMappingDTO;
    }
}
