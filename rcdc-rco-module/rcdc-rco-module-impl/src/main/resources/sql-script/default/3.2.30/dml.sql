update t_rco_terminal_work_mode_mapping set platform = 'IDV' where working_mode like 'IDV%' and platform is null;
update t_rco_terminal_work_mode_mapping set platform = 'VOI' where working_mode like 'VOI%' and platform is null;
update t_rco_terminal_work_mode_mapping set platform = 'VDI' where working_mode = 'VDI' and platform is null;

-- 插入评测功能策略
INSERT INTO t_rco_global_parameter("id", "param_key", "param_value", "default_value", "create_time", "update_time", "version")
VALUES ('39f3460f-6126-48b5-8f57-0acb63dcf9bb', 'virtual_application_state', 'false', 'false', now(), now(), 0);