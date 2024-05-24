ALTER TABLE t_rco_open_api_task_info ADD COLUMN IF NOT EXISTS exception_key VARCHAR(64) DEFAULT '';
COMMENT ON COLUMN t_rco_open_api_task_info.exception_key IS '业务异常码';