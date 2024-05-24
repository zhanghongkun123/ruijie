-- 是否需要处理云桌面策略数据权限
INSERT INTO t_rco_global_parameter(id, param_key, param_value, default_value, create_time, update_time, "version")
VALUES('11f12115-6633-4999-87e4-b28880a70011', 'need_compensate_strategy_data_permission', 'true', 'true', now(), now(), 0) ON CONFLICT(id) DO NOTHING;

-- 设置是否提示用户登录终端IP变更
INSERT INTO t_rco_global_parameter(id, param_key, param_value, default_value, create_time, update_time, version)
VALUES ('3ecd4b15-ce67-4385-8bd2-dbd20b7e0f35', 'enable_notify_login_terminal_change', 'false', 'false', now(), now(), 0) ON CONFLICT(param_key) DO NOTHING;
