--- 增加一键安装配置
INSERT INTO t_rco_global_parameter (id, param_key, param_value, default_value, create_time, update_time,version)
VALUES ('361a158d-a935-43da-9764-ff061c8f98e6','app_one_click_install', '{"openOneInstall":true,"serverIp":"","proxyServerIp":"","proxyPort":""}', '', now(), now(), 0);