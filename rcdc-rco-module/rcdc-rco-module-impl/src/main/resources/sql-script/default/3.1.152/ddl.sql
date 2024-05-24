/**rco用户配置路径表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_path
(
    id          uuid           NOT NULL,
    group_id    uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    description varchar(128),
    import_user_profile_path_type varchar(16),
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    creator_user_name varchar(64),
    is_default BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT t_rco_user_profile_path_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_path.id IS '用户配置路径ID';
COMMENT ON COLUMN t_rco_user_profile_path.group_id IS '用户配置路径组ID';
COMMENT ON COLUMN t_rco_user_profile_path.name IS '路径名称';
COMMENT ON COLUMN t_rco_user_profile_path.description IS '描述';
COMMENT ON COLUMN t_rco_user_profile_path.creator_user_name IS '创建人名称';
COMMENT ON COLUMN t_rco_user_profile_path.is_default IS '是否系统自带';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_path_id_index ON t_rco_user_profile_path USING btree (id);
CREATE INDEX IF NOT EXISTS t_rco_user_profile_path_group_id_index ON t_rco_user_profile_path USING btree (group_id);


/**rco用户配置路径组表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_path_group
(
    id          uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    description varchar(128),
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_path_group_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_path_group.id IS '用户配置路径组ID';
COMMENT ON COLUMN t_rco_user_profile_path_group.name IS '组名称';
COMMENT ON COLUMN t_rco_user_profile_path_group.description IS '组描述';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_group_id_index ON t_rco_user_profile_path_group USING btree (id);
CREATE unique INDEX IF NOT EXISTS t_rco_user_profile_path_group_name_index ON t_rco_user_profile_path_group USING btree (name);


/**rco用户配置策略表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_strategy
(
    id          uuid           NOT NULL,
    name        varchar(64)    NOT NULL,
    storage_type varchar(16)   NOT NULL,
    disk_path    text,
    disk_size    int2,
    description varchar(128),
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    creator_user_name varchar(64),
    CONSTRAINT t_rco_user_profile_strategy_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_strategy.id IS '用户配置策略ID';
COMMENT ON COLUMN t_rco_user_profile_strategy.name IS '策略名称';
COMMENT ON COLUMN t_rco_user_profile_strategy.storage_type IS '存储位置(本地/UNC路径)';
COMMENT ON COLUMN t_rco_user_profile_strategy.disk_path IS '磁盘路径';
COMMENT ON COLUMN t_rco_user_profile_strategy.disk_size IS '磁盘容量 单位:GB';
COMMENT ON COLUMN t_rco_user_profile_strategy.description IS '描述';
COMMENT ON COLUMN t_rco_user_profile_strategy.creator_user_name IS '创建人名称';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_id_index ON t_rco_user_profile_strategy USING btree (id);


/**rco用户配置策略与路径关联表*/
CREATE TABLE IF NOT EXISTS t_rco_user_profile_strategy_related
(
    id          uuid           NOT NULL,
    strategy_id uuid           NOT NULL,
    related_id  uuid           NOT NULL,
    related_type varchar(32)   NOT NULL,
    update_time timestamp(6),
    create_time timestamp(6),
    version     int4 DEFAULT 0 NOT NULL,
    CONSTRAINT t_rco_user_profile_strategy_related_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_user_profile_strategy_related.id IS '用户配置策略与路径关联ID';
COMMENT ON COLUMN t_rco_user_profile_strategy_related.strategy_id IS '用户配置策略ID';
COMMENT ON COLUMN t_rco_user_profile_strategy_related.related_id IS '关联对象ID';
COMMENT ON COLUMN t_rco_user_profile_strategy_related.related_type IS '关联对象类型（GROUP组、PATH路径）';

CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_related_id_index ON t_rco_user_profile_strategy_related USING btree (id);
CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_related_strategy_id_index ON t_rco_user_profile_strategy_related USING btree (strategy_id);
CREATE INDEX IF NOT EXISTS t_rco_user_profile_strategy_related_related_union_index ON t_rco_user_profile_strategy_related USING btree (related_id,related_type);

/**rco云桌面信息与策略关联*/
ALTER TABLE t_rco_desk_info ADD column if not exists user_profile_strategy_id uuid;
ALTER TABLE t_rco_desk_info ADD column if not exists update_time timestamp(6);
ALTER TABLE t_rco_desk_info ADD column if not exists create_time timestamp(6);

/**rco用户桌面信息配置表增加用户配置策略ID*/
ALTER TABLE t_rco_user_desktop_config ADD column if not exists user_profile_strategy_id uuid;
/**rco用户组桌面信息配置表增加用户配置策略ID*/
ALTER TABLE t_rco_user_group_desktop_config ADD column if not exists user_profile_strategy_id uuid;
/**rco访客桌面配置表增加用户配置策略ID*/
ALTER TABLE t_rco_user_desktop_visitor_config ADD column if not exists user_profile_strategy_id uuid;
/**rco终端组桌面信息配置表增加用户配置策略ID*/
alter table t_rco_user_terminal_group_desk_config ADD column if not exists user_profile_strategy_id uuid;
