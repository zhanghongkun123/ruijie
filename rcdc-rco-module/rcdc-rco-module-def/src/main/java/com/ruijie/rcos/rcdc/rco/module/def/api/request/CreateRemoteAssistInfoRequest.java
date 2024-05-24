package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RemoteAssistInfoDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/23 12:22
 *
 * @author ketb
 */
public class CreateRemoteAssistInfoRequest implements Request {

    @NotNull
    private UUID deskId;

    @Nullable
    private RemoteAssistInfoDTO infoDTO;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public RemoteAssistInfoDTO getInfoDTO() {
        return infoDTO;
    }

    public void setInfoDTO(RemoteAssistInfoDTO infoDTO) {
        this.infoDTO = infoDTO;
    }
}
