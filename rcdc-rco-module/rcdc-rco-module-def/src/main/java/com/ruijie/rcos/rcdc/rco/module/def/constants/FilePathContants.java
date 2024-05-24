package com.ruijie.rcos.rcdc.rco.module.def.constants;


/**
 * Description: 文件静态常量
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/22 20:05
 *
 * @author linrenjian
 */
public interface FilePathContants {

    /**
     * 镜像 QCOW2文件
     */
    String QCOW2_PATH = "/external_share/qcow2";


    /**
     * 镜像 ISO文件
     */
    String ISO_PATH = "/external_share/iso";

    /**
     * 软件安装包文件
     */
    String SOFT_PATH = "/opt/samba/soft";

    String ISO_TYPE = ".iso";

    String QCOW2_TYPE = ".qcow2";

    String UWS_ISO_SAMBA_PATH = ISO_PATH + "/UWS-CDROM.ISO";

    String CMS_ISO_SAMBA_PATH = ISO_PATH + "/CM-CDROM.ISO";

    String CM_ISO_FIX = "file.busiz.dir.clouddesktop.iso";
}
