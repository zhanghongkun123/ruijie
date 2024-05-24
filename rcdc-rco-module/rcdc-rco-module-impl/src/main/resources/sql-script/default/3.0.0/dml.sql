INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('8183c590-82f6-48e8-9abb-75ec1451a0a6', 'view_help_page_state', 'true', 'true', now(), now(), 0);

INSERT INTO "t_rco_global_parameter"("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES ('58e7c03a-fb0c-4408-a34f-c0274406adcd', 'network_model', 'bridge', 'bridge', now(), now(), 0);

-- 打印机全局配置项
INSERT INTO t_rco_global_parameter (id,param_key, param_value, default_value, create_time, update_time,version)
VALUES ('ed0dad6e-6f75-46ca-8d93-0dd1c055bd9b','rco_printer_manage_enable', 'false', 'false', now(), now(),0);

-- IDV终端临时授权标记
INSERT INTO t_rco_global_parameter (id,param_key, param_value, default_value, create_time, update_time,version)
VALUES ('e072f519-c4a3-4c76-8e61-5ed43ceb38a0','idv_auto_trial_license', 'can_license', '', now(), now(), 0);

--给表t_rco_user_desk_strategy_recommend插入国产化IDV推荐策略
INSERT INTO t_rco_user_desk_strategy_recommend ("id", "version", "create_time", "name", "pattern", "cpu", "memory", "system_size", "personal_size", "is_allow_internet", "is_open_usb_read_only", "is_show", "clip_board_mode", "is_open_double_screen", "cloud_desk_type", "is_allow_local_disk", "is_open_desktop_redirect")
VALUES ('bedb7ed7-7754-4ce4-8baf-585a23dcff79', '0', now(), '国产化IDV桌面', 'PERSONAL', NULL, NULL, '40', NULL, 'f', 'f', 't', NULL, NULL, 'IDV', 't', 'f');

INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('ed85433a-9341-4194-829f-1fd337999994', 'server_model', 'init', 'init', now(), now(), 0);

INSERT INTO
t_rco_global_parameter ("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version") VALUES
('ed85433a-9341-4194-829f-1fd337999995', 'cms_component', 'init', 'init', now(), now(), 0);
