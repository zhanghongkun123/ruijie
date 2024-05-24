-- 多会话用户登录桌面后绑定终端信息
ALTER TABLE t_rco_host_user ADD COLUMN IF NOT EXISTS terminal_id varchar(64) DEFAULT NULL;
COMMENT ON COLUMN t_rco_host_user.terminal_id IS '终端唯一标识';

