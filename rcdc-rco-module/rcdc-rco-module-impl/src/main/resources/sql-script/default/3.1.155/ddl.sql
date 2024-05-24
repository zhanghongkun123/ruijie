-- 桌面池会话列表增加会话id字段
ALTER TABLE t_rco_desktop_session_log ADD COLUMN IF NOT EXISTS "connection_id" int8;
COMMENT ON COLUMN t_rco_desktop_session_log.connection_id IS '会话id';