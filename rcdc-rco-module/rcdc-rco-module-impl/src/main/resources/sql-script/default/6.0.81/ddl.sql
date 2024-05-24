-- 桌面会话记录添加terminal_id字段
ALTER TABLE t_rco_desktop_session_log ADD COLUMN IF NOT EXISTS terminal_id varchar(64) DEFAULT NULL;
COMMENT ON COLUMN t_rco_desktop_session_log.terminal_id IS '终端唯一标识';

ALTER TABLE t_rco_desktop_session_log ADD COLUMN IF NOT EXISTS desktop_session_type varchar(64) DEFAULT 'SINGLE';
COMMENT ON COLUMN t_rco_desktop_session_log.desktop_session_type IS '会话类型';

ALTER TABLE t_rco_desktop_session_log ADD COLUMN IF NOT EXISTS state varchar(64) DEFAULT 'FINISHED';
COMMENT ON COLUMN t_rco_desktop_session_log.state IS '状态：CONNECTING、FINISHED';