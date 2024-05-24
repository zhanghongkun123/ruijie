package com.ruijie.rcos.rcdc.rco.module.impl.hotupdate.constants;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/27 14:22
 *
 * @author coderLee23
 */
public interface HotUpdateConstant {

    /**
     * 终端组件脚本路径
     */
    String DATA_WEB_TERMINAL_PATH = "/data/web/terminal";

    String TERMINAL_LINUX_X86_SH = DATA_WEB_TERMINAL_PATH + "/vdb_terminal_linux_x86.sh";

    String TERMINAL_LINUX_ARM_SH = DATA_WEB_TERMINAL_PATH + "/vdb_terminal_linux_arm.sh";

    String TERMINAL_ANDROID_ARM_SH = DATA_WEB_TERMINAL_PATH + "/vdb_terminal_android_arm.sh";

    List<String> TERMINAL_COMPONENT_SH_LIST = Arrays.asList(TERMINAL_LINUX_X86_SH, TERMINAL_LINUX_ARM_SH, TERMINAL_ANDROID_ARM_SH);

    List<String> TERMINAL_COMPONENT_LIST = Arrays.asList("linux_x86", "linux_arm", "android_arm");

    String TERMINAL_COMPONENT_PACKAGE_RESET_FAIL = "fail";

    String TERMINAL_COMPONENT_PACKAGE_INIT_STATUS_ANDROID_ARM = "terminal_component_package_init_status_android_arm";

    /**
     * linux
     */
    String TERMINAL_COMPONENT_PACKAGE_INIT_STATUS_LINUX_ARM = "terminal_component_package_init_status_linux_arm";

    /**
     * linux x86
     */
    String TERMINAL_COMPONENT_PACKAGE_INIT_STATUS_LINUX_X86 = "terminal_component_package_init_status_linux_x86";

    String INNER_DRIVER_UPDATE_FLAG = "/data/vm_images/isos/windows/x86-64/idv_driver_update_flag";

}
