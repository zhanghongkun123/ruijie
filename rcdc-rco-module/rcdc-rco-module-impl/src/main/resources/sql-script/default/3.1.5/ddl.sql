-- 增加管理员自定义功能列表列数据记录表更新时间
ALTER TABLE t_rco_admin_function_list_custom ADD COLUMN IF NOT EXISTS update_time timestamp(6) NOT NULL default CURRENT_TIMESTAMP;
COMMENT ON COLUMN t_rco_admin_function_list_custom.update_time IS '更新时间';

DROP INDEX IF EXISTS rcdc_t_rco_desksoft_use_record_index;
CREATE INDEX rcdc_t_rco_desksoft_use_record_index ON t_rco_desksoft_use_record (name);