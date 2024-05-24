
--用户桌面添加序号字段
ALTER TABLE t_rco_user_desktop ADD IF NOT EXISTS seq SERIAL;
COMMENT ON COLUMN t_rco_user_desktop.seq IS '序号';

-- 删除自助快照名字唯一索引
DROP INDEX IF EXISTS desk_snapshot_name_unique;

-- 用户组桌面信息配置表增加运行位置、存储位置
ALTER TABLE t_rco_user_group_desktop_config ADD COLUMN IF NOT EXISTS cluster_id uuid;
COMMENT ON COLUMN "public"."t_rco_user_group_desktop_config"."cluster_id" IS '桌面运行位置';
ALTER TABLE t_rco_user_group_desktop_config ADD COLUMN IF NOT EXISTS storage_pool_id uuid;
COMMENT ON COLUMN "public"."t_rco_user_group_desktop_config"."storage_pool_id" IS '桌面磁盘存储位置';

CREATE TABLE IF NOT EXISTS t_rco_desktop_online_situation_day_record (
  id uuid NOT NULL,
  day_key varchar(64)  NOT NULL,
  month_key varchar(64)  NOT NULL,
  create_time timestamp(6),
  update_time timestamp(6),
  version int4 NOT NULL DEFAULT 0,
  online_count int4,
  sleep_count int4,
  CONSTRAINT t_rco_desktop_online_situation_day_record_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.id IS 'id';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.day_key IS '天key';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.month_key IS '月key';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.create_time IS '创建时间';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.update_time IS '更新时间';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.version IS '版本号';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.online_count IS '最大在线数量';

COMMENT ON COLUMN t_rco_desktop_online_situation_day_record.sleep_count IS '最大休眠数量';

CREATE TABLE IF NOT EXISTS t_rco_desktop_online_situation_hour_record (
  id uuid NOT NULL,
  day_key varchar(64)  NOT NULL,
  hour_key varchar(64)  NOT NULL,
  create_time timestamp(6),
  version int4 NOT NULL DEFAULT 0,
  online_count int4,
  sleep_count int4,
  CONSTRAINT t_rco_desktop_online_situation_hour_record_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.id IS 'id';

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.day_key IS '天key';

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.hour_key IS '时key';

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.create_time IS '创建时间';

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.version IS '版本号';

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.online_count IS '最大在线数量';

COMMENT ON COLUMN t_rco_desktop_online_situation_hour_record.sleep_count IS '最大休眠数量';