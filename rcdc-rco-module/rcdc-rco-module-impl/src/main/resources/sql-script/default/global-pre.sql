drop view IF EXISTS v_cbb_image_export;
drop view IF EXISTS v_rco_user_hardware_certification;
drop view IF EXISTS v_rco_user_otp_certification;

-- 软件白名单--
DROP VIEW IF EXISTS "v_rco_soft_related_soft_strategy";
DROP VIEW IF EXISTS "v_rco_software_strategy_count";
DROP VIEW IF EXISTS "v_rco_software_strategy_related_software";
DROP VIEW IF EXISTS "v_rco_software_group_count";

DROP VIEW IF EXISTS "v_cbb_desktop_search";
--由于v_cbb_desktop_info_stat、v_cbb_rca_host_desktop_detail依赖v_cbb_user_desktop_detail，所以需要先删除v_cbb_desktop_info_stat
drop view IF EXISTS v_cbb_desktop_info_stat;
drop view IF EXISTS v_cbb_rca_host_desktop_detail;
drop view IF EXISTS v_cbb_user_desktop_detail;
drop view IF EXISTS v_cbb_terminal_search;

-- 桌面池相关
DROP VIEW IF EXISTS v_rco_pool_desktop_info;
DROP VIEW IF EXISTS v_rco_user_desktop_pool;
-- v_rco_desktop_pool_detail依赖v_rco_statistic_desktop_count视图，所以要先删除
DROP VIEW IF EXISTS v_rco_desktop_pool_detail;

-- 删除桌面池报表相关视图
DROP VIEW IF EXISTS v_rco_statistic_desktop_count;

DROP VIEW IF EXISTS v_rco_user_profile_path_group_count;
DROP VIEW IF EXISTS v_rco_user_profile_strategy_count;
DROP VIEW IF EXISTS v_rco_user_profile_strategy_related;
DROP VIEW IF EXISTS v_rco_user_profile_child_path;

--磁盘池相关
DROP VIEW IF EXISTS v_rco_user_disk;
DROP VIEW IF EXISTS v_rco_user_disk_pool;

-- 删除AD域组相关视图
DROP VIEW IF EXISTS v_rco_ad_group;


drop view IF EXISTS v_rco_ad_group;

-- uam
-- 临时删除 3月13号转测后删除
DROP VIEW IF EXISTS v_rco_test_terminal_group_desktop_related;
DROP VIEW IF EXISTS v_rco_test_user_group_desktop_related;


DROP VIEW IF EXISTS v_rco_uam_delivery_group;
DROP VIEW IF EXISTS v_rco_uam_delivery_app;
DROP VIEW IF EXISTS v_rco_uam_delivery_object;
DROP VIEW IF EXISTS v_rco_uam_delivery_object_detail;
DROP VIEW IF EXISTS v_rco_uam_app_disk;
DROP VIEW IF EXISTS v_rco_uam_push_install_package;
DROP VIEW IF EXISTS v_rco_uam_app_test_desktop_detail;
DROP VIEW IF EXISTS v_rco_uam_app_test_application_detail;
DROP VIEW IF EXISTS v_rco_user_group_desktop_related;
DROP VIEW IF EXISTS v_rco_terminal_group_desktop_related;


DROP VIEW IF EXISTS v_rco_uam_delivery_detail;
DROP VIEW IF EXISTS v_rco_user_desktop;
DROP VIEW IF EXISTS v_rco_uam_delivery_group_object_spec;
DROP VIEW IF EXISTS v_rco_uam_app_test_desk_app_detail;
DROP VIEW IF EXISTS v_rco_uam_app_test;
DROP VIEW IF EXISTS v_cbb_desktop_image_related;

DROP VIEW IF EXISTS v_cbb_uam_app_version_relative_desk;
DROP VIEW IF EXISTS v_rco_distribute_task;
DROP VIEW IF EXISTS v_rco_uam_user_desktop;

-- 删除文件审批申请单视图
DROP VIEW IF EXISTS v_rco_audit_file_apply;
DROP VIEW IF EXISTS v_rco_audit_apply;
DROP VIEW IF EXISTS v_rco_audit_print_log_static;

-- 删除桌面GT状态视图
DROP VIEW IF EXISTS  v_cbb_user_desktop_guesttool;

-- 删除用户同步数据视图
DROP VIEW IF EXISTS v_rco_user_sync_data;

-- 删除用户组同步数据视图
DROP VIEW IF EXISTS v_rco_user_group_sync_data;
-- 删除用户软终端视图
DROP VIEW IF EXISTS  v_cbb_user_soft_terminal;
-- 删除用户终端桌面简单视图

DROP VIEW IF EXISTS  v_cbb_user_terminal_desk;

-- 删除用户信息报表视图
DROP VIEW IF EXISTS  v_cbb_user_login_record;

DROP VIEW IF EXISTS  v_rco_rccm_sync_user;

DROP VIEW IF EXISTS v_rco_terminal_with_image_info;

DROP VIEW IF EXISTS v_cbb_user_terminal;

DROP VIEW IF EXISTS v_rco_audit_apply;

DROP VIEW IF EXISTS v_rco_audit_apply_print_log;

DROP VIEW IF EXISTS v_rco_disk_pool_user_assignment;

DROP VIEW IF EXISTS v_rco_terminal_stat;

DROP VIEW IF EXISTS v_rco_desktop_pool_total_user;

DROP VIEW IF EXISTS v_rca_strategy_user;

DROP VIEW IF EXISTS v_rca_dynamic_user_info;

DROP VIEW IF EXISTS v_rco_user_search;

DROP VIEW IF EXISTS v_rco_abnormal_user_group;

DROP VIEW IF EXISTS v_rco_abnormal_group_user;

DROP VIEW IF EXISTS v_rco_upgradeable_terminal;

DROP VIEW IF EXISTS v_cbb_desktop_physical_server_desktop_info;

DROP VIEW IF EXISTS v_rco_desktop_detail;

DROP VIEW IF EXISTS v_rco_user_group;

DROP VIEW IF EXISTS v_rco_terminal;

DROP VIEW IF EXISTS v_rco_desktop_info_search;

DROP VIEW IF EXISTS v_rco_desktop_info_stat;

DROP VIEW IF EXISTS v_rco_desktop_search;

DROP VIEW IF EXISTS v_rco_terminal_search;

DROP VIEW IF EXISTS v_rco_host_user;

DROP VIEW IF EXISTS v_cbb_user;

DROP VIEW IF EXISTS v_rco_user;

DROP VIEW IF EXISTS v_rco_desktop_session;

DROP VIEW IF EXISTS v_rco_desktop_pool_computer;

DROP VIEW IF EXISTS v_rco_desk_snapshot;

DROP VIEW IF EXISTS v_rco_desk_user_relation;

