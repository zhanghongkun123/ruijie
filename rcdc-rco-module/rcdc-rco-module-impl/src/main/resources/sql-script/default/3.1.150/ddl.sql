/* 桌面池与用户关系表 */
-- 池与用户关系
CREATE TABLE IF NOT EXISTS t_rco_desktop_pool_user (
                                         id uuid NOT NULL,
                                         desktop_pool_id uuid NOT NULL,
                                         related_id uuid NOT NULL,
                                         related_type varchar(64) NOT NULL,
                                         create_time timestamp,
                                         version int4 DEFAULT 0 NOT NULL,
                                         CONSTRAINT t_rco_desktop_pool_user_pkey PRIMARY KEY (id)
);

COMMENT ON TABLE t_rco_desktop_pool_user IS '桌面池与用户关系表';
COMMENT ON COLUMN t_rco_desktop_pool_user.id IS '桌面池与用户关系表id，主键';
COMMENT ON COLUMN t_rco_desktop_pool_user.desktop_pool_id IS '桌面池id';
COMMENT ON COLUMN t_rco_desktop_pool_user.related_id IS '关联对象id';
COMMENT ON COLUMN t_rco_desktop_pool_user.related_type IS '关联对象类型：;USER：用户;USER_GROUP：用户组';
COMMENT ON COLUMN t_rco_desktop_pool_user.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_pool_user.version IS '版本号';

CREATE INDEX IF NOT EXISTS "t_rco_desktop_pool_user_related_id_index" ON "t_rco_desktop_pool_user" ("related_id");
CREATE INDEX IF NOT EXISTS "t_rco_desktop_pool_user_related_id_type_index" ON "t_rco_desktop_pool_user" ("related_id", "related_type");
CREATE INDEX IF NOT EXISTS "t_rco_desktop_pool_user_related_id_pool_id_index" ON "t_rco_desktop_pool_user" ("desktop_pool_id", "related_id");

CREATE TABLE IF NOT EXISTS t_rco_desktop_pool_config (
    id uuid NOT NULL,
    desktop_pool_id uuid NOT NULL,
    software_strategy_id uuid,
    user_profile_strategy_id uuid,
    create_time timestamp(6) NOT NULL,
    update_time timestamp(6),
    version int4 NOT NULL DEFAULT 0,
    CONSTRAINT t_rco_desktop_pool_config_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS "uniq_desktop_pool_id" ON "t_rco_desktop_pool_config" USING btree (
  "desktop_pool_id" "pg_catalog"."uuid_ops" ASC NULLS LAST
);

COMMENT ON COLUMN t_rco_desktop_pool_config.id IS '主键';
COMMENT ON COLUMN t_rco_desktop_pool_config.desktop_pool_id IS '桌面池id';
COMMENT ON COLUMN t_rco_desktop_pool_config.software_strategy_id IS '软件管控策略id';
COMMENT ON COLUMN t_rco_desktop_pool_config.user_profile_strategy_id IS 'UPM策略id';
COMMENT ON COLUMN t_rco_desktop_pool_config.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_pool_config.update_time IS '修改时间';
COMMENT ON COLUMN t_rco_desktop_pool_config.version IS '版本号';
COMMENT ON TABLE t_rco_desktop_pool_config IS '桌面池的业务层配置信息';