
update t_rco_user_desk_strategy_recommend set name = 'UOS或Kylin的VDI普通桌面' where id = '916a79ae-1936-40e5-9cc8-a1ad674c0368';
update t_rco_user_desk_strategy_recommend set name = 'UOS或Kylin的VDI高性能桌面' where id = '1cef3c08-d4f6-4e61-94d7-4e5bc969b0c3';
update t_rco_user_desk_strategy_recommend set name = 'UOS的IDV桌面' where id = 'bedb7ed7-7754-4ce4-8baf-585a23dcff79';
INSERT INTO t_rco_user_desk_strategy_recommend ("id", "create_time", "name", "pattern", "cpu", "memory", "system_size", "personal_size", "is_allow_internet", "is_open_usb_read_only", "is_show", "version", "clip_board_mode", "is_open_double_screen", "cloud_desk_type", "is_allow_local_disk", "is_open_desktop_redirect","enable_disk_mapping","enable_disk_mapping_writeable","enable_lan_auto_detection")
VALUES ('46475c28-bbda-4ddf-bb2f-20766df57248', now(), 'UOS或Kylin的TCI桌面', 'PERSONAL', NULL, NULL, 40, NULL, 'f', 'f', 't', 0, NULL, NULL, 'VOI', 't', 'f', 'f', 'f', 'f') ON CONFLICT(id) DO NOTHING;

-- 桌面密码显示配置标识位
INSERT INTO t_rco_global_parameter (id, param_key, param_value, default_value, create_time, update_time,version)
VALUES ('f0dedf46-5a50-02cc-962f-7c91fe426594','has_init_desk_root_pwd_config', 'false', 'false', now(), now(), 0) ON CONFLICT (param_key) DO NOTHING;