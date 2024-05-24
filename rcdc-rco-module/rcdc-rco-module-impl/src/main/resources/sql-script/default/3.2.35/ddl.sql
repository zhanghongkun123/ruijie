-- 添加t_rco_cloud_desk_app_config 表新增字段
ALTER TABLE t_rco_cloud_desk_app_config ADD COLUMN IF NOT EXISTS iso_version text;
comment on column t_rco_cloud_desk_app_config.iso_version is 'iso版本号';

