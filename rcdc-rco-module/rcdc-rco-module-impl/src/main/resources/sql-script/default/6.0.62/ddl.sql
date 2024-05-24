-- 云桌面策略推荐表：添加会话类型字段（SINGLE、MULTIPLE）
ALTER TABLE t_rco_user_desk_strategy_recommend
ADD COLUMN IF NOT EXISTS session_type varchar(64) DEFAULT 'SINGLE';
COMMENT ON COLUMN t_rco_user_desk_strategy_recommend.session_type IS '会话类型：SINGLE、MULTIPLE';

ALTER TABLE t_rco_host_user
ADD COLUMN IF NOT EXISTS desktop_pool_id uuid DEFAULT NULL;
COMMENT ON COLUMN t_rco_host_user.desktop_pool_id IS '桌面池id';

ALTER TABLE public.t_rco_desk_user_session ALTER COLUMN terminal_id DROP NOT NULL;
