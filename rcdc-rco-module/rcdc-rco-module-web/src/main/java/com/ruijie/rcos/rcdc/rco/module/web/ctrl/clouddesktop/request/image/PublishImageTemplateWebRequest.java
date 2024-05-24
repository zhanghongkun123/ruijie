package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageSyncMode;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/3/29 <br>
 *
 * @author yyz
 */
public class PublishImageTemplateWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    @Nullable
    private String changeLog;

    private ImageSyncMode syncMode;

    private UUID[] storagePoolIdArr;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getChangeLog() {
        return changeLog;
    }

    public void setChangeLog(String changeLog) {
        this.changeLog = changeLog;
    }

    public ImageSyncMode getSyncMode() {
        return syncMode;
    }

    public void setSyncMode(ImageSyncMode syncMode) {
        this.syncMode = syncMode;
    }

    public UUID[] getStoragePoolIdArr() {
        return storagePoolIdArr;
    }

    public void setStoragePoolIdArr(UUID[] storagePoolIdArr) {
        this.storagePoolIdArr = storagePoolIdArr;
    }
}
