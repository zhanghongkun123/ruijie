package com.ruijie.rcos.rcdc.rco.module.impl.disksnapshot;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年7月25日
 *
 * @author lyb
 */
public interface DiskSnapshotBusinessKey {

    String RCDC_RCO_DISK_SNAPSHOT_NAME_GENERATE_BURST = "23200324";

    String RCDC_RCO_DISK_SNAPSHOT_NUKNOWN_ERROR = "rcdc_rco_disk_snapshot_unknown_error";

    String RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_SUCCESS_SYSTEM_LOG = "rcdc_rco_quartz_disk_create_snapshot_success_system_log";

    String RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_FAIL_SYSTEM_LOG = "rcdc_rco_quartz_disk_create_snapshot_fail_system_log";

    String RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_BY_DISKTOP_STATE_UNSUPPORT = "rcdc_rco_quartz_disk_create_snapshot_by_disktop_state_unsupport";

    String RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT = "rcdc_rco_quartz_disk_create_snapshot";

    String RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_DISK_NOT_EXIST = "rcdc_rco_quartz_disk_create_snapshot_disk_not_exist";

    String RCDC_RCO_QUARTZ_DISK_CREATE_SNAPSHOT_DISK_POOL_AND_DISK_ID_BOTH_BE_NULL =
            "23200326";

    String RCDC_RCO_DISK_SNAPSHOT_NOT_CREATE = "23200325";

}
