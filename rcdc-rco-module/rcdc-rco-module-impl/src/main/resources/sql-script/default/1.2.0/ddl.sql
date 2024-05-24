CREATE TABLE t_rco_cluster_server_trend (
id uuid NOT NULL,
cpu_used_rate varchar(16),
memory_used_rate varchar(16),
storage_read_bandwidth varchar(16),
storage_write_bandwidth varchar(16),
create_time timestamp(6),
version int4,
CONSTRAINT t_rco_cluster_server_trend_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_cluster_server_trend.cpu_used_rate IS 'CPU使用率';
COMMENT ON COLUMN t_rco_cluster_server_trend.memory_used_rate IS '内存使用率';
COMMENT ON COLUMN t_rco_cluster_server_trend.storage_read_bandwidth IS '存储读带宽';
COMMENT ON COLUMN t_rco_cluster_server_trend.storage_write_bandwidth IS '存储写带宽';
COMMENT ON COLUMN t_rco_cluster_server_trend.create_time IS '创建时间';

/* 告警记录日统计表 */
DROP TABLE IF EXISTS t_rco_alarm_count_day;
CREATE TABLE t_rco_alarm_count_day (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
statistic_time date NOT NULL,
alarm_count int4 NOT NULL,
alarm_type varchar(32) COLLATE "default" NOT NULL,
CONSTRAINT t_rco_alarm_count_day_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_alarm_count_day.id IS '主键id';
COMMENT ON COLUMN t_rco_alarm_count_day.version IS '版本号';
COMMENT ON COLUMN t_rco_alarm_count_day.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_alarm_count_day.statistic_time IS '数据统计期限';
COMMENT ON COLUMN t_rco_alarm_count_day.alarm_count IS '告警数';
COMMENT ON COLUMN t_rco_alarm_count_day.alarm_type IS '告警类型';

/* 机柜表 */
DROP TABLE IF EXISTS t_rco_cabinet;
CREATE TABLE t_rco_cabinet (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
name varchar(128) COLLATE "default" NOT NULL,
description varchar(128) COLLATE "default",
server_num int4,
CONSTRAINT t_rco_cabinet_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_cabinet.id IS '主键id';
COMMENT ON COLUMN t_rco_cabinet.version IS '版本号';
COMMENT ON COLUMN t_rco_cabinet.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_cabinet.name IS '机柜名';
COMMENT ON COLUMN t_rco_cabinet.description IS '描述';
COMMENT ON COLUMN t_rco_cabinet.server_num IS '机柜上的服务器数量';

/* 机柜表索引 */
CREATE UNIQUE INDEX t_rco_cabinet_name_unique_index ON t_rco_cabinet (name);

/* 机柜服务器关联表 */
DROP TABLE IF EXISTS t_rco_cabinet_server_mapping;
CREATE TABLE t_rco_cabinet_server_mapping (
id uuid NOT NULL,
version int4 NOT NULL,
cabinet_id uuid NOT NULL,
server_id uuid NOT NULL,
cabinet_location_begin int2 NOT NULL,
cabinet_location_end int2 NOT NULL,
CONSTRAINT t_rco_cabinet_server_mapping_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_cabinet_server_mapping.id IS '主键id';
COMMENT ON COLUMN t_rco_cabinet_server_mapping.version IS '版本号';
COMMENT ON COLUMN t_rco_cabinet_server_mapping.cabinet_id IS '机柜id';
COMMENT ON COLUMN t_rco_cabinet_server_mapping.server_id IS '服务器id';
COMMENT ON COLUMN t_rco_cabinet_server_mapping.cabinet_location_begin IS '服务器在机柜的起始位置';
COMMENT ON COLUMN t_rco_cabinet_server_mapping.cabinet_location_end IS '服务器在机柜的结束位置';

/* 配置表 */
DROP TABLE IF EXISTS t_rco_common_config;
CREATE TABLE t_rco_common_config (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
config_key varchar(64) COLLATE "default" NOT NULL,
config_value varchar(64) COLLATE "default" NOT NULL,
CONSTRAINT t_rco_common_config_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_common_config.id IS '主键id';
COMMENT ON COLUMN t_rco_common_config.version IS '版本号';
COMMENT ON COLUMN t_rco_common_config.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_common_config.config_key IS '配置项名称';
COMMENT ON COLUMN t_rco_common_config.config_value IS '配置项值';

/* 云桌面资源日统计表 */
DROP TABLE IF EXISTS t_rco_desktop_resource_usage_day;
CREATE TABLE t_rco_desktop_resource_usage_day (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
desktop_id uuid NOT NULL,
statistic_time date NOT NULL,
cpu_usage float4 NOT NULL,
memory_usage float4 NOT NULL,
disk_usage float4 NOT NULL,
CONSTRAINT t_rco_desktop_resource_usage_day_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_desktop_resource_usage_day.id IS '主键id';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.version IS '版本号';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.desktop_id IS '云桌面id';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.statistic_time IS '统计时间';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.cpu_usage IS 'cpu使用率';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.memory_usage IS '内存使用率';
COMMENT ON COLUMN t_rco_desktop_resource_usage_day.disk_usage IS '磁盘使用率';

/* 云桌面资源月统计表 */
DROP TABLE IF EXISTS t_rco_desktop_resource_usage_month;
CREATE TABLE t_rco_desktop_resource_usage_month (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
desktop_id uuid NOT NULL,
statistic_time date NOT NULL,
cpu_usage float4 NOT NULL,
memory_usage float4 NOT NULL,
disk_usage float4 NOT NULL,
CONSTRAINT t_rco_desktop_resource_usage_month_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_desktop_resource_usage_month.id IS '主键id';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.version IS '版本号';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.desktop_id IS '云桌面id';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.statistic_time IS '统计时间';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.cpu_usage IS 'cpu使用率';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.memory_usage IS '内存使用率';
COMMENT ON COLUMN t_rco_desktop_resource_usage_month.disk_usage IS '磁盘使用率';

/* 云桌面开机数日统计表 */
DROP TABLE IF EXISTS t_rco_desktop_start_count_day;
CREATE TABLE t_rco_desktop_start_count_day (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
statistic_time date NOT NULL,
start_count int4 NOT NULL,
CONSTRAINT t_rco_desktop_start_count_day_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_desktop_start_count_day.id IS '主键id';
COMMENT ON COLUMN t_rco_desktop_start_count_day.version IS '版本号';
COMMENT ON COLUMN t_rco_desktop_start_count_day.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_desktop_start_count_day.statistic_time IS '统计时间';
COMMENT ON COLUMN t_rco_desktop_start_count_day.start_count IS '开机数';

/* 服务器资源利用率日表 */
DROP TABLE IF EXISTS t_rco_server_resource_usage_day;
CREATE TABLE t_rco_server_resource_usage_day (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
server_id uuid NOT NULL,
statistic_time date NOT NULL,
cpu_usage float4 NOT NULL,
memory_usage float4 NOT NULL,
disk_usage float4 NOT NULL,
CONSTRAINT t_rco_server_resource_usage_day_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_server_resource_usage_day.id IS '主键id';
COMMENT ON COLUMN t_rco_server_resource_usage_day.version IS '版本号';
COMMENT ON COLUMN t_rco_server_resource_usage_day.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_server_resource_usage_day.server_id IS '服务器id';
COMMENT ON COLUMN t_rco_server_resource_usage_day.statistic_time IS '统计时间';
COMMENT ON COLUMN t_rco_server_resource_usage_day.cpu_usage IS 'cpu使用率';
COMMENT ON COLUMN t_rco_server_resource_usage_day.memory_usage IS '内存使用率';
COMMENT ON COLUMN t_rco_server_resource_usage_day.disk_usage IS '磁盘使用率';

/* 服务器资源利用率小时表 */
DROP TABLE IF EXISTS t_rco_server_resource_usage_hour;
CREATE TABLE t_rco_server_resource_usage_hour (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
server_id uuid NOT NULL,
statistic_time timestamp(6) NOT NULL,
cpu_usage float4 NOT NULL,
memory_usage float4 NOT NULL,
disk_usage float4 NOT NULL,
CONSTRAINT t_rco_server_resource_usage_hour_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_server_resource_usage_hour.id IS '主键id';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.version IS '版本号';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.server_id IS '服务器id';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.statistic_time IS '统计时间';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.cpu_usage IS 'cpu使用率';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.memory_usage IS '内存使用率';
COMMENT ON COLUMN t_rco_server_resource_usage_hour.disk_usage IS '磁盘使用率';

/* 服务器资源利用率历史表 */
DROP TABLE IF EXISTS t_rco_server_resource_usage_history;
CREATE TABLE t_rco_server_resource_usage_history (
id uuid NOT NULL,
version int4 NOT NULL,
create_time timestamp(6) NOT NULL,
server_id uuid NOT NULL,
collect_time timestamp(6) NOT NULL,
cpu_usage float4 NOT NULL,
memory_usage float4 NOT NULL,
disk_usage float4 NOT NULL,
CONSTRAINT t_rco_server_resource_usage_history_pkey PRIMARY KEY (id)
);

COMMENT ON COLUMN t_rco_server_resource_usage_history.id IS '主键id';
COMMENT ON COLUMN t_rco_server_resource_usage_history.version IS '版本号';
COMMENT ON COLUMN t_rco_server_resource_usage_history.create_time IS '创建时间';
COMMENT ON COLUMN t_rco_server_resource_usage_history.server_id IS '服务器id';
COMMENT ON COLUMN t_rco_server_resource_usage_history.collect_time IS '统计时间';
COMMENT ON COLUMN t_rco_server_resource_usage_history.cpu_usage IS 'cpu使用率';
COMMENT ON COLUMN t_rco_server_resource_usage_history.memory_usage IS '内存使用率';
COMMENT ON COLUMN t_rco_server_resource_usage_history.disk_usage IS '磁盘使用率';
