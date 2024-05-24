-- 是否需要创建用户别名
INSERT INTO t_rco_global_parameter(id, param_key, param_value, default_value, create_time, update_time, "version")
VALUES('47dd7fee-e5cc-43ac-b1be-932e5ee8f895', 'need_check_user_name_and_alarm', 'true', 'true', now(), now(), 0) ON CONFLICT(id) DO NOTHING;

-- 清理客户端日志时间间隔
INSERT INTO t_rco_global_parameter(id, param_key, param_value, default_value, create_time, update_time, "version")
VALUES('f57648c5-343e-48fc-965d-23f8262494d8', 'clear_client_op_log_interval', '6', '6', now(), now(), 0) ON CONFLICT(id) DO NOTHING;
