/**rco用户配置子路径表*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_child_path
(
    id          uuid           NOT NULL,
    mode        varchar(16)    NOT NULL,
    type        varchar(16)    NOT NULL,
    user_profile_path_id uuid  NOT NULL,
    index       int4           NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_child_path_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_child_path.id IS '子路径类型ID';
COMMENT ON COLUMN t_rco_user_profile_child_path.mode IS '配置方式(同步/排除)';
COMMENT ON COLUMN t_rco_user_profile_child_path.type IS '类型(文件夹/文件/注册表)';
COMMENT ON COLUMN t_rco_user_profile_child_path.user_profile_path_id IS '所属路径配置ID';
COMMENT ON COLUMN t_rco_user_profile_child_path.index  IS '序号';

CREATE INDEX if NOT EXISTS t_rco_user_profile_child_path_id_index ON t_rco_user_profile_child_path USING btree (id);

/**子路径表*/
CREATE TABLE if NOT EXISTS t_rco_user_profile_path_detail
(
    id          uuid           NOT NULL,
    path        text           NOT NULL,
    user_profile_child_path_id uuid  NOT NULL,
    user_profile_path_id uuid  NOT NULL,
    index       int4           NOT NULL,
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_path_detail_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_path_detail.id IS '子路径ID';
COMMENT ON COLUMN t_rco_user_profile_path_detail.path IS '具体路径';
COMMENT ON COLUMN t_rco_user_profile_path_detail.user_profile_child_path_id IS '所属子路径类型ID';
COMMENT ON COLUMN t_rco_user_profile_path_detail.user_profile_path_id IS '所属路径配置ID';
COMMENT ON COLUMN t_rco_user_profile_path_detail.index  IS '序号';

CREATE INDEX if NOT EXISTS t_rco_user_profile_path_detail_id_index  ON t_rco_user_profile_path_detail USING btree (id);

-- 桌面池会话连接记录表索引
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_create_time ON t_rco_desktop_session_log USING btree (create_time);
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_user_name ON t_rco_desktop_session_log USING btree (user_name);
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_user_group_name ON t_rco_desktop_session_log USING btree (user_group_name);
CREATE INDEX IF NOT EXISTS idx_desktop_session_log_related_id ON t_rco_desktop_session_log USING btree (related_id);

-- 用户分配桌面池失败表所索引
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_fault_time ON t_rco_user_connect_desktop_fault_log USING btree (fault_time);
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_user_name ON t_rco_user_connect_desktop_fault_log USING btree (user_name);
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_user_group_name ON t_rco_user_connect_desktop_fault_log USING btree (user_group_name);
CREATE INDEX IF NOT EXISTS idx_connect_desktop_fault_log_related_id ON t_rco_user_connect_desktop_fault_log USING btree (related_id);